package gov.noaa.pmel.sdig.shared.bean;

import gov.noaa.pmel.sdig.shared.HasContent;
import gov.noaa.pmel.sdig.shared.Stringy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rhs on 4/11/17.
 */
public class Document extends DbItem implements HasContent {

    private Long dbId;
    public Long getDbId() { return dbId; }
    public void setDbId(Long dbId) { this.dbId = dbId; }
    private Long dbVersion;
    public Long getDbVersion() { return dbVersion; }
    public void setDbVersion(Long dbVersion) { this.dbVersion = dbVersion; }

    String datasetIdentifier;
    public String getDatasetIdentifier() { return datasetIdentifier; }
    public void setDatasetIdentifier(String datasetIdentifier) { this.datasetIdentifier = datasetIdentifier; }

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

    public static List<? extends Stringy> getArrayCopy(List<? extends Stringy> list) {
        ArrayList<Stringy> newList = new ArrayList<>();
        if ( list != null ) {
            for (Stringy o : list) {
                newList.add(o.sClone());
            }
        }
        return newList;
    }
    private static Stringy copyOf(Stringy obj) {
        return obj != null ? obj.sClone() : null;
    }

    public static Document copy(Document doc) {
        Document copyDoc = new Document();
//        copyDoc.id = doc.id;
        copyDoc.datasetIdentifier = doc.datasetIdentifier;
        copyDoc.setCitation((Citation)copyOf(doc.getCitation()));
        copyDoc.setDataSubmitter((Person)copyOf(doc.getDataSubmitter()));
        copyDoc.setInvestigators((List<Person>) getArrayCopy(doc.getInvestigators()));
        copyDoc.setFunding((List<Funding>) getArrayCopy(doc.getFunding()));
        copyDoc.setPlatforms((List<Platform>) getArrayCopy(doc.getPlatforms()));
        copyDoc.setDic((Variable)copyOf(doc.getDic()));
        copyDoc.setPco2a((Variable)copyOf(doc.getPco2a()));
        copyDoc.setPco2d((Variable)copyOf(doc.getPco2d()));
        copyDoc.setPh((Variable)copyOf(doc.getPh()));
        copyDoc.setTa((Variable)copyOf(doc.getTa()));
        copyDoc.setTimeAndLocation((TimeAndLocation) copyOf(doc.getTimeAndLocation()));
        copyDoc.setVariables((List<Variable>) getArrayCopy(doc.getVariables()));
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

    private boolean hasContent(HasContent field) {
        return field != null && field.hasContent();
    }
    private boolean hasContent(List<? extends HasContent> list) {
        if ( list == null || list.isEmpty()) { return false; }
        for (HasContent item : list ) {
            if ( hasContent(item)) {
                return true;
            }
        }
        return false;
    }
    public boolean hasContent() {
        boolean hasContent =
                    hasContent(platforms) ||
                    hasContent(investigators) ||
                    hasContent(variables) ||
                    hasContent(dataSubmitter) ||
                    hasContent(citation) ||
                    hasContent(timeAndLocation) ||
                    hasContent(funding) ||
                    hasContent(dic) ||
                    hasContent(ta) ||
                    hasContent(ph) ||
                    hasContent(pco2a) ||
                    hasContent(pco2d);
        return hasContent;
    }

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
