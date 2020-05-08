package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
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
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.RangeChangeEvent;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.oracles.CountrySuggestionOracle;
import gov.noaa.pmel.sdig.client.oracles.PlatformSuggestOracle;
import gov.noaa.pmel.sdig.shared.bean.Platform;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.SuggestBox;
import org.gwtbootstrap3.client.ui.TextBox;
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
    TextBox platformId;
    @UiField (provided = true)
    SuggestBox country;
    @UiField
    TextBox owner;
    @UiField
    TextBox platformType;

    @UiField
    CellTable platforms;

    @UiField
    Button save;
    @UiField
    Form form;

    @UiField
    Pagination platformPagination;


    boolean showTable = true;
    boolean editing = false;
    int editIndex = -1;
    Platform displayedPlatform;
    Platform editPlatform;


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
        country = new SuggestBox(countrySuggestionOracle);
        initWidget(ourUiBinder.createAndBindUi(this));
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

        Column<Platform, String> edit = new Column<Platform, String>(new ButtonCell(IconType.EDIT, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL)) {
            @Override
            public String getValue(Platform object) {
                return "Edit";
            }
        };
        edit.setFieldUpdater(new FieldUpdater<Platform, String>() {
            @Override
            public void update(int index, Platform platform, String value) {
                editIndex = platformsData.getList().indexOf(platform);
                if ( editIndex < 0 ) {
                    Window.alert("Edit failed.");
                } else {
                    show(platform, true);
                    platformsData.getList().remove(platform);
                    platformsData.flush();
                    platformPagination.rebuild(cellTablePager);
                }
            }
        });
        platforms.addColumn(edit);

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
        platforms.addColumn(platformTypeColumn, "Platform Type");

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
                platformsData.getList().remove(platform);
                platformsData.flush();
                platformPagination.rebuild(cellTablePager);
                if ( platformsData.getList().size() == 0 ) {
                    setTableVisible(false);
                } else {
                    // hide/show the pager buttons if necessary
                    setTableVisible(true);
                }
            }
        });
        platforms.addColumn(delete);

        platforms.addRangeChangeHandler(new RangeChangeEvent.Handler() {

            @Override
            public void onRangeChange(final RangeChangeEvent event) {
                platformPagination.rebuild(cellTablePager);
            }
        });

        cellTablePager.setDisplay(platforms);

        platforms.setPageSize(4);

        platformsData.addDataDisplay(platforms);

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

    public boolean isDirty(List<Platform> originals) {
        OAPMetadataEditor.debugLog("PlatformPanel.isDirty:"+platforms);
        boolean isDirty = false;
        if ( isDirty()) {
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
        OAPMetadataEditor.debugLog("PlatformPanel.isDirty("+original+")");
        boolean isDirty =
                isDirty(country, original.getCountry() ) ||
                        isDirty(name, original.getName() ) ||
                        isDirty(owner, original.getOwner() ) ||
                        isDirty(platformId, original.getPlatformId() ) ||
                        isDirty(platformType, original.getPlatformType() );
        return isDirty;
    }
    public boolean isDirty() {
        if (country.getText().trim() != null && !country.getText().isEmpty() ) {
            return true;
        }
        if (name.getText().trim() != null && !name.getText().isEmpty() ) {
            return true;
        }
        if (owner.getText().trim() != null && !owner.getText().isEmpty() ) {
            return true;
        }
        if (platformId.getText().trim() != null && !platformId.getText().isEmpty() ) {
            return true;
        }
        if (platformType.getText().trim() != null && !platformType.getText().isEmpty() ) {
            return true;
        }
        return false;
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
    private void addPlatform(Platform p ) {
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
            if ( isDirty()) {
                addCurrentPlatform();
            }
    //            eventBus.fireEventFromSource(new SectionSave(getPlatform(), Constants.SECTION_GENERIC), GenericVariablePanel.this);
            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.SUCCESS);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.COMPLETE, settings);
            if ( showTable ) {
                setTableVisible(true);
                reset();
            }
        }
    }
    public List<Platform> getPlatforms() {
        return platformsData.getList();
    }

    public void addPlatforms(List<Platform> platformsList) {
        for (int i = 0; i < platformsList.size(); i++) {
            Platform p = platformsList.get(i);
            p.setPosition(i);
            platformsData.getList().add(p);
        }
        platformsData.flush();
        platformPagination.rebuild(cellTablePager);
        setTableVisible(true);
    }

    public void setTableVisible(boolean v) {
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
        if ( editPlatform != null ) {
            show(editPlatform);
            editPlatform = null;
        }
        setAllEditable(true);
    }

    public void clearPlatforms() {
        platformsData.getList().clear();
        platformsData.flush();
        platformPagination.rebuild(cellTablePager);
        setTableVisible(false); //add
    }
}