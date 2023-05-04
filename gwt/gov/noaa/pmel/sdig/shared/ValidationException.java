package gov.noaa.pmel.sdig.shared;

import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.panels.FormPanel;

public class ValidationException extends IllegalStateException {
    String section = null;
    transient FormPanel<?> panel = null;

    public ValidationException() { super(); }
    public ValidationException(String message) { super(message);}
    public ValidationException(String message, String section) {
        this(message);
        this.section = section;
    }
    public ValidationException(Exception ex, FormPanel<?> panel) {
        super(ex);
        this.panel = panel;
    }

    FormPanel<?> getPanel() {
        if ( panel != null && section != null ) {
            panel = OAPMetadataEditor.getSectionPanel(section);
        }
        return panel;
    }
}
