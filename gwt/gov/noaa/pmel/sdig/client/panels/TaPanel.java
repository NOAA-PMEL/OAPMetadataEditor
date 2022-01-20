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
import org.gwtbootstrap3.client.ui.FormLabel;
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

    // 017 How were the samples preserved (HgCl2, or others)
    @UiField
    TextBox poison;

    // 018 Concentration and amount of the preservative
    @UiField
    TextBox poisonVolume;

    // 019 Preservative correction description
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

    @UiField
    FormLabel standardizationTechniqueLabel;
    @UiField
    FormLabel freqencyOfStandardizationLabel;
    @UiField
    FormLabel poisonLabel;
    @UiField
    FormLabel poisonVolumeLabel;
    @UiField
    FormLabel poisonDescriptionLabel;

    interface VariablePanelUiBinder extends UiBinder<HTMLPanel, TaPanel> {
    }

    private static TaPanel.VariablePanelUiBinder ourUiBinder = GWT.create(TaPanel.VariablePanelUiBinder.class);

    public TaPanel() {

        initWidget(ourUiBinder.createAndBindUi(this));

        setDefaults();
        common.intakeLocationForm.setVisible(false);
        common.intakeDepthForm.setVisible(false);

        // common.abbreviation.setEnabled(false);
        common.fullVariableName.setEnabled(false);
        common.heading.setText("Enter the Information for Total Alkalinity (TA).");
        save.addClickHandler(saveIt);
        common.abbreviationModal.setTitle("21.1 Column header name of the variable in the data files, e.g., TA, Alk, etc.");
        common.observationTypeModal.setTitle("21.2 How the variable is observed, e.g., surface underway, profile, time series, model output.");

        // #OADSHELP
        common.manipulationMethodModal.setTitle("23.4 In perturbation experiments, seawater carbonate chemistry can be manipulated by different techniques, such as bubbling CO2, adding acids or bases, etc.");
        common.observationDetailModal.setTitle("23.3 Whether the variable belong to an in-situ observed variable, or a manipulation condition variable, or a response variable in a biological experimental study.");
        common.measuredModal.setTitle("23.6 Variable is measured in-situ, or calculated from other variables.");
        common.calculationMethodModal.setTitle("23.7 Variables can be calculated using different sets of constants or different software.");
        common.samplingInstrumentModal.setTitle("23.8 Instrument that is used to collect water samples, or deploy sensors, etc. For example, a Niskin bottle, pump, CTD, etc is a sampling instrument.");

        common.analyzingInstrumentModal.setTitle("21.7 Instrument that was used to analyze the water samples collected with the 'collection method' above, or the sensors that are mounted on the 'platform' to measure the water body continuously. We encourage you to document as many details (such as the make, model, resolution, precisions, etc) of the instrument as you can here.");

        // Analyzing information with citation
        // common.detailedInformationModal.setTitle("23.13 Detailed description of the sampling and analyzing procedures, including calibration procedures, model number of the instrument, etc.");
        common.detailedInformationModal.setTitle("21.8 Detailed description of the analyzing procedures, including the citation of the SOP used for the analysis (e.g. SOP 3a;  Dickson, A.G., Sabine, C.L. and Christian, J.R.  2007.  Guide to Best Practices for Ocean CO2  Measurements).");
        common.fieldReplicateModal.setTitle("21.16 Repetition of sample collection and measurement, e.g., triplicate samples.");
        common.uncertaintyModal.setTitle("21.12 Uncertainty of the results (e.g., 1%, 2 μmol/kg), or any pieces of information that are related to the quality control of the variable.");

        // Quality control/Data quality scheme (name of scheme)
        common.qcAppliedModal.setTitle("21.9 Indicate if quality control procedures were applied.");

        // #OADSHELP
        common.researcherNameModal.setTitle("23.21.1 The name of the PI, whose research team measured or derived this parameter.");
        common.researcherInstitutionModal.setTitle("23.21.2 The institution of the PI, whose research team measured or derived this parameter.");

        common.fullVariableNameModal.setTitle("The full name of the variable.");
        common.referenceMethodModal.setTitle("21.25 Citation for the alkalinity method.");
        common.unitsModal.setTitle("21.3 Units of the variable (e.g., μmol/kg).");


        // Data quality flag scheme
        common.qcSchemeNameModal.setTitle("21.11  Data quality flag scheme");
        common.modalContentQcSchemeName.setHTML(
                "<p>Indicate which of the following data quality schemes was used. For "
                        + "more details: <br /><a href='https://odv.awi.de/fileadmin/user_upload/odv/misc/ODV4_QualityFlagSets.pdf' "
                        + "target='_blank'>https://odv.awi.de/fileadmin/user_upload/odv/misc/ODV4_QualityFlagSets.pdf</a>"
                        + "<p>If no data quality scheme was used, please leave blank.</p>");

        // Abbreviation of data quality flag scheme
        common.qcVariableNameModal.setTitle("21.10 Column header name of the data quality flag scheme applied in the data files, e.g. QC, Quality, etc.");
        common.sopChangesModal.setTitle("21.26 Indicate if any changes were made to the method as described in the SOP, such as changes in the sample collection method, changes in storage of the sample, different volume, changes to the CRM used, etc. Please provide a detailed list of  all of the changes made.");
        common.collectionMethodModal.setTitle("21.4 Method that is used to collect water samples, or deploy sensors, etc. For example, bottle collection with a Niskin bottle, pump, CTD, etc is a collection method.");

        // ??? see detailedInformationModal
        common.analyzingInformationModal.setTitle("20.6 Detailed description of the analyzing procedures, including the citation of the SOP used for the analysis (e.g. SOP 7;  Dickson, A.G., Sabine, C.L. and Christian, J.R.  2007.  Guide to Best Practices for Ocean CO2  Measurements).");

        List<String> name = new ArrayList<String>();
        List<String> value = new ArrayList<String>();
        name.add("Open");
        value.add("open");
        name.add("Closed");
        value.add("closed");
        cellType.init("Cell Type: Open or Closed", name, value);

//        if (OAPMetadataEditor.getIsSocatParam()) {
//            common.qcAppliedLabel.setText("Data quality scheme (name of scheme)");
//            standardizationTechniqueLabel.setText("Calibration method");
//            freqencyOfStandardizationLabel.setText("Frequency of calibration");
//            poisonLabel.setText("Poison used to kill the sample");
//            poisonVolumeLabel.setText("Poison volume");
//            poisonDescriptionLabel.setText("Poisoning correction description");
//        }
    }
    private void setDefaults() {
        common.isBig5 = true;
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
        common.reset();
        setDefaults();
    }
}
