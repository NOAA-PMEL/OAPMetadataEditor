package oap

class Ta extends GenericVariable {
    static belongsTo = [document: Document]
    static constraints = {
        document(nullable: true)
    }
}
