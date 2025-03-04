package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.ConfirmClearFormCallback;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.DbItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Row;

public abstract class FormPanel <T extends DbItem>
        extends Composite
        implements GetsDirty {
    @UiField
    protected Form form;

    @UiField
    protected Button save;
    @UiField
    protected Button clear;

    private DbItem dbItem;

    protected FormPanel formPanelInstance;

    String panelName;

    public boolean isDirty() {
        return isDirty(getDbItem());
    }
    abstract void show(T item);
    abstract boolean hasContent();
    abstract boolean isDirty(T item);
    public void clear() {
        form.reset();
    }
    public void reset() {
        reset(false);
    }
    public void reset(boolean clearIds) {
        clear();
        if ( clearIds ) {
            dbItem = null;
        }
    }

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    private FormPanel() {
        formPanelInstance = this;
    }

    protected FormPanel(String panelName) {
        this();
        this.panelName = panelName;
    }

    public T getDbItem() {
        return (T)dbItem;
    }
    public void setDbItem(T item) {
        dbItem = item;
    }

    protected ClickHandler clearIt = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            if (hasContent()) {
                OAPMetadataEditor.ask("Clear Form?", "This will clear all data from this page.",
                        new ConfirmClearFormCallback(formPanelInstance));
            }
        }
    };

    protected Widget getRowWidget(Row row, int colIdx, int wgtIdx) {
        Widget clmWidget = row.getWidget(colIdx);
        org.gwtbootstrap3.client.ui.Column clm = (org.gwtbootstrap3.client.ui.Column)clmWidget;
        Widget formWidget = clm.getWidget(0);
        FormGroup form = (FormGroup)formWidget;
        GWT.log("form:"+String.valueOf(form != null));
        Widget widget = form.getWidget(wgtIdx);
        GWT.log("widget:"+widget);
        return widget;
    }
    protected FormGroup getFormGroup(Widget widget) {
        return (FormGroup) widget.getParent(); // XXX TODO: not always!
    }

    public void setFieldError(boolean set, Widget fieldForm) {
        FormGroup group = getFormGroup(fieldForm);
        if ( set )
            group.addStyleName("has-error");
        else
            group.removeStyleName("has-error");
    }
    public void setDropButtonError(boolean set, ButtonDropDown button) {
        Button fieldButton = button.getButton();
        FormGroup buttonFormGroup = getFormGroup(button);
        if (set) {
            buttonFormGroup.addStyleName("has-error");
             fieldButton.addStyleName("error-border");
        } else {
            buttonFormGroup.removeStyleName("has-error");
             fieldButton.removeStyleName("error-border");
        }
    }
    public boolean validateForm() {
        String valid = String.valueOf(form.validate());
        return !valid.equals("false") &&
                !valid.equals("0");
    }
    public boolean isValid() {
        // For some reason this returns a "0" in debug mode.
        GWT.log("FormPanel validate form:"+panelName);
        if ( form == null ) {
            OAPMetadataEditor.logToConsole("Form undefined!");
            OAPMetadataEditor.dumpStackTrace(new Exception("Form undefined"), 6);
            return true; // XXX ???
        }
        String valid = String.valueOf(form.validate(true));
        return !(valid.equals("false") || valid.equals("0"));
    }

}
