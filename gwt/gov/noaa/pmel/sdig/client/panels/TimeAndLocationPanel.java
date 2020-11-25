package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
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
public class TimeAndLocationPanel extends FormPanel<TimeAndLocation> implements GetsDirty<TimeAndLocation> {

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

    String type = Constants.SECTION_TIMEANDLOCATION;

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    interface TimeAndLocationUiBinder extends UiBinder<HTMLPanel, TimeAndLocationPanel> {
    }

    private static TimeAndLocationUiBinder ourUiBinder = GWT.create(TimeAndLocationUiBinder.class);

    public TimeAndLocationPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void reset() {
        form.reset();
    }

    private static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private Date tryToReadDate(String dateStr) throws Exception {
        Date d = null;
        String[] parts = dateStr.split("[/ -]");
//        if ( parts.length == 3) {
//            d = new Date(Integer.parseInt(parts[0]) - 1900, Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]));
//        } else {
        try {
            d = new Date(Integer.parseInt(parts[0]) - 1900, Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]));
        } catch (Exception e1) {
            GWT.log(e1.toString());
            try {
                d = new Date(dateStr);
            } catch (Exception e2) {
                GWT.log(e2.toString());
                DateTimeFormat dtf = DateTimeFormat.getFormat(ISO8601_FORMAT);
                d = dtf.parse(dateStr);
            }
        }
        return d;
    }

    public void show(TimeAndLocation timeAndLocation) {
        setDbItem(timeAndLocation);
        // TODO use joda and store an ISO string on both get and show
        if (timeAndLocation.getStartDate() != null && timeAndLocation.getStartDate().length() > 0) {
            try {
                Date d = tryToReadDate(timeAndLocation.getStartDate());
                startDate.setValue(d);
            } catch (Exception e) {
                GWT.log(e.toString());
                Window.alert("Could not convert start date string: " + timeAndLocation.getStartDate());
            }

        }
        if (timeAndLocation.getEndDate() != null && timeAndLocation.getEndDate().length() > 0) {
            try {
                Date d = tryToReadDate(timeAndLocation.getEndDate());
                endDate.setValue(d);
            } catch (Exception e) {
                GWT.log(e.toString());
                Window.alert("Could not convert end date string:" + timeAndLocation.getEndDate());
            }
        }
        if (timeAndLocation.getNorthLat() != null) {
            northLat.setText(timeAndLocation.getNorthLat());
        }
        if (timeAndLocation.getSpatialRef() != null) {
            spatialRef.setText(timeAndLocation.getSpatialRef());
        }
        if (timeAndLocation.getWestLon() != null) {
            westLon.setText(timeAndLocation.getWestLon());
        }
        if (timeAndLocation.getEastLon() != null) {
            eastLon.setText(timeAndLocation.getEastLon());
        }
        if (timeAndLocation.getGeoNames() != null) {
            geoNames.setText(timeAndLocation.getGeoNames());
        }
        if (timeAndLocation.getSouthLat() != null) {
            southLat.setText(timeAndLocation.getSouthLat());
        }
        if (timeAndLocation.getOrganismLoc() != null) {
            organismLoc.setText(timeAndLocation.getOrganismLoc());
        }
    }

    public TimeAndLocation getTimeAndLocation() {
        TimeAndLocation timeAndLocation = dbItem != null ? (TimeAndLocation) dbItem : new TimeAndLocation();
        timeAndLocation.setEastLon(eastLon.getText().trim());
        String end = endDate.getTextBox().getValue();
        timeAndLocation.setEndDate(end != null ? end.toString() : null);
        String start = startDate.getTextBox().getValue();
        timeAndLocation.setStartDate(start != null ? start.toString() : null);
        timeAndLocation.setGeoNames(geoNames.getText().trim());
        timeAndLocation.setNorthLat(northLat.getText().trim());
        timeAndLocation.setOrganismLoc(organismLoc.getText().trim());
        timeAndLocation.setSouthLat(southLat.getText().trim());
        timeAndLocation.setSpatialRef(spatialRef.getText().trim());
        timeAndLocation.setWestLon(westLon.getText().trim());
        return timeAndLocation;
    }

    private boolean isDirty(HasValue<Date> valueField, String original) {
        boolean isDirty = false;
        Date fieldValue = valueField.getValue();
        String originalValue = original != null ? original.trim() : "";
        isDirty = fieldValue == null ?
                !isEmpty(originalValue) :
                !originalValue.equals(String.valueOf(fieldValue));
        return isDirty;
    }

    public boolean isDirty(TimeAndLocation original) {
        OAPMetadataEditor.debugLog("TimeAndLocation.isDirty(" + original + ")");
        boolean isDirty =
                original == null ?
                        this.isDirty() :
                        isDirty(eastLon, original.getEastLon()) ||
                                isDirty(endDate, original.getEndDate()) ||
                                isDirty(startDate, original.getStartDate()) ||
                                isDirty(geoNames, original.getGeoNames()) ||
                                isDirty(northLat, original.getNorthLat()) ||
                                isDirty(organismLoc, original.getOrganismLoc()) ||
                                isDirty(southLat, original.getSouthLat()) ||
                                isDirty(spatialRef, original.getSpatialRef()) ||
                                isDirty(westLon, original.getWestLon());
        return isDirty;
    }

    public boolean isDirty() {
        if (eastLon.getText().trim() != null && !eastLon.getText().isEmpty()) {
            return true;
        }
        if (endDate.getValue() != null && !endDate.getValue().toString().isEmpty()) {
            return true;
        }
        if (startDate.getValue() != null && !startDate.getValue().toString().isEmpty()) {
            return true;
        }
        if (geoNames.getText().trim() != null && !geoNames.getText().isEmpty()) {
            return true;
        }
        if (northLat.getText().trim() != null && !northLat.getText().isEmpty()) {
            return true;
        }
        if (organismLoc.getText().trim() != null && !organismLoc.getText().isEmpty()) {
            return true;
        }
        if (southLat.getText().trim() != null && !southLat.getText().isEmpty()) {
            return true;
        }
        if (spatialRef.getText().trim() != null && !spatialRef.getText().isEmpty()) {
            return true;
        }
        if (westLon.getText().trim() != null && !westLon.getText().isEmpty()) {
            return true;
        }
        return false;
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