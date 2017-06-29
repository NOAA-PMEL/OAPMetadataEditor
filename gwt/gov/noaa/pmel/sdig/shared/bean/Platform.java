package gov.noaa.pmel.sdig.shared.bean;

/**
 * Created by rhs on 3/7/17.
 */
public class Platform {
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
}
