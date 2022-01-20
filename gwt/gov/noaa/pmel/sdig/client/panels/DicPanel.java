package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

/**
 * Created by rhs on 3/8/17.
 */
public class DicPanel extends FormPanel<Variable> implements GetsDirty<Variable> {

    @UiField
    Button save;

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
        common.intakeLocationForm.setVisible(false);
        common.intakeDepthForm.setVisible(false);
//        common.abbreviation.setEnabled(false);
        common.fullVariableName.setEnabled(false);
        common.heading.setText("Enter the Information for Dissolved Inorganic Carbon (DIC).");
        save.addClickHandler(saveIt);

        common.abbreviationModal.setTitle("20.1 Column header name of the variable in the data files, e.g., DIC, TCO2, CT.");
        common.observationTypeModal.setTitle("20.2 How the variable is observed, e.g., surface underway, profile, time series, etc.");

        // #OADSHELP
        common.manipulationMethodModal.setTitle("22.4 In perturbation experiments, seawater carbonate chemistry can be manipulated by different techniques, such as bubbling CO2, adding acids or bases, etc.");
        common.observationDetailModal.setTitle("22.3 Whether the variable belong to an in-situ observed variable, or a manipulation condition variable, or a response variable in a biological experimental study.");
        common.measuredModal.setTitle("22.6 Whether the variable is measured in-situ, or calculated from other variables.");
        common.calculationMethodModal.setTitle("22.7 Variables can be calculated using different sets of constants or different software.");
        common.samplingInstrumentModal.setTitle("22.8 Instrument that is used to collect water samples, or deploy sensors, etc. For example, a Niskin bottle, pump, CTD, etc is a sampling instrument.");

        common.analyzingInstrumentModal.setTitle("20.5 Instrument that was used to analyze the water samples collected with the 'collection method' above, or the sensors that are mounted on the 'platform' to measure the water body continuously. We encourage you to document as many details (such as the make, model, resolution, precisions, etc) of the instrument as you can here.");

        // Analyzing information with citation
        // common.detailedInformationModal.setTitle("22.10 Detailed description of the sampling and analyzing procedures, including calibration procedures, model number of the instrument, etc.");
        common.detailedInformationModal.setTitle("20.6 Detailed description of the analyzing procedures, including the citation of the SOP used for the analysis (e.g. SOP 7;  Dickson, A.G., Sabine, C.L. and Christian, J.R.  2007.  Guide to Best Practices for Ocean CO2  Measurements).");
        common.fieldReplicateModal.setTitle("20.11 Repetition of sample collection and measurement, e.g., triplicate samples.");
        common.uncertaintyModal.setTitle("20.10 Uncertainty of the results (e.g., 1%, 2 μmol kg-1), or any pieces of information that are related to the quality control of the variable.");

        // Quality control/Data quality scheme (name of scheme)
        common.qcAppliedModal.setTitle("20.7 Indicate if quality control procedures were applied.");

        // #OADSHELP
        common.researcherNameModal.setTitle("22.17.1 The name of the PI, whose research team measured or derived this parameter.");
        common.researcherInstitutionModal.setTitle("22.17.2 The institution of the PI, whose research team measured or derived this parameter.");

        common.fullVariableNameModal.setTitle("The full variable name.");
        common.referenceMethodModal.setTitle("20.19 Citation for the dissolved inorganic carbon method.");
        common.unitsModal.setTitle("20.3 Units of the variable (e.g., μmol/kg).");

        // Data quality flag scheme
        common.qcSchemeNameModal.setTitle("20.9  Data quality flag scheme");
        common.modalContentQcSchemeName.setHTML(
                "<p>Indicate which of the following data quality schemes was used. For "
                        + "more details: <br /><a href='https://odv.awi.de/fileadmin/user_upload/odv/misc/ODV4_QualityFlagSets.pdf' "
                        + "target='_blank'>https://odv.awi.de/fileadmin/user_upload/odv/misc/ODV4_QualityFlagSets.pdf</a>"
                        + "<p>If no data quality scheme was used, please leave blank.</p>");

        // Abbreviation of data quality flag scheme
        common.qcVariableNameModal.setTitle("20.8 Column header name of the data quality flag scheme applied in the data files, e.g. QC, Quality, etc.");
        common.sopChangesModal.setTitle("20.20 Indicate if any changes were made to the method as described in the SOP, such as changes in the sample collection method, changes in storage of the sample, different volume, changes to the CRM used, etc. Please provide a detailed list of  all of the changes made.");
        common.collectionMethodModal.setTitle("20.4 Method that is used to collect water samples, or deploy sensors, etc. For example, bottle collection with a Niskin bottle, pump, CTD, is a collection method.");

        // ??? see detailedInformationModal
        common.analyzingInformationModal.setTitle("20.6 Detailed description of the analyzing procedures, including the citation of the SOP used for the analysis (e.g. SOP 7;  Dickson, A.G., Sabine, C.L. and Christian, J.R.  2007.  Guide to Best Practices for Ocean CO2  Measurements).");

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
