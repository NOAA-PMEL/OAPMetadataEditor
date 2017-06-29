package oap

class Pco2a extends GenericVariable {
    static belongsTo = [document: Document]
    static constraints = {
        document(nullable: true)
    }
}
