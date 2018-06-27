package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

/**
 * Created by rhs on 3/30/17.
 */
public class Pco2dPanel extends Composite implements GetsDirty<Variable> {

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
    
    @UiField
    CommonVariablePanel common;

    interface Pco2dPanelUiBinder extends UiBinder<HTMLPanel, Pco2dPanel> {
    }

    private static Pco2dPanel.Pco2dPanelUiBinder ourUiBinder = GWT.create(Pco2dPanel.Pco2dPanelUiBinder.class);

    public Pco2dPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        common.abbreviation.setText("pCO2d");
        common.abbreviation.setEnabled(false);
        common.fullVariableName.setText("pco2 (fco2) discrete");
        common.fullVariableName.setEnabled(false);
        common.heading.setText("Enter the information for pCO2D.");
        save.addClickHandler(saveIt);
        common.abbreviationModal.setTitle("26.1 Column header name of the variable in the data files, e.g., pCO2, etc.");
        common.observationTypeModal.setTitle("26.2 How the variable is observed, e.g., surface underway, profile, time series, model output, etc. For experimental data, this could be: laboratory experiment, pelagic mesocosm, benthic mesocosm, benthic FOCE type studies, natural pertubration site studies, etc");
        common.manipulationMethodModal.setTitle("26.4 In perturbation experiments, seawater carbonate chemistry can be manipulated by different techniques, such as bubbling CO2, adding acids or bases, etc.");
        common.observationDetailModal.setTitle("26.3 Whether the variable belong to an in-situ observed variable, or a manipulation condition variable, or a response variable in a biological experimental study.");
        common.measuredModal.setTitle("26.6 Whether the variable is measured in-situ, or calculated from other variables");
        common.calculationMethodModal.setTitle("26.7 Variables can be calculated using different sets of constants or different software.");
        common.samplingInstrumentModal.setTitle("26.8 Instrument that is used to collect water samples, or deploy sensors, etc. For example, a Niskin bottle, pump, CTD, etc is a sampling instrument.");
        common.analyzingInstrumentModal.setTitle("26.9 Instrument that is used to analyze the water samples collected with the 'sampling instrument', or the sensors that are mounted on the 'sampling instrument' to measure the water body continuously. For example, a coulometer, winkler titrator, spectrophotometer, pH meter, thermosalinograph, oxygen sensor, YSI Multiparameter Meter, etc is an analyzing instrument. We encourage you to document as much details (such as the make, model, resolution, precisions, etc) of the instrument as you can here.");
        common.detailedInformationModal.setTitle("26.14 Detailed description of the sampling and analyzing procedures, including calibration procedures, model number of the instrument, etc.");
        common.fieldReplicateModal.setTitle("26.15 Repetition of sample collection and measurement, e.g., triplicate samples.");
        common.uncertaintyModal.setTitle("26.21 Uncertainty of the results (e.g., 1%, 2 μmol/kg), or any pieces of information that are related to the quality control of the variable.");
        common.qualityFlagModal.setTitle("26.22 Describe what the quality control flags stand for, e.g., 2 = good value, 3 = questionable value, 4 = bad value. The use of WOCE quality flags are recommended.");
        common.researcherNameModal.setTitle("26.24.1 The name of the PI, whose research team measured or derived this parameter.");
        common.researcherInstitutionModal.setTitle("26.24.2 The institution of the PI, whose research team measured or derived this parameter.");
        common.fullVariableNameModal.setTitle("The full variable name.");
        common.referenceMethodModal.setTitle("26.23 Citation for the pCO2 method.");
        common.unitsModal.setTitle("26.5 Units of the variable, e.g., μatm.");


    }
    public void fill (Variable pco2d) {
        common.fillCommonVariable(pco2d);
        pco2d.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        pco2d.setStorageMethod(storageMethod.getText());
        pco2d.setPco2Temperature(pco2Temperature.getText());
        pco2d.setGasConcentration(gasConcentration.getText());
        pco2d.setHeadspaceVolume(headspaceVolume.getText());
        pco2d.setStandardGasManufacture(standardGasManufacture.getText());
        pco2d.setGasDetectorManufacture(gasDetectorManufacture.getText());
        pco2d.setGasDetectorModel(gasDetectorModel.getText());
        pco2d.setGasDetectorManufacture(gasDectectorResolution.getText());
        pco2d.setSeawaterVolume(seawaterVolume.getText());
        pco2d.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
        pco2d.setTemperatureMeasurement(temperatureMeasurement.getText());
        pco2d.setTemperatureStandarization(temperatureStandarization.getText());
        pco2d.setStandardGasUncertainties(standardGasUncertainties.getText());
        pco2d.setGasDectectorUncertainty(gasDectectorUncertainty.getText());
        pco2d.setVaporCorrection(vaporCorrection.getText());
    }
    public Variable getPco2d() {
        Variable pco2d = common.getCommonVariable();
        pco2d.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        pco2d.setStorageMethod(storageMethod.getText());
        pco2d.setPco2Temperature(pco2Temperature.getText());
        pco2d.setGasConcentration(gasConcentration.getText());
        pco2d.setHeadspaceVolume(headspaceVolume.getText());
        pco2d.setStandardGasManufacture(standardGasManufacture.getText());
        pco2d.setGasDetectorManufacture(gasDetectorManufacture.getText());
        pco2d.setGasDetectorModel(gasDetectorModel.getText());
        pco2d.setGasDetectorManufacture(gasDectectorResolution.getText());
        pco2d.setSeawaterVolume(seawaterVolume.getText());
        pco2d.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
        pco2d.setTemperatureMeasurement(temperatureMeasurement.getText());
        pco2d.setTemperatureStandarization(temperatureStandarization.getText());
        pco2d.setStandardGasUncertainties(standardGasUncertainties.getText());
        pco2d.setGasDectectorUncertainty(gasDectectorUncertainty.getText());
        pco2d.setVaporCorrection(vaporCorrection.getText());
        return pco2d;
    }
    public void show(Variable pco2d) {
        common.show(pco2d);
        if (pco2d.getFreqencyOfStandardization() != null) {
            freqencyOfStandardization.setText(pco2d.getFreqencyOfStandardization());
        }
        if (pco2d.getStorageMethod() != null) {
            storageMethod.setText(pco2d.getStorageMethod());
        }
        if (pco2d.getPco2Temperature() != null) {
            pco2Temperature.setText(pco2d.getPco2Temperature());
        }
        if (pco2d.getGasConcentration() != null) {
            gasConcentration.setText(pco2d.getGasConcentration());
        }
        if (pco2d.getHeadspaceVolume() != null) {
            headspaceVolume.setText(pco2d.getHeadspaceVolume());
        }
        if (pco2d.getStandardGasManufacture() != null) {
            standardGasManufacture.setText(pco2d.getStandardGasManufacture());
        }
        if (pco2d.getGasDetectorManufacture() != null) {
            gasDetectorManufacture.setText(pco2d.getGasDetectorManufacture());
        }
        if (pco2d.getGasDetectorModel() != null) {
            gasDetectorModel.setText(pco2d.getGasDetectorModel());
        }
        if (pco2d.getGasDetectorManufacture() != null) {
            gasDectectorResolution.setText(pco2d.getGasDetectorManufacture());
        }
        if (pco2d.getSeawaterVolume() != null) {
            seawaterVolume.setText(pco2d.getSeawaterVolume());
        }
        if (pco2d.getTemperatureCorrectionMethod() != null) {
            temperatureCorrectionMethod.setText(pco2d.getTemperatureCorrectionMethod());
        }
        if (pco2d.getTemperatureMeasurement() != null) {
            temperatureMeasurement.setText(pco2d.getTemperatureMeasurement());
        }
        if (pco2d.getTemperatureStandarization() != null) {
            temperatureStandarization.setText(pco2d.getTemperatureStandarization());
        }
        if (pco2d.getStandardGasUncertainties() != null) {
            standardGasUncertainties.setText(pco2d.getStandardGasUncertainties());
        }
        if (pco2d.getGasDectectorUncertainty() != null) {
            gasDectectorUncertainty.setText(pco2d.getGasDectectorUncertainty());
        }
        if (pco2d.getVaporCorrection() != null) {
            vaporCorrection.setText(pco2d.getVaporCorrection());
        }
    }
    public ClickHandler saveIt = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            // For some reason this returns a "0" in debug mode.
            String valid = String.valueOf( form.validate());
            String warning = Constants.NOT_COMPLETE;
            NotifyType type = NotifyType.WARNING;
            if ( isDirty() && common.measured.getValue() == null  ) {
                valid = "false";
                warning = Constants.MEASURED;
                type = NotifyType.DANGER;
            }
            if ( isDirty() && common.observationDetail.getValue() == null ) {
                valid="false";
                warning = Constants.DETAILS;
                type = NotifyType.DANGER;
            }
            if ( valid.equals("false") ||
                    valid.equals("0")) {
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(type);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(warning, settings);
            } else {
                common.eventBus.fireEventFromSource(new SectionSave(null, Constants.SECTION_PCO2D), Pco2dPanel.this);
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.SUCCESS);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.COMPLETE, settings);
            }
        }
    };
    public boolean isDirty(Variable original) {
       boolean isDirty =
           original == null ?
           isDirty() :
           common.isDirty(original) ||
           isDirty(freqencyOfStandardization, original.getFreqencyOfStandardization() ) ||
           isDirty(storageMethod, original.getStorageMethod() ) ||
           isDirty(pco2Temperature, original.getPco2Temperature() ) ||
           isDirty(gasConcentration, original.getGasConcentration() ) ||
           isDirty(headspaceVolume, original.getHeadspaceVolume() ) ||
           isDirty(standardGasManufacture, original.getStandardGasManufacture() ) ||
           isDirty(gasDetectorManufacture, original.getGasDetectorManufacture() ) ||
           isDirty(gasDetectorModel, original.getGasDetectorModel() ) ||
           isDirty(gasDectectorResolution, original.getGasDectectorResolution() ) ||
           isDirty(seawaterVolume, original.getSeawaterVolume() ) ||
           isDirty(temperatureCorrectionMethod, original.getTemperatureCorrectionMethod() ) ||
           isDirty(temperatureMeasurement, original.getTemperatureMeasurement() ) ||
           isDirty(temperatureStandarization, original.getTemperatureStandarization() ) ||
           isDirty(standardGasUncertainties, original.getStandardGasUncertainties() ) ||
           isDirty(gasDectectorUncertainty, original.getGasDectectorUncertainty() ) ||
           isDirty(vaporCorrection, original.getVaporCorrection() );
       return isDirty;
    }
    public boolean isDirty() {
        if ( common.isDirty() ) {
            return true;
        }
        if (freqencyOfStandardization.getText() != null && !freqencyOfStandardization.getText().isEmpty() ) {
            return true;
        }
        if (storageMethod.getText() != null && !storageMethod.getText().isEmpty() ) {
            return true;
        }
        if (pco2Temperature.getText() != null && !pco2Temperature.getText().isEmpty() ) {
            return true;
        }
        if (gasConcentration.getText() != null && !gasConcentration.getText().isEmpty() ) {
            return true;
        }
        if (headspaceVolume.getText() != null && !headspaceVolume.getText().isEmpty() ) {
            return true;
        }
        if (standardGasManufacture.getText() != null && !standardGasManufacture.getText().isEmpty() ) {
            return true;
        }
        if (gasDetectorManufacture.getText() != null && !gasDetectorManufacture.getText().isEmpty() ) {
            return true;
        }
        if (gasDetectorModel.getText() != null && !gasDetectorModel.getText().isEmpty() ) {
            return true;
        }
        if (gasDectectorResolution.getText() != null && !gasDectectorResolution.getText().isEmpty() ) {
            return true;
        }
        if (seawaterVolume.getText() != null && !seawaterVolume.getText().isEmpty() ) {
            return true;
        }
        if (temperatureCorrectionMethod.getText() != null && !temperatureCorrectionMethod.getText().isEmpty() ) {
            return true;
        }
        if (temperatureMeasurement.getText() != null && !temperatureMeasurement.getText().isEmpty() ) {
            return true;
        }
        if (temperatureStandarization.getText() != null && !temperatureStandarization.getText().isEmpty() ) {
            return true;
        }
        if (standardGasUncertainties.getText() != null && !standardGasUncertainties.getText().isEmpty() ) {
            return true;
        }
        if (gasDectectorUncertainty.getText() != null && !gasDectectorUncertainty.getText().isEmpty() ) {
            return true;
        }
        if (vaporCorrection.getText() != null && !vaporCorrection.getText().isEmpty() ) {
            return true;
        }
        return false;
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
    public void reset() {
        form.reset();
    }
}

