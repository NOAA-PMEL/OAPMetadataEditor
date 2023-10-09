package oap

class StandardGas {
    static belongsTo = [variable: GenericVariable]

    String manufacturer
    String concentration
    String uncertainty
    String wmoTraceability

    static constraints = {
        manufacturer (nullable: false)
        concentration (nullable: false)
        uncertainty (nullable: true)
        wmoTraceability (nullable: true)
    }

    public StandardGas() {
        String manufacturer = ""
        String concentration = ""
        String uncertainty = ""
        String wmoTraceability = ""
    }
    public StandardGas(String manufacturer, String concentration, String uncertainty, String wmoTraceability) {
        this.manufacturer = manufacturer != null ? manufacturer.trim() : ""
        this.concentration = concentration != null ? concentration.trim() : ""
        this.uncertainty = uncertainty != null ? uncertainty.trim() : ""
        this.wmoTraceability = wmoTraceability != null ? wmoTraceability.trim() : ""
    }
}
