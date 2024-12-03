package gov.noaa.pmel.sdig.client.panels;

import gov.noaa.pmel.sdig.shared.bean.DbItem;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;

public abstract class BasicPanel <T extends DbItem> extends FormPanel<T> {

    BasicPanel(String panelName) {
        super(panelName);
    }

}
