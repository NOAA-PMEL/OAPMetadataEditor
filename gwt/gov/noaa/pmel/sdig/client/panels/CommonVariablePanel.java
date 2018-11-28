package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.oracles.InstrumentSuggestOracle;
import gov.noaa.pmel.sdig.client.oracles.ObservationTypeSuggestOracle;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.SuggestBox;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhs on 3/8/17.
 */
public class CommonVariablePanel extends Composite implements GetsDirty<Variable> {

    @UiField
    Heading heading;

    // 001 Variable abbreviation in data files
    @UiField
    TextBox abbreviation;

    // 002 Observation type
    @UiField (provided = true)
    SuggestBox observationType;

    // 003 Manipulation method
    @UiField
    TextBox manipulationMethod;

    // 004 In-situ observation / manipulation condition / response variable
    @UiField
    ButtonDropDown observationDetail;

    // 005 Variable unit
    @UiField
    TextBox units;

    // 006 Measured or calculated
    @UiField
    ButtonDropDown measured;

    // 008 Sampling instrument
    @UiField (provided = true)
    SuggestBox samplingInstrument;

    // 009 Analyzing instrument
    @UiField (provided = true)
    SuggestBox analyzingInstrument;

    // 010 Detailed sampling and analyzing information
    @UiField
    TextArea detailedInformation;

    // 011 Field replicate information
    @UiField
    TextBox fieldReplicate;

    // 020 Uncertainty
    @UiField
    TextBox uncertainty;

    // 021 Data quality flag description
    @UiField
    TextBox qualityFlag;

    // 022 Researcher Name
    @UiField
    TextBox researcherName;

    // 023 Researcher Institution
    @UiField
    TextBox researcherInstitution;

    // 035 Full variable name
    @UiField
    TextBox fullVariableName;

    // 045 Method reference (citation)
    @UiField
    TextBox referenceMethod;


    // The form groups that hold the labels and form entry widgets (textbox, textarea and dropdowns).

    // 001 Variable abbreviation in data files
    @UiField
    FormGroup abbreviationForm;

    // 002 Observation type
    @UiField
    FormGroup observationTypeForm;

    // 003 Manipulation method
    @UiField
    FormGroup manipulationMethodForm;

    // 004 In-situ observation / manipulation condition / response variable
    @UiField
    FormGroup observationDetailForm;

    // 005 Variable unit
    @UiField
    FormGroup unitsForm;

    // 006 Measured or calculated
    @UiField
    FormGroup measuredForm;

    // 007 Calculation method and parameters
    @UiField
    FormGroup calculationMethodForm;

    // 008 Sampling instrument
    @UiField
    FormGroup samplingInstrumentForm;

    // 009 Analyzing instrument
    @UiField
    FormGroup analyzingInstrumentForm;

    // 010 Detailed sampling and analyzing information
    @UiField
    FormGroup detailedInformationForm;

    // 011 Field replicate information
    @UiField
    FormGroup fieldReplicateForm;

    // 020 Uncertainty
    @UiField
    FormGroup uncertaintyForm;

    // 021 Data quality flag description
    @UiField
    FormGroup qualityFlagForm;

    // 022 Researcher Name
    @UiField
    FormGroup researcherNameForm;

    // 023 Researcher Institution
    @UiField
    FormGroup researcherInstitutionForm;

    // 035 Full variable name
    @UiField
    FormGroup fullVariableNameForm;

    // 045 Method reference (citation)
    @UiField
    FormGroup referenceMethodForm;

    // 001 Variable abbreviation in data files
    @UiField
    Modal abbreviationModal;

    // 002 Observation type
    @UiField
    Modal observationTypeModal;

    // 003 Manipulation method
    @UiField
    Modal manipulationMethodModal;

    // 004 In-situ observation / manipulation condition / response variable
    @UiField
    Modal observationDetailModal;

    // 005 Variable unit
    @UiField
    Modal unitsModal;

    // 006 Measured or calculated
    @UiField
    Modal measuredModal;

    // 007 Calculation method and parameters
    @UiField
    Modal calculationMethodModal;

    // 008 Sampling instrument
    @UiField
    Modal samplingInstrumentModal;

    // 009 Analyzing instrument
    @UiField
    Modal analyzingInstrumentModal;

    // 010 Detailed sampling and analyzing information
    @UiField
    Modal detailedInformationModal;

    // 011 Field replicate information
    @UiField
    Modal fieldReplicateModal;

    // 020 Uncertainty
    @UiField
    Modal uncertaintyModal;

    // 021 Data quality flag description
    @UiField
    Modal qualityFlagModal;

    // 022 Researcher Name
    @UiField
    Modal researcherNameModal;

    // 023 Researcher Institution
    @UiField
    Modal researcherInstitutionModal;

    // 035 Full variable name
    @UiField
    Modal fullVariableNameModal;

    // 045 Method reference (citation)
    @UiField
    Modal referenceMethodModal;



    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    InstrumentSuggestOracle instrumentSuggestOracle = new InstrumentSuggestOracle();
    ObservationTypeSuggestOracle observationTypeOracle = new ObservationTypeSuggestOracle();

    interface VariablePanelUiBinder extends UiBinder<HTMLPanel, CommonVariablePanel> {
    }

    private static VariablePanelUiBinder ourUiBinder = GWT.create(VariablePanelUiBinder.class);

    public CommonVariablePanel() {

        samplingInstrument = new SuggestBox(instrumentSuggestOracle);
        analyzingInstrument = new SuggestBox(instrumentSuggestOracle);
        observationType = new SuggestBox(observationTypeOracle);

        initWidget(ourUiBinder.createAndBindUi(this));

        List<String> detailNames = new ArrayList<String>();
        List<String> detailValues = new ArrayList<String>();
        detailNames.add("in-situ observation");
        detailValues.add("in-situ observation");
        detailNames.add("manipulation condition");
        detailValues.add("manipulation condition");
        detailNames.add("response variable");
        detailValues.add("response variable");
        observationDetail.init("Pick One ", detailNames, detailValues);

        List<String> measuredNames = new ArrayList<String>();
        List<String> measuredValues = new ArrayList<String>();
        measuredNames.add("Measured");
        measuredValues.add("measured");
        measuredNames.add("Calculated");
        measuredValues.add("calculated");
        measured.init("Measured or Calculated ", measuredNames, measuredValues);

        abbreviationModal.setTitle("");
        observationTypeModal.setTitle("");
        manipulationMethodModal.setTitle("");
        observationDetailModal.setTitle("");
        measuredModal.setTitle("");
        calculationMethodModal.setTitle("");
        samplingInstrumentModal.setTitle("");
        analyzingInstrumentModal.setTitle("");
        detailedInformationModal.setTitle("");
        fieldReplicateModal.setTitle("");
        uncertaintyModal.setTitle("");
        qualityFlagModal.setTitle("");
        researcherNameModal.setTitle("");
        researcherInstitutionModal.setTitle("");
        fullVariableNameModal.setTitle("");
        referenceMethodModal.setTitle("");

    }
    public void show(Variable variable) {
        if ( variable.getAbbreviation() != null ) {
            abbreviation.setText(variable.getAbbreviation());
        }
        if ( variable.getObservationDetail() != null ) {
            observationDetail.setSelected(variable.getObservationDetail());
        }
        if ( variable.getManipulationMethod() != null ) {
            manipulationMethod.setText(variable.getManipulationMethod());
        }
        if ( variable.getObservationType() != null ) {
            observationType.setText(variable.getObservationType());
        }
        if ( variable.getUnits() != null ) {
            units.setText(variable.getUnits());
        }
        if ( variable.getMeasured() != null ) {
            measured.setSelected(variable.getMeasured());
        }
        if ( variable.getSamplingInstrument() != null ) {
            samplingInstrument.setText(variable.getSamplingInstrument());
        }
        if ( variable.getAnalyzingInstrument() != null ) {
            analyzingInstrument.setText(variable.getAnalyzingInstrument());
        }
        if ( variable.getDetailedInformation() != null ) {
            detailedInformation.setText(variable.getDetailedInformation());
        }
        if ( variable.getFieldReplicate() != null ) {
            fieldReplicate.setText(variable.getFieldReplicate());
        }
        if ( variable.getUncertainty() != null ) {
            uncertainty.setText(variable.getUncertainty());
        }
        if ( variable.getQualityFlag() != null ) {
            qualityFlag.setText(variable.getQualityFlag());
        }
        if ( variable.getResearcherName() != null ) {
            researcherName.setText(variable.getResearcherName());
        }
        if ( variable.getResearcherInstitution() != null ) {
            researcherInstitution.setText(variable.getResearcherInstitution());
        }
        if ( variable.getFullVariableName() != null ) {
            fullVariableName.setText(variable.getFullVariableName());
        }
        if ( variable.getReferenceMethod() != null ) {
            referenceMethod.setText(variable.getReferenceMethod());
        }
    }

    public Variable getCommonVariable() {

        Variable commonVariable = new Variable();

        fillCommonVariable(commonVariable);

        return commonVariable;

    }
    public Variable fillCommonVariable(Variable commonVariable) {
        commonVariable.setAbbreviation(abbreviation.getText());
        commonVariable.setObservationType(observationType.getText());
        commonVariable.setManipulationMethod(manipulationMethod.getText());
        commonVariable.setObservationDetail(observationDetail.getValue());
        commonVariable.setUnits(units.getText());
        commonVariable.setMeasured(measured.getValue());
        commonVariable.setSamplingInstrument(samplingInstrument.getText());
        commonVariable.setAnalyzingInstrument(analyzingInstrument.getText());
        commonVariable.setDetailedInformation(detailedInformation.getText());
        commonVariable.setFieldReplicate(fieldReplicate.getText());
        commonVariable.setUncertainty(uncertainty.getText());
        commonVariable.setQualityFlag(qualityFlag.getText());
        commonVariable.setResearcherName(researcherName.getText());
        commonVariable.setResearcherInstitution(researcherInstitution.getText());
        commonVariable.setFullVariableName(fullVariableName.getText());
        commonVariable.setReferenceMethod(referenceMethod.getText());
        return commonVariable;
    }
    public boolean isDirty(Variable original) {
        boolean isDirty;
        isDirty = original == null ?
                  isDirty() :
                   // Don't check abbreviation and full name since they are filled automatically.
                   isDirty(observationType, original.getObservationType() ) ||
                   isDirty(manipulationMethod, original.getManipulationMethod() ) ||
                   isDirty(observationDetail.getValue(), original.getObservationDetail() ) ||
                   isDirty(units, original.getUnits() ) ||
                   isDirty(measured.getValue(), original.getMeasured() ) ||
                   isDirty(samplingInstrument, original.getSamplingInstrument() ) ||
                   isDirty(analyzingInstrument, original.getAnalyzingInstrument() ) ||
                   isDirty(detailedInformation, original.getDetailedInformation() ) ||
                   isDirty(fieldReplicate, original.getFieldReplicate() ) ||
                   isDirty(uncertainty, original.getUncertainty() ) ||
                   isDirty(qualityFlag, original.getQualityFlag() ) ||
                   isDirty(researcherName, original.getResearcherName() ) ||
                   isDirty(researcherInstitution, original.getResearcherInstitution() ) ||
                   isDirty(referenceMethod, original.getReferenceMethod() );
        return isDirty;
    }
    public boolean isDirty() {
        // Don't check abbreviation and full name since they are filled automatically.
        if (observationType.getText() != null && !observationType.getText().isEmpty() ) {
            return true;
        }
        if (manipulationMethod.getText() != null && !manipulationMethod.getValue().isEmpty() )
            return true;
        if (observationDetail.getValue() != null && !observationDetail.getValue().isEmpty())
            return true;
        if (units.getText() != null && !units.getValue().isEmpty() )
            return true;
        if (measured.getValue() != null && !measured.getValue().isEmpty() )
            return true;
        if (samplingInstrument.getText() != null && !samplingInstrument.getValue().isEmpty() )
            return true;
        if (analyzingInstrument.getText() != null && !analyzingInstrument.getValue().isEmpty() )
            return true;
        if (detailedInformation.getText() != null & !detailedInformation.getValue().isEmpty() )
            return true;
        if (fieldReplicate.getText() != null && !fieldReplicate.getValue().isEmpty() )
            return true;
        if (uncertainty.getText() != null && !uncertainty.getValue().isEmpty())
            return true;
        if (qualityFlag.getText() != null && !qualityFlag.getValue().isEmpty() )
            return true;
        if (researcherName.getText() != null && !researcherName.getValue().isEmpty())
            return true;
        if (researcherInstitution.getText() != null && !researcherInstitution.getValue().isEmpty() )
            return true;
        if (referenceMethod.getText() != null && !referenceMethod.getValue().isEmpty() )
            return true;
        return false;
    }

}