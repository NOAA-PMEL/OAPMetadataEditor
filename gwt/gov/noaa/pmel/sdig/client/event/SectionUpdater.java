package gov.noaa.pmel.sdig.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 *
 */
public class SectionUpdater extends GwtEvent<SectionUpdaterHandler> {

    String type;

    public SectionUpdater(String type) {
        this.type = type;
    }

    public static Type<SectionUpdaterHandler> TYPE = new Type<SectionUpdaterHandler>();

//    public Type<GWTEventHandler> getAssociatedType() {
//        return TYPE;
//    }
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SectionUpdaterHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SectionUpdaterHandler handler) {
        handler.onEvent(this);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}