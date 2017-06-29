package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhs on 3/8/17.
 */
public class Ph2Panel extends Composite {

    @UiField
    Heading heading;

    @UiField
    Button save;

    @UiField
    Form form;

    // 012 Standardization technique description
    @UiField
    TextBox standardizationTechnique;

    // 013 Frequency of standardization
    @UiField
    TextBox freqencyOfStandardization;

    // 025 at what temperature was pH reported
    @UiField
    TextBox pHtemperature;

    // 047 pH scale
    @UiField
    TextBox pHscale;

    // 048 pH values of the standards
    @UiField
    TextBox pHstandards;

    // 052 Temperature correction method
    @UiField
    TextBox temperatureCorrectionMethod;

    // 053 Temperature of measurement
    @UiField
    TextBox temperatureMeasurement;

    // 054 Temperature of standardization
    @UiField
    TextBox temperatureStandarization;

    // The form groups that hold the labels and form entry widgets (textbox, textarea and dropdowns).

    // 012 Standardization technique description
    @UiField
    FormGroup standardizationTechniqueForm;

    // 013 Frequency of standardization
    @UiField
    FormGroup freqencyOfStandardizationForm;

    // 025 at what temperature was pH reported
    @UiField
    FormGroup pHtemperatureForm;

    // 047 pH scale
    @UiField
    FormGroup pHscaleForm;

    // 048 pH values of the standards
    @UiField
    FormGroup pHstandardsForm;

    // 052 Temperature correction method
    @UiField
    FormGroup temperatureCorrectionMethodForm;

    // 053 Temperature of measurement
    @UiField
    FormGroup temperatureMeasurementForm;

    // 054 Temperature of standardization
    @UiField
    FormGroup temperatureStandarizationForm;

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    public void reset() {
        form.reset();
    }

//TODO initialize the cell type dropdown.

    interface Ph2PanelUiBinder extends UiBinder<HTMLPanel, Ph2Panel> {
    }

    private static Ph2PanelUiBinder ourUiBinder = GWT.create(Ph2PanelUiBinder.class);

    public Ph2Panel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        save.addClickHandler(saveIt);
        heading.setText("Continue entering information for pH.");
    }
    public void show(Variable variable) {

        if ( variable.getStandardizationTechnique() != null ) {
            standardizationTechnique.setText(variable.getStandardizationTechnique());
        }

        if ( variable.getFreqencyOfStandardization() != null ) {
            freqencyOfStandardization.setText(variable.getFreqencyOfStandardization());
        }

        if ( variable.getpHtemperature() != null ) {
            pHtemperature.setText(variable.getpHtemperature());
        }

        if ( variable.getpHscale() != null ) {
            pHscale.setText(variable.getpHscale());
        }

        if ( variable.getpHstandards() != null ) {
            pHstandards.setText(variable.getpHstandards());
        }

        if ( variable.getTemperatureCorrectionMethod() != null ) {
            temperatureCorrectionMethod.setText(variable.getTemperatureCorrectionMethod());
        }

        if ( variable.getTemperatureMeasurement() != null ) {
            temperatureMeasurement.setText(variable.getTemperatureMeasurement());
        }

        if ( variable.getTemperatureStandarization() != null ) {
            temperatureStandarization.setText(variable.getTemperatureStandarization());
        }

    }
    public Variable fill(Variable ph) {
        ph.setStandardizationTechnique(standardizationTechnique.getText().trim());
        ph.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        ph.setpHtemperature(pHtemperature.getText());
        ph.setpHscale(pHscale.getText());
        ph.setpHstandards(pHstandards.getText());
        ph.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
        ph.setTemperatureMeasurement(temperatureMeasurement.getText());
        ph.setTemperatureStandarization(temperatureStandarization.getText());
        return ph;
    }
    public ClickHandler saveIt = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            // For some reason this returns a "0" in debug mode.
            String valid = String.valueOf(form.validate());
            if (valid.equals("false") ||
                    valid.equals("0")) {
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.WARNING);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.NOT_COMPLETE, settings);
            } else {
                eventBus.fireEventFromSource(new SectionSave(null, Constants.SECTION_PH2), Ph2Panel.this);
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.SUCCESS);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.COMPLETE, settings);
            }
        }
    };
    public boolean valid() {
        String valid = String.valueOf(form.validate());
        if (valid.equals("false") ||
                valid.equals("0")) {
            return false;
        } else {
            return true;
        }
    }
}