package oap

class TimeAndLocation {
    static belongsTo = [document:Document]
    String startDate;
    String endDate;
    String northLat;
    String southLat;
    String westLon;
    String eastLon;
    String geoNames;
    String organismLoc;
    String spatialRef;
    String siteSpecificLon;
    String siteSpecificLat;

    static constraints = {
        document (nullable: true)
        startDate (nullable: true)
        endDate (nullable: true)
        northLat (nullable: true)
        southLat (nullable: true)
        westLon (nullable: true)
        eastLon (nullable: true)
        geoNames (nullable: true, type: 'text')
        organismLoc (nullable: true)
        spatialRef (nullable: true)
        siteSpecificLon (nullable: true)
        siteSpecificLat (nullable: true)
    }
}
