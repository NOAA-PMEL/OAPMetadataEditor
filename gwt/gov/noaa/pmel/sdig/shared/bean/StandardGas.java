package gov.noaa.pmel.sdig.shared.bean;

import gov.noaa.pmel.sdig.shared.HasContent;
import gov.noaa.pmel.sdig.shared.Stringy;

public class StandardGas implements Stringy, HasContent {

    String manufacturer;
    String concentration;
    String uncertainty;
    String wmoTraceability;

    public StandardGas() {
        manufacturer = "";
        concentration = "";
        uncertainty = "";
        wmoTraceability = "";
    }
    public StandardGas(String manufacturer, String concentration,
                       String uncertainty, String wmoTraceability) {
        setManufacturer(manufacturer);
        setConcentration(concentration);
        setUncertainty(uncertainty);
        setWmoTraceability(wmoTraceability);
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer != null ? manufacturer.trim() : "";
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration != null ? concentration.trim() : "";
    }

    public String getUncertainty() {
        return uncertainty;
    }

    public void setUncertainty(String uncertainty) {
        this.uncertainty = uncertainty != null ? uncertainty.trim() : "";
    }

    public String getWmoTraceability() {
        return wmoTraceability;
    }

    public void setWmoTraceability(String wmoTraceability) {
        this.wmoTraceability = wmoTraceability != null ? wmoTraceability.trim() : "";
    }

    @Override
    public boolean hasContent() {
        return ! (isEmpty(manufacturer) && isEmpty(concentration) &&
                  isEmpty(uncertainty) && isEmpty(wmoTraceability));
    }

    @Override
    public Stringy sClone() {
        return new StandardGas(manufacturer,concentration,uncertainty,wmoTraceability);
    }

    public boolean sEquals(StandardGas other) {
        return sAreEffectivelyTheSame(this.manufacturer, other.manufacturer) &&
                sAreEffectivelyTheSame(this.concentration, other.concentration) &&
                sAreEffectivelyTheSame(this.uncertainty, other.uncertainty) &&
                sAreEffectivelyTheSame(this.wmoTraceability, other.wmoTraceability);
    }
}
