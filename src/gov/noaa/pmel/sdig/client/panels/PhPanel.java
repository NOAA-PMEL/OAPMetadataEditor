package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

/**
 * Created by rhs on 3/22/17.
 */
public class PhPanel extends CommonVariablePanel {
    public PhPanel() {

        super();
        abbreviation.setText("pH");
        abbreviation.setEnabled(false);
        fullVariableName.setText("pH");
        fullVariableName.setEnabled(false);
        heading.setText("Enter the information for pH.");

        // 005 Variable unit
        unitsForm.setVisible(false);
        units.setAllowBlank(true);
        save.addClickHandler(saveIt);
  
    }
    public Variable getPh() {
        return getCommonVariable();
    }
    public ClickHandler saveIt = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            // For some reason this returns a "0" in debug mode.
            String valid = String.valueOf( form.validate());
            if ( valid.equals("false") ||
                    valid.equals("0")) {
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.WARNING);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.NOT_COMPLETE, settings);
            } else {
                eventBus.fireEventFromSource(new SectionSave(getPh(), Constants.SECTION_PH), PhPanel.this);
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.SUCCESS);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.COMPLETE, settings);
            }
        }
    };

    public void reset() {
        form.reset();
    }
}
