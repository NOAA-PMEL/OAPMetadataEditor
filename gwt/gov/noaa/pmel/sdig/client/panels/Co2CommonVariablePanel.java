package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.UIObject;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.oracles.InstrumentSuggestOracle;
import gov.noaa.pmel.sdig.client.oracles.ObservationTypeSuggestOracle;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.FormLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhs on 3/8/17.
 */
public class Co2CommonVariablePanel extends Composite implements GetsDirty<Variable> {

    Variable _displayedVariable = null;
    boolean isBig5 = false;

    // 001 Variable abbreviation in data files
//    @UiField
//    TextBox abbreviation;
    final String abbreviation = Constants.SECTION_CO2;

    // 002 Observation type
//    @UiField (provided = true) SuggestBox
    String observationType = "Surface Underway";

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

    @UiField
    TextBox calculationMethod;

    // 008 Sampling instrument
    @UiField (provided = true)
    SuggestBox samplingInstrument;
    @UiField
    Button showSamplingInstrumentListButton;

    // 009 Analyzing instrument
    @UiField (provided = true)
    SuggestBox analyzingInstrument;
    @UiField
    Button showAnalyzingInstrumentListButton;

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
//    @UiField
//    TextBox fullVariableName;

    // 045 Method reference (citation)
    @UiField
    TextBox referenceMethod;


    // The form groups that hold the labels and form entry widgets (textbox, textarea and dropdowns).

    // 001 Variable abbreviation in data files
//    @UiField
//    FormGroup abbreviationForm;

    // 002 Observation type
//    @UiField
//    FormGroup observationTypeForm;

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
//    @UiField
//    FormGroup fullVariableNameForm;

    // 045 Method reference (citation)
    @UiField
    FormGroup referenceMethodForm;

    // 001 Variable abbreviation in data files
//    @UiField
//    Modal abbreviationModal;

    // 002 Observation type
//    @UiField
//    Modal observationTypeModal;

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
//    @UiField
//    Modal fullVariableNameModal;

    // 045 Method reference (citation)
    @UiField
    Modal referenceMethodModal;


    @UiField
    FormLabel qualityFlagLabel;

    // new var for 14.3.1
    // Quality control
    @UiField
    TextBox qualityControl;
    @UiField
    Modal qualityControlModal;
    @UiField
    FormGroup qualityControlForm;

    // Abbreviation of data quality flag scheme
    @UiField
    TextBox abbreviationQualityFlag;
    @UiField
    Modal abbreviationQualityFlagModal;
    @UiField
    FormGroup abbreviationQualityFlagForm;

    // Changes to Method or SOP
    @UiField
    TextArea sopChanges;
    @UiField
    Modal sopChangesModal;
    @UiField
    FormGroup sopChangesForm;

    // Collection Method(e.g. bottle sampling)
    @UiField
    TextBox collectionMethod;
    @UiField
    Modal collectionMethodModal;
    @UiField
    FormGroup collectionMethodForm;

    // Analyzing information with citation
    @UiField
    TextArea analyzingInformation;
    @UiField
    Modal analyzingInformationModal;
    @UiField
    FormGroup analyzingInformationForm;

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    InstrumentSuggestOracle instrumentSuggestOracle = new InstrumentSuggestOracle();
    ObservationTypeSuggestOracle observationTypeOracle = new ObservationTypeSuggestOracle();

    interface VariablePanelUiBinder extends UiBinder<HTMLPanel, Co2CommonVariablePanel> {
    }

    private static VariablePanelUiBinder ourUiBinder = GWT.create(VariablePanelUiBinder.class);

    public Co2CommonVariablePanel() {

        samplingInstrument = new SuggestBox(instrumentSuggestOracle);
        analyzingInstrument = new SuggestBox(instrumentSuggestOracle);
        samplingInstrument.getValueBox().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("click samplebox:"+event.getNativeEvent().getType());
                if ( samplingInstrument.isSuggestionListShowing()) {
                    ((SuggestBox.DefaultSuggestionDisplay)samplingInstrument.getSuggestionDisplay()).hideSuggestions();
                }
            }
        });
        analyzingInstrument.getValueBox().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if ( analyzingInstrument.isSuggestionListShowing()) {
                    ((SuggestBox.DefaultSuggestionDisplay)analyzingInstrument.getSuggestionDisplay()).hideSuggestions();
                }
            }
        });
//        observationType = new SuggestBox(observationTypeOracle);

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
        measuredValues.add("Measured");
        measuredNames.add("Calculated");
        measuredValues.add("Calculated");
        measured.init("Measured or Calculated ", measuredNames, measuredValues);

//        abbreviationModal.setTitle("");
//        observationTypeModal.setTitle("");
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
//        fullVariableNameModal.setTitle("");
        referenceMethodModal.setTitle("");

        qualityControl.setTitle("");
        abbreviationQualityFlag.setTitle("");
        sopChanges.setTitle("");
        collectionMethod.setTitle("");
        analyzingInformation.setTitle("");

//        showSamplingInstrumentListButton.add
    }

    @UiHandler("showSamplingInstrumentListButton")
    public void onSamplingIntrumentListClick(ClickEvent clickEvent) {
        UIObject button = (UIObject)clickEvent.getSource();
        samplingInstrument.showSuggestionList();
    }

    @UiHandler("showAnalyzingInstrumentListButton")
    public void onAnalyzingIntrumentListClick(ClickEvent clickEvent) {
        analyzingInstrument.showSuggestionList();
    }

    void reset() {
        observationDetail.reset();
        measured.reset();
    }
    public void show(Variable variable) {
        _displayedVariable = variable;
//        if ( variable.getAbbreviation() != null ) {
//            abbreviation.setText(variable.getAbbreviation());
//        }
        if ( variable.getObservationDetail() != null ) {
            observationDetail.setSelected(variable.getObservationDetail());
        } else {
            observationDetail.reset();
        }
        if ( variable.getManipulationMethod() != null ) {
            manipulationMethod.setText(variable.getManipulationMethod());
        }
//        if ( variable.getObservationType() != null ) {
//            observationType.setText(variable.getObservationType());
//        }
        if ( variable.getUnits() != null ) {
            units.setText(variable.getUnits());
        }
        if ( variable.getMeasured() != null ) {
            measured.setSelected(variable.getMeasured());
        } else {
            measured.reset();
        }
        if ( variable.getSamplingInstrument() != null ) {
            samplingInstrument.setText(variable.getSamplingInstrument());
        }
        if ( variable.getAnalyzingInstrument() != null ) {
            analyzingInstrument.setText(variable.getAnalyzingInstrument());
        }
        if ( variable.getCalculationMethod() != null ) {
            calculationMethod.setText(variable.getCalculationMethod());
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
//        if ( variable.getFullVariableName() != null ) {
//            fullVariableName.setText(variable.getFullVariableName());
//        }
        if ( variable.getReferenceMethod() != null ) {
            referenceMethod.setText(variable.getReferenceMethod());
        }

        if ( variable.getQualityControl() != null ) {
            qualityControl.setText(variable.getQualityControl());
        }
        if ( variable.getAbbreviationQualityFlag() != null ) {
            abbreviationQualityFlag.setText(variable.getAbbreviationQualityFlag());
        }
        if ( variable.getSopChanges() != null ) {
            sopChanges.setText(variable.getSopChanges());
        }
        if ( variable.getCollectionMethod() != null ) {
            collectionMethod.setText(variable.getCollectionMethod());
        }
        if ( variable.getAnalyzingInformation() != null ) {
            analyzingInformation.setText(variable.getAnalyzingInformation());
        }
    }

    public Variable getCommonVariable() {

        Variable commonVariable = _displayedVariable != null ? _displayedVariable : new Variable();

        fillCommonVariable(commonVariable);

        return commonVariable;

    }
    public Variable fillCommonVariable(Variable commonVariable) {
        commonVariable.setAbbreviation(abbreviation); // .getText());
        commonVariable.setObservationType(observationType); //.getText());
        commonVariable.setManipulationMethod(manipulationMethod.getText());
        commonVariable.setObservationDetail(observationDetail.getValue());
        commonVariable.setUnits(units.getText());
        commonVariable.setMeasured(measured.getValue());
        commonVariable.setCalculationMethod(calculationMethod.getValue());
        commonVariable.setSamplingInstrument(samplingInstrument.getText());
        commonVariable.setAnalyzingInstrument(analyzingInstrument.getText());
        commonVariable.setDetailedInformation(detailedInformation.getText());
        commonVariable.setFieldReplicate(fieldReplicate.getText());
        commonVariable.setUncertainty(uncertainty.getText());
        commonVariable.setQualityFlag(qualityFlag.getText());
        commonVariable.setResearcherName(researcherName.getText());
        commonVariable.setResearcherInstitution(researcherInstitution.getText());
//        commonVariable.setFullVariableName(fullVariableName.getText());
        commonVariable.setReferenceMethod(referenceMethod.getText());

        commonVariable.setQualityControl(qualityControl.getText());
        commonVariable.setAbbreviationQualityFlag(abbreviationQualityFlag.getText());
        commonVariable.setSopChanges(sopChanges.getText());
        commonVariable.setCollectionMethod(collectionMethod.getText());
        commonVariable.setAnalyzingInformation(analyzingInformation.getText());
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
                   isDirty(calculationMethod, original.getCalculationMethod() ) ||
                   isDirty(detailedInformation, original.getDetailedInformation() ) ||
                   isDirty(fieldReplicate, original.getFieldReplicate() ) ||
                   isDirty(uncertainty, original.getUncertainty() ) ||
                   isDirty(qualityFlag, original.getQualityFlag() ) ||
                   isDirty(researcherName, original.getResearcherName() ) ||
                   isDirty(researcherInstitution, original.getResearcherInstitution() ) ||
                   isDirty(referenceMethod, original.getReferenceMethod() ) ||
                   isDirty(qualityControl, original.getQualityControl() ) ||
                   isDirty(abbreviationQualityFlag, original.getAbbreviationQualityFlag() ) ||
                   isDirty(sopChanges, original.getSopChanges() ) ||
                   isDirty(collectionMethod, original.getCollectionMethod() ) ||
                   isDirty(analyzingInformation, original.getAnalyzingInformation() );

        return isDirty;
    }
    public boolean isDirty() {
        // Don't check abbreviation and full name for Big5 since they are filled automatically.
//        if ( !isBig5 ) {
////            if (abbreviation.getText() != null && !abbreviation.getText().isEmpty() ) {
////                return true;
////            }
//            if (fullVariableName.getText() != null && !fullVariableName.getText().isEmpty() ) {
//                return true;
//            }
//        }
//        if (observationType.getText() != null && !observationType.getText().isEmpty() ) {
//            return true;
//        }
        if (manipulationMethod.getText().trim() != null && !manipulationMethod.getValue().isEmpty() )
            return true;
        if (observationDetail.getValue() != null && !observationDetail.getValue().isEmpty())
            return true;
        if (units.getText().trim() != null && !units.getValue().isEmpty() )
            return true;
        if (measured.getValue() != null && !measured.getValue().isEmpty() )
            return true;
        if (samplingInstrument.getText().trim() != null && !samplingInstrument.getValue().isEmpty() )
            return true;
        if (analyzingInstrument.getText().trim() != null && !analyzingInstrument.getValue().isEmpty() )
            return true;
        if (detailedInformation.getText().trim() != null & !detailedInformation.getValue().isEmpty() )
            return true;
        if (fieldReplicate.getText().trim() != null && !fieldReplicate.getValue().isEmpty() )
            return true;
        if (uncertainty.getText().trim() != null && !uncertainty.getValue().isEmpty())
            return true;
        if (qualityFlag.getText().trim() != null && !qualityFlag.getValue().isEmpty() )
            return true;
        if (researcherName.getText().trim() != null && !researcherName.getValue().isEmpty())
            return true;
        if (researcherInstitution.getText().trim() != null && !researcherInstitution.getValue().isEmpty() )
            return true;
        if (referenceMethod.getText().trim() != null && !referenceMethod.getValue().isEmpty() )
            return true;

        if (qualityControl.getText().trim() != null && !qualityControl.getValue().isEmpty() )
            return true;
        if (abbreviationQualityFlag.getText().trim() != null && !abbreviationQualityFlag.getValue().isEmpty() )
            return true;
        if (sopChanges.getText().trim() != null && !sopChanges.getValue().isEmpty() )
            return true;
        if (collectionMethod.getText().trim() != null && !collectionMethod.getValue().isEmpty() )
            return true;
        if (analyzingInformation.getText().trim() != null && !analyzingInformation.getValue().isEmpty() )
            return true;
        return false;
    }

}