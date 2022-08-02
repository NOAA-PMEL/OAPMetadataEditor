package gov.noaa.pmel.sdig.shared.bean;

import gov.noaa.pmel.sdig.shared.HasContent;
import gov.noaa.pmel.sdig.shared.Stringy;

/**
 * Created by rhs on 3/6/17.
 */
public class TimeAndLocation extends DbItem implements Stringy, HasContent {
    String startDate;
    String endDate;
    String northLat;
    String southLat;
    String westLon;
    String eastLon;
    boolean siteLocation;
    String geoNames;
    String organismLoc;
    String spatialRef;
    String siteSpecificLon;
    String siteSpecificLat;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getNorthLat() {
        return northLat;
    }

    public void setNorthLat(String northLat) {
        this.northLat = northLat;
    }

    public String getSouthLat() {
        return southLat;
    }

    public void setSouthLat(String southLat) {
        this.southLat = southLat;
    }

    public String getWestLon() {
        return westLon;
    }

    public void setWestLon(String westLon) {
        this.westLon = westLon;
    }

    public String getEastLon() {
        return eastLon;
    }

    public void setEastLon(String eastLon) {
        this.eastLon = eastLon;
    }

    public String getGeoNames() {
        return geoNames;
    }

    public void setGeoNames(String geoNames) {
        this.geoNames = geoNames;
    }

    public String getOrganismLoc() {
        return organismLoc;
    }

    public void setOrganismLoc(String organismLoc) {
        this.organismLoc = organismLoc;
    }

    public String getSpatialRef() {
        return spatialRef;
    }

    public void setSpatialRef(String spatialRef) {
        this.spatialRef = spatialRef;
    }

    public boolean isSiteLocation() { return siteLocation; }
    public void setSiteLocation(boolean isSiteLoc) { this.siteLocation = isSiteLoc; }

    public String getSiteSpecificLon() { return siteSpecificLon; }
    public void setSiteSpecificLon(String siteSpecificLon) {
        this.siteSpecificLon = siteSpecificLon;
    }

    public String getSiteSpecificLat() { return siteSpecificLat; }
    public void setSiteSpecificLat(String siteSpecificLat) {
        this.siteSpecificLat = siteSpecificLat;
    }

    @Override
    public TimeAndLocation sClone() {
        TimeAndLocation newt = new TimeAndLocation();
//        newt.id = this.id;
//        newt.version = this.version;
        newt.startDate = this.startDate;
        newt.endDate = this.endDate;
        newt.northLat = this.northLat;
        newt.southLat = this.southLat;
        newt.westLon = this.westLon;
        newt.eastLon = this.eastLon;
        newt.geoNames = this.geoNames;
        newt.organismLoc = this.organismLoc;
        newt.spatialRef = this.spatialRef;
        newt.siteSpecificLon = this.siteSpecificLon;
        newt.siteSpecificLat = this.siteSpecificLat;
        return newt;
    }

    @Override
    public boolean hasContent() {
        boolean hasContent = ! (
                    isEmpty(startDate) &&
                    isEmpty(endDate) &&
                    isEmpty(northLat) &&
                    isEmpty(southLat) &&
                    isEmpty(westLon) &&
                    isEmpty(eastLon) &&
                    isEmpty(geoNames) &&
                    isEmpty(organismLoc) &&
                    isEmpty(spatialRef) &&
                    isEmpty(siteSpecificLon) &&
                    isEmpty(siteSpecificLat));
        return hasContent;
    }
}
