package oap

class TypedString {
    String type
    String value

    static constraints = {
        type (nullable: true)
        value (nullable: true)
    }

    public TypedString() {
        type = ""
        value = ""
    }

    public TypedString(String type, String value) {
        setType(type)
        setValue(value)
    }
    public void setType(String type) {
        this.type = type != null ? type.trim() : "";
    }
    public void setValue(String value) {
        this.value = value != null ? value.trim() : "";
    }
}
