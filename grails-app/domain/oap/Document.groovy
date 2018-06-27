package oap

class Document {

    String lastModified;

    List<Platform> platforms
    List<Investigator> investigators
    List<Variable> variables
    List<Funding> funding;

    static hasOne = [dataSubmitter: DataSubmitter,
                     citation: Citation,
                     timeAndLocation: TimeAndLocation,
                     dic: Dic,
                     pco2a: Pco2a,
                     ta: Ta,
                     ph: Ph,
                     pco2d: Pco2d]



    static hasMany = [platforms:Platform,
                      investigators: Investigator,
                      variables: Variable,
                      funding: Funding]

    static mapping = {
        platforms (cascade: 'all-delete-orphan')
        investigators (cascade: 'all-delete-orphan')
        variables (cascade: 'all-delete-orphan')
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
}
