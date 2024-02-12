package gov.noaa.pmel.sdig.client.panels;

import gov.noaa.pmel.sdig.shared.bean.DbItem;

public interface HasDefault<T extends DbItem> {
    public T getDefault();
}
