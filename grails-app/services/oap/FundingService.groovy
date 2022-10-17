package oap

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory

class FundingService {


    static class FundingVocabulary<String> extends VocabularyService.DynamicVocabulary {
        Map<java.lang.String, Funding> fundingMap

        FundingVocabulary(VocabularyService.Vocabularies vocab, String fileName) {
            super(vocab, fileName)
            fundingMap = new TreeMap<>()
        }

        @Override
        void loadVocabulary() {
            synchronized (items) {
                System.out.print("loading vocabulary :" + vocabulary + " ")
                fundingMap.clear()
                Workbook workbook = null
                try {
                    InputStream sourceResource = new FileInputStream(sourceFile)
                    workbook = WorkbookFactory.create(sourceResource)
                    Sheet sheet = workbook.getSheetAt(0)

                    TreeSet<String[]> vocabItems = new TreeSet<>();
                    // read and sort items
                    int maxRows = sheet.getLastRowNum()
                    for (int rowNum = 1; rowNum < maxRows; rowNum++) {
                        Row row = sheet.getRow(rowNum)
                        Cell grantNumCell = row.getCell(3)
                        String grantNum;
                        if ( grantNumCell.getCellType().equals(CellType.NUMERIC)) {
                            Double vd = grantNumCell.getNumericCellValue()
                            Integer vi = vd.intValue()
                            grantNum = vi.toString()
                        } else {
                            grantNum = grantNumCell.getStringCellValue()
                        }
                        String title = row.getCell(4).getStringCellValue()
                        if ( ! grantNum ) {
                            continue
                        }
                        vocabItems.add(grantNum)
                        fundingMap.put(grantNum, new Funding([agencyName:"NOAA Ocean Acidification Program",
                                                          grantTitle:title,
                                                          grantNumber:grantNum]))
                    }
                    items.clear()
                    for (String item : vocabItems) {
                        items.add(item);
                    }
                    lastLoaded = new Date()
                    System.out.println(lastLoaded)
                } catch (Exception ex) {
                    ex.printStackTrace()
                } finally {
                    if ( workbook ) {
                        try { workbook.close() }
                        catch (Exception ex) {
                            log(ex)
                        }
                    }
                }
            }
        }
        void loadTextVocabulary() {
            synchronized (items) {
                System.out.print("loading vocabulary :" + vocabulary + " ")
                fundingMap.clear()
                BufferedReader reader = null
                try {
                    InputStream sourceResource = new FileInputStream(sourceFile)
                    reader = new BufferedReader(new InputStreamReader(sourceResource))
                    String line
                    TreeSet<String[]> vocabItems = new TreeSet<>();
                    // read and sort items
                    while ( (line=reader.readLine()) != null) {
                        String[] parts = line.split("[\t]")
                        String name = stdName(parts[0])
                        vocabItems.add(name)
                        fundingMap.put(name, new Funding([agencyName:"NOAA Ocean Acidification Program",
                                                           grantTitle:parts[1],
                                                           grantNumber:parts[0]]))
                    }
                    items.clear()
                    for (String item : vocabItems) {
                        items.add(item);
                    }
                    lastLoaded = new Date()
                    System.out.println(lastLoaded)
                } catch (Exception ex) {
                    ex.printStackTrace()
                } finally {
                    if ( reader ) {
                        try { reader.close() }
                        catch (Exception ex) {
                            log(ex)
                        }
                    }
                }
            }
        }
    }
    def getOapProjects() {
        return oap_fundings.getVocabularyItems()
    }
    public static String getOapProjectsSource() {
        return "content/MetadataEditor/vocabularies/projects/oap/OAP_projects.xlsx"
    }
    public static String getOapProjectsTextSource() {
        return "content/MetadataEditor/vocabularies/projects/oap/oap_projects.txt"
    }

//    def oap_fundings =
//        [
//            ["OAPFY13.03.PMEL.004","West Coast Algorithm Development: Ocean Acidification Monitoring Network","10/1/2011","9/30/2014"],
//            ["OAP1513-1002","Sustained Investment Workplan: PMEL Sustained Coastal Ocean Acidification Underway Observations (Alin)","10/1/2014","9/30/2017"],
//            ["13372","Enhancement of an existing ocean forecast system to include ocean acidification","10/1/2014","9/30/2017"],
//            ["17908","Sustained Investment Coastal Underway Ocean Acidification Observations (Alin PMEL)","10/1/2017","9/30/2020"],
//            ["OAP1834-1537","California Current Ecosystem OA Enterprise: Sustained Seasonal Forecasts of Ocean Acidification Variability in Washington and Oregon Waters (Alin, PMEL)","10/1/2017","9/30/2020"],
//            ["20847","PMEL Sustained Investment Coastal Underway Ocean Acidification Observations (PUO)","10/1/2020","9/30/2023"],
//            ["OAP 1901-1901","Quantifying coral reef net calcification capacity and vulnerability in the context of Ocean acidification","9/1/2019","12/31/2020"],
//            ["13352","East and Gulf Coast OA Observing-GOMECC-3","10/1/2014","9/30/2017"],
//            ["17928","Data acquisition and analysis from coastal OA research cruises (Barbero AOML)","10/1/2017","9/30/2020"],
//            ["20708","Data acquisition and analysis from coastal OA research cruises GOMECC-4 and ECOA-3, Coastal OA Cruises","10/1/2020","9/30/2023"],
//            ["21376","Evaluation of OA impacts to plankton and fish distributions in the Gulf of Mexico during GOMECC-4 with a focus on HAB-interactions (Short: Bio-GOMECC-4)","10/1/2020","9/30/2023"],
//            ["OAPFY15.AOML.GOMECC2","AOML FY2015-FY2017 East and Gulf Coast Ocean Acidification Observing Support: Climate quality CTD/O2 data in support of the GOMECC-­3 OA cruise (Baringer)","10/1/2014","9/30/2017"],
//            ["OAP1807-1020","NCRMP OA Enterprise: National Coral Reef Monitoring Program – Ocean Acidification Pacific (Oliver, PIFSC)","10/1/2017","9/30/2020"],
//            ["20676","National Coral Reef Monitoring Program Ocean Acidification Monitoring of Pacific US Coral Reefs","10/1/2020","9/30/2023"],
//            ["21416","Effects of pCO2, temperature, and deoxygenation stressors on juvenile and adult Euphausia pacifica: experimental assessment, enhancement of individual-based models, and connection to observational time series.","1/1/2020","9/30/2023"],
//            ["OAPFY12.02.PIFSC.001","An Interdisciplinary Approach: Ecological and Chemical Response to Ocean Acidification Across Diverse Environmental and Human Impact Gradients in the US Pacific Islands","10/1/2011","9/30/2014"],
//            ["13395","OADS-Scientific Data Integration System (OAP-SDIS)","10/1/2014","9/30/2017"],
//            ["17903","The Scientific Data Integration System SDIS (Burger PMEL)","10/1/2017","9/30/2020"],
//            ["21152","Science Data Information System: Additional Services Integration and Operationalizing","10/1/2020","9/30/2023"],
//            ["17913","Response of adult krill to global change (Busch NWFSC)","10/1/2017","9/30/2020"],
//            ["21043","Effect of CO2 and temperature on krill: Experiments and life-cycle modeling in the California Current","10/1/2020","9/30/2023"],
//            ["OAP1702-1702","PMEL FY2015–FY2017 NOAA Ocean Acidification Observing Network (NOA-ON) Sustained Investment Workplan: MARACOOS Establishing and Ocean Acidifcation Mooring Time-Series Station in Chesapeake Bay (Cai)","10/1/2014","9/30/2017"],
//            ["17918","East Coast Ocean Acidification Cruise 2018 [ECOA II] (Cai MERACOOS)","10/1/2017","9/30/2020"],
//            ["NA17OAR0170332","Establishing the traceability of pH measurements for long-term carbon system monitoring from coastal waters to open ocean","10/1/2017","9/30/2020"],
//            ["OAP1815-1702","NOA-ON Chesapeake Bay (Cai, MARACOOS)","10/1/2017","9/30/2020"],
//            ["OAPFY12.01.AFSC.006","Annual Carbon Cycles in Coastal Gulf of Alaska Water","10/1/2011","9/30/2014"],
//            ["OAPFY13.03.NEFSC.003","Resource Finfish Species and Ecosystem Impacts","10/1/2011","9/30/2014"],
//            ["13359","Testing critical population-level hypotheses regarding OA effects on early life-stages of marine fish of the northeastern USA (Chambers NEFSC)","10/1/2014","9/30/2017"],
//            ["13358","Synthesis and understanding of ocean acidification biological effects data by use of attribute- specific individual-based models (Chambers NEFSC)","10/1/2016","9/30/2017"],
//            ["17904","Testing OA Affects of Key Living Marine Resources of Northeast Continental Shelf Ecosystem (Chambers NEFSC)","10/1/2017","9/30/2020"],
//            ["NA12NOS4780145","Developing an Atlantic Sea Scallop Integrated Assessment Model","10/1/2011","8/31/2016"],
//            ["OAPFY12.03.PMEL.002","West Coast Ocean Acidification Monitoring Network: Volunteer Observing Ships","10/1/2011","9/30/2014"],
//            ["13323","Autonomous Observations of Ocean Acidification in Alaska Coastal Seas","10/1/2014","9/30/2017"],
//            ["13351","The Rise of Anthropogenic Impacts: CO2 Fluxes and Ocean Acidification in the Arctic Ocean","10/1/2014","9/30/2017"],
//            ["17929","Observations of ocean acidification in Alaska Coastal Seas - Moorings (Cross PMEL)","10/1/2017","9/30/2020"],
//            ["17930","Observations of ocean acidification in Alaska Coastal Seas - Coastal Cruise (Cross PMEL)","10/1/2017","9/30/2020"],
//            ["20781","PMEL Sustained Observations of Ocean Acidification in Alaska Coastal Seas","10/1/2020","9/30/2023"],
//            ["OAP 1902-1902","Seasonal Forecasts of Ocean Acidification in the Bering Sea","5/1/2019","12/31/2020"],
//            ["OAPFY13.03.AFSC.002","Forecast effects of ocean acidification on Alaska crab abundance","10/1/2011","9/30/2014"],
//            ["13346","Forecast effects of ocean acidification on Alaska crabs and pollock abundance (Dalton AFSC)","10/1/2014","9/30/2017"],
//            ["17924","Forecast effects of ocean acidification on Alaska crab and groundfish fisheries (Dalton AFSC)","10/1/2017","9/30/2020"],
//            ["20945","Forecast effects of ocean acidification on Alaska crab and groundfish fisheries","10/1/2020","9/30/2023"],
//            ["OAP1519-1034","CRCP FY2015-FY2017 National Coral Reef Monitoring Program –Ocean Acidification Enterprise: PacIOOS MApCO2 buoys at NCRMP Class III sites in US coral reefs (DeCarlo)","10/1/2014","9/30/2017"],
//            ["OAP1819-1034","NOA-ON Oahu NCRMP Class III (DeCarlo, PACIOOS)","10/1/2017","9/30/2020"],
//            ["OAP1101- 1101","SWFSC - Multi-frequency active-acoustic observations of zooplankton and fish from instrumented moorings in the California Current","10/1/2011","9/30/2012"],
//            ["OAP1838-1201","CIMEC CalCOFI OA Monitoring and QA/QC Analytical Support (Dickson)","10/1/2017","9/30/2020"],
//            ["17925","Effects of CO2 on salmon olfactory and magnetoreception function (Dittman NWFSC)","10/1/2017","9/30/2020"],
//            ["21032","Resiliency and sensitivity of marine fish to elevated CO2: Osmoregulatory, neurosensory, behavioral and metabolic responses in salmon and sablefish.","10/1/2020","9/30/2023"],
//            ["NA19OAR0170357","An observing system optimization study for ocean acidification along the central and northern California coast","9/1/2019","8/31/2021"],
//            ["OAP1022-1022","AFSC - Survival of calcareous Zooplankton","10/1/2011","9/30/2012"],
//            ["OAP1508-1019","CRCP FY2015-FY2017 National Coral Reef Monitoring Program –Ocean Acidification Enterprise: NCRMP-OA Atlantic (Manzello)","10/1/2014","9/30/2017"],
//            ["OAP1807-1019","NCRMP OA Enterprise: National Coral Reef Monitoring Program – Ocean Acidification Atlantic (Manzello, AOML)","10/1/2017","9/30/2020"],
//            ["17912","Lab-based bioerosion and calcification experiments (Enochs AOML)","10/1/2017","9/30/2020"],
//            ["21356","National Coral Reef Monitoring Program – Support for Annual Refurbishment of MApCO2 Buoy and Cal/Val Sampling at Class III Site in American Sāmoa","10/1/2020","9/30/2023"],
//            ["20680","National Coral Reef Monitoring Program: Ocean Acidification Monitoring of Atlantic US Coral Reefs - Caribbean","10/1/2020","9/30/2023"],
//            ["OAPFY13.03.PMEL.006","Advanced technology: Moored DIC","10/1/2011","9/30/2014"],
//            ["OAP1505-1003","PMEL FY2015–FY2017 NOAA Ocean Acidification Observing Network (NOA-ON) Sustained Investment Workplan: PMEL Sustained Ocean Acidification Large-Scale Survey Observations. (Feely)","10/1/2014","9/30/2017"],
//            ["17919","PMEL Sustained Ocean Acidification Biogeochemical and Ecological Survey Observations (Feely PMEL)","10/1/2017","9/30/2020"],
//            ["NA18NOS4780180","Thresholds in a changing ocean environment: bioeconomic implications to inform adaptation decisions for Alaska's salmon fisheries","10/1/2017","9/30/2020"],
//            ["OAPFY11.01.AFSC.003","AFSC-Alaska king crab genomics","10/1/2011","9/30/2014"],
//            ["OAPFY13.03.AFSC.001","Effects of ocean acidification on federally managed crab species in Alaska","10/1/2011","9/30/2014"],
//            ["OAP1530-1213","AFSC FY2015-FY2017 Alaska Ocean Acidification Research: Physiological response of commercially important crab species to predicted increases in pCO2 (Foy)","10/1/2014","9/30/2017"],
//            ["17926","Physiological response and acclimation potential of commercially important crab species to predicted changes in marine chemistry due to ocean acidification in Alaska. (Foy AFSC)","10/1/2017","9/30/2020"],
//            ["NA18OAR0170430","Vulnerability of the largest U.S. estuary to acidification: Implications of declining pH for shellfish hatcheries in the Chesapeake Bay","9/1/2018","8/31/2021"],
//            ["21408","Developing resilience to ocean acidification in the Pacific Northwest shellfish aquaculture industry: species diversification and mechanisms underlying parental priming","1/1/2020","9/30/2023"],
//            ["NA12NOS4780148","Using Ecological Models to Predict Population Effects of Increasing OA on Northeast US Resource Bivalves","9/1/2012","8/31/2015"],
//            ["13368","Assessing the capacity for evolutionary adaptation to ocean acidification in geoduck (Goetz NWFSC)","10/1/2014","9/30/2017"],
//            ["OAPFY13.03.PMEL.003","Ocean Acidification Monitoring Network: West Coast Hydrographic Cruise","10/1/2011","9/30/2014"],
//            ["OAPFY13.03.PRTNR.004","Ocean Acidification Monitoring and Prediction in the Oregon Coastal Waters","10/1/2011","9/30/2014"],
//            ["OAP1703-1703","PMEL FY2015–FY2017 NOAA Ocean Acidification Observing Network (NOA-ON) Sustained Investment Workplan: Ocean Acidification Monitoring and Prediction in Oregon Coastal Waters (NANOOS, Hales) ","10/1/2014","9/30/2017"],
//            ["OAP1612-1612","Multi-Scale Prediction of California Current Carbonate System Dynamics (Hales)","10/1/2016","9/30/2018"],
//            ["OAP1820-1703","NOA-ON NH-10 (Hales, NANOOS)","10/1/2017","9/30/2020"],
//            ["NEFSC FY2015-FY2017 ECM","Monitoring of Water Column DIC, TAlk, and pH on the Northeast U.S. Shelf and the Development of Ocean Acidification Indicators to Inform Marine Resource Management ","10/1/2011","9/30/2014"],
//            ["OAPFY13.03.NEFSC.005","Population and Ecosystem Modeling of Ocean Acidification on the Northeast U.S. Continental Shelf","10/1/2011","9/30/2014"],
//            ["OAPFY13.03.NEFSC.007","Henry Bigelow Ship of Opportunity Program","10/1/2011","9/30/2014"],
//            ["13436","Developing Carbon Dioxide Climatology and Ocean Acidification Indicators in the Northeastern U.S. Coastal Waters","10/1/2014","9/30/2017"],
//            ["13361","Monitoring of Water Column DIC TAlk and pH on the Northeast U.S. Shelf and the Development of Ocean Acidification Indicators to Inform Marine Resource Management","10/1/2014","9/30/2017"],
//            ["OAPFY13.03.PRTNR.007","Time-series and Underway Assessments of OA and Carbon System Properties In Coastal Waters - Mississippi Gulf Coast","10/1/2011","9/30/2014"],
//            ["OAP1704-1704","PMEL FY2015–FY2017 NOAA Ocean Acidification Observing Network (NOA-ON) Sustained Investment Workplan: GCOOS Time-series and Underway Assessments of Ocean Acidification and Carbon System Properties in Coastal Waters (Howden)","10/1/2014","9/30/2017"],
//            ["OAP1818-1704","NOA-ON Gulf of Mexico (Howden, GCOOS)","10/1/2017","9/30/2020"],
//            ["NA19OAR0170354","Ocean Acidification on a Crossroad: Enhanced Respiration, Upwelling, Increasing Atmospheric CO2, and their interactions in the northwestern Gulf of Mexico","10/1/2019","8/31/2022"],
//            ["OAPFY13.03.AFSC.004","Effects of Ocean Acidification on Early Life Stages of Alaska Fishery Resources","10/1/2011","9/30/2014"],
//            ["13327","Effects of OA on Alaskan gadids: sensitivity to variation in prey quality and behavioral responses","10/1/2014","9/30/2017"],
//            ["17927","Effects of OA on Alaskan groundfishes: identifying patterns and mechanisms of sensitivity and resiliency in physiological and behavioral performance (Hurst AFSC)","10/1/2017","9/30/2020"],
//            ["OAP-AFSC-FY20-001","Mapping OA impacts: Modeling the Multiple Action Pathways of OA effects on Alaska gadids","7/1/2019","9/30/2021"],
//            ["20903","Effects of OA on Alaskan and Arctic fishes: physiological sensitivity in a changing ecosystem","10/1/2020","9/30/2023"],
//            ["OAPFY13.03.NODC.001","Ocean Acidification Data Stewardship (OADS) Project","10/1/2011","9/30/2014"],
//            ["13394","Data Stewardship Project","10/1/2014","9/30/2017"],
//            ["17910","Scientific stewardship of the OADS project (Jiang NCEI)","10/1/2017","9/30/2020"],
//            ["OAP 1903-1903","Synthesis and Visualization of carbonate and nutrient data on North American Continental Shelves","9/1/2019","12/31/2020"],
//            ["21047","Scientific stewardship of ocean acidification data: The Ocean Carbon and Acidification Data System (OCADS)","10/1/2020","9/30/2023"],
//            ["21413","Global and regional perspectives on ocean acidification and multi-stressor extremes under climate change and mitigation","1/1/2020","9/30/2023"],
//            ["NA12NOS4780147","Vulnerability Assessment of California Current Food Webs and Economics to Ocean Acidification","9/1/2012","9/30/2014"],
//            ["OAP1813-1616","NCRMP OA Enterprise: MApCO2 buoys at NCRMP Class III sites in US coral reefs (Towle, CRCP)","10/1/2017","9/30/2020"],
//            ["13354","High-resolution ocean-biogeochemistry modeling for the East and Gulf coasts of the U.S. (Lee AOML)","10/1/2014","9/30/2017"],
//            ["17923","High-resolution modeling and mapping of the ocean acidification in the East and Gulf Coasts (Lee AOML)","10/1/2017","9/30/2020"],
//            ["20770","High-­‐resolution ocean-­‐biogeochemistry modeling for the east and gulf coast of the U.S. in support of the coastal monitoring and research objectives of the NOAA OA Program (Sang-Ki)","1/1/2020","9/30/2023"],
//            ["20900","Effects of predicted changes in ocean pCO2 and interactions with other stressors on the physiology and behavior of commercially important crabs in Alaska","10/1/2020","9/30/2023"],
//            ["21404","Using next-generation sequencing techniques to assess adaptive capacity and illuminate mechanism underlying the effects of high pCO2 on Alaskan crab and fish species","1/1/2021","9/30/2023"],
//            ["OAP1828-1020","Alaska Enterprise: Physiological response and acclimation potential of commercially important crab species to predicted changes in marine chemistry due to ocean acidification in Alaska. (Foy, AFSC)","10/1/2017","9/30/2020"],
//            ["OAPFY13.03.CRCP.002","Coral Reef Ocean Acidification Monitoring: Atlantic Test Bed","10/1/2011","9/30/2014"],
//            ["OAPFY13.03.PRTNR.005","Retrospective Analyses of CalCOFI Datasets","10/1/2011","9/30/2014"],
//            ["OAP1531-1120","NEFSC FY2015-FY2017 Ocean Acidification Research at the Northeast Fisheries Science Center: CINAR An experimental study of the impact of ocean acidification on the early life stages of sea scallops, including the interactive effects of feeding. (McCorkle","10/1/2014","9/30/2017"],
//            ["OAPFY13.03.NWFSC.001","Northwest Fisheries Science Center Ocean Acidification Research Facility","10/1/2011","9/30/2014"],
//            ["OAPFY13.03.NWFSC.002","Single species response experiments","10/1/2011","9/30/2014"],
//            ["OAPFY13.03.NWFSC.004","Modeling Ecosystem Effects of Ocean Acidification in the North Pacific","10/1/2011","9/30/2014"],
//            ["OAPFY13.03.NWFSC.005","Biological and Carbon Chemistry Sampling in Puget Sound: Variability on Scales Relevant to Zooplankton, From the Field to the Lab","10/1/2011","9/30/2014"],
//            ["OAPFY13.03.NWFSC.007","Zooplankton Response and Assemblage Experiments","10/1/2011","9/30/2014"],
//            ["13367","Zooplankton OA Exposure Modeling (McElhany NWFSC)","10/1/2014","9/30/2017"],
//            ["13370","NWFSC OA Species Exposure Experiments","10/1/2014","9/30/2017"],
//            ["OAP1536-1009","NWFSC FY2015-FY2017 Northwest Fisheries Science Center Sustained Investment Workplan: NWFSC OA Facility (McElhany)","10/1/2014","9/30/2017"],
//            ["17915","Potential pH acclimation and adaptation in Dungeness Crab (McElhany NWFSC)","10/1/2017","9/30/2020"],
//            ["17922","Evaluating the power of time series analyses to detect biological effects of OA (McElhany NWFSC)","10/1/2017","9/30/2020"],
//            ["21029","Understanding CO2 effects on Dungeness crab: Population variability, temperature interactions, calcification process, and carbonate parameter sensitivity.","10/1/2020","9/30/2023"],
//            ["21046","Development of a mixed-gas CO2 exposure system for optimizing OA experiments at the new NWFSC Mukilteo lab","10/1/2020","9/30/2023"],
//            ["NA15NOS4780186","Ocean Acidification in the California Current: Predicting Impacts on Food Webs and Economies within a Context of Climate Change","9/1/2015","8/31/2018"],
//            ["21357","Technology Refresh: Accelerating OA Sensor Development","1/1/2020","9/30/2023"],
//            ["17909","Monitoring of OA on the Northeast U.S. Shelf (Melrose NEFSC)","10/1/2017","9/30/2020"],
//            ["21411","Deployment of a prototype two-channel carbonate chemistry sensor on Northeast Fisheries Science Center Surveys to enhance monitoring of ocean acidification","1/1/2020","9/30/2023"],
//            ["21161","Measurement and Synthesis of Water Column Carbonate Chemistry Nutrients, and Biological Indicators of Ocean Acidification on the Northeast Fisheries Science Center's Ecosystems Monitoring (EcoMon) Cruises","10/1/2020","9/30/2023"],
//            ["OAPFY13.03.NEFSC.001","Effects of ocean acidification on phytoplankton physiology and nutrition for fishery-based food webs","10/1/2011","9/30/2014"],
//            ["17911","OA Growth Affects on Eastern oysters Atlantic surfclams and Atlantic sea scallops (Meseck NEFSC)","10/1/2017","9/30/2020"],
//            ["21268","Wild scallop population resilience: Using multigenerational studies to estimate robustness and adaptive potential to rapidly changing ocean acidification","10/1/2020","9/30/2023"],
//            ["OAPFY13.03.NEFSC.002","Physiological Effects of Ocean Acidification on Atlantic Surf Clams","10/1/2011","9/30/2014"],
//            ["17916","NOAA Ocean Acidification Outreach Toolkit (Mintz OAP; Francis NMS)","10/1/2017","9/30/2020"],
//            ["17917","NOAA Ocean Acidification Education Minigrant Program (Mintz OAP; Francis NMS)","10/1/2017","9/30/2020"],
//            ["NA15NOS4780185","Explaining Acidification and Nutrient Pollution in Texas Estuaries","9/1/2015","8/31/2018"],
//            ["OAP1521-1033","CARICOOS CRCP FY2015-FY2017 National Coral Reef Monitoring Program –Ocean Acidification Enterprise: NCRMP-OA La Parguera (Morelles)","10/1/2014","9/30/2017"],
//            ["OAP1817-1033","NOA-ON La Parguera (Morelles, CARICOOS)","10/1/2017","9/30/2020"],
//            ["NA18NOS4780178","Ocean and Coastal Acidification Thresholds from Long Island Sound to the Nova Scotian Shelf","9/1/2018","8/31/2022"],
//            ["OAP15.01.EPIO.002","Sustaining OA Measurements on the Washington Coast NANOOS NEMO Moorings","10/1/2011","9/30/2014"],
//            ["NA14NOS0120151","NANOOS FY2014 Marine Sensor and Other Advanced Observing Technologies Transition Project:  Turning the headlights on 'high': Improving an ocean acidification observation system in support of Pacific coast shell fish growers (Newton)","10/1/2014","3/31/2015"],
//            ["OAPFY15.PMEL.CHABA","PMEL FY2015–FY2017 NOAA Ocean Acidification Observing Network (NOA-ON) Sustained Investment Workplan: NANOOS UW OA observatories (Newton): Closing the data Gap Enhancing the Cha'ba Mooring Program to Allow Year-Round Deployments Total","10/1/2014","9/30/2017"],
//            ["17589","RVA-OA2017 The Olympic Coast as a Sentinel: An Integrated Social-Ecological Regional Vulnerability Assessment to Ocean Acidification","9/1/2017","6/30/2022"],
//            ["OAP1821-1031","NOA-ON Cha'ba (Newton, NANOOS)","10/1/2017","9/30/2020"],
//            ["13365","Metabolomic and genomic response of Dungeness crab and pteropods to ocean acidification (Nichols NWFSC)","10/1/2014","9/30/2017"],
//            ["OAPFY12.03.PRTNR.002","Service and maintenance of the Grey’s Reef Ocean Acidification Mooring: In Support of NOAA Ocean Acidification Program","10/1/2011","9/30/2014"],
//            ["OAPFY13.03.PRTNR.002","Time-series and underway assessments of OA and carbon system properties in coastal waters - Gray's Reef","10/1/2011","9/30/2014"],
//            ["OAP1715-1032","PMEL FY2015–FY2017 NOAA Ocean Acidification Observing Network (NOA-ON) Sustained Investment Workplan:  Grey's Reef OA Observatory (SECORRA, Cai, Noakes) ","10/1/2014","9/30/2017"],
//            ["OAP1816-1032","NOA-ON Grey's Reef (Noakes, Cai, SECORA)","10/1/2017","9/30/2020"],
//            ["OAPFY13.03.PMEL.005","West Coast Ocean Acidification Monitoring Network: Data Management and Product Discovery","10/1/2011","9/30/2014"],
//            ["13381","NCRMP FY2015-FY2017 National Coral Reef Monitoring Program –Ocean Acidification Enterprise:  Effects of elevated pCO2 and temperature on reef biodiversity and ecosystem functioning using Autonomous Reef Monitoring Structures (Timmers/Olenski PIFSC)","10/1/2014","9/30/2017"],
//            ["13399","Building Robust Reef Carbonate Projections from Synthesized NCRMP Ocean Acidification Datasets (Oliver PIFSC)","10/1/2014","9/30/2017"],
//            ["OAP1018-1018","NEFSC - low ph effects on an Aquaculture fish species","10/1/2010","9/30/2011"],
//            ["OAPFY13.03.NEFSC.004","Otolith Condition and Growth of Juvenile Scup, Stenotomus chrysops,and Embryonic and Larval Development in the Black Sea Bass (Centriopristis striat spp.) ","10/1/2011","9/30/2014"],
//            ["20711","Ocean acidification measurements from Ships of opportunity, SOOP-OA","10/1/2020","9/30/2023"],
//            ["21403","Expanding near-shore carbonate measurements along the East-coast and Gulf of Mexico through multiple collaborations","1/1/2021","9/30/2023"],
//            ["20780","PMEL Sustained Seasonal Forecasts of Ocean Acidification in the Bering Sea (Combined)","10/1/2020","9/30/2023"],
//            ["21269","Susceptibility of Atlantic surfclams, Spisula sp., to ocean acidification: transitioning from laboratory experiments to benthic habitat sampling and characterization","10/1/2020","9/30/2023"],
//            ["OAP1015-1015","NEFSC - Experimental facility","10/1/2009","9/30/2010"],
//            ["OAP1116-1015","NEFSC - Experimental Facility","10/1/2010","9/30/2011"],
//            ["21405","Microbial relationships to ocean acidification chemistry in the northern California Current ecosystem","1/1/2021","9/30/2023"],
//            ["NA18NOS4780177","Can meadow of underwater eelgrass help mitigate the harmful effects of Ocean Acidification on East oysters","9/1/2017","8/30/2020"],
//            ["NA19OAR0170349","Optimizing Ocean Acidification Observations for Model Parameterization in the Coupled Slope Water System of the U.S. Northeast Large Marine Ecosystem","9/1/2019","8/31/2022"],
//            ["OAP15.01.EPIO.005","Time-series Assessments of Ocean Acidification and Carbon System Properties in the Western Gulf of Maine","10/1/2011","9/30/2014"],
//            ["13383","Tracking Ocean Alkalinity Using New Carbon Technologies","10/1/2014","9/30/2017"],
//            ["OAP1715-1033","PMEL FY2015–FY2017 NOAA Ocean Acidification Observing Network (NOA-ON) Sustained Investment Workplan:NERACOOS Synthesis of time-series CO2 observations from the Gray's Reef and Gulf of Maine moorings (Salisbury) - ECOA Build Out","10/1/2014","9/30/2017"],
//            ["OAP1814-1028","NOA-ON Gulf of Maine (Salisbury, NERACOOS)","10/1/2017","9/30/2020"],
//            ["OAP1812-1527","East Coast Ocean Acidification Cruise (ECOA)[\$560K Total Extramural] - Extramural (Salisbury)","10/1/2017","9/30/2020"],
//            ["NA17RJ1231","NOAA Award NA17RJ1231, The Joint Institute for Marine Observations (JIMO), Project Title: Moored Carbon Biogeochemical and Ecosystem Observations in the Southern California Current","10/1/2009","6/30/2011"],
//            ["NA10OAR4320156","NOAA Award NA10OAR4320156, The Cooperative Institute For Marine Ecosystems and Climate (CIMEC), Project Title: Moored Carbon, Biogeochemical, and Ecosystem Observations in the Southern California Current","10/1/2010","6/30/2015"],
//            ["OAP1517-1027","PMEL FY2015–FY2017 NOAA Ocean Acidification Observing Network (NOA-ON) Sustained Investment Workplan: CIMEC Moored Carbon, Biogeochemical, and Ecosystem Observations in the Southern California Current (Send) ","10/1/2014","9/30/2017"],
//            ["NA15OAR4320071","NOAA Award NA15OAR4320071, The Cooperative Institute For Marine Ecosystems and Climate (CIMEC), Project Title: CCE Moorings: Moored Carbon, Biogeochemical, and Ecosystem Observations in the Southern California Current”","7/1/2015","6/30/2020"],
//            ["NA20OAR4320278","NOAA Award NA20OAR4320278, Cooperative Institute for Marine, Earth and Atmospheric Systems (CIMEAS), Project Title: CCE Moorings: Moored Carbon, Biogeochemical, and Ecosystem Observations in the Southern California Current”","7/1/2020","6/30/2022"],
//            ["17905","Seasonal Forecasts of Ocean Acidification Variability in Washington and Oregon Waters (Siedlecki JISAO/PMEL)","10/1/2017","9/30/2020"],
//            ["NA19OAR0170351","Assessment of the Observing Network to Identify Processes Relevant to the Predictability of the Coastal Ocean of the Northeast on Centennial Time Scales","9/1/2019","8/31/2021"],
//            ["20842","PMEL Sustained Seasonal Forecasts of Ocean Acidification Variability in Washington and Oregon Waters (PFS)","10/1/2020","9/30/2023"],
//            ["OAPFY13.03.AFSC.005","Calcium carbonate mineralogy of Alaskan corals","10/1/2011","9/30/2014"],
//            ["13350","Physiological response of the red tree coral (Primnoa pacifica) to low pH scenarios in the laboratory","10/1/2014","9/30/2017"],
//            ["OAPFY13.03.PMEL.001","West Coast Ocean Acidification Monitoring Network: Moorings Operations","10/1/2011","9/30/2014"],
//            ["13371","Developing innovative tools to connect stakeholders with NOAA's Ocean Acidification Observing Network","10/1/2014","9/30/2017"],
//            ["13375","Evaluation of New Subsurface Carbon Technologies for OA Moorings","10/1/2014","9/30/2017"],
//            ["13430","MARACOOS Establishing and Ocean Acidifcation Mooring Time-Series Station in Chesapeake Bay (Sutton PMEL)","10/1/2014","9/30/2017"],
//            ["13432","PMEL Data Management Quality Control Access and Products","10/1/2014","9/30/2017"],
//            ["OAP1506-1001","PMEL FY2015–FY2017 NOAA Ocean Acidification Observing Network (NOA-ON) Sustained Investment Workplan: PMEL Sustained Ocean Acidification Mooring Observations (Sutton)","10/1/2014","9/30/2017"],
//            ["17906","Sustained Ocean Acidification Mooring Observations (Sutton PMEL)","10/1/2017","9/30/2020"],
//            ["17907","Sustained Ocean Acidification Data Management Quality Control Access and Products (Sutton PMEL)","10/1/2017","9/30/2020"],
//            ["17914","OA Mooring Test-beds (Sutton PMEL)","10/1/2017","9/30/2020"],
//            ["OAP 1904-1904","Maintaining and expanding NOA-ON moored time-series product","5/1/2019","12/31/2020"],
//            ["20845","PMEL Sustained OA Data Management (PDM)","10/1/2020","9/30/2023"],
//            ["20894","PMEL OA Mooring Test-beds and Sensor Development : Evaluating and Expanding New Carbon Technologies to Subsurface Habitats","10/1/2020","9/30/2023"],
//            ["20895","PMEL Sustained Ocean Acidification Mooring Observations (PMO)","10/1/2020","9/30/2023"],
//            ["NA15NOS4780184","Interactions between ocean acidification and eutrophication in estuaries: Modeling opportunities and limitations for shellfish restoration","9/1/2015","8/31/2019"],
//            ["NA18NOS4780179","Identifying thresholds in coupled biogeochemical-biological-economic systems under multiple stressors","9/1/2018","8/30/2022"],
//            ["21392","Assessing ecosystem responses of Gulf of Mexico coastal communities to OA using environmental DNA (Short: eDNA-GOMECC-4)","1/1/2021","9/30/2023"],
//            ["21415","An Integrated Physical-Chemical-Biological Ocean Acidification Monitoring Program in the Southern California Current Ecosystem","1/1/2021","9/30/2023"],
//            ["13589","National Coral Reef Monitoring Program - Ocean Acidification Enterprise","10/1/2015","9/30/2017"],
//            ["17921","MApCO2 buoys at NCRMP Class III sites in US coral reefs (Towle CRCP)","10/1/2017","9/30/2020"],
//            ["NA17OAR0170164","Low pH in coastal waters of the Gulf of Maine: A data synthesis-driven investigation of probable sources, patterns and processes involved","9/1/2017","8/30/2019"],
//            ["13385","Ocean Acidification Products for the Gulf of Mexico and East Coast","10/1/2014","9/30/2017"],
//            ["OAP-AOML-FY18-20-001","High-resolution modeling and mapping of the ocean acidification in the East and Gulf Coasts","10/1/2017","9/30/2020"],
//            ["OAPFY13.03.AOML.001","Coastal Observations on the East Coast: Ocean Acidification Monitoring Network","10/1/2011","9/30/2014"],
//            ["13353","East and Gulf Coast OA Observing-SOOP OA","10/1/2014","9/30/2017"],
//            ["OAP1527-1114","AOML FY2015-FY2017 East and Gulf Coast Ocean Acidification Observing Support: Eastcoast Ocean Acidificatio Cruise FY15 [ECOA] (Wanninkhof)","10/1/2014","9/30/2017"],
//            ["17920","Surface OA observations on Ships of Opportunity SOOP-OA (Wanninkhof AOML)","10/1/2017","9/30/2020"],
//            ["18969","Synthesizing the Impacts of Ocean Acidification on Coral Reef Ecosystem Goods and Services for Future Vulnerability Assessments using an Atlantis Ecosystem Model","10/1/2017","9/30/2020"],
//            ["21366","Assessing Ocean Acidification as a Driver for Enhanced Metals Uptake by Blue Mussels (Mytilus edulis): Implications for aquaculture and seafood safety","1/1/2021","9/30/2023"],
//            ["OAPFY12.02.NEFSC.006","Calcium Carbonate Measurements and Experiments","10/1/2011","9/30/2014"],
//            ["NA17OAR0170165","Vulnerability and adaptation to OA in the Pacific Northwest mussel and oyster stakeholders","9/1/2017","8/30/2019"],
//            ["OAPFY15.AOML.ECOA","AOML FY2015-FY2017 East and Gulf Coast Ocean Acidification Observing Support: Eastcoast Ocean Acidification Cruise FY15 [ECOA] (Salisbury, NERACOOS) ","10/1/2014","9/30/2017"]
//        ]


    FundingVocabulary oap_fundings

    FundingService() {
        oap_fundings = new FundingVocabulary(VocabularyService.Vocabularies.PROJECTS, getOapProjectsSource());
    }

    private Map<String, Funding> buildMap() {
        Map<String, Funding> map = new TreeMap<>()
        for (String[] fundingInfo : oap_fundings.getVocabularyItems()) {
            map.put(fundingInfo[0], new Funding([agencyName:"NOAA Ocean Acidification Program",grantTitle:fundingInfo[1],grantNumber:fundingInfo[0]]))
        }
        return map
    }

    Set<String> fundingSources() {
        return oap_fundings.getVocabularyItems()
    }

    def fundingInfo(String grantNum) {
        Funding funding = oap_fundings.getFundingMap().get(grantNum)
        return funding
    }
}
