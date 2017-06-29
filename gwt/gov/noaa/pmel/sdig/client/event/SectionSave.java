package gov.noaa.pmel.sdig.client.event;

import com.google.gwt.event.shared.GwtEvent;
import gov.noaa.pmel.sdig.shared.bean.Person;

/**
 * Created by rhs on 2/28/17.
 */
public class SectionSave extends GwtEvent<SectionSaveHandler> {

    Object sectionContents;
    String type;

    public SectionSave(Object contents, String type) {
        this.type = type;
        this.sectionContents = contents;
    }

    public static Type<SectionSaveHandler> TYPE = new Type<SectionSaveHandler>();

    public Type<SectionSaveHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(SectionSaveHandler handler) {
        handler.onSectionSave(this);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getSectionContents() {
        return sectionContents;
    }

    public void setSectionContents(Object sectionContents) {
        this.sectionContents = sectionContents;
    }
}
