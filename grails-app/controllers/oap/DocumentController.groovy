package oap


import grails.converters.JSON
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

import javax.servlet.http.HttpServletResponse
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import java.nio.charset.Charset
import java.util.stream.Collectors

class DocumentController {

    UtilsService utilsService
    XmlService xmlService
    OadsXmlService oadsXmlService
    Oads2sXmlService oads2sXmlService

    static scaffold = Document

//    def clearAll() {
//        Document.deleteAll(Document.findAll())
//        DocumentUpdateListener.deleteAll(DocumentUpdateListener.findAll())
//    }

    def error() {
        log.warn("error:" + request.requestURI)
        response.sendError(500, "")
    }

    def missing() {
        log.info("not found:" + request.requestURI)
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "")
    }

    private def Document findDocById(String id) {
        Document doc
        try {
            try {
                long lid = Long.valueOf(id)
                doc = Document.findById(lid)
            } catch (NumberFormatException nfe) {
                doc = Document.findByDatasetIdentifier(id)
            }
            if ( ! doc ) {
                Citation savedCitation = Citation.findByExpocode(id)
                if ( savedCitation ) {
                    doc = savedCitation.getDocument()
                }
                log.warn("Found doc by expocode: "+ id + ":" + doc + ( doc ? " : " + doc.id : ""))
            }
            log.info("Found doc " + doc + ( doc ? " : " + doc.id : ""))
            if ( doc ) {
                doc.dbId = doc.id
                doc.dbVersion = doc.version
            }
        } catch (Exception ex) {
            ex.printStackTrace()
        }
        return doc
    }
    def saveDoc() {
//        def sessionFactory
//        def session = sessionFactory.currentSession
        String id = params.id
        if ( id != null && "null".equals(id)) {
            id = null
        }
        log.info("saveDoc id " + id)
        def documentJSON = request.JSON
        // part of trying to do proper database updates.  Abandoned...
//        removeNullIds(documentJSON)
        Document doc = new Document(documentJSON)
        if ( ! doc.datasetIdentifier ) {
            def datasetId = _getDocumentIdentifier(id, doc)
            if ( doc.id ) {
                log.warn("Document with id " + doc.id + " has no datasetIdentifier! " +
                         "Setting datasetIdentifier to " + datasetId + ", which will cause an update!")
            }
            doc.datasetIdentifier = datasetId
        }
        if ( ! doc.id && documentJSON.containsKey("id") && documentJSON.get("id") != null ) {
            def jsonId = documentJSON.get("id")
            try {
                doc.setId(new Long(String.valueOf(jsonId)))
                if ( documentJSON.containsKey("dbVersion")) {
                    def jsonVers = documentJSON.get("dbVersion")
                    try { doc.setVersion(new Long(String.valueOf(jsonVers)))}
                    catch (Exception ex) {
                        log.warn("Bad db version:"+jsonVers)
                    }
                }
            } catch (NumberFormatException nfe) {
                log.warn("Bad db id:"+jsonId)
            }
        }
        log.info("saveDoc doc:"+doc + " as " + doc.datasetIdentifier + " at " + System.currentTimeMillis())

        DocumentUpdateListener dul = _findDocUpdateListener(doc.datasetIdentifier)
        log.info("DocListener:"+dul)
        def savedDoc = _saveDoc(doc)
        String newId = savedDoc.datasetIdentifier // String.valueOf(savedDoc.id)
        String documentLocation = _getDocumentLocation(savedDoc, "saveDoc", "getXml")
        if ( dul != null ) {
            dul.documentId = newId
            dul.documentLocation = documentLocation
            log.info("doc location:"+dul.documentLocation)
            dul.save(flush:true)
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    dul.notifyListener(oadsXmlService.createOadsXml(doc))
                }
            };
            Timer t = new Timer();
            t.schedule(tt, 50);
        }
        render documentLocation
    }

    private _saveDoc(Document doc, boolean removePrior = true) {

        def datasetId = doc.datasetIdentifier
        DateTime currently = DateTime.now(DateTimeZone.UTC)
        DateTimeFormatter format = ISODateTimeFormat.basicDateTimeNoMillis()

        String update = format.print(currently)

        doc.setLastModified(update)

        Document d
        if ( doc.validate() ) {
            try {
                if ( removePrior ) {
                    List<Document> prior = Document.findAllByDatasetIdentifier(datasetId)
                    if ( prior && ! prior.isEmpty()) {
                        log.debug("Found previous documents " + prior + " found for id " + datasetId)
                        Document.deleteAll(prior)
                    }
//                    Document savedVersion = Document.findByDatasetIdentifier(datasetId)
//                    if (savedVersion) {
//                        log.debug("Found previous document found for id " + datasetId)
//                        savedVersion.delete(flush: true)
//                    d = doc.merge(flush: true, failOnError: true)
                }
                d = doc.save(flush: true, failOnError: true)
                d.dbId = d.id
                d.dbVersion = d.version
                log.debug("Save: " + d)
            } catch (Throwable t) {
                log.warn("Error saving document " + doc)
                t.printStackTrace()
                throw t;
            }
        } else {
            doc.errors.each {Error error -> log.info(error.getMessage())            }
        }
        return d
    }

    private def _findDocUpdateListener(String datasetId) {
        DocumentUpdateListener dul
        dul = DocumentUpdateListener.findByDocumentId(datasetId)
        return dul
    }

    private def _getDocumentIdentifier(String id, Document doc) {
        def datasetId
        if ( id ) {
            datasetId = id
        } else {
//            String expocode = _findExpocode(doc)
//            if ( expocode ) {
//                datasetId = expocode
//            } else {
                datasetId = _generateDatasetId(doc)
//            }
        }
        return  datasetId
    }
    private def _findExpocode(Document doc) {
        String expocode = null
        Citation c = doc.getCitation()
        if ( c ) {
            expocode = c.getExpocode()
        }
        return expocode
    }
    private synchronized def _generateDatasetId(Document doc) {
        return utilsService.idToShortURL(System.currentTimeMillis())
    }

    def getDoc() {
        String id = params.id
        log.info("getDoc " + id + " at " + System.currentTimeMillis())
        Document d = findDocById(id)
//        if ( d == null ) {
//            d = oaMetadataFileService.readOAMetadataFile(id)
//        }
        if ( d && ! d.datasetIdentifier ) {
            d.datasetIdentifier = id
        }
        String pathI = request.getPathInfo()
        String pathT = request.getPathTranslated()
        String uri = request.getRequestURI()
        String from = request.getRemoteHost()
        if ( d ) {
            d.dbId = d.id
            d.dbVersion = d.version
            JSON.use("deep") {
                render d as JSON
            }
        } else {
            log.info("No document found for id : " + id)
            throw new Exception("No document found for id:"+id)
//            render "Document not found."
        }
    }

    def _delete() {
        String id = params.id
        log.info("delete " + id + " at " + System.currentTimeMillis())
        Document d = findDocById(id)
        log.info("Found doc: "+ d)
        if ( d ) {
            try {
                d.delete()
                respond "deleted " + id
            } catch (Exception ex) {
                ex.printStackTrace()
                respond "There was an error deleting the document: " + ex.getMessage()
            }
        }
        else {
            respond id + " not found"
        }
    }

// multipart/form-data; boundary=gwuu1ZDGWlhZ6XiMBo3Zx2RU-xMu1LR
    def postit() {
        String ctype = request.getContentType()
        String postedDocId = params.id
        log.info("posting: " + request.getRequestURL().toString() + " from " + request.getRemoteHost() + " as " + postedDocId)
        InputStream ins;
        if ( ! ctype.startsWith("multipart/form-data")) {
            // bad post
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Post Document requires Content-Type: multipart/form-data")
            return
        }
        def f = request.getPart('xmlFile')
        ins = f?.getInputStream();
        // Create the document
        Document document;
        try {
            document = createDocumentFromXml(ins)
        } catch (Exception ex) {
            String msg = "Error parsing metadata XML document: " + ex.toString()
            log.warn(msg, ex)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg)
            return
        }
        document.datasetIdentifier = postedDocId
        Document savedDoc = _saveDoc(document, true)
        def notificationUrlPart = request.getPart("notificationUrl")
        ins = notificationUrlPart.getInputStream()
        String notificationUrl = readStream(ins)
        String docLocation = _getDocumentLocation(savedDoc, "postit", "getXml");
        DocumentUpdateListener dul = DocumentUpdateListener.findByDocumentId(postedDocId)
        if ( dul == null ) {
            dul = new DocumentUpdateListener()
        }
        dul.notificationUrl = notificationUrl
        dul.documentId = savedDoc.datasetIdentifier ? savedDoc.datasetIdentifier : savedDoc.id
        dul.documentLocation = docLocation
        log.info("saving dul for docId " + dul.documentId + ", notify: " + notificationUrl);
        dul.save(flush:true)
        render docLocation
    }

    private def _getDocumentLocation(Document doc, String requestMethod, String accessMethod) {
        String url = request.getRequestURL().toString()
        log.info("docUrl request:"+url);
        if ( url.indexOf("www.pmel") > 0 ) {
            int contextIdx = url.indexOf(".gov") + 3
            url = "https://www.pmel.noaa.gov" + url.substring(contextIdx) // url.indexOf("/sdig"))
            log.info("docUrl revised:"+url)
        }

        String docId = doc.datasetIdentifier ? doc.datasetIdentifier : doc.id
        String docLocation = url.substring(0, url.indexOf(requestMethod))+accessMethod+"/"+docId
        log.info("doc location:"+docLocation)
        return docLocation
    }

    private revise(String url, String from, String to) {
        System.out.println("Url: " + url);
        int idx1 = url.indexOf(":") + 3;
        int idx2 = url.indexOf('/', idx1);
        int idx3 = url.indexOf(from);
        String base = url.substring(0, idx3);
        String revised = base + to;
        return revised;
    }

    private def setDocumentExpocode(Document doc, String docId) {
        Citation c = doc.getCitation()
        if (c) {
            String expocode = c.getExpocode()
            if ( expocode != null && ! expocode.trim().equals("") &&
                 ! expocode.equalsIgnoreCase(docId)) {
                // XXX TODO This should probably be an error !
                String msg = "Posted dataset ID "+docId+ " does not equal document Excocode: " + expocode
                log.warn(msg)
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

    private def _createXDoc(String xml) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance()
        log.debug("dbf:"+dbf.getClass().getName());
        dbf.setNamespaceAware(true)

        // Prevent XXE attacks
        dbf.setXIncludeAware(false)
//        dbf.setExpandEntityReferences(false)  // XXX ??? TODO: not sure about this, Will it break anything?
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false)
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false)
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)

//        dbf.setFeature(XMLConstants.ACCESS_EXTERNAL_DTD, false)
        DocumentBuilder db = dbf.newDocumentBuilder()
        org.w3c.dom.Document doc = db.parse(new ByteArrayInputStream(xml.bytes))
        return doc
    }
    private def createDocumentFromXml(InputStream inStream) {
        log.debug("Creating xml document")
        String xml = new BufferedReader(new InputStreamReader(inStream))
                .lines().parallel().collect(Collectors.joining("\n"));
        org.w3c.dom.Document xdoc = _createXDoc(xml)
        org.w3c.dom.Element rootElem = xdoc.getDocumentElement()
        String version = rootElem.getAttribute("version")
        if (version) {
            return oadsXmlService.createMetadataDocumentFromVersionedXml(xdoc, version)
        } else {
            return xmlService.createDocumentFromLegacyXML(new ByteArrayInputStream(xml.bytes))
        }
    }

    def upload() {
        def datasetId = params.id

        log.info("uploading: " + request.getRequestURL().toString() + " from " + request.getRemoteHost() + " as " + datasetId)
        String ua = request.getHeader("User-Agent")
        log.info("Agent: " + ua)

        def f = request.getPart('xmlFile')
        if ( !f ) {
            throw new IllegalStateException("Uploaded file not received.")
        }
        File stashed = utilsService.stash(f)
        InputStream ins = new FileInputStream(stashed)

        Document document;

        try {
            String name = f.getSubmittedFileName()
            log.info("uploading: " + name)

            if (name.toLowerCase().endsWith(".xml")) {
                document = createDocumentFromXml(ins)
            } else {
                Charset charset = ua.toLowerCase().contains("mac") ?
                                    Charset.defaultCharset() :
                                    Charset.forName("windows-1258")
                document = translateSpreadsheet(ins, charset);
            }

            if ( document ) {
                if ( ! datasetId ) {
                    datasetId = _findExpocode(document)
                }
                document.datasetIdentifier = datasetId
            }
            // Set its last modified date
            DateTime currently = DateTime.now(DateTimeZone.UTC)
            DateTimeFormatter format = ISODateTimeFormat.basicDateTimeNoMillis()
            String update = format.print(currently)
            document.setLastModified(update)

//            if (!document.validate()) {
//                document.errors.allErrors.each {
//                    log.debug it.toString()
//                }
//            } else {
//                log.debug("Document is valid...")
//                _saveDoc(document, true)
//            }

            JSON.use("deep") {
                render document as JSON
            }
        } catch (Exception ex) {
            String msg = "ERROR: There was an error processing your uploaded file: "+ ex.getMessage()
            log.warn(msg,ex)
            render msg
        }
    }

    private translateSpreadsheet(InputStream inputStream, Charset charset) {            // TODO: should move this elsewhere
        Document doc = oads2sXmlService.translateSpreadsheet(inputStream, charset)
        return doc
    }

    /**
     * @return an XML represntation of the metadata
     */
    def getXml() {
        String docId = params.id;
        Document doc = findDocById(docId)
        if ( ! doc ) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Document not found for id " + docId)
            return;
        }
        String output = oadsXmlService.createXml(doc);
        response.contentType = 'text/xml'
        response.outputStream << output
        response.outputStream.flush()
    }
    /**
     * @return the metadata document as an XML file
     */
    def xml() {
        try {
            String pid = params.id
            String version = params.v
            Document doc = findDocById(pid)
            if ( ! doc ) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Document not found for id " + pid)
                return;
            }
            String docId = doc.datasetIdentifier ? doc.datasetIdentifier : pid
            String filename = "oap_metadata_" + docId + ".xml"
            String output
            if ( version && "ocads".equalsIgnoreCase(version)) {
                output = xmlService.createXml(doc);
            } else {
                output = oadsXmlService.createXml(doc);
            }
            response.setHeader "Content-disposition", "attachment; filename=${filename}"
            response.contentType = 'text/xml'
            response.outputStream << output
            response.outputStream.flush()
        } catch (Throwable t) {
            t.printStackTrace()
            response.sendError(500, "There was an error on the server. Please try again later.")
        }
    }
    /**
     * @return a preview of the metadata produced by a version of the NCEI xsl template
     */
    def preview() {
        try {
            String pid = params.id;
            Document doc = Document.findByDatasetIdentifier(pid)
            ByteArrayOutputStream baos = new ByteArrayOutputStream()
            oadsXmlService.transformDoc(doc, baos);
            String output = new String(baos.toByteArray())
            response.contentType = 'text/html'
            response.outputStream << output
            response.outputStream.flush()
        } catch (Throwable throwable) {
            throwable.printStackTrace()
        }
    }

    // Part of trying to do proper database updates.
//    def void removeNullIds(JSONElement json) {
//        if ( json instanceof JSONArray ) {
//            for ( JSONObject v : json ) {
//               removeNullIds(v)
//            }
//        } else if ( json instanceof JSONObject ) {
//            def removeSet = new TreeSet()
//            def keySet = json.keySet()
//            for (String key : keySet) {
//                def val = json.get(key)
//                if ( val == null ) {
//                    removeSet.add(key)
//                } else if ( val instanceof JSONElement ) {
//                    removeNullIds(val)
//                } else {
//                    System.out.println("What have we in here: "+ val + ":" + val.getClass())
//                }
//            }
//            for ( String key : removeSet ) {
//                json.remove(key)
//            }
//        } else {
//            System.out.println("What have we out here: "+ json)
//        }
//    }

    def boolean nullValue(Object o) {
        return o == null || String.valueOf(o).equalsIgnoreCase("null")
    }
}
