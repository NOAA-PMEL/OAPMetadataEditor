package oap

import grails.transaction.Transactional
import org.jdom2.Attribute
import org.jdom2.Element
import org.jdom2.input.SAXBuilder
import org.jdom2.output.Format
import org.jdom2.output.XMLOutputter

import gov.noaa.pmel.excel2oap.PoiReader2;

@Transactional
class XmlService {

    private static boolean isEmpty(Element e) {
        if ( e == null ) {
            return true;
        }
        if ( e.getTextTrim().length() > 0 || e.hasAttributes()) {
            return false;
        }
        boolean emptyChildren = true;
        Iterator<Element> children = e.getChildren().iterator();
        while ( emptyChildren && children.hasNext()) {
            Element child = children.next();
            emptyChildren = isEmpty(child);
        }
        return emptyChildren;
    }

    def createDocumentFromLegacyXML(InputStream ins) {
        log.info("Creating Document from legacy xml")
        SAXBuilder saxBuilder = new SAXBuilder()
        org.jdom2.Document xmlDocument = saxBuilder.build(ins)
        Document mdDoc = new Document()
        Element root = xmlDocument.getRootElement()

        List<Element> people = root.getChildren("person")

        // Investigators
        for (int i = 0; i < people.size(); i++) {
            Element person = people.get(i)
            if ( person.getChild("role") != null && person.getChild("role").getText().equalsIgnoreCase("investigator") ) {
                Investigator p = fillPersonDomain(person, new Investigator())
                mdDoc.addToInvestigators(p)
            }
        }
        // Data Submitter
        Element datasubmitter = root.getChild("datasubmitter")
        if ( ! isEmpty(datasubmitter) ) {
            DataSubmitter p = fillPersonDomain(datasubmitter, new DataSubmitter())
            mdDoc.setDataSubmitter(p)
        }

        // Citation
        Citation citation = new Citation()
        Element title = root.getChild("title")
        if ( ! isEmpty(title) ) {
            citation.setTitle(title.getTextTrim());
        }

        Element datasetAbstract = root.getChild("abstract")
        if ( ! isEmpty(datasetAbstract) ) {
            citation.setDatasetAbstract(datasetAbstract.getTextTrim())
        }

        Element purpose = root.getChild("purpose")
        if ( ! isEmpty(purpose) ) {
            citation.setPurpose(purpose.getTextTrim())
        }

        Element researchProjects = root.getChild("researchProject")
        if ( ! isEmpty(researchProjects) ) {
            citation.setResearchProjects(researchProjects.getTextTrim())
        }

        Element expocode = root.getChild("expocode")
        if ( ! isEmpty(expocode) ) {
            citation.setExpocode(expocode.getTextTrim())
        }

        Element cruiseID = root.getChild("cruiseID")
        if ( ! isEmpty(cruiseID) ) {
            citation.setCruiseId(cruiseID.getTextTrim())
        }

        Element section = root.getChild("section")
        if ( ! isEmpty(section) ) {
            citation.setSection(section.getTextTrim())
        }

        Element reference = root.getChild("reference")
        if ( ! isEmpty(reference) ) {
            citation.setScientificReferences(reference.getTextTrim())
        }

        Element citationList = root.getChild("citation")
        if ( ! isEmpty(citationList) ) {
            citation.setCitationAuthorList(citationList.getTextTrim())
        }

        Element suppleInfo = root.getChild("suppleInfo")
        if ( ! isEmpty(suppleInfo) ) {
            citation.setSupplementalInformation(suppleInfo.getTextTrim())
        }
        mdDoc.setCitation(citation)

        // Time and Location Information
        TimeAndLocation timeAndLocation = new TimeAndLocation()
        Element startdate = root.getChild("startdate")
        if ( ! isEmpty(startdate) ) {
            timeAndLocation.setStartDate(startdate.getTextTrim())
        }
        Element enddate = root.getChild("enddate")
        if ( ! isEmpty(enddate) ) {
            timeAndLocation.setEndDate(enddate.getTextTrim())
        }
        Element westbd = root.getChild("westbd")
        if ( ! isEmpty(westbd) ) {
            timeAndLocation.setWestLon(westbd.getTextTrim())
        }
        Element eastbd = root.getChild("eastbd")
        if ( ! isEmpty(eastbd) ) {
            timeAndLocation.setEastLon(eastbd.getTextTrim())
        }
        Element northbd = root.getChild("northbd")
        if ( ! isEmpty(northbd) ) {
            timeAndLocation.setNorthLat(northbd.getTextTrim())
        }
        Element southbd = root.getChild("southbd")
        if ( ! isEmpty(southbd) ) {
            timeAndLocation.setSouthLat(southbd.getTextTrim())
        }
        Element spatialReference = root.getChild("spatialReference")
        if ( ! isEmpty(spatialReference) ) {
            timeAndLocation.setSpatialRef(spatialReference.getTextTrim())
        }
        Element geographicName = root.getChild("geographicName")
        if ( ! isEmpty(geographicName) ) {
            // TODO this is wrong. We need to collect a list, either by comma separating or entering and accumulating them
            timeAndLocation.setGeoNames(geographicName.getTextTrim())
        }
        Element locationOrganism = root.getChild("locationOrganism")
        if ( ! isEmpty(locationOrganism) ) {
            timeAndLocation.setOrganismLoc(locationOrganism.getText())
        }
        mdDoc.setTimeAndLocation(timeAndLocation)

        // Funding

        List<Element> funding = root.getChildren("fundingAgency");
        List<Funding> fundingList = new ArrayList<>();
        for ( int i = 0; i < funding.size(); i++ ) {
            Element fund = funding.get(i);
            Funding finst = new Funding();
            Element agency = fund.getChild("agency")

            if ( ! isEmpty(agency) ) {
                finst.setAgencyName(agency.getTextTrim())
            }
            Element granttitle = fund.getChild("title")
            if ( ! isEmpty(granttitle) ) {
                finst.setGrantTitle(granttitle.getTextTrim())
            }
            Element ID = fund.getChild("ID")
            if ( ! isEmpty(ID) ) {
                finst.setGrantNumber(ID.getTextTrim())
            }
            fundingList.add(finst)
        }


        mdDoc.setFunding(fundingList)

        Platform platform = new Platform()
        Element platformE = root.getChild("platform")

        if (! isEmpty(platformE)) {

            Element platformName = platformE.getChild("name")
            if (! isEmpty(platformName)) {
                platform.setName(platformName.getTextTrim())
            }
            Element platformId = platformE.getChild("ID")
            if (! isEmpty(platformId)) {
                platform.setPlatformId(platformId.getTextTrim())
            }
            Element platformType = platformE.getChild("type")
            if (! isEmpty(platformType)) {

                platform.setPlatformType(platformType.getText())
            }
            Element platformOwner = platformE.getChild("owner")
            if (! isEmpty(platformOwner)) {
                platform.setOwner(platformOwner.getText())
            }
            Element platformCountry = platformE.getChild("country")
            if (! isEmpty(platformCountry)) {
                String proposedCountry = platformCountry.getTextTrim()
                String countryThreeLetter = OracleController.getThreeLetter(proposedCountry)
                if ( countryThreeLetter != null ) {
                    platform.setCountry(countryThreeLetter)
                } else {
                    platform.setCountry(proposedCountry)
                }
            }
            mdDoc.addToPlatforms(platform)
        } else {
            // Or try the "old" style if it's included
            Element oldPlatE = root.getChild("Platform")

            if (! isEmpty(oldPlatE)) {

                Element platformName = oldPlatE.getChild("PlatformName")
                if (! isEmpty(platformName)) {
                    platform.setName(platformName.getTextTrim())
                }
                Element platformId = oldPlatE.getChild("PlatformID")
                if (! isEmpty(platformId)) {
                    platform.setPlatformId(platformId.getTextTrim())
                }
                Element platformType = oldPlatE.getChild("PlatformType")
                if (! isEmpty(platformType)) {

                    platform.setPlatformType(platformType.getText())
                }
                Element platformOwner = oldPlatE.getChild("PlatformOwner")
                if (! isEmpty(platformOwner)) {
                    platform.setOwner(platformOwner.getText())
                }
                Element platformCountry = oldPlatE.getChild("PlatformCountry")
                if (! isEmpty(platformCountry)) {
                    String proposedCountry = platformCountry.getTextTrim()
                    String countryThreeLetter = OracleController.getThreeLetter(proposedCountry)
                    if ( countryThreeLetter != null ) {
                        platform.setCountry(countryThreeLetter)
                    } else {
                        platform.setCountry(proposedCountry)
                    }
                }
                mdDoc.addToPlatforms(platform)
            }
        }

        List<Element> variables = root.getChildren("variable")
        for (int i = 0; i < variables.size(); i++) {
            Element variableE = variables.get(i)

            if ( variableE.getChild("fullname") && variableE.getChild("fullname").getTextTrim().toLowerCase().equalsIgnoreCase("dissolved inorganic carbon") ) {
                Dic dic = fillVariableDomain(variableE, new Dic())
                mdDoc.setDic(dic)
            } else if ( variableE.getChild("fullname") && variableE.getChild("fullname").getTextTrim().toLowerCase().equalsIgnoreCase("ph") ) {
                Ph ph = fillVariableDomain(variableE, new Ph())
                mdDoc.setPh(ph)
            } else if ( variableE.getChild("fullname") && variableE.getChild("fullname").getTextTrim().toLowerCase().equalsIgnoreCase("pco2 (fco2) autonomous") ) {
                Pco2a p = fillVariableDomain(variableE, new Pco2a())
                mdDoc.setPco2a(p)
            } else if ( variableE.getChild("fullname") && variableE.getChild("fullname").getTextTrim().toLowerCase().equalsIgnoreCase("pco2 (fco2) discrete")) {
                Pco2d p = fillVariableDomain(variableE, new Pco2d())
                mdDoc.setPco2d(p)
            } else if ( variableE.getChild("fullname") && variableE.getChild("fullname").getTextTrim().toLowerCase().equalsIgnoreCase("total alkalinity") ) {
                Ta ta = fillVariableDomain(variableE, new Ta())
                mdDoc.setTa(ta)
            } else {
                Variable variable = fillVariableDomain(variableE, new Variable())
                mdDoc.addToVariables(variable)
            }
        }
        return mdDoc
    }

    def translateSpreadsheet(InputStream inputStream) {            // TODO: should move this elsewhere
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        PoiReader2.ConvertExcelToOADS(inputStream, baos)
        ByteArrayInputStream convertedIS = new ByteArrayInputStream(baos.toByteArray())
        return createDocumentFromLegacyXML(convertedIS)

    }

    private GenericVariable fillVariableDomain(Element varElement, GenericVariable domainVar) {

        Element fullname = varElement.getChild("fullname")
        if ( ! isEmpty(fullname) ) {
            domainVar.setFullVariableName(fullname.getTextTrim())
        }
        Element abbrev = varElement.getChild("abbrev")
        if ( ! isEmpty(abbrev) ) {
            domainVar.setAbbreviation(abbrev.getTextTrim())
        }
        Element observationType = varElement.getChild("observationType")
        if ( ! isEmpty(observationType) ) {
            domainVar.setObservationType(observationType.getTextTrim())
        }
        Element insitu = varElement.getChild("insitu")
        if ( ! isEmpty(insitu) ) {
            domainVar.setObservationDetail(insitu.getTextTrim())
        }
        Element manipulationMethod = varElement.getChild("manipulationMethod")
        if ( ! isEmpty(manipulationMethod) ) {
            domainVar.setManipulationMethod(manipulationMethod.getTextTrim())
        }
        Element unit = varElement.getChild("unit")
        if ( ! isEmpty(unit) ) {
            domainVar.setUnits(unit.getTextTrim())
        }
        Element measured = varElement.getChild("measured")
        if ( ! isEmpty(measured) ) {
            domainVar.setMeasured(measured.getTextTrim())
        }
        Element calcMethod = varElement.getChild("calcMethod")
        if ( ! isEmpty(calcMethod) ) {
            domainVar.setCalculationMethod(calcMethod.getTextTrim())
        }
        Element samplingInstrument = varElement.getChild("samplingInstrument")
        if ( ! isEmpty(samplingInstrument) ) {
            domainVar.setSamplingInstrument(samplingInstrument.getTextTrim())
        }
        Element analyzingInstrument = varElement.getChild("analyzingInstrument")
        if ( ! isEmpty(analyzingInstrument) ) {
            domainVar.setAnalyzingInstrument(analyzingInstrument.getTextTrim())
        }
        Element detailedInfo = varElement.getChild("detailedInfo")
        if ( ! isEmpty(detailedInfo) ) {
            domainVar.setDetailedInformation(detailedInfo.getTextTrim())
        }
        Element replicate = varElement.getChild("replicate")
        if ( ! isEmpty(replicate) ) {
            domainVar.setFieldReplicate(replicate.getTextTrim())
        }



        // TODO this is in two different parent elements in the example <standard> and <standardization>
        Element standard = varElement.getChild("standard")
        if ( isEmpty( standard )) {
            standard = varElement.getChild("standardization")
        }
        if ( ! isEmpty(standard) ) {
            Element technique = standard.getChild("description")
            if ( ! isEmpty(technique) ) {
                domainVar.setStandardizationTechnique(technique.getTextTrim())
            }
            Element frequency = standard.getChild("frequency")
            if ( ! isEmpty(frequency) ) {
                domainVar.setFreqencyOfStandardization(frequency.getTextTrim())
            }
            Element standardPhValues = standard.getChild("standardphvalues")
            if ( ! isEmpty(standardPhValues)) {
                domainVar.setPhStandards(standardPhValues.getTextTrim())
            }
            Element standardTemp = standard.getChild("temperatureStandardization")
            if ( ! isEmpty(standardTemp)) {
                domainVar.setTemperatureStandarization(standardTemp.getTextTrim())
            }
            Element crm = standard.getChild("crm")
            if ( ! isEmpty(crm) ) {
                Element manufacture = crm.getChild("manufacturer")
                if ( ! isEmpty(manufacture) ) {
                    domainVar.setCrmManufacture(manufacture.getTextTrim())
                }
                Element batch = crm.getChild("batch")
                if ( ! isEmpty(batch) ) {
                    domainVar.setBatchNumber(batch.getText())
                }
            }
            Element stdGas = standard.getChild("standardgas")
            if ( ! isEmpty(stdGas) ) {
                Element sgasMfc = stdGas.getChild("manufacturer")
                if ( !isEmpty( sgasMfc )) {
                    domainVar.setStandardGasManufacture(sgasMfc.getText())
                }
                Element sgasConc = stdGas.getChild("concentration")
                if ( !isEmpty( sgasConc )) {
                    domainVar.setGasConcentration(sgasConc.getText())
                }
                Element sgasUnc = stdGas.getChild("uncertainty")
                if ( !isEmpty( sgasUnc )) {
                    domainVar.setStandardGasUncertainties(sgasUnc.getText())
                }
            }
        }



        Element poison = varElement.getChild("poison")

        if ( ! isEmpty(poison) ) {
            Element poisonName = poison.getChild("poisonName")
            if ( ! isEmpty(poisonName) ) {
                domainVar.setPoison(poisonName.getTextTrim())
            }

            Element volume = poison.getChild("volume")
            if ( ! isEmpty(volume) ) {
                domainVar.setPoisonVolume(volume.getText())
            }
            Element poisonDescription = poison.getChild("correction")
            if ( ! isEmpty(poisonDescription) ) {
                domainVar.setPoisonDescription(poisonDescription.getTextTrim())
            }


    }


//
        Element uncertainty = varElement.getChild("uncertainty")
        if ( ! isEmpty(uncertainty) ) {
            domainVar.setUncertainty(uncertainty.getText())
        }
        Element flag = varElement.getChild("flag")
        if ( ! isEmpty(flag) ) {
            domainVar.setQualityFlag(flag.getText())
        }
        Element methodReference = varElement.getChild("methodReference")
        if ( ! isEmpty(methodReference) ) {
            domainVar.setReferenceMethod(methodReference.getText())
        }
        Element researcherName = varElement.getChild("researcherName")
        if ( ! isEmpty(researcherName) ) {
            domainVar.setResearcherName(researcherName.getText())
        }
        Element researcherInstitution = varElement.getChild("researcherInstitution")
        if ( ! isEmpty(researcherInstitution) ) {
            domainVar.setResearcherInstitution(researcherInstitution.getText())
        }

        Element storageMethod = varElement.getChild("storageMethod")
        if ( ! isEmpty(storageMethod) ) {
            domainVar.setStorageMethod(storageMethod.getTextTrim())
        }

        Element co2ReportTemperature = varElement.getChild("co2ReportTemperature")
        if ( ! isEmpty(co2ReportTemperature) ) {
            domainVar.setPco2Temperature(co2ReportTemperature.getTextTrim());
        }

        // 025 at what temperature was pH reported
        // <phReportTemperature>
        // TextBox pHtemperature;
        Element phReportTemperature = varElement.getChild("phReportTemperature")
        if ( ! isEmpty(phReportTemperature) ) {
            domainVar.setPhTemperature(phReportTemperature.getTextTrim())
        }

        // 026 Biological subject
        // <biologicalSubject>
        // TextBox biologicalSubject;
        Element biologicalSubject = varElement.getChild("biologicalSubject")
        if ( ! isEmpty(biologicalSubject) ) {
            domainVar.setBiologicalSubject(biologicalSubject.getTextTrim())
        }

        // 027 Cell type (open or closed)
        // <cellType>
        // ButtonDropDown cellType;
        Element cellType = varElement.getChild("cellType")
        if ( ! isEmpty(cellType) ) {
            domainVar.setCellType(cellType.getTextTrim())
        }

        // 029 Curve fitting method
        // <curveFitting>
        // TextBox curveFittingMethod;

        Element curveFitting = varElement.getChild("curveFitting")
        if ( ! isEmpty(curveFitting) ) {
            domainVar.setCurveFittingMethod(curveFitting.getTextTrim())
        }

        // 030 Depth of seawater intake
        // <DepthSeawaterIntake>
        // TextBox intakeDepth;
        Element DepthSeawaterIntake = varElement.getChild("DepthSeawaterIntake")
        if ( ! isEmpty(DepthSeawaterIntake) ) {
            domainVar.setIntakeDepth(DepthSeawaterIntake.getTextTrim())
        }



        // 032 Duration (for settlement/colonization methods)
        // <duration>
        // TextBox duration;

        Element duration = varElement.getChild("duration")
        if ( ! isEmpty(duration) ) {
            domainVar.setDuration(duration.getTextTrim())
        }

        Element equilibrator = varElement.getChild("equilibrator")
        if ( ! isEmpty(equilibrator) ) {
            Element type = equilibrator.getChild("type")
            if ( ! isEmpty(type) ) {
                domainVar.setEquilibratorType(type.getTextTrim())
            }
            Element volume = equilibrator.getChild("volume")
            if ( ! isEmpty(volume) ) {
                domainVar.setEquilibratorVolume(volume.getTextTrim())
            }
            Element vented = equilibrator.getChild("vented")
            if ( ! isEmpty(vented) ) {
                domainVar.setVented(vented.getTextTrim())
            }
            Element waterFlowRate = equilibrator.getChild("waterFlowRate")
            if ( ! isEmpty(waterFlowRate) ) {
                domainVar.setFlowRate(waterFlowRate.getTextTrim())
            }
            Element gasFlowRate = equilibrator.getChild("gasFlowRate")
            if ( ! isEmpty(gasFlowRate) ) {
                domainVar.setGasFlowRate(gasFlowRate.getTextTrim())
            }
            Element temperatureEquilibratorMethod = equilibrator.getChild("temperatureEquilibratorMethod")
            if ( ! isEmpty(temperatureEquilibratorMethod) ) {
                domainVar.setEquilibratorTemperatureMeasureMethod(temperatureEquilibratorMethod.getTextTrim())
            }
            Element pressureEquilibratorMethod = equilibrator.getChild("pressureEquilibratorMethod")
            if ( ! isEmpty(pressureEquilibratorMethod) ) {
                domainVar.setEquilibratorPressureMeasureMethod(pressureEquilibratorMethod.getTextTrim())
            }

            // 031 Drying method for CO2 gas
            // <dryMethod>
            // TextBox dryingMethod;

            Element dryMethod = equilibrator.getChild("dryMethod")
            if ( ! isEmpty(dryMethod) ) {
                domainVar.setDryingMethod(dryMethod.getTextTrim())
            }
        }

        Element headspacevol = varElement.getChild("headspacevol");
        if ( ! isEmpty(headspacevol) ) {
            domainVar.setHeadspaceVolume(headspacevol.getTextTrim())
        }

        Element lifeStage = varElement.getChild("lifeStage")
        if ( ! isEmpty(lifeStage) ) {
            domainVar.setLifeStage(lifeStage.getTextTrim())
        }

        Element locationSeawaterIntake = varElement.getChild("locationSeawaterIntake")
        if ( ! isEmpty(locationSeawaterIntake) ) {
            domainVar.setIntakeLocation(locationSeawaterIntake.getTextTrim())
        }


        Element gasDetector = varElement.getChild("gasDetector")
        if ( ! isEmpty(gasDetector) ) {
            Element manufacturer = gasDetector.getChild("manufacturer")
            if (! isEmpty(manufacturer)  ) {
                domainVar.setGasDetectorManufacture(manufacturer.getTextTrim())
            }
            Element model = gasDetector.getChild("model")
            if ( ! isEmpty(model) ) {
                domainVar.setGasDetectorModel(model.getTextTrim())
            }
            Element resolution = gasDetector.getChild("resolution")
            if ( ! isEmpty(resolution) ) {
                domainVar.setGasDectectorResolution(resolution.getTextTrim())
            }
            Element gasuncertainty = gasDetector.getChild("uncertainty")
            if ( ! isEmpty(gasuncertainty) ) {
                domainVar.setGasDectectorUncertainty(gasuncertainty.getTextTrim())
            }
        }

        Element phscale = varElement.getChild("phscale")
        if ( ! isEmpty(phscale) ) {
            domainVar.setPhScale(phscale.getTextTrim())
        }

        Element seawatervol = varElement.getChild("seawatervol")
        if ( ! isEmpty(seawatervol) ) {
            domainVar.setSeawaterVolume(seawatervol.getTextTrim())
        }

        Element speciesID = varElement.getChild("speciesID")
        if ( ! isEmpty(speciesID) ) {
            domainVar.setSpeciesIdCode(speciesID.getTextTrim())
        }

        Element temperatureCorrectionMethod = varElement.getChild("temperatureCorrectionMethod") // ph variables... sigh
        if ( ! isEmpty(temperatureCorrectionMethod) ) {
            domainVar.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getTextTrim())
        } else {
            Element temperatureCorrection = varElement.getChild("temperatureCorrection")
            if ( ! isEmpty(temperatureCorrection) ) {
                domainVar.setTemperatureCorrectionMethod(temperatureCorrection.getTextTrim())
            }
        }

        Element temperatureMeasure = varElement.getChild("temperatureMeasure")
        if ( ! isEmpty(temperatureMeasure) ) {
            domainVar.setTemperatureMeasurement(temperatureMeasure.getTextTrim())
        }

        Element titrationType = varElement.getChild("titrationType")
        if ( ! isEmpty(titrationType) ) {
            domainVar.setTitrationType(titrationType.getTextTrim())
        }

        Element waterVaporCorrection = varElement.getChild("waterVaporCorrection")
        if ( ! isEmpty(waterVaporCorrection) ) {
            domainVar.setVaporCorrection(waterVaporCorrection.getTextTrim())
        }
        // TODO set the internal variable number

        return domainVar
    }

    private Person fillPersonDomain(Element p, Person human) {
        if ( p == null ) { return null }
//        def human;
//        if ( p.getChild("role") != null && p.getChild("role").getText().equals("investigator") ) {
//            human = new Investigator()
//        } else {
//            human = new DataSubmitter()
//        }
        if ( p.getChild("name") ) {
            String name = p.getChild("name").getText().trim();
            if (name.length() > 0 ) {
                // TODO mi????
                int firstSpace = name.indexOf(' ')
                if ( firstSpace > 0 ) {
                    int lastSpace = name.lastIndexOf(' ')
                    int endFirstName = lastSpace
                    if ( firstSpace != lastSpace && lastSpace - firstSpace == 2 ) {
                        endFirstName = firstSpace
                        human.setMi(name.substring(firstSpace, lastSpace))
                    }
                    human.setFirstName(name.substring(0, endFirstName))
                    human.setLastName(name.substring(lastSpace))
                } else {
                    human.setFirstName(name)
                }
            }
            Element organization = p.getChild("organization");
            if ( ! isEmpty(organization) ) {
                human.setInstitution(organization.getTextTrim())
            }
            Element deliverypoint1 = p.getChild("deliverypoint1");
            if ( ! isEmpty(deliverypoint1) ) {
                human.setAddress1(deliverypoint1.getTextTrim())
            }
            Element deliverypoint2 = p.getChild("deliverypoint2");
            if ( ! isEmpty(deliverypoint2) ) {
                human.setAddress2(deliverypoint2.getTextTrim())
            }
            Element city = p.getChild("city");
            if ( ! isEmpty(city) ) {
                human.setCity(city.getTextTrim())
            }
            Element administrativeArea = p.getChild("administrativeArea");
            if ( ! isEmpty(administrativeArea) ) {
                human.setState(administrativeArea.getTextTrim())
            }
            Element zip = p.getChild("zip");
            if ( ! isEmpty(zip) ) {
                human.setZip(zip.getTextTrim())
            }
            Element country = p.getChild("country");
            if ( ! isEmpty(country) ) {
                String proposedCountry = country.getTextTrim();
                String countryName = OracleController.getCountryName(country.getTextTrim())
                if ( countryName !=  null ) {
                    human.setCountry(countryName)
                } else {
                    human.setCountry(proposedCountry)
                }
            }
            Element phone = p.getChild("phone");
            if ( ! isEmpty( phone )) {
                human.setTelephone(phone.getTextTrim())
            }
            Element email = p.getChild("email");
            if ( ! isEmpty(email) ) {
                human.setEmail(email.getTextTrim())
            }
            Element ID = p.getChild("ID");
            if ( ! isEmpty(ID) ) {
                human.setRid(ID.getTextTrim())
            }
            Element IDtype = p.getChild("IDtype");
            if ( ! isEmpty(IDtype) ) {
                human.setIdType(IDtype.getTextTrim())
            }
        }
        return human;
    }

    def createXml(Document doc) {

        org.jdom2.Document xmlDoc = new org.jdom2.Document();
        Element metadata = new Element("metadata");
        xmlDoc.setRootElement(metadata)

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
            if (citation.getDatasetAbstract()) {
                Element platformAbstract = new Element("abstract")
                platformAbstract.setText(citation.getDatasetAbstract())
                metadata.addContent(platformAbstract)
            }

            if (citation.getPurpose()) {
                Element purpose = new Element("purpose")
                purpose.setText(citation.getPurpose())
                metadata.addContent(purpose)
            }
            if ( citation.getResearchProjects() ) {
                Element researchProjects = new Element("researchProject")
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

        List<Funding> fundingList = doc.getFunding();
        for (int i = 0; i < fundingList.size(); i++) {
            Funding funding = fundingList.get(i)
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
                    String proposedCountry = platform.getCountry();
                    String countryName = OracleController.getCountryName(proposedCountry)
                    if ( countryName != null ) {
                        platformCountry.setText(countryName)
                    } else {
                        platformCountry.setText(proposedCountry)
                    }
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
            Element variable = fillDic(doc.getDic())
            metadata.addContent(variable)
        }
        if ( doc.getTa() ) {
            Element variable = fillTa(doc.getTa())
            metadata.addContent(variable)
        }
        if ( doc.getPh() ) {
            Element variable = fillPh(doc.getPh())
            metadata.addContent(variable)
        }
        if ( doc.getPco2a() ) {
            Element variable = fillPCO2a(doc.getPco2a())
            metadata.addContent(variable)
        }
        if ( doc.getPco2d() ) {
            Element variable = fillPCO2d(doc.getPco2d())
            metadata.addContent(variable)
        }
        if ( doc.getVariables() ) {
            for(int i = 0; i < doc.getVariables().size(); i++ ) {
                Variable v = doc.getVariables().get(i)
                Element variable = fillVariable(v, "0")
                metadata.addContent(variable)
            }
        }

        // TODO the rest of the variable stuff.
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat())
        String xml = outputter.outputString(xmlDoc)
        return xml
    }

    private Element fillDic(GenericVariable v) {
        Element element = fillVariable(v, "1");
        return element;
    }
    private Element fillTa(GenericVariable v) {
        Element element = fillVariable(v, "2");
        /*
        TA: Type of titration
        TA: Cell type (open or closed)
        TA: Curve fitting method
        TA: Magnitude of blank correction
        */
        if ( v.getTitrationType() ) {
            Element e = new Element("titrationType")
            e.setText(v.getTitrationType())
            element.addContent(e)
        }
        if ( v.getCellType() ) {
            Element e = new Element("cellType")
            e.setText(v.getCellType())
            element.addContent(e)
        }
        if ( v.getCurveFittingMethod() ) {
            Element e = new Element("curveFitting")
            e.setText(v.getCurveFittingMethod())
            element.addContent(e)
        }
        if ( v.getMagnitudeOfBlankCorrection() ) {
            Element e = new Element("blank")
            e.setText(v.getMagnitudeOfBlankCorrection())
            element.addContent(e)
        }
        return element;
    }
    private Element fillPh(GenericVariable v) {
        Element element = fillVariable(v, "3");
        /*
        pH: pH scale
        pH: Temperature of measurement
        XXX Standardization element !!! TODO: pH: pH values of the standards
        pH: Temperature correction method
        pH: at what temperature was pH reported
        */
        if ( v.getPhScale() ) {
            Element e = new Element("phscale")
            e.setText(v.getPhScale())
            element.addContent(e)
        }
        if ( v.getTemperatureMeasurement() ) {
            Element e = new Element("temperatureMeasure")
            e.setText(v.getTemperatureMeasurement())
            element.addContent(e)
        }
        if ( v.getTemperatureCorrectionMethod() ) {
            Element e = new Element("temperatureCorrectionMethod") // TODO: "temperatureCorrection" in pCO2
            e.setText(v.getTemperatureCorrectionMethod())
            element.addContent(e)
        }
        if ( v.getPhTemperature() ) {
            Element e = new Element("phReportTemperature")
            e.setText(v.getPhTemperature())
            element.addContent(e)
        }
        return element;
    }
    private Element fillPCO2a(GenericVariable v) {
        Element element = fillPCO2x(v, "4");
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
        if ( v.getIntakeLocation() ) {
            Element e = new Element("intakeLocation")
            e.setText(v.getIntakeLocation())
            element.addContent(e)
        }
        if ( v.getIntakeDepth() ) {
            Element e = new Element("intakeDepth")
            e.setText(v.getIntakeDepth())
            element.addContent(e)
        }
        Element eq = new Element("equilibrator")
        if ( v.getEquilibratorType() ) {
            Element e = new Element("equilibratorType")
            e.setText(v.getEquilibratorType())
            eq.addContent(e)
        }
        if ( v.getEquilibratorVolume() ) {
            Element e = new Element("equilibratorVolume")
            e.setText(v.getEquilibratorVolume())
            eq.addContent(e)
        }
        if ( v.getVented() ) {
            Element e = new Element("vented")
            e.setText(v.getVented())
            eq.addContent(e)
        }
        if ( v.getFlowRate() ) {
            Element e = new Element("waterFlowRate")
            e.setText(v.getFlowRate())
            eq.addContent(e)
        }
        if ( v.getGasFlowRate() ) {
            Element e = new Element("gasFlowRate")
            e.setText(v.getGasFlowRate())
            eq.addContent(e)
        }
        if ( v.getEquilibratorTemperatureMeasureMethod() ) {
            Element e = new Element("temperatureEquilibratorMethod")
            e.setText(v.getEquilibratorTemperatureMeasureMethod())
            eq.addContent(e)
        }
        if ( v.getEquilibratorPressureMeasureMethod() ) {
            Element e = new Element("pressureEquilibratorMethod")
            e.setText(v.getEquilibratorPressureMeasureMethod())
            eq.addContent(e)
        }
        if ( v.getDryingMethod() ) {
            Element e = new Element("dryMethod")
            e.setText(v.getDryingMethod())
            eq.addContent(e)
        }
        element.addContent(eq)
        return element;
    }
    private Element fillPCO2d(GenericVariable v) {
        Element element = fillPCO2x(v, "5");
        /*
        pCO2D: Storage method
        pCO2D: Seawater volume (mL)
        pCO2D: Headspace volume (mL)
        pCO2D: Temperature of measurement
        */
        if ( v.getStorageMethod() ) {
            Element e = new Element("storageMethod")
            e.setText(v.getStorageMethod())
            element.addContent(e)
        }
        if ( v.getSeawaterVolume() ) {
            Element e = new Element("seawatervol")
            e.setText(v.getSeawaterVolume())
            element.addContent(e)
        }
        if ( v.getHeadspaceVolume() ) {
            Element e = new Element("headspacevol")
            e.setText(v.getHeadspaceVolume())
            element.addContent(e)
        }
        if ( v.getTemperatureMeasurement() ) {
            Element e = new Element("temperatureMeasure")
            e.setText(v.getTemperatureMeasurement())
            element.addContent(e)
        }
        return element;
    }
    private Element fillPCO2x(GenericVariable v, String internalId) {
        Element element = fillVariable(v, internalId);
        /*
        pCO2A: Manufacturer of the gas detector
        pCO2A: Model of the gas detector
        pCO2A: Resolution of the gas detector
        pCO2A: Uncertainty of the gas detector
*/
        Element gasDetector = new Element("gasDetector")
        if ( v.getGasDetectorManufacture() ) {
            Element e = new Element("manufacture")
            e.setText(v.getGasDetectorManufacture())
            gasDetector.addContent(e)
        }
        if ( v.getGasDetectorModel() ) {
            Element e = new Element("model")
            e.setText(v.getGasDetectorModel())
            gasDetector.addContent(e)
        }
        if ( v.getGasDectectorResolution() ) {
            Element e = new Element("resolution")
            e.setText(v.getGasDectectorResolution())
            gasDetector.addContent(e)
        }
        if ( v.getGasDectectorUncertainty() ) {
            Element e = new Element("uncertainty")
            e.setText(v.getGasDectectorUncertainty())
            gasDetector.addContent(e)
        }
        element.addContent(gasDetector)
        /*
    // TODO: This is under "standardization" AOT "standard" for other vars.
        // Done in fillVariable, for some reason
        pCO2A: Manufacturer of standard gas
        pCO2A: Concentrations of standard gas
        pCO2A: Uncertainties of standard gas
        */

        /*
        pCO2A: Water vapor correction method
        pCO2A: Temperature correction method
        pCO2A: at what temperature was pCO2 reported
        */

        if ( v.getVaporCorrection() ) {
            Element e = new Element("waterVaporCorrection")
            e.setText(v.getVaporCorrection())
            element.addContent(e)
        }
        if ( v.getTemperatureCorrectionMethod() ) {
            Element e = new Element("temperatureCorrection")
            e.setText(v.getTemperatureCorrectionMethod())
            element.addContent(e)
        }
        if ( v.getPco2Temperature() ) {
            Element e = new Element("co2ReportTemperature")
            e.setText(v.getPco2Temperature())
            element.addContent(e)
        }
        return element;
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
    private Element fillVariable(GenericVariable v, String internalId) {
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
        if ( v.getObservationDetail() ) {
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
            variable.addContent(samplingInstrument)
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
        String standardName = getStandardNameFor(v)
        Element standard = new Element(standardName)
        if ( v.getStandardizationTechnique()) {
            Element description = new Element("description")
            description.setText(v.getStandardizationTechnique())
            standard.addContent(description)
        }
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
        if ( !isEmpty(crm)) {
            standard.addContent(crm)
        }
        if ( v.getStandardGasManufacture() || v.getStandardGasUncertainties() ||
             v.getGasConcentration()) {
            Element sgas = new Element("standardgas")
            if ( v.getStandardGasManufacture()) {
                Element mnf = new Element("manufacturer")
                mnf.addContent(v.getStandardGasManufacture())
                sgas.addContent(mnf)
            }
            if ( v.getStandardGasUncertainties()) {
                Element unc = new Element("uncertainty")
                unc.addContent(v.getStandardGasUncertainties())
                sgas.addContent(unc)
            }
            if ( v.getGasConcentration()) {
                Element conc = new Element("concentration")
                conc.addContent(v.getGasConcentration())
                sgas.addContent(conc)
            }
            standard.addContent(sgas)
        }
        if ( !isEmpty(standard)) {
            variable.addContent(standard)
        }
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

        Element internal = new Element("internal");
        internal.addContent(internalId);
        variable.addContent(internal);
        return variable
    }

    String getStandardNameFor(GenericVariable genericVariable) {
        return ( genericVariable instanceof Pco2a || genericVariable instanceof Pco2d ) ?
                "standardization" :
                "standard"
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
        if ( p.getCountry() ) {
            String proposedCountry = p.getCountry();
            String countryName = OracleController.getThreeLetter(proposedCountry)
            if ( countryName != null ) {
                person.addContent(new Element("country").setText(countryName))
            } else {
                person.addContent(new Element("country").setText(proposedCountry))
            }
        }
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
