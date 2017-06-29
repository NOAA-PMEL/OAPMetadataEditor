package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhs on 3/8/17.
 */
public class VariablePanel extends Composite {
/*
 *

Private variable index number
<internal
Don't know which of several that these belongs to
<correction
<description
<model


*/
    @UiField
    Heading heading;

    // 001 Variable abbreviation in data files
    // <abbrev>
    @UiField
    TextBox abbreviation;

    // 002 Observation type
    // <observationType>
    @UiField
    TextBox observationType;

    // 003 Manipulation method
    // <manipulationMethod>
    @UiField
    TextBox manipulationMethod;

    // 004 In-situ observation / manipulation condition / response variable
    // <insitu>
    @UiField
    ButtonDropDown observationDetail;

    // 005 Variable unit
    // <unit>
    @UiField
    TextBox units;

    // 006 Measured or calculated
    // <measured>
    @UiField
    ButtonDropDown measured;

    // 007 Calculation method and parameters
    // <calcMethod>
    @UiField
    TextBox calculationMethod;

    // 008 Sampling instrument
    // <samplingInstrument>
    @UiField
    ButtonDropDown samplingInstrument;

    // 009 Analyzing instrument
    // <analyzingInstrument>
    @UiField
    ButtonDropDown analyzingInstrument;

    // 010 Detailed sampling and analyzing information
    // <detailedInfo>
    @UiField
    TextArea detailedInformation;

    // 011 Field replicate information
    // <replicate>
    @UiField
    TextBox fieldReplicate;

    // 012 Standardization technique description
    // <standardization><description>
    @UiField
    TextBox standardizationTechnique;

    // 013 Frequency of standardization
    // <standardization><frequency>
    @UiField
    TextBox freqencyOfStandardization;

    // 014 CRM manufacturer
    // <crm><manufacturer>
    @UiField
    TextBox crmManufacture;

    // 015 Batch Number
    // <crm><batch>
    @UiField
    TextBox batchNumber;

    // 016 Storage method
    // <storageMethod>
    @UiField
    TextBox storageMethod;

    // 017 Poison used to kill the sample
    // <poison><poisonName>
    @UiField
    TextBox poison;

    // 018 Poison volume
    @UiField
    TextBox poisonVolume;

    // 019 Poisoning correction description
    @UiField
    TextBox poisonDescription;

    // 020 Uncertainty
    // <standardization><standardgas><uncertainty>
    @UiField
    TextBox uncertainty;

    // 021 Data quality flag description
    // <flag>
    @UiField
    TextBox qualityFlag;

    // 022 Researcher Name
    // <researcherName>
    @UiField
    TextBox researcherName;

    // 023 Researcher Institution
    // <researcherInstitution>
    @UiField
    TextBox researcherInstitution;

    // 024 at what temperature was pCO2 reported
    // <co2ReportTemperature>
    @UiField
    TextBox pCO2temperature;

    // 025 at what temperature was pH reported
    // <phReportTemperature>
    @UiField
    TextBox pHtemperature;

    // 026 Biological subject
    // <biologicalSubject>
    @UiField
    TextBox biologicalSubject;

    // 027 Cell type (open or closed)
    // <cellType>
    @UiField
    ButtonDropDown cellType;

    // 028 Concentrations of standard gas
    // <standardization><standardgas><concentration>
    @UiField
    TextBox gasConcentration;

    // 029 Curve fitting method
    // <curveFitting>
    @UiField
    TextBox curveFittingMethod;

    // 030 Depth of seawater intake
    // <DepthSeawaterIntake>
    @UiField
    TextBox intakeDepth;

    // 031 Drying method for CO2 gas
    // <dryMethod>
    @UiField
    TextBox dryingMethod;

    // 032 Duration (for settlement/colonization methods)
    // <duration>
    @UiField
    TextBox duration;

    // 033 Equilibrator type
    // <equilibrator><type>
    @UiField
    TextBox equilibratorType;

    // 034 Equilibrator volume (L)
    // <equilibrator><volume>
    @UiField
    TextBox equilibratorVolume;

    // 035 Full variable name
    // <fullname>
    @UiField
    TextBox fullVariableName;

    // 036 Headspace gas flow rate (L/min)
    // <gasFlowRate>
    @UiField
    TextBox gasFlowRate;

    // 037 Headspace volume (mL)
    // <headspacevol>
    @UiField
    TextBox headspaceVolume;

    // 038 How was pressure inside the equilibrator measured.
    // <equilibrator><pressureEquilibratorMethod>
    @UiField
    TextBox equilibratorPressureMeasureMethod;

    // 039 How was temperature inside the equilibrator measured .
    // <equilibrator><temperatureEquilibratorMethod>
    @UiField
    TextBox equilibratorTemperatureMeasureMethod;

    // 040 Life stage of the biological subject
    // <lifeStage>
    @UiField
    TextBox lifeStage;

    // 041 Location of seawater intake
    // <locationSeawaterIntake>
    @UiField
    TextBox intakeLocation;

    // 042 Magnitude of blank correction
    // <blank>
    @UiField
    TextBox magnitudeOfBlankCorrection;

    // 043 Manufacturer of standard gas
    // <standardization><standardgas><manufacture>
    @UiField
    TextBox standardGasManufacture;

    // 044 Manufacturer of the gas detector
    // <gasDetector> ???
    @UiField
    TextBox gasDetectorManufacture;

    // 045 Method reference (citation)
    // <methodReference>
    @UiField
    TextBox referenceMethod;

    // 046 Model of the gas detector
    @UiField
    TextBox gasDetectorModel;

    // 047 pH scale
    // <phscale>
    @UiField
    TextBox pHscale;

    // 048 pH values of the standards
    @UiField
    TextBox pHstandards;

    // 049 Resolution of the gas detector
    // <resolution>
    @UiField
    TextBox gasDectectorResolution;

    // 050 Seawater volume (mL)
    // <seawatervol>
    @UiField
    TextBox seawaterVolume;

    // 051 Species Identification code
    // <speciesID>
    @UiField
    TextBox speciesIdCode;

    // 052 Temperature correction method
    // ???
    // <temperatureCorrectionMethod>
    // <temperatureCorrection>
    @UiField
    TextBox temperatureCorrectionMethod;

    // 053 Temperature of measurement
    // <temperatureMeasure>
    @UiField
    TextBox temperatureMeasurement;

    // 054 Temperature of standardization
    // <temperatureStandardization>
    // <temperatureStd
    @UiField
    TextBox temperatureStandarization;

    // 055 Type of titration
    // <titrationType>
    @UiField
    TextBox titrationType;

    // 056 Uncertainties of standard gas
    // <standard
    @UiField
    TextBox standardGasUncertainties;

    // 057 Uncertainty of the gas detector
    @UiField
    TextBox gasDectectorUncertainty;

    // 058 Vented or not
    // <equilibrator><vented>
    @UiField
    TextBox vented;

    // 059 Water flow rate (L/min)
    // <equilabrator><waterFlowRate>
    @UiField
    TextBox flowRate;

    // 060 Water vapor correction method
    // <waterVaporCorrection>
    @UiField
    TextBox vaporCorrection;


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

    // 012 Standardization technique description
    @UiField
    FormGroup standardizationTechniqueForm;

    // 013 Frequency of standardization
    @UiField
    FormGroup freqencyOfStandardizationForm;

    // 014 CRM manufacturer
    // <crm>
    @UiField
    FormGroup crmManufactureForm;

    // 015 Batch Number
    @UiField
    FormGroup batchNumberForm;

    // 016 Storage method
    @UiField
    FormGroup storageMethodForm;

    // 017 Poison used to kill the sample
    @UiField
    FormGroup poisonForm;

    // 018 Poison volume
    @UiField
    FormGroup poisonVolumeForm;

    // 019 Poisoning correction description
    @UiField
    FormGroup poisonDescriptionForm;

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

    // 024 at what temperature was pCO2 reported
    @UiField
    FormGroup pCO2temperatureForm;

    // 025 at what temperature was pH reported
    @UiField
    FormGroup pHtemperatureForm;

    // 026 Biological subject
    @UiField
    FormGroup biologicalSubjectForm;

    // 027 Cell type (open or closed)
    @UiField
    FormGroup cellTypeForm;

    // 028 Concentrations of standard gas
    @UiField
    FormGroup gasConcentrationForm;

    // 029 Curve fitting method
    @UiField
    FormGroup curveFittingMethodForm;

    // 030 Depth of seawater intake
    @UiField
    FormGroup intakeDepthForm;

    // 031 Drying method for CO2 gas
    @UiField
    FormGroup dryingMethodForm;

    // 032 Duration (for settlement/colonization methods)
    @UiField
    FormGroup durationForm;

    // 033 Equilibrator type
    @UiField
    FormGroup equilibratorTypeForm;

    // 034 Equilibrator volume (L)
    @UiField
    FormGroup equilibratorVolumeForm;

    // 035 Full variable name
    @UiField
    FormGroup fullVariableNameForm;

    // 036 Headspace gas flow rate (L/min)
    @UiField
    FormGroup gasFlowRateForm;

    // 037 Headspace volume (mL)
    @UiField
    FormGroup headspaceVolumeForm;

    // 038 How was pressure inside the equilibrator measured.
    @UiField
    FormGroup equilibratorPressureMeasureMethodForm;

    // 039 How was temperature inside the equilibrator measured .
    @UiField
    FormGroup equilibratorTemperatureMeasureMethodForm;

    // 040 Life stage of the biological subject
    @UiField
    FormGroup lifeStageForm;

    // 041 Location of seawater intake
    @UiField
    FormGroup intakeLocationForm;

    // 042 Magnitude of blank correction
    @UiField
    FormGroup magnitudeOfBlankCorrectionForm;

    // 043 Manufacturer of standard gas
    @UiField
    FormGroup standardGasManufactureForm;

    // 044 Manufacturer of the gas detector
    @UiField
    FormGroup gasDetectorManufactureForm;

    // 045 Method reference (citation)
    @UiField
    FormGroup referenceMethodForm;

    // 046 Model of the gas detector
    @UiField
    FormGroup gasDetectorModelForm;

    // 047 pH scale
    @UiField
    FormGroup pHscaleForm;

    // 048 pH values of the standards
    @UiField
    FormGroup pHstandardsForm;

    // 049 Resolution of the gas detector
    @UiField
    FormGroup gasDectectorResolutionForm;

    // 050 Seawater volume (mL)
    @UiField
    FormGroup seawaterVolumeForm;

    // 051 Species Identification code
    @UiField
    FormGroup speciesIdCodeForm;

    // 052 Temperature correction method
    @UiField
    FormGroup temperatureCorrectionMethodForm;

    // 053 Temperature of measurement
    @UiField
    FormGroup temperatureMeasurementForm;

    // 054 Temperature of standardization
    @UiField
    FormGroup temperatureStandarizationForm;

    // 055 Type of titration
    @UiField
    FormGroup titrationTypeForm;

    // 056 Uncertainties of standard gas
    @UiField
    FormGroup standardGasUncertaintiesForm;

    // 057 Uncertainty of the gas detector
    @UiField
    FormGroup gasDectectorUncertaintyForm;

    // 058 Vented or not
    @UiField
    FormGroup ventedForm;

    // 059 Water flow rate (L/min)
    @UiField
    FormGroup flowRateForm;

    // 060 Water vapor correction method
    @UiField
    FormGroup vaporCorrectionForm;


//TODO initialize the cell type dropdown.

    interface VariablePanelUiBinder extends UiBinder<HTMLPanel, VariablePanel> {
    }

    private static VariablePanelUiBinder ourUiBinder = GWT.create(VariablePanelUiBinder.class);

    public VariablePanel() {
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



    }
    public void show(Variable variable) {
        if ( variable.getAbbreviation() != null )
            abbreviation.setText(variable.getAbbreviation());
        if ( variable.getObservationDetail() != null )
            observationDetail.setSelected(variable.getObservationDetail());
        if ( variable.getManipulationMethod() != null )
            manipulationMethod.setText(variable.getManipulationMethod());
        if ( variable.getObservationType() != null ) {
            observationType.setText(variable.getObservationType());
        }
    }

    public ButtonDropDown getSamplingInstrument() {
        return samplingInstrument;
    }
    public ButtonDropDown getAnalyzingInstrument() {
        return analyzingInstrument;
    }



}
