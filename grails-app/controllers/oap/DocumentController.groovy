package oap

import grails.converters.JSON
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

class DocumentController {

    XmlService xmlService

    static scaffold = Document

    def saveDoc() {

        def documentJSON = request.JSON
        Document doc = new Document(documentJSON)

        DateTime currently = DateTime.now(DateTimeZone.UTC)
        DateTimeFormatter format = ISODateTimeFormat.basicDateTimeNoMillis()

        String update = format.print(currently)

        doc.setLastModified(update)

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
                        }
                    }
                }
            }
            doc.save(flush:true)

        } else {
            doc.errors.each {Error error ->
                log.debug(error.getMessage())            }
        }

        render doc.id




    }
    def upload() {

        def f = request.getPart('xmlFile')

        // Create the document
        Document document = xmlService.createDocument(f)
        // Set its last modified date
        DateTime currently = DateTime.now(DateTimeZone.UTC)
        DateTimeFormatter format = ISODateTimeFormat.basicDateTimeNoMillis()
        String update = format.print(currently)
        document.setLastModified(update)

        if ( !document.validate() ) {
            document.errors.allErrors.each {
                log.debug it.toString()
            }
        } else {
            log.debug("Document is valid...")
        }

        JSON.use("deep") {
            render document as JSON
        }
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
