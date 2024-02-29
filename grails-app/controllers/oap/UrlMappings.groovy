package oap

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/document"(controller: 'document', action:'missing')
        "/document/index"(controller: 'document', action:'missing')
        "/document/create"(controller: 'document', action:'missing')
        "/document/save"(controller: 'document', action:'missing')
        "/document/show"(controller: 'document', action:'missing')
        "/document/update"(controller: 'document', action:'missing')
        "/document/delete"(controller: 'document', action:'missing')

        "/funding"(controller: 'document', action:'missing')
        "/funding/index"(controller: 'document', action:'missing')
        "/funding/create"(controller: 'document', action:'missing')
        "/funding/save"(controller: 'document', action:'missing')
        "/funding/show"(controller: 'document', action:'missing')
        "/funding/update"(controller: 'document', action:'missing')
        "/funding/delete"(controller: 'document', action:'missing')

        "/"(view:"/index")
//        "/dashboard"(view:"/dashboard")
        "500"(controller: 'document', action:  'error')
        "404"(controller: 'document', action:  'missing')

    }
}
