package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.RangeChangeEvent;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.oracles.CountrySuggestionOracle;
import gov.noaa.pmel.sdig.client.oracles.PlatformSuggestOracle;
import gov.noaa.pmel.sdig.client.widgets.MyButtonCell;
import gov.noaa.pmel.sdig.shared.bean.Platform;
import org.gwtbootstrap3.client.ui.SuggestBox;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.*;

/**
 * Created by rhs on 3/7/17.
 */
public class PlatformPanel extends MultiPanel<Platform> {

//    ClientFactory clientFactory = GWT.create(ClientFactory.class);
//    EventBus eventBus = clientFactory.getEventBus();

    @UiField ( provided = true )
    SuggestBox name;
    @UiField
    TextBox platformId;
    @UiField (provided = true)
    SuggestBox country;
    @UiField
    TextBox owner;
    @UiField
    TextBox platformType;

//    @UiField
//    CellTable cellTable;

//    @UiField
//    Button save;
//    @UiField
//    Button clear;

//    @UiField
//    Pagination platformPagination;


//    boolean showTable = true;
//    boolean editing = false;
//    int editIndex = -1;
    int pageSize = 4;
    Platform displayedPlatform;
    Platform editPlatform;

//    ButtonCell editButton = new ButtonCell(IconType.EDIT, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL);
//    ButtonCell deleteButton = new ButtonCell(IconType.TRASH, ButtonType.DANGER, ButtonSize.EXTRA_SMALL);

    CountrySuggestionOracle countrySuggestionOracle = new CountrySuggestionOracle();
    PlatformSuggestOracle platformSuggestionOracle = new PlatformSuggestOracle();

//    ListDataProvider<Platform> tableData = new ListDataProvider<Platform>();

//    private SimplePager cellTablePager = new SimplePager();

    String type = Constants.SECTION_PLATFORMS;

    public Platform savePlatform() {
        Platform p = getPlatform();
        tableData.getList().add(p);
        tableData.flush();
        tablePagination.rebuild(cellTablePager);
        return p;
    }

    interface PlatformPanelUiBinder extends UiBinder<HTMLPanel, PlatformPanel> {
    }

    private static PlatformPanelUiBinder ourUiBinder = GWT.create(PlatformPanelUiBinder.class);

    public PlatformPanel() {
        super(Constants.SECTION_PLATFORMS);
        name = new SuggestBox(platformSuggestionOracle);
        country = new SuggestBox(countrySuggestionOracle);
        initWidget(ourUiBinder.createAndBindUi(this));
        clear.addClickHandler(clearIt);
        cellTable.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
        cellTable.addCellPreviewHandler(new CellPreviewEvent.Handler<Platform>() {
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

        cellTable.addColumn(editColumn);
        editColumn.setCellStyleNames("text-center");

        // Add a text column to show the name.
        TextColumn<Platform> nameColumn = new TextColumn<Platform>() {
            @Override
            public String getValue(Platform object) {
                return object.getName();
            }
        };
        cellTable.addColumn(nameColumn, "Name");

        TextColumn<Platform> platformTypeColumn = new TextColumn<Platform>() {
            @Override
            public String getValue(Platform object) {
                return object.getPlatformType();
            }
        };
        cellTable.addColumn(platformTypeColumn, "Platform Type");

        TextColumn<Platform> platformIdColumn = new TextColumn<Platform>() {
            @Override
            public String getValue(Platform object) {
                return object.getPlatformId();
            }
        };
        cellTable.addColumn(platformIdColumn, "Platform ID");

        cellTable.addColumn(deleteColumn);
        deleteColumn.setCellStyleNames("text-center");

        // set RowStyles on required fields
//        cellTable.setRowStyles(new RowStyles<Platform>() {
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

        cellTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {

            @Override
            public void onRangeChange(final RangeChangeEvent event) {
                tablePagination.rebuild(cellTablePager);
            }
        });

        cellTablePager.setDisplay(cellTable);

        cellTable.setPageSize(pageSize);

        tableData.addDataDisplay(cellTable);

        save.setEnabled(false);
    }

    public Platform getPlatform() {

        Platform platform = displayedPlatform != null ? displayedPlatform : new Platform();
        platform.setCountry(country.getText().trim());
        platform.setName(name.getText().trim());
        platform.setOwner(owner.getText().trim());
        platform.setPlatformId(platformId.getText().trim());
        platform.setPlatformType(platformType.getText().trim());
        platform.setPosition(editIndex);
        return platform;
    }

    public boolean isDirty() {
        return isDirty(originalList);
    }
    public boolean isDirty(List<Platform> originals) {
        OAPMetadataEditor.debugLog("PlatformPanel.isDirty:"+originals);
        boolean isDirty = false;
        if ( hasContent()) {
            addCurrentPlatform();
        }
        if ( originals == null || originals.isEmpty()) {
            return ! getPlatforms().isEmpty();
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
                        isDirty(platformId, original.getPlatformId() ) ||
                        isDirty(platformType, original.getPlatformType() );
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
        if (platformType.getText().trim() != null && !platformType.getText().isEmpty() ) {
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
        platformType.setEnabled(editable);
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
            platformType.setText(platform.getPlatformType());
        }
    }

//    private void addPlatform(Platform p ) {
    public void addPlatform(Platform p ) {
        if ( p == null ) { return; }
        int position = p.getPosition() >= 0 ? p.getPosition() : tableData.getList().size();
        p.setPosition(position);
        tableData.getList().add(position, p); //add position
        tableData.flush();
        tablePagination.rebuild(cellTablePager);
    }
    private void addCurrentPlatform() {
        Platform p = getPlatform();
        addPlatform(p);
        setTableVisible(true);
        reset();
    }

    @UiHandler("save")
    public void onSave(ClickEvent clickEvent) {
        GWT.log("onSave:"+clickEvent.getSource());
        doSave();
    }
    public boolean doSave() {

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
            return false;
        } else {
            this.editing = false;
            editIndex = INACTIVE;
            if ( hasContent()) {
                Platform p = getPlatform();
                if ( !p.isEditing ) {
                    OAPMetadataEditor.debugLog("addomg currentPlatform");
                    addPlatform(p);
                } else {
                    reset();
                }
                p.isEditing = false;
            }

            // check if any platform in tableData is missing required fields
//            boolean meetsRequired = true;
//            for (int i = 0; i < tableData.getList().size(); i++) {
//                Platform p = tableData.getList().get(i);
//                if (((p.getName() == null) || (p.getName().isEmpty()))) {
//                    meetsRequired = false;
//                }
//            }
            OAPMetadataEditor.debugLog("tableData size: " + tableData.getList().size());
//            if (meetsRequired == true && tableData.getList().size() > 0) {
            if (tableData.getList().size() > 0) {
                OAPMetadataEditor.debugLog("bus event section save platform");
                eventBus.fireEventFromSource(new SectionSave(getPlatform(), Constants.SECTION_PLATFORMS), PlatformPanel.this);
            }

            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.SUCCESS);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.COMPLETE, settings);
            if ( showTable && tableData.getList().size() > 0) {
                OAPMetadataEditor.debugLog("showtable is true or tableData is > 0");
                setTableVisible(true);
                setEnableTableRowButtons(true);
                reset();
            }
            save.setEnabled(false);
        }
        return true;
    }
    public List<Platform> getPlatforms() {
        return tableData.getList();
    }

    public void addPlatforms(List<Platform> platformList) {
        originalList = platformList;
//        OAPMetadataEditor.debugLog("in addPlatforms:"+cellTableList);
//        OAPMetadataEditor.debugLog("chk1 cellTableList size: " + cellTableList.size());
        int listSize =  platformList.size();
        for (int i = 0; i < listSize; i++) {
            Platform p = platformList.get(i);
            if ( p == null) { // XXX badness
                GWT.log("Null variable at pos " + i);
                continue;
            }

            if (i > listSize) {
                OAPMetadataEditor.debugLog("List index exceeded(max: " + listSize + "): " + i);
                break;
            }

            p.setPosition(i);
            tableData.getList().add(p);
        }
//        OAPMetadataEditor.debugLog("chk2 cellTableList size: " + cellTableList.size());
        tableData.flush();
        tablePagination.rebuild(cellTablePager);
        setTableVisible(true);
    }

    public void setTableVisible(boolean v) {
        cellTable.setVisible(v);
        if ( v ) {
            int page = cellTablePager.getPage();
            if (cellTablePager.getPageCount() > 1) {
                tablePagination.setVisible(true);
                cellTablePager.setPage(page);
            } else {
                tablePagination.setVisible(false);
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
        tableData.getList().clear();
        tableData.flush();
        tablePagination.rebuild(cellTablePager);
        setEnableTableRowButtons(true);
        setTableVisible(false); //add
    }

    @UiHandler({"platformId","owner", "platformType"})
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
        for (int i = 0; i < tableData.getList().size(); i++) {
            setEnableButton(editButton, b);
            setEnableButton(deleteButton, b);
        }
        cellTable.redraw();
    }

    public void setEnableButton(MyButtonCell button, boolean enabled) {
        if (enabled) {
            button.setEnabled(true);
        }
        else {
            button.setEnabled(false);
        }
    }


}
