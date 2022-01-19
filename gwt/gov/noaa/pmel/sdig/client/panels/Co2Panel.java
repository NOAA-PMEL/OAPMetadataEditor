package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.cell.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.builder.shared.DivBuilder;
import com.google.gwt.dom.builder.shared.InputBuilder;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ListDataProvider;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.widgets.SizedEditTextCell;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rhs on 3/22/17.
 */
public class Co2Panel extends Composite implements GetsDirty<Variable> {
    private static final char CO2_VARS_SEPARATOR = ';';

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

    // 028 Concentrations of standard gas
    @UiField
    TextBox gasConcentration;

    // 030 Depth of seawater intake -> move to Co2CommonVariablePanel
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

    // 041 Location of seawater intake -> move to Co2CommonVariablePanel
//    @UiField
//    TextBox intakeLocation;

    // 043 Manufacturer of standard gas
    @UiField
    TextBox standardGasManufacture;

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

    // 056 Uncertainties of standard gas
    @UiField
    TextBox standardGasUncertainties;

    // 057 Uncertainty of the gas detector
    @UiField
    TextBox gasDectectorUncertainty;

    // 058 Vented or not
    @UiField
    TextBox vented;

    // 059 Water flow rate (L/min)
    @UiField
    TextBox flowRate;

    // 060 Water vapor correction method
    @UiField
    TextBox vaporCorrection;

    /*
    TextBox equilibratorTemperatureMeasureUncertainty
    String uncertaintyOfTemperature
    TextBox equilibratorTemperatureSensorCalibrationMethod
    String temperatureMeasurementCalibrationMethod
    TextBox totalMeasurementPressureDetermined
    String totalPressureCalcMethod
    TextBox totalMeasurementPressureUncertaintyCalculated
    String uncertaintyOfTotalPressure
    TextBox calibrationMethodPressureSensorFrequency
    String pressureMeasurementCalibrationMethod
    TextBox stdGasTraceability
    String traceabilityOfStdGas
    TextBox pco2FromXco2Method
    String pCo2CalcMethod
    TextBox fco2FromPco2Method
    String fCo2CalcMethod
     */

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

    // Traceability of standard gases to WMO standards
    @UiField
    TextBox stdGasTraceability;

    // Method to calculate pCO2 from xCO2 (reference)
    @UiField
    TextBox pco2FromXco2Method;

    // Method to calculate fCO2 from pCO2 (reference)
    @UiField
    TextBox fco2FromPco2Method;

    @UiField
    FormLabel ventedLabel;
    @UiField
    FormLabel flowRateLabel;
    @UiField
    FormLabel gasFlowRateLabel;
    @UiField
    FormLabel standardizationTechniqueLabel;
    @UiField
    FormLabel freqencyOfStandardizationLabel;


    @UiField
    Co2CommonVariablePanel common;

    @UiField
    Button save;

    @UiField
    Form form;

    ButtonCell addButton = new ButtonCell(IconType.EDIT, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL);
    ButtonCell deleteButton = new ButtonCell(IconType.TRASH, ButtonType.DANGER, ButtonSize.EXTRA_SMALL);

    interface Co2aPanelUiBinder extends UiBinder<HTMLPanel, Co2Panel> {
    }

    private static Co2aPanelUiBinder ourUiBinder = GWT.create(Co2aPanelUiBinder.class);

    public Co2Panel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        setDefaults();
        // common.abbreviation.setEnabled(false);
//        common.abbreviation.setVisible(false);
//        common.fullVariableName.setText("CO2 variables recorded.");
//        common.fullVariableName.setEnabled(false);
//        common.heading.setText("Enter the Information for the CO2 variables.");
        common.fieldReplicate.setAllowBlank(true);
        common.fieldReplicateForm.setVisible(false);
        save.addClickHandler(saveIt);
//        common.abbreviationModal.setTitle("25.1 Column header name of the variable in the data files, e.g., pCO2, etc.");
//        common.observationTypeModal.setTitle("25.2 How the variable is observed, e.g., surface underway, profile, time series, model output, etc. For experimental data, this could be: laboratory experiment, pelagic mesocosm, benthic mesocosm, benthic FOCE type studies, natural pertubration site studies, etc");
//        common.manipulationMethodModal.setTitle("25.4 In perturbation experiments, seawater carbonate chemistry can be manipulated by different techniques, such as bubbling CO2, adding acids or bases, etc.");
//        common.observationDetailModal.setTitle("25.3 Whether the variable belong to an in-situ observed variable, or a manipulation condition variable, or a response variable in a biological experimental study");
        common.measuredModal.setTitle("25.6 Whether the variable is measured in-situ, or calculated from other variables");
        common.calculationMethodModal.setTitle("25.7 Variables can be calculated using different sets of constants or different software.");
        common.samplingInstrumentModal.setTitle("25.8 Instrument that is used to collect water samples, or deploy sensors, etc. For example, a Niskin bottle, pump, CTD, etc is a sampling instrument.");
        common.analyzingInstrumentModal.setTitle("25.11 Instrument that is used to analyze the water samples collected with the sampling instrument , or the sensors that are mounted on the 'sampling instrument' to measure the water body continuously. For example, a coulometer, winkler titrator, spectrophotometer, pH meter, thermosalinograph, oxygen sensor, YSI Multiparameter Meter, etc is an analyzing instrument. We encourage you to document as much details (such as the make, model, resolution, precisions, etc) of the instrument as you can here");
        common.detailedInformationModal.setTitle("25.12 Detailed description of the sampling and analyzing procedures, including calibration procedures, model number of the instrument, etc.");
        common.fieldReplicateModal.setTitle("??? Does apply here ???");
        common.uncertaintyModal.setTitle("25.20 Uncertainty of the results (e.g., 1%, 2 μmol/kg), or any pieces of information that are related to the quality control of the variable.");
        common.qcAppliedModal.setTitle("25.21 Describe what the quality control flags stand for, e.g., 2 = good value, 3 = questionable value, 4 = bad value. The use of WOCE quality flags are recommended.");
        common.researcherNameModal.setTitle("25.23.1 The name of the PI, whose research team measured or derived this parameter.");
        common.researcherInstitutionModal.setTitle("25.23.2 The institution of the PI, whose research team measured or derived this parameter.");
//        common.fullVariableNameModal.setTitle("Full variable name.");
        common.referenceMethodModal.setTitle("25.22 Citation for the pCO2 method.");
//        common.unitsModal.setTitle("25.5 Units of the variable, e.g., μatm.");

        common.qcSchemeNameModal.setTitle("22.7 Indicate if quality control procedures were applied.");
        common.qcVariableNameModal.setTitle("22.8 Column header name of the data quality flag scheme applied in the data files, e.g. QC, Quality, etc.");
        common.sopChangesModal.setTitle("20.2 Indicate if any changes were made to the method as described in the SOP, such as changes in the sample collection method, changes in storage of the sample, different volume, changes to the CRM used, etc. Please provide a detailed list of  all of the changes made.");
        common.collectionMethodModal.setTitle("21.4 Method that is used to collect water samples, or deploy sensors, etc. For example, bottle collection with a Niskin bottle, pump, CTD, etc is a collection method.");
        common.analyzingInformationModal.setTitle("20.6 Detailed description of the analyzing procedures, including the citation of the SOP used for the analysis (e.g. SOP 7;  Dickson, A.G., Sabine, C.L. and Christian, J.R.  2007.  Guide to Best Practices for Ocean CO2  Measurements).");

        if (OAPMetadataEditor.getIsSocatParam()) {

            common.qcAppliedLabel.setText("Data quality scheme (name of scheme)");
            standardizationTechniqueLabel.setText("Calibration method");
            freqencyOfStandardizationLabel.setText("Frequency of calibration");

            ventedLabel.setText("Equilibrator vented or not");
            flowRateLabel.setText("Equilibrator water flow rate (L min-1)");
            gasFlowRateLabel.setText("Equilibrator headspace gas flow rate (L min-1)");
        }

        Column<Variable, String> abbrevColumn = addColumn(new SizedEditTextCell(25), "Abbreviation", new GetValue<String>() {
            @Override
            public String getValue(Variable var) {
                return var.getAbbreviation();
            }
        }, new FieldUpdater<Variable, String>() {
            @Override
            public void update(int index, Variable var, String value) {
                OAPMetadataEditor.logToConsole("UPDATE abbrev for " + var.getAbbreviation() + " as " + value);
                var.setAbbreviation(value);
            }
        });

        Column<Variable, String> unitsColumn = addColumn(new SizedEditTextCell(10), "Units", new GetValue<String>() {
            @Override
            public String getValue(Variable var) {
//                OAPMetadataEditor.logToConsole("units for " + var.getAbbreviation() + ":" + var.getUnits());
                return var.getUnits();
            }
        }, new FieldUpdater<Variable, String>() {
            @Override
            public void update(int index, Variable var, String value) {
                OAPMetadataEditor.logToConsole("UPDATE units for " + var.getAbbreviation() + " as " + value);
                var.setUnits(value);
            }
        });
        Column<Variable, String> fullNameColumn = addColumn(new SizedEditTextCell(72), "Full Name", new GetValue<String>() {
            @Override
            public String getValue(Variable var) {
//                OAPMetadataEditor.logToConsole("var name for " + var.getAbbreviation() + ":" + var.getFullVariableName());
                return var.getFullVariableName();
            }
        }, new FieldUpdater<Variable, String>() {
            @Override
            public void update(int index, Variable var, String value) {
                OAPMetadataEditor.logToConsole("UPDATE var name for " + var.getAbbreviation() + " as " + value);
                var.setFullVariableName(value);
            }
        });
        Column<Variable, String> deleteColumn = new com.google.gwt.user.cellview.client.Column<Variable, String>(deleteButton) {
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
    private void setDefaults() {
        common.isBig5 = true;
    }

    public void _addCO2variables(List<Variable> variables) {
        List<Variable> dataList = variableData.getList();
        dataList.clear();
        this.reset();
        if ( variables.isEmpty()) {
            return;
        }
        Variable co2var = variables.get(0);
        if ( variables.size() > 1 ) {
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
    public void addCO2variables(List<Variable> variables) {
        List<Variable> dataList = variableData.getList();
        dataList.clear();
        this.reset();
        if ( variables.isEmpty()) {
            return;
        }
        Variable co2var = variables.get(0);
        if ( variables.size() > 1 ) {
            for (int i = 1; i < variables.size(); i++ ) {
                dataList.add(variables.get(i));
            }
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
//        co2.setGasConcentration(gasConcentration.getText());
//        co2.setDryingMethod(dryingMethod.getText());
//        co2.setEquilibratorType(equilibratorType.getText());
//        co2.setEquilibratorVolume(equilibratorVolume.getText());
//        co2.setGasFlowRate(gasFlowRate.getText());
//        co2.setEquilibratorPressureMeasureMethod(equilibratorPressureMeasureMethod.getText());
//        co2.setPressureMeasurementCalibrationMethod(equilibratorPressureSensorCalibrationMethod.getText());
//        co2.setEquilibratorTemperatureMeasureMethod(equilibratorTemperatureMeasureMethod.getText());
//        co2.setTemperatureMeasurementCalibrationMethod(equilibratorTemperatureSensorCalibrationMethod.getText());
//        co2.setUncertaintyOfTemperature(equilibratorTemperatureMeasureUncertainty.getText());
//        co2.setStandardGasManufacture(standardGasManufacture.getText());
//        co2.setTraceabilityOfStdGas(stdGasTraceability.getText());
//        co2.setGasDetectorManufacture(gasDetectorManufacture.getText());
//        co2.setGasDetectorModel(gasDetectorModel.getText());
//        co2.setGasDectectorResolution(gasDectectorResolution.getText());
//        co2.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
//        co2.setStandardGasUncertainties(standardGasUncertainties.getText());
//        co2.setGasDectectorUncertainty(gasDectectorUncertainty.getText());
//        co2.setVented(vented.getText());
//        co2.setFlowRate(flowRate.getText());
//        co2.setVaporCorrection(vaporCorrection.getText());
    public List<Variable> getCO2variables() {
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
            co2common.setGasConcentration(gasConcentration.getText());
            co2common.setDryingMethod(dryingMethod.getText());
            co2common.setEquilibratorType(equilibratorType.getText());
            co2common.setEquilibratorVolume(equilibratorVolume.getText());
            co2common.setGasFlowRate(gasFlowRate.getText());
            co2common.setEquilibratorPressureMeasureMethod(equilibratorPressureMeasureMethod.getText());
            co2common.setEquilibratorTemperatureMeasureMethod(equilibratorTemperatureMeasureMethod.getText());
            co2common.setStandardGasManufacture(standardGasManufacture.getText());
            co2common.setGasDetectorManufacture(gasDetectorManufacture.getText());
            co2common.setGasDetectorModel(gasDetectorModel.getText());
            co2common.setGasDectectorResolution(gasDectectorResolution.getText());
            co2common.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
            co2common.setStandardGasUncertainties(standardGasUncertainties.getText());
            co2common.setGasDectectorUncertainty(gasDectectorUncertainty.getText());
            co2common.setVented(vented.getText());
            co2common.setFlowRate(flowRate.getText());
            co2common.setVaporCorrection(vaporCorrection.getText());
            // missing socat
            co2common.setTraceabilityOfStdGas(stdGasTraceability.getText());
            co2common.setPco2CalcMethod(pco2FromXco2Method.getText());
            co2common.setFco2CalcMethod(fco2FromPco2Method.getText());
            co2common.setTemperatureMeasurementCalibrationMethod(equilibratorTemperatureSensorCalibrationMethod.getText());
            co2common.setUncertaintyOfTemperature(equilibratorTemperatureMeasureUncertainty.getText());
            co2common.setPressureMeasurementCalibrationMethod(equilibratorPressureSensorCalibrationMethod.getText());
            co2common.setTotalPressureCalcMethod(totalMeasurementPressureDetermined.getText());
            co2common.setUncertaintyOfTotalPressure(totalMeasurementPressureUncertaintyCalculated.getText());
//        }
        co2vars.add(co2common);
        co2vars.addAll(variableData.getList());
        return co2vars;
    }

    public Variable getDisplayedVariable() {
        Variable co2 = common.getCommonVariable();
        co2.setStandardizationTechnique(standardizationTechnique.getText());
        co2.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        co2.setPco2Temperature(pco2Temperature.getText());
        co2.setGasConcentration(gasConcentration.getText());
        co2.setDryingMethod(dryingMethod.getText());
        co2.setEquilibratorType(equilibratorType.getText());
        co2.setEquilibratorVolume(equilibratorVolume.getText());
        co2.setGasFlowRate(gasFlowRate.getText());
        co2.setEquilibratorPressureMeasureMethod(equilibratorPressureMeasureMethod.getText());
        co2.setPressureMeasurementCalibrationMethod(equilibratorPressureSensorCalibrationMethod.getText());
        co2.setEquilibratorTemperatureMeasureMethod(equilibratorTemperatureMeasureMethod.getText());
        co2.setTemperatureMeasurementCalibrationMethod(equilibratorTemperatureSensorCalibrationMethod.getText());
        co2.setUncertaintyOfTemperature(equilibratorTemperatureMeasureUncertainty.getText());
        co2.setStandardGasManufacture(standardGasManufacture.getText());
        co2.setTraceabilityOfStdGas(stdGasTraceability.getText());
        co2.setGasDetectorManufacture(gasDetectorManufacture.getText());
        co2.setGasDetectorModel(gasDetectorModel.getText());
        co2.setGasDectectorResolution(gasDectectorResolution.getText());
        co2.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
        co2.setStandardGasUncertainties(standardGasUncertainties.getText());
        co2.setGasDectectorUncertainty(gasDectectorUncertainty.getText());
        co2.setVented(vented.getText());
        co2.setFlowRate(flowRate.getText());
        co2.setVaporCorrection(vaporCorrection.getText());
        return co2;
    }
    public void fill(Variable pco2a) {
        common.fillCommonVariable(pco2a);
        pco2a.setStandardizationTechnique(standardizationTechnique.getText());
        pco2a.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        pco2a.setPco2Temperature(pco2Temperature.getText());
        pco2a.setGasConcentration(gasConcentration.getText());
//        pco2a.setIntakeDepth(intakeDepth.getText());
        pco2a.setDryingMethod(dryingMethod.getText());
        pco2a.setEquilibratorType(equilibratorType.getText());
        pco2a.setEquilibratorVolume(equilibratorVolume.getText());
        pco2a.setGasFlowRate(gasFlowRate.getText());
        pco2a.setEquilibratorPressureMeasureMethod(equilibratorPressureMeasureMethod.getText());
        pco2a.setEquilibratorTemperatureMeasureMethod(equilibratorTemperatureMeasureMethod.getText());
        pco2a.setUncertaintyOfTemperature(equilibratorTemperatureMeasureUncertainty.getText());
//        pco2a.setIntakeLocation(intakeLocation.getText());
        pco2a.setStandardGasManufacture(standardGasManufacture.getText());
        pco2a.setGasDetectorManufacture(gasDetectorManufacture.getText());
        pco2a.setGasDetectorModel(gasDetectorModel.getText());
        pco2a.setGasDectectorResolution(gasDectectorResolution.getText());
        pco2a.setTemperatureCorrectionMethod(temperatureCorrectionMethod.getText());
        pco2a.setStandardGasUncertainties(standardGasUncertainties.getText());
        pco2a.setGasDectectorUncertainty(gasDectectorUncertainty.getText());
        pco2a.setVented(vented.getText());
        pco2a.setFlowRate(flowRate.getText());
        pco2a.setVaporCorrection(vaporCorrection.getText());
    }
    public void show(Variable co2common) {
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

        if ( co2common.getGasConcentration() != null ) {
            gasConcentration.setText(co2common.getGasConcentration());
        }

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

        if ( co2common.getStandardGasManufacture() != null ) {
            standardGasManufacture.setText(co2common.getStandardGasManufacture());
        }

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

        if ( co2common.getStandardGasUncertainties() != null ) {
            standardGasUncertainties.setText(co2common.getStandardGasUncertainties());
        }

        if ( co2common.getGasDectectorUncertainty() != null ) {
            gasDectectorUncertainty.setText(co2common.getGasDectectorUncertainty());
        }

        if ( co2common.getVented() != null ) {
            vented.setText(co2common.getVented());
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
        if ( co2common.getTraceabilityOfStdGas() != null ) {
            stdGasTraceability.setText(co2common.getTraceabilityOfStdGas());
        }
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
    public boolean isDirty(Variable original) {
        boolean isDirty =
            original == null ?
            isDirty() :
            common.isDirty(original) ||
            isDirty(standardizationTechnique, original.getStandardizationTechnique() ) ||
            isDirty(freqencyOfStandardization, original.getFreqencyOfStandardization() ) ||
            isDirty(pco2Temperature, original.getPco2Temperature() ) ||
            isDirty(gasConcentration, original.getGasConcentration() ) ||
//            isDirty(intakeDepth, original.getIntakeDepth() ) ||
            isDirty(dryingMethod, original.getDryingMethod() ) ||
            isDirty(equilibratorType, original.getEquilibratorType() ) ||
            isDirty(equilibratorVolume, original.getEquilibratorVolume() ) ||
            isDirty(gasFlowRate, original.getGasFlowRate() ) ||
            isDirty(equilibratorPressureMeasureMethod, original.getEquilibratorPressureMeasureMethod() ) ||
            isDirty(equilibratorTemperatureMeasureMethod, original.getEquilibratorTemperatureMeasureMethod() ) ||
//            isDirty(intakeLocation, original.getIntakeLocation() ) ||
            isDirty(standardGasManufacture, original.getStandardGasManufacture() ) ||
            isDirty(gasDetectorManufacture, original.getGasDetectorManufacture() ) ||
            isDirty(gasDetectorModel, original.getGasDetectorModel() ) ||
            isDirty(gasDectectorResolution, original.getGasDectectorResolution() ) ||
            isDirty(temperatureCorrectionMethod, original.getTemperatureCorrectionMethod() ) ||
            isDirty(standardGasUncertainties, original.getStandardGasUncertainties() ) ||
            isDirty(gasDectectorUncertainty, original.getGasDectectorUncertainty() ) ||
            isDirty(vented, original.getVented() ) ||
            isDirty(flowRate, original.getFlowRate() ) ||
            isDirty(vaporCorrection, original.getVaporCorrection() );
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
        if (pco2Temperature.getText().trim() != null && !pco2Temperature.getText().isEmpty() ) {
            return true;
        }
        if (gasConcentration.getText().trim() != null && !gasConcentration.getText().isEmpty() ) {
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
        if (standardGasManufacture.getText().trim() != null && !standardGasManufacture.getText().isEmpty() ) {
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
        if (standardGasUncertainties.getText().trim() != null && !standardGasUncertainties.getText().isEmpty() ) {
            return true;
        }
        if (gasDectectorUncertainty.getText().trim() != null && !gasDectectorUncertainty.getText().isEmpty() ) {
            return true;
        }
        if (vented.getText().trim() != null && !vented.getText().isEmpty() ) {
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
        common.reset();
        clearVariables();
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
    private <C> Column<Variable, C> addColumn(Cell<C> cell, String headerText,
                                              final GetValue<C> getter, FieldUpdater<Variable, C> fieldUpdater) {
        Column<Variable, C> column = new Column<Variable, C>(cell) {
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
