package oap

import grails.converters.JSON

class FundingController {

    FundingService fundingService

    static scaffold = Funding

    def getGrantInfo() {
        def grant = params.id
        if ( grant ) {
            grant = URLDecoder.decode(grant, "UTF-8")
            Funding funding = fundingService.fundingInfo(grant)
            if ( funding ) {
                render funding as JSON
            } else {
                respond Document.NOT_FOUND
//                throw new FileNotFoundException(Document.NOT_FOUND)
            }
        } else {
            respond Document.NOT_FOUND
//            throw new FileNotFoundException(Document.NOT_FOUND)
        }
    }
}
