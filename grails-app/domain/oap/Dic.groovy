package oap

class Dic extends GenericVariable {
    public static final String FULL_NAME = "Dissolved Inorganic Carbon"
    static belongsTo = [document: Document]
    static constraints = {
        document(nullable: true)
    }
}
