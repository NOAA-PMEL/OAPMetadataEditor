package gov.noaa.pmel.sdig.client;

//import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.client.ui.constants.Type;
import org.gwtbootstrap3.client.ui.base.helper.EnumHelper;

import com.google.gwt.dom.client.Style;

/**
 * Contains enum constant type for missing contextual classes for gwtbootstap3 tables
 * https://getbootstrap.com/docs/3.3/css/#tables-contextual-classes
 */
public enum TableContextualType implements Type, Style.HasCssName {
    DEFAULT(""),
    ACTIVE("active"),
    SUCCESS("success"),
    INFO("info"),
    WARNING("warning"),
    DANGER("danger");

    private final String cssClass;

    private TableContextualType(final String cssClass) {
        this.cssClass = cssClass;
    }

    @Override
    public String getCssName() {
        return cssClass;
    }

    public static TableContextualType fromStyleName(final String styleName) {
        return EnumHelper.fromStyleName(styleName, TableContextualType.class, DEFAULT);
    }
}
