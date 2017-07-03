package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.oracles.InstrumentSuggestOracle;
import gov.noaa.pmel.sdig.client.oracles.ObservationTypeSuggestOracle;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Popover;
import org.gwtbootstrap3.client.ui.SuggestBox;
import org.gwtbootstrap3.client.ui.TextArea;
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
public class CommonVariablePanel extends Composite {

    @UiField
    Heading heading;

    @UiField
    Form form;

    @UiField
    Button save;

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
    Popover abbreviationPopover;

    // 002 Observation type
    @UiField
    Popover observationTypePopover;

    // 003 Manipulation method
    @UiField
    Popover manipulationMethodPopover;

    // 004 In-situ observation / manipulation condition / response variable
    @UiField
    Popover observationDetailPopover;

    // 005 Variable unit
    @UiField
    Popover unitsPopover;

    // 006 Measured or calculated
    @UiField
    Popover measuredPopover;

    // 007 Calculation method and parameters
    @UiField
    Popover calculationMethodPopover;

    // 008 Sampling instrument
    @UiField
    Popover samplingInstrumentPopover;

    // 009 Analyzing instrument
    @UiField
    Popover analyzingInstrumentPopover;

    // 010 Detailed sampling and analyzing inPopoveration
    @UiField
    Popover detailedInformationPopover;

    // 011 Field replicate inPopoveration
    @UiField
    Popover fieldReplicatePopover;

    // 020 Uncertainty
    @UiField
    Popover uncertaintyPopover;

    // 021 Data quality flag description
    @UiField
    Popover qualityFlagPopover;

    // 022 Researcher Name
    @UiField
    Popover researcherNamePopover;

    // 023 Researcher Institution
    @UiField
    Popover researcherInstitutionPopover;

    // 035 Full variable name
    @UiField
    Popover fullVariableNamePopover;

    // 045 Method reference (citation)
    @UiField
    Popover referenceMethodPopover;



    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    InstrumentSuggestOracle instrumentSuggestOracle = new InstrumentSuggestOracle();
    ObservationTypeSuggestOracle observationTypeOracle = new ObservationTypeSuggestOracle();

//TODO initialize the cell type dropdown.

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

        abbreviationPopover.setContent("");
        observationTypePopover.setContent("");
        manipulationMethodPopover.setContent("");
        observationDetailPopover.setContent("");
        measuredPopover.setContent("");
        calculationMethodPopover.setContent("");
        samplingInstrumentPopover.setContent("");
        analyzingInstrumentPopover.setContent("");
        detailedInformationPopover.setContent("");
        fieldReplicatePopover.setContent("");
        uncertaintyPopover.setContent("");
        qualityFlagPopover.setContent("");
        researcherNamePopover.setContent("");
        researcherInstitutionPopover.setContent("");
        fullVariableNamePopover.setContent("");
        referenceMethodPopover.setContent("");

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
            measured.setTitle(variable.getMeasured());
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