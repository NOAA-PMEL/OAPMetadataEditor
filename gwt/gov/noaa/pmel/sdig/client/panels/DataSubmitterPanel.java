package gov.noaa.pmel.sdig.client.panels;

import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.shared.bean.Document;

/**
 * Created by rhs on 2/27/17.
 */
public class DataSubmitterPanel extends PersonPanel {
    public DataSubmitterPanel() {
        super("data submitter");
        setType(Constants.SECTION_SUBMITTER);
        heading.setText("Enter the Information for this Data Submitter.");
        showTable = false;
    }
    public boolean isDirty() {
        return isDirty(getDbItem()); // XXX Not sure...
    }

}
