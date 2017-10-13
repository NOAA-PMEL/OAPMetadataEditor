package oap

import grails.transaction.Transactional
import org.jdom2.Element
import org.jdom2.input.SAXBuilder
import org.jdom2.output.Format
import org.jdom2.output.XMLOutputter

@Transactional
class XmlService {

    def createDocument(FileInputStream f) {

        SAXBuilder saxBuilder = new SAXBuilder()
        org.jdom2.Document document = saxBuilder.build(f)
        Document doc = new Document()

        Element root = document.getRootElement();
        List<Element> people = root.getChildren("person")

        // Investigators
        for (int i = 0; i < people.size(); i++) {
            Element person = people.get(i)
            if ( person.getChild("role") != null && person.getChild("role").getText().equals("investigator") ) {
                Investigator p = fillPersonDomain(person)
                doc.addToInvestigators(p)
            }
        }
        // Data Submitter
        Element datasubmitterE = root.getChild("datasubmitter")
        if ( datasubmitterE ) {
            DataSubmitter p = fillPersonDomain(datasubmitterE)
            doc.setDataSubmitter(p)
        }

        // Citation
        Citation citation = new Citation()
        Element title = root.getChild("title")
        if ( title ) {
            citation.setTitle(title.getTextTrim());
        }

        Element platformAbstract = root.getChild("abstract")
        if ( platformAbstract ) {
            citation.setPlatformAbstract(platformAbstract.getTextTrim())
        }

        Element purpose = root.getChild("purpose")
        if ( purpose ) {
            citation.setPurpose(purpose.getTextTrim())
        }

        Element researchProjects = root.getChild("researchProjects")
        if ( researchProjects ) {
            citation.setResearchProjects(researchProject.getTextTrim())
        }

        Element expocode = root.getChild("expocode")
        if ( expocode ) {
            citation.setExpocode(expocode.getTextTrim())
        }

        Element cruiseID = root.getChild("cruiseID")
        if ( cruiseID ) {
            citation.setCruiseId(cruiseID.getTextTrim())
        }

        Element section = root.getChild("section")
        if ( section ) {
            citation.setSection(section.getTextTrim())
        }

        Element reference = root.getChild("reference")
        if ( reference ) {
            citation.setScientificReferences(reference.getTextTrim())
        }

        Element citationList = root.getChild("citation")
        if ( citationList ) {
            citation.setCitationAuthorList(citationList.getTextTrim())
        }

        Element suppleInfo = root.getChild("suppleInfo")
        if ( suppleInfo ) {
            citation.setSupplementalInformation(suppleInfo.getTextTrim())
        }
        doc.setCitation(citation)

        // Time and Location Information
        TimeAndLocation timeAndLocation = new TimeAndLocation()
        Element startdate = root.getChild("startdate")
        if ( startdate ) {
            timeAndLocation.setStartDate(startdate.getTextTrim())
        }
        Element enddate = root.getChild("enddate")
        if ( enddate ) {
            timeAndLocation.setEndDate(enddate.getTextTrim())
        }
        Element westbd = root.getChild("westbd")
        if (westbd ) {
            timeAndLocation.setWestLon(westbd.getTextTrim())
        }
        Element eastbd = root.getChild("eastbd")
        if ( eastbd ) {
            timeAndLocation.setEastLon(eastbd.getTextTrim())
        }
        Element northbd = root.getChild("northbd")
        if ( northbd ) {
            timeAndLocation.setNorthLat(northbd.getTextTrim())
        }
        Element southbd = root.getChild("southbd")
        if ( southbd ) {
            timeAndLocation.setSouthLat(southbd.getTextTrim())
        }
        Element spatialReference = root.getChild("spatialReference")
        if ( spatialReference ) {
            timeAndLocation.setSpatialRef(spatialReference.getTextTrim())
        }
        Element geographicName = root.getChild("geographicName")
        if ( geographicName ) {
            // TODO this is wrong. We need to collect a list, either by comma separating or entering and accumulating them
            timeAndLocation.setGeoNames(geographicName.getTextTrim())
        }
        Element locationOrganism = root.getChild("locationOrganism")
        if ( locationOrganism ) {
            timeAndLocation.setOrganismLoc(locationOrganism.getText())
        }
        doc.setTimeAndLocation(timeAndLocation)

        // Funding
        Funding funding = new Funding()

        Element agency = root.getChild("agency")
        if ( agency ) {
            funding.setAgencyName(agency.getTextTrim())
        }
        Element granttitle = root.getChild("title")
        if ( granttitle ) {
            funding.setGrantTitle(granttitle.getTextTrim())
        }
        Element ID = root.getChild("ID")
        if ( ID ) {
            funding.setGrandNumber(ID.getTextTrim())
        }

        doc.setFunding(funding)

        Platform platform = new Platform()
        Element platformE = root.getChild("Platform")

        if (platformE) {

            Element platformName = platformE.getChild("PlatformName")
            if (platformName) {
                platform.setName(platformName.getTextTrim())
            }
            Element platformId = platformE.getChild("PlatformID")
            if (platformId) {
                platform.setPlatformId(platformId.getTextTrim())
            }
            Element platformType = platformE.getChild("PlatformType")
            if (platformType) {

                platform.setPlatformType(platformType.getText())
            }
            Element platformOwner = platformE.getChild("PlatformOwner")
            if (platformOwner) {
                platform.setOwner(platformOwner.getText())
            }
            Element platformCountry = platformE.getChild("PlatformCountry")
            if (platformCountry) {
                platform.setCountry(platformCountry.getText())
            }
            doc.addToPlatforms(platform)
        }

        List<Element> variables = root.getChildren("variable")
        for (int i = 0; i < variables.size(); i++) {
            Element variableE = variables.get(i)

            if ( variableE.getChild("fullname") && variableE.getChild("fullname").getTextTrim().toLowerCase().equals("dissolved inorganic carbon") ) {
                Dic dic = fillVariableDomain(variableE)
                doc.setDic(dic)
            } else if ( variableE.getChild("fullname") && variableE.getChild("fullname").getTextTrim().toLowerCase().equals("ph") ) {
                Ph ph = fillVariableDomain(variableE)
                doc.setPh(ph)
            } else if ( variableE.getChild("fullname") && variableE.getChild("fullname").getTextTrim().toLowerCase().equals("pco2 (fco2) autonomous") ) {
                Pco2a p = fillVariableDomain(variableE)
                doc.setPco2a(p)
            } else if ( variableE.getChild("fullname") && variableE.getChild("fullname").getTextTrim().toLowerCase().equals("pco2 (fco2) discrete")) {
                Pco2d p = fillVariableDomain(variableE)
                doc.setPco2d(p)
            } else if ( variableE.getChild("fullname") && variableE.getChild("fullname").getTextTrim().toLowerCase().equals("total alkalinity") ) {
                Ta ta = fillVariableDomain(variableE)
                doc.setTa(ta)
            } else {
                Variable variable = fillVariableDomain(variableE)
                doc.addToVariables(variable)
            }
        }


        return doc
    }


    private GenericVariable fillVariableDomain(Element variable) {

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
    // ???
    // <temperatureCorrectionMethod>
    // <temperatureCorrection>
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

        def v
        if ( variable.getChild("fullname") && variable.getChild("fullname").getTextTrim().toLowerCase().equals("dissolved inorganic carbon") ) {
            v = new Dic()
        } else if ( variable.getChild("fullname") && variable.getChild("fullname").getTextTrim().toLowerCase().equals("ph") ) {
            v = new Ph()
        } else if ( variable.getChild("fullname") && variable.getChild("fullname").getTextTrim().toLowerCase().equals("pco2 (fco2) autonomous") ) {
            v = new Pco2a()
        } else if ( variable.getChild("fullname") && variable.getChild("fullname").getTextTrim().toLowerCase().equals("pco2 (fco2) discrete")) {
            v = new Pco2d()
        } else if ( variable.getChild("fullname") && variable.getChild("fullname").getTextTrim().toLowerCase().equals("total alkalinity") ) {
            v = new Ta()
        } else {
            v = new Variable()
        }

        Element fullname = variable.getChild("fullname")
        if ( fullname ) {
            v.setFullVariableName(fullname.getTextTrim())
        }
        Element abbrev = variable.getChild("abbrev")
        if ( abbrev ) {
            v.setAbbreviation(abbrev.getTextTrim())
        }
        Element observationType = variable.getChild("observationType")
        if ( observationType ) {
            v.setObservationType(observationType.getTextTrim())
        }
        Element insitu = variable.getChild("insitu")
        if ( insitu ) {
            v.setObservationDetail(insitu.getTextTrim())
        }
        Element manipulationMethod = variable.getChild("manipulationMethod")
        if ( manipulationMethod ) {
            v.setManipulationMethod(manipulationMethod.getTextTrim())
        }
        Element unit = variable.getChild("unit")
        if ( unit ) {
            v.setUnits(unit.getTextTrim())
        }
        Element measured = variable.getChild("measured")
        if ( measured ) {
            v.setMeasured(measured.getTextTrim())
        }
        Element calcMethod = variable.getChild("calcMethod")
        if ( calcMethod ) {
            v.setCalculationMethod(calcMethod.getTextTrim())
        }
        Element samplingInstrument = variable.getChild("samplingInstrument")
        if ( samplingInstrument ) {
            v.setSamplingInstrument(samplingInstrument.getTextTrim())
        }
        Element analyzingInstrument = variable.getChild("analyzingInstrument")
        if ( analyzingInstrument ) {
            v.setAnalyzingInstrument(analyzingInstrument.getTextTrim())
        }
        Element detailedInfo = variable.getChild("detailedInfo")
        if ( detailedInfo ) {
            v.setDetailedInformation(detailedInfo.getTextTrim())
        }
        Element replicate = variable.getChild("replicate")
        if ( replicate ) {
            v.setFieldReplicate(replicate.getTextTrim())
        }



        // TODO this is in two different parent elements in the example <standard> and <standardization>
        Element standard = variable.getChild("standard")
        if ( standard ) {
            Element technique = standard.getChild("standardizationTechnique")
            if ( technique ) {
                v.setStandardizationTechnique(technique.getTextTrim())
            }
            Element frequency = standard.getChild("frequency")
            // TODO description
            if ( frequency ) {
                v.setFreqencyOfStandardization(frequency.getTextTrim())
            }
            Element crm = standard.getChild("crm")
            if ( crm ) {
                Element manufacture = crm.getChild("manufacturer")
                if ( manufacture ) {
                    v.setCrmManufacture(manufacture.getTextTrim())
                }
                Element batch = crm.getChild("batch")
                if ( batch ) {
                    v.setBatchNumber(batch.getText())
                }
            }
        }



        Element poison = variable.getChild("poison")

        if ( poison ) {
            Element poisonName = poison.getChild("poisonName")
            if  ( poisonName ) {
                v.setPoison(poisonName.getTextTrim())
            }

            Element volume = poison.getChild("volume")
            if ( volume ) {
                v.setPoisonVolume(volume.getText())
            }
            Element poisonDescription = poison.getChild("correction")
            if ( poisonDescription ) {
                v.setPoisonDescription(poisonDescription.getTextTrim())
            }


    }


//
        Element uncertainty = variable.getChild("uncertainty")
        if ( uncertainty ) {
            v.setUncertainty(uncertainty.getText())
        }
        Element flag = variable.getChild("flag")
        if ( flag ) {
            v.setQualityFlag(flag.getText())
        }
        Element methodReference = variable.getChild("methodReference")
        if ( methodReference ) {
            v.setReferenceMethod(methodReference.getText())
        }
        Element researcherName = variable.getChild("researcherName")
        if ( researcherName ) {
            v.setResearcherName(researcherName.getText())
        }
        Element researcherInstitution = variable.getChild("researcherInstitution")
        if ( researcherInstitution ) {
            v.setResearcherInstitution(researcherInstitution.getText())
        }

        Element storageMethod = variable.getChild("storageMethod")
        if ( storageMethod ) {
            v.setStorageMethod(storageMethod.getTextTrim())
        }

        Element co2ReportTemperature = variable.getChild("co2ReportTemperature")
        if ( co2ReportTemperature ) {
            v.setPco2Temperature(co2ReportTemperature.getTextTrim());
        }

        // 025 at what temperature was pH reported
        // <phReportTemperature>
        // TextBox pHtemperature;
        Element phReportTemperature = variable.getChild("phReportTemperature")
        if ( phReportTemperature ) {
            v.setPhTemperature(phReportTemperature.getTextTrim())
        }

        // 026 Biological subject
        // <biologicalSubject>
        // TextBox biologicalSubject;
        Element biologicalSubject = variable.getChild("biologicalSubject")
        if ( biologicalSubject ) {
            v.setBiologicalSubject(biologicalSubject.getTextTrim())
        }

        // 027 Cell type (open or closed)
        // <cellType>
        // ButtonDropDown cellType;
        Element cellType = variable.getChild("cellType")
        if ( cellType ) {
            v.setCellType(cellType.getTextTrim())
        }

        // 029 Curve fitting method
        // <curveFitting>
        // TextBox curveFittingMethod;

        Element curveFitting = variable.getChild("curveFitting")
        if ( curveFitting ) {
            v.setCurveFittingMethod(curveFitting.getTextTrim())
        }

        // 030 Depth of seawater intake
        // <DepthSeawaterIntake>
        // TextBox intakeDepth;
        Element DepthSeawaterIntake = variable.getChild("DepthSeawaterIntake")
        if ( DepthSeawaterIntake ) {
            v.setIntakeDepth(DepthSeawaterIntake.getTextTrim())
        }



        // 032 Duration (for settlement/colonization methods)
        // <duration>
        // TextBox duration;

        Element duration = variable.getChild("duration")
        if ( duration ) {
            v.setDuration(duration.getTextTrim())
        }

        Element equilibrator = variable.getChild("equilibrator")
        if ( equilibrator ) {
            Element type = equilibrator.getChild("type")
            if ( type ) {
                v.setEquilibratorType(type.getTextTrim())
            }
            Element volume = equilibrator.getChild("volume")
            if ( volume ) {
                v.setEquilibratorVolume(volume.getTextTrim())
            }
            Element vented = equilibrator.getChild("vented")
            if ( vented ) {
                v.setVented(vented.getTextTrim())
            }
            Element waterFlowRate = equilibrator.getChild("waterFlowRate")
            if ( waterFlowRate ) {
                v.setFlowRate(waterFlowRate.getTextTrim())
            }
            Element gasFlowRate = equilibrator.getChild("gasFlowRate")
            if ( gasFlowRate ) {
                v.setGasFlowRate(gasFlowRate.getTextTrim())
            }
            Element temperatureEquilibratorMethod = equilibrator.getChild("temperatureEquilibratorMethod")
            if ( temperatureEquilibratorMethod ) {
                v.setEquilibratorTemperatureMeasureMethod(temperatureEquilibratorMethod.getTextTrim())
            }
            Element pressureEquilibratorMethod = equilibrator.getChild("pressureEquilibratorMethod")
            if ( pressureEquilibratorMethod ) {
                v.setEquilibratorPressureMeasureMethod(pressureEquilibratorMethod.getTextTrim())
            }

            // 031 Drying method for CO2 gas
            // <dryMethod>
            // TextBox dryingMethod;

            Element dryMethod = variable.getChild("dryMethod")
            if ( dryMethod ) {
                v.setDryingMethod(dryMethod.getTextTrim())
            }
        }

        Element headspacevol = variable.getChild("headspacevol");
        if ( headspacevol ) {
            v.setHeadspaceVolume(headspacevol.getTextTrim())
        }

        Element lifeStage = variable.getChild("lifeStage")
        if ( lifeStage ) {
            v.setLifeStage(lifeStage.getTextTrim())
        }

        Element locationSeawaterIntake = variable.getChild("locationSeawaterIntake")
        if ( locationSeawaterIntake ) {
            v.setIntakeLocation(locationSeawaterIntake.getTextTrim())
        }


        Element gasDetector = variable.getChild("gasDetector")
        if ( gasDetector ) {
            Element manufacturer = gasDetector.getChild("manufacturer")
            if (manufacturer  ) {
                v.setGasDetectorManufacture(manufacturer.getTextTrim())
            }
            Element model = gasDetector.getChild("model")
            if ( model ) {
                v.setGasDetectorModel(model.getTextTrim())
            }
            Element resolution = gasDetector.getChild("resolution")
            if ( resolution ) {
                v.setGasDectectorResolution(resolution.getTextTrim())
            }
            Element gasuncertainty = gasDetector.getChild("uncertainty")
            if ( gasuncertainty ) {
                v.setGasDectectorUncertainty(gasuncertainty.getTextTrim())
            }
        }

        Element phscale = variable.getChild("phscale")
        if ( phscale ) {
            v.setPhScale(phscale.getTextTrim())
        }

        Element seawatervol = variable.getChild("seawatervol")
        if ( seawatervol ) {
            v.setSeawaterVolume(seawatervol.getTextTrim())
        }

        Element speciesID = variable.getChild("speciesID")
        if ( speciesID ) {
            v.setSpeciesIdCode(speciesID.getTextTrim())
        }

        Element temperatureCorrectionMethod = variable.getChild("temperatureCorrectionMethod")
        if ( temperatureCorrectionMethod ) {
            v.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getTextTrim())
        }

        Element temperatureCorrection = variable.getChild("temperatureCorrection")
        if ( temperatureCorrection ) {
            v.setTemperatureCorrection(temperatureCorrection.getTextTrim())
        }

        Element temperatureMeasure = variable.getChild("temperatureMeasure")
        if ( temperatureMeasure ) {
            v.setTemperatureMeasurement(temperatureMeasure.getTextTrim())
        }

        Element titrationType = variable.getChild("titrationType")
        if ( titrationType ) {
            v.setTitrationType(titrationType.getTextTrim())
        }

        Element waterVaporCorrection = variable.getChild("waterVaporCorrection")
        if ( waterVaporCorrection ) {
            v.setVaporCorrection(waterVaporCorrection.getTextTrim())
        }
        // TODO set the internal variable number

        return v
    }
    private Person fillPersonDomain(Element p) {
        def human;
        if ( p.getChild("role") != null && p.getChild("role").getText().equals("investigator") ) {
            human = new Investigator()
        } else {
            human = new DataSubmitter()
        }
        if ( p.getChild("name") ) {
            String name = p.getChild("name").getText().trim();
            if (name.length() > 0 ) {
                // TODO mi????
                human.setFirstName(name.substring(0, name.lastIndexOf(" ")))
                human.setLastName(name.substring(name.lastIndexOf(" ")))
            }
            Element organization = p.getChild("organization");
            if ( organization ) {
                human.setInstitution(organization.getTextTrim())
            }
            Element deliverypoint1 = p.getChild("deliverypoint1");
            if ( deliverypoint1 ) {
                human.setAddress1(deliverypoint1.getTextTrim())
            }
            Element deliverypoint2 = p.getChild("deliverypoint2");
            if ( deliverypoint2 ) {
                human.setAddress2(deliverypoint2.getTextTrim())
            }
            Element city = p.getChild("city");
            if ( city ) {
                human.setCity(city.getTextTrim())
            }
            Element administrativeArea = p.getChild("administrativeArea");
            if ( administrativeArea ) {
                human.setState(administrativeArea.getTextTrim())
            }
            Element zip = p.getChild("zip");
            if ( zip ) {
                human.setZip(zip.getTextTrim())
            }
            Element country = p.getChild("country");
            if ( country ) {
                human.setCountry(country.getTextTrim())
            }
            Element phone = p.getChild("phone");
            if ( phone ) {
                human.setTelephone(phone.getTextTrim())
            }
            Element email = p.getChild("email");
            if ( email ) {
                human.setEmail(email.getTextTrim())
            }
            Element ID = p.getChild("ID");
            if ( ID ) {
                human.setRid(ID.getTextTrim())
            }
            Element IDtype = p.getChild("IDtype");
            if ( IDtype ) {
                human.setIdType(IDtype.getTextTrim())
            }
        }
        return human;
    }
    def createXml(Document doc) {

        org.jdom2.Document document = new org.jdom2.Document();
        Element metadata = new Element("metadata");
        document.setRootElement(metadata)

        for (int i = 0; i < doc.getInvestigators().size(); i++) {
            Person p = doc.getInvestigators().get(i)
            Element person = new Element("person")
            fillPerson(p, person, "investigator")
            metadata.addContent(person)
        }

        if ( doc.getDataSubmitter() ) {
            Person ds = doc.getDataSubmitter();
            Element dataSubmitter = new Element("datasubmitter");
            fillPerson(ds, dataSubmitter, null)
            metadata.addContent(dataSubmitter)
        }

        Citation citation = doc.getCitation();
        if ( citation ) {
            if (citation.getTitle()) {
                Element title = new Element("title")
                title.setText(citation.getTitle())
                metadata.addContent(title)
            }

            // The element is called "abstract" which is a reserved word in Java
            if (citation.getPlatformAbstract()) {
                Element platformAbstract = new Element("abstract")
                platformAbstract.setText(citation.getPlatformAbstract())
                metadata.addContent(platformAbstract)
            }

            if (citation.getPurpose()) {
                Element purpose = new Element("purpose")
                purpose.setText(citation.getPurpose())
                metadata.addContent(purpose)
            }
            if ( citation.getResearchProjects() ) {
                Element researchProjects = new Element("researchProjects")
                researchProjects.setText(citation.getResearchProjects())
                metadata.addContent(researchProjects)
            }

            if ( citation.getExpocode() ) {
                Element expocode = new Element("expocode")
                expocode.setText(citation.getExpocode())
                metadata.addContent(expocode)
            }
            if ( citation.getCruiseId() ) {
                Element cruiseID = new Element("cruiseID")
                cruiseID.setText(citation.getCruiseId())
                metadata.addContent(cruiseID)
            }
            if ( citation.getSection() ) {
                Element section = new Element("section")
                section.setText(citation.getSection())
                metadata.addContent(section)
            }
            if ( citation.getScientificReferences() ) {
                Element reference = new Element("reference")
                reference.setText(citation.getScientificReferences())
                metadata.addContent(reference)
            }
            // TODO the XML does not match the spreadsheet
            if ( citation.getCitationAuthorList() ) {
                Element citationList = new Element("citation")
                citationList.setText(citation.getCitationAuthorList())
                metadata.addContent(citationList)
            }
            if ( citation.getSupplementalInformation() ) {
                Element suppleInfo = new Element("suppleInfo")
                suppleInfo.setText(citation.getSupplementalInformation())
                metadata.addContent(suppleInfo)
            }
        }

        TimeAndLocation timeAndLocation = doc.getTimeAndLocation()
        if ( timeAndLocation ) {
            if ( timeAndLocation.getStartDate() ) {
                Element startdate = new Element("startdate")
                startdate.setText(timeAndLocation.getStartDate())
                metadata.addContent(startdate)
            }
            if ( timeAndLocation.getEndDate() ) {
                Element enddate = new Element("enddate")
                enddate.setText(timeAndLocation.getEndDate())
                metadata.addContent(enddate)
            }
            if (timeAndLocation.getWestLon() ) {
                Element westbd = new Element("westbd")
                westbd.setText(timeAndLocation.getWestLon())
                metadata.addContent(westbd)
            }
            if ( timeAndLocation.getEastLon() ) {
                Element eastbd = new Element("eastbd")
                eastbd.setText(timeAndLocation.getEastLon())
                metadata.addContent(eastbd)
            }
            if ( timeAndLocation.getNorthLat() ) {
                Element northbd = new Element("northbd")
                northbd.setText(timeAndLocation.getNorthLat())
                metadata.addContent(northbd)
            }
            if ( timeAndLocation.getSouthLat() ) {
                Element southbd = new Element("southbd")
                southbd.setText(timeAndLocation.getSouthLat())
                metadata.addContent(southbd)
            }
            if ( timeAndLocation.getSpatialRef() ) {
                Element spatialReference = new Element("spatialReference")
                spatialReference.setText(timeAndLocation.getSpatialRef())
                metadata.addContent(spatialReference)
            }
            if ( timeAndLocation.getGeoNames() ) {
                Element geographicName = new Element("geographicName")
                // TODO this is wrong. We need to collect a list, either by comma separating or entering and accumulating them
                geographicName.setText(timeAndLocation.getGeoNames())
                metadata.addContent(geographicName)
            }
            if ( timeAndLocation.getOrganismLoc() ) {
                Element locationOrganism = new Element("locationOrganism")
                locationOrganism.setText(timeAndLocation.getOrganismLoc())
                metadata.addContent(locationOrganism)
            }
        }
        Funding funding = doc.getFunding()
        if ( funding ) {
            Element fundingAgency = new Element("fundingAgency")
            if ( funding.getAgencyName() ) {
                Element agency = new Element("agency")
                agency.setText(funding.getAgencyName())
                fundingAgency.addContent(agency)
            }
            if ( funding.getGrantTitle() ) {
                Element title = new Element("title")
                title.setText(funding.getGrantTitle())
                fundingAgency.addContent(title)
            }
            if ( funding.getGrantNumber() ) {
                Element ID = new Element("ID")
                ID.setText(funding.getGrantNumber())
                fundingAgency.addContent(ID)
            }
            metadata.addContent(fundingAgency)
        }

        List<Platform> platforms = doc.getPlatforms()
        if ( platforms ) {
            for (int i = 0; i < platforms.size(); i++) {
                Platform platform = platforms.get(i)
                Element platformE = new Element("Platform");
                if (platform.getName()) {
                    Element platformName = new Element("PlatformName")
                    platformName.setText(platform.getName())
                    platformE.addContent(platformName)
                }
                if (platform.getPlatformId()) {
                    Element platformId = new Element("PlatformID")
                    platformId.setText(platform.getPlatformId())
                    platformE.addContent(platformId)
                }
                if (platform.getPlatformType()) {
                    Element platformType = new Element("PlatformType")
                    platformType.setText(platform.getPlatformType())
                    platformE.addContent(platformType)
                }
                if (platform.getOwner()) {
                    Element platformOwner = new Element("PlatformOwner")
                    platformOwner.setText(platform.getOwner())
                    platformE.addContent(platformOwner)
                }
                if (platform.getCountry()) {
                    Element platformCountry = new Element("PlatformCountry")
                    platformCountry.setText(platform.getCountry())
                    platformE.addContent(platformCountry)
                }
                metadata.addContent(platformE)
            }
        }

        /* TODO what about these
           <link_landing>http://data.nodc.noaa.gov/geoportal/catalog/search/resource/details.page?uuid={316B5C53-4B84-4909-A599-0A62D05D60D2}</link_landing>
           <link_download>http://www.nodc.noaa.gov/archive/arc0069/0117971/2.2/</link_download>
         */

        if ( doc.getDic() ) {
            Element variable = fillVariable(doc.getDic())
            metadata.addContent(variable)
        }
        if ( doc.getTa() ) {
            Element variable = fillVariable(doc.getTa())
            metadata.addContent(variable)
        }
        if ( doc.getPh() ) {
            Element variable = fillVariable(doc.getPh())
            metadata.addContent(variable)
        }
        if ( doc.getPco2a() ) {
            Element variable = fillVariable(doc.getPco2a())
            metadata.addContent(variable)
        }
        if ( doc.getPco2d() ) {
            Element variable = fillVariable(doc.getPco2d())
            metadata.addContent(variable)
        }
        if ( doc.getVariables() ) {
            for(int i = 0; i < doc.getVariables().size(); i++ ) {
                Variable v = doc.getVariables().get(i)
                Element variable = fillVariable(v)
                metadata.addContent(variable)
            }
        }

        // TODO the rest of the variable stuff.
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat())
        String xml = outputter.outputString(document)
        return xml
    }
    private Element fillVariable(GenericVariable v) {
        Element variable = new Element("variable")
        if ( v.getFullVariableName() ) {
            Element fullname = new Element("fullname")
            fullname.setText(v.getFullVariableName())
            variable.addContent(fullname)
        }
        if ( v.getAbbreviation() ) {
            Element abbrev = new Element("abbrev")
            abbrev.setText(v.getAbbreviation())
            variable.addContent(abbrev)
        }
        if ( v.getObservationType() ) {
            Element observationType = new Element("observationType")
            observationType.setText(v.getObservationType())
            variable.addContent(observationType)
        }
        if ( v.getObservationType() ) {
            Element insitu = new Element("insitu")
            insitu.setText(v.getObservationDetail())
            variable.addContent(insitu)
        }
        if ( v.getManipulationMethod() ) {
            Element manipulationMethod = new Element("manipulationMethod")
            manipulationMethod.setText(v.getManipulationMethod())
            variable.addContent(manipulationMethod)
        }
        if ( v.getUnits() ) {
            Element unit = new Element("unit")
            unit.setText(v.getUnits())
            variable.addContent(unit)
        }
        if ( v.getMeasured() ) {
            Element measured = new Element("measured")
            measured.setText(v.getMeasured())
            variable.addContent(measured)
        }
        if ( v.getCalculationMethod() ) {
            Element calcMethod = new Element("calcMethod")
            calcMethod.setText(v.getCalculationMethod())
            variable.addContent(calcMethod)
        }
        if ( v.getSamplingInstrument() ) {
            Element samplingInstrument = new Element("samplingInstrument")
            samplingInstrument.setText(v.getSamplingInstrument())
            variable.addContent(v.getSamplingInstrument())
        }
        if ( v.getAnalyzingInstrument() ) {
            Element analyzingInstrument = new Element("analyzingInstrument")
            analyzingInstrument.setText(v.getAnalyzingInstrument())
            variable.addContent(analyzingInstrument)
        }
        if ( v.getDetailedInformation() ) {
            Element detailedInfo = new Element("detailedInfo")
            detailedInfo.setText(v.getDetailedInformation())
            variable.addContent(detailedInfo)
        }
        if ( v.getFieldReplicate() ) {
            Element replicate = new Element("replicate")
            replicate.setText(v.getFieldReplicate())
            variable.addContent(replicate)
        }
        Element standard = new Element("standard")
        // TODO description
        if ( v.getFreqencyOfStandardization() ) {
            Element frequency = new Element("frequency")
            frequency.setText(v.getFreqencyOfStandardization())
            standard.addContent(frequency)
        }
        Element crm = new Element("crm")
        if ( v.getCrmManufacture() ) {
            Element manufacturer = new Element("manufacturer")
            manufacturer.setText(v.getCrmManufacture())
            crm.addContent(manufacturer)
        }
        if ( v.getBatchNumber() ) {
            Element batch = new Element("batch")
            batch.setText(v.getBatchNumber())
            crm.addContent(batch)
        }
        standard.addContent(crm)
        variable.addContent(standard)
        Element poison = new Element("poison")
        if ( v.getPoison() ) {
            Element poisonName = new Element("poisonName")
            poisonName.setText(v.getPoison())
            poison.addContent(poisonName)
        }
        if ( v.getPoisonVolume() ) {
            Element volume = new Element("volume")
            volume.setText(v.getPoisonVolume())
            poison.addContent(volume)
        }
        if ( v.getPoisonDescription() ) {
            Element correction = new Element("correction")
            correction.setText(v.getPoisonDescription())
            poison.addContent(correction)
        }
        variable.addContent(poison)
        if ( v.getUncertainty() ) {
            Element uncertainty = new Element("uncertainty")
            uncertainty.setText(v.getUncertainty())
            variable.addContent(uncertainty)
        }
        if ( v.getQualityFlag() ) {
            Element flag = new Element("flag")
            flag.setText(v.getQualityFlag())
            variable.addContent(flag)
        }
        if ( v.getReferenceMethod() ) {
            Element methodReference = new Element("methodReference")
            methodReference.setText(v.getReferenceMethod())
            variable.addContent(methodReference)
        }
        if ( v.getResearcherName() ) {
            Element researcherName = new Element("researcherName")
            researcherName.setText(v.getResearcherName())
            variable.addContent(researcherName)
        }
        if ( v.getResearcherInstitution() ) {
            Element researcherInstitution = new Element("researcherInstitution")
            researcherInstitution.setText(v.getResearcherInstitution())
            variable.addContent(researcherInstitution)
        }
        // TODO set the internal variable number

        return variable
    }
    private void fillPerson(Person p, Element person, String type) {
        def name = "";
        if ( p.getFirstName() ) {
            name = p.getFirstName()
        }
        if ( p.getLastName() ) {
            if ( name.length() > 0 ) name = name + " "
            name = name + p.getLastName()
        }
        person.addContent(new Element("name").setText(name))
        // Apparently institution in the spreadsheet is organization in the XML
        if ( p.getInstitution() )
            person.addContent(new Element("organization").setText(p.getInstitution()))
        // Apparently address1 in the spreadsheet is deliverypoint1 in the XML.
        person.addContent(new Element("deliverypoint1").setText(p.getAddress1()))

        if ( p.getAddress2() ) {
            person.addContent(new Element("deliverypoint2").setText(p.getAddress2()))
        } else {
            person.addContent(new Element("deliverypoint2"))
        }
        if ( p.getCity() )
            person.addContent(new Element("city").setText(p.getCity()))
        // Same comment above state=adminsitrativeArea
        if ( p.getState() )
            person.addContent(new Element("administrativeArea").setText(p.getState()))
        // But, not postal code instead of zip??
        if ( p.getZip() )
            person.addContent(new Element("zip").setText(p.getZip()))
//            person.addContent(new Element("country").setText(p.getCountry()))
        if ( p.getTelephone() && p.getExtension() ) {
            person.addContent(new Element("phone").setText(p.getTelephone() + " " +p.getExtension() ))
        } else if ( p.getTelephone() ) {
            person.addContent(new Element("phone").setText(p.getTelephone()))
        }
        if ( p.getEmail() )
            person.addContent(new Element("email").setText(p.getEmail()))
        if ( p.getRid() )
            person.addContent(new Element("ID").setText(p.getRid()))
        if ( p.getIdType() )
            person.addContent(new Element("IDtype").setText(p.getIdType()))
        if ( type != null )
            person.addContent(new Element("role").setText(type))
    }
}
