package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiHandler;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.shared.bean.Person;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhs on 3/8/17.
 */
public class DicPanel extends CommonVariablePanel {



    public DicPanel() {
        /*
DIC: Variable abbreviation in data files
DIC: Observation type
DIC: In-situ observation / manipulation condition / response variable
DIC: Manipulation method
DIC: Variable unit
DIC: Measured or calculated
DIC: Calculation method and parameters
DIC: Sampling instrument
DIC: Analyzing instrument
DIC: Detailed sampling and analyzing information
DIC: Field replicate information
DIC: Standardization technique description
DIC: Frequency of standardization
DIC: CRM manufacturer
DIC: Batch number
DIC: Poison used to kill the sample
DIC: Poison volume
DIC Poisoning correction description
DIC: Uncertainty
DIC: Data quality flag description
DIC: Method reference (citation)
DIC: Researcher Name
DIC: Researcher Institution
         */
        super();
        abbreviation.setText("DIC");
        abbreviation.setEnabled(false);
        fullVariableName.setText("Dissolved Inorganic Carbon");
        fullVariableName.setEnabled(false);
        heading.setText("Enter the information for Dissolved Inorganic Carbon (DIC)");
        save.addClickHandler(saveIt);
    }
    public Variable getDic() {
        Variable dic = getCommonVariable();
        return dic;
    }

    public void fill(Variable variable) {
        fillCommonVariable(variable);
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
                eventBus.fireEventFromSource(new SectionSave(getDic(), Constants.SECTION_DIC), DicPanel.this);
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
