package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.shared.bean.Funding;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

/**
 * Created by rhs on 3/7/17.
 */
public class FundingPanel extends Composite {
    @UiField
    TextBox agencyName;
    @UiField
    TextBox title;
    @UiField
    TextBox grantNumber;

    @UiField
    Button save;

    @UiField
    Form form;

    String type = Constants.SECTION_FUNDING;

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    public void reset() {
        form.reset();
    }

    interface FundingPanelUiBinder extends UiBinder<HTMLPanel, FundingPanel> {
    }

    private static FundingPanelUiBinder ourUiBinder = GWT.create(FundingPanelUiBinder.class);

    public FundingPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void show(Funding funding) {
        if ( funding.getAgencyName() != null ) {
            agencyName.setText(funding.getAgencyName());
        }
        if ( funding.getGrantTitle() != null ) {
            title.setText(funding.getGrantTitle());
        }
        if ( funding.getGrantNumber() != null ) {
            grantNumber.setText(funding.getGrantNumber());
        }
    }

    public Funding getFunding() {
        Funding funding = new Funding();
        funding.setAgencyName(agencyName.getText().trim());
        funding.setGrantTitle(title.getText().trim());
        funding.setGrantNumber(grantNumber.getText().trim());
        return funding;
    }
    @UiHandler("save")
    public void onSave(ClickEvent clickEvent) {

        // For some reason this returns a "0" in debug mode.
        String valid = String.valueOf(form.validate());
        if (valid.equals("false") || valid.equals("0")) {
            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.WARNING);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.NOT_COMPLETE, settings);
        } else {
            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.SUCCESS);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.COMPLETE, settings);
            eventBus.fireEventFromSource(new SectionSave(getFunding(), this.type), FundingPanel.this);
        }
    }
    public boolean valid() {
        String valid = String.valueOf(form.validate());
        if (valid.equals("false") ||
                valid.equals("0")) {
            return false;
        } else {
            return true;
        }
    }
}