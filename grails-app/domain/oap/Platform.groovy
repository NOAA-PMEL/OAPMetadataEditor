package oap

class Platform {
    static belongsTo = [document: Document]
    String name;
    String platformId;
    String country;
    String owner;
    String platformType;
    static constraints = {
        document (nullable: true)
        name(nullable: true)
        platformId(nullable: true)
        country(nullable: true)
        owner(nullable: true)
        platformType(nullable: true)
    }
}
