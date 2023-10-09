package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.builder.shared.DivBuilder;
import com.google.gwt.dom.builder.shared.InputBuilder;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.AbstractHeaderOrFooterBuilder;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.client.widgets.SizedEditTextCell;
import gov.noaa.pmel.sdig.shared.bean.StandardGas;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.*;


/**
 * Created by rhs on 3/22/17.
 * Changed by lkp on some later date.
 */
public class Co2Panel extends Composite implements GetsDirty<Variable> {
    private static final char CO2_VARS_SEPARATOR = ';';
    private static final String MANUF_ID = "sg_manuf_";
    private static final String CONC_ID = "sg_conc_";
    private static final String UNC_ID = "sg_uncert_";
    private static final String TRACE_ID = "sg_trace_";

    private static final String RMV_BTN_ID = "sg_rmv_";
    private static final String STD_GAS_ROW_ = "sg_row_";
    private static final String CO2_COMMON = "CO2_Common";

//    List<Variable> variablesList;

    @UiField
    CellTable<Variable> variablesTable;

//    @UiField
//    Pagination variablePagination;

    ListDataProvider<Variable> variableData = new ListDataProvider<Variable>();
//    private SimplePager cellTablePager = new SimplePager();

    @UiField
    Heading heading;

    // 012 Standardization technique description
    @UiField
    TextBox standardizationTechnique;

    // 013 Frequency of standardization
    @UiField
    TextBox freqencyOfStandardization;

    // 024 at what temperature was pCO2 reported
    @UiField
    TextBox pco2Temperature;

//    // 030 Depth of seawater intake -> move to Co2CommonVariablePanel
//    @UiField
//    TextBox intakeDepth;

    // 031 Drying method for CO2 gas
    @UiField
    TextBox dryingMethod;

    // 033 Equilibrator type
    @UiField
    TextBox equilibratorType;

    // 034 Equilibrator volume (L)
    @UiField
    TextBox equilibratorVolume;

    // 036 Headspace gas flow rate (L/min)
    @UiField
    TextBox gasFlowRate;

    // 038 How was pressure inside the equilibrator measured.
    @UiField
    TextBox equilibratorPressureMeasureMethod;

    // 039 How was temperature inside the equilibrator measured .
    @UiField
    TextBox equilibratorTemperatureMeasureMethod;

//    // 041 Location of seawater intake -> move to Co2CommonVariablePanel
//    @UiField
//    TextBox intakeLocation;

    @UiField
    Button addStdGasButton;
    @UiField
    Row standardGasRow0;

    // Traceability of standard gases to WMO standards
    @UiField
    TextBox standardGasTraceability0;

    @UiField
    Modal stdGasBtnPopover;

    // 043 Manufacturer of standard gas
    @UiField
    TextBox standardGasManufacture0;

    // 028 Concentrations of standard gas
    @UiField
    TextBox standardGasConcentration0;

    // 056 Uncertainties of standard gas
    @UiField
    TextBox standardGasUncertainty0;

//    @UiField
//    TextBox standardGasId0;

    // 044 Manufacturer of the gas detector
    @UiField
    TextBox gasDetectorManufacture;

    // 046 Model of the gas detector
    @UiField
    TextBox gasDetectorModel;

    // 049 Resolution of the gas detector
    @UiField
    TextBox gasDectectorResolution;

    // 052 Temperature correction method
    @UiField
    TextBox temperatureCorrectionMethod;

    // 057 Uncertainty of the gas detector
    @UiField
    TextBox gasDectectorUncertainty;

    // 058 Vented or not
    @UiField
    ButtonDropDown vented;

    // 059 Water flow rate (L/min)
    @UiField
    TextBox flowRate;

    // 060 Water vapor correction method
    @UiField
    TextBox vaporCorrection;

    // Uncertainty of temperature measured inside the equlibrator
    @UiField
    TextBox equilibratorTemperatureMeasureUncertainty;

    // Calibration method and frequency for temperature sensor inside the equlibrator
    @UiField
    TextBox equilibratorTemperatureSensorCalibrationMethod;

    // How was the total measurement pressure determined?
    @UiField
    TextBox totalMeasurementPressureDetermined;

    // Uncertainty of total measurement pressure, and how was this calculated?
    @UiField
    TextBox totalMeasurementPressureUncertaintyCalculated;

    // Calibration method and frequency for pressure sensor(s)
    @UiField
    TextBox equilibratorPressureSensorCalibrationMethod;

    // Method to calculate pCO2 from xCO2 (reference)
    @UiField
    TextBox pco2FromXco2Method;

    // Method to calculate fCO2 from pCO2 (reference)
    @UiField
    TextBox fco2FromPco2Method;

    @UiField
    Co2CommonVariablePanel common;

    @UiField
    Button save;

    @UiField
    Form form;

    List<Row> addedRows = new ArrayList<>();
    HashMap<String, TextBox> stdGasManufTextBoxes = new LinkedHashMap<>();
    HashMap<String, TextBox> stdGasConcTextBoxes = new LinkedHashMap<>();
    HashMap<String, TextBox> stdGasUncTextBoxes = new LinkedHashMap<>();
    HashMap<String, TextBox> stdGasTraceTextBoxes = new LinkedHashMap<>();
    int row0index = 2;
    //    int addedInstIdx = 0;
    Container formContainer;

    ButtonCell addButton = new ButtonCell(IconType.EDIT, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL);
    ButtonCell deleteButton = new ButtonCell(IconType.TRASH, ButtonType.DANGER, ButtonSize.EXTRA_SMALL);

    interface Co2aPanelUiBinder extends UiBinder<HTMLPanel, Co2Panel> {
    }

    private static Co2aPanelUiBinder ourUiBinder = GWT.create(Co2aPanelUiBinder.class);

    public enum VarType {
        OTHER,
        FCO2_WATER_EQU,
        FCO2_WATER_SST,
        PCO2_WATER_EQU,
        PCO2_WATER_SST,
        XCO2_WATER_EQU,
        XCO2_WATER_SST,
        FCO2_ATM_ACTUAL,
        FCO2_ATM_INTERP,
        PCO2_ATM_ACTUAL,
        PCO2_ATM_INTERP,
        XCO2_ATM_ACTUAL,
        XCO2_ATM_INTERP; /*,
        SEA_SURFACE_TEMP,
        EQUILIBRATOR_TEMP,
        SEA_LEVEL_PRESSURE,
        EQUILIBRATOR_PRESSURE,
        SALINITY,
        WOCE_CO2_WATER,
        WOCE_CO2_ATM;
        */

        static List<String> _names = new ArrayList<>(VarType.values().length);
//        static List<String> _lower = new ArrayList<>(VarType.values().length);
        public static List<String> names() {
            synchronized (_names) {
                if (_names.isEmpty()) {
                    for (VarType t : values()) {
                        _names.add(t.name());
//                        _lower.add(t.name().toLowerCase());
                    }
                }
                return _names;
            }
        }

        public static VarType guess(String fromName) {
            String upper = fromName.toUpperCase();
            OAPMetadataEditor.debugLog("guess " + upper);
            if ( _names.contains(upper)) {
                return VarType.valueOf(upper);
            }
            return VarType.OTHER;
        }
    }

    public Co2Panel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        Widget thisWidget = this.asWidget();
        HTMLPanel panel = (HTMLPanel) this.getWidget();
        Form form = (Form)panel.getWidget(0);
        FieldSet fieldSet = (FieldSet) form.getWidget(2);
        Container container = (Container) fieldSet.getWidget(0);
//        Element ste = standardizationTechnique.getElement();
//        Element stp = ste.getParentElement();
//        stp.addClassName("has-error");
//        OAPMetadataEditor.logToConsole("that: " + thatWidget);
//        OAPMetadataEditor.setFieldError(true, standardizationTechnique);
        setDefaults();
//        common.abbreviation.setEnabled(false);
//        common.abbreviation.setVisible(false);
//        common.fullVariableName.setText("CO2 variables recorded.");
//        common.fullVariableName.setEnabled(false);
//        common.heading.setText("Enter the Information for the CO2 variables.");
        common.fieldReplicate.setAllowBlank(true);
        common.fieldReplicateForm.setVisible(false);
        save.addClickHandler(saveIt);
//        common.abbreviationModal.setTitle("24.1 Column header name of the variable in the data files, e.g., fCO2/pCO2/xCO2, etc.");
//        common.observationTypeModal.setTitle("24.2 How the variable was observed, e.g., surface underway, profile, time series etc.");
//        common.manipulationMethodModal.setTitle("25.4 In perturbation experiments, seawater carbonate chemistry can be manipulated by different techniques, such as bubbling CO2, adding acids or bases, etc.");
//        common.observationDetailModal.setTitle("25.3 Whether the variable belong to an in-situ observed variable, or a manipulation condition variable, or a response variable in a biological experimental study");
        // #OADSHELP
        common.measuredModal.setTitle("25.6 Whether the variable is measured in-situ, or calculated from other variables");
        // #OADSHELP
        common.calculationMethodModal.setTitle("25.7 Variables can be calculated using different sets of constants or different software.");
        // #OADSHELP
        common.samplingInstrumentModal.setTitle("25.8 Instrument that is used to collect water samples, or deploy sensors, etc. For example, a Niskin bottle, pump, CTD, etc is a sampling instrument.");

        common.analyzingInstrumentModal.setTitle("24.7 Instrument that was used to analyze the water samples collected with the 'collection method' above, or the sensors that are mounted on the 'platform' to measure the water body continuously. We encourage you to document as many details (such as the make, model, resolution, precisions, etc) of the instrument as you can here.");

        // #OADSHELP
        common.intakeLocationModal.setTitle("25.9 Whereabout of the seawater intake");
        common.intakeDepthModal.setTitle("25.10 Water depth of the seawater intake");

        // Analyzing information with citation (SOP etc)
        common.detailedInformationModal.setTitle("24.8 Detailed description of the analyzing procedures, including the citation of the SOP used for the analysis (e.g. SOP 3a;  Dickson, A.G., Sabine, C.L. and Christian, J.R.  2007.  Guide to Best Practices for Ocean CO2  Measurements).");

        // #OADSHELP ??? Does apply here ???
        common.fieldReplicateModal.setTitle("26.15 Repetition of sample collection and measurement, e.g., triplicate samples.");

        common.uncertaintyModal.setTitle("24.12 Uncertainty of the results (e.g., 1%, 2 μmol/kg), or any pieces of information that are related to the quality control of the variable.");

        // Quality control/Data quality scheme (name of scheme)
        common.qcAppliedModal.setTitle("24.9 Indicate if quality control procedures were applied.");

        // #OADSHELP
        common.researcherNameModal.setTitle("25.23.1 The name of the PI, whose research team measured or derived this parameter.");
        common.researcherInstitutionModal.setTitle("25.23.2 The institution of the PI, whose research team measured or derived this parameter.");
//        common.fullVariableNameModal.setTitle("Full variable name.");
        common.referenceMethodModal.setTitle("24.33 Citation for the fCO2 method.");
//        common.unitsModal.setTitle("25.5 Units of the variable, e.g., μatm.");

        // Data quality flag scheme
        common.modalQcSchemeName.setTitle("24.11 Data quality flag scheme");
        common.modalContentQcSchemeName.setHTML(
                "<p>Indicate which of the following data quality schemes was used. For "
                        + "more details: <br /><a href='https://odv.awi.de/fileadmin/user_upload/odv/misc/ODV4_QualityFlagSets.pdf' "
                        + "target='_blank'>https://odv.awi.de/fileadmin/user_upload/odv/misc/ODV4_QualityFlagSets.pdf</a>"
                        + "<p>If no data quality scheme was used, please leave blank.</p>");

        // Abbreviation of data quality flag scheme
        common.qcVariableNameModal.setTitle("24.10 Column header name of the data quality flag scheme applied in the data files, e.g. QC, Quality, etc.");

        // Changes to Method or SOP
        common.sopChangesModal.setTitle("24.34 Indicate if any changes were made to the method as described in the SOP, such as changes in the sample collection method, changes in storage of the sample, different volume, changes to the CRM used, etc. Please provide a detailed list of all of the changes made.");
        common.collectionMethodModal.setTitle("24.4 Method that was used to collect water samples, or deploy sensors, etc. For example, bottle collection with a Niskin bottle, pump, CTD, etc is a collection method.");
        common.analyzingInformationModal.setTitle("24.8 Detailed description of the analyzing procedures, including the citation of the SOP used for the analysis (e.g. SOP 3a;  Dickson, A.G., Sabine, C.L. and Christian, J.R.  2007.  Guide to Best Practices for Ocean CO2  Measurements).");

        if (OAPMetadataEditor.getIsSocatParam()) {

//            common.qcAppliedLabel.setText("Data quality scheme (name of scheme)");
//            standardizationTechniqueLabel.setText("Calibration method");
//            freqencyOfStandardizationLabel.setText("Frequency of calibration");

//            ventedLabel.setText("Equilibrator vented or not");
//            flowRateLabel.setText("Equilibrator water flow rate (L min-1)");
//            gasFlowRateLabel.setText("Equilibrator headspace gas flow rate (L min-1)");
        }

        // Vented or Not
        List<String> ventedNames = new ArrayList<String>();
        List<String> ventedValues = new ArrayList<String>();
        ventedNames.add("Vented "); // needs space to push carat over... :-(
        ventedValues.add("true");
        ventedNames.add("Not Vented ");
        ventedValues.add("false");
        vented.init("Select Vented or Not ", ventedNames, ventedValues);

        List<String> co2varTypeNames = VarType.names();
        com.google.gwt.user.cellview.client.Column <Variable, String> varTypeColumn =
                addColumn(new SelectionCell(co2varTypeNames), "Var Type",
                        new GetValue<String>() {
                            @Override
                            public String getValue(Variable var) {
                                OAPMetadataEditor.logToConsole("getValue varType for " + var.getAbbreviation());
                                try {
                                    String socatType = var.getSocatType();
                                    if ( socatType != null && ! socatType.trim().isEmpty() )
                                        return socatType;
                                    else
                                        return guessVarType(var);
                                } catch (Exception exception) {
                                    OAPMetadataEditor.logToConsole(exception.toString());
                                    return VarType.OTHER.name();
                                }
                            }
                        },
                        new FieldUpdater<Variable, String>() {
                            @Override
                            public void update(int index, Variable var, String value) {
                                OAPMetadataEditor.logToConsole("UPDATE varType for " + var.getAbbreviation() + " as " + value);
                                var.setSocatType(value);
                            }
                        });

        com.google.gwt.user.cellview.client.Column <Variable, String> abbrevColumn =
            addColumn(new SizedEditTextCell(25), "Abbreviation",
                    new GetValue<String>() {
                        @Override
                        public String getValue(Variable var) {
                            return var.getAbbreviation();
                        }
                    },
                    new FieldUpdater<Variable, String>() {
                        @Override
                        public void update(int index, Variable var, String value) {
                            OAPMetadataEditor.logToConsole("UPDATE abbrev for " + var.getAbbreviation() + " as " + value);
                            var.setAbbreviation(value);
                        }
                    });

        com.google.gwt.user.cellview.client.Column <Variable, String> unitsColumn =
            addColumn(new SizedEditTextCell(10), "Units",
                    new GetValue<String>() {
                        @Override
                        public String getValue(Variable var) {
                            OAPMetadataEditor.logToConsole("units for " + var.getAbbreviation() + ":" + var.getUnits());
                            return var.getUnits();
                        }
                    },
                    new FieldUpdater<Variable, String>() {
                        @Override
                        public void update(int index, Variable var, String value) {
                            OAPMetadataEditor.logToConsole("UPDATE units for " + var.getAbbreviation() + " as " + value);
                            var.setUnits(value);
                        }
                    });
        com.google.gwt.user.cellview.client.Column<Variable, String> fullNameColumn =
            addColumn(new SizedEditTextCell(72), "Full Name",
                    new GetValue<String>() {
                        @Override
                        public String getValue(Variable var) {
            //                OAPMetadataEditor.logToConsole("var name for " + var.getAbbreviation() + ":" + var.getFullVariableName());
                            return var.getFullVariableName();
                        }
                    },
                    new FieldUpdater<Variable, String>() {
                        @Override
                        public void update(int index, Variable var, String value) {
                            OAPMetadataEditor.logToConsole("UPDATE var name for " + var.getAbbreviation() + " as " + value);
                            var.setFullVariableName(value);
                        }
                    });
        com.google.gwt.user.cellview.client.Column <Variable, String> deleteColumn =
                new com.google.gwt.user.cellview.client.Column<Variable, String>(deleteButton) {
                        @Override
                        public String getValue(Variable object) {
                                                                      return "Delete";
                                                                                      }
                    };
        deleteColumn.setFieldUpdater(new FieldUpdater<Variable, String>() {
            @Override
            public void update(int index, Variable variable, String value) {
//                form.reset(); // Because the mouseover will have filled the form
                variableData.getList().remove(variable);
                variableData.flush();
                if ( variableData.getList().size() == 0 ) {
//                    setTableVisible(false);
//                    show(variable, true);
                    reset();
//                    eventBus.fireEventFromSource(new SectionUpdater(Constants.SECTION_GENERIC),GenericVariablePanel.this);
//                } else {
//                    setTableVisible(true);
                }
            }
        });
        Header<String> addVariableHeader = new Header<String>(addButton) {
            @Override
            public String getValue() {
                return "Add";
            }
        };
        addVariableHeader.setUpdater(new ValueUpdater<String>() {
            @Override
            public void update(String value) {
                OAPMetadataEditor.logToConsole("update:" + value);
                variableData.getList().add(new Variable());
            }
        });
        variablesTable.addColumn(deleteColumn, addVariableHeader);

        variablesTable.setColumnWidth(abbrevColumn,20., Style.Unit.PCT);
        variablesTable.setColumnWidth(unitsColumn,10., Style.Unit.PCT);
        variablesTable.setColumnWidth(fullNameColumn,60., Style.Unit.PCT);

        variableData.addDataDisplay(variablesTable);

        variablesTable.setVisible(true);
    }

    private String guessVarType(Variable var) {
        OAPMetadataEditor.logToConsole("guess varType for var: " + var.getAbbreviation());
        String vabbrev = var.getAbbreviation();
        return VarType.guess(vabbrev).name();
    }

    private void setDefaults() {
        common.isBig5 = true;
    }

    public void addCO2variables(List<Variable> variables) {
        List<Variable> dataList = variableData.getList();
        dataList.clear();
        this.reset();
        if ( variables.isEmpty()) {
            return;
        }
        Variable co2var = variables.remove(0);
        if ( ! co2var.getAbbreviation().equals(CO2_COMMON)) {
            Variable copy = co2var.clone();
            dataList.add(copy);
            co2var.setAbbreviation("CO2_Common");
            co2var.setFullVariableName("CO2_Common");
            OAPMetadataEditor.logToConsole("CO2vars[0] is NOT CO2_Common!");
        }
        if ( variables.size() > 0 ) {
            dataList.addAll(variables);
        } else {
            String varList = co2var.getAbbreviation();
            List<Variable>co2vars = new ArrayList<>();
            OAPMetadataEditor.logToConsole("Co2Panel varList:"+varList);
            if ( varList != null && ! varList.isEmpty()) {
                co2var.setAbbreviation("");
                String[] vars = split(varList, CO2_VARS_SEPARATOR);
                for (String var : vars) {
                    if (var.isEmpty()) {
                        continue;
                    }
                    String[] parts = split(var, ':');
                    Variable v = new Variable();
                    v.setAbbreviation(parts[0].trim());
                    String units = "";
                    if (parts.length > 1) {
                        if ( parts[1] != null ) {
                            units = parts[1].trim();
                        } else {
                            OAPMetadataEditor.logToConsole("Null units for " + v.getAbbreviation());
                        }
                    }
                    v.setUnits( units );
                    v.setFullVariableName(v.getAbbreviation());
                    co2vars.add(v);
                }
            }
            dataList.addAll(co2vars);
        }
        show(co2var);
    }

    private static String[] split(String listString, char separator) {
        List<String> pieces = new ArrayList<>();
        if ( listString != null && ! listString.isEmpty()) {
            do {
                String piece = "";
                int idx = listString.indexOf(separator);
                if ( idx < 0 ) {
                    piece = listString;
                    listString = "";
                } else if ( idx < listString.length()) {
                    piece = listString.substring(0, idx);
                    listString = idx < listString.length() - 1 ?
                            listString.substring(idx+1) :
                            "";
                }
                if ( !piece.isEmpty()) {
                    pieces.add(piece);
                }
            } while ( ! listString.isEmpty());
        }
        return pieces.toArray(new String[pieces.size()]);
    }

    //    public void setCO2variables(List<Variable> variables) {
//        common.fullVariableName.clear();
//        if ( variables == null || variables.size() == 0) {
//            return;
//        }
//        String comma = "";
//        StringBuilder bldr = new StringBuilder();
//        for (Variable v : variables) {
//            String abbrev = v.getAbbreviation().toLowerCase();
//            String vtype = v.getVariableType();
//            if ( vtype != null && vtype.contains("AquGasConc")) {
//                bldr.append(comma).append(abbrev);
//                comma = ", ";
//            }
//        }
//        common.fullVariableName.setText(bldr.toString());
//    }
//        co2.setStandardizationTechnique(standardizationTechnique.getText());
//        co2.setFreqencyOfStandardization(freqencyOfStandardization.getText());
//        co2.setPco2Temperature(pco2Temperature.getText());
//        co2.setGasConcentration(standardGasConcentration0.getText());
//        co2.setDryingMethod(dryingMethod.getText());
//        co2.setEquilibratorType(equilibratorType.getText());
//        co2.setEquilibratorVolume(equilibratorVolume.getText());
//        co2.setGasFlowRate(gasFlowRate.getText());
//        co2.setEquilibratorPressureMeasureMethod(equilibratorPressureMeasureMethod.getText());
//        co2.setPressureMeasurementCalibrationMethod(equilibratorPressureSensorCalibrationMethod.getText());
//        co2.setEquilibratorTemperatureMeasureMethod(equilibratorTemperatureMeasureMethod.getText());
//        co2.setTemperatureMeasurementCalibrationMethod(equilibratorTemperatureSensorCalibrationMethod.getText());
//        co2.setUncertaintyOfTemperature(equilibratorTemperatureMeasureUncertainty.getText());
//        co2.setStandardGasManufacture(standardGasManufacture0.getText());
//        co2.setTraceabilityOfStdGas(standardGasTraceability.getText());
//        co2.setGasDetectorManufacture(gasDetectorManufacture.getText());
//        co2.setGasDetectorModel(gasDetectorModel.getText());
//        co2.setGasDectectorResolution(gasDectectorResolution.getText());
//        co2.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
//        co2.setStandardGasUncertainties(standardGasUncertainty0.getText());
//        co2.setGasDectectorUncertainty(gasDectectorUncertainty.getText());
//        co2.setVented(vented.getText());
//        co2.setFlowRate(flowRate.getText());
//        co2.setVaporCorrection(vaporCorrection.getText());
    public List<Variable> getCO2variables() {
        GWT.log("co2panel:getCO2variables");
        List<Variable> co2vars = new ArrayList<>();
        Variable co2common = common.getCommonVariable();
//        for (Variable co2 : variableData.getList()) {
            // fill in from common panel
        // common fields already filled in by C02CommonPanel
//            co2.setObservationType(displayed.getObservationType());
//            co2.setSamplingInstrument(displayed.getSamplingInstrument());
//            co2.setAnalyzingInstrument(displayed.getAnalyzingInstrument());
//            co2.setMeasured(displayed.getMeasured());
//            co2.setCalculationMethod(displayed.getCalculationMethod());
//            co2.setReferenceMethod(displayed.getReferenceMethod());
//            co2.setDetailedInformation(displayed.getDetailedInformation());
//            co2.setUncertainty(displayed.getUncertainty());
//            co2.setQcApplied(displayed.getQcApplied());
//            co2.setResearcherName(displayed.getResearcherName());
//            co2.setResearcherInstitution(displayed.getResearcherInstitution());
//            // socat missing
//            co2.setIntakeLocation(displayed.getIntakeLocation());
//            co2.setIntakeDepth(displayed.getIntakeDepth());
//            co2.setQcVariableName(displayed.getQcVariableName());
//            co2.setQcSchemeName(displayed.getQcSchemeName());
//            co2.setSopChanges(displayed.getSopChanges());
            // fill in from fields
            co2common.setStandardizationTechnique(standardizationTechnique.getText());
            co2common.setFreqencyOfStandardization(freqencyOfStandardization.getText());
            co2common.setPco2Temperature(pco2Temperature.getText());
//            co2common.setGasConcentration(standardGasConcentration0.getText());
            co2common.setDryingMethod(dryingMethod.getText());
            co2common.setEquilibratorType(equilibratorType.getText());
            co2common.setEquilibratorVolume(equilibratorVolume.getText());
            co2common.setGasFlowRate(gasFlowRate.getText());
            co2common.setEquilibratorPressureMeasureMethod(equilibratorPressureMeasureMethod.getText());
            co2common.setEquilibratorTemperatureMeasureMethod(equilibratorTemperatureMeasureMethod.getText());
//            co2common.setStandardGasManufacture(standardGasManufacture0.getText());
            co2common.setGasDetectorManufacture(gasDetectorManufacture.getText());
            co2common.setGasDetectorModel(gasDetectorModel.getText());
            co2common.setGasDectectorResolution(gasDectectorResolution.getText());
            co2common.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
//            co2common.setStandardGasUncertainties(standardGasUncertainty0.getText());
            co2common.setGasDectectorUncertainty(gasDectectorUncertainty.getText());
            co2common.setVented(vented.getValue());
            co2common.setFlowRate(flowRate.getText());
            co2common.setVaporCorrection(vaporCorrection.getText());
            // missing socat
//            co2common.setTraceabilityOfStdGas(standardGasTraceability.getText());
            co2common.setPco2CalcMethod(pco2FromXco2Method.getText());
            co2common.setFco2CalcMethod(fco2FromPco2Method.getText());
            co2common.setTemperatureMeasurementCalibrationMethod(equilibratorTemperatureSensorCalibrationMethod.getText());
            co2common.setUncertaintyOfTemperature(equilibratorTemperatureMeasureUncertainty.getText());
            co2common.setPressureMeasurementCalibrationMethod(equilibratorPressureSensorCalibrationMethod.getText());
            co2common.setTotalPressureCalcMethod(totalMeasurementPressureDetermined.getText());
            co2common.setUncertaintyOfTotalPressure(totalMeasurementPressureUncertaintyCalculated.getText());
            co2common.setStandardGases(getStandardGases());
//        }
        List<Variable> dataList = variableData.getList();
        if ( dataList.isEmpty() ) {
            OAPMetadataEditor.warn("You must define at least one CO2 variable.");
            co2common.setAbbreviation(CO2_COMMON);
            co2common.setFullVariableName(CO2_COMMON);
            co2common.setUnits("");
            co2vars.add(co2common);
        } else {
            Variable c0 = dataList.get(0);
            co2common.setAbbreviation(c0.getAbbreviation());
            co2common.setFullVariableName(c0.getFullVariableName());
            co2common.setUnits(c0.getUnits());
            co2vars.add(co2common);
            for (int i = 1; i<dataList.size(); i++) {
                co2vars.add(dataList.get(i));
            }
        }
        return co2vars;
    }

    private List<StandardGas> getStandardGases() {
        List<StandardGas> stdGases = new ArrayList<>();
        StandardGas std = new StandardGas(standardGasManufacture0.getText().trim(),
                                          standardGasConcentration0.getText().trim(),
                                          standardGasUncertainty0.getText().trim(),
                                          standardGasTraceability0.getText().trim());
        if ( std.hasContent()) {
            stdGases.add(std);
        }
        for (Row row : addedRows ) {
            String rowId = row.getId();
            std = new StandardGas(stdGasManufTextBoxes.get(rowId).getText().trim(),
                                  stdGasConcTextBoxes.get(rowId).getText().trim(),
                                  stdGasUncTextBoxes.get(rowId).getText().trim(),
                                  stdGasTraceTextBoxes.get(rowId).getText().trim());
            if ( std.hasContent()) {
                stdGases.add(std);
            }
        }
        return stdGases;
    }

    public Variable getDisplayedVariable() {
        GWT.log("co2panel:getDisplayedVariable");
        Variable co2 = common.getCommonVariable();
        co2.setStandardizationTechnique(standardizationTechnique.getText());
        co2.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        co2.setPco2Temperature(pco2Temperature.getText());
//        co2.setGasConcentration(standardGasConcentration0.getText());
        co2.setDryingMethod(dryingMethod.getText());
        co2.setEquilibratorType(equilibratorType.getText());
        co2.setEquilibratorVolume(equilibratorVolume.getText());
        co2.setGasFlowRate(gasFlowRate.getText());
        co2.setEquilibratorPressureMeasureMethod(equilibratorPressureMeasureMethod.getText());
        co2.setPressureMeasurementCalibrationMethod(equilibratorPressureSensorCalibrationMethod.getText());
        co2.setEquilibratorTemperatureMeasureMethod(equilibratorTemperatureMeasureMethod.getText());
        co2.setTemperatureMeasurementCalibrationMethod(equilibratorTemperatureSensorCalibrationMethod.getText());
        co2.setUncertaintyOfTemperature(equilibratorTemperatureMeasureUncertainty.getText());
//        co2.setStandardGasManufacture(standardGasManufacture0.getText());
//        co2.setTraceabilityOfStdGas(standardGasTraceability.getText());
        co2.setGasDetectorManufacture(gasDetectorManufacture.getText());
        co2.setGasDetectorModel(gasDetectorModel.getText());
        co2.setGasDectectorResolution(gasDectectorResolution.getText());
        co2.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
//        co2.setStandardGasUncertainties(standardGasUncertainty0.getText());
        co2.setGasDectectorUncertainty(gasDectectorUncertainty.getText());
        co2.setVented(vented.getValue());
        co2.setFlowRate(flowRate.getText());
        co2.setVaporCorrection(vaporCorrection.getText());
        co2.setStandardGases(getStandardGases());
        return co2;
    }
    public void fill(Variable pco2a) {
        GWT.log("co2panel:fill(pco2a)");
        common.fillCommonVariable(pco2a);
        pco2a.setStandardizationTechnique(standardizationTechnique.getText());
        pco2a.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        pco2a.setPco2Temperature(pco2Temperature.getText());
//        pco2a.setGasConcentration(standardGasConcentration0.getText());
//        pco2a.setIntakeDepth(intakeDepth.getText());
        pco2a.setDryingMethod(dryingMethod.getText());
        pco2a.setEquilibratorType(equilibratorType.getText());
        pco2a.setEquilibratorVolume(equilibratorVolume.getText());
        pco2a.setGasFlowRate(gasFlowRate.getText());
        pco2a.setEquilibratorPressureMeasureMethod(equilibratorPressureMeasureMethod.getText());
        pco2a.setEquilibratorTemperatureMeasureMethod(equilibratorTemperatureMeasureMethod.getText());
        pco2a.setUncertaintyOfTemperature(equilibratorTemperatureMeasureUncertainty.getText());
//        pco2a.setIntakeLocation(intakeLocation.getText());
//        pco2a.setStandardGasManufacture(standardGasManufacture0.getText());
        pco2a.setGasDetectorManufacture(gasDetectorManufacture.getText());
        pco2a.setGasDetectorModel(gasDetectorModel.getText());
        pco2a.setGasDectectorResolution(gasDectectorResolution.getText());
        pco2a.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
//        pco2a.setStandardGasUncertainties(standardGasUncertainty0.getText());
        // XXX Note that the traceability of gases was not being set! XXX
        pco2a.setGasDectectorUncertainty(gasDectectorUncertainty.getText());
        pco2a.setVented(vented.getValue());
        pco2a.setFlowRate(flowRate.getText());
        pco2a.setVaporCorrection(vaporCorrection.getText());
        pco2a.setStandardGases(getStandardGases());
    }
    public void show(Variable co2common) {
        GWT.log("showing co2: " + co2common);
        if ( co2common == null ) {
            reset();
            return;
        }

        common.show(co2common);
        if (co2common.getStandardizationTechnique() != null ) {
            standardizationTechnique.setText(co2common.getStandardizationTechnique());
        }

        if ( co2common.getFreqencyOfStandardization() != null ) {
            freqencyOfStandardization.setText(co2common.getFreqencyOfStandardization());
        }

        if ( co2common.getPco2Temperature() != null ) {
            pco2Temperature.setText(co2common.getPco2Temperature());
        }

//        if ( co2common.getGasConcentration() != null ) {
//            standardGasConcentration0.setText(co2common.getGasConcentration());
//        }

//        if ( pco2a.getIntakeDepth() != null ) {
//            intakeDepth.setText(pco2a.getIntakeDepth());
//        }

        if (co2common.getDryingMethod() != null ) {
            dryingMethod.setText(co2common.getDryingMethod());
        }

        if ( co2common.getEquilibratorType() != null ) {
            equilibratorType.setText(co2common.getEquilibratorType());
        }

        if ( co2common.getEquilibratorVolume() != null ) {
            equilibratorVolume.setText(co2common.getEquilibratorVolume());
        }

        if ( co2common.getGasFlowRate() != null ) {
            gasFlowRate.setText(co2common.getGasFlowRate());
        }

        if ( co2common.getEquilibratorPressureMeasureMethod() != null ) {
            equilibratorPressureMeasureMethod.setText(co2common.getEquilibratorPressureMeasureMethod());
        }

        if ( co2common.getEquilibratorTemperatureMeasureMethod() != null ) {
            equilibratorTemperatureMeasureMethod.setText(co2common.getEquilibratorTemperatureMeasureMethod());
        }

//        if ( pco2a.getIntakeLocation() != null ) {
//            intakeLocation.setText(pco2a.getIntakeLocation());
//        }

//        if ( co2common.getStandardGasManufacture() != null ) {
//            standardGasManufacture0.setText(co2common.getStandardGasManufacture());
//        }

        if ( co2common.getGasDetectorManufacture() != null ) {
            gasDetectorManufacture.setText(co2common.getGasDetectorManufacture());
        }

        if ( co2common.getGasDetectorModel() != null ) {
            gasDetectorModel.setText(co2common.getGasDetectorModel());
        }

        if ( co2common.getGasDectectorResolution() != null ) {
            gasDectectorResolution.setText(co2common.getGasDectectorResolution());
        }

        if ( co2common.getTemperatureCorrectionMethod() != null ) {
            temperatureCorrectionMethod.setText(co2common.getTemperatureCorrectionMethod());
        }

//        if ( co2common.getStandardGasUncertainties() != null ) {
//            standardGasUncertainty0.setText(co2common.getStandardGasUncertainties());
//        }

        if ( co2common.getGasDectectorUncertainty() != null ) {
            gasDectectorUncertainty.setText(co2common.getGasDectectorUncertainty());
        }

        if ( co2common.getVented() != null ) {
            String isVented = vented.getTruth("vented", co2common.getVented());
            vented.setSelected(isVented);
        }

        if ( co2common.getFlowRate() != null ) {
            flowRate.setText(co2common.getFlowRate());
        }

        if ( co2common.getVaporCorrection() != null ) {
            vaporCorrection.setText(co2common.getVaporCorrection());
        }

        // SDG 14.3.1 + SOCAT additions
        if ( co2common.getTemperatureMeasurementCalibrationMethod() != null ) {
            equilibratorTemperatureSensorCalibrationMethod.setText(co2common.getTemperatureMeasurementCalibrationMethod());
        }
        if ( co2common.getUncertaintyOfTemperature() != null ) {
            equilibratorTemperatureMeasureUncertainty.setText(co2common.getUncertaintyOfTemperature());
        }
        if ( co2common.getUncertaintyOfTotalPressure() != null ) {
            totalMeasurementPressureUncertaintyCalculated.setText(co2common.getUncertaintyOfTotalPressure());
        }
        if ( co2common.getPressureMeasurementCalibrationMethod() != null ) {
            equilibratorPressureSensorCalibrationMethod.setText(co2common.getPressureMeasurementCalibrationMethod());
        }
//        if ( co2common.getTraceabilityOfStdGas() != null ) {
//            standardGasTraceability.setText(co2common.getTraceabilityOfStdGas());
//        }
        if ( co2common.getPco2CalcMethod() != null ) {
            pco2FromXco2Method.setText(co2common.getPco2CalcMethod());
        }
        if ( co2common.getFco2CalcMethod() != null ) {
            fco2FromPco2Method.setText(co2common.getFco2CalcMethod());
        }
        if ( co2common.getTotalPressureCalcMethod() != null ) {
            totalMeasurementPressureDetermined.setText(co2common.getTotalPressureCalcMethod());
        }
        if ( co2common.getUncertaintyOfTotalPressure() != null ) {
            totalMeasurementPressureUncertaintyCalculated.setText(co2common.getUncertaintyOfTotalPressure());
        }
        showStandardGases(co2common);
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
//            if ( isDirty() && common.observationDetail.getValue() == null ) {
//                valid="false";
//                warning = Constants.DETAILS;
//                type = NotifyType.DANGER;
//            }
            if ( valid.equals("false") ||
                    valid.equals("0")) {
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(type);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(warning, settings);
            } else {
                common.eventBus.fireEventFromSource(new SectionSave(getDisplayedVariable(), Constants.SECTION_CO2), Co2Panel.this);
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.SUCCESS);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.COMPLETE, settings);
            }        }
    };
    public boolean isDirty(List<Variable> co2vars) {
       if ( co2vars.size() != variableData.getList().size()) { return true; }
       if ( ! co2vars.isEmpty()) {
           return isDirty(co2vars.get(0));
       } else {
           return isDirty();
       }
    }
    public boolean isDirty(Variable original) {
        boolean isDirty =
            original == null ?
            isDirty() :
            common.isDirty(original) ||
            isDirty(standardizationTechnique, original.getStandardizationTechnique() ) ||
            isDirty(freqencyOfStandardization, original.getFreqencyOfStandardization() ) ||
            isDirty(pco2Temperature, original.getPco2Temperature() ) ||
//            isDirty(standardGasConcentration0, original.getGasConcentration() ) ||
//            isDirty(intakeDepth, original.getIntakeDepth() ) ||
            isDirty(dryingMethod, original.getDryingMethod() ) ||
            isDirty(equilibratorType, original.getEquilibratorType() ) ||
            isDirty(equilibratorVolume, original.getEquilibratorVolume() ) ||
            isDirty(gasFlowRate, original.getGasFlowRate() ) ||
            isDirty(equilibratorPressureMeasureMethod, original.getEquilibratorPressureMeasureMethod() ) ||
            isDirty(equilibratorTemperatureMeasureMethod, original.getEquilibratorTemperatureMeasureMethod() ) ||
//            isDirty(intakeLocation, original.getIntakeLocation() ) ||
//            isDirty(standardGasManufacture0, original.getStandardGasManufacture() ) ||
            isDirty(gasDetectorManufacture, original.getGasDetectorManufacture() ) ||
            isDirty(gasDetectorModel, original.getGasDetectorModel() ) ||
            isDirty(gasDectectorResolution, original.getGasDectectorResolution() ) ||
            isDirty(temperatureCorrectionMethod, original.getTemperatureCorrectionMethod() ) ||
//            isDirty(standardGasUncertainty0, original.getStandardGasUncertainties() ) ||
                    // XXX Note was not checking isDirty(traceablitity) XXX
            isDirty(gasDectectorUncertainty, original.getGasDectectorUncertainty() ) ||
            isDirty(vented.getValue(), original.getVented() ) ||
            isDirty(flowRate, original.getFlowRate() ) ||
            isDirty(vaporCorrection, original.getVaporCorrection()) ||
            standardGasesChanged(original);
        return isDirty;
    }

    private boolean standardGasesChanged(Variable original) {
        List<StandardGas> gases = getStandardGases();
        List<StandardGas> originalGases = original.getStandardGases();
        if (gases.size() != originalGases.size()) {
            return true;
        }
        for ( int i = 0; i < gases.size(); i++) {
            if ( ! gases.get(i).sEquals(originalGases.get(i))) {
                GWT.log("std gases differ");
                return true;
            }
        }
        GWT.log("std gases the same");
        return false;
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
        if (pco2Temperature.getText().trim() != null && !pco2Temperature.getText().isEmpty() ) {
            return true;
        }
        if (standardGasConcentration0.getText().trim() != null && !standardGasConcentration0.getText().isEmpty() ) {
            return true;
        }
//        if (intakeDepth.getText().trim() != null && !intakeDepth.getText().isEmpty() ) {
//            return true;
//        }
        if (dryingMethod.getText().trim() != null && !dryingMethod.getText().isEmpty() ) {
            return true;
        }
        if (equilibratorType.getText().trim() != null && !equilibratorType.getText().isEmpty() ) {
            return true;
        }
        if (equilibratorVolume.getText().trim() != null && !equilibratorVolume.getText().isEmpty() ) {
            return true;
        }
        if (gasFlowRate.getText().trim() != null && !gasFlowRate.getText().isEmpty() ) {
            return true;
        }
        if (equilibratorPressureMeasureMethod.getText().trim() != null && !equilibratorPressureMeasureMethod.getText().isEmpty() ) {
            return true;
        }
        if (equilibratorTemperatureMeasureMethod.getText().trim() != null && !equilibratorTemperatureMeasureMethod.getText().isEmpty() ) {
            return true;
        }
//        if (intakeLocation.getText().trim() != null && !intakeLocation.getText().isEmpty() ) {
//            return true;
//        }
        if (standardGasManufacture0.getText().trim() != null && !standardGasManufacture0.getText().isEmpty() ) {
            return true;
        }
        if (gasDetectorManufacture.getText().trim() != null && !gasDetectorManufacture.getText().isEmpty() ) {
            return true;
        }
        if (gasDetectorModel.getText().trim() != null && !gasDetectorModel.getText().isEmpty() ) {
            return true;
        }
        if (gasDectectorResolution.getText().trim() != null && !gasDectectorResolution.getText().isEmpty() ) {
            return true;
        }
        if (temperatureCorrectionMethod.getText().trim() != null && !temperatureCorrectionMethod.getText().isEmpty() ) {
            return true;
        }
        if (standardGasUncertainty0.getText().trim() != null && !standardGasUncertainty0.getText().isEmpty() ) {
            return true;
        }
        if (gasDectectorUncertainty.getText().trim() != null && !gasDectectorUncertainty.getText().isEmpty() ) {
            return true;
        }
        if (vented.getValue() != null && !vented.getValue().isEmpty() ) {
            return true;
        }
        if (flowRate.getText().trim() != null && !flowRate.getText().isEmpty() ) {
            return true;
        }
        if (vaporCorrection.getText().trim() != null && !vaporCorrection.getText().isEmpty() ) {
            return true;
        }
        return false;
    }
    public void reset() {
        form.reset();
        vented.reset();
        common.reset();
        common.qcApplied.reset();
        clearVariables();
        clearStdGases();
        setDefaults();
    }
    public void clearVariables() {
        variableData.getList().clear();
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

    @UiHandler("addStdGasButton")
    public void onAdd(ClickEvent clickEvent) {
        GWT.log("Add StdGas clicked:"+clickEvent);
        addStdGasRow();
    }

    private void setFormContainer() {
        formContainer = (Container)standardGasRow0.getParent();
//        GWT.log("parent:"+ formContainer);
        int row0idx = formContainer.getWidgetIndex(standardGasRow0);
        if ( row0idx != row0index ) {
            GWT.log("WARN: Row0index has changed from " + row0index + " to " + row0idx);
            row0index = row0idx;
        }
        GWT.log("standardGasRow index:"+ row0index);
    }

    private TextBox addRowField(Row newRow, ColumnSize cSize, String itemId, String title) {
        org.gwtbootstrap3.client.ui.Column theColumn = new org.gwtbootstrap3.client.ui.Column(cSize);
        FormGroup theFgrp = new FormGroup();
        TextBox theTextBox = new TextBox();
        theTextBox.setPlaceholder(title);
        theTextBox.setId(itemId);
        theFgrp.add(theTextBox);
        theColumn.add(theFgrp);
        newRow.add(theColumn);
        return theTextBox;
    }

    private Row addStdGasRow() {
        setFormContainer();
        org.gwtbootstrap3.client.ui.Column row0col = (org.gwtbootstrap3.client.ui.Column) standardGasRow0.getWidget(0);
        int addedGasIdx = addedRows.size() + 1;
        int addedId = row0index + addedGasIdx;
        addedGasIdx += 1; // so it's 1-based.
        Row newRow = new Row();
        String row_id = STD_GAS_ROW_ + addedId;
        newRow.setId(row_id);

        // manufacturer
        TextBox stdGasManufTextBox = addRowField(newRow, ColumnSize.SM_4, MANUF_ID+ addedId, "Gas Manufacturer " + addedGasIdx);
        stdGasManufTextBoxes.put(row_id, stdGasManufTextBox);
//        org.gwtbootstrap3.client.ui.Column manufColumn = new org.gwtbootstrap3.client.ui.Column(ColumnSize.SM_12, ColumnSize.MD_6, ColumnSize.LG_2);
//        FormGroup manufFgrp = new FormGroup();
//        TextBox stdGasManufTextBox = new TextBox();
//        stdGasManufTextBox.setPlaceholder("Std Gas Manufacturer " + addedGasIdx);
//        stdGasManufTextBox.setId(MANUF_ID+ addedId);
//        manufFgrp.add(stdGasManufTextBox);
//        manufColumn.add(manufFgrp);
//        newRow.add(manufColumn);

        // concentration
        TextBox stdGasConcTextBox = addRowField(newRow, ColumnSize.SM_2, CONC_ID+ addedId, "Concentration" );
        stdGasConcTextBoxes.put(row_id, stdGasConcTextBox);
//        org.gwtbootstrap3.client.ui.Column concColumn = new org.gwtbootstrap3.client.ui.Column(ColumnSize.SM_12, ColumnSize.MD_6, ColumnSize.LG_2);
//        FormGroup concFgrp = new FormGroup();
//        TextBox stdGasConcTextBox = new TextBox();
//        stdGasConcTextBox.setPlaceholder("Std Gas Concentration " + (addedGasIdx+1));
//        stdGasConcTextBox.setId(CONC_ID+ addedId);
//        concFgrp.add(stdGasConcTextBox);
//        concColumn.add(concFgrp);
//        newRow.add(concColumn);

        // uncertainty
        TextBox stdGasUncTextBox = addRowField(newRow, ColumnSize.SM_2, UNC_ID+ addedId, "Uncertainty" );
        stdGasUncTextBoxes.put(row_id, stdGasUncTextBox);
//        org.gwtbootstrap3.client.ui.Column uncColumn = new org.gwtbootstrap3.client.ui.Column(ColumnSize.SM_12, ColumnSize.MD_6, ColumnSize.LG_2);
//        FormGroup uncFgrp = new FormGroup();
//        TextBox stdGasUncTextBox = new TextBox();
//        stdGasUncTextBox.setPlaceholder("Std Gas Uncertainty " + (addedGasIdx+1));
//        stdGasUncTextBox.setId(UNC_ID+ addedId);
//        uncFgrp.add(stdGasUncTextBox);
//        uncColumn.add(uncFgrp);
//        newRow.add(uncColumn);

        // traceability
        TextBox stdGasTraceTextBox = addRowField(newRow, ColumnSize.SM_2, TRACE_ID+ addedId, "Traceability" );
        stdGasTraceTextBoxes.put(row_id, stdGasTraceTextBox);
//        org.gwtbootstrap3.client.ui.Column traceColumn = new org.gwtbootstrap3.client.ui.Column(ColumnSize.SM_12, ColumnSize.MD_6, ColumnSize.LG_2);
//        FormGroup traceFgrp = new FormGroup();
//        TextBox stdGasTraceTextBox = new TextBox();
//        stdGasTraceTextBox.setPlaceholder("WMO Traceability " + (addedGasIdx+1));
//        stdGasTraceTextBox.setId(UNC_ID+ addedId);
//        traceFgrp.add(stdGasTraceTextBox);
//        traceColumn.add(traceFgrp);
//        newRow.add(traceColumn);

        // remove row button
        org.gwtbootstrap3.client.ui.Column buttonColumn = new org.gwtbootstrap3.client.ui.Column(ColumnSize.SM_2);
        FormGroup buttonFgrp = new FormGroup();
        Button removeButton = new Button("REMOVE");
        removeButton.setId(RMV_BTN_ID+ addedId);
        removeButton.addStyleName("float_right");
        removeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                removeStdGas(event.getSource());
            }
        });
        buttonFgrp.add(removeButton);
        buttonColumn.add(buttonFgrp);
        newRow.add(buttonColumn);

        formContainer.insert(newRow, addedId);

        addedRows.add(newRow);
        return newRow;
    }

    private void removeStdGas(Object source) {
        GWT.log("remove:"+source);
        Button removeButton = (Button) source;
        Row rowToRemove = getRowFor(removeButton); // Row)bclm.getParent();
        removeStdGasRow(rowToRemove);
        addedRows.remove(rowToRemove);
    }

    private void removeStdGasRow(Row rowToRemove) {
        boolean removed = formContainer.remove(rowToRemove);
        GWT.log("removed: " + removed);
        rowToRemove.removeFromParent();
        removeStdGasFields(rowToRemove);
    }

    private void removeStdGasFields(Row rowToRemove) {
        String rowId = rowToRemove.getId();
//        String rowIdxStr = rowId.substring(rowId.lastIndexOf("_")+1);
//        int rowIdx = row0index + 1;
//        try {
//            rowIdx = Integer.parseInt(rowIdxStr);
//        } catch (NumberFormatException e) {
//            GWT.log("Failed to parse row index " + rowIdxStr);
//        }
//        int instListIdx = rowIdx - row0index;
        removeField(rowId, stdGasManufTextBoxes);
        removeField(rowId, stdGasConcTextBoxes);
        removeField(rowId, stdGasUncTextBoxes);
        removeField(rowId, stdGasTraceTextBoxes);
    }
    private void removeField(String rowId, Map<String, TextBox> map) {
        if ( map.containsKey(rowId)) {
            map.remove(rowId);
        } else {
            GWT.log("WARN: Missing textBox for added standardGas row "+ rowId);
        }
    }

    private Row getRowFor(Widget widget) {
        FormGroup bfg = (FormGroup)widget.getParent();
        org.gwtbootstrap3.client.ui.Column bclm = (org.gwtbootstrap3.client.ui.Column)bfg.getParent();
        Row rowToRemove = (Row)bclm.getParent();
        return rowToRemove;
    }

    private void showStandardGases(Variable co2var) {
        GWT.log("showing stdgases:" + co2var);
        clearStdGases();
        List<StandardGas>standardGases = co2var.getStandardGases();
        GWT.log("standardGases:" + standardGases);
        if ( standardGases.isEmpty()) { return; }
        StandardGas s0 = standardGases.get(0);
        standardGasManufacture0.setText(s0.getManufacturer().trim());
        standardGasConcentration0.setText(s0.getConcentration().trim());
        standardGasUncertainty0.setText(s0.getUncertainty().trim());
        standardGasTraceability0.setText(s0.getWmoTraceability().trim());
        for (int i = 1; i<standardGases.size(); i++) {
            StandardGas std = standardGases.get(i);
            addStandardGas(std);
        }
    }

    private void clearStdGases() {
        standardGasManufacture0.setText("");
        standardGasConcentration0.setText("");
        standardGasUncertainty0.setText("");
        standardGasTraceability0.setText("");
        for (Row addedRow : addedRows) {
            removeStdGasRow(addedRow);
        }
        addedRows.clear();
//        if ( personInstitutions.size() > 1 ) {
//            GWT.log("Orphan institution suggest box in clearInstitutions");
//            personInstitutions.clear();
//            personInstitutions.put(ROW_0_ID, institution0);
//        }
    }

    private void addStandardGas(StandardGas gas) {
        Row addedRow = addStdGasRow();
        String rowId = addedRow.getId();
        stdGasManufTextBoxes.get(rowId).setText(gas.getManufacturer());
        stdGasConcTextBoxes.get(rowId).setText(gas.getConcentration());
        stdGasUncTextBoxes.get(rowId).setText(gas.getUncertainty());
        stdGasTraceTextBoxes.get(rowId).setText(gas.getWmoTraceability());
    }

    ClickHandler addVariableHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            variableData.getList().add(new Variable());
        }
    };
    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private static interface GetValue<C> {
        C getValue(Variable contact);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> com.google.gwt.user.cellview.client.Column<Variable, C> addColumn(Cell<C> cell, String headerText,
                                              final GetValue<C> getter, FieldUpdater<Variable, C> fieldUpdater) {
        com.google.gwt.user.cellview.client.Column<Variable, C> column = new com.google.gwt.user.cellview.client.Column<Variable, C>(cell) {
            @Override
            public C getValue(Variable object) {
                return getter.getValue(object);
            }
        };
        column.setFieldUpdater(fieldUpdater);
        variablesTable.addColumn(column, headerText);
        return column;
    }

    class Co2VarHeaderBuilder extends AbstractHeaderOrFooterBuilder<Variable> {

        public Co2VarHeaderBuilder(AbstractCellTable<Variable> table, boolean isFooter) {
            super(table, isFooter);
        }

        @Override
        protected boolean buildHeaderOrFooterImpl() {
            Button addButton = new Button("Add");
            addButton.addClickHandler(addVariableHandler);

            TableRowBuilder row = startRow();
            TableCellBuilder th = row.startTH();
            th.html(SafeHtmlUtils.fromTrustedString("Abbreviation"));
            th.endTH();
            th= row.startTH();
            th.html(SafeHtmlUtils.fromTrustedString("Full Name"));
            th.endTH();
            th= row.startTH();
            DivBuilder div = th.startDiv();
            InputBuilder btnbldr = div.startButtonInput();

            div.html(SafeHtmlUtils.fromTrustedString(addButton.getElement().getInnerHTML()));
            div.end();
            th.endTH();
            row.endTR();
            return true;
        }
    }
}
