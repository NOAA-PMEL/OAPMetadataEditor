package oap

class Document {

    public static final String NOT_FOUND = "Not Found"

    String docType
    String datasetIdentifier
    String lastModified
    transient Long dbId
    transient Long dbVersion

    List<Platform> platforms
    List<Investigator> investigators
    List<Variable> variables
    List<Co2> co2vars
    List<Funding> funding

    static hasOne = [dataSubmitter: DataSubmitter,
                     citation: Citation,
                     timeAndLocation: TimeAndLocation,
                     dic: Dic,
                     pco2a: Pco2a,
                     ta: Ta,
                     ph: Ph,
                     pco2d: Pco2d,
                     docType: String]
    static hasMany = [platforms:Platform,
                      investigators: Investigator,
                      variables: Variable,
                      co2vars: Co2,
                      funding: Funding]
    static mapping = {
        platforms (cascade: 'all-delete-orphan')
        investigators (cascade: 'all-delete-orphan')
        variables (cascade: 'all-delete-orphan',lazy: false)
        co2vars (cascade: 'all-delete-orphan',lazy: false)
        dataSubmitter (cascade: 'all-delete-orphan')
        citation (cascade: 'all-delete-orphan')
        timeAndLocation (cascade: 'all-delete-orphan')
        dic (cascade: 'all-delete-orphan')
        ta (cascade: 'all-delete-orphan')
        ph (cascade: 'all-delete-orphan')
        pco2a (cascade: 'all-delete-orphan')
        pco2d (cascade: 'all-delete-orphan')
        funding (cascade: 'all-delete-orphan')
    }
    static constraints = {
        platforms (nullable: true)
        investigators (nullable: true)
        variables (nullable: true)
        co2vars (nullable: true)
        dataSubmitter (nullable: true)
        citation (nullable: true)
        timeAndLocation (nullable: true)
        dic (nullable: true)
        ta (nullable: true)
        ph (nullable: true)
        pco2a (nullable: true)
        pco2d (nullable: true)
        funding (nullable: true)
    }

    def addVariable(Variable v) {
        addToVariables(v)
    }
    def addVariable(Dic v) {
        dic = v
    }
    def addVariable(Ta v) {
        ta = v
    }
    def addVariable(Ph v) {
        ph = v
    }
    def addVariable(Pco2a v) {
        pco2a = v
    }
    def addVariable(Pco2d v) {
        pco2d = v
    }
}
