package oap

import grails.converters.JSON

class FundingController {

    FundingService fundingService

    static scaffold = Funding

    def getGrantInfo() {
        def grant = params.id
        if ( grant ) {
            Funding funding = fundingService.fundingInfo(grant)
            if ( funding ) {
                render funding as JSON
            } else {
                throw new FileNotFoundException(Document.NOT_FOUND)
            }
        } else {
            throw new FileNotFoundException(Document.NOT_FOUND)
        }
    }
}
