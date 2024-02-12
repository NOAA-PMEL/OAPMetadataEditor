package oap

class Ph extends GenericVariable {
    public static final String FULL_NAME = "pH"
    static belongsTo = [document: Document]
    static constraints = {
        document(nullable: true)
    }
}
