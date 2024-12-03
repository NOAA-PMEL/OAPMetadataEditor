package oap

class Person {
    String lastName
    String mi
    String firstName
    String institution
    String address1
    String address2
    String telephone
    String extension
    String email
    String city
    String state
    String zip
    String country

    List<TypedString> researcherIds

    //static mappedBy = [document: "dataSubmitter", document: "investigators"]

    static constraints = {
        lastName (nullable: true)
        mi (nullable: true)
        firstName (nullable: true)
        institution (nullable: true)
        address1 (nullable: true)
        address2 (nullable: true)
        telephone (nullable: true)
        extension (nullable: true)
        email (nullable: true)
//        rid (nullable: true)
        city (nullable: true)
        state (nullable: true)
        country(nullable: true)
        zip (nullable: true)
//        idType (nullable: true)
        researcherIds (nullable: true)
    }
    static hasMany = [
            researcherIds : TypedString
    ]
    static mapping = {
        researcherIds(cascade: 'all-delete-orphan')
    }
}
