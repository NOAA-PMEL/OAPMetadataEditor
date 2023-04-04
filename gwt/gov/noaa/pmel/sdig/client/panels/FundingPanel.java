package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.*;
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
import gov.noaa.pmel.sdig.client.event.SectionUpdater;
import gov.noaa.pmel.sdig.client.oracles.FundingSuggestOracle;
import gov.noaa.pmel.sdig.shared.bean.Funding;
import org.fusesource.restygwt.client.*;
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

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.*;

/**
 * Created by rhs on 3/7/17.
 */
public class FundingPanel extends FormPanel implements GetsDirty<Funding> {

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

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

    @UiField
    TextBox agencyName;
    @UiField
    TextBox title;
    @UiField(provided = true)
    SuggestBox grantNumber;

    @UiField
    CellTable fundings;
    @UiField
    Button save;
    @UiField
    Button clear;
    @UiField
    Form form;
    @UiField
    Pagination fundingPagination;

    boolean showTable = true;
    boolean editing = false;
    int editIndex = -1;
    int pageSize = 4;
    Funding displayedFunding = null;
    Funding editFunding;

    ButtonCell editButton = new ButtonCell(IconType.EDIT, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL);
    ButtonCell deleteButton = new ButtonCell(IconType.TRASH, ButtonType.DANGER, ButtonSize.EXTRA_SMALL);

    ListDataProvider<Funding> fundingListDataProvider = new ListDataProvider<Funding>();

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

        Column<Funding, String> edit = new Column<Funding, String>(editButton) {
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
                }
                else {
                    show(funding, true);
                    fundingListDataProvider.getList().remove(funding);
                    fundingListDataProvider.flush();
                    fundingPagination.rebuild(cellTablePager);
                    save.setEnabled(true);
                    setEnableTableRowButtons(false);
                }
            }
        });
        fundings.addColumn(edit);
        edit.setCellStyleNames("text-center");

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

        Column<Funding, String> delete = new Column<Funding, String>(deleteButton) {
            @Override
            public String getValue(Funding object) {
                return "Delete";
            }
        };

        delete.setFieldUpdater(new FieldUpdater<Funding, String>() {
            @Override
            public void update(int index, Funding funding, String value) {
                form.reset(); // Because the mouseover will have filled the form
                fundingListDataProvider.getList().remove(funding);
                fundingListDataProvider.flush();
                fundingPagination.rebuild(cellTablePager);
                if ( fundingListDataProvider.getList().size() == 0 ) {
                    setTableVisible(false);
                    show(funding, true);
                    reset();
                    eventBus.fireEventFromSource(new SectionUpdater(Constants.SECTION_FUNDING),FundingPanel.this);
                } else {
                    // hide/show the pager buttons if necessary
                    setTableVisible(true);
                }
            }
        });
        fundings.addColumn(delete);
        delete.setCellStyleNames("text-center");

        fundings.addRangeChangeHandler(new RangeChangeEvent.Handler() {

            @Override
            public void onRangeChange(final RangeChangeEvent event) {
                fundingPagination.rebuild(cellTablePager);
            }
        });

        cellTablePager.setDisplay(fundings);

        fundings.setPageSize(pageSize);

        fundingListDataProvider.addDataDisplay(fundings);

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

    public List<Funding> getFundings() { return fundingListDataProvider.getList(); }

    public Funding getFunding() {
        Funding funding = displayedFunding != null ? displayedFunding : new Funding();
        funding.setAgencyName(agencyName.getText().trim());
        funding.setGrantTitle(title.getText().trim());
        funding.setGrantNumber(grantNumber.getText().trim());
        funding.setPosition(editIndex);
        return funding;
    }

    public boolean isDirty(List<Funding> originals) {
        boolean isDirty = false;
        if (hasContent()) {
            addCurrentFunding();
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
        for (int i = 0; i < fundingList.size(); i++) {
            Funding f = fundingList.get(i);
            f.setPosition(i);
            fundingListDataProvider.getList().add(f);
        }
        fundingListDataProvider.flush();
        fundingPagination.rebuild(cellTablePager);
        setTableVisible(true);
    }
//    private void addFunding(Funding f) {
    public void addFunding(Funding f) {
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
            if ( hasContent() ) {
                addCurrentFunding();
            }
//            eventBus.fireEventFromSource(new SectionSave(getFunding(), Constants.SECTION_Funding), FundingPanel.this);
            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.SUCCESS);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.COMPLETE, settings);
            if ( showTable && fundingListDataProvider.getList().size() > 0) {
                setTableVisible(true);
                setEnableTableRowButtons(true);
                reset();
            }
            save.setEnabled(false);
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
        setEnableTableRowButtons(true);
    }
    public void setEditing(boolean isEditing) {
        editing = isEditing;
    }
    public void clearFundings() {
        fundingListDataProvider.getList().clear();
        fundingListDataProvider.flush();
        fundingPagination.rebuild(cellTablePager);
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
        for (int i = 0; i < fundingListDataProvider.getList().size(); i++) {
            setEnableButton(editButton, b);
            setEnableButton(deleteButton, b);
        }
        fundings.redraw();
    }

    public void setEnableButton(ButtonCell button, boolean enabled) {
        if (enabled) {
            button.setEnabled(true);
        }
        else {
            button.setEnabled(false);
        }
    }

}