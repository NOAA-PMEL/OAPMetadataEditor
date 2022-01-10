package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.RangeChangeEvent;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.event.SectionUpdater;
import gov.noaa.pmel.sdig.client.oracles.CountrySuggestionOracle;
import gov.noaa.pmel.sdig.client.oracles.PlatformSuggestOracle;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.Platform;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.*;

/**
 * Created by rhs on 3/7/17.
 */
public class PlatformPanel extends Composite implements GetsDirty<Platform> {

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    @UiField ( provided = true )
    SuggestBox name;
    @UiField
    Button showPlatformNameListButton;

    @UiField
    TextBox platformId;
    @UiField (provided = true)
    SuggestBox country;
    @UiField
    TextBox owner;
    @UiField
    ButtonDropDown platformType;

    @UiField
    CellTable platforms;

    @UiField
    Button save;
    @UiField
    Form form;

    @UiField
    Pagination platformPagination;

    @UiField
    ButtonDropDown platformIdType;

    // modified for 14.3.1
//    @UiField
//    Modal platformTypePopover;
//    @UiField
//    FormLabel platformTypeLabel;

//    TextHeader textHeader = new TextHeader("Platform Category");


    boolean showTable = true;
    boolean editing = false;
    int editIndex = -1;
    int pageSize = 4;
    Platform displayedPlatform;
    Platform editPlatform;

    ButtonCell editButton = new ButtonCell(IconType.EDIT, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL);
    ButtonCell deleteButton = new ButtonCell(IconType.TRASH, ButtonType.DANGER, ButtonSize.EXTRA_SMALL);

    CountrySuggestionOracle countrySuggestionOracle = new CountrySuggestionOracle();
    PlatformSuggestOracle platformSuggestionOracle = new PlatformSuggestOracle();

    ListDataProvider<Platform> platformsData = new ListDataProvider<Platform>();

    private SimplePager cellTablePager = new SimplePager();

    String type = Constants.SECTION_PLATFORMS;

    public Platform savePlatform() {
        Platform p = getPlatform();
        platformsData.getList().add(p);
        platformsData.flush();
        platformPagination.rebuild(cellTablePager);
        return p;
    }

    interface PlatformPanelUiBinder extends UiBinder<HTMLPanel, PlatformPanel> {
    }

    private static PlatformPanelUiBinder ourUiBinder = GWT.create(PlatformPanelUiBinder.class);

    public PlatformPanel() {
        name = new SuggestBox(platformSuggestionOracle);
        name.getValueBox().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if ( name.isSuggestionListShowing()) {
                    ((SuggestBox.DefaultSuggestionDisplay)name.getSuggestionDisplay()).hideSuggestions();
                }
            }
        });
        name.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            @Override
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
                String selectedName = name.getValue().trim();
                int sidx = selectedName.indexOf('(');
                String platformName = selectedName.substring(0, sidx);
                String platformCode = selectedName.substring(sidx+1, selectedName.length()-1);
                name.setValue(platformName.trim());
                platformId.setValue(platformCode);
                country.setFocus(true);
            }
        });
        country = new SuggestBox(countrySuggestionOracle);
        initWidget(ourUiBinder.createAndBindUi(this));

        // platformIdType
        List<String> idNames = new ArrayList<String>();
        List<String> idValues = new ArrayList<String>();
        idNames.add("ICES ");
        idValues.add("ices");
        idNames.add("IMO ");
        idValues.add("imo");
        idNames.add("WMO ");
        idValues.add("wmo");
        platformIdType.init("Select PlatformID Type ", idNames, idValues);

        // platformType (Platform category)
        List<String> catNames = new ArrayList<String>();
        List<String> catValues = new ArrayList<String>();
        idNames.add("Fixed Ocean Time Series ");
        idValues.add("fixed ocean time series");
        idNames.add("Mooring ");
        idValues.add("mooring");
        idNames.add("Coastal Monitoring Site ");
        idValues.add("coastal monitoring site");
        idNames.add("Repeat Hydrography (vessel) ");
        idValues.add("repeat hydrography (vessel)");
        idNames.add("Ship-based time series (vessel) ");
        idValues.add("ship-based time series (vessel)");
        idNames.add("Glider ");
        idValues.add("glider");
        idNames.add("Voluntary Observing Ship ");
        idValues.add("voluntary observing ship");
        platformType.init("Select Platform Category ", idNames, idValues);

        if (OAPMetadataEditor.getIsSocatParam()) {

        }

        platforms.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
        platforms.addCellPreviewHandler(new CellPreviewEvent.Handler<Platform>() {
            @Override
            public void onCellPreview(CellPreviewEvent<Platform> event) {
//                OAPMetadataEditor.logToConsole("event:"+ event.getNativeEvent().getType());
                if ( !editing && "mouseover".equals(event.getNativeEvent().getType())) {
                    show(event.getValue(), false);
                } else if ( !editing && "mouseout".equals(event.getNativeEvent().getType())) {
                    reset();
                }
            }
        });

        Column<Platform, String> edit = new Column<Platform, String>(editButton) {
            @Override
            public String getValue(Platform object) {
                return "Edit";
            }
        };
        edit.setFieldUpdater(new FieldUpdater<Platform, String>() {
            @Override
            public void update(int index, Platform platform, String value) {
                editIndex = platformsData.getList().indexOf(platform);
//                GWT.log("update " + platform + "["+index+"] at " + editIndex );
                platform.setPosition(editIndex);
                if ( editIndex < 0 ) {
                    Window.alert("Edit failed.");
                } else {
                    show(platform, true);
                    platformsData.getList().remove(platform);
                    platformsData.flush();
                    platformPagination.rebuild(cellTablePager);
                    save.setEnabled(true);
                    setEnableTableRowButtons(false);
                }
            }
        });
        platforms.addColumn(edit);
        edit.setCellStyleNames("text-center");

        // Add a text column to show the name.
        TextColumn<Platform> nameColumn = new TextColumn<Platform>() {
            @Override
            public String getValue(Platform object) {
                return object.getName();
            }
        };
        platforms.addColumn(nameColumn, "Name");

        TextColumn<Platform> platformTypeColumn = new TextColumn<Platform>() {
            @Override
            public String getValue(Platform object) {
                return object.getPlatformType();
            }
        };
        if (OAPMetadataEditor.getIsSocatParam()) {
            platforms.addColumn(platformTypeColumn, "Platform Category");
        }
        else {
            platforms.addColumn(platformTypeColumn, "Platform Type");
        }

        TextColumn<Platform> platformIdColumn = new TextColumn<Platform>() {
            @Override
            public String getValue(Platform object) {
                return object.getPlatformId();
            }
        };
        platforms.addColumn(platformIdColumn, "Platform ID");

        Column<Platform, String> delete = new Column<Platform, String>(new ButtonCell(IconType.TRASH, ButtonType.DANGER, ButtonSize.EXTRA_SMALL)) {
            @Override
            public String getValue(Platform object) {
                return "Delete";
            }
        };
        delete.setFieldUpdater(new FieldUpdater<Platform, String>() {
            @Override
            public void update(int index, Platform platform, String value) {
                form.reset(); // Because the mouseover will have filled the form
                platformsData.getList().remove(platform);
                platformsData.flush();
                platformPagination.rebuild(cellTablePager);
                if ( platformsData.getList().size() == 0 ) {
                    setTableVisible(false);
                    show(platform, true);
                    reset();
                    eventBus.fireEventFromSource(new SectionUpdater(Constants.SECTION_PLATFORMS),PlatformPanel.this);
                } else {
                    // hide/show the pager buttons if necessary
                    setTableVisible(true);
                }
            }
        });
        platforms.addColumn(delete);
        delete.setCellStyleNames("text-center");

        // set RowStyles on required fields
//        platforms.setRowStyles(new RowStyles<Platform>() {
//            @Override
//            public String getStyleNames(Platform row, int rowIndex) {
//                if (((row.getName() == null) || (row.getName().isEmpty()))) {
//                    return TableContextualType.DANGER.getCssName();
//                }
//                else {
//                    return "";
//                }
//            }
//        });

        platforms.addRangeChangeHandler(new RangeChangeEvent.Handler() {

            @Override
            public void onRangeChange(final RangeChangeEvent event) {
                platformPagination.rebuild(cellTablePager);
            }
        });

        cellTablePager.setDisplay(platforms);

        platforms.setPageSize(pageSize);

        platformsData.addDataDisplay(platforms);

        save.setEnabled(false);
    }

    public Platform getPlatform() {

        Platform platform = displayedPlatform != null ? displayedPlatform : new Platform();
        platform.setCountry(country.getText().trim());
        platform.setName(name.getText().trim());
        platform.setOwner(owner.getText().trim());
        platform.setPlatformType(platformType.getValue());
        platform.setPlatformIdType(platformIdType.getValue());
        platform.setPlatformId(platformId.getValue());

        platform.setPosition(editIndex);
        return platform;
    }

    public boolean isDirty(List<Platform> originals) {
        OAPMetadataEditor.debugLog("PlatformPanel.isDirty:"+originals);
        boolean isDirty = false;
        if ( hasContent()) {
            addCurrentPlatform();
        }
        Set<Platform> thisPlatforms = new TreeSet<>(getPlatforms());
        if ( thisPlatforms.size() != originals.size()) { return true; }
        Iterator<Platform> originalPlatforms = new TreeSet<>(originals).iterator();
        for ( Platform p : thisPlatforms ) {
            if ( !p.equals(originalPlatforms.next())) {
                isDirty = true;
                break;
            }
        }
        return isDirty;
    }

    public boolean isDirty(Platform original) {
        OAPMetadataEditor.debugLog("@PlatformPanel.isDirty("+original+")");
        boolean isDirty = false;
        isDirty = original == null ?
                hasContent() : //       XXX get hasBeenModified() right!
                isDirty(country, original.getCountry() ) ||
                        isDirty(name, original.getName() ) ||
                        isDirty(owner, original.getOwner() ) ||
                        isDirty(platformId, original.getPlatformId() );
        OAPMetadataEditor.debugLog("PlatformPanel.isDirty: "+isDirty);
        return isDirty;
    }

//    public boolean isDirty() {
    public boolean hasContent() {
        OAPMetadataEditor.debugLog("@PlatformPanel.hasContent()");
        boolean hasContent = false;
        save.setEnabled(false);
        if (country.getText().trim() != null && !country.getText().isEmpty() ) {
            hasContent = true;
        }
        if (name.getText().trim() != null && !name.getText().isEmpty() ) {
            hasContent = true;
        }
        if (owner.getText().trim() != null && !owner.getText().isEmpty() ) {
            hasContent = true;
        }
        if (platformId.getText().trim() != null && !platformId.getText().isEmpty() ) {
            hasContent = true;
        }
        if ( hasContent == true ) {
            save.setEnabled(true);
        }

        OAPMetadataEditor.debugLog("PlatformPanel.hasContent is " + hasContent);

        return hasContent;
    }

    private void setAllEditable(boolean editable) {
        country.setEnabled(editable);
        name.setEnabled(editable);
        owner.setEnabled(editable);
        platformId.setEnabled(editable);
    }
    public void show(Platform platform, boolean editable) {
        setAllEditable(editable);
        editing = editable;
        if ( editable ) {
            displayedPlatform = platform;
        } else {
            editPlatform = getPlatform();
        }
        show(platform);
    }

    public void show(Platform platform) {
//        if (OAPMetadataEditor.getIsSocatParam()) {
//            platformTypePopover.setTitle("14.2 Start date of the first measurement (e.g. 2001-02-25). Please use ISO 8601 date format and if applicable time format (YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS).");
//            platformTypeLabel.setText("Platform category");
//            platformType.getElement().setAttribute("placeHolder", "Platform category");
//
//        }

        if ( platform == null ) {
            reset();
            return;
        }
        if ( platform.getCountry() != null ) {
            country.setText(platform.getCountry());
        }
        if ( platform.getName() != null ) {
            name.setText(platform.getName());
        }
        if ( platform.getOwner() != null ) {
            owner.setText(platform.getOwner());
        }
        if ( platform.getPlatformId() != null ) {
            platformId.setText(platform.getPlatformId());
        }

        if ( platform.getPlatformType() != null ) {
            platformType.setSelected(platform.getPlatformType());
        }

        if ( platform.getPlatformIdType() != null ) {
            platformIdType.setSelected(platform.getPlatformIdType());
        }

    }

//    private void addPlatform(Platform p ) {
    public void addPlatform(Platform p ) {
        if ( p == null ) { return; }
        int position = p.getPosition() >= 0 ? p.getPosition() : platformsData.getList().size();
        p.setPosition(position);
        platformsData.getList().add(position, p); //add position
        platformsData.flush();
        platformPagination.rebuild(cellTablePager);
    }
    private void addCurrentPlatform() {
        Platform p = getPlatform();
        addPlatform(p);
        setTableVisible(true);
        reset();
    }

    @UiHandler("showPlatformNameListButton")
    public void onClick(ClickEvent clickEvent) { name.showSuggestionList(); }

    @UiHandler("save")
    public void onSave(ClickEvent clickEvent) {

        // For some reason this returns a "0" in debug mode.
        String valid = String.valueOf( form.validate());
        String warning = Constants.NOT_COMPLETE;
        NotifyType type = NotifyType.WARNING;

        if ( valid.equals("false") ||
                valid.equals("0")) {
            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(type);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(warning, settings);
        } else {
//            if ( isDirty()) {
            if ( hasContent()) {
                OAPMetadataEditor.debugLog("addomg currentPlatform");
                addCurrentPlatform();
            }

            // check if any platform in platformsData is missing required fields
//            boolean meetsRequired = true;
//            for (int i = 0; i < platformsData.getList().size(); i++) {
//                Platform p = platformsData.getList().get(i);
//                if (((p.getName() == null) || (p.getName().isEmpty()))) {
//                    meetsRequired = false;
//                }
//            }
            OAPMetadataEditor.debugLog("platformsData size: " + platformsData.getList().size());
//            if (meetsRequired == true && platformsData.getList().size() > 0) {
            if (platformsData.getList().size() > 0) {
                OAPMetadataEditor.debugLog("bus event section save platform");
                eventBus.fireEventFromSource(new SectionSave(getPlatform(), Constants.SECTION_PLATFORMS), PlatformPanel.this);
            }

            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.SUCCESS);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.COMPLETE, settings);
            if ( showTable && platformsData.getList().size() > 0) {
                OAPMetadataEditor.debugLog("showtable is true or platformsData is > 0");
                setTableVisible(true);
                setEnableTableRowButtons(true);
                reset();
            }
            save.setEnabled(false);
        }
    }
    public List<Platform> getPlatforms() {
        return platformsData.getList();
    }

    public void addPlatforms(List<Platform> platformsList) {
//        OAPMetadataEditor.debugLog("in addPlatforms:"+platformsList);
//        OAPMetadataEditor.debugLog("chk1 platformsList size: " + platformsList.size());
        int listSize =  platformsList.size();
        for (int i = 0; i < listSize; i++) {
            Platform p = platformsList.get(i);
            if ( p == null) { // XXX badness
                GWT.log("Null variable at pos " + i);
                continue;
            }

            if (i > listSize) {
                OAPMetadataEditor.debugLog("List index exceeded(max: " + listSize + "): " + i);
                break;
            }

            p.setPosition(i);
            platformsData.getList().add(p);
        }
//        OAPMetadataEditor.debugLog("chk2 platformsList size: " + platformsList.size());
        platformsData.flush();
        platformPagination.rebuild(cellTablePager);
        setTableVisible(true);
    }

    public void setTableVisible(boolean v) {
//        platforms.getTableHeadElement().
//        platforms.getHeader(2).getCell().setValue("Platform Category");
        OAPMetadataEditor.debugLog("m in setTableVisible" + platforms.getHeader(2).getValue());
//        platforms.getHeader(2).setUpdater("hi");
        if (OAPMetadataEditor.getIsSocatParam() && platforms.getHeader(2).getValue() == "Platform Type") {
            platforms.removeColumn(2);
            TextColumn<Platform> platformTypeColumn = new TextColumn<Platform>() {
                @Override
                public String getValue(Platform object) {
                    return object.getPlatformType();
                }
            };
            platforms.insertColumn(2, platformTypeColumn, "Platform Category");
        }

        OAPMetadataEditor.debugLog("m in setTableVisible" + platforms.getHeader(2).getValue());
        GWT.log("g in setTableVisible" + platforms.getHeader(2));

        platforms.setVisible(v);
        if ( v ) {
            int page = cellTablePager.getPage();
            if (cellTablePager.getPageCount() > 1) {
                platformPagination.setVisible(true);
                cellTablePager.setPage(page);
            } else {
                platformPagination.setVisible(false);
            }
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

    public void reset() {
        form.reset();
        displayedPlatform = null;
        editIndex = -1;
        editing = false;
        platformIdType.reset();
        platformType.reset();
        if ( editPlatform != null ) {
            show(editPlatform);
            editPlatform = null;
        }
        setAllEditable(true);
        setEnableTableRowButtons(true);
    }
    public void setEditing(boolean isEditing) {
        editing = isEditing;
    }

    public void clearPlatforms() {
        platformsData.getList().clear();
        platformsData.flush();
        platformPagination.rebuild(cellTablePager);
        setEnableTableRowButtons(true);
        setTableVisible(false); //add
    }

    @UiHandler({"platformId","owner"})
    public void onChange(ChangeEvent event) {
        OAPMetadataEditor.debugLog("getsource: "+event.getSource());
        save.setEnabled(true);
    }
    @UiHandler({"name", "country"})
    public void onValueChange(ValueChangeEvent<String> event) {
        OAPMetadataEditor.debugLog("Here be the new value:" + event.getValue());
        save.setEnabled(true);
    }

    public void setEnableTableRowButtons(boolean b) {
        for (int i = 0; i < platformsData.getList().size(); i++) {
            setEnableButton(editButton, b);
            setEnableButton(deleteButton, b);
        }
        platforms.redraw();
    }

    public void setEnableButton(ButtonCell button, boolean enabled) {
        if (enabled) {
            button.setEnabled(true);
        }
        else {
            button.setEnabled(false);
        }
    }
//    public void setIsSocat(boolean socat) {
//        OAPMetadataEditor.debugLog("Platform.setIsSocat(" + socat + ")");
//        isSocat = socat;
//    }

}