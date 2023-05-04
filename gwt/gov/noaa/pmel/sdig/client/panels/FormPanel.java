package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import gov.noaa.pmel.sdig.client.ConfirmClearFormCallback;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.shared.bean.DbItem;
import org.gwtbootstrap3.client.ui.Form;

public abstract class FormPanel <T extends DbItem> extends Composite {
    @UiField
    protected Form form;

    DbItem dbItem;

    protected FormPanel formPanelInstance;

    String panelName;

//    abstract boolean isDirty();
    abstract boolean hasContent();
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

    protected FormPanel() {
        formPanelInstance = this;
    }

    protected FormPanel(String panelName) {
        this();
        this.panelName = panelName;
    }

    public void setDbItem(T item) {
//        if ( dbItem != null && dbItem.getId() != null ) {
//            if ( item.getId() != null && ! item.getId().equals(dbItem.getId())) {
//                OAPMetadataEditor.logToConsole("Changing incoming " + dbItem + " id from " + item.getId());
//            }
//            item.setId(dbItem.getId());
//        }
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

    public boolean valid() {
        // For some reason this returns a "0" in debug mode.
        GWT.log("form:"+form);
        String valid = String.valueOf(form.validate());
        return !(valid.equals("false") || valid.equals("0"));
    }

}
