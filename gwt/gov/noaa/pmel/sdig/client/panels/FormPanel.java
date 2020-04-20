package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.shared.bean.DbItem;
import org.gwtbootstrap3.client.ui.Form;

public abstract class FormPanel <T extends DbItem> extends Composite {
    @UiField
    Form form;

    DbItem dbItem;

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

    public void setDbItem(T item) {
//        if ( dbItem != null && dbItem.getId() != null ) {
//            if ( item.getId() != null && ! item.getId().equals(dbItem.getId())) {
//                OAPMetadataEditor.logToConsole("Changing incoming " + dbItem + " id from " + item.getId());
//            }
//            item.setId(dbItem.getId());
//        }
        dbItem = item;
    }
}
