package gov.noaa.pmel.sdig.client.panels;

import gov.noaa.pmel.sdig.shared.bean.DbItem;

import java.util.List;

public abstract class MultiPanel<T extends DbItem> extends FormPanel<T> {

    protected List<T> originals;

    protected MultiPanel() {super();}
    protected MultiPanel(String type) {super(type);}

    @Override
    public abstract boolean hasContent();
    @Override
    public abstract boolean isDirty();

    public abstract boolean isDirty(List<T> originals);
}
