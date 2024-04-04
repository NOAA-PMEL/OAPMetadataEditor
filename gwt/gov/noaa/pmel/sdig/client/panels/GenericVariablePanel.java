package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.TableContextualType;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.oracles.InstrumentSuggestOracle;
import gov.noaa.pmel.sdig.client.oracles.ObservationTypeSuggestOracle;
import gov.noaa.pmel.sdig.client.oracles.VariableSuggestOracle;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.SuggestBox;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.*;

/**
 * Created by rhs on 3/8/17.
 */
public class GenericVariablePanel extends MultiPanel<Variable> {

    // Maybe someday this will be used dynamically
    // currently prevents requiring observation details (in-situ, etc) and Measured/Calculated
    private static boolean strict = false;

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

    Variable displayedVariable = null;
    Variable editVariable = null;

    int pageSize = 3;

    VariableSuggestOracle variableSuggestOracle = new VariableSuggestOracle();
    InstrumentSuggestOracle instrumentSuggestOracle = new InstrumentSuggestOracle();
    ObservationTypeSuggestOracle observationTypeSuggestOracle = new ObservationTypeSuggestOracle();

    public void reset() {
        form.reset();
        displayedVariable = null;
        editIndex = -1;
        editing = false;
        if ( editVariable != null ) {
            show(editVariable);
            editVariable = null;
        }
        setAllEditable(true);
        resetDropDowns();
    }

    private void resetDropDowns() {
        observationDetail.reset();
        measured.reset();
    }

    public void setEditing(boolean isEditing) {
        editing = isEditing;
    }

    public void clearVariables() {
        tableData.getList().clear();
        tableData.flush();
        tablePagination.rebuild(cellTablePager);
        redrawTableAndSetHighlight(false);
        setTableVisible(false);
    }

    interface VariablePanelUiBinder extends UiBinder<HTMLPanel, GenericVariablePanel> {
    }

    private static VariablePanelUiBinder ourUiBinder = GWT.create(VariablePanelUiBinder.class);

    public GenericVariablePanel() {
        super(Constants.SECTION_VARIABLES);
        fullVariableName = new SuggestBox(variableSuggestOracle);
        fullVariableName.getValueBox().addDoubleClickHandler(new DoubleClickHandler() {
            @Override
            public void onDoubleClick(DoubleClickEvent event) {
                NativeEvent nevent = event.getNativeEvent();
                String type = nevent.getType();
                fullVariableName.showSuggestionList();
            }
        });

        samplingInstrument = new SuggestBox(instrumentSuggestOracle);
        analyzingInstrument = new SuggestBox(instrumentSuggestOracle);
        observationType = new SuggestBox(observationTypeSuggestOracle);

        initWidget(ourUiBinder.createAndBindUi(this));

        clear.addClickHandler(clearIt);
        addCellTableListeners();

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

        cellTable.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
        cellTable.addCellPreviewHandler(new CellPreviewEvent.Handler<Variable>() {
            @Override
            public void onCellPreview(CellPreviewEvent<Variable> event) {
//                OAPMetadataEditor.logToConsole("event:"+ event.getNativeEvent().getType());
                if ( !editing && "mouseover".equals(event.getNativeEvent().getType())) {
                    show(event.getValue(), false);
                    form.validate();
                } else if ( !editing && "mouseout".equals(event.getNativeEvent().getType())) {
                    reset();
                }
            }
        });

        cellTable.addColumn(editColumn);
        editColumn.setCellStyleNames("text-center");

        // abbrevation
        TextColumn<Variable> abbrevColumn = new TextColumn<Variable>() {
            @Override
            public String getValue(Variable object) {
                return object.getAbbreviation();
            }
        };
        cellTable.addColumn(abbrevColumn,
                SimpleHtmlSanitizer.getInstance().sanitize("Variable Name or <br>Column Header"));

        // Add a text column to show the name.
        TextColumn<Variable> nameColumn = new TextColumn<Variable>() {
            @Override
            public String getValue(Variable object) {
                return object.getFullVariableName();
            }
        };
        cellTable.addColumn(nameColumn, "Full Name");

        cellTable.addColumn(deleteColumn);
        deleteColumn.setCellStyleNames("text-center");

        cellTable.setColumnWidth(0, 12, Style.Unit.PCT);
        cellTable.setColumnWidth(1, 20, Style.Unit.PCT);
        cellTable.setColumnWidth(3, 12, Style.Unit.PCT);

        // set RowStyles on required fields
        cellTable.setRowStyles(new RowStyles<Variable>() {
            @Override
            public String getStyleNames(Variable row, int rowIndex) {
                if (((row.getAbbreviation() == null) || (row.getAbbreviation().isEmpty()))
                || ((row.getFullVariableName() == null) || (row.getFullVariableName().isEmpty()))) {
                    return TableContextualType.DANGER.getCssName();
                }
                else {
                    return "";
                }
            }
        });

        cellTablePager.setDisplay(cellTable);

        tableData.addDataDisplay(cellTable);

        cellTable.setPageSize(pageSize);

        save.setEnabled(false);
    }
    private void setAllEditable(boolean editable) {
        abbreviation.setEnabled(editable);
        observationType.setEnabled(editable);
        manipulationMethod.setEnabled(editable);
//        observationDetail.setEnabled(editable);
        units.setEnabled(editable);
//        measured.setEnabled(editable);
        calculationMethod.setEnabled(editable);
        samplingInstrument.setEnabled(editable);
        analyzingInstrument.setEnabled(editable);
        detailedInformation.setEnabled(editable);
        fieldReplicate.setEnabled(editable);
        uncertainty.setEnabled(editable);
        qualityFlag.setEnabled(editable);
        researcherName.setEnabled(editable);
        researcherInstitution.setEnabled(editable);
        fullVariableName.setEnabled(editable);
        referenceMethod.setEnabled(editable);
        biologicalSubject.setEnabled(editable);
        duration.setEnabled(editable);
        lifeStage.setEnabled(editable);
        speciesIdCode.setEnabled(editable);
    }
    public void show(Variable variable, boolean editable) {
        setAllEditable(editable);
        editing = editable;
        if ( editable ) {
            displayedVariable = variable;
        } else {
            editVariable = getGenericVariable();
        }
        show(variable);
    }
    public void show(Variable variable) {
        if ( variable.getAbbreviation() != null ) {
            abbreviation.setText(variable.getAbbreviation());
            abbreviation.removeStyleName("has-error");
        } else {
            abbreviation.addStyleName("has-error");
        }
        if ( variable.getFullVariableName() != null ) {
            fullVariableName.setText(variable.getFullVariableName());
            fullVariableName.removeStyleName("has-error");
        } else {
            fullVariableName.addStyleName("has-error");
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
            units.removeStyleName("has-error");
        } else {
            units.addStyleName("has-error");
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
        if ( variable.getReferenceMethod() != null ) {
            referenceMethod.setText(variable.getReferenceMethod());
        }
        if ( variable.getBiologicalSubject() != null ) {
            biologicalSubject.setText(variable.getBiologicalSubject());
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

        Variable commonVariable = displayedVariable != null ? displayedVariable : new Variable();

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
        commonVariable.setCalculationMethod(calculationMethod.getValue());
        commonVariable.setDetailedInformation(detailedInformation.getText());
        commonVariable.setFieldReplicate(fieldReplicate.getText());
        commonVariable.setUncertainty(uncertainty.getText());
        commonVariable.setQualityFlag(qualityFlag.getText());
        commonVariable.setResearcherName(researcherName.getText());
        commonVariable.setResearcherInstitution(researcherInstitution.getText());
        commonVariable.setFullVariableName(fullVariableName.getText());
        commonVariable.setReferenceMethod(referenceMethod.getText());
        commonVariable.setBiologicalSubject(biologicalSubject.getText());
        commonVariable.setDuration(duration.getText());
        commonVariable.setLifeStage(lifeStage.getText());
        commonVariable.setSpeciesIdCode(speciesIdCode.getText());
        if ( editing ) {
            commonVariable.setPosition(editIndex);
        }
        return commonVariable;
    }

    public boolean isDirty() {
        return isDirty(originalList);
    }

    @Override
    public boolean isDirty(Variable original) {
        return !getGenericVariable().equals(original);
    }

    public boolean isDirty(List<Variable> originals) {
        boolean isDirty = false;
        if ( hasContent()) {
            addCurrentVariable();
        }
        if ( originals == null || originals.isEmpty() ) {
            return !getVariables().isEmpty();
        }
        Set<Variable>thisVariables = new TreeSet<>(getVariables());
        if ( thisVariables.size() != originals.size()) { return true; }
        Iterator<Variable>originalVariables = new TreeSet<>(originals).iterator();
        for ( Variable v : thisVariables ) {
            if ( !v.equals(originalVariables.next())) {
                isDirty = true;
                break;
            }
        }
        return isDirty;
    }

    public boolean hasContent() {
        boolean hasContent = false;
        save.setEnabled(false);
        if (abbreviation.getText() != null && !abbreviation.getText().isEmpty() ) {
            hasContent = true;
        }
        if (observationType.getText() != null && !observationType.getText().isEmpty() ) {
            hasContent = true;
        }
        if (manipulationMethod.getText() != null && !manipulationMethod.getText().isEmpty() ) {
            hasContent = true;
        }
        if (units.getText() != null && !units.getText().isEmpty() ) {
            hasContent = true;
        }
        if (samplingInstrument.getText() != null && !samplingInstrument.getText().isEmpty() ) {
            hasContent = true;
        }
        if (analyzingInstrument.getText() != null && !analyzingInstrument.getText().isEmpty() ) {
            hasContent = true;
        }
        if (calculationMethod.getText() != null && !calculationMethod.getText().isEmpty() ) {
            hasContent = true;
        }
        if (detailedInformation.getText() != null && !detailedInformation.getText().isEmpty() ) {
            hasContent = true;
        }
        if (fieldReplicate.getText() != null && !fieldReplicate.getText().isEmpty() ) {
            hasContent = true;
        }
        if (uncertainty.getText() != null && !uncertainty.getText().isEmpty() ) {
            hasContent = true;
        }
        if (qualityFlag.getText() != null && !qualityFlag.getText().isEmpty() ) {
            hasContent = true;
        }
        if (researcherName.getText() != null && !researcherName.getText().isEmpty() ) {
            hasContent = true;
        }
        if (researcherInstitution.getText() != null && !researcherInstitution.getText().isEmpty() ) {
            hasContent = true;
        }
        if (fullVariableName.getText() != null && !fullVariableName.getText().isEmpty() ) {
            hasContent = true;
        }
        if (referenceMethod.getText() != null && !referenceMethod.getText().isEmpty() ) {
            hasContent = true;
        }
        if (biologicalSubject.getText() != null && !biologicalSubject.getText().isEmpty() ) {
            hasContent = true;
        }
        if (duration.getText() != null && !duration.getText().isEmpty() ) {
            hasContent = true;
        }
        if (lifeStage.getText() != null && !lifeStage.getText().isEmpty() ) {
            hasContent = true;
        }
        if (speciesIdCode.getText() != null && !speciesIdCode.getText().isEmpty() ) {
            hasContent = true;
        }

        if (hasContent == true) {
            save.setEnabled(true);
        }

        return hasContent;
    }
    public List<Variable> getVariables() {
        return tableData.getList();
    }

    public void addVariables(List<Variable> variableList) {
        originalList = variableList;
        for (int i = 0; i < variableList.size(); i++) {
            Variable p = variableList.get(i);
            if ( p == null ) { // XXX badness
                GWT.log("Null variable at pos " + i);
                continue;
            }
            p.setPosition(i);
            tableData.getList().add(p);
        }
        tableData.flush();
        tablePagination.rebuild(cellTablePager);
        setTableVisible(true);
    }

    @UiHandler("save")
    public void onSave(ClickEvent clickEvent) {
        GWT.log("onSave:"+clickEvent.getSource());
        doSave();
    }
    public boolean doSave() {

        // For some reason this returns a "0" in debug mode.
        String valid = String.valueOf( form.validate());
        String warning = Constants.NOT_COMPLETE;
        NotifyType type = NotifyType.WARNING;
        // These are not currently required...
        if ( strict && measured.getValue() == null  ) {
            valid = "false";
            warning = Constants.MEASURED;
            type = NotifyType.DANGER;
        }
        if ( strict && observationDetail.getValue() == null ) {
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
            return false;
        } else {
            editIndex = INACTIVE;
            this.editing = false;
            if ( hasContent()) {
                Variable v = getGenericVariable();
                if (! v.isEditing) {
                    addCurrentVariable();
                } else {
                    reset();
                }
                v.isEditing = false;
            }

            // check if any variable in tableData is missing required fields
            // XXX I'm not sure this is necessary after validating the form.
            boolean meetsRequired = true;
            for (int i = 0; i < tableData.getList().size(); i++) {
                Variable v = tableData.getList().get(i);
                if (((v.getAbbreviation() == null) || (v.getAbbreviation().isEmpty()))
                || ((v.getFullVariableName() == null) || (v.getFullVariableName().isEmpty()))) {
                    meetsRequired = false;
                }
            }
            if ( !meetsRequired ) {
                return false;
            }
            if (meetsRequired == true  && tableData.getList().size() > 0) {
                // XXX This event is ignored in OAPMetadataEditor.saveSection(), the only place it is "used"
                eventBus.fireEventFromSource(new SectionSave(getGenericVariable(), Constants.SECTION_VARIABLES), GenericVariablePanel.this);
            }

            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.SUCCESS);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.COMPLETE, settings);
            if ( showTable && tableData.getList().size() > 0) {
                setTableVisible(true);
                redrawTableAndSetHighlight(false);
                reset();
            }
            save.setEnabled(false);
        }
        return true;
    }

    private void updateEditingVariable() {
        Variable v = getGenericVariable();
    }
    private void addCurrentVariable() {
        Variable v = getGenericVariable();
        addVariable(v);
        setTableVisible(true);
        reset();
    }
    public void addVariable(Variable v) {
        if ( v == null ) { return; }
        int position = v.getPosition() >= 0 ? v.getPosition() : tableData.getList().size();
        v.setPosition(position);
        tableData.getList().add(position, v);
        tableData.flush();
        tablePagination.rebuild(cellTablePager);
    }
    private void resetRequiredLabels() {
        abbreviation.removeStyleName("has-error");
        fullVariableName.removeStyleName("has-error");
        units.removeStyleName("has-error");
    }
    public boolean valid() {
        resetRequiredLabels();
        String valid = String.valueOf(form.validate());
        if (valid.equals("false") ||
                valid.equals("0")) {
            return false;
        } else {
            return true;
        }
    }

    @UiHandler({"abbreviation","manipulationMethod","units","calculationMethod",
            "detailedInformation","fieldReplicate","uncertainty","qualityFlag",
            "researcherName","researcherInstitution","referenceMethod",
            "biologicalSubject","duration","lifeStage", "speciesIdCode"})
    public void onChange(ChangeEvent event) {
//        OAPMetadataEditor.debugLog("getsource: "+event.getSource());
        save.setEnabled(true);
    }
    @UiHandler({"observationType","samplingInstrument",
            "analyzingInstrument", "fullVariableName"})
    public void onValueChange(ValueChangeEvent<String> event) {
//        OAPMetadataEditor.debugLog("Here be the new value:" + event.getValue());
        save.setEnabled(true);
    }

    public void setEnableButton(ButtonCell button, boolean enabled) {
        if (enabled) {
            button.setEnabled(true);
        }
        else {
            button.setEnabled(false);
        }
    }

}