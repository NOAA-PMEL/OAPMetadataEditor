package gov.noaa.pmel.sdig.client.panels;

import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.shared.bean.Document;
import gov.noaa.pmel.sdig.shared.bean.Person;

import java.util.*;

/**
 * Created by rhs on 2/27/17.
 */
public class InvestigatorPanel extends PersonPanel {
    public InvestigatorPanel() {
        super();
        setType(Constants.SECTION_INVESTIGATOR);
        heading.setText("Enter the information for this investigator. You may enter more than one investigator.");

        namePopover.setTitle("3.1 Full name of the investigator (First Middle Last).");
        institutionPopover.setTitle("3.2 Affiliated institution of the investigator (e.g., Woods Hole Oceanographic Institution).");
        addressPopover.setTitle("3.3 Address of the affiliated institution of the investigator.");
        telephonePopover.setTitle("3.4 Phone number of the investigator (xxx-xxx-xxxx).");
        emailPopover.setTitle("3.5 Email address of the investigator.");
        idTypePopover.setTitle("3.7 Please indicate which type of researcher ID.");
        idPopover.setTitle("3.6 We recommend to use person identifiers (e.g. ORCID, Researcher ID, etc.) to unambiguously identify the investigator");

    }

    public boolean isDirty(List<Person> originals) {
        if ( this.isDirty()) {
            addPerson(getPerson());
            form.reset();
        }
        if ( originals == null ) { originals = Collections.EMPTY_LIST; }
        Set<Person> thisPeople = new TreeSet<>(getInvestigators());
        if ( thisPeople.size() != originals.size()) {
            return true;
        }
        Set<Person> orderedOriginals = new TreeSet<>(originals);
        Iterator<Person> ooI = orderedOriginals.iterator();
        for ( Person person : thisPeople ) {
            Person originalPerson = ooI.next();
            if ( ! person.isTheSameAs(originalPerson)) {
                return true;
            }
        }
        return false;
    }


}
