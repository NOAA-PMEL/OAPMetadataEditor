package oap

class GenericVariable {
    String abbreviation
    String manipulationMethod
    String observationType
    String observationDetail
    String units
    String measured
    String calculationMethod
    String samplingInstrument
    String analyzingInstrument
    String detailedInformation
    String fieldReplicate
    String uncertainty
    String qcApplied
    String researcherName
    String researcherInstitution
    String fullVariableName
    String referenceMethod
    String standardizationTechnique
    String crmManufacture
    String batchNumber
    String poison
    String poisonVolume
    String poisonDescription
    String cellType
    String curveFittingMethod
    String magnitudeOfBlankCorrection
    String phTemperature
    String phScale
    String phStandards
    String titrationType
    String intakeDepth
    String dryingMethod
    String equilibratorType
    String equilibratorVolume
    String gasFlowRate
    String equilibratorPressureMeasureMethod
    String equilibratorTemperatureMeasureMethod
    String intakeLocation
    String flowRate
    String freqencyOfStandardization
    String storageMethod
    String pco2Temperature
//    String gasConcentration
    String headspaceVolume
//    String standardGasManufacture
    String gasDetectorManufacture
    String gasDetectorModel
    String gasDectectorResolution
    String seawaterVolume
    String temperatureCorrectionMethod
    String temperatureMeasurement
    String temperatureStandarization
//    String standardGasUncertainties
    String gasDectectorUncertainty
    String vaporCorrection
    String vented
    String biologicalSubject
    String duration
    String lifeStage
    String speciesIdCode
    String variableType

    String qcSchemeName
    String qcVariableName
    String sopChanges
    String collectionMethod
    String analyzingInformation
    String phDyeTypeManuf

//    String traceabilityOfStdGas
    String fco2CalcMethod
    String pco2CalcMethod
    String temperatureMeasurementCalibrationMethod
    String pressureMeasurementCalibrationMethod
    String uncertaintyOfTemperature
    String uncertaintyOfTotalPressure
    String totalPressureCalcMethod
    String socatType

    List<StandardGas> standardGases

//    static transients = ['variableType']
    static constraints = {
        abbreviation (nullable: true)
        manipulationMethod (nullable: true, type: 'text')
        observationType (nullable: true)
        observationDetail (nullable: true, type: 'text')
        units (nullable: true)
        measured (nullable: true)
        calculationMethod (nullable: true, type: 'text')
        samplingInstrument (nullable: true, type: 'text')
        analyzingInstrument (nullable: true, type: 'text')
        detailedInformation (nullable: true, type: 'text')
        fieldReplicate (nullable: true, type: 'text')
        uncertainty (nullable: true, type: 'text')
        qcApplied (nullable: true, type: 'text')
        researcherName (nullable: true)
        researcherInstitution (nullable: true, type: 'text')
        fullVariableName (nullable: true, type: 'text')
        referenceMethod (nullable: true, type: 'text')
        standardizationTechnique (nullable: true, type: 'text')
        crmManufacture (nullable: true)
        batchNumber (nullable: true)
        poison (nullable: true, type: 'text')
        poisonVolume (nullable: true)
        poisonDescription (nullable: true, type: 'text')
        cellType (nullable: true)
        curveFittingMethod (nullable: true, type: 'text')
        magnitudeOfBlankCorrection (nullable: true)
        phTemperature (nullable: true)
        phScale (nullable: true)
        phStandards (nullable: true, type: 'text')
        titrationType (nullable: true, type: 'text')
        intakeDepth (nullable: true)
        dryingMethod (nullable: true, type: 'text')
        equilibratorType (nullable: true)
        equilibratorVolume (nullable: true)
        gasFlowRate (nullable: true)
        equilibratorPressureMeasureMethod (nullable: true, type: 'text')
        equilibratorTemperatureMeasureMethod (nullable: true, type: 'text')
        intakeLocation (nullable: true)
        flowRate (nullable: true)
        freqencyOfStandardization (nullable: true, type: 'text')
        storageMethod (nullable: true, type: 'text')
        pco2Temperature (nullable: true)
//        gasConcentration (nullable: true)
        headspaceVolume (nullable: true)
//        standardGasManufacture (nullable: true)
        gasDetectorManufacture (nullable: true)
        gasDetectorModel (nullable: true)
        gasDectectorResolution (nullable: true)
        seawaterVolume (nullable: true)
        temperatureCorrectionMethod (nullable: true, type: 'text')
        temperatureMeasurement (nullable: true)
        temperatureStandarization (nullable: true, type: 'text')
//        standardGasUncertainties (nullable: true, type: 'text')
        gasDectectorUncertainty (nullable: true, type: 'text')
        vaporCorrection (nullable: true, type: 'text')
        vented (nullable: true)
        biologicalSubject (nullable: true, type: 'text')
        duration (nullable: true)
        lifeStage (nullable: true)
        speciesIdCode (nullable: true)
        variableType (nullable: true)
        qcSchemeName (nullable: true)
        qcVariableName (nullable: true)
        sopChanges (nullable: true, type: 'text')
        collectionMethod (nullable: true, type: 'text')
        analyzingInformation (nullable: true, type: 'text')
        phDyeTypeManuf (nullable: true)
//        traceabilityOfStdGas (nullable: true, type: 'text')
        fco2CalcMethod (nullable: true, type: 'text')
        pco2CalcMethod (nullable: true, type: 'text')
        temperatureMeasurementCalibrationMethod (nullable: true, type: 'text')
        pressureMeasurementCalibrationMethod (nullable: true, type: 'text')
        uncertaintyOfTemperature (nullable: true)
        uncertaintyOfTotalPressure (nullable: true)
        totalPressureCalcMethod (nullable: true, type: 'text')
        socatType (nullable: true)
        standardGases (nullable: true)
    }
    static hasMany = [
            standardGases : StandardGas
    ]
    static mapping = {
        standardGases(cascade: 'all-delete-orphan')
    }
}
