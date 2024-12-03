package oap

class DescribedValue {
    String value
    String description

    static constraints = {
        value (nullable: false)
        description (nullable: true)
    }

    public DescribedValue() {
        value = ""
        description = ""
    }

    public DescribedValue(String value, String description) {
        setValue(value)
        setDescription(description)
    }
    public String getDescription() { return description }
    public void setDescription(String description) {
        this.description = description != null ? description.trim() : "";
    }
    public String getValue() { return value }
    public void setValue(String value) {
        this.value = value != null ? value.trim() : "";
    }
}
