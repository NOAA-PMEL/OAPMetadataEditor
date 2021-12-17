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
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;


/**
 * Created by rhs on 3/22/17.
 */
public class Pco2aPanel extends Composite implements GetsDirty<Variable> {
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

    @UiField
    CommonVariablePanel common;

    @UiField
    Button save;

    @UiField
    Form form;


    @UiField
    FormLabel standardizationTechniqueLabel;
    @UiField
    FormLabel freqencyOfStandardizationLabel;

    interface Pco2aPanelUiBinder extends UiBinder<HTMLPanel, Pco2aPanel> {
    }

    private static Pco2aPanel.Pco2aPanelUiBinder ourUiBinder = GWT.create(Pco2aPanel.Pco2aPanelUiBinder.class);

    public Pco2aPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        setDefaults();
        // common.abbreviation.setEnabled(false);
        common.fullVariableName.setEnabled(false);
        common.heading.setText("Enter the Information for pCO2 (fCO2) Autonomous.");
        common.fieldReplicate.setAllowBlank(true);
        common.fieldReplicateForm.setVisible(false);
        save.addClickHandler(saveIt);
        common.abbreviationModal.setTitle("25.1 Column header name of the variable in the data files, e.g., pCO2, etc.");
        common.observationTypeModal.setTitle("25.2 How the variable is observed, e.g., surface underway, profile, time series, model output, etc. For experimental data, this could be: laboratory experiment, pelagic mesocosm, benthic mesocosm, benthic FOCE type studies, natural pertubration site studies, etc");
        common.manipulationMethodModal.setTitle("25.4 In perturbation experiments, seawater carbonate chemistry can be manipulated by different techniques, such as bubbling CO2, adding acids or bases, etc.");
        common.observationDetailModal.setTitle("25.3 Whether the variable belong to an in-situ observed variable, or a manipulation condition variable, or a response variable in a biological experimental study");
        common.measuredModal.setTitle("25.6 Whether the variable is measured in-situ, or calculated from other variables");
        common.calculationMethodModal.setTitle("25.7 Variables can be calculated using different sets of constants or different software.");
        common.samplingInstrumentModal.setTitle("25.8 Instrument that is used to collect water samples, or deploy sensors, etc. For example, a Niskin bottle, pump, CTD, etc is a sampling instrument.");
        common.analyzingInstrumentModal.setTitle("25.11 Instrument that is used to analyze the water samples collected with the sampling instrument , or the sensors that are mounted on the 'sampling instrument' to measure the water body continuously. For example, a coulometer, winkler titrator, spectrophotometer, pH meter, thermosalinograph, oxygen sensor, YSI Multiparameter Meter, etc is an analyzing instrument. We encourage you to document as much details (such as the make, model, resolution, precisions, etc) of the instrument as you can here");
        common.detailedInformationModal.setTitle("25.12 Detailed description of the sampling and analyzing procedures, including calibration procedures, model number of the instrument, etc.");
        common.fieldReplicateModal.setTitle("??? Does apply here ???");
        common.uncertaintyModal.setTitle("25.20 Uncertainty of the results (e.g., 1%, 2 μmol/kg), or any pieces of information that are related to the quality control of the variable.");
        common.qcAppliedModal.setTitle("25.21 Describe what the quality control flags stand for, e.g., 2 = good value, 3 = questionable value, 4 = bad value. The use of WOCE quality flags are recommended.");
        common.researcherNameModal.setTitle("25.23.1 The name of the PI, whose research team measured or derived this parameter.");
        common.researcherInstitutionModal.setTitle("25.23.2 The institution of the PI, whose research team measured or derived this parameter.");
        common.fullVariableNameModal.setTitle("Full variable name.");
        common.referenceMethodModal.setTitle("25.22 Citation for the pCO2 method.");
        common.unitsModal.setTitle("25.5 Units of the variable, e.g., μatm.");

        common.qcSchemeNameModal.setTitle("22.7 Indicate if quality control procedures were applied.");
        common.qcVariableNameModal.setTitle("22.8 Column header name of the data quality flag scheme applied in the data files, e.g. QC, Quality, etc.");
        common.sopChangesModal.setTitle("20.2 Indicate if any changes were made to the method as described in the SOP, such as changes in the sample collection method, changes in storage of the sample, different volume, changes to the CRM used, etc. Please provide a detailed list of  all of the changes made.");
        common.collectionMethodModal.setTitle("21.4 Method that is used to collect water samples, or deploy sensors, etc. For example, bottle collection with a Niskin bottle, pump, CTD, etc is a collection method.");
        common.analyzingInformationModal.setTitle("20.6 Detailed description of the analyzing procedures, including the citation of the SOP used for the analysis (e.g. SOP 7;  Dickson, A.G., Sabine, C.L. and Christian, J.R.  2007.  Guide to Best Practices for Ocean CO2  Measurements).");

//        if (OAPMetadataEditor.getIsSocatParam()) {
            common.qcAppliedLabel.setText("Data quality scheme (name of scheme)");
            standardizationTechniqueLabel.setText("Calibration method");
            freqencyOfStandardizationLabel.setText("Frequency of calibration");
//        }

    }
    private void setDefaults() {
        common.isBig5 = true;
        common.abbreviation.setText("pCO2a");
        common.fullVariableName.setText("pco2 (fco2) autonomous");
    }
    public Variable getPco2a() {
        Variable pco2a = common.getCommonVariable();
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
        pco2a.setStandardGasUncertainties(standardGasUncertainties.getText());
        pco2a.setGasDectectorUncertainty(gasDectectorUncertainty.getText());
        pco2a.setVented(vented.getText());
        pco2a.setFlowRate(flowRate.getText());
        pco2a.setVaporCorrection(vaporCorrection.getText());
        return pco2a;
    }
    public void fill(Variable pco2a) {
        common.fillCommonVariable(pco2a);
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
        pco2a.setStandardGasUncertainties(standardGasUncertainties.getText());
        pco2a.setGasDectectorUncertainty(gasDectectorUncertainty.getText());
        pco2a.setVented(vented.getText());
        pco2a.setFlowRate(flowRate.getText());
        pco2a.setVaporCorrection(vaporCorrection.getText());
    }
    public void show(Variable pco2a) {
        common.show(pco2a);
        if (pco2a.getStandardizationTechnique() != null ) {
            standardizationTechnique.setText(pco2a.getStandardizationTechnique());
        }

        if ( pco2a.getFreqencyOfStandardization() != null ) {
            freqencyOfStandardization.setText(pco2a.getFreqencyOfStandardization());
        }

        if ( pco2a.getPco2Temperature() != null ) {
            pco2Temperature.setText(pco2a.getPco2Temperature());
        }

        if ( pco2a.getGasConcentration() != null ) {
            gasConcentration.setText(pco2a.getGasConcentration());
        }

        if ( pco2a.getIntakeDepth() != null ) {
            intakeDepth.setText(pco2a.getIntakeDepth());
        }

        if (pco2a.getDryingMethod() != null ) {
            dryingMethod.setText(pco2a.getDryingMethod());
        }

        if ( pco2a.getEquilibratorType() != null ) {
            equilibratorType.setText(pco2a.getEquilibratorType());
        }

        if ( pco2a.getEquilibratorVolume() != null ) {
            equilibratorVolume.setText(pco2a.getEquilibratorVolume());
        }

        if ( pco2a.getGasFlowRate() != null ) {
            gasFlowRate.setText(pco2a.getGasFlowRate());
        }

        if ( pco2a.getEquilibratorPressureMeasureMethod() != null ) {
            equilibratorPressureMeasureMethod.setText(pco2a.getEquilibratorPressureMeasureMethod());
        }

        if ( pco2a.getEquilibratorTemperatureMeasureMethod() != null ) {
            equilibratorTemperatureMeasureMethod.setText(pco2a.getEquilibratorTemperatureMeasureMethod());
        }

        if ( pco2a.getIntakeLocation() != null ) {
            intakeLocation.setText(pco2a.getIntakeLocation());
        }

        if ( pco2a.getStandardGasManufacture() != null ) {
            standardGasManufacture.setText(pco2a.getStandardGasManufacture());
        }

        if ( pco2a.getGasDetectorManufacture() != null ) {
            gasDetectorManufacture.setText(pco2a.getGasDetectorManufacture());
        }


        if ( pco2a.getGasDetectorModel() != null ) {
            gasDetectorModel.setText(pco2a.getGasDetectorModel());
        }

        if ( pco2a.getGasDectectorResolution() != null ) {
            gasDectectorResolution.setText(pco2a.getGasDectectorResolution());
        }

        if ( pco2a.getTemperatureCorrectionMethod() != null ) {
            temperatureCorrectionMethod.setText(pco2a.getTemperatureCorrectionMethod());
        }

        if ( pco2a.getStandardGasUncertainties() != null ) {
            standardGasUncertainties.setText(pco2a.getStandardGasUncertainties());
        }

        if ( pco2a.getGasDectectorUncertainty() != null ) {
            gasDectectorUncertainty.setText(pco2a.getGasDectectorUncertainty());
        }

        if ( pco2a.getVented() != null ) {
            vented.setText(pco2a.getVented());
        }

        if ( pco2a.getFlowRate() != null ) {
            flowRate.setText(pco2a.getFlowRate());
        }

        if ( pco2a.getVaporCorrection() != null ) {
            vaporCorrection.setText(pco2a.getVaporCorrection());
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
                common.eventBus.fireEventFromSource(new SectionSave(getPco2a(), Constants.SECTION_PCO2A), Pco2aPanel.this);
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.SUCCESS);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.COMPLETE, settings);
            }        }
    };
    public boolean isDirty(Variable original) {
        boolean isDirty =
            original == null ?
            isDirty() :
            common.isDirty(original) ||
            isDirty(standardizationTechnique, original.getStandardizationTechnique() ) ||
            isDirty(freqencyOfStandardization, original.getFreqencyOfStandardization() ) ||
            isDirty(pco2Temperature, original.getPco2Temperature() ) ||
            isDirty(gasConcentration, original.getGasConcentration() ) ||
            isDirty(intakeDepth, original.getIntakeDepth() ) ||
            isDirty(dryingMethod, original.getDryingMethod() ) ||
            isDirty(equilibratorType, original.getEquilibratorType() ) ||
            isDirty(equilibratorVolume, original.getEquilibratorVolume() ) ||
            isDirty(gasFlowRate, original.getGasFlowRate() ) ||
            isDirty(equilibratorPressureMeasureMethod, original.getEquilibratorPressureMeasureMethod() ) ||
            isDirty(equilibratorTemperatureMeasureMethod, original.getEquilibratorTemperatureMeasureMethod() ) ||
            isDirty(intakeLocation, original.getIntakeLocation() ) ||
            isDirty(standardGasManufacture, original.getStandardGasManufacture() ) ||
            isDirty(gasDetectorManufacture, original.getGasDetectorManufacture() ) ||
            isDirty(gasDetectorModel, original.getGasDetectorModel() ) ||
            isDirty(gasDectectorResolution, original.getGasDectectorResolution() ) ||
            isDirty(temperatureCorrectionMethod, original.getTemperatureCorrectionMethod() ) ||
            isDirty(standardGasUncertainties, original.getStandardGasUncertainties() ) ||
            isDirty(gasDectectorUncertainty, original.getGasDectectorUncertainty() ) ||
            isDirty(vented, original.getVented() ) ||
            isDirty(flowRate, original.getFlowRate() ) ||
            isDirty(vaporCorrection, original.getVaporCorrection() );
        return isDirty;
    }
    public boolean isDirty() {
        if ( common.isDirty() ) {
            return true;
        }
        if (standardizationTechnique.getText().trim() != null && !standardizationTechnique.getText().isEmpty() ) {
            return true;
        }
        if (freqencyOfStandardization.getText().trim() != null && !freqencyOfStandardization.getText().isEmpty() ) {
            return true;
        }
        if (pco2Temperature.getText().trim() != null && !pco2Temperature.getText().isEmpty() ) {
            return true;
        }
        if (gasConcentration.getText().trim() != null && !gasConcentration.getText().isEmpty() ) {
            return true;
        }
        if (intakeDepth.getText().trim() != null && !intakeDepth.getText().isEmpty() ) {
            return true;
        }
        if (dryingMethod.getText().trim() != null && !dryingMethod.getText().isEmpty() ) {
            return true;
        }
        if (equilibratorType.getText().trim() != null && !equilibratorType.getText().isEmpty() ) {
            return true;
        }
        if (equilibratorVolume.getText().trim() != null && !equilibratorVolume.getText().isEmpty() ) {
            return true;
        }
        if (gasFlowRate.getText().trim() != null && !gasFlowRate.getText().isEmpty() ) {
            return true;
        }
        if (equilibratorPressureMeasureMethod.getText().trim() != null && !equilibratorPressureMeasureMethod.getText().isEmpty() ) {
            return true;
        }
        if (equilibratorTemperatureMeasureMethod.getText().trim() != null && !equilibratorTemperatureMeasureMethod.getText().isEmpty() ) {
            return true;
        }
        if (intakeLocation.getText().trim() != null && !intakeLocation.getText().isEmpty() ) {
            return true;
        }
        if (standardGasManufacture.getText().trim() != null && !standardGasManufacture.getText().isEmpty() ) {
            return true;
        }
        if (gasDetectorManufacture.getText().trim() != null && !gasDetectorManufacture.getText().isEmpty() ) {
            return true;
        }
        if (gasDetectorModel.getText().trim() != null && !gasDetectorModel.getText().isEmpty() ) {
            return true;
        }
        if (gasDectectorResolution.getText().trim() != null && !gasDectectorResolution.getText().isEmpty() ) {
            return true;
        }
        if (temperatureCorrectionMethod.getText().trim() != null && !temperatureCorrectionMethod.getText().isEmpty() ) {
            return true;
        }
        if (standardGasUncertainties.getText().trim() != null && !standardGasUncertainties.getText().isEmpty() ) {
            return true;
        }
        if (gasDectectorUncertainty.getText().trim() != null && !gasDectectorUncertainty.getText().isEmpty() ) {
            return true;
        }
        if (vented.getText().trim() != null && !vented.getText().isEmpty() ) {
            return true;
        }
        if (flowRate.getText().trim() != null && !flowRate.getText().isEmpty() ) {
            return true;
        }
        if (vaporCorrection.getText().trim() != null && !vaporCorrection.getText().isEmpty() ) {
            return true;
        }
        return false;
    }
    public void reset() {
        form.reset();
        common.reset();
        setDefaults();
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
}
