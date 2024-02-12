package oap

class Pco2a extends GenericVariable {
    public static final String FULL_NAME = "pco2 (fco2) autonomous"
    static belongsTo = [document: Document]
    static constraints = {
        document(nullable: true)
    }
}
