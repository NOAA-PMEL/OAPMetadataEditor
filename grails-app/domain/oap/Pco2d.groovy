package oap

class Pco2d extends GenericVariable {
    public static final String FULL_NAME = "pco2 (fco2) discrete"
    static belongsTo = [document: Document]
    static constraints = {
        document(nullable: true)
    }
}
