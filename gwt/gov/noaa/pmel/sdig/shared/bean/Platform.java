package gov.noaa.pmel.sdig.shared.bean;

import gov.noaa.pmel.sdig.shared.Stringy;

/**
 * Created by rhs on 3/7/17.
 */
public class Platform extends Ordered implements Comparable<Platform>, Stringy {
    String name;
    String platformId;
    String country;
    String owner;
    String platformType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    @Override
    public Platform sClone() {
        Platform newp = new Platform();
        newp.position = this.position;
//        newp.id = this.id;
//        newp.version = this.version;
        newp.name = this.name;
        newp.platformId = this.platformId;
        newp.country = this.country;
        newp.owner = this.owner;
        newp.platformType = this.platformType;
        return newp;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Platform platform = (Platform) o;

        if ( ! sAreTheSame(name, platform.name)) return false;
        if ( ! sAreTheSame(country, platform.country)) return false;
        if ( ! sAreTheSame(owner, platform.owner)) return false;
        if ( ! sAreTheSame(platformId, platform.platformId)) return false;
        return sAreTheSame(platformType,platform.platformType);
    }

    @Override
    public int compareTo(Platform p) {
        if ( p == null ) { return 1; }
        int z = sCompare(name, p.name);
        if ( z != 0 ) { return z; }
        z = sCompare(platformId, p.platformId);
        if ( z != 0 ) { return z; }
        z = sCompare(platformType, p.platformType);
        if ( z != 0 ) { return z; }
        z = sCompare(owner, p.owner);
        if ( z != 0 ) { return z; }
        return sCompare(country, p.country);
    }

}
