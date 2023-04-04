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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhs on 3/8/17.
 */
public class DicPanel extends FormPanel<Variable> implements GetsDirty<Variable> {

    @UiField
    Button save;
    @UiField
    Button clear;

    @UiField
    CommonVariablePanel common;

//  // 012 Standardization technique description
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

    // 017 How were the samples preserved (HgCl2, or others)
    @UiField
    TextBox poison;

    // 018 Concentration and amount of the preservative added
    @UiField
    TextBox poisonVolume;

    // 019 Preservative correction description
    @UiField
    TextBox poisonDescription;

    interface DicPanelUiBinder extends UiBinder<HTMLPanel, DicPanel> {
    }

    private static DicPanel.DicPanelUiBinder ourUiBinder = GWT.create(DicPanel.DicPanelUiBinder.class);


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
DIC: How were the samples preserved (HgCl2, or others)
DIC: Concentration and amount of the preservative
DIC: Preservative correction description
DIC: Uncertainty
DIC: Data quality flag description
DIC: Method reference (citation)
DIC: Researcher Name
DIC: Researcher Institution
         */
        initWidget(ourUiBinder.createAndBindUi(this));

        setDefaults();
//        common.abbreviation.setEnabled(false);
        common.fullVariableName.setEnabled(false);
        common.heading.setText("Enter the Information for Dissolved Inorganic Carbon (DIC).");
        save.addClickHandler(saveIt);
        clear.addClickHandler(clearIt);

        common.abbreviationModal.setTitle("22.1 Column header name of the variable in the data files, e.g., DIC, TCO2, etc.");
        common.observationTypeModal.setTitle("22.2 How the variable is observed, e.g., surface underway, profile, time series, model output, etc. For experimental data, this could be: laboratory experiment, pelagic mesocosm, benthic mesocosm, benthic FOCE type studies, natural pertubration site studies, etc");
        common.manipulationMethodModal.setTitle("22.4 In perturbation experiments, seawater carbonate chemistry can be manipulated by different techniques, such as bubbling CO2, adding acids or bases, etc.");
        common.observationDetailModal.setTitle("22.3 Whether the variable belong to an in-situ observed variable, or a manipulation condition variable, or a response variable in a biological experimental study.");
        common.measuredModal.setTitle("22.6 Whether the variable is measured in-situ, or calculated from other variables.");
        common.calculationMethodModal.setTitle("22.7 Variables can be calculated using different sets of constants or different software.");
        common.samplingInstrumentModal.setTitle("22.8 Instrument that is used to collect water samples, or deploy sensors, etc. For example, a Niskin bottle, pump, CTD, etc is a sampling instrument.");
        common.analyzingInstrumentModal.setTitle("22.9 Instrument that is used to analyze the water samples collected with the 'sampling instrument', or the sensors that are mounted on the 'sampling instrument' to measure the water body continuously. For example, a coulometer, winkler titrator, spectrophotometer, pH meter, thermosalinograph, oxygen sensor, YSI Multiparameter Meter, etc is an analyzing instrument. We encourage you to document as much details (such as the make, model, resolution, precisions, etc) of the instrument as you can here.");
        common.detailedInformationModal.setTitle("22.10 Detailed description of the sampling and analyzing procedures, including calibration procedures, model number of the instrument, etc.");
        common.fieldReplicateModal.setTitle("22.11 Repetition of sample collection and measurement, e.g., triplicate samples.");
        common.uncertaintyModal.setTitle("22.14 Uncertainty of the results (e.g., 1%, 2 μmol/kg), or any pieces of information that are related to the quality control of the variable.");
        common.qualityFlagModal.setTitle("22.15 Describe what the quality control flags stand for, e.g., 2 = good value, 3 = questionable value, 4 = bad value. The use of WOCE quality flags are recommended.");
        common.researcherNameModal.setTitle("22.17.1 The name of the PI, whose research team measured or derived this parameter.");
        common.researcherInstitutionModal.setTitle("22.17.2 The institution of the PI, whose research team measured or derived this parameter.");
        common.fullVariableNameModal.setTitle("The full variable name.");
        common.referenceMethodModal.setTitle("22.16 Citation for the dissolved inorganic carbon method.");
        common.unitsModal.setTitle("22.5 Units of the variable (e.g., μmol/kg).");

    }

    private void setDefaults() {
        common.isBig5 = true;
        common.abbreviation.setText("DIC");
        common.fullVariableName.setText("Dissolved Inorganic Carbon");
    }

    public Variable getDic() {
        Variable dic = common.getCommonVariable();
        dic.setStandardizationTechnique(standardizationTechnique.getText().trim());
        dic.setFreqencyOfStandardization(freqencyOfStandardization.getText().trim());
        dic.setCrmManufacture(crmManufacture.getText().trim());
        dic.setBatchNumber(batchNumber.getText());
        dic.setPoison(poison.getText());
        dic.setPoisonVolume(poisonVolume.getText());
        dic.setPoisonDescription(poisonDescription.getText());
        return dic;
    }

    public void fill(Variable dic) {
        common.fillCommonVariable(dic);
        dic.setStandardizationTechnique(standardizationTechnique.getText().trim());
        dic.setFreqencyOfStandardization(freqencyOfStandardization.getText().trim());
        dic.setCrmManufacture(crmManufacture.getText().trim());
        dic.setBatchNumber(batchNumber.getText());
        dic.setPoison(poison.getText());
        dic.setPoisonVolume(poisonVolume.getText());
        dic.setPoisonDescription(poisonDescription.getText());
    }

    public void show(Variable dic) {
        common.show(dic);
        if ( dic.getStandardizationTechnique() != null ) {
            standardizationTechnique.setText(dic.getStandardizationTechnique());
        }

        if ( dic.getFreqencyOfStandardization() != null ) {
            freqencyOfStandardization.setText(dic.getFreqencyOfStandardization());
        }

        if ( dic.getCrmManufacture() != null ) {
            crmManufacture.setText(dic.getCrmManufacture());
        }

        if ( dic.getBatchNumber() != null ) {
            batchNumber.setText(dic.getBatchNumber());
        }

        if ( dic.getPoison() != null ) {
            poison.setText(dic.getPoison());
        }

        if ( dic.getPoisonVolume() != null ) {
            poisonVolume.setText(dic.getPoisonVolume());
        }

        if (dic.getPoisonDescription() != null ) {
            poisonDescription.setText(dic.getPoisonDescription());
        }

    }

    public ClickHandler saveIt = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            // For some reason this returns a "0" in debug mode.
//TODO fixme           String valid = String.valueOf( form.validate());
            String valid = "true"; // TODO temporary
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
                common.eventBus.fireEventFromSource(new SectionSave(getDic(), Constants.SECTION_DIC), DicPanel.this);
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.SUCCESS);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.COMPLETE, settings);
            }
        }
    };

    public boolean isDirty(Variable original) {
        boolean isDirty;
        isDirty = original == null ?
            isDirty() :
            common.isDirty(original) ||
            isDirty(standardizationTechnique, original.getStandardizationTechnique() ) ||
            isDirty(freqencyOfStandardization, original.getFreqencyOfStandardization() ) ||
            isDirty(crmManufacture, original.getCrmManufacture() ) ||
            isDirty(batchNumber, original.getBatchNumber() ) ||
            isDirty(poison, original.getPoison() ) ||
            isDirty(poisonVolume, original.getPoisonVolume() ) ||
            isDirty(poisonDescription, original.getPoisonDescription() );
        return isDirty;
    }

    public boolean hasContent() {
        return isDirty();
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
        if (batchNumber != null && !batchNumber.getText().isEmpty() ) {
            return true;
        }
        if (poison != null && !poison.getText().isEmpty() ) {
            return true;
        }
        if (poisonVolume != null && !poisonVolume.getText().isEmpty() ) {
            return true;
        }
        if (poisonDescription != null && !poisonDescription.getText().isEmpty() ) {
            return true;
        }
        return false;
    }
    public void reset() {
        super.reset();
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
