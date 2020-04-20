package gov.noaa.pmel.sdig.shared.bean;


import gov.noaa.pmel.sdig.shared.Stringy;

/**
 * Created by rhs on 3/7/17.
 */
public class Funding extends Ordered implements Comparable<Funding>, Stringy {

    String agencyName;
    String grantTitle;
    String grantNumber;

    @Override
    public int compareTo(Funding o) {
        if ( o == null ) { return 1; }
        int z = sCompare(agencyName, o.agencyName);
        if ( z != 0 ) { return z; }
        z = sCompare(grantNumber, o.grantNumber);
        if ( z != 0 ) { return z; }
        return sCompare(grantTitle, o.grantTitle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Funding funding = (Funding) o;

        if ( ! sAreTheSame(agencyName, funding.agencyName)) return false;
        if ( ! sAreTheSame(grantTitle, funding.grantTitle)) return false;
        return sAreTheSame(grantNumber,funding.grantNumber);
    }

    @Override
    public int hashCode() {
        int result = agencyName != null ? agencyName.hashCode() : 0;
        result = 31 * result + (grantTitle != null ? grantTitle.hashCode() : 0);
        result = 31 * result + (grantNumber != null ? grantNumber.hashCode() : 0);
        return result;
    }

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

    @Override
    public Funding sClone() {
        Funding newf = new Funding();
        newf.position = this.position;
//        newf.id = this.id;
//        newf.version = this.version;
        newf.agencyName = this.agencyName;
        newf.grantTitle = this.grantTitle;
        newf.grantNumber = this.grantNumber;
        return newf;
    }
}
