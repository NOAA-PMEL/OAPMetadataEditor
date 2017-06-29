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

    static constraints = {
        document (nullable: true)
        startDate (nullable: true)
        endDate (nullable: true)
        northLat (nullable: true)
        southLat (nullable: true)
        westLon (nullable: true)
        eastLon (nullable: true)
        geoNames (nullable: true)
        organismLoc (nullable: true)
        spatialRef (nullable: true)
    }
}
