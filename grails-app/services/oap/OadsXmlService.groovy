package oap

import gov.noaa.ncei.oads.xml.v_a0_2_2s.*
import gov.noaa.ncei.oads.xml.v_a0_2_2s.BaseVariableType.BaseVariableTypeBuilder
import gov.noaa.ncei.oads.xml.v_a0_2_2s.DicVariableType.DicVariableTypeBuilder
import gov.noaa.ncei.oads.xml.v_a0_2_2s.PersonType.PersonTypeBuilder
import gov.noaa.pmel.oads.util.TimeUtils
import gov.noaa.pmel.oads.xml.a0_2_2.OadsXmlReader
import gov.noaa.pmel.oads.xml.a0_2_2.OadsXmlWriter
import gov.noaa.pmel.oads.xml.a0_2_2.StandardizedVariable
import gov.noaa.pmel.tws.util.StringUtils
import grails.transaction.Transactional

import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

@Transactional
class OadsXmlService {

    private static boolean isEmpty(Collection<?> list) {
        return list == null || list.size() == 0
    }
//    private static boolean isEmpty(Element e) {
//        if ( e == null ) {
//            return true;
//        }
//        if ( e.getTextTrim().length() > 0 || e.hasAttributes()) {
//            return false;
//        }
//        boolean emptyChildren = true;
//        Iterator<Element> children = e.getChildren().iterator();
//        while ( emptyChildren && children.hasNext()) {
//            Element child = children.next();
//            emptyChildren = isEmpty(child);
//        }
//        return emptyChildren;
//    }

//    private def getCountry(String proposed) {
//        if ( !proposed ) { return null }
//        String countryThreeLetter = OracleController.getThreeLetter(proposed)
//        return countryThreeLetter ? countryThreeLetter : proposed;
//    }

    def createMetadataDocumentFromVersionedXml(InputStream inputStream) {
        log.info("Creating OadsMetadata from input stream")
        OadsMetadataDocumentType xmlMetadata = OadsXmlReader.read(inputStream)
        return buildDocumentFromMetadata(xmlMetadata)
    }
    def createMetadataDocumentFromVersionedXml(org.w3c.dom.Document xDoc, String version) {

        log.info("Creating OadsMetadata from " + xDoc + " version " + version)
        OadsMetadataDocumentType xmlMetadata = OadsXmlReader.read(xDoc)

        return buildDocumentFromMetadata(xmlMetadata)
    }

    def buildDocumentFromMetadata(OadsMetadataDocumentType xmlMetadata) {
        Document mdDoc = new Document()

        // Investigators
        for (PersonType person : xmlMetadata.getInvestigators()) {
            mdDoc.addToInvestigators(fillPersonDomain(person, new Investigator()))
        }
        // Data Submitter
        mdDoc.setDataSubmitter(fillPersonDomain(xmlMetadata.getDataSubmitter(), new DataSubmitter()))

        // Citation
        Citation citation = fillCitationDomain(xmlMetadata)
        mdDoc.setCitation(citation)

        // Time and Location Information
        TimeAndLocation timeAndLocation = fillTimeAndLocDomain(xmlMetadata)
        mdDoc.setTimeAndLocation(timeAndLocation)

        // Funding
        List<Funding> fundingList = new ArrayList<>()
        for (FundingSourceType funding : xmlMetadata.getFundingInfo()) {
            Funding f = new Funding()
            f.setAgencyName(funding.agency)
            f.setGrantTitle(funding.title)
            f.setGrantNumber(funding.identifier.value)
            fundingList.add(f)
        }
        mdDoc.setFunding(fundingList)

        List<Platform> platformList = new ArrayList<>()
        for (PlatformType p : xmlMetadata.platforms) {
            Platform platform = new Platform()
            platform.setName(p.name)
            platform.setOwner(p.owner)
            platform.setPlatformId(p.identifier.value)
            platform.setPlatformType(p.type)
            platform.setCountry(p.country)
            mdDoc.addToPlatforms(platform)
        }
        mdDoc.setDocType("oads")
        for (BaseVariableType baseVar : xmlMetadata.getVariables()) {
            if (baseVar instanceof Co2Socat) {
                mdDoc.setDocType("socat")
                Co2 co2 = fillVariableDomain(baseVar)
                mdDoc.addToCo2vars(co2)
            } else {
                GenericVariable genV = fillVariableDomain(baseVar)
                mdDoc.addVariable(genV)
            }
        }
        return mdDoc
    }

//    def translateSpreadsheet(InputStream inputStream) {            // TODO: should move this elsewhere
//        ByteArrayOutputStream baos = new ByteArrayOutputStream()
//        PoiReader2.ConvertExcelToOADS(inputStream, baos)
//        ByteArrayInputStream convertedIS = new ByteArrayInputStream(baos.toByteArray())
//        return createDocument(convertedIS)
//
//    }

    private Citation fillCitationDomain(OadsMetadataDocumentType metadata) {
        Citation citation = new Citation()
        citation.setTitle(metadata.getTitle())
        citation.setDatasetAbstract(metadata.getAbstract())
        citation.setUseLimitation(metadata.getUseLimitation())
//        citation.setDataUse(metadata.getDataUse())
        citation.setPurpose(metadata.getPurpose())
        if ( metadata.getResearchProjects() != null && metadata.getResearchProjects().size() > 0 ) {
            String semi = ""
            String researchProjects = ""
            for (String project : metadata.getResearchProjects()) {
                researchProjects = researchProjects + semi + project
                semi = "; "
            }
            citation.setResearchProjects(String.valueOf(researchProjects))
        }
        def expocode = ""
        for (String code : metadata.getExpocodes()) {
            expocode += code + " "
        }
        citation.setExpocode(expocode.trim())
        def cruiseId = ""
        for (TypedIdentifierType code : metadata.getCruiseIds()) {
            cruiseId += code.getType()+":"+code.getValue() + " "
        }
        citation.setCruiseId(cruiseId.trim())

        def section = ""
        for (String code : metadata.getSections()) {
            section += code + " "
        }
        if ( !StringUtils.emptyOrNull(section)) {
            citation.setSection(section)
        }
        def reference = ""
        for (String ref : metadata.getReferences()) {
            reference += ref + "\n"
        }
        citation.setScientificReferences(reference.trim())
        if ( metadata.getMethods()) {
            citation.setMethodsApplied(metadata.getMethods())
        }

        def authorList = ""
        def semi = ""
        for (String author : metadata.authors) {
            authorList += semi + author
            semi = "; "
        }
        citation.setCitationAuthorList(authorList.trim())

        citation.setSupplementalInformation(metadata.getSupplementalInfo())
        return citation
    }

    def nonNullString(Object obj) {
        if ( obj != null ) { return String.valueOf(obj); }
        return null;
    }
    def fillTimeAndLocDomain(OadsMetadataDocumentType metadata) {
        TimeAndLocation timeAndLocation = new TimeAndLocation()
        TemporalExtentsType tempExtents = metadata.temporalExtents
        if ( tempExtents != null ) {
            if (tempExtents.startDate != null )
                timeAndLocation.setStartDate(TimeUtils.formatUTCdate(tempExtents.startDate))
            if ( tempExtents.endDate != null )
                timeAndLocation.setEndDate(TimeUtils.formatUTCdate(tempExtents.endDate))
        }

        GeospatialExtentsType geospatialExtents = metadata.getSpatialExtents()
        if ( geospatialExtents ) {
            if ( geospatialExtents.isSetBounds()) {
                SpatialExtentsType spatialExtents = geospatialExtents.getBounds();
                timeAndLocation.setWestLon(nonNullString(spatialExtents.westernBounds))
                timeAndLocation.setEastLon(nonNullString(spatialExtents.easternBounds))
                timeAndLocation.setNorthLat(nonNullString(spatialExtents.northernBounds))
                timeAndLocation.setSouthLat(nonNullString(spatialExtents.southernBounds))
                timeAndLocation.setSpatialRef(metadata.getSpatialReference())
            } else if ( geospatialExtents.isSetLocation()) {
                SpatialLocationType location = geospatialExtents.getLocation();
                timeAndLocation.setWestLon(nonNullString(location.getLongitude()))
                timeAndLocation.setEastLon(nonNullString(location.getLongitude()))
                timeAndLocation.setNorthLat(nonNullString(location.getLatitude()))
                timeAndLocation.setSouthLat(nonNullString(location.getLatitude()))
                timeAndLocation.setSpatialRef(metadata.getSpatialReference())
            }
        }
        def sampleRegions = ""
        def comma = ""
        for (String region : metadata.sampleCollectionRegions) {
            sampleRegions += comma + region
            comma = ", "
        }
        timeAndLocation.setGeoNames(sampleRegions.trim())
        sampleRegions = ""
        comma = ""
        for (String region : metadata.organismCollectionRegions) {
            sampleRegions += comma + region
            comma = ", "
        }
        timeAndLocation.setOrganismLoc(sampleRegions)
        return timeAndLocation
    }

    private GenericVariable fillVariableDomain(DicVariableType dicVar) {
        return fillVariableDic(dicVar, new Dic())
    }

    private GenericVariable fillVariableDic(DicVariableType dicVar, GenericVariable dicType) {
        GenericVariable variable = fillVariableGeneric(dicVar, dicType)
        if (dicVar.poison) {
//            Element poisonName = poison.getChild("poisonName")
//            if ( ! isEmpty(poisonName) ) {
            variable.setPoison(dicVar.poison.name)
//            }
//            Element volume = poison.getChild("volume")
//            if ( ! isEmpty(volume) ) {
            variable.setPoisonVolume(dicVar.poison.volume)
//            }
//            Element poisonDescription = poison.getChild("correction")
//            if ( ! isEmpty(poisonDescription) ) {
            variable.setPoisonDescription(dicVar.poison.correction)
//            }
        }

        return variable
    }

    private GenericVariable fillVariableDomain(TaVariableType taVar) {
        GenericVariable variable = fillVariableDic(taVar, new Ta())

//        Element titrationType = variable.getChild("titrationType")
//        if ( ! isEmpty(titrationType) ) {
        variable.setTitrationType(taVar.titrationType)
//        }
//        Element cellType = variable.getChild("cellType")
//        if ( ! isEmpty(cellType) ) {
        variable.setCellType(taVar.cellType)
//        }
//        Element curveFitting = variable.getChild("curveFitting")
//        if ( ! isEmpty(curveFitting) ) {
        variable.setCurveFittingMethod(taVar.curveFitting)
//        }
        variable.setMagnitudeOfBlankCorrection(taVar.blankCorrection)

        return variable
    }

    private GenericVariable fillVariableDomain(PhVariableType phVar) {
        GenericVariable variable = fillVariableGeneric(phVar, new Ph())
        // 025 at what temperature was pH reported
        // <phReportTemperature>
        // TextBox pHtemperature;
//        Element phscale = variable.getChild("phscale")
//        if ( ! isEmpty(phscale) ) {
        variable.setPhScale(phVar.phScale)
//        }
//        if ( phVar.temperatureCorrection ) {
        variable.setTemperatureCorrectionMethod(phVar.temperatureCorrectionMethod)
        variable.setTemperatureMeasurement(phVar.getMeasurementTemperature())
//        }
//        Element phReportTemperature = variable.getChild("phReportTemperature")
//        if ( ! isEmpty(phReportTemperature) ) {
        variable.setPhTemperature(phVar.phReportTemperature)
//        }
        variable.setPhDyeTypeManuf(phVar.typeOfDye)

        return variable
    }

    private GenericVariable fillVariableDomain(Co2Socat co2socat) {
        GenericVariable variable = fillVariableCo2a(co2socat, new Co2())
        variable.totalPressureCalcMethod = co2socat.totalMeasurementPressure.method
        variable.uncertaintyOfTotalPressure = co2socat.totalMeasurementPressure.uncertainty
        return variable
    }

    private GenericVariable fillVariableDomain(Co2Autonomous co2aVar) {
        GenericVariable variable = fillVariableCo2a(co2aVar, new Pco2a())
    }

    private GenericVariable fillVariableCo2a(Co2Autonomous co2aVar, GenericVariable co2x) {
        GenericVariable variable = fillVariableCo2(co2aVar, co2x)
        // 030 Depth of seawater intake
        // <DepthSeawaterIntake>
        // TextBox intakeDepth;
//        Element DepthSeawaterIntake = variable.getChild("DepthSeawaterIntake")
//        if ( ! isEmpty(DepthSeawaterIntake) ) {
        variable.setIntakeDepth(co2aVar.depthSeawaterIntake)
//        }
//        Element locationSeawaterIntake = variable.getChild("locationSeawaterIntake")
//        if ( ! isEmpty(locationSeawaterIntake) ) {
        variable.setIntakeLocation(co2aVar.locationSeawaterIntake)
//        }

//        Element equilibrator = variable.getChild("equilibrator")
        if (co2aVar.equilibrator) {
//            Element type = equilibrator.getChild("type")
//            if ( ! isEmpty(type) ) {
            variable.setEquilibratorType(co2aVar.equilibrator.type)
//            }
//            Element volume = equilibrator.getChild("volume")
//            if ( ! isEmpty(volume) ) {
            variable.setEquilibratorVolume(co2aVar.equilibrator.volume)
//            }
//            Element vented = equilibrator.getChild("vented")
//            if ( ! isEmpty(vented) ) {
            variable.setVented(co2aVar.equilibrator.vented)
//            }
//            Element waterFlowRate = equilibrator.getChild("waterFlowRate")
//            if ( ! isEmpty(waterFlowRate) ) {
            variable.setFlowRate(co2aVar.equilibrator.waterFlowRate)
//            }
//            Element gasFlowRate = equilibrator.getChild("gasFlowRate")
//            if ( ! isEmpty(gasFlowRate) ) {
            variable.setGasFlowRate(co2aVar.equilibrator.gasFlowRate)
//            }
//            Element temperatureEquilibratorMethod = equilibrator.getChild("temperatureEquilibratorMethod")
//            if ( ! isEmpty(temperatureEquilibratorMethod) ) {
            variable.setEquilibratorTemperatureMeasureMethod(co2aVar.equilibrator.temperatureMeasurement.method)
//            }
//            Element pressureEquilibratorMethod = equilibrator.getChild("pressureEquilibratorMethod")
//            if ( ! isEmpty(pressureEquilibratorMethod) ) {
            variable.setEquilibratorPressureMeasureMethod(co2aVar.equilibrator.pressureMeasurement.method)
//            }

            // need null checks all down the line... (and above.)
            // eg if ( co2var.equilibrator && co2aVar.equilibrator.temperatureMeasurement && ... )
            // OR change back to nonNullHashMap..
            variable.setUncertaintyOfTemperature(co2aVar.equilibrator.temperatureMeasurement.uncertainty)
            variable.setTemperatureMeasurementCalibrationMethod(co2aVar.equilibrator.temperatureMeasurement.sensor.calibration)
            variable.setPressureMeasurementCalibrationMethod(co2aVar.equilibrator.pressureMeasurement.sensor.calibration)
            List<StandardGasType> stdGases = co2aVar.standardization.getStandardGas()
            if ( stdGases != null && !stdGases.isEmpty()) {
                StandardGasType stdGas = stdGases.get(0)
                variable.setTraceabilityOfStdGas(stdGas.traceabilityToWmoStandards)
            }
            variable.setpCo2CalcMethod(co2aVar.calculationMethodForPCO2)
            variable.setfCo2CalcMethod(co2aVar.calculationMethodForFCO2)

            // 031 Drying method for CO2 gas
            // <dryMethod>
            // TextBox dryingMethod;
//            Element dryMethod = variable.getChild("dryMethod")
//            if ( ! isEmpty(dryMethod) ) {
            variable.setDryingMethod(co2aVar.co2GasDryingMethod)
//            }
        }

        return variable
    }

    private GenericVariable fillVariableDomain(Co2Discrete co2dVar) {
        GenericVariable co2d = fillVariableCo2(co2dVar, new Pco2d())
//        Element storageMethod = variable.getChild("storageMethod")
//        if ( ! isEmpty(storageMethod) ) {
        co2d.setStorageMethod(co2dVar.storageMethod)
//        }
//        Element headspacevol = variable.getChild("headspacevol");
//        if ( ! isEmpty(headspacevol) ) {
        co2d.setHeadspaceVolume(co2dVar.headspaceVolume)
//        }
//        Element seawatervol = variable.getChild("seawatervol")
//        if ( ! isEmpty(seawatervol) ) {
        co2d.setSeawaterVolume(co2dVar.seawaterVolume)
        co2d.setTemperatureMeasurement(co2dVar.getMeasurementTemperature())
//        }

        return co2d
    }

    private GenericVariable fillVariableCo2(Co2Base co2Var, GenericVariable co2x) {
        co2x = fillVariableGeneric(co2Var, co2x)
//        Element gasDetector = variable.getChild("gasDetector")
        if (co2Var.gasDetector) {
//            Element manufacturer = gasDetector.getChild("manufacturer")
//            if (! isEmpty(manufacturer)  ) {
            co2x.setGasDetectorManufacture(co2Var.gasDetector.manufacturer)
//            }
//            Element model = gasDetector.getChild("model")
//            if ( ! isEmpty(model) ) {
            co2x.setGasDetectorModel(co2Var.gasDetector.model)
//            }
//            Element resolution = gasDetector.getChild("resolution")
//            if ( ! isEmpty(resolution) ) {
            co2x.setGasDectectorResolution(co2Var.gasDetector.resolution)
//            }
//            Element gasuncertainty = gasDetector.getChild("uncertainty")
//            if ( ! isEmpty(gasuncertainty) ) {
            co2x.setGasDectectorUncertainty(co2Var.gasDetector.uncertainty)
//            }
        }
//        Element waterVaporCorrection = variable.getChild("waterVaporCorrection")
//        if ( ! isEmpty(waterVaporCorrection) ) {
        co2x.setVaporCorrection(co2Var.waterVaporCorrection)
//        }
//        if ( co2Var.temperatureCorrection ) {
        co2x.setTemperatureCorrectionMethod(co2Var.temperatureCorrectionMethod)
//        }
//        Element co2ReportTemperature = variable.getChild("co2ReportTemperature")
//        if ( ! isEmpty(co2ReportTemperature) ) {
        co2x.setPco2Temperature(co2Var.co2ReportTemperature)
//        }

        return co2x
    }

    private GenericVariable fillVariableDomain(BiologicalVariable bioVar) {
        GenericVariable variable = fillVariableGeneric(bioVar, new Variable())
        // 026 Biological subject
        // <biologicalSubject>
        // TextBox biologicalSubject;
//        Element biologicalSubject = variable.getChild("biologicalSubject")
//        if ( ! isEmpty(biologicalSubject) ) {
        variable.setBiologicalSubject(bioVar.biologicalSubject)
//        }
//        Element duration = variable.getChild("duration")
//        if ( ! isEmpty(duration) ) {
        variable.setDuration(bioVar.duration)
//        }
//        Element lifeStage = variable.getChild("lifeStage")
//        if ( ! isEmpty(lifeStage) ) {
        variable.setLifeStage(bioVar.lifeStage)
//        }
//        Element speciesID = variable.getChild("speciesID")
//        if ( ! isEmpty(speciesID) ) {
        variable.setSpeciesIdCode(bioVar.speciesID)
//        }

        return variable
    }

    private GenericVariable fillVariableDomain(BaseVariableType variable) {
        return fillVariableGeneric(variable, new Variable())
    }
    /*

    // 001 Variable abbreviation in data files
    // <abbrev>
    @UiField
    TextBox abbreviation;

    // 002 Observation type
    // <observationType>
    @UiField
    TextBox observationType;

    // 003 Manipulation method
    // <manipulationMethod>
    @UiField
    TextBox manipulationMethod;

    // 004 In-situ observation / manipulation condition / response variable
    // <insitu>
    @UiField
    ButtonDropDown observationDetail;

    // 005 Variable unit
    // <unit>
    @UiField
    TextBox units;

    // 006 Measured or calculated
    // <measured>
    @UiField
    ButtonDropDown measured;

    // 007 Calculation method and parameters
    // <calcMethod>
    @UiField
    TextBox calculationMethod;

    // 008 Sampling instrument
    // <samplingInstrument>
    @UiField
    ButtonDropDown samplingInstrument;

    // 009 Analyzing instrument
    // <analyzingInstrument>
    @UiField
    ButtonDropDown analyzingInstrument;

    // 010 Detailed sampling and analyzing information
    // <detailedInfo>
    @UiField
    TextArea detailedInformation;

    // 011 Field replicate information
    // <replicate>
    @UiField
    TextBox fieldReplicate;

    // 012 Standardization technique description
    // <standardization><description>
    @UiField
    TextBox standardizationTechnique;

    // 013 Frequency of standardization
    // <standardization><frequency>
    @UiField
    TextBox freqencyOfStandardization;

    // 014 CRM manufacturer
    // <crm><manufacturer>
    @UiField
    TextBox crmManufacture;

    // 015 Batch Number
    // <crm><batch>
    @UiField
    TextBox batchNumber;

    // 016 Storage method
    // <storageMethod>
    @UiField
    TextBox storageMethod;

    // 017 Poison used to kill the sample
    // <poison><poisonName>
    @UiField
    TextBox poison;

    // 018 Poison volume
    @UiField
    TextBox poisonVolume;

    // 019 Poisoning correction description
    @UiField
    TextBox poisonDescription;

    // TODO standard gas uncertainty
    // 020 Uncertainty
    // <standardization><standardgas><uncertainty>
    @UiField
    TextBox uncertainty;

    // 021 Data quality flag description
    // <flag>
    @UiField
    TextBox qualityFlag;

    // 022 Researcher Name
    // <researcherName>
    @UiField
    TextBox researcherName;

    // 023 Researcher Institution
    // <researcherInstitution>
    @UiField
    TextBox researcherInstitution;

    // 024 at what temperature was pCO2 reported
    // <co2ReportTemperature>
    @UiField
    TextBox pCO2temperature;

    // 025 at what temperature was pH reported
    // <phReportTemperature>
    @UiField
    TextBox pHtemperature;

    // 026 Biological subject
    // <biologicalSubject>
    @UiField
    TextBox biologicalSubject;

    // 027 Cell type (open or closed)
    // <cellType>
    @UiField
    ButtonDropDown cellType;

    // 028 Concentrations of standard gas
    // <standardization><standardgas><concentration>
    @UiField
    TextBox gasConcentration;

    // 029 Curve fitting method
    // <curveFitting>
    @UiField
    TextBox curveFittingMethod;

    // 030 Depth of seawater intake
    // <DepthSeawaterIntake>
    @UiField
    TextBox intakeDepth;

    // 031 Drying method for CO2 gas
    // <dryMethod>
    @UiField
    TextBox dryingMethod;

    // 032 Duration (for settlement/colonization methods)
    // <duration>
    @UiField
    TextBox duration;

    // 033 Equilibrator type
    // <equilibrator><type>
    @UiField
    TextBox equilibratorType;

    // 034 Equilibrator volume (L)
    // <equilibrator><volume>
    @UiField
    TextBox equilibratorVolume;

    // 035 Full variable name
    // <fullname>
    @UiField
    TextBox fullVariableName;

    // 036 Headspace gas flow rate (L/min)
    // <gasFlowRate>
    @UiField
    TextBox gasFlowRate;

    // 037 Headspace volume (mL)
    // <headspacevol>
    @UiField
    TextBox headspaceVolume;

    // 038 How was pressure inside the equilibrator measured.
    // <equilibrator><pressureEquilibratorMethod>
    @UiField
    TextBox equilibratorPressureMeasureMethod;

    // 039 How was temperature inside the equilibrator measured .
    // <equilibrator><temperatureEquilibratorMethod>
    @UiField
    TextBox equilibratorTemperatureMeasureMethod;

    // 040 Life stage of the biological subject
    // <lifeStage>
    @UiField
    TextBox lifeStage;

    // 041 Location of seawater intake
    // <locationSeawaterIntake>
    @UiField
    TextBox intakeLocation;

    // 042 Magnitude of blank correction
    // <blank>
    @UiField
    TextBox magnitudeOfBlankCorrection;

    // 043 Manufacturer of standard gas
    // <standardization><standardgas><manufacture>
    @UiField
    TextBox standardGasManufacture;

    // 044 Manufacturer of the gas detector
    // <gasDetector> ???
    @UiField
    TextBox gasDetectorManufacture;

    // 045 Method reference (citation)
    // <methodReference>
    @UiField
    TextBox referenceMethod;

    // 046 Model of the gas detector
    @UiField
    TextBox gasDetectorModel;

    // 047 pH scale
    // <phscale>
    @UiField
    TextBox pHscale;

    // 048 pH values of the standards
    @UiField
    TextBox pHstandards;

    // 049 Resolution of the gas detector
    // <resolution>
    @UiField
    TextBox gasDectectorResolution;

    // 050 Seawater volume (mL)
    // <seawatervol>
    @UiField
    TextBox seawaterVolume;

    // 051 Species Identification code
    // <speciesID>
    @UiField
    TextBox speciesIdCode;

    // 052 Temperature correction method
    // <temperatureCorrectionMethod>
    @UiField
    TextBox temperatureCorrectionMethod;

    // 053 Temperature of measurement
    // <temperatureMeasure>
    @UiField
    TextBox temperatureMeasurement;

    // 054 Temperature of standardization
    // <temperatureStandardization>
    // <temperatureStd
    @UiField
    TextBox temperatureStandarization;

    // 055 Type of titration
    // <titrationType>
    @UiField
    TextBox titrationType;

    // 056 Uncertainties of standard gas
    // <standard
    @UiField
    TextBox standardGasUncertainties;

    // 057 Uncertainty of the gas detector
    @UiField
    TextBox gasDectectorUncertainty;

    // 058 Vented or not
    // <equilibrator><vented>
    @UiField
    TextBox vented;

    // 059 Water flow rate (L/min)
    // <equilabrator><waterFlowRate>
    @UiField
    TextBox flowRate;

    // 060 Water vapor correction method
    // <waterVaporCorrection>
    @UiField
    TextBox vaporCorrection;
         */

    private GenericVariable fillVariableGeneric(BaseVariableType source, GenericVariable v) {
//        def v
//        if ( variable.getChild("fullname") && variable.getChild("fullname").getTextTrim().toLowerCase().equals("dissolved inorganic carbon") ) {
//            v = new Dic()
//        } else if ( variable.getChild("fullname") && variable.getChild("fullname").getTextTrim().toLowerCase().equals("ph") ) {
//            v = new Ph()
//        } else if ( variable.getChild("fullname") && variable.getChild("fullname").getTextTrim().toLowerCase().equals("pco2 (fco2) autonomous") ) {
//            v = new Pco2a()
//        } else if ( variable.getChild("fullname") && variable.getChild("fullname").getTextTrim().toLowerCase().equals("pco2 (fco2) discrete")) {
//            v = new Pco2d()
//        } else if ( variable.getChild("fullname") && variable.getChild("fullname").getTextTrim().toLowerCase().equals("total alkalinity") ) {
//            v = new Ta()
//        } else {
//            v = new Variable()
//        }

//        Element fullname = variable.getChild("fullname")
//        if ( ! isEmpty(fullname) ) {
        v.setFullVariableName(source.getFullName())
//        }
//        Element abbrev = variable.getChild("abbrev")
//        if ( ! isEmpty(abbrev) ) {
        v.setAbbreviation(source.getDatasetVarName())
//        }
//        Element observationType = variable.getChild("observationType")
//        if ( ! isEmpty(observationType) ) {
        v.setObservationType(source.getObservationType())
//        }
//        Element insitu = variable.getChild("insitu")
//        if ( ! isEmpty(insitu) ) {
        v.setObservationDetail(source.getVariableType())
//        }
//        Element manipulationMethod = variable.getChild("manipulationMethod")
//        if ( ! isEmpty(manipulationMethod) ) {
        v.setManipulationMethod(source.getManipulationMethod())
//        }
//        Element unit = variable.getChild("unit")
//        if ( ! isEmpty(unit) ) {
        v.setUnits(source.getUnits())
//        }
//        Element measured = variable.getChild("measured")
//        if ( ! isEmpty(measured) ) {
        v.setMeasured(source.getMeasuredOrCalculated())
//            v.setMeasured(measured.getTextTrim())
//        }
//        Element calcMethod = variable.getChild("calcMethod")
        if (source.getCalculationMethod()) {
            v.setCalculationMethod(source.getCalculationMethod().description)
        }
//        Element samplingInstrument = variable.getChild("samplingInstrument")
//        if ( ! isEmpty(samplingInstrument) ) {
        v.setSamplingInstrument(source.getSamplingInstrument())
//        }
//        Element analyzingInstrument = variable.getChild("analyzingInstrument")
//        if ( ! isEmpty(analyzingInstrument) ) {
        v.setAnalyzingInstrument(source.getAnalyzingInstrument())
//        }
//        Element detailedInfo = variable.getChild("detailedInfo")
//        if ( ! isEmpty(detailedInfo) ) {
        String samplingInfo = source.getDetailedSamplingInfo()
        String analizingInfo = source.getDetailedAnalyzingInfo()
        String detailedInfo = samplingInfo ? samplingInfo + "\n" : ""
        detailedInfo = detailedInfo + analizingInfo ? analizingInfo : ""
        v.setDetailedInformation( detailedInfo )
//        }
//        Element replicate = variable.getChild("replicate")
//        if ( ! isEmpty(replicate) ) {
        v.setFieldReplicate(source.getFieldReplicateHandling())
//        }

        // TODO this is in two different parent elements in the example <standard> and <standardization>
//        Element standard = variable.getChild("standard")
//        if ( isEmpty( standard )) {
//            standard = variable.getChild("standardization")
//        }
        if ( source instanceof StandardizedVariable ) {
            StandardizedVariable stdVar = (StandardizedVariable)source
            if (stdVar.getStandardization()) {
                StandardizationType std = source.standardization
//                Element technique = standard.getChild("description")
    //            if ( ! isEmpty(technique) ) {
                v.setStandardizationTechnique(std.description)
    //            }
    //            Element frequency = standard.getChild("frequency")
    //            if ( ! isEmpty(frequency) ) {
                v.setFreqencyOfStandardization(std.frequency)
    //            }
                if ( source instanceof PhVariableType ) {
                    v.setPhStandards(std.phOfStandards)
                }
                v.setTemperatureStandarization(std.temperature)
    //            Element crm = standard.getChild("crm")
                if (std.crm) {
    //                Element manufacture = crm.getChild("manufacturer")
    //                if ( ! isEmpty(manufacture) ) {
                    v.setCrmManufacture(std.crm.manufacturer)
    //                }
    //                Element batch = crm.getChild("batch")
    //                if ( ! isEmpty(batch) ) {
                    v.setBatchNumber(std.crm.batch)
    //                }
                }
    //            Element stdGas = standard.getChild("standardgas")
                if (!isEmpty(std.standardGas)) {
    //                Element sgasMfc = stdGas.getChild("manufacturer")
    //                if ( !isEmpty( sgasMfc )) {
                    v.setStandardGasManufacture(std.standardGas.get(0).manufacturer)
    //                }
    //                Element sgasConc = stdGas.getChild("concentration")
    //                if ( !isEmpty( sgasConc )) {
                    v.setGasConcentration(std.standardGas.get(0).concentration)
    //                }
    //                Element sgasUnc = stdGas.getChild("uncertainty")
    //                if ( !isEmpty( sgasUnc )) {
                    v.setStandardGasUncertainties(std.standardGas.get(0).uncertainty)
    //                }
                }
            }
        }
//
//        Element uncertainty = variable.getChild("uncertainty")
//        if ( ! isEmpty(uncertainty) ) {
        v.setUncertainty(source.getUncertainty())
//        }
//        Element flag = variable.getChild("flag")
        if (source.getQcFlag()) {
            QcFlagInfoType qc = source.getQcFlag()
            v.setQualityFlag(qc.getScheme())
            v.setQualityControl(qc.getDescription())
            v.setAbbreviationQualityFlag(qc.getQcFlagVarName())
        }
//        Element methodReference = variable.getChild("methodReference")
//        if ( ! isEmpty(methodReference) ) {
        v.setReferenceMethod(source.methodReference)

        v.setSopChanges(source.getVariationsFromMethod())
//        }
//        Element researcherName = variable.getChild("researcherName")
        if (source.researcher) {
            v.setResearcherName(source.researcher.name)
            v.setResearcherInstitution(source.researcher.organization)
        }

        // TODO set the internal variable number

        return v
    }

    private Person fillPersonDomain(PersonType p, Person human) {
        if (p == null) {
            return null
        }
        PersonNameType name = p.name
        human.firstName = name?.first
        human.mi = name?.middle
        human.lastName = name?.last

        human.setInstitution(p.organization)

        PersonContactInfoType contactInfo = p.contactInfo
        AddressType address = contactInfo.address
        if (address) {
            List<OrderedStringElementType> addrs = address.deliveryPoint
            if (addrs.size() > 0) {
                human.setAddress1(addrs.get(0).value)
                if (addrs.size() > 1) {
                    human.setAddress2(addrs.get(1).value)
                }
            }
            human.setCity(address.city)
            human.setState(address.administrativeArea)
            human.setZip(address.postalCode)
            human.setCountry(address.country)
//            def proposedCountry = address.country
//            String countryName = OracleController.getCountryName(proposedCountry)
//            if ( countryName !=  null ) {
//                human.setCountry(countryName)
//            } else {
//                human.setCountry(proposedCountry)
//            }
        }
        human.setTelephone(contactInfo.phone)
        human.setEmail(contactInfo.email)
        def ids = p.identifier
        if (ids && !ids.isEmpty()) {
            TypedIdentifierType id = ids.get(0)
            human.setRid(id.value)
            human.setIdType(id.type)
        }
        return human
    }

    def createXml(Document doc) {
        return createOadsXml(doc)
    }

    def createOadsXml(Document doc) {
        OadsMetadataDocumentType mdDoc = buildMetadataDoc(doc)
        String xml = OadsXmlWriter.getXml(mdDoc)
        return xml
    }

    def transformDoc(Document doc, OutputStream outS) {
        try {
            OadsMetadataDocumentType mdDoc = buildMetadataDoc(doc)
            ByteArrayOutputStream baos = new ByteArrayOutputStream()
            OadsXmlWriter.outputXml(mdDoc, baos)
            ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray())
            doXslTransformation(bis, outS)
        } catch (Throwable t) {
            t.printStackTrace()
        }
    }

//    def getXslResourceFile(String path) {
//        File xslFile;
//        File f = new File(".")
//        String p = f.getAbsolutePath()
//        if ( p.indexOf("OAPMetadataEditor") > -1 ) {
//            String check = path;
//            def resource = this.class.getResource(path)
//            System.out.println(check + ":" + resource)
//            check = path.substring(1)
//            resource = this.class.getResource(check)
//            System.out.println(check + ":" + resource)
//            check = "src/main/webapp"+path
//            resource = this.class.getResource(check);
//            System.out.println(check + ":" + resource)
//            check = "/src/main/webapp"+path
//            resource = this.class.getResource(check);
//            System.out.println(check + ":" + resource)
//            check = "xslr" + path.substring(4)
//            resource = this.class.getResource(check);
//            System.out.println(check + ":" + resource)
//            check = "/" + check
//            resource = this.class.getResource(check);
//            System.out.println(check + ":" + resource)
//            xslFile = new File("src/main/webapp"+path)
//            System.out.println(xslFile.getAbsolutePath())
//        } else {
//            def resource = this.class.getResource(path)
//            System.out.println(resource)
//            xslFile = resource.getFile()
//        }
//        return xslFile
//    }

    def doXslTransformation(InputStream inXml, OutputStream outXfrm) {
        def resource = this.class.getResource("/xsl/a0.2.2/ocads_a0.2.2.xsl")
        String absFile = resource?.getFile()
        System.out.println("using XSL file " + absFile)
        absFile = URLDecoder.decode(absFile)
        File xslFile = new File(absFile)
        TransformerFactory xfrmFactory = TransformerFactory.newInstance()
        Transformer xfrm = xfrmFactory.newTransformer(new StreamSource(xslFile))
        xfrm.transform(new StreamSource(inXml), new StreamResult(outXfrm))
    }
    def buildMetadataDoc(Document doc) {
        OadsMetadataDocumentType.OadsMetadataDocumentTypeBuilder metadata = OadsMetadataDocumentType.builder()
//        metadata.version("a0.2.2")

        if ( doc.getInvestigators() ) {
            for (Person p : doc.getInvestigators()) {
                PersonType person = fillPerson(p)
                metadata.addInvestigator(person)
            }
        }

        metadata.dataSubmitter(fillPerson(doc.getDataSubmitter()))

        Citation citation = doc.getCitation()
        if ( citation ) {
            metadata.title(citation.getTitle())
            metadata._abstract(citation.getDatasetAbstract())
            metadata.useLimitation(citation.getUseLimitation())
//            metadata.dataUse(citation.getDataUse())
            metadata.purpose(citation.getPurpose())

            // XXX TODO: use single string, multiple strings, or ResearchProjectType ???
            if ( citation.getResearchProjects() ) {
                metadata.addResearchProject(citation.getResearchProjects())
            }

            String expocodeField = citation.getExpocode()
            if ( expocodeField ) {
                String[] expocodes = expocodeField.split("[, ;]")
                for (String expocode : expocodes) {
                    if ( ! expocode.trim().isEmpty()) {
                        metadata.addExpocode(expocode)
                    }
                }
            }
            String cruiseIdField = citation.getCruiseId()
            if ( cruiseIdField ) {
                String[] cruises = cruiseIdField.split("[, ;]")
                for (String cruiseId : cruises) {
                    if ( ! cruiseId.trim().isEmpty()) {
                        metadata.addCruiseId(TypedIdentifierType.builder().value(cruiseId).build())
                    }
                }
            }

            String sectionField = citation.getSection()
            if ( sectionField ) {
                String[] sections = sectionField.split("[, ;]")
                for (String section : sections) {
                    if ( ! section.trim().isEmpty()) {
                        metadata.addSection(section)
                    }
                }
            }

            // XXX TODO: use single string, multiple strings, or ScientificReferenceType ???
            if ( citation.getScientificReferences() ) {
                metadata.addReference(citation.getScientificReferences())
            }

            // TODO the XML does not match the spreadsheet
            String authorList = citation.getCitationAuthorList()
            if ( authorList ) {
                String delimiter = lookForDelimiter(authorList)
                if ( delimiter ) {
                    String[] authors = authorList.split(delimiter)
                    for (String author : authors) {
                        if ( author ) {
                            metadata.addAuthor(author.trim())
                        }
                    }
                } else {
                    metadata.addAuthor(authorList)
                }
            }

            metadata.supplementalInfo(citation.getSupplementalInformation())
        }

        TimeAndLocation timeAndLocation = doc.getTimeAndLocation()
        if ( timeAndLocation ) {
            metadata.spatialExtents(GeospatialExtentsType.builder()
                                        .bounds(SpatialExtentsType.builder()
                                            .easternBounds(timeAndLocation.getEastLon() != null ?
                                                    new BigDecimal(timeAndLocation.getEastLon()) : null )
                                            .northernBounds(timeAndLocation.getNorthLat() != null ?
                                                    new BigDecimal(timeAndLocation.getNorthLat()) : null )
                                            .southernBounds(timeAndLocation.getSouthLat() != null ?
                                                    new BigDecimal(timeAndLocation.getSouthLat()) : null )
                                            .westernBounds(timeAndLocation.getWestLon() != null ?
                                                    new BigDecimal(timeAndLocation.getWestLon()) : null )
                                            .build()).build()
            )
            metadata.temporalExtents(TemporalExtentsType.builder()
                        .startDate(TimeUtils.parseUTCdate(timeAndLocation.getStartDate()))
                        .endDate(TimeUtils.parseUTCdate(timeAndLocation.getEndDate()))
                        .build()
            )
            metadata.spatialReference(timeAndLocation.getSpatialRef())

            String regions = timeAndLocation.getGeoNames()
            if ( regions ) {
                for (String region : regions.split("[,;]") ) {
                    metadata.addSampleCollectionRegion(region)
                }
            }
            regions = timeAndLocation.getOrganismLoc()
            if ( regions ) {
                for (String region : regions.split("[,;]") ) {
                    metadata.addOrganismCollectionRegion(region)
                }
            }
        }

        List<Funding> fundingList = doc.getFunding()
        if ( fundingList ) {
            for (int i = 0; i < fundingList.size(); i++) {
                Funding funding = fundingList.get(i)
                if ( funding ) {
                    metadata.addFunding(FundingSourceType.builder()
                            .agency(funding.getAgencyName())
                            .title(funding.getGrantTitle())
                            .identifier(TypedIdentifierType.builder().value(funding.getGrantNumber()).build())
                            .build()
                    )
                }
            }
        }

        List<Platform> platforms = doc.getPlatforms()

        if ( platforms ) {
            for (int i = 0; i < platforms.size(); i++) {
                Platform p = platforms.get(i)
                PlatformType.PlatformTypeBuilder platform = PlatformType.builder()
                    .name(p.getName())
                    .identifier(TypedIdentifierType.builder().value(p.getPlatformId()).build())
                    .type(p.getPlatformType())
                    .owner(p.getOwner())
                    .country(p.getCountry())
//                if (p.getCountry()) {
//                    String proposedCountry = p.getCountry();
//                    String countryName = OracleController.getCountryName(proposedCountry)
//                    platform.country(countryName != null ? countryName : proposedCountry)
//                }
                metadata.addPlatform(platform.build())
            }
        }

        /* TODO what about these
           <link_landing>http://data.nodc.noaa.gov/geoportal/catalog/search/resource/details.page?uuid={316B5C53-4B84-4909-A599-0A62D05D60D2}</link_landing>
           <link_download>http://www.nodc.noaa.gov/archive/arc0069/0117971/2.2/</link_download>
         */

        if ( doc.getDic() ) {
            metadata.addVariable(fillDic(doc.getDic()))
        }
        if ( doc.getTa() ) {
            metadata.addVariable(fillTa(doc.getTa()))
        }
        if ( doc.getPh() ) {
            metadata.addVariable(fillPh(doc.getPh()))
        }
        if ( doc.getPco2a() ) {
            metadata.addVariable(fillPCO2a(doc.getPco2a()))
        }
        if ( doc.getPco2d() ) {
            metadata.addVariable(fillPCO2d(doc.getPco2d()))
        }
        if ( doc.getCo2vars() ) {
            for (int i = 0; i < doc.getCo2vars().size(); i++) {
                GenericVariable v = doc.getCo2vars().get(i)
                BaseVariableType var = fillPCO2a(v)
                metadata.addVariable(var)
            }
        }
        if ( doc.getVariables() ) {
            for(int i = 0; i < doc.getVariables().size(); i++ ) {
                Variable v = doc.getVariables().get(i)
                BaseVariableType var = isBioVar(v) ? fillBioVar(v) : fillVariable(v)
                metadata.addVariable(var)
            }
        }

        OadsMetadataDocumentType md = metadata.build()
        return md
        // TODO the rest of the variable stuff.
    }

    private boolean isBioVar(GenericVariable v) {
        return ( v.getBiologicalSubject()
                || v.getDuration()
                || v.getLifeStage()
                || v.getSpeciesIdCode() )
    }

    private DicVariableType fillDic(GenericVariable v) {
        DicVariableType.DicVariableTypeBuilder dicVarBuilder = (DicVariableType.DicVariableTypeBuilder) _fillDic(v, DicVariableType.builder())
        dicVarBuilder.name("disolved_inorganic_carbon")
        return dicVarBuilder.build()
    }
    private DicVariableType.DicVariableTypeBuilder _fillDic(GenericVariable v, DicVariableTypeBuilder builder) {
        DicVariableType.DicVariableTypeBuilder dicVarBuilder = (DicVariableType.DicVariableTypeBuilder) fillVariable(v, builder)
        if ( v.getPoison()) {
            dicVarBuilder.poison(PoisonType.builder()
                .correction(v.getPoisonDescription())
                .name(v.getPoison())
                .volume(v.getPoisonVolume())
                .build()
            )
        }
        return dicVarBuilder
    }
    private TaVariableType fillTa(GenericVariable v) {
        TaVariableType.TaVariableTypeBuilder taBuilder = _fillDic(v, TaVariableType.builder())
        taBuilder.name("total_alkalinity")
        /*
        TA: Type of titration
        TA: Cell type (open or closed)
        TA: Curve fitting method
        TA: Magnitude of blank correction
        *
        */
        taBuilder.titrationType(v.getTitrationType())
        taBuilder.cellType(v.getCellType())
        taBuilder.curveFitting(v.getCurveFittingMethod())
        taBuilder.blankCorrection(v.getMagnitudeOfBlankCorrection())
        return taBuilder.build()
    }
    private PhVariableType fillPh(GenericVariable v) {
        PhVariableType.PhVariableTypeBuilder phBuilder = fillVariable(v, PhVariableType.builder())
        phBuilder.name("ph_total")
        /*
        pH: pH scale
        pH: Temperature of measurement
        XXX Standardization element !!! TODO: pH: pH values of the standards
        pH: Temperature correction method
        pH: at what temperature was pH reported
        */
        phBuilder.phScale(v.getPhScale())
        phBuilder.measurementTemperature(v.getTemperatureMeasurement())
        phBuilder.temperatureCorrectionMethod(v.getTemperatureCorrectionMethod())
        phBuilder.phReportTemperature(v.getPhTemperature())
        phBuilder.typeOfDye(v.getPhDyeTypeManuf())

        // doing this in fillVariable, since you cannot access the standardization element from the builder.
//        ph.standardization.phOfStandards(v.getPhStandards())
        return phBuilder.build()
    }
    private Co2Autonomous fillPCO2a(GenericVariable v) {
        Co2Autonomous.Co2AutonomousBuilder co2Builder = fillPCO2x(v, Co2Autonomous.builder())
        co2Builder.name("pco2/fco2 (autonomous)")
        /*
        pCO2A: Location of seawater intake
        pCO2A: Depth of seawater intake
        pCO2A: Equilbrator type
        pCO2A: Equilibrator volume (L)
        pCO2A: Vented or not
        pCO2A: Water flow rate (L/min)
        pCO2A: Headspace gas flow rate (L/min)
        pCO2A: How was temperature inside the equilibrator measured .
        pCO2A: How was pressure inside the equilibrator measured.
        pCO2A: Drying method for CO2 gas
        */
        co2Builder.locationSeawaterIntake(v.getIntakeLocation())
                .depthSeawaterIntake(v.getIntakeDepth())
                .co2GasDryingMethod(v.getDryingMethod())

        co2Builder.equilibrator(EquilibratorType.builder()
                        .type(v.getEquilibratorType())
                        .volume(v.getEquilibratorVolume())
                        .vented(v.getVented())
                        .waterFlowRate(v.getFlowRate())
                        .temperatureMeasurement(EquilibratorMeasurementType.builder()
                                .method(v.getEquilibratorTemperatureMeasureMethod())
                                .build())
                        .pressureMeasurement(EquilibratorMeasurementType.builder()
                        .method(v.getEquilibratorPressureMeasureMethod())
                        .build())
                .build()
        )
        return co2Builder.build()
    }
    private Co2Discrete fillPCO2d(GenericVariable v) {
        Co2Discrete.Co2DiscreteBuilder co2Builder = fillPCO2x(v, Co2Discrete.builder())
        co2Builder.name("pco2/fco2 (discrete)")
        /*
        pCO2D: Storage method
        pCO2D: Seawater volume (mL)
        pCO2D: Headspace volume (mL)
        pCO2D: Temperature of measurement
        */
        co2Builder.storageMethod(v.getStorageMethod())
                .seawaterVolume(v.getSeawaterVolume())
                .headspaceVolume(v.getHeadspaceVolume())
                .measurementTemperature(v.getTemperatureMeasurement())
        return co2Builder.build()
    }
    private Co2Base.Co2BaseBuilder fillPCO2x(GenericVariable v, Co2Base.Co2BaseBuilder builder) {
        Co2Base.Co2BaseBuilder co2Builder = fillVariable(v, builder)
        /*
        pCO2A: Manufacturer of the gas detector
        pCO2A: Model of the gas detector
        pCO2A: Resolution of the gas detector
        pCO2A: Uncertainty of the gas detector
        */
        co2Builder.gasDetector(GasDetectorType.builder()
                .manufacturer(v.getGasDetectorManufacture())
                .model(v.getGasDetectorModel())
                .resolution(v.getGasDectectorResolution())
                .uncertainty(v.getGasDectectorUncertainty())
                .build())

        /*
        pCO2A: Water vapor correction method
        pCO2A: Temperature correction method
        pCO2A: at what temperature was pCO2 reported
        */
        co2Builder.waterVaporCorrection(v.getVaporCorrection())
                .temperatureCorrectionMethod(v.getTemperatureCorrectionMethod())
                .co2ReportTemperature(v.getPco2Temperature())
        return co2Builder
    }
    /*
    fullname
    abbrev
    observationType
    insitu
    manipulationMethod
    unit
    measured
    calcMethod
    samplingInstrument
    analyzingInstrument
    detailedInfo
    replicate
    standard
        description
        frequency
        TODO: ph Values of Standards ...
        crm
            manufacturer
            batch
    poison
        poisonName
        volume
        correction
    uncertainty
    flag
    methodReference
    researcherName
    researcherInstitution
    */
    private BaseVariableType fillVariable(GenericVariable v) {
        return fillVariable(v, BaseVariableType.builder()).build()
    }

    private BiologicalVariable fillBioVar(GenericVariable v) {
        BiologicalVariable.BiologicalVariableBuilder builder = fillVariable(v, BiologicalVariable.builder())
        builder.biologicalSubject(v.getBiologicalSubject())
               .duration(v.getDuration())
               .lifeStage(v.getLifeStage())
               .speciesID(v.getSpeciesIdCode())
        return builder.build()
    }

    private BaseVariableTypeBuilder fillVariable(GenericVariable v, BaseVariableTypeBuilder variable) {
        variable.fullName(v.getFullVariableName())
            .name(v.getAbbreviation())
            .datasetVarName(v.getAbbreviation())
            .observationType(v.getObservationType())
            .variableType(v.getObservationDetail())
            .manipulationMethod(v.getManipulationMethod())
            .units(v.getUnits())
            .measuredOrCalculated(v.getMeasured())
            .samplingInstrument(v.getSamplingInstrument())
            .analyzingInstrument(v.getAnalyzingInstrument())
            .detailedAnalyzingInfo(v.getDetailedInformation())
            .fieldReplicateHandling(v.getFieldReplicate())

        if (v.getCalculationMethod())
            variable.calculationMethod(CalculationMethodType.builder().description(v.getCalculationMethod()).build())

        if ( v.getStandardizationTechnique() || v.getCrmManufacture() || v.getBatchNumber() ||
             v.getStandardGasManufacture() || v.getStandardGasUncertainties() || v.getGasConcentration() ||
             v.getPhStandards() || v.getTemperatureStandarization()) {

            StandardizationType.StandardizationTypeBuilder standard = StandardizationType.builder()
            standard.description(v.getStandardizationTechnique())
                    .frequency(v.getFreqencyOfStandardization())
                    .temperature(v.getTemperatureStandarization())
            if ( v instanceof PhVariableType ) {
                standard.phOfStandards(v.getPhStandards())
            }
            if ( v.getCrmManufacture() || v.getBatchNumber() ) {
                standard.crm(CrmType.builder()
                        .manufacturer(v.getCrmManufacture())
                        .batch(v.getBatchNumber())
                        .build()
                )
            }
            if ( v.getStandardGasManufacture() || v.getStandardGasUncertainties() || v.getGasConcentration()) {
                standard.addStandardGas(StandardGasType.builder()
                        .manufacturer(v.getStandardGasManufacture())
                        .uncertainty(v.getStandardGasUncertainties())
                        .concentration(v.getGasConcentration())
                        .build()
                )
            }
            variable.standardization(standard.build())
        }
        variable.uncertainty(v.getUncertainty())
        variable.qcFlag(QcFlagInfoType.builder()
                    .description(v.getQualityFlag())
                    .build())
        variable.methodReference(v.getReferenceMethod())
        variable.researcher(PersonReferenceType.builder()
                    .name(v.getResearcherName())
                    .organization(v.getResearcherInstitution())
                    .build())

       return variable
    }

    String getStandardNameFor(GenericVariable genericVariable) {
        return ( genericVariable instanceof Pco2a || genericVariable instanceof Pco2d ) ?
                "standardization" :
                "standard"
    }

    private PersonType fillPerson(Person p) {
        if  ( p == null ) {
            return null
        }
        PersonTypeBuilder person = PersonType.builder()
        person.name(PersonNameType.builder().first(p.getFirstName()).middle(p.getMi()).last(p.getLastName()).build())
        // Apparently institution in the spreadsheet is organization in the XML
        person.organization(p.getInstitution())
        person.contactInfo(
            PersonContactInfoType.builder()
                .address(AddressType.builder()
                    .addDeliveryPoint(new OrderedStringElementType(p.getAddress1(), 1))
                    .addDeliveryPoint(new OrderedStringElementType(p.getAddress2(), 2))
                    .city(p.getCity())
                    .administrativeArea(p.getState())
                    .country(p.getCountry())
                    .postalCode(p.getZip())
                    .build()
                 )
                .email(p.getEmail())
                .phone(p.getTelephone())
            .build()
        )
        if ( p.getRid()) {
            person.addIdentifier(TypedIdentifierType.builder().value(p.getRid()).type(p.getIdType()).build())
        }
        return person.build()
    }

    def lookForDelimiter(String authorList) {
        if ( authorList.contains(";")) {
            return ";"
        } else if ( count(authorList, ',' as char) > 1 ) {
            return ","
        } else {
            return null
        }
    }

    // This actually is likely to get it wrong.
    def _lookForDelimiter(String peak) {
        SortedMap<Integer, Character> sort = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.intValue() - o1.intValue();
            }
        });
        sort.put(count(peak, ',' as char), new Character(',' as char));
        sort.put(count(peak, ';' as char), new Character(';' as char));
        sort.put(count(peak, '\t' as char), new Character('\t' as char));
        sort.put(count(peak, '|' as char), new Character('|' as char));
        return sort.values().iterator().next().charValue();
    }

    /**
     * @param peak
     * @param c
     * @return
     */
    def count(String peak, char c) {
        int count = 0;
        for (int i = 0; i < peak.length(); i++) {
            if ( peak.charAt(i) == c) {
                count += 1;
            }
        }
        return new Integer(count);
    }
}
