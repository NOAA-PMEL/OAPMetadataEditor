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
import gov.noaa.pmel.sdig.shared.bean.TimeAndLocation;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.datepicker.client.ui.DatePicker;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.Date;

/**
 * Created by rhs on 3/6/17.
 */
public class TimeAndLocationPanel extends Composite {

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    @UiField
    DatePicker startDate;
    @UiField
    DatePicker endDate;
    @UiField
    TextBox northLat;
    @UiField
    TextBox spatialRef;
    @UiField
    TextBox westLon;
    @UiField
    TextBox eastLon;
    @UiField
    TextBox geoNames;
    @UiField
    TextBox southLat;
    @UiField
    TextBox organismLoc;

    @UiField
    Button save;

    @UiField
    Form form;

    String type = Constants.SECTION_TIMEANDLOCATION;

    interface TimeAndLocationUiBinder extends UiBinder<HTMLPanel, TimeAndLocationPanel> {
    }

    private static TimeAndLocationUiBinder ourUiBinder = GWT.create(TimeAndLocationUiBinder.class);

    public TimeAndLocationPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void show(TimeAndLocation timeAndLocation) {
        // TODO use joda and store an ISO string on both get and show
        if ( timeAndLocation.getStartDate() != null ) {
            startDate.setValue(new Date(timeAndLocation.getStartDate()));
        }
        if ( timeAndLocation.getEndDate() != null ) {
            endDate.setValue(new Date(timeAndLocation.getEndDate()));
        }
        if ( timeAndLocation.getNorthLat() != null ) {
            northLat.setText(timeAndLocation.getNorthLat());
        }
        if ( timeAndLocation.getSpatialRef() != null ) {
            spatialRef.setText(timeAndLocation.getSpatialRef());
        }
        if ( timeAndLocation.getWestLon() != null ) {
            westLon.setText(timeAndLocation.getWestLon());
        }
        if ( timeAndLocation.getEastLon() != null ) {
            eastLon.setText(timeAndLocation.getEastLon());
        }
        if ( timeAndLocation.getGeoNames() != null ) {
            geoNames.setText(timeAndLocation.getEastLon());
        }
        if ( timeAndLocation.getSouthLat() != null ) {
            southLat.setText(timeAndLocation.getSouthLat());
        }
        if ( timeAndLocation.getOrganismLoc() != null ) {
            organismLoc.setText(timeAndLocation.getOrganismLoc());
        }
    }
    public TimeAndLocation getTimeAndLocation() {
        TimeAndLocation timeAndLocation = new TimeAndLocation();
        timeAndLocation.setEastLon(eastLon.getText().trim());
        // TODO manage dates with gwttime
        timeAndLocation.setEndDate(endDate.getValue().toString());
        timeAndLocation.setStartDate(startDate.getValue().toString());
        timeAndLocation.setGeoNames(geoNames.getText().trim());
        timeAndLocation.setNorthLat(northLat.getText().trim());
        timeAndLocation.setOrganismLoc(organismLoc.getText().trim());
        timeAndLocation.setSouthLat(southLat.getText().trim());
        timeAndLocation.setSpatialRef(spatialRef.getText().trim());
        timeAndLocation.setWestLon(westLon.getText().trim());
        return timeAndLocation;
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
            eventBus.fireEventFromSource(new SectionSave(getTimeAndLocation(), this.type), TimeAndLocationPanel.this);
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