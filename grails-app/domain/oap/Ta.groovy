package oap

class Ta extends GenericVariable {
    public static final String FULL_NAME = "Total Alkalinity"
    static belongsTo = [document: Document]
    static constraints = {
        document(nullable: true)
    }
}
