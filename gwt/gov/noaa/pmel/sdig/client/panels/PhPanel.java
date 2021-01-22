package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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
 * Created by rhs on 3/22/17.
 */
public class PhPanel extends Composite implements GetsDirty<Variable> {

    @UiField
    public CommonVariablePanel common;

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

    @UiField
    Form form;

    @UiField
    Button save;

    interface PhPanelUiBinder extends UiBinder<HTMLPanel, PhPanel> {
    }

    private static PhPanel.PhPanelUiBinder ourUiBinder = GWT.create(PhPanel.PhPanelUiBinder.class);

    public PhPanel() {

        initWidget(ourUiBinder.createAndBindUi(this));
        setDefaults();
        // common.abbreviation.setEnabled(false);
        common.fullVariableName.setEnabled(false);
        common.heading.setText("Enter the Information for pH.");

        // 005 Variable unit
        common.unitsForm.setVisible(false);
        common.units.setAllowBlank(true);


        common.abbreviationModal.setTitle("24.1 Column header name of the variable in the data files, e.g., pH");
        common.observationTypeModal.setTitle("24.2 How the variable is observed, e.g., surface underway, profile, time series, model output, etc. For experimental data, this could be: laboratory experiment, pelagic mesocosm, benthic mesocosm, benthic FOCE type studies, natural pertubration site studies, etc");
        common.manipulationMethodModal.setTitle("24.4 In perturbation experiments, seawater carbonate chemistry can be manipulated by different techniques, such as bubbling CO2, adding acids or bases, etc.");
        common.observationDetailModal.setTitle("24.3 Whether the variable belong to an in-situ observed variable, or a manipulation condition variable, or a response variable in a biological experimental study.");
        common.measuredModal.setTitle("24.5 Whether the variable is measured in-situ, or calculated from other variables");
        common.calculationMethodModal.setTitle("24.6 Variables can be calculated using different sets of constants or different software.");
        common.samplingInstrumentModal.setTitle("24.7 Instrument that is used to collect water samples, or deploy sensors, etc. For example, a Niskin bottle, pump, CTD, etc is a sampling instrument.");
        common.analyzingInstrumentModal.setTitle("24.8 Instrument that is used to analyze the water samples collected with the 'sampling instrument', or the sensors that are mounted on the 'sampling instrument' to measure the water body continuously. For example, a coulometer, winkler titrator, spectrophotometer, pH meter, thermosalinograph, oxygen sensor, YSI Multiparameter Meter, etc is an analyzing instrument. We encourage you to document as much details (such as the make, model, resolution, precisions, etc) of the instrument as you can here.");
        common.detailedInformationModal.setTitle("24.11 Detailed description of the sampling and analyzing procedures.");
        common.fieldReplicateModal.setTitle("24.12 Repetition of sample collection and measurement, e.g., triplicate samples.");
        common.uncertaintyModal.setTitle("24.16 Uncertainty of the results (e.g., 1%, 2 μmol/kg), or any pieces of information that are related to the quality control of the variable.");
        common.qualityFlagModal.setTitle("24.17 Describe what the quality control flags stand for, e.g., 2 = good value, 3 = questionable value, 4 = bad value. The use of WOCE quality flags are recommended.");
        common.researcherNameModal.setTitle("24.19.1 The name of the PI, whose research team measured or derived this parameter.");
        common.researcherInstitutionModal.setTitle("24.19.2 The institution of the PI, whose research team measured or derived this parameter.");
        common.fullVariableNameModal.setTitle("The full name of the variable.");
        common.referenceMethodModal.setTitle("24.18 Citation for the pH method.");

        save.addClickHandler(saveIt);
    }

    private void setDefaults() {
        common.isBig5 = true;
        common.abbreviation.setText("pH");
        common.fullVariableName.setText("pH total");
    }
    public void fill( Variable ph ) {
        common.fillCommonVariable(ph);
        ph.setStandardizationTechnique(standardizationTechnique.getText().trim());
        ph.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        ph.setPhTemperature(pHtemperature.getText());
        ph.setPhScale(pHscale.getText());
        ph.setPhStandards(pHstandards.getText());
        ph.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
        ph.setTemperatureMeasurement(temperatureMeasurement.getText());
        ph.setTemperatureStandarization(temperatureStandarization.getText());
    }

    public Variable getPh() {
        Variable ph = common.getCommonVariable();
        ph.setStandardizationTechnique(standardizationTechnique.getText());
        ph.setStandardizationTechnique(standardizationTechnique.getText().trim());
        ph.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        ph.setPhTemperature(pHtemperature.getText());
        ph.setPhScale(pHscale.getText());
        ph.setPhStandards(pHstandards.getText());
        ph.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
        ph.setTemperatureMeasurement(temperatureMeasurement.getText());
        ph.setTemperatureStandarization(temperatureStandarization.getText());
        return ph;
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
                common.eventBus.fireEventFromSource(new SectionSave(getPh(), Constants.SECTION_PH), PhPanel.this);
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.SUCCESS);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.COMPLETE, settings);
            }
        }
    };

    public boolean isDirty(Variable original) {
        boolean isDirty = original == null ?
            isDirty() :
            common.isDirty(original) ||
            isDirty( standardizationTechnique, original.getStandardizationTechnique() ) ||
            isDirty( freqencyOfStandardization, original.getFreqencyOfStandardization() ) ||
            isDirty( pHtemperature, original.getPhTemperature() ) ||
            isDirty( pHscale, original.getPhScale() ) ||
            isDirty( pHstandards, original.getPhStandards() ) ||
            isDirty( temperatureCorrectionMethod, original.getTemperatureCorrectionMethod() ) ||
            isDirty( temperatureMeasurement, original.getTemperatureMeasurement() ) ||
            isDirty( temperatureStandarization, original.getTemperatureStandarization() );
        return isDirty;
    }

    public boolean isDirty() {

        if ( common.isDirty() ) {
            return true;
        }
        if ( standardizationTechnique.getText().trim() != null && !standardizationTechnique.getText().isEmpty() ) {
            return true;
        }

        if ( freqencyOfStandardization.getText().trim() != null && !freqencyOfStandardization.getText().isEmpty() ) {
            return true;
        }
        if ( pHtemperature.getText().trim() != null && !pHtemperature.getText().isEmpty() ) {
            return true;
        }
        if ( pHscale.getText().trim() != null && !pHscale.getText().isEmpty() ) {
            return true;
        }
        if (pHstandards.getText().trim() != null && !pHstandards.getText().isEmpty()) {
            return true;
        }
        if (temperatureCorrectionMethod.getText().trim() != null && !temperatureCorrectionMethod.getText().isEmpty() ) {
            return true;
        }
        if ( temperatureMeasurement.getText().trim() != null && !temperatureMeasurement.getText().isEmpty() ) {
            return true;
        }
        if ( temperatureStandarization.getText().trim() != null && !temperatureStandarization.getText().isEmpty() ) {
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
    public void show( Variable ph ) {
        common.show(ph);
        if ( ph.getStandardizationTechnique() != null ) {
            standardizationTechnique.setText(ph.getStandardizationTechnique());
        }

        if ( ph.getFreqencyOfStandardization() != null ) {
            freqencyOfStandardization.setText(ph.getFreqencyOfStandardization());
        }

        if ( ph.getPhTemperature() != null ) {
            pHtemperature.setText(ph.getPhTemperature());
        }

        if ( ph.getPhScale() != null ) {
            pHscale.setText(ph.getPhScale());
        }

        if ( ph.getPhStandards() != null ) {
            pHstandards.setText(ph.getPhStandards());
        }

        if ( ph.getTemperatureCorrectionMethod() != null ) {
            temperatureCorrectionMethod.setText(ph.getTemperatureCorrectionMethod());
        }

        if ( ph.getTemperatureMeasurement() != null ) {
            temperatureMeasurement.setText(ph.getTemperatureMeasurement());
        }

        if ( ph.getTemperatureStandarization() != null ) {
            temperatureStandarization.setText(ph.getTemperatureStandarization());
        }
    }
}
