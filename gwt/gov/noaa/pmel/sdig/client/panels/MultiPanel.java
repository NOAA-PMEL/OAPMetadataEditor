package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.RangeChangeEvent;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.event.SectionUpdater;
import gov.noaa.pmel.sdig.shared.bean.Ordered;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import gov.noaa.pmel.sdig.client.widgets.MyButtonCell;

import java.util.List;

public abstract class MultiPanel<T extends Ordered> extends FormPanel<T> {

    List<T> originalList;

    @UiField
    CellTable<T> cellTable;

    @UiField
    Pagination tablePagination;

    @UiField
    Button save;
    @UiField
    Button clear;

    ListDataProvider<T> tableData = new ListDataProvider<>();
    SimplePager cellTablePager = new SimplePager();

    boolean showTable = true;
    boolean editing = false;

    T formContents = null;

    int INACTIVE = -1;

    static final String EDITING_HIGHLIGHT = "editing_highlight";
    static final String ERROR_HIGHLIST = "error_highlight";

    static final String MODE_EDIT = "Edit";
    static final String MODE_CANCEL = "Cancel";
    static final String MODE_DELETE = "Delete";
    static final String MODE_SAVE = "Save";

    int editIndex = -1;
    int cellTableRange_start = 0;

    protected boolean isActiveRow(int index) {
        return ( index == editIndex );
    }

    MyButtonCell editButton = new MyButtonCell(IconType.EDIT, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL);
    MyButtonCell deleteButton = new MyButtonCell(IconType.TRASH, ButtonType.DANGER, ButtonSize.EXTRA_SMALL);

    @Override
    public boolean isDirty() {
        return isDirty(originalList);
    }
    abstract  boolean isDirty(List<T> originals);
    abstract void show(T item, boolean editing);
//    abstract T getItem();
    public abstract boolean doSave();
    protected void setEnableButtons(boolean enable) {
        // to be overridden where appropriate
    }

    Column<T, String> editColumn;
    Column<T, String> deleteColumn;

    MultiPanel(String panelName) {
        super(panelName);

        editColumn = new Column<T, String>(editButton) {
            @Override
            public String getValue(T item) {
                return item != null ?
                        item.isEditing ?
                                MODE_CANCEL :
                                MODE_EDIT
                        : MODE_EDIT; // Shouldn't happen.
            }
        };
        editColumn.setFieldUpdater(new FieldUpdater<T, String>() {
            @Override
            public void update(int index, T item, String value) {
                int varIdx = tableData.getList().indexOf(item);

                OAPMetadataEditor.logToConsole("update editing " + editing + " " + editIndex +
                        " item: " + item + "{"+item.isEditing + "}" +

                        "["+index+"] at " + varIdx );
                item.setPosition(editIndex);
                if ( index < 0 ) {
                    Window.alert("Edit failed.");
                } else if ( editing && ! isActiveRow(index)) {
                    // TODO: would be good to blur() here.
                    return;
                } else if ( ! item.isEditing ) { // edit
                    editing = true;
                    editIndex = index;
                    item.isEditing = true;
                    setEnableButtons(false);
//                    deleteButton.setIcon(IconType.SAVE);
//                    deleteButton.setType(ButtonType.SUCCESS);
//                    formContents = getItem();
                    show(item, true);
                    save.setEnabled(true);
                    redrawTableAndSetHighlight(true);
//                    save.setText("SAVE VARIABLE");
                } else { // cancel
                    reset();
                    editing = false;
                    item.isEditing = false;
//                    deleteButton.setIcon(IconType.TRASH);
//                    deleteButton.setType(ButtonType.DANGER);
                    setEnableButtons(true);
//                    if ( formContents != null ) {
//                        show(formContents);
//                        formContents = null;
//                    }
                    // need to do this before unsetting editIndex
                    redrawTableAndSetHighlight(false);
                    editIndex = INACTIVE;
//                    save.setText("ADD VARIABLE");
                }
            }
        });
        deleteColumn = new Column<T, String>(deleteButton) {
            @Override
            public String getValue(T object) {
                return ( object != null ) ?
                        object.isEditing ?
                                MODE_SAVE :
                                MODE_DELETE
                        : MODE_DELETE; // shouldn't happen
            };
        };

        deleteColumn.setFieldUpdater(new FieldUpdater<T, String>() {
            @Override
            public void update(int index, T item, String value) {
                if ( editing && ! isActiveRow(index)) { return; }
                else if ( item.isEditing ) { // save item
                    editIndex = INACTIVE;
                    setEnableButtons(true);
//                    deleteButton.setIcon(IconType.TRASH);
//                    deleteButton.setType(ButtonType.DANGER);
                    doSave();
                    reset();
                } else { // delete // XXX TODO: Should we confirm?
                    form.reset(); // Because the mouseover will have filled the form
                    tableData.getList().remove(item);
                    tableData.flush();
                    tablePagination.rebuild(cellTablePager);
                    if (tableData.getList().size() == 0) {
                        setTableVisible(false);
                        reset();
                        eventBus.fireEventFromSource(new SectionUpdater(Constants.SECTION_VARIABLES), MultiPanel.this);
                    } else {
                        setTableVisible(true);
                    }
                }
            }
        });
    }

    public void setTableVisible(boolean b) {
        cellTable.setVisible(b);
        tablePagination.setVisible(b);
        if ( b ) {
            if (cellTablePager.getPageCount() > 1) {
                int page = cellTablePager.getPage();
                tablePagination.setVisible(true);
                cellTablePager.setPage(page);
            } else {
                tablePagination.setVisible(false);
            }
        }
    }
    protected void redrawTableAndSetHighlight(boolean setHighlight) {
        cellTable.redraw();
        cellTable.flush();
        setRowHighlight(EDITING_HIGHLIGHT, setHighlight);
    }

    protected void setRowHighlight(String highlight, boolean setHighlight) {
//        if ( true ) return; // XXX TODO: Right now this is redundant for PersonPanel due to setRowStyles
        // Need to add setRowStyles to other panels.
        // Actually, preferably, promote it to MultiPanel with the rest.
        OAPMetadataEditor.logToConsole("setRowHightlight " + setHighlight +
                " active: " + editIndex +
                ", range: " + cellTableRange_start);
        if ( editIndex >= 0 ) {
            int rangeRow = editIndex - cellTableRange_start;
            if ( rangeRow < 0 ) {
                return;
            }
            try {
                TableRowElement row = cellTable.getRowElement(rangeRow);
                GWT.log("row:"+row);
                if (setHighlight) {
                    row.addClassName(highlight);
                } else {
                    row.removeClassName(highlight);
                }
            } catch (Exception exception) {
                GWT.log(exception.toString());
            }
        }
    }

    protected void addCellTableListeners() {
        cellTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent event) {
                GWT.log("range change: " +event.toDebugString());
                cellTableRange_start = event.getNewRange().getStart();
            }
        });
        cellTable.addRedrawHandler(new AbstractHasData.RedrawEvent.Handler() {
            @Override
            public void onRedraw() {
                GWT.log("redraw displayed:"+cellTable.getDisplayedItems());
                setRowHighlight(EDITING_HIGHLIGHT, editIndex >= 0);
            }
        });
    }
}
