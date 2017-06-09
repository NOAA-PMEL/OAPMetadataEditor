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
public class Pco2aPanel extends CommonVariablePanel {
    public Pco2aPanel() {
        super();
        abbreviation.setText("pCO2");
        abbreviation.setEnabled(false);
        fullVariableName.setText("pCO2");
        fullVariableName.setEnabled(false);
        heading.setText("Enter the information for pCO2A.");
        fieldReplicate.setAllowBlank(true);
        fieldReplicateForm.setVisible(false);
        save.addClickHandler(saveIt);
    }
    public Variable getPco2a() {
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
                eventBus.fireEventFromSource(new SectionSave(getPco2a(), Constants.SECTION_PCO2A), Pco2aPanel.this);
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
