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
import gov.noaa.pmel.sdig.shared.bean.Funding;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.Pagination;
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

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by rhs on 3/7/17.
 */
public class FundingPanel extends Composite implements GetsDirty<Funding> {

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    @UiField
    TextBox agencyName;
    @UiField
    TextBox title;
    @UiField
    TextBox grantNumber;

    @UiField
    CellTable fundings;
    @UiField
    Button save;
    @UiField
    Form form;
    @UiField
    Pagination fundingPagination;

    boolean showTable = true;
    boolean editing = false;
    int editIndex = -1;
    Funding displayedFunding = null;
    Funding editFunding;

    ListDataProvider<Funding> fundingListDataProvider = new ListDataProvider<>();

    private SimplePager cellTablePager = new SimplePager();

    String type = Constants.SECTION_FUNDING;

    public Funding saveFunding() {
       Funding f = getFunding();
       fundingListDataProvider.getList().add(f);
       fundingListDataProvider.flush();
       fundingPagination.rebuild(cellTablePager);
       return f;
    }

    interface FundingPanelUiBinder extends UiBinder<HTMLPanel, FundingPanel> {
    }

    private static FundingPanelUiBinder ourUiBinder = GWT.create(FundingPanelUiBinder.class);

    public FundingPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        fundings.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
        fundings.addCellPreviewHandler(new CellPreviewEvent.Handler<Funding>() {
            @Override
            public void onCellPreview(CellPreviewEvent<Funding> event) {
//                OAPMetadataEditor.logToConsole("event:"+ event.getNativeEvent().getType());
                if ( !editing && "mouseover".equals(event.getNativeEvent().getType())) {
                    show(event.getValue(), false);
                } else if ( !editing && "mouseout".equals(event.getNativeEvent().getType())) {
                    reset();
                }
            }
        });

        Column<Funding, String> edit = new Column<Funding, String>(new ButtonCell(IconType.EDIT, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL)) {
            @Override
            public String getValue(Funding object) {
                return "Edit";
            }
        };
        edit.setFieldUpdater(new FieldUpdater<Funding, String>() {
            @Override
            public void update(int index, Funding funding, String value) {
                editIndex = fundingListDataProvider.getList().indexOf(funding);
                if ( editIndex < 0 ) {
                    Window.alert("Edit failed.");
                } else {
                    show(funding, true);
                    fundingListDataProvider.getList().remove(funding);
                    fundingListDataProvider.flush();
                    fundingPagination.rebuild(cellTablePager);
                }
            }
        });
        fundings.addColumn(edit);

        // Add a text column to show the name.
        TextColumn<Funding> nameColumn = new TextColumn<Funding>() {
            @Override
            public String getValue(Funding object) {
                return object.getAgencyName();
            }
        };
        fundings.addColumn(nameColumn, "Agency Name");

        TextColumn<Funding> grantNumberColumn = new TextColumn<Funding>() {
            @Override
            public String getValue(Funding object) {
                return object.getGrantNumber();
            }
        };
        fundings.addColumn(grantNumberColumn, "Grant Number");

        Column<Funding, String> delete = new Column<Funding, String>(new ButtonCell(IconType.TRASH, ButtonType.DANGER, ButtonSize.EXTRA_SMALL)) {
            @Override
            public String getValue(Funding object) {
                return "Delete";
            }
        };

        delete.setFieldUpdater(new FieldUpdater<Funding, String>() {
            @Override
            public void update(int index, Funding funding, String value) {
                fundingListDataProvider.getList().remove(funding);
                fundingListDataProvider.flush();
                fundingPagination.rebuild(cellTablePager);
                if ( fundingListDataProvider.getList().size() == 0 ) {
                    setTableVisible(false);
                } else {
                    // hide/show the pager buttons if necessary
                    setTableVisible(true);
                }
            }
        });
        fundings.addColumn(delete);

        fundings.addRangeChangeHandler(new RangeChangeEvent.Handler() {

            @Override
            public void onRangeChange(final RangeChangeEvent event) {
                fundingPagination.rebuild(cellTablePager);
            }
        });

        cellTablePager.setDisplay(fundings);

        fundings.setPageSize(4);

        fundingListDataProvider.addDataDisplay(fundings);
    }

    public List<Funding> getFundings() {

        return fundingListDataProvider.getList();
    }
    public Funding getFunding() {
        Funding funding = displayedFunding != null ? displayedFunding : new Funding();
        funding.setAgencyName(agencyName.getText().trim());
        funding.setGrantTitle(title.getText().trim());
        funding.setGrantNumber(grantNumber.getText().trim());
        funding.setPosition(editIndex);
        return funding;
    }

    public boolean isDirty(List<Funding> originals) {
//        OAPMetadataEditor.debugLog("FundingPanel.isDirty("+originals+")");
        boolean isDirty = false;
        if ( isDirty()) {
            addCurrentFunding();
        }
        Set<Funding> thisFundings = new TreeSet<>(getFundings());
        if ( thisFundings.size() != originals.size()) {
//            OAPMetadataEditor.debugLog("FundingPanel.isDirty.size:"+thisFundings.size());
            return true;
        }
        Iterator<Funding> otherFundings = new TreeSet<>(originals).iterator();
        for ( Funding f : thisFundings ) {
            if ( !f.equals(otherFundings.next())) {
                isDirty = true;
//               OAPMetadataEditor.debugLog("FundingPanel.isDirty.other:"+f);
                break;
            }
        }
        return isDirty;
    }
    public boolean isDirty(Funding original) {
//        OAPMetadataEditor.debugLog("FundingPanel.isDirty("+original+")");
        boolean isDirty =
                isDirty( agencyName, original.getAgencyName() ) ||
                        isDirty( title, original.getGrantTitle() ) ||
                        isDirty( grantNumber, original.getGrantNumber() );
        return isDirty;
    }
    public boolean isDirty() {
//        OAPMetadataEditor.debugLog("FundingPanel.isDirty()");
        if ( agencyName.getText().trim() != null && !agencyName.getText().isEmpty() ) {
            return true;
        }
        if ( title.getText().trim() != null && !title.getText().isEmpty() ) {
            return true;
        }
        if ( grantNumber.getText().trim() != null & !grantNumber.getText().isEmpty() ) {
            return true;
        }
        return false;
    }

    private void setAllEditable(boolean editable) {
        agencyName.setEnabled(editable);
        title.setEnabled(editable);
        grantNumber.setEnabled(editable);
    }
    public void show(Funding funding, boolean editable) {
        setAllEditable(editable);
        editing = editable;
        if ( editable ) {
            displayedFunding = funding;
        } else {
            editFunding = getFunding();
        }
        show(funding);
    }

    public void show(Funding funding) {
        if ( funding == null ) {
            reset();
            return;
        }
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
    public void addFundings(List<Funding> fundingList) {
        for (int i = 0; i < fundingList.size(); i++) {
            Funding f = fundingList.get(i);
            f.setPosition(i);
            fundingListDataProvider.getList().add(f);
        }
        fundingListDataProvider.flush();
        fundingPagination.rebuild(cellTablePager);
        setTableVisible(true);
    }
    private void addFunding(Funding f) {
        if ( f == null ) { return; }
        int position = f.getPosition() >= 0 ? f.getPosition() : fundingListDataProvider.getList().size();
        f.setPosition(position);
        fundingListDataProvider.getList().add(position, f);
        fundingListDataProvider.flush();
        fundingPagination.rebuild(cellTablePager);
    }
    private void addCurrentFunding() {
        Funding f = getFunding();
        addFunding(f);
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
                addCurrentFunding();
            }
//            eventBus.fireEventFromSource(new SectionSave(getFunding(), Constants.SECTION_Funding), FundingPanel.this);
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
    public void setTableVisible(boolean v) {
        fundings.setVisible(v);
        if ( v ) {
            int page = cellTablePager.getPage();
            if (cellTablePager.getPageCount() > 1) {
                fundingPagination.setVisible(true);
                cellTablePager.setPage(page);
            } else {
                fundingPagination.setVisible(false);
            }
        }
    }
    public boolean valid() {
        String valid = String.valueOf(form.validate());
        // For some reason this returns a "0" in debug mode.
        if (valid.equals("false") ||
                valid.equals("0")) {
            return false;
        } else {
            return true;
        }
    }
    public void reset() {
        form.reset();
        displayedFunding = null;
        editIndex = -1;
        editing = false;
        if ( editFunding != null ) {
            show(editFunding);
            editFunding = null;
        }
        setAllEditable(true);
    }
    public void clearFundings() {
        fundingListDataProvider.getList().clear();
        fundingListDataProvider.flush();
        fundingPagination.rebuild(cellTablePager);
        setTableVisible(false);
    }
}