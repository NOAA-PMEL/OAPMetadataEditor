package gov.noaa.pmel.sdig.shared.bean;

/**
 * Created by rhs on 3/3/17.
 */
public class Citation {

    String title;
    String platformAbstract;
    String purpose;
    String projects;
    String expocode;
    String cruiseId;
    String section;
    String citationAuthorList;
    String references;
    String supplementalInformation;
    String researchProjects;

    public String getResearchProjects() {
        return researchProjects;
    }

    public void setResearchProjects(String researchProjects) {
        this.researchProjects = researchProjects;
    }

    public String getCruiseId() {
        return cruiseId;
    }

    public void setCruiseId(String cruiseId) {
        this.cruiseId = cruiseId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCitationAuthorList() {
        return citationAuthorList;
    }

    public void setCitationAuthorList(String citationAuthorList) {
        this.citationAuthorList = citationAuthorList;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getSupplementalInformation() {
        return supplementalInformation;
    }

    public void setSupplementalInformation(String supplementalInformation) {
        this.supplementalInformation = supplementalInformation;
    }

    public String getExpocode() {
        return expocode;
    }

    public void setExpocode(String expocode) {
        this.expocode = expocode;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlatformAbstract() {
        return platformAbstract;
    }

    public void setPlatformAbstract(String platformAbstract) {
        this.platformAbstract = platformAbstract;
    }
}
