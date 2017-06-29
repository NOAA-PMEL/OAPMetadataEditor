package gov.noaa.pmel.sdig.shared.bean;

import java.util.List;

/**
 * Created by rhs on 4/11/17.
 */
public class Document {

    List<Platform> platforms;
    List<Person> investigators;
    List<Variable> variables;
    Person dataSubmitter;
    Citation citation;
    TimeAndLocation timeAndLocation;
    Funding funding;
    Variable dic;
    Variable ta;
    Variable ph;
    Variable pco2a;
    Variable pco2d;

    String update;


    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public List<Person> getInvestigators() {
        return investigators;
    }

    public void setInvestigators(List<Person> investigators) {
        this.investigators = investigators;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }
    public Funding getFunding() {
        return funding;
    }

    public void setFunding(Funding funding) {
        this.funding = funding;
    }

    public Person getDataSubmitter() {
        return dataSubmitter;
    }

    public void setDataSubmitter(Person dataSubmitter) {
        this.dataSubmitter = dataSubmitter;
    }

    public Citation getCitation() {
        return citation;
    }

    public void setCitation(Citation citation) {
        this.citation = citation;
    }

    public TimeAndLocation getTimeAndLocation() {
        return timeAndLocation;
    }

    public void setTimeAndLocation(TimeAndLocation timeAndLocation) {
        this.timeAndLocation = timeAndLocation;
    }

    public Variable getDic() {
        return dic;
    }

    public void setDic(Variable dic) {
        this.dic = dic;
    }

    public Variable getTa() {
        return ta;
    }

    public void setTa(Variable ta) {
        this.ta = ta;
    }

    public Variable getPh() {
        return ph;
    }

    public void setPh(Variable ph) {
        this.ph = ph;
    }

    public Variable getPco2a() {
        return pco2a;
    }

    public void setPco2a(Variable pco2a) {
        this.pco2a = pco2a;
    }

    public Variable getPco2d() {
        return pco2d;
    }

    public void setPco2d(Variable pco2d) {
        this.pco2d = pco2d;
    }
}
