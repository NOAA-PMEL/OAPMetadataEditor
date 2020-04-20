package gov.noaa.pmel.sdig.shared.bean;

import gov.noaa.pmel.sdig.shared.Stringy;

/**
 * Created by rhs on 3/3/17.
 */
public class Citation extends DbItem implements Stringy {

    String title;
    String datasetAbstract;
    String useLimitation;
    String purpose;
    String expocode;
    String cruiseId;
    String section;
    String citationAuthorList;
    String scientificReferences;
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

    public String getScientificReferences() {
        return scientificReferences;
    }

    public void setScientificReferences(String scientificReferences) {
        this.scientificReferences = scientificReferences;
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

    public String getDatasetAbstract() { return datasetAbstract; }

    public void setDatasetAbstract(String datasetAbstract) {
        this.datasetAbstract = datasetAbstract;
    }

    public String getUseLimitation() { return useLimitation; }

    public void setUseLimitation(String useLimitation) { this.useLimitation = useLimitation; }

    @Override
    public Citation sClone() {
        Citation newc = new Citation();
//        newc.id = this.id;
//        newc.version = this.version;
        newc.title = this.title;
        newc.datasetAbstract = this.datasetAbstract;
        newc.useLimitation = this.useLimitation;
        newc.purpose = this.purpose;
        newc.expocode = this.expocode;
        newc.cruiseId = this.cruiseId;
        newc.section = this.section;
        newc.citationAuthorList = this.citationAuthorList;
        newc.scientificReferences = this.scientificReferences;
        newc.supplementalInformation = this.supplementalInformation;
        newc.researchProjects = this.researchProjects;
        return newc;
    }
}
