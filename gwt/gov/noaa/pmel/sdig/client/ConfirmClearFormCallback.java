package gov.noaa.pmel.sdig.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import gov.noaa.pmel.sdig.client.panels.FormPanel;

public class ConfirmClearFormCallback implements AsyncCallback<Boolean> {

    private final FormPanel formPanel;

    public ConfirmClearFormCallback(FormPanel formPanel) {
        this.formPanel = formPanel;
    }
    @Override
    public void onFailure(Throwable caught) {
        OAPMetadataEditor.logToConsole("Confirm clear form fail: " + caught);
    }

    @Override
    public void onSuccess(Boolean result) {
        if ( result.booleanValue() ) {
            OAPMetadataEditor.logToConsole("Clear form confirmed.");
            formPanel.reset(); // XXX ??? clear() ?
        } else {
            OAPMetadataEditor.logToConsole("Clear form rejected.");
        }
    }
}
