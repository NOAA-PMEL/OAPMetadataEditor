package gov.noaa.pmel.sdig.shared.bean;

/**
 * Created by rhs on 3/7/17.
 */
public class Funding {
    String agencyName;
    String grantTitle;
    String grantNumber;

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getGrantTitle() {
        return grantTitle;
    }

    public void setGrantTitle(String title) {
        this.grantTitle = title;
    }

    public String getGrantNumber() {
        return grantNumber;
    }

    public void setGrantNumber(String grantNumber) {
        this.grantNumber = grantNumber;
    }
}
