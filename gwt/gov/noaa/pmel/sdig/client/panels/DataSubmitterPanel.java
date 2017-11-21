package gov.noaa.pmel.sdig.client.panels;

import gov.noaa.pmel.sdig.client.Constants;

/**
 * Created by rhs on 2/27/17.
 */
public class DataSubmitterPanel extends PersonPanel {
    public DataSubmitterPanel() {
        super();
        setType(Constants.SECTION_SUBMITTER);
        heading.setText("Enter the information for this data submitter.");
        people.setVisible(false);
        showTable = false;

        namePopover.setTitle("4.1 Full name of the data submitter (First Midde Last).");
        institutionPopover.setTitle("4.2 Affiliated institution of the data submitter (e.g., Woods Hole Oceanographic Institution).");
        addressPopover.setTitle("4.3 Address of the affiliated institution of the data submitter.");
        telephonePopover.setTitle("4.4 Phone number of the data submitter.");
        emailPopover.setTitle("4.5 Email address of the data submitter.");
        idTypePopover.setTitle("4.7 Please indicate which type of researcher ID.");
        idPopover.setTitle("4.6 We recommend to use person identifiers (e.g. ORCID, Researcher ID, etc.) to unambiguously identify the investigator");


    }
}
