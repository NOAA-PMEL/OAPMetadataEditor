package gov.noaa.pmel.sdig.shared.bean;

import gov.noaa.pmel.sdig.shared.HasContent;
import gov.noaa.pmel.sdig.shared.Stringy;

public class DescribedValue  implements HasContent, Stringy {

    private String value = "";
    private String description = "";

    public DescribedValue() {}
    public DescribedValue(String value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * Returns whether the object's value has been set.
     *
     * Only setting the description will return false, and
     * whether or not the description has been set does not affect the result.
     *
     * @return whether the value is set.
     */
    @Override
    public boolean hasContent() {
        return ! isEmpty(value);
    }

    /**
     * @return
     */
    @Override
    public Stringy sClone() {
        DescribedValue clone = new DescribedValue();
        clone.value = value;
        clone.description = description;
        return clone;
    }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getDescription() { return description; }
    public void setDescription(String description) {this.description = description;}

    @Override
    public boolean equals(Object obj) {
        if ( ! (obj instanceof DescribedValue) ) { return false; }
        DescribedValue other = (DescribedValue) obj;
        return other.value.equals(value) && other.description.equals(description);
    }

}
