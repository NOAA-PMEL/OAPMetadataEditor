package oap

class Pco2d extends GenericVariable {
    static belongsTo = [document: Document]
    static constraints = {
        document(nullable: true)
    }
}
