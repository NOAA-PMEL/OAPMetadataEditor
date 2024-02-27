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
    public boolean isDirty() {
        return isDirty(originalList);
    }
}
