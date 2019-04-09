package oap

import grails.transaction.Transactional

class OaMetadataFileService {

    XmlService xmlService

    oap.Document readOAMetadataFile(String expocode) {
        Document doc;
        File mdFile = getMetadataFile(expocode)
        if ( !mdFile.exists()) {
            doc = _createNewDocument(expocode)
        } else {
            InputStream fis = new FileInputStream(mdFile)
            doc = xmlService.createDocument(fis)
        }
        return doc
    }

    Document _createNewDocument(String expocode) {
        Document d = new Document()
        Citation c = new Citation()
        c.setExpocode(expocode)
        d.setCitation(c)
        return d
    }

    def getMetadataFile(String expocode) {
        File metadataDir = getMetadataDir(expocode)
        String mdataFileName = expocode.toUpperCase()+"_OADS.xml"
        File mdataFile = new File(metadataDir, mdataFileName)
//        if ( !mdataFile.exists()) {
//            mdataFile = new File(metadataDir, "extracted_"+mdataFileName)
//            if ( mdataFile.exists()) {
//                mdataFile = null
//            }
//        }
        return mdataFile;
    }

    File getMetadataDir(String expocode) {
        String partialDirName = expocode.substring(0, 4)
        String path = "/Users/kamb/tomcat/80/content/OAPUploadDashboard/MetadataDocs/"+partialDirName+"/"+expocode
        File dir = new File(path)
        return dir
    }
}
