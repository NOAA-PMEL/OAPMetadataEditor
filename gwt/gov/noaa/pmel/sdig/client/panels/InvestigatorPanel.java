package gov.noaa.pmel.sdig.client.panels;

import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.shared.bean.Person;

import java.util.*;
/**
 * Created by rhs on 2/27/17.
 */

public class InvestigatorPanel extends PersonPanel {
    public InvestigatorPanel() {
        super("investigator");
        setType(Constants.SECTION_INVESTIGATOR);
        heading.setText("Enter the Information for this Investigator. You may enter more than one investigator.");
        showTable=true;
        save.setEnabled(false);
        email.setAllowBlank(true); //allowBlank="false"
        emailLabel.setText("Email Address");
        emailLabel.setColor("#000000");
    }
    public void setEditing(boolean isEditing) {
        editing = isEditing;
    }
    public boolean isDirty(List<Person> originals) {
        OAPMetadataEditor.debugLog("Investigator.isDirty("+originals+")");
        if ( originals == null ) { originals = Collections.EMPTY_LIST; }
        Set<Person> thisPeople = new TreeSet<>(getInvestigators());
        if ( this.hasContent()) {
            thisPeople.add(getPerson());
        }
        if ( thisPeople.size() != originals.size()) {
            OAPMetadataEditor.debugLog("Investigator.isDirty(orig:"+originals.size()+"): size:" + thisPeople.size());
            return true;
        }
        Set<Person> orderedOriginals = new TreeSet<>(originals);
        Iterator<Person> ooI = orderedOriginals.iterator();
        for ( Person person : thisPeople ) {
            Person originalPerson = ooI.next();
            if ( ! person.isTheSameAs(originalPerson)) {
                OAPMetadataEditor.debugLog("Investigator.isDirty: " + person + " v " + originalPerson);
                return true;
            }
        }
        return false;
    }

    public boolean isValidTest(List<Person> originals) {
        OAPMetadataEditor.debugLog("Investigator.isValidTest("+originals+")");
//        Set<Person> thisPeople = new TreeSet<>(getInvestigators());
        Set<Person> orderedOriginals = new TreeSet<>(originals);
        for ( Person person : orderedOriginals ) {
                OAPMetadataEditor.debugLog("Investigator.valid: " + person);
                return true;
        }
        return false;
    }

}
