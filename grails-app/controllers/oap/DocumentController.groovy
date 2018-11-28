package oap

import grails.converters.JSON
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

import javax.servlet.http.HttpServletResponse

class DocumentController {

    XmlService xmlService
    OaMetadataFileService oaMetadataFileService

    static scaffold = Document

//    def clearAll() {
//        Document.deleteAll(Document.findAll())
//        DocumentUpdateListener.deleteAll(DocumentUpdateListener.findAll())
//    }
    def saveDoc() {
//        def sessionFactory
//        def session = sessionFactory.currentSession
        String id = params.id
        def documentJSON = request.JSON
        Document doc = new Document(documentJSON)
        System.out.println("saveDoc doc:"+doc);

        DocumentUpdateListener dul = _findDocUpldateListener(doc, id)
        System.out.println("DocListener:"+dul)
        String newId = _saveDoc(doc)
        if ( dul != null ) {
            dul.documentId = newId
            dul.documentLocation = _getDocumentLocation(newId, "saveDoc", "getXml")
            dul.save(flush:true)
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    dul.notifyListener()
                }
            };
            Timer t = new Timer();
            t.schedule(tt, 50);
            newId = dul.documentLocation
        }
        render newId
    }

    private def _findDocUpldateListener(Document doc, String id) {
//        Document savedDoc = _findSavedDoc(doc)
        String expo = _findExpocode(doc)
        System.out.println("Expocode: " + expo + ", id: " + id)
        DocumentUpdateListener dul
        if ( expo ) {
            dul = DocumentUpdateListener.findByExpoCode(expo)
        } else {
            dul = DocumentUpdateListener.findByDocumentId(id)
        }
        return dul
    }

    private def _findExpocode(Document doc) {
        String expocode = null;
        Citation c = doc.getCitation()
        if ( c ) {
            expocode = c.getExpocode()
        }
        return expocode
    }

    private def _findSavedDoc(Document doc) {
        Document savedDoc = null
        Citation c = doc.getCitation()
        if ( c ) {
            String expocode = c.getExpocode()
            if ( expocode ) {
                Citation savedCitation = Citation.findByExpocode(expocode)
                if ( savedCitation ) {
                    savedDoc = savedCitation.getDocument()
                    System.out.println("SavedDoc: "+ savedDoc)
                }
            }
        }
        return savedDoc
    }

    def _saveDoc(Document doc) {

        DateTime currently = DateTime.now(DateTimeZone.UTC)
        DateTimeFormatter format = ISODateTimeFormat.basicDateTimeNoMillis()

        String update = format.print(currently)

        doc.setLastModified(update)

        Document d
        if ( doc.validate() ) {
            Citation c = doc.getCitation()
            if ( c ) {
                String expocode = c.getExpocode()
                if ( expocode ) {
                    Citation savedCitation = Citation.findByExpocode(expocode)
                    if ( savedCitation ) {
                        Document savedVersion = savedCitation.getDocument()
                        if (savedVersion) {
                            // Out with the old and in with the new
                            savedVersion.delete(flush: true)
//                            doc.id = savedVersion.id
//                            d = doc.merge(flush:true)
////                            d = savedVersion.save(flush: true)
//                            System.out.println("Save existg: " + d)
//                            saved = true
                        }
                    }
                }
            }
            d = doc.save(flush:true)
            System.out.println("Save: " + d)
        } else {
            doc.errors.each {Error error ->
                log.debug(error.getMessage())            }
        }

        return d.id
    }

    def getDoc() {
        String id = params.id
        System.out.println("getDoc " + id)
        Document d;
        try {
            d = Document.findById(Long.parseLong(id));
        } catch (NumberFormatException nfe) {
            d = oaMetadataFileService.readOAMetadataFile(id)
        }

        String pathI = request.getPathInfo();
        String pathT = request.getPathTranslated();
        String uri = request.getRequestURI();
        String from = request.getRemoteHost();
        JSON.use("deep") {
            render d as JSON
        }
    }

// multipart/form-data; boundary=gwuu1ZDGWlhZ6XiMBo3Zx2RU-xMu1LR
    def postit() {
        String ctype = request.getContentType()
        String postedDocId = params.id
        System.out.println("posting: " + request.getRequestURL().toString() + " from " + request.getRemoteHost() + " as " + postedDocId);
        InputStream ins;
        if ( ! ctype.startsWith("multipart/form-data")) {
            // bad post
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Post Document requires Content-Type: multipart/form-data")
            return
        }
        def f = request.getPart('xmlFile')
        ins = f.getInputStream();
        // Create the document
        Document document;
        try {
            document = xmlService.createDocument(ins)
        } catch (Exception ex) {
            String msg = "Error parsing metadata XML document: " + ex.toString()
            System.out.println(msg)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg)
            return
        }
        setDocumentId(document, postedDocId)
        String docId = _saveDoc(document)
        def notificationUrlPart = request.getPart("notificationUrl")
        ins = notificationUrlPart.getInputStream()
        String notificationUrl = readStream(ins)
        String docLocation = _getDocumentLocation(docId, "postit", "getXml");
        DocumentUpdateListener dul = DocumentUpdateListener.findByExpoCode(postedDocId)
        if ( dul == null ) {
            dul = new DocumentUpdateListener()
        }
        dul.notificationUrl = notificationUrl
        dul.expoCode = postedDocId
        dul.documentId = docId
        dul.documentLocation = docLocation
        System.out.println("saving dul for expo " + dul.expoCode + " docId " + dul.documentId + ", notify: " + notificationUrl);
        dul.save(flush:true)
        render docId
    }

    private def _getDocumentLocation(String docId, String requestMethod, String accessMethod) {
        StringBuffer url = request.getRequestURL()
        String docLocation = url.substring(0, url.indexOf(requestMethod))+accessMethod+"/"+docId
        return docLocation
    }

    private def setDocumentId(Document doc, String docId) {
        Citation c = doc.getCitation()
        if (c) {
            String expocode = c.getExpocode()
            if ( expocode != null && ! expocode.trim().equals("") &&
                 ! expocode.equalsIgnoreCase(docId)) {
                // XXX TODO This should probably be an error !
                String msg = "Posted dataset ID "+docId+ " does not equal document Excocode: " + expocode
                System.out.println(msg)
            }
        } else {
            c = new Citation()
            doc.setCitation(c)
        }
        c.setExpocode(docId)
    }

    private def readStream(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        byte[] buf = new byte[4096]
        int read = 0
        while ((read = is.read(buf)) > 0 ) {
            baos.write(buf, 0, read)
        }
        return new String(baos.toByteArray())
    }

    def test() {
        String query = request.getParameter("query")
        if ( query == null ) {
            String queryString = request.getQueryString()
            if ( queryString != null ) {
                String[] parts = queryString.split("=")
                query = parts[1]
            }
        }
        if ( query == null ) {
            def queryJSON = request.JSON
            query = queryJSON
        }

        String method = request.getMethod();
        String myResponse = method + ": " + query
        response.outputStream << myResponse;
        response.outputStream.flush()
    }

    def upload() {

        def f = request.getPart('xmlFile')
        InputStream ins = f.getInputStream()

        Document document;

        try {
            String name = f.getSubmittedFileName()
            if (name.toLowerCase().endsWith(".xml")) {
                // Create the document
                document = xmlService.createDocument(ins)
            } else {
                document = xmlService.translateSpreadsheet(ins)
            }

            // Set its last modified date
            DateTime currently = DateTime.now(DateTimeZone.UTC)
            DateTimeFormatter format = ISODateTimeFormat.basicDateTimeNoMillis()
            String update = format.print(currently)
            document.setLastModified(update)

            if (!document.validate()) {
                document.errors.allErrors.each {
                    log.debug it.toString()
                }
            } else {
                log.debug("Document is valid...")
            }

            JSON.use("deep") {
                render document as JSON
            }
        } catch (Exception ex) {
            String msg = "ERROR: There was an error processing your uploaded file: "+ ex.getMessage()
            render msg
        }
    }
    def getXml() {
        String pid = params.id;
        long id  = Long.valueOf(pid)
        Document doc = Document.findById(id)
        Citation c = doc.getCitation()
        String expocode
        String filename = "oap_metadata";
        if ( c ) {
            expocode = c.getExpocode()
        }
        if ( expocode ) {
            filename = filename + "_" + expocode + ".xml"
        } else {
            filename = filename + "_" + id + ".xml"
        }
        String output = xmlService.createXml(doc);
        response.contentType = 'text/xml'
        response.outputStream << output
        response.outputStream.flush()
    }
    def xml() {
        String pid = params.id;
        long id  = Long.valueOf(pid)
        Document doc = Document.findById(id)
        Citation c = doc.getCitation()
        String expocode
        String filename = "oap_metadata";
        if ( c ) {
            expocode = c.getExpocode()
        }
        if ( expocode ) {
            filename = filename + "_" + expocode + ".xml"
        } else {
            filename = filename + "_" + id + ".xml"
        }
        String output = xmlService.createXml(doc);
        response.setHeader "Content-disposition", "attachment; filename=${filename}"
        response.contentType = 'text/xml'
        response.outputStream << output
        response.outputStream.flush()

    }
}
