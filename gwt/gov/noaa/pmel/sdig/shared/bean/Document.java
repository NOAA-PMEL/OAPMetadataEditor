package gov.noaa.pmel.sdig.shared.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rhs on 4/11/17.
 */
public class Document {

    String id;
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

//    String datasetId;
    List<Platform> platforms;
    List<Person> investigators;
    List<Variable> variables;
    Person dataSubmitter;
    Citation citation;
    TimeAndLocation timeAndLocation;
    List<Funding> funding;
    Variable dic;
    Variable ta;
    Variable ph;
    Variable pco2a;
    Variable pco2d;

    String update;

    public static Document copy(Document doc) {
        Document copyDoc = new Document();
        copyDoc.id = doc.id;
        copyDoc.setCitation(doc.getCitation());
        copyDoc.setDataSubmitter(doc.getDataSubmitter());
        copyDoc.setInvestigators(new ArrayList<>(doc.getInvestigators()));
        copyDoc.setFunding(doc.getFunding());
        copyDoc.setPlatforms(new ArrayList<>(doc.getPlatforms()));
        copyDoc.setDic(doc.getDic());
        copyDoc.setPco2a(doc.getPco2a());
        copyDoc.setPco2d(doc.getPco2d());
        copyDoc.setPh(doc.getPh());
        copyDoc.setTa(doc.getTa());
        copyDoc.setTimeAndLocation(doc.getTimeAndLocation());
        copyDoc.setVariables(new ArrayList<>(doc.getVariables()));
        return copyDoc;
    }
    public static Document EmptyDocument() {
       Document empty = new Document();
//       empty.setCitation(new Citation());
//       empty.setDataSubmitter(new Person());
//       empty.setDic(new Variable());
       empty.setFunding(Arrays.asList(new Funding[0]));
       empty.setInvestigators(Arrays.asList(new Person[0]));
//       empty.setPco2a(new Variable());
//       empty.setPco2d(new Variable());
//       empty.setPh(new Variable());
       empty.setPlatforms(Arrays.asList(new Platform[0]));
//       empty.setTa(new Variable());
//       empty.setTimeAndLocation(new TimeAndLocation());
       empty.setVariables(Arrays.asList(new Variable[0]));
       return empty;
    }

//    public String getDatasetId() { return datasetId; }
//    public void setDatasetId(String id) { datasetId = id; }

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
    public List<Funding> getFunding() {
        return funding;
    }

    public void setFunding(List<Funding> funding) {
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
