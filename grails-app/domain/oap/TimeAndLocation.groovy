package oap

class TimeAndLocation {
    static belongsTo = [document:Document]
    String startDate
    String endDate
    String northLat
    String southLat
    String westLon
    String eastLon
    boolean siteLocation
    String geoNames
    String organismLoc
    String spatialRef
    String siteSpecificLon
    String siteSpecificLat

    static constraints = {
        document (nullable: true)
        startDate (nullable: true)
        endDate (nullable: true)
        northLat (nullable: true)
        southLat (nullable: true)
        westLon (nullable: true)
        eastLon (nullable: true)
        siteLocation (nullable: false)
        geoNames (nullable: true, type: 'text')
        organismLoc (nullable: true)
        spatialRef (nullable: true)
        siteSpecificLon (nullable: true)
        siteSpecificLat (nullable: true)
    }

    static mapping = {
        siteLocation defaultValue: false
    }

    public boolean isEmpty() {
        return Document.emptyOrNull(startDate) &&
               Document.emptyOrNull(endDate) &&
               Document.emptyOrNull(northLat) &&
               Document.emptyOrNull(southLat) &&
               Document.emptyOrNull(westLon) &&
               Document.emptyOrNull(eastLon) &&
               Document.emptyOrNull(geoNames) &&
               Document.emptyOrNull(organismLoc) &&
               Document.emptyOrNull(spatialRef) &&
               Document.emptyOrNull(siteSpecificLon) &&
               Document.emptyOrNull(siteSpecificLat)
    }
}
