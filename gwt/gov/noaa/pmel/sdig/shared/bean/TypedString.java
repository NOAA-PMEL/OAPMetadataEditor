package gov.noaa.pmel.sdig.shared.bean;

import gov.noaa.pmel.sdig.client.panels.GetsDirty;
import gov.noaa.pmel.sdig.shared.HasContent;
import gov.noaa.pmel.sdig.shared.Stringy;

public class TypedString implements HasContent, Stringy {
    private String type;
    private String value;

    public TypedString() {
        type = "";
        value = "";
    }

    public TypedString(String type, String value) {
        setType(type);
        setValue(value);
    }

    @Override
    public String toString() {
        return type+":"+value;
    }
    @Override
    public TypedString sClone() {
        return new TypedString(type, value);
    }

    @Override
    public boolean hasContent() {
        return ! (isEmpty(type) && isEmpty(value));
    }

    public boolean isDirty(TypedString original) {
        return ! this.equals(original);
    }

    public String getType() { return type; }
    public void setType(String type) {
        this.type = type != null ? type.trim() : "";
    }
    public String getValue() { return value; }
    public void setValue(String value) {
        this.value = value != null ? value.trim() : "";
    }

    @Override
    public boolean equals(Object other) {
        if ( other == null || ! (other instanceof TypedString)) {
            return false;
        }
        TypedString otherStr = (TypedString) other;
        return type.equals(otherStr.type) && value.equals(otherStr.value);
    }
    @Override
    public int hashCode() {
        return 37 + type.hashCode() + value.hashCode();
    }
}
