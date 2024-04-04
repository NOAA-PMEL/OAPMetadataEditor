package gov.noaa.pmel.sdig.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.base.HasId;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by rhs on 1/21/17.
 */
public class ButtonDropDown extends Composite
        implements HasId {

    String id;
    @UiField
    Button button;
    public Button getButton() {
        return button;
    }
    @UiField
    DropDownMenu menu;

    Map<String, String> values = new HashMap<String, String>();
    String currentValue;
    String initialValue;

//    Row formRow;
    ButtonDropDown thisButton;

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    interface ButtonDropDownUiBinder extends UiBinder<HTMLPanel, ButtonDropDown> {
    }

    private static ButtonDropDownUiBinder ourUiBinder = GWT.create(ButtonDropDownUiBinder.class);

    public ButtonDropDown() {
        initWidget(ourUiBinder.createAndBindUi(this));
        thisButton = this;
    }
    public void init(String initialValue, List<String> labels, List<String> inputValues) {
//        this.init(initialValue, labels, inputValues, null);
//    }
//    public void init(String initialValue, List<String> labels, List<String> inputValues, Row formRow) {
//        this.formRow = formRow;
        this.currentValue = "";
        this.initialValue = initialValue;
        button.setText(initialValue);
        button.setTitle(initialValue);
        for ( int i = 0; i < labels.size(); i++ ) {
            String label = labels.get(i);
            values.put(label, standardValue(inputValues.get(i)));
            final AnchorListItem a = new AnchorListItem(label);
            a.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    String label = a.getText();
                    button.setText(label);
                    currentValue = values.get(label);
                    thisButton.getButton().removeStyleName("error-border");
                }
            });
            menu.add(a);
        }
    }

    public static String standardValue(String value) {
        if ( value == null || value.trim().isEmpty()) {
            return "";
        }
        String stdValue = value.trim().replaceAll("[ _]", "").toUpperCase();
        return stdValue;
    }
    public String getValue() {
        return currentValue;
    }
    public boolean setSelected(String value) {
        boolean set = false;
        if ( value == null || value.trim().isEmpty()) {
            OAPMetadataEditor.logToConsole("Empty or null RID type value");
            return false;
        }
        String ridValue = standardValue(value);
        Set<String> keys = values.keySet();
        for (Iterator kIt = keys.iterator(); kIt.hasNext(); ) {
            String key = (String) kIt.next();
            String v = values.get(key);
            if ( v.equals(ridValue) ) {
                button.setText(key);
                currentValue = v;
                set = true;
                break;
            }
        }
        if ( !set ) {
            OAPMetadataEditor.logToConsole("Failed to set " + this.initialValue + " dropbutton for value \"" + value + "\"");
            currentValue = "";
//            reset();
        }
        return set;
    }

    public  void reset() {
        button.setText(initialValue);
        currentValue = "";
    }
}