package oap

class Ph extends GenericVariable {
    static belongsTo = [document: Document]
    static constraints = {
        document(nullable: true)
    }
}
