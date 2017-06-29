package oap

class Funding {
    static belongsTo = [document: Document]
    String agencyName;
    String grantTitle;
    String grantNumber;
    static constraints = {
        document (nullable: true)
        agencyName (nullable: true)
        grantNumber (nullable: true)
        grantTitle (nullable: true, type: 'text')
    }
}
