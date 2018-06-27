package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.user.client.ui.HasText;
import gov.noaa.pmel.sdig.shared.bean.Variable;

public interface GetsDirty<T extends Object> {
    default boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
    default boolean isEmpty(HasText field) {
        String value = field.getText();
        return isEmpty(value);
    }
    default boolean isDirty(HasText field, String original) {
        boolean isDirty = false;
        String fieldValue = field.getText() != null ? field.getText().trim() : "";
        String originalValue = original != null ? original.trim() : "";
        isDirty = ( isEmpty(fieldValue) != isEmpty(originalValue)) ||
                ( ! fieldValue.equals(originalValue));
        return isDirty;
    }
    default boolean isDirty(String fieldValue, String originalValue) {
        boolean isDirty = isEmpty(fieldValue) ?
                            ! isEmpty(originalValue) :
                            ! fieldValue.equals(originalValue);
        return isDirty;
    }

    boolean isDirty(T original);

}
