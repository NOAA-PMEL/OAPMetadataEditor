package gov.noaa.pmel.sdig.shared.bean;

import gov.noaa.pmel.sdig.shared.HasContent;
import gov.noaa.pmel.sdig.shared.Stringy;

/**
 * Created by rhs on 3/8/17.
 */
public class Variable extends Ordered implements Comparable<Variable>, Cloneable, Stringy, HasContent {

    String abbreviation;
    String manipulationMethod;
    String observationType;
    String observationDetail;
    String units;
    String measured;
    String calculationMethod;
    String samplingInstrument;
    String analyzingInstrument;
    String detailedInformation;
    String fieldReplicate;
    String uncertainty;
    String qualityFlag;
    String researcherName;
    String researcherInstitution;
    String fullVariableName;
    String referenceMethod;
    String standardizationTechnique;
    String crmManufacture;
    String batchNumber;
    String poison;
    String poisonVolume;
    String poisonDescription;
    String cellType;
    String curveFittingMethod;
    String magnitudeOfBlankCorrection;
    String phTemperature;
    String phScale;
    String phStandards;
    String titrationType;
    String intakeDepth;
    String dryingMethod;
    String equilibratorType;
    String equilibratorVolume;
    String gasFlowRate;
    String equilibratorPressureMeasureMethod;
    String equilibratorTemperatureMeasureMethod;
    String intakeLocation;
    String flowRate;
    String freqencyOfStandardization;
    String storageMethod;
    String pco2Temperature;
    String gasConcentration;
    String headspaceVolume;
    String standardGasManufacture;
    String gasDetectorManufacture;
    String gasDetectorModel;
    String gasDectectorResolution;
    String seawaterVolume;
    String temperatureCorrectionMethod;
    String temperatureMeasurement;
    String temperatureStandarization;
    String standardGasUncertainties;
    String gasDectectorUncertainty;
    String vaporCorrection;
    String vented;
    String biologicalSubject;
    String duration;
    String lifeStage;
    String speciesIdCode;
    String variableType;

    String qualityControl;
    String abbreviationQualityFlag;
    String sopChanges;
    String collectionMethod;
    String analyzingInformation;
    String phDyeTypeManuf;

    public Variable clone() {
        Variable newV = new Variable(
            this.abbreviation,
            this.manipulationMethod,
            this.observationType,
            this.observationDetail,
            this.units,
            this.measured,
            this.calculationMethod,
            this.samplingInstrument,
            this.analyzingInstrument,
            this.detailedInformation,
            this.fieldReplicate,
            this.uncertainty,
            this.qualityFlag,
            this.researcherName,
            this.researcherInstitution,
            this.fullVariableName,
            this.referenceMethod,
            this.standardizationTechnique,
            this.crmManufacture,
            this.batchNumber,
            this.poison,
            this.poisonVolume,
            this.poisonDescription,
            this.cellType,
            this.curveFittingMethod,
            this.magnitudeOfBlankCorrection,
            this.phTemperature,
            this.phScale,
            this.phStandards,
            this.titrationType,
            this.intakeDepth,
            this.dryingMethod,
            this.equilibratorType,
            this.equilibratorVolume,
            this.gasFlowRate,
            this.equilibratorPressureMeasureMethod,
            this.equilibratorTemperatureMeasureMethod,
            this.intakeLocation,
            this.flowRate,
            this.freqencyOfStandardization,
            this.storageMethod,
            this.pco2Temperature,
            this.gasConcentration,
            this.headspaceVolume,
            this.standardGasManufacture,
            this.gasDetectorManufacture,
            this.gasDetectorModel,
            this.gasDectectorResolution,
            this.seawaterVolume,
            this.temperatureCorrectionMethod,
            this.temperatureMeasurement,
            this.temperatureStandarization,
            this.standardGasUncertainties,
            this.gasDectectorUncertainty,
            this.vaporCorrection,
            this.vented,
            this.biologicalSubject,
            this.duration,
            this.lifeStage,
            this.speciesIdCode,
            this.qualityControl,
            this.abbreviationQualityFlag,
            this.sopChanges,
            this.collectionMethod,
            this.analyzingInformation,
            this.phDyeTypeManuf,
            this.variableType
        );
        return newV;
    }
    public Variable() { super(); }
    Variable(
            String abbreviation,
            String manipulationMethod,
            String observationType,
            String observationDetail,
            String units,
            String measured,
            String calculationMethod,
            String samplingInstrument,
            String analyzingInstrument,
            String detailedInformation,
            String fieldReplicate,
            String uncertainty,
            String qualityFlag,
            String researcherName,
            String researcherInstitution,
            String fullVariableName,
            String referenceMethod,
            String standardizationTechnique,
            String crmManufacture,
            String batchNumber,
            String poison,
            String poisonVolume,
            String poisonDescription,
            String cellType,
            String curveFittingMethod,
            String magnitudeOfBlankCorrection,
            String phTemperature,
            String phScale,
            String phStandards,
            String titrationType,
            String intakeDepth,
            String dryingMethod,
            String equilibratorType,
            String equilibratorVolume,
            String gasFlowRate,
            String equilibratorPressureMeasureMethod,
            String equilibratorTemperatureMeasureMethod,
            String intakeLocation,
            String flowRate,
            String freqencyOfStandardization,
            String storageMethod,
            String pco2Temperature,
            String gasConcentration,
            String headspaceVolume,
            String standardGasManufacture,
            String gasDetectorManufacture,
            String gasDetectorModel,
            String gasDectectorResolution,
            String seawaterVolume,
            String temperatureCorrectionMethod,
            String temperatureMeasurement,
            String temperatureStandarization,
            String standardGasUncertainties,
            String gasDectectorUncertainty,
            String vaporCorrection,
            String vented,
            String biologicalSubject,
            String duration,
            String lifeStage,
            String speciesIdCode,
            String qualityControl,
            String abbreviationQualityFlag,
            String sopChanges,
            String collectionMethod,
            String phDyeTypeManuf,
            String analyzingInformation,
            String variableType
    ) {
        this.abbreviation = abbreviation;
        this.manipulationMethod = manipulationMethod;
        this.observationType = observationType;
        this.observationDetail = observationDetail;
        this.units = units;
        this.measured = measured;
        this.calculationMethod = calculationMethod;
        this.samplingInstrument = samplingInstrument;
        this.analyzingInstrument = analyzingInstrument;
        this.detailedInformation = detailedInformation;
        this.fieldReplicate = fieldReplicate;
        this.uncertainty = uncertainty;
        this.qualityFlag = qualityFlag;
        this.researcherName = researcherName;
        this.researcherInstitution = researcherInstitution;
        this.fullVariableName = fullVariableName;
        this.referenceMethod = referenceMethod;
        this.standardizationTechnique = standardizationTechnique;
        this.crmManufacture = crmManufacture;
        this.batchNumber = batchNumber;
        this.poison = poison;
        this.poisonVolume = poisonVolume;
        this.poisonDescription = poisonDescription;
        this.cellType = cellType;
        this.curveFittingMethod = curveFittingMethod;
        this.magnitudeOfBlankCorrection = magnitudeOfBlankCorrection;
        this.phTemperature = phTemperature;
        this.phScale = phScale;
        this.phStandards = phStandards;
        this.titrationType = titrationType;
        this.intakeDepth = intakeDepth;
        this.dryingMethod = dryingMethod;
        this.equilibratorType = equilibratorType;
        this.equilibratorVolume = equilibratorVolume;
        this.gasFlowRate = gasFlowRate;
        this.equilibratorPressureMeasureMethod = equilibratorPressureMeasureMethod;
        this.equilibratorTemperatureMeasureMethod = equilibratorTemperatureMeasureMethod;
        this.intakeLocation = intakeLocation;
        this.flowRate = flowRate;
        this.freqencyOfStandardization = freqencyOfStandardization;
        this.storageMethod = storageMethod;
        this.pco2Temperature = pco2Temperature;
        this.gasConcentration = gasConcentration;
        this.headspaceVolume = headspaceVolume;
        this.standardGasManufacture = standardGasManufacture;
        this.gasDetectorManufacture = gasDetectorManufacture;
        this.gasDetectorModel = gasDetectorModel;
        this.gasDectectorResolution = gasDectectorResolution;
        this.seawaterVolume = seawaterVolume;
        this.temperatureCorrectionMethod = temperatureCorrectionMethod;
        this.temperatureMeasurement = temperatureMeasurement;
        this.temperatureStandarization = temperatureStandarization;
        this.standardGasUncertainties = standardGasUncertainties;
        this.gasDectectorUncertainty = gasDectectorUncertainty;
        this.vaporCorrection = vaporCorrection;
        this.vented = vented;
        this.biologicalSubject = biologicalSubject;
        this.duration = duration;
        this.lifeStage = lifeStage;
        this.speciesIdCode = speciesIdCode;
        this.qualityControl = qualityControl;
        this.abbreviationQualityFlag = abbreviationQualityFlag;
        this.sopChanges = sopChanges;
        this.collectionMethod=collectionMethod;
        this.phDyeTypeManuf = phDyeTypeManuf;
        this.analyzingInformation = analyzingInformation;
        this.variableType = variableType;
    }
    public String getVariableType() { return variableType; }
    public void setVariableType(String variableType) { this.variableType = variableType; }

    public String getCalculationMethod() {
        return calculationMethod;
    }
    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public String getBiologicalSubject() {
        return biologicalSubject;
    }
    public void setBiologicalSubject(String biologicalSubject) {
        this.biologicalSubject = biologicalSubject;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLifeStage() {
        return lifeStage;
    }
    public void setLifeStage(String lifeStage) {
        this.lifeStage = lifeStage;
    }

    public String getSpeciesIdCode() {
        return speciesIdCode;
    }
    public void setSpeciesIdCode(String speciesIdCode) {
        this.speciesIdCode = speciesIdCode;
    }

    public String getStorageMethod() {
        return storageMethod;
    }
    public void setStorageMethod(String storageMethod) {
        this.storageMethod = storageMethod;
    }

    public String getHeadspaceVolume() {
        return headspaceVolume;
    }
    public void setHeadspaceVolume(String headspaceVolume) {
        this.headspaceVolume = headspaceVolume;
    }

    public String getSeawaterVolume() {
        return seawaterVolume;
    }
    public void setSeawaterVolume(String seawaterVolume) {
        this.seawaterVolume = seawaterVolume;
    }

    public String getPco2Temperature() {
        return pco2Temperature;
    }
    public void setPco2Temperature(String pco2Temperature) {
        this.pco2Temperature = pco2Temperature;
    }

    public String getGasConcentration() {
        return gasConcentration;
    }
    public void setGasConcentration(String gasConcentration) { this.gasConcentration = gasConcentration; }

    public String getIntakeDepth() {
        return intakeDepth;
    }
    public void setIntakeDepth(String intakeDepth) {
        this.intakeDepth = intakeDepth;
    }

    public String getDryingMethod() {
        return dryingMethod;
    }
    public void setDryingMethod(String dryingMethod) {
        this.dryingMethod = dryingMethod;
    }

    public String getEquilibratorType() {
        return equilibratorType;
    }
    public void setEquilibratorType(String equilibratorType) {
        this.equilibratorType = equilibratorType;
    }

    public String getEquilibratorVolume() {
        return equilibratorVolume;
    }
    public void setEquilibratorVolume(String equilibratorVolume) {
        this.equilibratorVolume = equilibratorVolume;
    }

    public String getGasFlowRate() {
        return gasFlowRate;
    }
    public void setGasFlowRate(String gasFlowRate) {
        this.gasFlowRate = gasFlowRate;
    }

    public String getEquilibratorPressureMeasureMethod() {
        return equilibratorPressureMeasureMethod;
    }
    public void setEquilibratorPressureMeasureMethod(String equilibratorPressureMeasureMethod) {
        this.equilibratorPressureMeasureMethod = equilibratorPressureMeasureMethod;
    }

    public String getEquilibratorTemperatureMeasureMethod() {
        return equilibratorTemperatureMeasureMethod;
    }
    public void setEquilibratorTemperatureMeasureMethod(String equilibratorTemperatureMeasureMethod) {
        this.equilibratorTemperatureMeasureMethod = equilibratorTemperatureMeasureMethod;
    }

    public String getIntakeLocation() {
        return intakeLocation;
    }
    public void setIntakeLocation(String intakeLocation) {
        this.intakeLocation = intakeLocation;
    }

    public String getStandardGasManufacture() {
        return standardGasManufacture;
    }
    public void setStandardGasManufacture(String standardGasManufacture) {
        this.standardGasManufacture = standardGasManufacture;
    }

    public String getGasDetectorManufacture() {
        return gasDetectorManufacture;
    }
    public void setGasDetectorManufacture(String gasDetectorManufacture) {
        this.gasDetectorManufacture = gasDetectorManufacture;
    }

    public String getGasDetectorModel() {
        return gasDetectorModel;
    }
    public void setGasDetectorModel(String gasDetectorModel) {
        this.gasDetectorModel = gasDetectorModel;
    }

    public String getGasDectectorResolution() {
        return gasDectectorResolution;
    }
    public void setGasDectectorResolution(String gasDectectorResolution) {
        this.gasDectectorResolution = gasDectectorResolution;
    }

    public String getStandardGasUncertainties() {
        return standardGasUncertainties;
    }


    public String getQualityControl() {
        return qualityControl;
    }

    public void setQualityControl(String qualityControl) {
        this.qualityControl = qualityControl;
    }

    public String getAbbreviationQualityFlag() {
        return abbreviationQualityFlag;
    }

    public void setAbbreviationQualityFlag(String abbreviationQualityFlag) {
        this.abbreviationQualityFlag = abbreviationQualityFlag;
    }

    public String getSopChanges() {
        return sopChanges;
    }

    public void setSopChanges(String sopChanges) {
        this.sopChanges = sopChanges;
    }

    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    public String getAnalyzingInformation() {
        return analyzingInformation;
    }

    public void setAnalyzingInformation(String analyzingInformation) {
        this.analyzingInformation = analyzingInformation;
    }

    public String getPhDyeTypeManuf() {
        return phDyeTypeManuf;
    }

    public void setPhDyeTypeManuf(String phDyeTypeManuf) {
        this.phDyeTypeManuf = phDyeTypeManuf;
    }

    public void setStandardGasUncertainties(String standardGasUncertainties) {
        this.standardGasUncertainties = standardGasUncertainties;
    }

    public String getGasDectectorUncertainty() {
        return gasDectectorUncertainty;
    }
    public void setGasDectectorUncertainty(String gasDectectorUncertainty) {
        this.gasDectectorUncertainty = gasDectectorUncertainty;
    }

    public String getVented() {
        return vented;
    }
    public void setVented(String vented) {
        this.vented = vented;
    }

    public String getFlowRate() {
        return flowRate;
    }
    public void setFlowRate(String flowRate) {
        this.flowRate = flowRate;
    }

    public String getVaporCorrection() {
        return vaporCorrection;
    }
    public void setVaporCorrection(String vaporCorrection) {
        this.vaporCorrection = vaporCorrection;
    }

    public String getPhTemperature() {
        return phTemperature;
    }
    public void setPhTemperature(String pHtemperature) {
        this.phTemperature = pHtemperature;
    }

    public String getPhScale() {
        return phScale;
    }
    public void setPhScale(String pHscale) {
        this.phScale = pHscale;
    }

    public String getPhStandards() {
        return phStandards;
    }
    public void setPhStandards(String pHstandards) {
        this.phStandards = pHstandards;
    }

    public String getTemperatureCorrectionMethod() {
        return temperatureCorrectionMethod;
    }
    public void setTemperatureCorrectionMethod(String temperatureCorrectionMethod) {
        this.temperatureCorrectionMethod = temperatureCorrectionMethod;
    }

    public String getTemperatureMeasurement() {
        return temperatureMeasurement;
    }
    public void setTemperatureMeasurement(String temperatureMeasurement) {
        this.temperatureMeasurement = temperatureMeasurement;
    }

    public String getTemperatureStandarization() {
        return temperatureStandarization;
    }
    public void setTemperatureStandarization(String temperatureStandarization) {
        this.temperatureStandarization = temperatureStandarization;
    }

    public String getCurveFittingMethod() {
        return curveFittingMethod;
    }
    public void setCurveFittingMethod(String curveFittingMethod) {
        this.curveFittingMethod = curveFittingMethod;
    }

    public String getMagnitudeOfBlankCorrection() {
        return magnitudeOfBlankCorrection;
    }
    public void setMagnitudeOfBlankCorrection(String magnitudeOfBlankCorrection) {
        this.magnitudeOfBlankCorrection = magnitudeOfBlankCorrection;
    }

    public String getTitrationType() {
        return titrationType;
    }
    public void setTitrationType(String titrationType) {
        this.titrationType = titrationType;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getManipulationMethod() {
        return manipulationMethod;
    }
    public void setManipulationMethod(String manipulationMethod) {
        this.manipulationMethod = manipulationMethod;
    }

    public String getObservationType() {
        return observationType;
    }
    public void setObservationType(String observationType) {
        this.observationType = observationType;
    }

    public String getObservationDetail() {
        return observationDetail;
    }
    public void setObservationDetail(String observationDetail) {
        this.observationDetail = observationDetail;
    }

    public String getUnits() {
        return units;
    }
    public void setUnits(String units) {
        this.units = units;
    }

    public String getMeasured() {
        return measured;
    }
    public void setMeasured(String measured) {
        this.measured = measured;
    }

    public String getSamplingInstrument() {
        return samplingInstrument;
    }
    public void setSamplingInstrument(String samplingInstrument) {
        this.samplingInstrument = samplingInstrument;
    }

    public String getAnalyzingInstrument() {
        return analyzingInstrument;
    }
    public void setAnalyzingInstrument(String analyzingInstrument) {
        this.analyzingInstrument = analyzingInstrument;
    }

    public String getDetailedInformation() {
        return detailedInformation;
    }
    public void setDetailedInformation(String detailedInformation) {
        this.detailedInformation = detailedInformation;
    }

    public String getFieldReplicate() {
        return fieldReplicate;
    }
    public void setFieldReplicate(String fieldReplicate) {
        this.fieldReplicate = fieldReplicate;
    }

    public String getUncertainty() {
        return uncertainty;
    }
    public void setUncertainty(String uncertainty) {
        this.uncertainty = uncertainty;
    }

    public String getQualityFlag() {
        return qualityFlag;
    }
    public void setQualityFlag(String qualityFlag) {
        this.qualityFlag = qualityFlag;
    }

    public String getResearcherName() {
        return researcherName;
    }
    public void setResearcherName(String researcherName) {
        this.researcherName = researcherName;
    }

    public String getResearcherInstitution() {
        return researcherInstitution;
    }
    public void setResearcherInstitution(String researcherInstitution) {
        this.researcherInstitution = researcherInstitution;
    }

    public String getFullVariableName() {
        return fullVariableName;
    }
    public void setFullVariableName(String fullVariableName) {
        this.fullVariableName = fullVariableName;
    }

    public String getReferenceMethod() {
        return referenceMethod;
    }
    public void setReferenceMethod(String referenceMethod) {
        this.referenceMethod = referenceMethod;
    }

    public String getStandardizationTechnique() {
        return standardizationTechnique;
    }
    public void setStandardizationTechnique(String standardizationTechnique) {
        this.standardizationTechnique = standardizationTechnique;
    }

    public String getFreqencyOfStandardization() {
        return freqencyOfStandardization;
    }
    public void setFreqencyOfStandardization(String freqencyOfStandardization) {
        this.freqencyOfStandardization = freqencyOfStandardization;
    }

    public String getCrmManufacture() {
        return crmManufacture;
    }
    public void setCrmManufacture(String crmManufacture) {
        this.crmManufacture = crmManufacture;
    }

    public String getBatchNumber() {
        return batchNumber;
    }
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getPoison() {
        return poison;
    }
    public void setPoison(String poison) {
        this.poison = poison;
    }

    public String getPoisonVolume() {
        return poisonVolume;
    }
    public void setPoisonVolume(String poisonVolume) {
        this.poisonVolume = poisonVolume;
    }

    public String getPoisonDescription() {
        return poisonDescription;
    }
    public void setPoisonDescription(String poisonDescription) {
        this.poisonDescription = poisonDescription;
    }

    public String getCellType() {
        return cellType;
    }
    public void setCellType(String cellType) {
        this.cellType = cellType;
    }

    @Override
    public int compareTo(Variable v) {
        int z = sCompare(abbreviation, v.abbreviation);
        if (z != 0) {
            return z;
        }
        return sCompare(fullVariableName, v.fullVariableName);
    }

    @Override
    public int hashCode() {
        int result = abbreviation != null ? abbreviation.hashCode() : 0;
        result = 31 * result + (manipulationMethod != null ? manipulationMethod.hashCode() : 0);
        result = 31 * result + (observationType != null ? observationType.hashCode() : 0);
        result = 31 * result + (observationDetail != null ? observationDetail.hashCode() : 0);
        result = 31 * result + (units != null ? units.hashCode() : 0);
        result = 31 * result + (measured != null ? measured.hashCode() : 0);
        result = 31 * result + (calculationMethod != null ? calculationMethod.hashCode() : 0);
        result = 31 * result + (samplingInstrument != null ? samplingInstrument.hashCode() : 0);
        result = 31 * result + (analyzingInstrument != null ? analyzingInstrument.hashCode() : 0);
        result = 31 * result + (detailedInformation != null ? detailedInformation.hashCode() : 0);
        result = 31 * result + (fieldReplicate != null ? fieldReplicate.hashCode() : 0);
        result = 31 * result + (uncertainty != null ? uncertainty.hashCode() : 0);
        result = 31 * result + (qualityFlag != null ? qualityFlag.hashCode() : 0);
        result = 31 * result + (researcherName != null ? researcherName.hashCode() : 0);
        result = 31 * result + (researcherInstitution != null ? researcherInstitution.hashCode() : 0);
        result = 31 * result + (fullVariableName != null ? fullVariableName.hashCode() : 0);
        result = 31 * result + (referenceMethod != null ? referenceMethod.hashCode() : 0);
        result = 31 * result + (standardizationTechnique != null ? standardizationTechnique.hashCode() : 0);
        result = 31 * result + (crmManufacture != null ? crmManufacture.hashCode() : 0);
        result = 31 * result + (batchNumber != null ? batchNumber.hashCode() : 0);
        result = 31 * result + (poison != null ? poison.hashCode() : 0);
        result = 31 * result + (poisonVolume != null ? poisonVolume.hashCode() : 0);
        result = 31 * result + (poisonDescription != null ? poisonDescription.hashCode() : 0);
        result = 31 * result + (cellType != null ? cellType.hashCode() : 0);
        result = 31 * result + (curveFittingMethod != null ? curveFittingMethod.hashCode() : 0);
        result = 31 * result + (magnitudeOfBlankCorrection != null ? magnitudeOfBlankCorrection.hashCode() : 0);
        result = 31 * result + (phTemperature != null ? phTemperature.hashCode() : 0);
        result = 31 * result + (phScale != null ? phScale.hashCode() : 0);
        result = 31 * result + (phStandards != null ? phStandards.hashCode() : 0);
        result = 31 * result + (titrationType != null ? titrationType.hashCode() : 0);
        result = 31 * result + (intakeDepth != null ? intakeDepth.hashCode() : 0);
        result = 31 * result + (dryingMethod != null ? dryingMethod.hashCode() : 0);
        result = 31 * result + (equilibratorType != null ? equilibratorType.hashCode() : 0);
        result = 31 * result + (equilibratorVolume != null ? equilibratorVolume.hashCode() : 0);
        result = 31 * result + (gasFlowRate != null ? gasFlowRate.hashCode() : 0);
        result = 31 * result + (equilibratorPressureMeasureMethod != null ? equilibratorPressureMeasureMethod.hashCode() : 0);
        result = 31 * result + (equilibratorTemperatureMeasureMethod != null ? equilibratorTemperatureMeasureMethod.hashCode() : 0);
        result = 31 * result + (intakeLocation != null ? intakeLocation.hashCode() : 0);
        result = 31 * result + (flowRate != null ? flowRate.hashCode() : 0);
        result = 31 * result + (freqencyOfStandardization != null ? freqencyOfStandardization.hashCode() : 0);
        result = 31 * result + (storageMethod != null ? storageMethod.hashCode() : 0);
        result = 31 * result + (pco2Temperature != null ? pco2Temperature.hashCode() : 0);
        result = 31 * result + (gasConcentration != null ? gasConcentration.hashCode() : 0);
        result = 31 * result + (headspaceVolume != null ? headspaceVolume.hashCode() : 0);
        result = 31 * result + (standardGasManufacture != null ? standardGasManufacture.hashCode() : 0);
        result = 31 * result + (gasDetectorManufacture != null ? gasDetectorManufacture.hashCode() : 0);
        result = 31 * result + (gasDetectorModel != null ? gasDetectorModel.hashCode() : 0);
        result = 31 * result + (gasDectectorResolution != null ? gasDectectorResolution.hashCode() : 0);
        result = 31 * result + (seawaterVolume != null ? seawaterVolume.hashCode() : 0);
        result = 31 * result + (temperatureCorrectionMethod != null ? temperatureCorrectionMethod.hashCode() : 0);
//        result = 31 * result + (temperatureCorrection != null ? temperatureCorrection.hashCode() : 0);
        result = 31 * result + (temperatureMeasurement != null ? temperatureMeasurement.hashCode() : 0);
        result = 31 * result + (temperatureStandarization != null ? temperatureStandarization.hashCode() : 0);
        result = 31 * result + (standardGasUncertainties != null ? standardGasUncertainties.hashCode() : 0);
        result = 31 * result + (gasDectectorUncertainty != null ? gasDectectorUncertainty.hashCode() : 0);
        result = 31 * result + (vaporCorrection != null ? vaporCorrection.hashCode() : 0);
        result = 31 * result + (vented != null ? vented.hashCode() : 0);
        result = 31 * result + (biologicalSubject != null ? biologicalSubject.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (lifeStage != null ? lifeStage.hashCode() : 0);
        result = 31 * result + (speciesIdCode != null ? speciesIdCode.hashCode() : 0);
        result = 31 * result + (qualityControl != null ? qualityControl.hashCode() : 0);
        result = 31 * result + (abbreviationQualityFlag != null ? abbreviationQualityFlag.hashCode() : 0);
        result = 31 * result + (sopChanges != null ? sopChanges.hashCode() : 0);
        result = 31 * result + (collectionMethod != null ? collectionMethod.hashCode() : 0);
        result = 31 * result + (analyzingInformation != null ? analyzingInformation.hashCode() : 0);
        result = 31 * result + (phDyeTypeManuf != null ? phDyeTypeManuf.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (!this.getClass().equals(other.getClass())) {
            return false;
        }
        Variable v = (Variable) other;
        return
                sCompare(abbreviation, v.abbreviation) == 0 &&
                        sCompare(fullVariableName, v.fullVariableName) == 0 &&
                        sCompare(manipulationMethod, v.manipulationMethod) == 0 &&
                        sCompare(observationType, v.observationType) == 0 &&
                        sCompare(observationDetail, v.observationDetail) == 0 &&
                        sCompare(units, v.units) == 0 &&
                        sCompare(measured, v.measured) == 0 &&
                        sCompare(calculationMethod, v.calculationMethod) == 0 &&
                        sCompare(samplingInstrument, v.samplingInstrument) == 0 &&
                        sCompare(analyzingInstrument, v.analyzingInstrument) == 0 &&
                        sCompare(detailedInformation, v.detailedInformation) == 0 &&
                        sCompare(fieldReplicate, v.fieldReplicate) == 0 &&
                        sCompare(uncertainty, v.uncertainty) == 0 &&
                        sCompare(qualityFlag, v.qualityFlag) == 0 &&
                        sCompare(researcherName, v.researcherName) == 0 &&
                        sCompare(researcherInstitution, v.researcherInstitution) == 0 &&
                        sCompare(referenceMethod, v.referenceMethod) == 0 &&
                        sCompare(standardizationTechnique, v.standardizationTechnique) == 0 &&
                        sCompare(crmManufacture, v.crmManufacture) == 0 &&
                        sCompare(batchNumber, v.batchNumber) == 0 &&
                        sCompare(poison, v.poison) == 0 &&
                        sCompare(poisonVolume, v.poisonVolume) == 0 &&
                        sCompare(poisonDescription, v.poisonDescription) == 0 &&
                        sCompare(cellType, v.cellType) == 0 &&
                        sCompare(curveFittingMethod, v.curveFittingMethod) == 0 &&
                        sCompare(magnitudeOfBlankCorrection, v.magnitudeOfBlankCorrection) == 0 &&
                        sCompare(phTemperature, v.phTemperature) == 0 &&
                        sCompare(phScale, v.phScale) == 0 &&
                        sCompare(phStandards, v.phStandards) == 0 &&
                        sCompare(titrationType, v.titrationType) == 0 &&
                        sCompare(intakeDepth, v.intakeDepth) == 0 &&
                        sCompare(dryingMethod, v.dryingMethod) == 0 &&
                        sCompare(equilibratorType, v.equilibratorType) == 0 &&
                        sCompare(equilibratorVolume, v.equilibratorVolume) == 0 &&
                        sCompare(gasFlowRate, v.gasFlowRate) == 0 &&
                        sCompare(equilibratorPressureMeasureMethod, v.equilibratorPressureMeasureMethod) == 0 &&
                        sCompare(equilibratorTemperatureMeasureMethod, v.equilibratorTemperatureMeasureMethod) == 0 &&
                        sCompare(intakeLocation, v.intakeLocation) == 0 &&
                        sCompare(flowRate, v.flowRate) == 0 &&
                        sCompare(freqencyOfStandardization, v.freqencyOfStandardization) == 0 &&
                        sCompare(storageMethod, v.storageMethod) == 0 &&
                        sCompare(pco2Temperature, v.pco2Temperature) == 0 &&
                        sCompare(gasConcentration, v.gasConcentration) == 0 &&
                        sCompare(headspaceVolume, v.headspaceVolume) == 0 &&
                        sCompare(standardGasManufacture, v.standardGasManufacture) == 0 &&
                        sCompare(gasDetectorManufacture, v.gasDetectorManufacture) == 0 &&
                        sCompare(gasDetectorModel, v.gasDetectorModel) == 0 &&
                        sCompare(gasDectectorResolution, v.gasDectectorResolution) == 0 &&
                        sCompare(seawaterVolume, v.seawaterVolume) == 0 &&
                        sCompare(temperatureCorrectionMethod, v.temperatureCorrectionMethod) == 0 &&
//            sCompare(temperatureCorrection, v.temperatureCorrection) == 0 &&
                        sCompare(temperatureMeasurement, v.temperatureMeasurement) == 0 &&
                        sCompare(temperatureStandarization, v.temperatureStandarization) == 0 &&
                        sCompare(standardGasUncertainties, v.standardGasUncertainties) == 0 &&
                        sCompare(gasDectectorUncertainty, v.gasDectectorUncertainty) == 0 &&
                        sCompare(vaporCorrection, v.vaporCorrection) == 0 &&
                        sCompare(vented, v.vented) == 0 &&
                        sCompare(biologicalSubject, v.biologicalSubject) == 0 &&
                        sCompare(duration, v.duration) == 0 &&
                        sCompare(lifeStage, v.lifeStage) == 0 &&
                        sCompare(speciesIdCode, v.speciesIdCode) == 0 &&
                        sCompare(qualityControl, v.qualityControl) == 0 &&
                        sCompare(abbreviationQualityFlag, v.abbreviationQualityFlag) == 0 &&
                        sCompare(sopChanges, v.sopChanges) == 0 &&
                        sCompare(collectionMethod, v.collectionMethod) == 0 &&
                        sCompare(analyzingInformation, v.analyzingInformation) == 0 &&
                        sCompare(phDyeTypeManuf, v.phDyeTypeManuf) == 0;
    }

    @Override
    public Variable sClone() {
        Variable newv = new Variable();
        newv.position = this.position;
//        newv.id = this.id;
//        newv.version = this.version;
        newv.abbreviation = this.abbreviation;
        newv.manipulationMethod = this.manipulationMethod;
        newv.observationType = this.observationType;
        newv.observationDetail = this.observationDetail;
        newv.units = this.units;
        newv.measured = this.measured;
        newv.calculationMethod = this.calculationMethod;
        newv.samplingInstrument = this.samplingInstrument;
        newv.analyzingInstrument = this.analyzingInstrument;
        newv.detailedInformation = this.detailedInformation;
        newv.fieldReplicate = this.fieldReplicate;
        newv.uncertainty = this.uncertainty;
        newv.qualityFlag = this.qualityFlag;
        newv.researcherName = this.researcherName;
        newv.researcherInstitution = this.researcherInstitution;
        newv.fullVariableName = this.fullVariableName;
        newv.referenceMethod = this.referenceMethod;
        newv.standardizationTechnique = this.standardizationTechnique;
        newv.crmManufacture = this.crmManufacture;
        newv.batchNumber = this.batchNumber;
        newv.poison = this.poison;
        newv.poisonVolume = this.poisonVolume;
        newv.poisonDescription = this.poisonDescription;
        newv.cellType = this.cellType;
        newv.curveFittingMethod = this.curveFittingMethod;
        newv.magnitudeOfBlankCorrection = this.magnitudeOfBlankCorrection;
        newv.phTemperature = this.phTemperature;
        newv.phScale = this.phScale;
        newv.phStandards = this.phStandards;
        newv.titrationType = this.titrationType;
        newv.intakeDepth = this.intakeDepth;
        newv.dryingMethod = this.dryingMethod;
        newv.equilibratorType = this.equilibratorType;
        newv.equilibratorVolume = this.equilibratorVolume;
        newv.gasFlowRate = this.gasFlowRate;
        newv.equilibratorPressureMeasureMethod = this.equilibratorPressureMeasureMethod;
        newv.equilibratorTemperatureMeasureMethod = this.equilibratorTemperatureMeasureMethod;
        newv.intakeLocation = this.intakeLocation;
        newv.flowRate = this.flowRate;
        newv.freqencyOfStandardization = this.freqencyOfStandardization;
        newv.storageMethod = this.storageMethod;
        newv.pco2Temperature = this.pco2Temperature;
        newv.gasConcentration = this.gasConcentration;
        newv.headspaceVolume = this.headspaceVolume;
        newv.standardGasManufacture = this.standardGasManufacture;
        newv.gasDetectorManufacture = this.gasDetectorManufacture;
        newv.gasDetectorModel = this.gasDetectorModel;
        newv.gasDectectorResolution = this.gasDectectorResolution;
        newv.seawaterVolume = this.seawaterVolume;
        newv.temperatureCorrectionMethod = this.temperatureCorrectionMethod;
        newv.temperatureMeasurement = this.temperatureMeasurement;
        newv.temperatureStandarization = this.temperatureStandarization;
        newv.standardGasUncertainties = this.standardGasUncertainties;
        newv.gasDectectorUncertainty = this.gasDectectorUncertainty;
        newv.vaporCorrection = this.vaporCorrection;
        newv.vented = this.vented;
        newv.biologicalSubject = this.biologicalSubject;
        newv.duration = this.duration;
        newv.lifeStage = this.lifeStage;
        newv.speciesIdCode = this.speciesIdCode;
        newv.qualityControl = this.qualityControl;
        newv.abbreviationQualityFlag = this.abbreviationQualityFlag;
        newv.sopChanges = this.sopChanges;
        newv.collectionMethod = this.collectionMethod;
        newv.analyzingInformation = this.analyzingInformation;
        newv.phDyeTypeManuf = this.phDyeTypeManuf;
        return newv;
    }

    @Override
    public boolean hasContent() {
        boolean hasConent = ! (
                    isEmpty(abbreviation) &&
                    isEmpty(manipulationMethod) &&
                    isEmpty(observationType) &&
                    isEmpty(observationDetail) &&
                    isEmpty(units) &&
                    isEmpty(measured) &&
                    isEmpty(calculationMethod) &&
                    isEmpty(samplingInstrument) &&
                    isEmpty(analyzingInstrument) &&
                    isEmpty(detailedInformation) &&
                    isEmpty(fieldReplicate) &&
                    isEmpty(uncertainty) &&
                    isEmpty(qualityFlag) &&
                    isEmpty(researcherName) &&
                    isEmpty(researcherInstitution) &&
                    isEmpty(fullVariableName) &&
                    isEmpty(referenceMethod) &&
                    isEmpty(standardizationTechnique) &&
                    isEmpty(crmManufacture) &&
                    isEmpty(batchNumber) &&
                    isEmpty(poison) &&
                    isEmpty(poisonVolume) &&
                    isEmpty(poisonDescription) &&
                    isEmpty(cellType) &&
                    isEmpty(curveFittingMethod) &&
                    isEmpty(magnitudeOfBlankCorrection) &&
                    isEmpty(phTemperature) &&
                    isEmpty(phScale) &&
                    isEmpty(phStandards) &&
                    isEmpty(titrationType) &&
                    isEmpty(intakeDepth) &&
                    isEmpty(dryingMethod) &&
                    isEmpty(equilibratorType) &&
                    isEmpty(equilibratorVolume) &&
                    isEmpty(gasFlowRate) &&
                    isEmpty(equilibratorPressureMeasureMethod) &&
                    isEmpty(equilibratorTemperatureMeasureMethod) &&
                    isEmpty(intakeLocation) &&
                    isEmpty(flowRate) &&
                    isEmpty(freqencyOfStandardization) &&
                    isEmpty(storageMethod) &&
                    isEmpty(pco2Temperature) &&
                    isEmpty(gasConcentration) &&
                    isEmpty(headspaceVolume) &&
                    isEmpty(standardGasManufacture) &&
                    isEmpty(gasDetectorManufacture) &&
                    isEmpty(gasDetectorModel) &&
                    isEmpty(gasDectectorResolution) &&
                    isEmpty(seawaterVolume) &&
                    isEmpty(temperatureCorrectionMethod) &&
                    isEmpty(temperatureMeasurement) &&
                    isEmpty(temperatureStandarization) &&
                    isEmpty(standardGasUncertainties) &&
                    isEmpty(gasDectectorUncertainty) &&
                    isEmpty(vaporCorrection) &&
                    isEmpty(vented) &&
                    isEmpty(biologicalSubject) &&
                    isEmpty(duration) &&
                    isEmpty(lifeStage) &&
                    isEmpty(speciesIdCode) &&
                    isEmpty(qualityControl) &&
                    isEmpty(abbreviationQualityFlag) &&
                    isEmpty(sopChanges) &&
                    isEmpty(collectionMethod) &&
                    isEmpty(analyzingInformation) &&
                    isEmpty(phDyeTypeManuf));
        return hasConent;
    }
}
