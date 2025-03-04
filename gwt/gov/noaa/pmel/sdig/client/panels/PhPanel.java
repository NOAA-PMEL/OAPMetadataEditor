package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.DbItem;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhs on 3/22/17.
 */
public class PhPanel extends FormPanel<Variable> implements HasDefault<Variable> {

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
//    @UiField
    ButtonDropDown pHscale;

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
    Button save;
    @UiField
    Button clear;
    private List<String> messages = new ArrayList<>();

    interface PhPanelUiBinder extends UiBinder<HTMLPanel, PhPanel> {
    }

    private static final PhPanel.PhPanelUiBinder ourUiBinder = GWT.create(PhPanel.PhPanelUiBinder.class);

    private static final String SCALE_TOTAL = "Total scale (T)";
    private static final String SCALE_SEAWATER = "Seawater scale (SWS)";
    private static final String SCALE_FREE = "“Free” hydrogen ion content scale (F)";
    private static final String SCALE_NBS = "NBS scale (NBS or NIST)";

//    public static final String PhAbbrevDEFAULT = "pH";
    public static final String PhNameDEFAULT = "pH";

    public Variable getDefault() {
        Variable defaultVariable = new Variable();
//        defaultVariable.setAbbreviation(PhAbbrevDEFAULT);
        defaultVariable.setFullVariableName(PhNameDEFAULT);
        return defaultVariable;
    }
    public PhPanel() {
        super("ph");
        initWidget(ourUiBinder.createAndBindUi(this));
        // common.abbreviation.setEnabled(false);
        common.fullVariableName.setEnabled(false);
        common.heading.setText("Enter the Information for pH.");

        // 005 Variable unit
        common.unitsForm.setVisible(false);
        common.units.setAllowBlank(true);
        common.pHscaleForm.setVisible(true);
        pHscale = common.pHscale;
        setDefaults();
        setDbItem(getDefault());

        List<String> scaleNames = new ArrayList<>();
        List<String> scaleValues = new ArrayList<>();

        scaleNames.add(SCALE_TOTAL);
        scaleValues.add(SCALE_TOTAL);
//        scaleValues.add(ButtonDropDown.standardizeValue(SCALE_TOTAL));
        scaleNames.add(SCALE_SEAWATER);
        scaleValues.add(SCALE_SEAWATER);
//        scaleValues.add(ButtonDropDown.standardizeValue(SCALE_SEAWATER));
        scaleNames.add(SCALE_FREE);
        scaleValues.add(SCALE_FREE);
//        scaleValues.add(ButtonDropDown.standardizeValue(SCALE_FREE));
        scaleNames.add(SCALE_NBS);
        scaleValues.add(SCALE_NBS);
//        scaleValues.add(ButtonDropDown.standardizeValue(SCALE_NBS));
        pHscale.init("ph Scale ", false, scaleNames, scaleValues);

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
        clear.addClickHandler(clearIt);
    }

    private void setDefaults() {
        common.isBig5 = true;
//        common.abbreviation.setText(PhAbbrevDEFAULT);
        common.fullVariableName.setText(PhNameDEFAULT);
        pHscale.reset();
    }
    public void fill( Variable ph ) {
        common.fillCommonVariable(ph);
        ph.setStandardizationTechnique(standardizationTechnique.getText().trim());
        ph.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        ph.setPhTemperature(pHtemperature.getText());
        ph.setPhScale(pHscale.getValue());
        ph.setPhStandards(pHstandards.getText());
        ph.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
        ph.setTemperatureMeasurement(temperatureMeasurement.getText());
        ph.setTemperatureStandarization(temperatureStandarization.getText());
    }

    public Variable getPh() {
        return getPh(false);
    }
    public Variable getPh(boolean validate) {
        if ( validate && ! this.isValid()) {
            StringBuilder msgBldr = new StringBuilder();
            String newLine = "";
            for (String msg : messages) {
                msgBldr.append(newLine).append(msg);
                newLine = "<br/>";
            }
            messages.clear();
            throw new IllegalStateException(msgBldr.toString());
        }
        Variable ph = common.getCommonVariable();
        ph.setStandardizationTechnique(standardizationTechnique.getText());
        ph.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        ph.setPhTemperature(pHtemperature.getText());
        ph.setPhScale(pHscale.getValue());
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

    public boolean isDirty() {
        return isDirty(getDbItem());
                // common.abbreviation.getValue() != getDbItem().getAbbreviation() ||
                // common.pHscale.getValue() != getDbItem().getPhScale();
    }
    public boolean isDirty(Variable original) {
        OAPMetadataEditor.debugLog("phPanel.isDirty: " + original);
        boolean isDirty = original == null ?
            hasContent() :
            common.isDirty(original) ||
            isDirty( standardizationTechnique, original.getStandardizationTechnique() ) ||
            isDirty( freqencyOfStandardization, original.getFreqencyOfStandardization() ) ||
            isDirty( pHtemperature, original.getPhTemperature() ) ||
            isDirty( pHscale.getValue(), original.getPhScale() ) ||
            isDirty( pHstandards, original.getPhStandards() ) ||
            isDirty( temperatureCorrectionMethod, original.getTemperatureCorrectionMethod() ) ||
            isDirty( temperatureMeasurement, original.getTemperatureMeasurement() ) ||
            isDirty( temperatureStandarization, original.getTemperatureStandarization() );
        return isDirty;
    }

    public boolean hasContent() {
        OAPMetadataEditor.debugLog("@pH.hasContent()");
        if ( common.hasContent()) {
            return true;
        }
        if ( standardizationTechnique.getText() != null && !standardizationTechnique.getText().trim().isEmpty() ) {
            return true;
        }

        if ( freqencyOfStandardization.getText() != null && !freqencyOfStandardization.getText().trim().isEmpty() ) {
            return true;
        }
        if ( pHtemperature.getText() != null && !pHtemperature.getText().trim().isEmpty() ) {
            return true;
        }
        if ( pHscale.getValue() != null && !pHscale.getValue().isEmpty() ) {
            return true;
        }
        if (pHstandards.getText() != null && !pHstandards.getText().trim().isEmpty()) {
            return true;
        }
        if (temperatureCorrectionMethod.getText() != null && !temperatureCorrectionMethod.getText().trim().isEmpty() ) {
            return true;
        }
        if ( temperatureMeasurement.getText() != null && !temperatureMeasurement.getText().trim().isEmpty() ) {
            return true;
        }
        if ( temperatureStandarization.getText() != null && !temperatureStandarization.getText().trim().isEmpty() ) {
            return true;
        }

        return false;
    }
    public void reset() {
        form.reset();
        common.reset();
        setDefaults();
    }
    @Override
    public boolean isValid() {
        messages.clear();
        boolean isValid = super.isValid();
        boolean emptyAbbrev = common.abbreviation.getValue().trim().isEmpty();
        boolean validScale = validScale();
        validScale = emptyAbbrev || validScale;
        if ( ! validScale ) {
            messages.add("Invalid pH scale.");
            setDropButtonError(true, pHscale);
        }
        boolean hasContent = this.hasContent();
        if ( hasContent && emptyAbbrev ) {
            messages.add("Missing pH variable abbreviation.");
            setFieldError(true, common.abbreviation);
            isValid = false;
        } else {
            setFieldError(false, common.abbreviation);
        }
        isValid = isValid && validScale;
        return isValid;
    }

    private boolean validScale() {
        String phValue = pHscale.getValue();
        GWT.log("phScale: " + phValue);
        return phValue != "";
    }

    public void show( Variable ph ) {
        setDbItem(ph);
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
            String phScale = ph.getPhScale();
            String pdown = phScale.toLowerCase();
            if (pdown.contains("total")) {
                phScale = SCALE_TOTAL;
            } else if ( pdown.contains("seawater")) {
                phScale = SCALE_SEAWATER;
            } else if ( pdown.contains("free") ||
                        pdown.contains("hydrogen")) {
                phScale = SCALE_FREE;
            } else if ( pdown.contains("nbs") ||
                        pdown.contains("nist")) {
                phScale = SCALE_NBS;
            }
            pHscale.setSelected(phScale);
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
