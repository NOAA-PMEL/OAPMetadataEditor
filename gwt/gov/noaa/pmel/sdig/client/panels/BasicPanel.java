package gov.noaa.pmel.sdig.client.panels;

import gov.noaa.pmel.sdig.shared.bean.DbItem;

public abstract class BasicPanel <T extends DbItem> extends FormPanel<T> {

    BasicPanel(String panelName) {
        super(panelName);
    }
}
