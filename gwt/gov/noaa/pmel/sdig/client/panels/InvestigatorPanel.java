package gov.noaa.pmel.sdig.client.panels;

import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.shared.bean.Person;

import java.util.List;

/**
 * Created by rhs on 2/27/17.
 */
public class InvestigatorPanel extends PersonPanel {
    public InvestigatorPanel() {
        super();
        setType(Constants.SECTION_INVESTIGATOR);
        heading.setText("Enter the information for this investigator. You may enter more than one investigator.");

        namePopover.setContent("3.1 Full name of the investigator (First Middle Last).");
        institutionPopover.setContent("3.2 Affiliated institution of the investigator (e.g., Woods Hole Oceanographic Institution).");
        addressPopover.setContent("3.3 Address of the affiliated institution of the investigator.");
        telephonePopover.setContent("3.4 Phone number of the investigator (xxx-xxx-xxxx).");
        emailPopover.setContent("3.5 Email address of the investigator.");
        idTypePopover.setContent("3.7 Please indicate which type of researcher ID.");
        idPopover.setContent("3.6 We recommend to use person identifiers (e.g. ORCID, Researcher ID, etc.) to unambiguously identify the investigator");

    }

}
