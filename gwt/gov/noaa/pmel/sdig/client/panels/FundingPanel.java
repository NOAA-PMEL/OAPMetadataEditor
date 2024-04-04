package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.RangeChangeEvent;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.event.SectionUpdater;
import gov.noaa.pmel.sdig.client.oracles.FundingSuggestOracle;
import gov.noaa.pmel.sdig.client.widgets.MyButtonCell;
import gov.noaa.pmel.sdig.shared.bean.Funding;
import org.fusesource.restygwt.client.*;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by rhs on 3/7/17.
 */
public class FundingPanel extends MultiPanel<Funding> {

//    ClientFactory clientFactory = GWT.create(ClientFactory.class);
//    EventBus eventBus = clientFactory.getEventBus();

    FundingSuggestOracle fundingSuggestOracle = new FundingSuggestOracle();

    public interface FundingCodec extends JsonEncoderDecoder<Funding> {
    }

    public interface GetFundingService extends RestService {
        @Path("{id}")
        public void get(@PathParam("id") String grant, TextCallback textCallback);
    }

    Resource getFundingResource = new Resource(Constants.getFunding);
    GetFundingService getFundingService = GWT.create(GetFundingService.class);

    FundingCodec codec = GWT.create(FundingCodec.class);

//    List<Funding> originalList = null;

    @UiField
    TextBox agencyName;
    @UiField
    TextBox title;
    @UiField(provided = true)
    SuggestBox grantNumber;

//    @UiField
//    CellTable cellTable;
//    @UiField
//    Button save;
//    @UiField
//    Button clear;
//    @UiField
//    Pagination tablePagination;

//    boolean showTable = true;
//    boolean editing = false;
//    int editIndex = -1;
    int pageSize = 4;
    Funding displayedFunding = null;
    Funding editFunding;


//    ListDataProvider<Funding> tableData = new ListDataProvider<Funding>();

//    private SimplePager cellTablePager = new SimplePager();

    String type = Constants.SECTION_FUNDING;

    public Funding saveFunding() {
       Funding f = getFunding();
       tableData.getList().add(f);
       tableData.flush();
       tablePagination.rebuild(cellTablePager);
       return f;
    }

    interface FundingPanelUiBinder extends UiBinder<HTMLPanel, FundingPanel> {
    }

    private static FundingPanelUiBinder ourUiBinder = GWT.create(FundingPanelUiBinder.class);

    public FundingPanel() {
        super(Constants.SECTION_FUNDING);
        ((RestServiceProxy) getFundingService).setResource(getFundingResource);

        grantNumber = new SuggestBox(fundingSuggestOracle);
        grantNumber.getValueBox().addDoubleClickHandler(new DoubleClickHandler() {
            @Override
            public void onDoubleClick(DoubleClickEvent event) {
                NativeEvent nevent = event.getNativeEvent();
                String type = nevent.getType();
                grantNumber.showSuggestionList();
            }
        });
        grantNumber.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            @Override
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
                Object source = event.getSource();
                SuggestBox grantBox = grantNumber;
                String grant = grantBox.getText();
                if ( grant != null && grant.trim().length() > 0 ) {
                    lookForFundingInfo(grant);
                }
            }
        });

        initWidget(ourUiBinder.createAndBindUi(this));

        clear.addClickHandler(clearIt);

        cellTable.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
        cellTable.addCellPreviewHandler(new CellPreviewEvent.Handler<Funding>() {
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

        cellTable.addColumn(editColumn);
        editColumn.setCellStyleNames("text-center");

        // Add a text column to show the name.
        TextColumn<Funding> nameColumn = new TextColumn<Funding>() {
            @Override
            public String getValue(Funding object) {
                return object.getAgencyName();
            }
        };
        cellTable.addColumn(nameColumn, "Agency Name");

        TextColumn<Funding> grantNumberColumn = new TextColumn<Funding>() {
            @Override
            public String getValue(Funding object) {
                return object.getGrantNumber();
            }
        };
        cellTable.addColumn(grantNumberColumn, "Grant Number");

        cellTable.addColumn(deleteColumn);
        deleteColumn.setCellStyleNames("text-center");

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

//        grantNumber.addValueChangeHandler()
    }

    public static native String encode(String grant) /*-{
        return grant.replace(/\./g, "%2E" );
    }-*/;


    private void lookForFundingInfo(String grantId) {
        GWT.log("looking for grant info for " + grantId);
        if ( grantId != null ) {
            grantId = grantId.trim();
            grantId = encode(grantId);
            GWT.log("query encoded grant id " + grantId);
            getFundingService.get(grantId, new TextCallback() {
                @Override
                public void onFailure(Method method, Throwable exception) {
                    GWT.log("get funding failed:"+exception);
                    agencyName.setText("");
                    title.setText("");
                }

                @Override
                public void onSuccess(Method method, String response) {
                    GWT.log("funding response: "+ response);
                    if ( response != null ) {
                        response = response.trim();
                        if ( ! Constants.NOT_FOUND.equals(response)) {
                            Funding funding = codec.decode(response);
                            save.setEnabled(true);
                            show(funding);
                        } else {
                            agencyName.setText("");
                            title.setText("");
                        }
                    } else {
                        agencyName.setText("");
                        title.setText("");
                    }
                }
            });
        }
    }

    public List<Funding> getFundings() { return tableData.getList(); }

    public Funding getFunding() {
        Funding funding = displayedFunding != null ? displayedFunding : new Funding();
        funding.setAgencyName(agencyName.getText().trim());
        funding.setGrantTitle(title.getText().trim());
        funding.setGrantNumber(grantNumber.getText().trim());
        funding.setPosition(editIndex);
        return funding;
    }

    public boolean isDirty() {
        return isDirty(originalList);
    }
    public boolean isDirty(List<Funding> originals) {
        boolean isDirty = false;
        if (hasContent()) {
            addCurrentFunding();
        }
        if ( originals == null || originals.isEmpty()) {
            return ! getFundings().isEmpty();
        }
        Set<Funding>thisFundings = new TreeSet<>(getFundings());
        if (thisFundings.size() != originals.size()) { return true; }
        Iterator<Funding>otherFundings = new TreeSet<>(originals).iterator();
        for (Funding f : thisFundings) {
            if (!f.equals(otherFundings.next())) {
                isDirty = true;
                break;
            }
        }
        return isDirty;
    }
    public boolean isDirty(Funding original) {
        OAPMetadataEditor.debugLog("@FundingPanel.isDirty(" + original + ")");
        boolean isDirty = false;
        isDirty = original == null ? hasContent() :
                isDirty(agencyName, original.getAgencyName())
                        || isDirty(title, original.getGrantTitle())
                        || isDirty(grantNumber, original.getGrantNumber());
        OAPMetadataEditor.debugLog("FundingPanel.isDirty: " + isDirty);
        return isDirty;
    }
    public boolean hasContent() {
        OAPMetadataEditor.debugLog("@FundingPanel.hasContent()");
        boolean hasContent = false;
        save.setEnabled(false);

        if (agencyName.getText().trim() != null && !agencyName.getText().isEmpty()) {
            hasContent = true;
        }
        if (title.getText().trim() != null && !title.getText().isEmpty()) {
            hasContent = true;
        }
        if (grantNumber.getText().trim() != null & !grantNumber.getText().isEmpty()) {
            hasContent = true;
        }
        if (hasContent == true) {
            save.setEnabled(true);
        }
        OAPMetadataEditor.debugLog("FundingPanel.hasContent is " + hasContent);
        return hasContent;
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
        originalList = fundingList;
        for (int i = 0; i < fundingList.size(); i++) {
            Funding f = fundingList.get(i);
            f.setPosition(i);
            tableData.getList().add(f);
        }
        tableData.flush();
        tablePagination.rebuild(cellTablePager);
        setTableVisible(true);
    }
//    private void addFunding(Funding f) {
    public void addFunding(Funding f) {
        if ( f == null ) { return; }
        int position = f.getPosition() >= 0 ? f.getPosition() : tableData.getList().size();
        f.setPosition(position);
        tableData.getList().add(position, f);
        tableData.flush();
        tablePagination.rebuild(cellTablePager);
    }
    private void addCurrentFunding() {
        Funding f = getFunding();
        addFunding(f);
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
            if ( hasContent() ) {
                Funding f = getFunding();
                if ( !f.isEditing ) {
                    addCurrentFunding();
                } else {
                    reset();
                }
                f.isEditing = false;
            }
//            eventBus.fireEventFromSource(new SectionSave(getFunding(), Constants.SECTION_Funding), FundingPanel.this);
            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.SUCCESS);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.COMPLETE, settings);
            if ( showTable && tableData.getList().size() > 0) {
                setTableVisible(true);
                setEnableTableRowButtons(true);
                reset();
            }
            save.setEnabled(false);
        }
        return true;
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
        setEnableTableRowButtons(true);
    }
    public void setEditing(boolean isEditing) {
        editing = isEditing;
    }
    public void clearFundings() {
        tableData.getList().clear();
        tableData.flush();
        tablePagination.rebuild(cellTablePager);
        setEnableTableRowButtons(true);
        setTableVisible(false);
    }

    @UiHandler({"agencyName","title"})// , "grantNumber"})
    public void onChange(ChangeEvent event) {
        OAPMetadataEditor.debugLog("getsource: "+event.getSource());
        save.setEnabled(true);
    }

    @UiHandler({"grantNumber"})
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
        button.setEnabled(enabled);
    }
}
