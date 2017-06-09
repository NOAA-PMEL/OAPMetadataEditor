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
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

/**
 * Created by rhs on 3/8/17.
 */
public class Pco2a2Panel extends Composite {

    @UiField
    Heading heading;

    // 012 Standardization technique description
    @UiField
    TextBox standardizationTechnique;

    // 013 Frequency of standardization
    @UiField
    TextBox freqencyOfStandardization;

    // 024 at what temperature was pCO2 reported
    @UiField
    TextBox pco2Temperature;

    // 028 Concentrations of standard gas
    @UiField
    TextBox gasConcentration;

    // 030 Depth of seawater intake
    @UiField
    TextBox intakeDepth;

    // 031 Drying method for CO2 gas
    @UiField
    TextBox dryingMethod;

    // 033 Equilibrator type
    @UiField
    TextBox equilibratorType;

    // 034 Equilibrator volume (L)
    @UiField
    TextBox equilibratorVolume;

    // 036 Headspace gas flow rate (L/min)
    @UiField
    TextBox gasFlowRate;
    ;

    // 038 How was pressure inside the equilibrator measured.
    @UiField
    TextBox equilibratorPressureMeasureMethod;

    // 039 How was temperature inside the equilibrator measured .
    @UiField
    TextBox equilibratorTemperatureMeasureMethod;

    // 041 Location of seawater intake
    @UiField
    TextBox intakeLocation;

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

    // ??? Temperature correction
    @UiField
    TextBox temperatureCorrection;

    // 052 Temperature correction method
    @UiField
    TextBox temperatureCorrectionMethod;

    // 056 Uncertainties of standard gas
    @UiField
    TextBox standardGasUncertainties;

    // 057 Uncertainty of the gas detector
    @UiField
    TextBox gasDectectorUncertainty;

    // 058 Vented or not
    @UiField
    TextBox vented;

    // 059 Water flow rate (L/min)
    @UiField
    TextBox flowRate;

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

    // 024 at what temperature was pCO2 reported
    @UiField
    FormGroup pco2TemperatureForm;

    // 028 Concentrations of standard gas
    @UiField
    FormGroup gasConcentrationForm;

    // 030 Depth of seawater intake
    @UiField
    FormGroup intakeDepthForm;

    // 031 Drying method for CO2 gas
    @UiField
    FormGroup dryingMethodForm;

    // 033 Equilibrator type
    @UiField
    FormGroup equilibratorTypeForm;

    // 034 Equilibrator volume (L)
    @UiField
    FormGroup equilibratorVolumeForm;

    // 036 Headspace gas flow rate (L/min)
    @UiField
    FormGroup gasFlowRateForm;

    // 038 How was pressure inside the equilibrator measured.
    @UiField
    FormGroup equilibratorPressureMeasureMethodForm;

    // 039 How was temperature inside the equilibrator measured .
    @UiField
    FormGroup equilibratorTemperatureMeasureMethodForm;

    // 041 Location of seawater intake
    @UiField
    FormGroup intakeLocationForm;

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

    // ??
    @UiField
    FormGroup temperatureCorrectionForm;

    // 052 Temperature correction method
    @UiField
    FormGroup temperatureCorrectionMethodForm;

    // 056 Uncertainties of standard gas
    @UiField
    FormGroup standardGasUncertaintiesForm;

    // 057 Uncertainty of the gas detector
    @UiField
    FormGroup gasDectectorUncertaintyForm;

    // 058 Vented or not
    @UiField
    FormGroup ventedForm;

    // 059 Water flow rate (L/min)
    @UiField
    FormGroup flowRateForm;

    // 060 Water vapor correction method
    @UiField
    FormGroup vaporCorrectionForm;

    @UiField
    Button save;

    @UiField
    Form form;

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    public void reset() {
        form.reset();
    }

//TODO initialize the cell type dropdown.

    interface Pco2a2PanelUiBinder extends UiBinder<HTMLPanel, Pco2a2Panel> {
    }

    private static Pco2a2PanelUiBinder ourUiBinder = GWT.create(Pco2a2PanelUiBinder.class);

    public Pco2a2Panel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        heading.setText("Continue entering information for pC02A");
        save.addClickHandler(saveIt);
    }
    public void show(Variable variable) {
        if (variable.getStandardizationTechnique() != null ) {
            standardizationTechnique.setText(variable.getStandardizationTechnique());
        }

        if ( variable.getFreqencyOfStandardization() != null ) {
            freqencyOfStandardization.setText(variable.getFreqencyOfStandardization());
        }

        if ( variable.getPco2Temperature() != null ) {
            pco2Temperature.setText(variable.getPco2Temperature());
        }

        if ( variable.getGasConcentration() != null ) {
            gasConcentration.setText(variable.getGasConcentration());
        }

        if ( variable.getIntakeDepth() != null ) {
            intakeDepth.setText(variable.getIntakeDepth());
        }

        if (variable.getDryingMethod() != null ) {
            dryingMethod.setText(variable.getDryingMethod());
        }

        if ( variable.getEquilibratorType() != null ) {
            equilibratorType.setText(variable.getEquilibratorType());
        }

        if ( variable.getEquilibratorVolume() != null ) {
            equilibratorVolume.setText(variable.getEquilibratorVolume());
        }

        if ( variable.getGasFlowRate() != null ) {
            gasFlowRate.setText(variable.getGasFlowRate());
        }

        if ( variable.getEquilibratorPressureMeasureMethod() != null ) {
            equilibratorPressureMeasureMethod.setText(variable.getEquilibratorPressureMeasureMethod());
        }

        if ( variable.getEquilibratorTemperatureMeasureMethod() != null ) {
            equilibratorTemperatureMeasureMethod.setText(variable.getEquilibratorTemperatureMeasureMethod());
        }

        if ( variable.getIntakeLocation() != null ) {
            intakeLocation.setText(variable.getIntakeLocation());
        }

        if ( variable.getStandardGasManufacture() != null ) {
            standardGasManufacture.setText(variable.getStandardGasManufacture());
        }

        if ( variable.getGasDetectorManufacture() != null ) {
            gasDetectorManufacture.setText(variable.getGasDetectorManufacture());
        }


        if ( variable.getGasDetectorModel() != null ) {
            gasDetectorModel.setText(variable.getGasDetectorModel());
        }

        if ( variable.getGasDectectorResolution() != null ) {
            gasDectectorResolution.setText(variable.getGasDectectorResolution());
        }

        if ( variable.getTemperatureCorrection() != null ) {
            temperatureCorrection.setText(variable.getTemperatureCorrection());
        }
        if ( variable.getTemperatureCorrectionMethod() != null ) {
            temperatureCorrectionMethod.setText(variable.getTemperatureCorrectionMethod());
        }

        if ( variable.getStandardGasUncertainties() != null ) {
            standardGasUncertainties.setText(variable.getStandardGasUncertainties());
        }

        if ( variable.getGasDectectorUncertainty() != null ) {
            gasDectectorUncertainty.setText(variable.getGasDectectorUncertainty());
        }

        if ( variable.getVented() != null ) {
            vented.setText(variable.getVented());
        }

        if ( variable.getFlowRate() != null ) {
            flowRate.setText(variable.getFlowRate());
        }

        if ( variable.getVaporCorrection() != null ) {
            vaporCorrection.setText(variable.getVaporCorrection());
        }
    }
    public Variable fill (Variable pco2a) {
        pco2a.setStandardizationTechnique(standardizationTechnique.getText());
        pco2a.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        pco2a.setPco2Temperature(pco2Temperature.getText());
        pco2a.setGasConcentration(gasConcentration.getText());
        pco2a.setIntakeDepth(intakeDepth.getText());
        pco2a.setDryingMethod(dryingMethod.getText());
        pco2a.setEquilibratorType(equilibratorType.getText());
        pco2a.setEquilibratorVolume(equilibratorVolume.getText());
        pco2a.setGasFlowRate(gasFlowRate.getText());
        pco2a.setEquilibratorPressureMeasureMethod(equilibratorPressureMeasureMethod.getText());
        pco2a.setEquilibratorTemperatureMeasureMethod(equilibratorTemperatureMeasureMethod.getText());
        pco2a.setIntakeLocation(intakeLocation.getText());
        pco2a.setStandardGasManufacture(standardGasManufacture.getText());
        pco2a.setGasDetectorManufacture(gasDetectorManufacture.getText());
        pco2a.setGasDetectorModel(gasDetectorModel.getText());
        pco2a.setGasDectectorResolution(gasDectectorResolution.getText());
        pco2a.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
        pco2a.setTemperatureCorrection(temperatureCorrection.getText());
        pco2a.setStandardGasUncertainties(standardGasUncertainties.getText());
        pco2a.setGasDectectorUncertainty(gasDectectorUncertainty.getText());
        pco2a.setVented(vented.getText());
        pco2a.setFlowRate(flowRate.getText());
        pco2a.setVaporCorrection(vaporCorrection.getText());
        return pco2a;
    }
    public boolean valid() {
        String valid = String.valueOf(form.validate());
        if (valid.equals("false") ||
                valid.equals("0")) {
            return false;
        } else {
            return true;
        }
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
                eventBus.fireEventFromSource(new SectionSave(null, Constants.SECTION_PCO2A2), Pco2a2Panel.this);
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.SUCCESS);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.COMPLETE, settings);
            }
        }
    };

}