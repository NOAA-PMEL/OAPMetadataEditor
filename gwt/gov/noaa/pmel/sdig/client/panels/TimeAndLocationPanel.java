package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DefaultDateTimeFormatInfo;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.shared.bean.TimeAndLocation;
import org.gwtbootstrap3.client.ui.*;
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
    FormGroup organismForm;

    @UiField
    Button save;


    // new fields for 14.3.1
    @UiField
    TextBox siteSpecificLon;
    @UiField
    TextBox siteSpecificLat;

    // modified for 14.3.1
    @UiField
    Modal startDatePopover;
    @UiField
    FormLabel startDateLabel;
    @UiField
    Modal endDatePopover;
    @UiField
    FormLabel endDateLabel;

    @UiField
    FormLabel westLonLabel;
    @UiField
    Modal westLonPopover;

    @UiField
    FormLabel eastLonLabel;
    @UiField
    Modal eastLonPopover;

    @UiField
    FormLabel northLatLabel;
    @UiField
    Modal northLatPopover;

    @UiField
    FormLabel southLatLabel;
    @UiField
    Modal southLatPopover;

    // assume it is true for now
//    boolean isSocat = false;


    String type = Constants.SECTION_TIMEANDLOCATION;

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    interface TimeAndLocationUiBinder extends UiBinder<HTMLPanel, TimeAndLocationPanel> {
    }

    private static TimeAndLocationUiBinder ourUiBinder = GWT.create(TimeAndLocationUiBinder.class);

    public TimeAndLocationPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
//        GWT.log("gwt.T&LPanel isSocat = " + isSocat);
//        OAPMetadataEditor.debugLog("debug.T&LPanel isSocat = " + isSocat );

        if (OAPMetadataEditor.getIsSocatParam()) {
            organismForm.setVisible(false);
            startDatePopover.setTitle("11.1 Start date of the first measurement (e.g. 2001-02-25). Please use ISO 8601 date format and if applicable time format (YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS).");
            startDateLabel.setText("First day of measurement included in data file");
            startDate.getElement().setAttribute("placeHolder", "YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");

            endDatePopover.setTitle("11.2 End date of the last measurement (e.g. 2002-05-16). Please use ISO 8601 date format and if applicable time format (YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS).");
            endDateLabel.setText("Last day of measurement included in data file");
            endDate.getElement().setAttribute("placeHolder", "YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");

            westLonPopover.setTitle("12.3 Westernmost longitude of the sampling (decimal degrees, negative for Western Hemisphere longitude).");
            westLonLabel.setText("Transect measurement longitude westernmost");

            eastLonPopover.setTitle("12.4 Easternmost longitude of the sampling (decimal degrees, negative for Western Hemisphere longitude)");
            eastLonLabel.setText("Transect measurement longitude easternmost");

            northLatPopover.setTitle("12.5 Northernmost latitude of the sampling (decimal degrees, negative for Southern Hemisphere latitude)");
            northLatLabel.setText("Transect measurement latitude northernmost");

            southLatPopover.setTitle("12.6 Southernmost latitude of the sampling (decimal degrees, negative for Southern Hemisphere latitude)");
            southLatLabel.setText("Transect measurement latitude southernmost");
        }


    }

    public void reset() {
        form.reset();
    }

    private static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String ISODATE_FORMAT = "yyyy-MM-dd";

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
        if (OAPMetadataEditor.getIsSocatParam()) {
            startDatePopover.setTitle("11.1 Start date of the first measurement (e.g. 2001-02-25). Please use ISO 8601 date format and if applicable time format (YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS).");
            startDateLabel.setText("First day of measurement included in data file");
            startDate.getElement().setAttribute("placeHolder", "YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");

            endDatePopover.setTitle("11.2 End date of the last measurement (e.g. 2002-05-16). Please use ISO 8601 date format and if applicable time format (YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS).");
            endDateLabel.setText("Last day of measurement included in data file");
            endDate.getElement().setAttribute("placeHolder", "YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");

            westLonPopover.setTitle("12.3 Westernmost longitude of the sampling (decimal degrees, negative for Western Hemisphere longitude).");
            westLonLabel.setText("Transect measurement longitude westernmost");

            eastLonPopover.setTitle("12.4 Easternmost longitude of the sampling (decimal degrees, negative for Western Hemisphere longitude)");
            eastLonLabel.setText("Transect measurement longitude easternmost");

            northLatPopover.setTitle("12.5 Northernmost latitude of the sampling (decimal degrees, negative for Southern Hemisphere latitude)");
            northLatLabel.setText("Transect measurement latitude northernmost");

            southLatPopover.setTitle("12.6 Southernmost latitude of the sampling (decimal degrees, negative for Southern Hemisphere latitude)");
            southLatLabel.setText("Transect measurement latitude southernmost");
        }
        setDbItem(timeAndLocation);
        // TODO use joda and store an ISO string on both get and show
        if (timeAndLocation.getStartDate() != null && timeAndLocation.getStartDate().length() > 0) {

            OAPMetadataEditor.debugLog("timeAndLocation.getStartDate()" + timeAndLocation.getStartDate());

            try {
                Date d = tryToReadDate(timeAndLocation.getStartDate());

                OAPMetadataEditor.debugLog("date d" + d);

                startDate.setValue(d);

                OAPMetadataEditor.debugLog("startDate" + startDate.getTextBox().getValue());

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
        if ( timeAndLocation.isSiteLocation()) {
            if (timeAndLocation.getSiteSpecificLat() != null) {
                siteSpecificLat.setText(timeAndLocation.getSiteSpecificLat());
            }
            if (timeAndLocation.getSiteSpecificLon() != null) {
                siteSpecificLon.setText(timeAndLocation.getSiteSpecificLon());
            }
        } else {
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
        if (timeAndLocation.getSiteSpecificLon() != null) {
            siteSpecificLon.setText(timeAndLocation.getSiteSpecificLon());
        }
        if (timeAndLocation.getSiteSpecificLat() != null) {
            siteSpecificLat.setText(timeAndLocation.getSiteSpecificLat());
        }
    }

    public TimeAndLocation getTimeAndLocation() {
        TimeAndLocation timeAndLocation = dbItem != null ? (TimeAndLocation) dbItem : new TimeAndLocation();
        String end = endDate.getTextBox().getValue();
        timeAndLocation.setEndDate(end != null ? end.toString() : null);
//        if (end != null) {
//            try {
//                Date d = tryToReadDate(end);
//                timeAndLocation.setEndDate(d.toString());
//            } catch (Exception e) {
//                timeAndLocation.setEndDate(null);
//            }
//        }

        String start = startDate.getTextBox().getValue();
        timeAndLocation.setStartDate(start != null ? start.toString() : null);
//        if (start != null) {
//            try {
//                Date d = tryToReadDate(start);
//                timeAndLocation.setStartDate(d.toString());
//            } catch (Exception e) {
//                timeAndLocation.setStartDate(null);
//            }
//        }

        timeAndLocation.setSpatialRef(spatialRef.getText().trim());
        if ( timeAndLocation.isSiteLocation() || ! siteSpecificLon.getText().isEmpty()) {
            timeAndLocation.setSiteSpecificLon(siteSpecificLon.getText().trim());
            timeAndLocation.setSiteSpecificLat(siteSpecificLat.getText().trim());
            timeAndLocation.setSiteLocation(true);
        } else {
            timeAndLocation.setNorthLat(northLat.getText().trim());
            timeAndLocation.setSouthLat(southLat.getText().trim());
            timeAndLocation.setEastLon(eastLon.getText().trim());
            timeAndLocation.setWestLon(westLon.getText().trim());
        }
        timeAndLocation.setGeoNames(geoNames.getText().trim());
        timeAndLocation.setOrganismLoc(organismLoc.getText().trim());
        return timeAndLocation;
    }

//    private boolean isDirty(HasValue<Date> valueField, String original) {
//        boolean isDirty = false;
//        Date fieldValue = valueField.getValue();
//        String originalValue = original != null ? original.trim() : "";
//        isDirty = fieldValue == null ?
//                !isEmpty(originalValue) :
//                !originalValue.equals(String.valueOf(fieldValue));
//        return isDirty;
//    }

    public boolean isDirty(TimeAndLocation original) {
        OAPMetadataEditor.debugLog("TimeAndLocation.isDirty(" + original + ")");
        boolean isDirty = false;

        // #DEBUG values
        if (original != null) {
            OAPMetadataEditor.debugLog("enddate (" + dateStringFormatter(original.getEndDate()) + ", " + dateDateFormatter(endDate.getValue(), ISO8601_FORMAT) + ") >" + isDirty(dateDateFormatter(endDate.getValue(), ISO8601_FORMAT), dateStringFormatter(original.getEndDate())));
            OAPMetadataEditor.debugLog("startDate (" + dateStringFormatter(original.getStartDate()) + ", " + dateDateFormatter(startDate.getValue(), ISO8601_FORMAT) + ") >" + isDirty(dateDateFormatter(startDate.getValue(), ISO8601_FORMAT), dateStringFormatter(original.getStartDate())));
//            OAPMetadataEditor.debugLog("geoNames (" + original.getGeoNames() + ", " + geoNames.getValue().toString()+ ") >" + isDirty(geoNames, original.getGeoNames()));
//            OAPMetadataEditor.debugLog("northLat (" + original.getNorthLat() + ", " + northLat.getValue().toString()+ ") >" + isDirty(northLat, original.getNorthLat()));
//            OAPMetadataEditor.debugLog("organismLoc (" + original.getOrganismLoc() + ", " + organismLoc.getValue().toString()+ ") >" + isDirty(organismLoc, original.getOrganismLoc()));
//            OAPMetadataEditor.debugLog("southLat (" + original.getSouthLat() + ", " + southLat.getValue().toString()+ ") >" + isDirty(southLat, original.getSouthLat()));
//            OAPMetadataEditor.debugLog("spatialRef (" + original.getSpatialRef() + ", " + spatialRef.getValue().toString()+ ") >" + isDirty(spatialRef, original.getSpatialRef()));
//            OAPMetadataEditor.debugLog("westLon (" + original.getWestLon() + ", " + westLon.getValue().toString()+ ") >" + isDirty(westLon, original.getWestLon()));
//            OAPMetadataEditor.debugLog("siteSpecificLon (" + original.getSiteSpecificLon() + ", " + siteSpecificLon.getValue().toString()+ ") >" + isDirty(siteSpecificLon, original.getSiteSpecificLon()));
//            OAPMetadataEditor.debugLog("siteSpecificLat (" + original.getSiteSpecificLat() + ", " + siteSpecificLat.getValue().toString()+ ") >" + isDirty(siteSpecificLat, original.getSiteSpecificLat()));
        }

        isDirty = original == null ? hasContent() : isDirty(eastLon, original.getEastLon())
//                || isDirty(endDate.getValue().toString(), original.getEndDate())
//                || isDirty(startDate.getValue().toString(), original.getStartDate())
                || isDirty(dateDateFormatter(endDate.getValue(), ISO8601_FORMAT), dateStringFormatter(original.getEndDate()))
                || isDirty(dateDateFormatter(startDate.getValue(), ISO8601_FORMAT), dateStringFormatter(original.getStartDate()))
                || isDirty(geoNames, original.getGeoNames())
                || isDirty(northLat, original.getNorthLat())
                || isDirty(organismLoc, original.getOrganismLoc())
                || isDirty(southLat, original.getSouthLat())
                || isDirty(spatialRef, original.getSpatialRef())
                || isDirty(westLon, original.getWestLon())
                || isDirty(siteSpecificLon, original.getSiteSpecificLon())
                || isDirty(siteSpecificLat, original.getSiteSpecificLat());

        return isDirty;
    }

    private String dateStringFormatter(String datetime) {
        String isodate = "^([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$";
        RegExp isodate_regex = RegExp.compile(isodate);
        String iso8601 = "/^\\d{4}-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d(\\.\\d+)?(([+-]\\d\\d:\\d\\d)|Z)?$/i";
        RegExp iso8601_regex = RegExp.compile(iso8601);

        Date od = null;
        DefaultDateTimeFormatInfo info = new DefaultDateTimeFormatInfo();
//        DateTimeFormat dtf = new DateTimeFormat(ISO8601_FORMAT, info) {};
        String formatter = ISO8601_FORMAT;
        if (isodate_regex.test(datetime)) {
            DateTimeFormat dtf = new DateTimeFormat(ISODATE_FORMAT, info) {};
            try {
                od = dtf.parse(datetime);
            } catch (Exception e) {
                OAPMetadataEditor.debugLog(e.toString());
            }
            DateTimeFormat fmt = DateTimeFormat.getFormat(formatter);
            OAPMetadataEditor.debugLog(fmt.format(od));
            return fmt.format(od);
        }
        else if (iso8601_regex.test(datetime)) {
            DateTimeFormat dtf = new DateTimeFormat(ISO8601_FORMAT, info) {};
            try {
                od = dtf.parse(datetime);
            } catch (Exception e) {
                OAPMetadataEditor.debugLog(e.toString());
            }
            DateTimeFormat fmt = DateTimeFormat.getFormat(formatter);
            OAPMetadataEditor.debugLog(fmt.format(od));
            return fmt.format(od);
        }
        return null;
    }

    private String dateDateFormatter(Date datetime, String formatter) {
        return DateTimeFormat.getFormat(formatter).format(datetime);
    }


    public boolean hasContent() {
        OAPMetadataEditor.debugLog("@Time&LocPanel.hasContent()");
        boolean hasContent = false;

//        save.setEnabled(false);

        if (eastLon.getText().trim() != null && !eastLon.getText().isEmpty()) {
            hasContent = true;
        }
        if (endDate.getValue() != null && !endDate.getValue().toString().isEmpty()) {
            hasContent = true;
        }
        if (startDate.getValue() != null && !startDate.getValue().toString().isEmpty()) {
            hasContent = true;
        }
        if (geoNames.getText().trim() != null && !geoNames.getText().isEmpty()) {
            hasContent = true;
        }
        if (northLat.getText().trim() != null && !northLat.getText().isEmpty()) {
            hasContent = true;
        }
        if (organismLoc.getText().trim() != null && !organismLoc.getText().isEmpty()) {
            hasContent = true;
        }
        if (southLat.getText().trim() != null && !southLat.getText().isEmpty()) {
            hasContent = true;
        }
        if (spatialRef.getText().trim() != null && !spatialRef.getText().isEmpty()) {
            hasContent = true;
        }
        if (westLon.getText().trim() != null && !westLon.getText().isEmpty()) {
            hasContent = true;
        }
        if (siteSpecificLon.getText().trim() != null && !siteSpecificLon.getText().isEmpty()) {
            hasContent = true;
        }
        if (siteSpecificLat.getText().trim() != null && !siteSpecificLat.getText().isEmpty()) {
            hasContent = true;
        }

//        if (hasContent == true) {
//            save.setEnabled(true);
//        }

        OAPMetadataEditor.debugLog("@Time&LocPanel.hasContent is " + hasContent);
        return hasContent;
    }

//    public boolean isDirty() {
//        if (eastLon.getText().trim() != null && !eastLon.getText().isEmpty()) {
//            return true;
//        }
//        if (endDate.getValue() != null && !endDate.getValue().toString().isEmpty()) {
//            return true;
//        }
//        if (startDate.getValue() != null && !startDate.getValue().toString().isEmpty()) {
//            return true;
//        }
//        if (geoNames.getText().trim() != null && !geoNames.getText().isEmpty()) {
//            return true;
//        }
//        if (northLat.getText().trim() != null && !northLat.getText().isEmpty()) {
//            return true;
//        }
//        if (organismLoc.getText().trim() != null && !organismLoc.getText().isEmpty()) {
//            return true;
//        }
//        if (southLat.getText().trim() != null && !southLat.getText().isEmpty()) {
//            return true;
//        }
//        if (spatialRef.getText().trim() != null && !spatialRef.getText().isEmpty()) {
//            return true;
//        }
//        if (westLon.getText().trim() != null && !westLon.getText().isEmpty()) {
//            return true;
//        }
//        if (siteSpecificLon.getText().trim() != null && !siteSpecificLon.getText().isEmpty()) {
//            return true;
//        }
//        if (siteSpecificLat.getText().trim() != null && !siteSpecificLat.getText().isEmpty()) {
//            return true;
//        }
//        return false;
//    }

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
//    public void setIsSocat(boolean socat) {
//        OAPMetadataEditor.debugLog("TimeAndLocation.setIsSocat(" + socat + ")");
//        isSocat = socat;
//    }

}