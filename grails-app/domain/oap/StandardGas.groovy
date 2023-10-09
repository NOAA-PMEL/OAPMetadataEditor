package oap

class StandardGas {
    static belongsTo = [variable: GenericVariable]

    String gasId
    String manufacturer
    String concentration
    String uncertainty
    String wmoTraceability

    static constraints = {
        gasId (nullable: true)
        manufacturer (nullable: true)
        concentration (nullable: true)
        uncertainty (nullable: true)
        wmoTraceability (nullable: true)
    }

    StandardGas() {
        String gasId = ""
        String manufacturer = ""
        String concentration = ""
        String uncertainty = ""
        String wmoTraceability = ""
    }
    public StandardGas(String manufacturer, String concentration, String uncertainty, String wmoTraceability) {
        this("", manufacturer, concentration, uncertainty, wmoTraceability);
    }
    public StandardGas(String gasId, String manufacturer, String concentration, String uncertainty, String wmoTraceability) {
        this.gasId = gasId != null ? gasId.trim() : ""
        this.manufacturer = manufacturer != null ? manufacturer.trim() : ""
        this.concentration = concentration != null ? concentration.trim() : ""
        this.uncertainty = uncertainty != null ? uncertainty.trim() : ""
        this.wmoTraceability = wmoTraceability != null ? wmoTraceability.trim() : ""
    }
}
