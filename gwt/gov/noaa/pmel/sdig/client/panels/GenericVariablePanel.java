package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.RangeChangeEvent;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.oracles.InstrumentSuggestOracle;
import gov.noaa.pmel.sdig.client.oracles.ObservationTypeSuggestOracle;
import gov.noaa.pmel.sdig.client.oracles.VariableSuggestOracle;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.Person;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import gov.noaa.pmel.sdig.shared.bean.list.SuggestedVariables;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.SuggestBox;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rhs on 3/8/17.
 */
public class GenericVariablePanel extends Composite {

    @UiField
    Heading heading;

    @UiField
    Form form;

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

    // 007 Calculation method and parameters
    @UiField
    TextBox calculationMethod;

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
    @UiField (provided = true)
    SuggestBox fullVariableName;

    // 045 Method reference (citation)
    @UiField
    TextBox referenceMethod;


    // 026 Biological subject
    @UiField
    TextBox biologicalSubject;

    // 032 Duration (for settlement/colonization methods)
    @UiField
    TextBox duration;

    // 040 Life stage of the biological subject
    @UiField
    TextBox lifeStage;

    // 051 Species Identification code
    @UiField
    TextBox speciesIdCode;


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

    // 026 Biological subject
    @UiField
    FormGroup biologicalSubjectForm;

    // 032 Duration (for settlement/colonization methods)
    @UiField
    FormGroup durationForm;

    // 040 Life stage of the biological subject
    @UiField
    FormGroup lifeStageForm;

    // 051 Species Identification code
    @UiField
    FormGroup speciesIdCodeForm;

    @UiField
    CellTable<Variable> variables;

    @UiField
    Pagination variablePagination;

    ListDataProvider<Variable> variableData = new ListDataProvider<Variable>();
    private SimplePager cellTablePager = new SimplePager();

    @UiField
    Button save;

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    boolean showTable = true;

    Variable editVariable;
    int editIndex;

    VariableSuggestOracle variableSuggestOracle = new VariableSuggestOracle();
    InstrumentSuggestOracle instrumentSuggestOracle = new InstrumentSuggestOracle();
    ObservationTypeSuggestOracle observationTypeSuggestOracle = new ObservationTypeSuggestOracle();

    public void reset() {
        form.reset();
    }

    public void clearVariables() {
        variableData.getList().clear();
        variableData.flush();
        variablePagination.rebuild(cellTablePager);
    }

    interface VariablePanelUiBinder extends UiBinder<HTMLPanel, GenericVariablePanel> {
    }

    private static VariablePanelUiBinder ourUiBinder = GWT.create(VariablePanelUiBinder.class);

    public GenericVariablePanel() {


        fullVariableName = new SuggestBox(variableSuggestOracle);
        samplingInstrument = new SuggestBox(instrumentSuggestOracle);
        analyzingInstrument = new SuggestBox(instrumentSuggestOracle);
        observationType = new SuggestBox(observationTypeSuggestOracle);


        initWidget(ourUiBinder.createAndBindUi(this));
        heading.setText("Continue entering information for this variable.");

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

        variables.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);

        Column<Variable, String> edit = new Column<Variable, String>(new ButtonCell(IconType.EDIT, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL)) {
            @Override
            public String getValue(Variable object) {
                return "Edit";
            }
        };
        edit.setFieldUpdater(new FieldUpdater<Variable, String>() {
            @Override
            public void update(int index, Variable variable, String value) {
                editIndex = variableData.getList().indexOf(variable);
                if ( editIndex < 0 ) {
                    Window.alert("Edit failed.");
                } else {
                    show(variable);
                    variableData.getList().remove(variable);
                    variableData.flush();
                    variablePagination.rebuild(cellTablePager);
                }

            }
        });
        variables.addColumn(edit);
        // Add a text column to show the name.
        TextColumn<Variable> nameColumn = new TextColumn<Variable>() {
            @Override
            public String getValue(Variable object) {
                return object.getFullVariableName();
            }
        };
        variables.addColumn(nameColumn, "Variable Name");
        Column<Variable, String> delete = new Column<Variable, String>(new ButtonCell(IconType.TRASH, ButtonType.DANGER, ButtonSize.EXTRA_SMALL)) {
            @Override
            public String getValue(Variable object) {
                return "Delete";
            }
        };
        delete.setFieldUpdater(new FieldUpdater<Variable, String>() {
            @Override
            public void update(int index, Variable variable, String value) {
                variableData.getList().remove(variable);
                variableData.flush();
                variablePagination.rebuild(cellTablePager);
                if ( variableData.getList().size() == 0 ) {
                    setTableVisible(false);
                } else {
                    setTableVisible(true);
                }
            }
        });
        variables.addColumn(delete);

        variables.addRangeChangeHandler(new RangeChangeEvent.Handler() {

            @Override
            public void onRangeChange(final RangeChangeEvent event) {
                variablePagination.rebuild(cellTablePager);
            }
        });

        cellTablePager.setDisplay(variables);

        variableData.addDataDisplay(variables);

        variables.setPageSize(3);

    }
    public void setTableVisible(boolean b) {
        variables.setVisible(b);
        if ( b ) {
            if (cellTablePager.getPageCount() > 1) {
                int page = cellTablePager.getPage();
                variablePagination.setVisible(true);
                cellTablePager.setPage(page);
            } else {
                variablePagination.setVisible(false);
            }
        }
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
        if ( variable.getBatchNumber() != null ) {
            biologicalSubject.setText(variable.getBatchNumber());
        }

        if ( variable.getDuration() != null ) {
            duration.setText(variable.getDuration());
        }


        if ( variable.getLifeStage() != null ) {
            lifeStage.setText(variable.getLifeStage());
        }

        if ( variable.getSpeciesIdCode() != null ) {
            speciesIdCode.setText(variable.getSpeciesIdCode());
        }

    }

    public Variable getGenericVariable() {

        Variable commonVariable = new Variable();

        fillGenericVariable(commonVariable);

        return commonVariable;

    }
    public Variable fillGenericVariable(Variable commonVariable) {
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
        commonVariable.setBatchNumber(biologicalSubject.getText());
        commonVariable.setDuration(duration.getText());
        commonVariable.setLifeStage(lifeStage.getText());
        commonVariable.setSpeciesIdCode(speciesIdCode.getText());
        return commonVariable;
    }

    public boolean isDirty() {
        if (abbreviation.getText() != null && !abbreviation.getText().isEmpty() ) {
            return true;
        }
        if (observationType.getText() != null && !observationType.getText().isEmpty() ) {
            return true;
        }
        if (manipulationMethod.getText() != null && !manipulationMethod.getText().isEmpty() ) {
            return true;
        }
        if (units.getText() != null && !units.getText().isEmpty() ) {
            return true;
        }
        if (samplingInstrument.getText() != null && !samplingInstrument.getText().isEmpty() ) {
            return true;
        }
        if (analyzingInstrument.getText() != null && !analyzingInstrument.getText().isEmpty() ) {
            return true;
        }
        if (detailedInformation.getText() != null && !detailedInformation.getText().isEmpty() ) {
            return true;
        }
        if (fieldReplicate.getText() != null && !fieldReplicate.getText().isEmpty() ) {
            return true;
        }
        if (uncertainty.getText() != null && !uncertainty.getText().isEmpty() ) {
            return true;
        }
        if (qualityFlag.getText() != null && !qualityFlag.getText().isEmpty() ) {
            return true;
        }
        if (researcherName.getText() != null && !researcherName.getText().isEmpty() ) {
            return true;
        }
        if (researcherInstitution.getText() != null && !researcherInstitution.getText().isEmpty() ) {
            return true;
        }
        if (fullVariableName.getText() != null && !fullVariableName.getText().isEmpty() ) {
            return true;
        }
        if (referenceMethod.getText() != null && !referenceMethod.getText().isEmpty() ) {
            return true;
        }
        if (biologicalSubject.getText() != null && !biologicalSubject.getText().isEmpty() ) {
            return true;
        }
        if (duration.getText() != null && !duration.getText().isEmpty() ) {
            return true;
        }
        if (lifeStage.getText() != null && !lifeStage.getText().isEmpty() ) {
            return true;
        }
        if (speciesIdCode.getText() != null && !speciesIdCode.getText().isEmpty() ) {
            return true;
        }
        return false;
    }
    public List<Variable> getVariables() {
        return variableData.getList();
    }

    public void addVariables(List<Variable> variableList) {

        for (int i = 0; i < variableList.size(); i++) {
            Variable p = variableList.get(i);
            variableData.getList().add(p);
        }
        variableData.flush();
        variablePagination.rebuild(cellTablePager);
        setTableVisible(true);

    }

    @UiHandler("save")
    public void onSave(ClickEvent clickEvent) {


        // For some reason this returns a "0" in debug mode.
        String valid = String.valueOf( form.validate());
        String warning = Constants.NOT_COMPLETE;
        NotifyType type = NotifyType.WARNING;
        if ( measured.getValue() == null  ) {
            valid = "false";
            warning = Constants.MEASURED;
            type = NotifyType.DANGER;
        }
        if ( observationDetail.getValue() == null ) {
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
            eventBus.fireEventFromSource(new SectionSave(getGenericVariable(), Constants.SECTION_GENERIC), GenericVariablePanel.this);
            Variable v = getGenericVariable();
            variableData.getList().add(v);
            variableData.flush();
            variablePagination.rebuild(cellTablePager);
            setTableVisible(true);
            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.SUCCESS);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.COMPLETE, settings);
            if ( showTable ) {
                setTableVisible(true);
                form.reset();
            }
        }

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