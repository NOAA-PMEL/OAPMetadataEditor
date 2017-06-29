package oap

class Variable extends GenericVariable {
    static belongsTo = [document: Document]
    static constraints = {
        document(nullable: true)
    }
}
