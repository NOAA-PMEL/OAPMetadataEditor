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
public class Pco2d2Panel extends Composite {

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

    // 016 Storage method
    @UiField
    TextBox storageMethod;

    // 024 at what temperature was pCO2 reported
    @UiField
    TextBox pco2Temperature;
    
    // 028 Concentrations of standard gas
    @UiField
    TextBox gasConcentration;

    // 037 Headspace volume (mL)
    @UiField
    TextBox headspaceVolume;

    // 043 Manufacturer of standard gas
    @UiField
    TextBox standardGasManufacture;

    // 044 Manufacturer of the gas detector
    @UiField
    TextBox gasDetectorManufacture;

    // 046 Model of the gas detector
    @UiField
    TextBox gasDetectorModel;

    // 049 Resolution of the gas detector
    @UiField
    TextBox gasDectectorResolution;

    // 050 Seawater volume (mL)
    @UiField
    TextBox seawaterVolume;

    // 052 Temperature correction method
    @UiField
    TextBox temperatureCorrectionMethod;

    // 053 Temperature of measurement
    @UiField
    TextBox temperatureMeasurement;

    // 054 Temperature of standardization
    @UiField
    TextBox temperatureStandarization;

    // 056 Uncertainties of standard gas
    @UiField
    TextBox standardGasUncertainties;

    // 057 Uncertainty of the gas detector
    @UiField
    TextBox gasDectectorUncertainty;

    // 060 Water vapor correction method
    @UiField
    TextBox vaporCorrection;


    // The form groups that hold the labels and form entry widgets (textbox, textarea and dropdowns).
    
    // 012 Standardization technique description
    @UiField
    FormGroup standardizationTechniqueForm;

    // 013 Frequency of standardization
    @UiField
    FormGroup freqencyOfStandardizationForm;
    
    // 016 Storage method
    @UiField
    FormGroup storageMethodForm;

    // 024 at what temperature was pCO2 reported
    @UiField
    FormGroup pco2TemperatureForm;

    // 028 Concentrations of standard gas
    @UiField
    FormGroup gasConcentrationForm;

    // 037 Headspace volume (mL)
    @UiField
    FormGroup headspaceVolumeForm;

    // 043 Manufacturer of standard gas
    @UiField
    FormGroup standardGasManufactureForm;

    // 044 Manufacturer of the gas detector
    @UiField
    FormGroup gasDetectorManufactureForm;

    // 046 Model of the gas detector
    @UiField
    FormGroup gasDetectorModelForm;

    // 049 Resolution of the gas detector
    @UiField
    FormGroup gasDectectorResolutionForm;

    // 050 Seawater volume (mL)
    @UiField
    FormGroup seawaterVolumeForm;

    // 052 Temperature correction method
    @UiField
    FormGroup temperatureCorrectionMethodForm;

    // 053 Temperature of measurement
    @UiField
    FormGroup temperatureMeasurementForm;

    // 054 Temperature of standardization
    @UiField
    FormGroup temperatureStandarizationForm;

    // 056 Uncertainties of standard gas
    @UiField
    FormGroup standardGasUncertaintiesForm;

    // 057 Uncertainty of the gas detector
    @UiField
    FormGroup gasDectectorUncertaintyForm;

    // 060 Water vapor correction method
    @UiField
    FormGroup vaporCorrectionForm;

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    public void reset() {
        form.reset();
    }

//TODO initialize the cell type dropdown.

    interface Pco2d2PanelUiBinder extends UiBinder<HTMLPanel, Pco2d2Panel> {
    }

    private static Pco2d2PanelUiBinder ourUiBinder = GWT.create(Pco2d2PanelUiBinder.class);

    public Pco2d2Panel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        heading.setText("Continue adding information for pCO2d.");
        save.addClickHandler(saveIt);
    }
    public void show(Variable variable) {
        if (variable.getFreqencyOfStandardization() != null) {            
            freqencyOfStandardization.setText(variable.getFreqencyOfStandardization());
        }
        if (variable.getStorageMethod() != null) {
            storageMethod.setText(variable.getStorageMethod());
        }
        if (variable.getPco2Temperature() != null) {
            pco2Temperature.setText(variable.getPco2Temperature());
        }
        if (variable.getGasConcentration() != null) {
            gasConcentration.setText(variable.getGasConcentration());
        }
        if (variable.getHeadspaceVolume() != null) {
            headspaceVolume.setText(variable.getHeadspaceVolume());
        }
        if (variable.getStandardGasManufacture() != null) {
            standardGasManufacture.setText(variable.getStandardGasManufacture());
        }
        if (variable.getGasDetectorManufacture() != null) {
            gasDetectorManufacture.setText(variable.getGasDetectorManufacture());
        }
        if (variable.getGasDetectorModel() != null) {
            gasDetectorModel.setText(variable.getGasDetectorModel());
        }
        if (variable.getGasDetectorManufacture() != null) {
            gasDectectorResolution.setText(variable.getGasDetectorManufacture());
        }
        if (variable.getSeawaterVolume() != null) {
            seawaterVolume.setText(variable.getSeawaterVolume());
        }
        if (variable.getTemperatureCorrectionMethod() != null) {
            temperatureCorrectionMethod.setText(variable.getTemperatureCorrectionMethod());
        }
        if (variable.getTemperatureMeasurement() != null) {
            temperatureMeasurement.setText(variable.getTemperatureMeasurement());
        }
        if (variable.getTemperatureStandarization() != null) {
            temperatureStandarization.setText(variable.getTemperatureStandarization());
        }
        if (variable.getStandardGasUncertainties() != null) {
            standardGasUncertainties.setText(variable.getStandardGasUncertainties());
        }
        if (variable.getGasDectectorUncertainty() != null) {
            gasDectectorUncertainty.setText(variable.getGasDectectorUncertainty());
        }
        if (variable.getVaporCorrection() != null) {
            vaporCorrection.setText(variable.getVaporCorrection());
        }
    }
    public Variable fill(Variable variable) {
        variable.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        variable.setStorageMethod(storageMethod.getText());
        variable.setPco2Temperature(pco2Temperature.getText());
        variable.setGasConcentration(gasConcentration.getText());
        variable.setHeadspaceVolume(headspaceVolume.getText());
        variable.setStandardGasManufacture(standardGasManufacture.getText());
        variable.setGasDetectorManufacture(gasDetectorManufacture.getText());
        variable.setGasDetectorModel(gasDetectorModel.getText());
        variable.setGasDetectorManufacture(gasDectectorResolution.getText());
        variable.setSeawaterVolume(seawaterVolume.getText());
        variable.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
        variable.setTemperatureMeasurement(temperatureMeasurement.getText());
        variable.setTemperatureStandarization(temperatureStandarization.getText());
        variable.setStandardGasUncertainties(standardGasUncertainties.getText());
        variable.setGasDectectorUncertainty(gasDectectorUncertainty.getText());
        variable.setVaporCorrection(vaporCorrection.getText());
        return variable;
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
                eventBus.fireEventFromSource(new SectionSave(null, Constants.SECTION_PCO2D2), Pco2d2Panel.this);
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