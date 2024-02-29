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
