package gov.noaa.pmel.sdig.client.panels;

import gov.noaa.pmel.sdig.client.Constants;

/**
 * Created by rhs on 2/27/17.
 */
public class DataSubmitterPanel extends PersonPanel {
    public DataSubmitterPanel() {
        super("data submitter");
        setType(Constants.SECTION_SUBMITTER);
        heading.setText("Enter the Information for this Data Submitter.");
        email.setAllowBlank(false);
        emailLabel.addStyleName("has-error");
        showTable = false;
    }
}
