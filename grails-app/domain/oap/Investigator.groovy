package oap

class Investigator extends Person {
    static belongsTo = [document: Document]
    static constraints = {
        document(nullable: true)
    }
}
