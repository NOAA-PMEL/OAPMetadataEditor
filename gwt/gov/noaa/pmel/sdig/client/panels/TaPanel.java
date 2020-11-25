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
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
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
public class TaPanel extends Composite implements GetsDirty<Variable> {
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

    // 014 CRM manufacturer
    @UiField
    TextBox crmManufacture;

    // 015 Batch Number
    @UiField
    TextBox batchNumber;

    // 017 Poison used to kill the sample
    @UiField
    TextBox poison;

    // 018 Poison volume
    @UiField
    TextBox poisonVolume;

    // 019 Poisoning correction description
    @UiField
    TextBox poisonDescription;

    // 027 Cell type (open or closed)
    @UiField
    ButtonDropDown cellType;

    // 029 Curve fitting method
    @UiField
    TextBox curveFittingMethod;

    // 042 Magnitude of blank correction
    @UiField
    TextBox magnitudeOfBlankCorrection;

    // 055 Type of titration
    @UiField
    TextBox titrationType;

    @UiField
    CommonVariablePanel common;

    interface VariablePanelUiBinder extends UiBinder<HTMLPanel, TaPanel> {
    }

    private static TaPanel.VariablePanelUiBinder ourUiBinder = GWT.create(TaPanel.VariablePanelUiBinder.class);

    public TaPanel() {

        initWidget(ourUiBinder.createAndBindUi(this));

        setDefaults();
        // common.abbreviation.setEnabled(false);
        common.fullVariableName.setEnabled(false);
        common.heading.setText("Enter the Information for Total Alkalinity (TA).");
        save.addClickHandler(saveIt);
        common.abbreviationModal.setTitle("23.1 Column header name of the variable in the data files, e.g., TA, Alk, etc.");
        common.observationTypeModal.setTitle("23.2 How the variable is observed, e.g., surface underway, profile, time series, model output, etc. For experimental data, this could be: laboratory experiment, pelagic mesocosm, benthic mesocosm, benthic FOCE type studies, natural pertubration site studies, etc");
        common.manipulationMethodModal.setTitle("23.4 In perturbation experiments, seawater carbonate chemistry can be manipulated by different techniques, such as bubbling CO2, adding acids or bases, etc.");
        common.observationDetailModal.setTitle("23.3 Whether the variable belong to an in-situ observed variable, or a manipulation condition variable, or a response variable in a \n" +
                "biological experimental study.");
        common.unitsModal.setTitle("23.5 Units of the variable (e.g., μmol/kg).");
        common.measuredModal.setTitle("23.6 Variable is measured in-situ, or calculated from other variables.");
        common.calculationMethodModal.setTitle("23.7 Variables can be calculated using different sets of constants or different software.");
        common.samplingInstrumentModal.setTitle("23.8 Instrument that is used to collect water samples, or deploy sensors, etc. For example, a Niskin bottle, pump, CTD, etc is a\n" +
                " sampling instrument.");
        common.analyzingInstrumentModal.setTitle("23.9 Instrument that is used to analyze the water samples collected with the 'sampling instrument', or the sensors that are\n" +
                " mounted on the 'sampling instrument' to measure the water body continuously. For example, a coulometer, winkler titrator,\n" +
                " spectrophotometer, pH meter, thermosalinograph, oxygen sensor, YSI Multiparameter Meter, etc is an analyzing instrument.\n" +
                " We encourage you to document as much details (such as the make, model, resolution, precisions, etc) of the instrument as\n" +
                " you can here.");
        common.detailedInformationModal.setTitle("23.13 Detailed description of the sampling and analyzing procedures, including calibration procedures, model number of the\n" +
                " instrument, etc.");
        common.fieldReplicateModal.setTitle("23.14 Repetition of sample collection and measurement, e.g., triplicate samples.");
        common.uncertaintyModal.setTitle("23.18\n" +
                " Uncertainty of the results (e.g., 1%, 2 μmol/kg), or any pieces of information that are related to the quality control of the \n" +
                " variable.");
        common.qualityFlagModal.setTitle("23.19 Describe what the quality control flags stand for, e.g., 2 = good value, 3 = questionable value, 4 = bad value. The use of\n" +
                " WOCE quality flags are recommended.");
        common.researcherNameModal.setTitle("23.21.1 The name of the PI, whose research team measured or derived this parameter.");
        common.researcherInstitutionModal.setTitle("23.21.2 The institution of the PI, whose research team measured or derived this parameter.");
        common.fullVariableNameModal.setTitle("The full name of the variable.");
        common.referenceMethodModal.setTitle("23.20 Citation for the alkalinity method.");

        List<String> name = new ArrayList<String>();
        List<String> value = new ArrayList<String>();
        name.add("Open");
        value.add("open");
        name.add("Closed");
        value.add("closed");
        cellType.init("Cell Type: Open or Closed", name, value);
    }
    private void setDefaults() {
        common.abbreviation.setText("TA");
        common.fullVariableName.setText("Total Alkalinity");
    }
    public Variable getTa() {
        Variable ta = common.getCommonVariable();
        ta.setStandardizationTechnique(standardizationTechnique.getText());
        ta.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        ta.setCrmManufacture(crmManufacture.getText());
        ta.setBatchNumber(batchNumber.getText());
        ta.setPoison(poison.getText());
        ta.setPoisonVolume(poisonVolume.getText());
        ta.setPoisonDescription(poisonDescription.getText());
        ta.setCellType(cellType.getValue());
        ta.setCurveFittingMethod(curveFittingMethod.getText());
        ta.setMagnitudeOfBlankCorrection(magnitudeOfBlankCorrection.getText());
        ta.setTitrationType(titrationType.getText());
        return ta;
    }
    public void fill(Variable ta) {
        common.fillCommonVariable(ta);
        ta.setStandardizationTechnique(standardizationTechnique.getText());
        ta.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        ta.setCrmManufacture(crmManufacture.getText());
        ta.setBatchNumber(batchNumber.getText());
        ta.setPoison(poison.getText());
        ta.setPoisonVolume(poisonVolume.getText());
        ta.setPoisonDescription(poisonDescription.getText());
        ta.setCellType(cellType.getValue());
        ta.setCurveFittingMethod(curveFittingMethod.getText());
        ta.setMagnitudeOfBlankCorrection(magnitudeOfBlankCorrection.getText());
        ta.setTitrationType(titrationType.getText());
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
                common.eventBus.fireEventFromSource(new SectionSave(getTa(), Constants.SECTION_TA), TaPanel.this);
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.SUCCESS);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.COMPLETE, settings);
            }
        }
    };
    public void show(Variable ta) {
        common.show(ta);
        if ( ta.getStandardizationTechnique() != null ) {
            standardizationTechnique.setText(ta.getStandardizationTechnique());
        }

        if ( ta.getFreqencyOfStandardization() != null ) {
            freqencyOfStandardization.setText(ta.getFreqencyOfStandardization());
        }

        if ( ta.getCrmManufacture() != null ) {
            crmManufacture.setText(ta.getCrmManufacture());
        }

        if (ta.getBatchNumber() != null ) {
            batchNumber.setText(ta.getBatchNumber());
        }

        if ( ta.getPoison() != null ) {
            poison.setText(ta.getPoison());
        }

        if ( ta.getPoisonVolume() != null ) {
            poisonVolume.setText(ta.getPoisonVolume());
        }

        if ( ta.getPoisonDescription() != null ) {
            poisonDescription.setText(ta.getPoisonDescription());
        }

        if ( ta.getCellType() != null ) {
            cellType.setSelected(ta.getCellType());
        }

        if ( ta.getCurveFittingMethod() != null ) {
            curveFittingMethod.setText(ta.getCurveFittingMethod());
        }

        if ( ta.getMagnitudeOfBlankCorrection() != null ) {
            magnitudeOfBlankCorrection.setText(ta.getMagnitudeOfBlankCorrection());
        }

        if ( ta.getTitrationType() != null ) {
            titrationType.setText(ta.getTitrationType());
        }
    }
    public boolean isDirty(Variable original) {
        boolean isDirty = original == null ?
            isDirty() :
            common.isDirty(original) ||
            isDirty(standardizationTechnique, original.getStandardizationTechnique() ) ||
            isDirty(freqencyOfStandardization, original.getFreqencyOfStandardization() ) ||
            isDirty(crmManufacture, original.getCrmManufacture() ) ||
            isDirty(batchNumber, original.getBatchNumber() ) ||
            isDirty(poison, original.getPoison() ) ||
            isDirty(poisonVolume, original.getPoisonVolume() ) ||
            isDirty(poisonDescription, original.getPoisonDescription() ) ||
            isDirty(curveFittingMethod, original.getCurveFittingMethod() ) ||
            isDirty(magnitudeOfBlankCorrection, original.getMagnitudeOfBlankCorrection() ) ||
            isDirty(titrationType, original.getTitrationType() );
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
        if (crmManufacture.getText().trim() != null && !crmManufacture.getText().isEmpty() ) {
            return true;
        }
        if (batchNumber.getText().trim() != null && !batchNumber.getText().isEmpty() ) {
            return true;
        }
        if (poison.getText().trim() != null && !poison.getText().isEmpty() ) {
            return true;
        }
        if (poisonVolume.getText().trim() != null && !poisonVolume.getText().isEmpty() ) {
            return true;
        }
        if (poisonDescription.getText().trim() != null && !poisonDescription.getText().isEmpty() ) {
            return true;
        }
        if (curveFittingMethod.getText().trim() != null && !curveFittingMethod.getText().isEmpty() ) {
            return true;
        }
        if (magnitudeOfBlankCorrection.getText().trim() != null && !magnitudeOfBlankCorrection.getText().isEmpty() ) {
            return true;
        }
        if (titrationType.getText().trim() != null && !titrationType.getText().isEmpty() ) {
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
        setDefaults();
    }
}
