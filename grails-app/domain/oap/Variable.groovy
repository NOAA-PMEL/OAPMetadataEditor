package oap

class Variable extends GenericVariable {
    static belongsTo = [document: Document]
    static constraints = {
        document(nullable: true)
    }
    static Variable from(GenericVariable genV) {
        Variable v = new Variable()
        v.abbreviation = genV.abbreviation
        v.manipulationMethod = genV.manipulationMethod
        v.observationType = genV.observationType
        v.observationDetail = genV.observationDetail
        v.units = genV.units
        v.measured = genV.measured
        v.calculationMethod = genV.calculationMethod
        v.samplingInstrument = genV.samplingInstrument
        v.analyzingInstrument = genV.analyzingInstrument
        v.detailedInformation = genV.detailedInformation
        v.fieldReplicate = genV.fieldReplicate
        v.uncertainty = genV.uncertainty
        v.qcApplied = genV.qcApplied
        v.researcherName = genV.researcherName
        v.researcherInstitution = genV.researcherInstitution
        v.fullVariableName = genV.fullVariableName
        v.referenceMethod = genV.referenceMethod
        v.standardizationTechnique = genV.standardizationTechnique
        v.crmManufacture = genV.crmManufacture
        v.batchNumber = genV.batchNumber
        v.poison = genV.poison
        v.poisonVolume = genV.poisonVolume
        v.poisonDescription = genV.poisonDescription
        v.cellType = genV.cellType
        v.curveFittingMethod = genV.curveFittingMethod
        v.magnitudeOfBlankCorrection = genV.magnitudeOfBlankCorrection
        v.phTemperature = genV.phTemperature
        v.phScale = genV.phScale
        v.phStandards = genV.phStandards
        v.titrationType = genV.titrationType
        v.intakeDepth = genV.intakeDepth
        v.dryingMethod = genV.dryingMethod
        v.equilibratorType = genV.equilibratorType
        v.equilibratorVolume = genV.equilibratorVolume
        v.gasFlowRate = genV.gasFlowRate
        v.equilibratorPressureMeasureMethod = genV.equilibratorPressureMeasureMethod
        v.equilibratorTemperatureMeasureMethod = genV.equilibratorTemperatureMeasureMethod
        v.intakeLocation = genV.intakeLocation
        v.flowRate = genV.flowRate
        v.freqencyOfStandardization = genV.freqencyOfStandardization
        v.storageMethod = genV.storageMethod
        v.pco2Temperature = genV.pco2Temperature
//        v.gasConcentration = genV.gasConcentration
        v.headspaceVolume = genV.headspaceVolume
//        v.standardGasManufacture = genV.standardGasManufacture
        v.gasDetectorManufacture = genV.gasDetectorManufacture
        v.gasDetectorModel = genV.gasDetectorModel
        v.gasDectectorResolution = genV.gasDectectorResolution
        v.seawaterVolume = genV.seawaterVolume
        v.temperatureCorrectionMethod = genV.temperatureCorrectionMethod
        v.temperatureMeasurement = genV.temperatureMeasurement
        v.temperatureStandarization = genV.temperatureStandarization
//        v.standardGasUncertainties = genV.standardGasUncertainties
        v.gasDectectorUncertainty = genV.gasDectectorUncertainty
        v.vaporCorrection = genV.vaporCorrection
        v.vented = genV.vented
        v.biologicalSubject = genV.biologicalSubject
        v.duration = genV.duration
        v.lifeStage = genV.lifeStage
        v.speciesIdCode = genV.speciesIdCode
        v.variableType = genV.variableType
        v.qcSchemeName = genV.qcSchemeName
        v.qcVariableName = genV.qcVariableName
        v.sopChanges = genV.sopChanges
        v.collectionMethod = genV.collectionMethod
        v.analyzingInformation =genV.analyzingInformation
        v.phDyeTypeManuf = genV.phDyeTypeManuf
//        v.traceabilityOfStdGas = genV.traceabilityOfStdGas
        v.pco2CalcMethod = genV.pco2CalcMethod
        v.pco2CalcMethod = genV.pco2CalcMethod
        v.temperatureMeasurementCalibrationMethod = genV.temperatureMeasurementCalibrationMethod
        v.pressureMeasurementCalibrationMethod = genV.pressureMeasurementCalibrationMethod
        v.uncertaintyOfTemperature = genV.uncertaintyOfTemperature
        v.uncertaintyOfTotalPressure = genV.uncertaintyOfTotalPressure
        v.totalPressureCalcMethod = genV.totalPressureCalcMethod
        v.standardGases = genV.standardGases

        /*
        new VariableBuilder()
                .abbreviation(genV.abbreviation)
                .manipulationMethod(genV.manipulationMethod)
                .observationType(genV.observationType)
                .observationDetail(genV.observationDetail)
                .units(genV.units)
                .measured(genV.measured)
                .calculationMethod(genV.calculationMethod)
                .samplingInstrument(genV.samplingInstrument)
                .analyzingInstrument(genV.analyzingInstrument)
                .detailedInformation(genV.detailedInformation)
                .fieldReplicate(genV.fieldReplicate)
                .uncertainty(genV.uncertainty)
                .qcApplied(genV.qcApplied)
                .researcherName(genV.researcherName)
                .researcherInstitution(genV.researcherInstitution)
                .fullVariableName(genV.fullVariableName)
                .referenceMethod(genV.referenceMethod)
                .standardizationTechnique(genV.standardizationTechnique)
                .crmManufacture(genV.crmManufacture)
                .batchNumber(genV.batchNumber)
                .poison(genV.poison)
                .poisonVolume(genV.poisonVolume)
                .poisonDescription(genV.poisonDescription)
                .cellType(genV.cellType)
                .curveFittingMethod(genV.curveFittingMethod)
                .magnitudeOfBlankCorrection(genV.magnitudeOfBlankCorrection)
                .phTemperature(genV.phTemperature)
                .phScale(genV.phScale)
                .phStandards(genV.phStandards)
                .titrationType(genV.titrationType)
                .intakeDepth(genV.intakeDepth)
                .dryingMethod(genV.dryingMethod)
                .equilibratorType(genV.equilibratorType)
                .equilibratorVolume(genV.equilibratorVolume)
                .gasFlowRate(genV.gasFlowRate)
                .equilibratorPressureMeasureMethod(genV.equilibratorPressureMeasureMethod)
                .equilibratorTemperatureMeasureMethod(genV.equilibratorTemperatureMeasureMethod)
                .intakeLocation(genV.intakeLocation)
                .flowRate(genV.flowRate)
                .freqencyOfStandardization(genV.freqencyOfStandardization)
                .storageMethod(genV.storageMethod)
                .pco2Temperature(genV.pco2Temperature)
                .gasConcentration(genV.gasConcentration)
                .headspaceVolume(genV.headspaceVolume)
                .standardGasManufacture(genV.standardGasManufacture)
                .gasDetectorManufacture(genV.gasDetectorManufacture)
                .gasDetectorModel(genV.gasDetectorModel)
                .gasDectectorResolution(genV.gasDectectorResolution)
                .seawaterVolume(genV.seawaterVolume)
                .temperatureCorrectionMethod(genV.temperatureCorrectionMethod)
                .temperatureMeasurement(genV.temperatureMeasurement)
                .temperatureStandarization(genV.temperatureStandarization)
                .standardGasUncertainties(genV.standardGasUncertainties)
                .gasDectectorUncertainty(genV.gasDectectorUncertainty)
                .vaporCorrection(genV.vaporCorrection)
                .vented(genV.vented)
                .biologicalSubject(genV.biologicalSubject)
                .duration(genV.duration)
                .lifeStage(genV.lifeStage)
                .speciesIdCode(genV.speciesIdCode)
                .variableType(genV.variableType)
         */
        return v
    }
}

/*
@Builder(builderStrategy = ExternalStrategy,
         forClass = Variable,
         includeSuperProperties = true)
class VariableBuilder{}
 */
