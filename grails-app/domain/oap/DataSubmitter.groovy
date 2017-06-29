package oap

class DataSubmitter extends Person {
    static belongsTo = [document:Document]
    static constraints = {
        document(nullable: true)
    }
}
