package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.RowStyles;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.RangeChangeEvent;

import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.TableContextualType;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.event.SectionUpdater;

import gov.noaa.pmel.sdig.client.oracles.CountrySuggestionOracle;
import gov.noaa.pmel.sdig.client.oracles.InstitutionSuggestOracle;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.Person;
import gov.noaa.pmel.sdig.shared.bean.TypedString;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.form.error.BasicEditorError;
import org.gwtbootstrap3.client.ui.form.validator.Validator;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.*;

/**
 * Created by rhs on 2/27/17.
 */
public class PersonPanel extends FormPanel implements GetsDirty<Person> {

//    @UiField
//    Form form;
    @UiField
    Button save;
    @UiField
    Button clear;
    @UiField
    TextBox lastName;
    @UiField
    TextBox mi;
    @UiField
    TextBox firstName;
    @UiField(provided = true)
    SuggestBox institution;
    @UiField
    TextBox address1;
    @UiField
    TextBox address2;
    @UiField
    FormLabel telephoneLabel;
    @UiField
    TextBox telephone;
//    @UiField
//    Label telephoneError;
    @UiField
    TextBox extension;
    @UiField
    TextBox city;
    @UiField
    TextBox state;
    @UiField
    TextBox zip;
    @UiField(provided = true)
    Select countrySelect;
    @UiField
    FormLabel emailLabel;
    @UiField
    TextBox email;
    @UiField
    Heading heading;

    @UiField
    Row ridRow0;
    @UiField
    FormGroup ridTypeForm0;
    @UiField
    ButtonDropDown ridType0;
    @UiField
    TextBox rid0;
    @UiField
    Button addRidButton;
    @UiField
    Modal ridBtnPopover;

    List<Row> addedRows = new ArrayList<>();
    HashMap<String, ButtonDropDown> ridIdTypeDrops = new LinkedHashMap<>();
    HashMap<String, TextBox> ridTextBoxes = new LinkedHashMap<>();
    int row0index = 9; // XXX CHANGE
    //    int addedInstIdx = 0;
    Container formContainer;

    private static final char CO2_VARS_SEPARATOR = ';';
    private static final String RID_ID = "rid_id_";
    private static final String TYPE_ID = "rid_type_";

    private static final String RMV_BTN_ID = "rid_rmv_";
    private static final String RID_ROW_ = "rid_row_";

    // Form help items to be customized...
    @UiField
    Modal namePopover;
    @UiField
    Modal institutionPopover;
    @UiField
    Modal addressPopover;
    @UiField
    Modal telephonePopover;
    @UiField
    Modal emailPopover;
    @UiField
    Modal idPopover;
    @UiField
    Modal idTypePopover;

    @UiField
    CellTable<Person> people;

    boolean showTable = true;
    boolean editing = false;
    Person displayedPerson;
    Person editPerson = null;
    String type;
    int editIndex = -1;
    int pageSize = 4;

    @UiField
    Pagination peoplePagination;

    ButtonCell editButton = new ButtonCell(IconType.EDIT, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL);
    ButtonCell moveUpButton = new ButtonCell(IconType.ARROW_UP, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL);
    ButtonCell moveDownButton = new ButtonCell(IconType.ARROW_DOWN, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL);
    ButtonCell deleteButton = new ButtonCell(IconType.TRASH, ButtonType.DANGER, ButtonSize.EXTRA_SMALL);

    public static final String myRegex         = "^\\w[\\w-_#$%\\.]*@[\\w-_]+(\\.[\\w-_]+)*(\\.[a-z]{2,})$";
    public static RegExp emailRegex = RegExp.compile(myRegex);
    // from http://emailregex.com/ -- Doesn't seem to work.  Tried various strings
//    public static RegExp emailRegex = RegExp.compile("(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
//    public static RegExp checkRegex = RegExp.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
//    public static RegExp jsRegex = RegExp.compile("/^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$/");

    InstitutionSuggestOracle institutionSuggestOracle = new InstitutionSuggestOracle();
    CountrySuggestionOracle countrySuggestionOracle = new CountrySuggestionOracle();

    ListDataProvider<Person> peopleData = new ListDataProvider<Person>();

    private SimplePager cellTablePager = new SimplePager();

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    interface PersonUiBinder extends UiBinder<HTMLPanel, PersonPanel> {
    }

    private static PersonUiBinder ourUiBinder = GWT.create(PersonUiBinder.class);

    static List<String> idNames = new ArrayList<String>();
    static List<String> idValues = new ArrayList<String>();
    static {
        idNames.add("ORCID ");
        idValues.add("ORCID");
        idNames.add("Researcher ID ");
        idValues.add("RESEARCHER_ID");
        idNames.add("Ocean Expert ");
        idValues.add("OCEAN_EXPERT");
    }

    public PersonPanel(String personType) {
        institution = new SuggestBox(institutionSuggestOracle);
//        countrySuggest = new SuggestBox(countrySuggestionOracle);
        countrySelect = new Select();
        countrySelect.setLiveSearch(true);
        countrySelect.setPlaceholder("Country");
        countrySelect.setLiveSearchPlaceholder("Country"); // This doesn't seem to work.  Needs Placeholder in ui.xml
//        provides accent-insenstive search.  Not available until 0.9.4
//        countrySelect.setLiveSearchNormalize(true);
        for (String country : Constants.COUNTRIES) {
            Option option = new Option();
            option.setValue(country);
            option.setText(country);
            countrySelect.add(option);
        }

        initWidget(ourUiBinder.createAndBindUi(this));
        ridType0.init("Pick an ID Type ", idNames, idValues);
//        ridType0.setId(TYPE_ID+"0");

        clear.addClickHandler(clearIt);

        namePopover.setTitle("3.1 Full name of the " + personType + " (First Middle Last).");
        institutionPopover.setTitle("3.2 Affiliated institution of the " + personType + " (e.g., Woods Hole Oceanographic Institution).");
        addressPopover.setTitle("3.3 Address of the affiliated institution of the " + personType + ".");
        telephonePopover.setTitle("3.4 Phone number of the " + personType + ".");
        emailPopover.setTitle("3.5 Email address of the " + personType + ".");
        idTypePopover.setTitle("3.7 Please indicate which type of researcher ID.");
        idPopover.setTitle("3.6 We recommend to use person identifiers (e.g. ORCID, Researcher ID, etc.) to unambiguously identify the " + personType + ".");

        if ("data submitter".equalsIgnoreCase(personType)) {
            emailLabel.setText("Email Address *");
            emailLabel.setColor("#B22222");
            email.setAllowBlank(false);
//            telephone.setAllowBlank(false);
//            telephoneLabel.setText("Telephone Number *");
//            telephoneLabel.setColor("#B22222");
            telephone.addValidator(new Validator() {
                @Override
                public List<EditorError> validate(Editor editor, Object value) {
                    List<EditorError> result = new ArrayList<EditorError>();
                    String valueStr = value == null ? "" : value.toString().trim();
                    if (valueStr.length() == 0) {
                        return result;
                    }
                    // The more complex of the two answers here:
                    // https://stackoverflow.com/questions/16699007/regular-expression-to-match-standard-10-digit-phone-number
                    //RegExp p = RegExp.compile("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$");
                    //                RegExp p = RegExp.compile("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$");
                    RegExp p = RegExp.compile("(?:\\+|00|011)?[\\(\\)\\d .-]{8,16}");
                    if (!p.test(valueStr)) {
                        // try international number
                        //                    p = RegExp.compile("/(\\+|00)(297|93|244|1264|358|355|376|971|54|374|1684|1268|61|43|994|257|32|229|226|880|359|973|1242|387|590|375|501|1441|591|55|1246|673|975|267|236|1|61|41|56|86|225|237|243|242|682|57|269|238|506|53|5999|61|1345|357|420|49|253|1767|45|1809|1829|1849|213|593|20|291|212|34|372|251|358|679|500|33|298|691|241|44|995|44|233|350|224|590|220|245|240|30|1473|299|502|594|1671|592|852|504|385|509|36|62|44|91|246|353|98|964|354|972|39|1876|44|962|81|76|77|254|996|855|686|1869|82|383|965|856|961|231|218|1758|423|94|266|370|352|371|853|590|212|377|373|261|960|52|692|389|223|356|95|382|976|1670|258|222|1664|596|230|265|60|262|264|687|227|672|234|505|683|31|47|977|674|64|968|92|507|64|51|63|680|675|48|1787|1939|850|351|595|970|689|974|262|40|7|250|966|249|221|65|500|4779|677|232|503|378|252|508|381|211|239|597|421|386|46|268|1721|248|963|1649|235|228|66|992|690|993|670|676|1868|216|90|688|886|255|256|380|598|1|998|3906698|379|1784|58|1284|1340|84|678|681|685|967|27|260|263)(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{4,20}$/\n");
                        //                    if ( !p.test(valueStr) ) {
                        result.add(new BasicEditorError(telephone, value, "Does not look like a phone number to me."));
                        //                    }
                    }
                    return result;
                }

                @Override
                public int getPriority() {
                    return Priority.HIGH;
                }
            });
//            telephone.setErrorHandler(new ErrorHandler() {
//                @Override
//                public void cleanup() {
//                    System.out.println("Cleanup()");
//                    telephoneError.setText("");
//                    telephoneError.setVisible(false);
//                }
//
//                @Override
//                public void clearErrors() {
//                    System.out.println("clearErrors()");
//                    telephoneError.setText("");
//                    telephoneError.setVisible(false);
//                }
//
//                @Override
//                public void showErrors(List<EditorError> errors) {
//                    StringBuilder sb = new StringBuilder();
//                    String sep = "";
//                    for (EditorError e : errors) {
//                        sb.append(sep);
//                        sb.append(e.getMessage());
//                        sep = "; ";
//                    }
//                    telephoneError.setText(sb.toString());
//                    telephoneError.setVisible(true);
//                }
//            });
        }

        email.addValidator(new Validator() {
            @Override
            public List<EditorError> validate(Editor editor, Object value) {
                GWT.log("validate email: " + value);
                List<EditorError> result = new ArrayList<EditorError>();
                String valueStr = value == null ? "" : value.toString();
                if (valueStr.length() == 0) {
                    return result;
                }
                boolean isGood = emailRegex.test(valueStr);
//                isGood = valueStr.matches(myRegex);
                if (!isGood) {
                    result.add(new BasicEditorError(email, value, "Does not look like an email address to me."));
                }
                return result;
            }

            @Override
            public int getPriority() {
                return Priority.HIGH;
            }
        });

        people.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);

        people.addCellPreviewHandler(new CellPreviewEvent.Handler<Person>() {
            @Override
            public void onCellPreview(CellPreviewEvent<Person> event) {
                OAPMetadataEditor.logToConsole("event:"+ event.getNativeEvent().getType());
                Person rowPerson = event.getValue();
                if ( editing ) { // ignore
                    return;
                } else if ("mouseover".equals(event.getNativeEvent().getType())) {
                    show(rowPerson, false);
                } else if ("mouseout".equals(event.getNativeEvent().getType())) {
                    reset();
                }
            }
        });

        Column<Person, String> edit = new Column<Person, String>(editButton) {
            @Override
            public String getValue(Person object) {
                return "Edit";
            }
        };
        edit.setFieldUpdater(new FieldUpdater<Person, String>() {
            @Override
            public void update(int index, Person person, String value) {
                editIndex = peopleData.getList().indexOf(person);
                OAPMetadataEditor.debugLog("EDIT editIndex: " + editIndex);
                if (editIndex < 0) {
                    Window.alert("Edit failed.");
                } else {
                    show(person, true);
                    peopleData.getList().remove(person);
                    peopleData.flush();
                    peoplePagination.rebuild(cellTablePager);
                    save.setEnabled(true);
                    setEnableTableRowButtons(false);
                }
            }
        });
        people.addColumn(edit);
        edit.setCellStyleNames("text-center");

        // Add a text column to show the position.
//        TextColumn<Person> positionColumn = new TextColumn<Person>() {
//            @Override
//            public String getValue(Person object) {
////                String position = String.valueOf(object.getPosition() + 1);
////                return String.valueOf(object.getPosition() + 1);
//                return String.valueOf(object.getPosition());
//            }
//        };
//        people.addColumn(positionColumn, "Position");
//        positionColumn.setCellStyleNames("text-center");

        Column<Person, String> moveUp = new Column<Person, String>(moveUpButton) {
            @Override
            public String getValue(Person object) {
                return "";
            }
        };
        moveUp.setFieldUpdater(new FieldUpdater<Person, String>() {
            @Override
            public void update(int index, Person person, String value) {
                int position = person.getPosition();
                int newposition = position - 1;

                if (newposition < 0) {
                    OAPMetadataEditor.debugLog("is at top position 0 > " + newposition);
                } else {
                    OAPMetadataEditor.debugLog("moveUp final position: " + newposition);

                    // update previousPerson's position
                    Person previousPerson = peopleData.getList().get(newposition);
                    previousPerson.setPosition(position);
                    peopleData.getList().remove(previousPerson);
                    peopleData.getList().add(newposition, previousPerson);

                    // update current person's position
                    peopleData.getList().remove(person);
                    person.setPosition(newposition);
                    peopleData.getList().add(newposition, person);

                    peopleData.flush();
                    peoplePagination.rebuild(cellTablePager);
//                    save.setEnabled(true);
                    reset();

//                    // for debugging position check
//                    for (int i = 0; i < peopleData.getList().size(); i++) {
//                        OAPMetadataEditor.debugLog("UPWARD LOOP");
//                        OAPMetadataEditor.debugLog("Loop index: " + i);
//                        Person who = peopleData.getList().get(i);
//
//                        int guessWhoPos = who.getPosition();
////                        OAPMetadataEditor.debugLog("who pos: " + guessWhoPos);
//
//                        String guessWho;
//                        if ( who.getLastName() != null ) {
//                            guessWho = who.getLastName();
//                            OAPMetadataEditor.debugLog(guessWho + "'s position is set to " + guessWhoPos);
//                        }
//                        else {
//                            OAPMetadataEditor.debugLog("Unknown'sposition is set to " + guessWhoPos);
//                        }
//
//                    }

                }

            }
        });
        people.addColumn(moveUp);
        moveUp.setCellStyleNames("text-center");

        Column<Person, String> moveDown = new Column<Person, String>(moveDownButton) {
            @Override
            public String getValue(Person object) {
                return "";
            }
        };
        moveDown.setFieldUpdater(new FieldUpdater<Person, String>() {
            @Override
            public void update(int index, Person person, String value) {
                int position = person.getPosition();
                int newposition = position + 1;

                if (newposition >= peopleData.getList().size()) {
                    OAPMetadataEditor.debugLog("is at bottom postion " + peopleData.getList().size() + " < " + newposition);
                } else {
                    OAPMetadataEditor.debugLog("moveDown final position: " + newposition);

                    // update nextPerson's position
                    Person nextPerson = peopleData.getList().get(newposition);
                    nextPerson.setPosition(position);
                    peopleData.getList().remove(nextPerson);
                    peopleData.getList().add(newposition, nextPerson);

                    // update current person's position
                    peopleData.getList().remove(person);
                    person.setPosition(newposition);
                    peopleData.getList().add(newposition, person);

                    peopleData.flush();
                    peoplePagination.rebuild(cellTablePager);
                    reset();

//                    // for debugging position check
//                    for (int i = 0; i < peopleData.getList().size(); i++) {
//                        OAPMetadataEditor.debugLog("DOWNWARD LOOP");
//                        OAPMetadataEditor.debugLog("Loop index: " + i);
//                        Person who = peopleData.getList().get(i);
//
//                        int guessWhoPos = who.getPosition();
////                        OAPMetadataEditor.debugLog("who pos: " + guessWhoPos);
//
//                        String guessWho;
//                        if ( who.getLastName() != null ) {
//                            guessWho = who.getLastName();
//                            OAPMetadataEditor.debugLog(guessWho + "'s position is set to " + guessWhoPos);
//                        }
//                        else {
//                            OAPMetadataEditor.debugLog("Unknown'sposition is set to " + guessWhoPos);
//                        }
//                    }
                }
            }
        });
        people.addColumn(moveDown);
        moveDown.setCellStyleNames("text-center");

        // Add a text column to show the name.
        TextColumn<Person> nameColumn = new TextColumn<Person>() {
            @Override
            public String getValue(Person object) {
                return object.getLastName();
            }
        };
        people.addColumn(nameColumn, "Last Name");

        // Add a text column to show the institution.
        TextColumn<Person> institutionColumn = new TextColumn<Person>() {
            @Override
            public String getValue(Person object) {
                return object.getInstitution();
            }
        };
        people.addColumn(institutionColumn, "Institution");

        Column<Person, String> delete = new Column<Person, String>(deleteButton) {
            @Override
            public String getValue(Person object) {
                return "Delete";
            }
        };
        delete.setFieldUpdater(new FieldUpdater<Person, String>() {
            @Override
            public void update(int index, Person person, String value) {
                form.reset(); // Because the mouseover will have filled the form
                peopleData.getList().remove(person);
                hasRequiredFields();
                peopleData.flush();
                peoplePagination.rebuild(cellTablePager);
                if (peopleData.getList().size() == 0) {
                    setTableVisible(false);
                    show(person, true);
                    reset();
                    eventBus.fireEventFromSource(new SectionUpdater(Constants.SECTION_INVESTIGATOR),PersonPanel.this);
                } else {
                    setTableVisible(true);
                }
            }
        });
        people.addColumn(delete);
        delete.setCellStyleNames("text-center");

        // set RowStyles on required fields
        people.setRowStyles(new RowStyles<Person>() {
            @Override
            public String getStyleNames(Person row, int rowIndex) {
                if (((row.getInstitution() == null) || (row.getInstitution().isEmpty()))
                        || ((row.getFirstName() == null) || (row.getFirstName().isEmpty()))
                        || ((row.getLastName() == null) || (row.getLastName().isEmpty()))
                        || ((row.getEmail() != null ) && (row.getEmail().toString().length() != 0) && (!emailRegex.test(row.getEmail())))) {
                    OAPMetadataEditor.debugLog("row.getEmail(): \"" + row.getEmail() + "\"");
    //                OAPMetadataEditor.debugLog("row.getEmail() string length: " + row.getEmail().toString().length());
                    OAPMetadataEditor.debugLog("getCssName(TableContextualType.DANGER): " + TableContextualType.DANGER.getCssName());
                    return TableContextualType.DANGER.getCssName();
                } else {
                    return "";
                }
            }
        });

        people.addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(final RangeChangeEvent event) {
                peoplePagination.rebuild(cellTablePager);
            }
        });

        cellTablePager.setDisplay(people);

        people.setPageSize(pageSize);

        peopleData.addDataDisplay(people);

//        countrySelect.addValueChangeHandler(new ValueChangeHandler<String>() {
//                  @Override
//                  public void onValueChange(ValueChangeEvent<String> event) {
//                      modified = true;
//                  }
//              }
//        );
    }


    private Widget getRowWidget(Row row, int colIdx, int wgtIdx) {
        Widget clmWidget = row.getWidget(colIdx);
        GWT.log("clmWidget:"+clmWidget);
        org.gwtbootstrap3.client.ui.Column clm = (org.gwtbootstrap3.client.ui.Column)clmWidget;
        GWT.log("clm:"+clm);
        Widget formWidget = clm.getWidget(0);
        GWT.log("formWidget:"+formWidget);
        FormGroup form = (FormGroup)formWidget;
        GWT.log("form:"+String.valueOf(form != null));
        Widget widget = form.getWidget(wgtIdx);
        GWT.log("widget:"+widget);
        return widget;
    }
    public boolean validate() {
        GWT.log("PersonPanel validate");
        boolean isOk = this.valid();
        List<Row>ridRows = getRidRows();
        boolean isAdded = false;
        for (Row rrow : ridRows) {
            GWT.log("row:"+rrow);
            int wgtIdx = isAdded ? 0 : 3;
            ButtonDropDown rTypeBtn = (ButtonDropDown) getRowWidget(rrow, 0, wgtIdx);
            TextBox ridBox = (TextBox) getRowWidget(rrow, 1, wgtIdx);
            TypedString resid = buildResId(rTypeBtn, ridBox);
            if ( ! resid.getValue().trim().isEmpty() &&
                   resid.getType().isEmpty() ) {
                isOk = false;
                setRidError(rrow, wgtIdx, true);
            } else {
                if ( ! resid.getType().equals(rTypeBtn.getValue()) ||
                     ! resid.getValue().equals(ridBox.getText())) {
                    rTypeBtn.setSelected(resid.getType());
                    ridBox.setText(resid.getValue());
                }
                setRidError(rrow, wgtIdx, false);
            }
            isAdded = true;
        }
        return isOk;
    }

    public void setRidError(int rowIdx, boolean set) {
        Row rrow = getRidRows().get(rowIdx);
        int wgtIdx = rowIdx == 0 ? 3 : 0;
        setRidError(rrow, wgtIdx, set);
    }
    public void setRidError(Row rrow, int wgtIdx, boolean set) {
        ButtonDropDown rTypeBtn = (ButtonDropDown) getRowWidget(rrow, 0, wgtIdx);
        setRidError(set, rTypeBtn);
    }

    public void setFieldError(boolean set, Widget fieldForm) {
        FormGroup group = getFormGroup(fieldForm);
        if ( set )
            group.addStyleName("has-error");
        else
            group.removeStyleName("has-error");
    }

    public void setRidError(boolean set, ButtonDropDown ridType) {
        Button typeBtn = ridType.getButton();
        if (set) {
            ridTypeForm0.addStyleName("has-error");
            typeBtn.addStyleName("error-border");
        } else {
            ridTypeForm0.removeStyleName("has-error");
            typeBtn.removeStyleName("error-border");
        }
    }
    private FormGroup getFormGroup(Widget widget) {
        return (FormGroup) widget.getParent(); // XXX TODO: not always!
    }

    private List<Row> getRidRows() {
        List<Row> ridRows = new ArrayList<>();
        ridRows.add(ridRow0);
        ridRows.addAll(addedRows);
        return ridRows;
    }

//    private List<?>[] getRidBoxes() {
//        List<TextBox> rids = new ArrayList<>();
//        List<ButtonDropDown> types = new ArrayList<>();
//        List[] lists = new List[2];
//        lists[0] = rids;
//        lists[1] = types;
//        rids.add(rid0);
//        types.add(ridType0);
//        for ( Row rrow : addedRows) {
//
//        }
//        return lists;
//    }

    public void addPerson(Person p) {
        if (p == null) {
            return;
        }
        int position = p.getPosition() >= 0 ? p.getPosition() : peopleData.getList().size();
        p.setPosition(position);
        peopleData.getList().add(position, p);
        peopleData.flush();
        peoplePagination.rebuild(cellTablePager);
    }

    @UiHandler("save")
    public void onSave(ClickEvent clickEvent) {
        OAPMetadataEditor.logToConsole("Save Person: " + clickEvent);
        if ( ! validate() || ! valid()) {
            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.WARNING);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.NOT_COMPLETE, settings);
        } else {
//            this.modified = false;
            this.editing = false;
            if (hasContent()) {
                Person p = getPerson();
                addPerson(p);
            }

            // check if any person in peopleData is missing required fields
            hasRequiredFields();

            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.SUCCESS);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.COMPLETE, settings);
            if (showTable && peopleData.getList().size() > 0) {
                setTableVisible(true);
                setEnableTableRowButtons(true);
                reset();
            }
            save.setEnabled(false);
        }

    }
//    public Person savePerson() {
//        Person p = getPerson();
//        peopleData.getList().add(getPerson());
//        peopleData.flush();
//        peoplePagination.rebuild(cellTablePager);
//        return p;
//    }

    public Person getPerson() {
//        OAPMetadataEditor.debugLog("PersonPanel.get()");
        Person person = displayedPerson != null ? displayedPerson : new Person();
        person.setAddress1(address1.getText().trim());
        person.setAddress2(address2.getText().trim());
        person.setEmail(email.getText().trim());
        person.setFirstName(firstName.getText().trim());
        person.setInstitution(institution.getText().trim());
        person.setLastName(lastName.getText().trim());
        person.setMi(mi.getText().trim());
//        person.setRid(rid0.getText().trim());
        person.setTelephone(telephone.getText().trim());
        person.setExtension(extension.getText().trim());
        person.setCity(city.getText().trim());
        person.setState(state.getText().trim());
        person.setZip(zip.getText().trim());
        Option opt = countrySelect.getSelectedItem();
        OAPMetadataEditor.debugLog("country opt:"+opt);
        if ( opt != null ) {
            person.setCountry(opt.getValue());
        }
//        person.setIdType(ridType0.getValue());
        person.setResearcherIds(getResearcherIds());
        person.setPosition(editIndex);
//        person.setComplete(person.isValid());  // Don't do this here, screws up form.
        return person;
    }

    private TypedString buildResId(ButtonDropDown typeBtn, TextBox ridField) {
        String typeGuess = "";
        String ridVal = ridField.getText();
        GWT.log("ridValue:"+ridVal);
        String ridStr = ridVal.trim();
        if ( ridStr.toLowerCase().startsWith("http")) {
            if ( ridStr.endsWith("/")) {
                ridStr = ridStr.substring(ridStr.length()-1);
            }
            ridStr = ridStr.substring(ridStr.lastIndexOf('/')+1);
            int firstDot = ridVal.indexOf('.');
            int lastDot = ridVal.lastIndexOf('.');
            int colon = ridVal.indexOf(':');
            int startIdx = firstDot < lastDot ? firstDot+1 : colon+3;
            typeGuess = ridVal.substring(startIdx, lastDot).toUpperCase();
            GWT.log("Guess : " + typeGuess);
        }
        String ridTypeVal = typeBtn.getValue();
        String ridType = ridTypeVal != null ? ridTypeVal.trim() : "";
        if ( ridType.isEmpty() && ! typeGuess.isEmpty() && typeGuess.toUpperCase().equals("ORCID")) {
            GWT.log("Guessing : " + typeGuess);
            ridType = typeGuess;
        }
        TypedString rid = new TypedString(ridType, ridStr);
        return rid;
    }
    private List<TypedString> getResearcherIds() {
        List<TypedString> resIds = new ArrayList<>();
        TypedString id0 = buildResId(ridType0, rid0);
        if ( id0.hasContent()) {
            GWT.log("Got rid0:"+ id0);
            resIds.add(id0);
        }
        int x = 0;
        for (Row row : addedRows ) {
            x += 1;
            String rowId = row.getId();
            TypedString ridx = buildResId(ridIdTypeDrops.get(rowId), ridTextBoxes.get(rowId));
            if ( ridx.hasContent()) {
                GWT.log("Got rid" + x + ": "+ ridx);
                resIds.add(ridx);
            }
        }
        return resIds;
    }

    @Override
    public boolean isDirty(Person original) {
        OAPMetadataEditor.debugLog("@PersonPanel.isDirty(" + original + ")");
        boolean isDirty = false;
        isDirty = original == null ?
                hasContent() : //       XXX get hasBeenModified() right!
                isDirty(address1, original.getAddress1()) ||
                isDirty(address2, original.getAddress2()) ||
                isDirty(email, original.getEmail()) ||
                isDirty(firstName, original.getFirstName()) ||
                isDirty(institution, original.getInstitution()) ||
                isDirty(lastName, original.getLastName()) ||
                isDirty(mi, original.getMi()) ||
//                isDirty(rid0, original.getRid()) ||
                researcherIdsChanged(original) ||
                isDirty(telephone, original.getTelephone()) ||
                isDirty(extension, original.getExtension()) ||
                isDirty(city, original.getCity()) ||
                isDirty(state, original.getState()) ||
                isDirty(zip, original.getZip()) ||
                isDirty(countrySelect, original.getCountry());
        OAPMetadataEditor.debugLog("PersonPanel.isDirty: " + isDirty);
        return isDirty;
    }

    private boolean researcherIdsChanged(Person original) {
        List<TypedString> rids = getResearcherIds();
        List<TypedString> originalRids = original.getResearcherIds();
        if (rids.size() != originalRids.size()) {
            return true;
        }
        for ( int i = 0; i < rids.size(); i++) {
            if ( ! rids.get(i).equals(originalRids.get(i))) {
                GWT.log("researcher IDs differ");
                return true;
            }
        }
        GWT.log("std gases the same");
        return false;
    }

    boolean isDirty(Select cSelect, String originalCountry) {
        Option selectedOption = cSelect.getSelectedItem();
        String originalValue = originalCountry != null ? originalCountry.trim() : "";
        if ( selectedOption == null || selectedOption.getText().trim().isEmpty()) {
            return ! originalValue.isEmpty();
        }
        return ! selectedOption.getValue().equals(originalValue);
    }
    public boolean hasContent() {
        OAPMetadataEditor.debugLog("@PersonPanel.hasContent()");
        boolean hasContent = false;
        save.setEnabled(false);
        if (address1.getText().trim() != null && !address1.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.address1:" + address1.getText());
            hasContent = true;
        }
        if (address2.getText().trim() != null && !address2.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.address2:" + address2.getText());
            hasContent = true;
        }
        if (email.getText().trim() != null && !email.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.email:" + email.getText());
            hasContent = true;
        }
        if (firstName.getText().trim() != null && !firstName.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.firstName:" + firstName.getText());
            hasContent = true;
        }
        if (institution.getText().trim() != null && !institution.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.institution:" + institution.getText());
            hasContent = true;
        }
        if (lastName.getText().trim() != null && !lastName.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.lastName:" + lastName.getText());
            hasContent = true;
        }
        if (mi.getText().trim() != null && !mi.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.mi:" + mi.getText());
            hasContent = true;
        }
        if (rid0.getText().trim() != null && !rid0.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.rid:" + rid0.getText());
            hasContent = true;
        }
        // XXX check researcherIds
        if (telephone.getText().trim() != null && !telephone.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.telephone:" + telephone.getText());
            hasContent = true;
        }
        if (extension.getText().trim() != null && !extension.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.extension:" + extension.getText());
            hasContent = true;

        }
        if (city.getText().trim() != null && !city.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.city:" + city.getText());
            hasContent = true;
        }
        if (state.getText().trim() != null && !state.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.state:" + state.getText());
            hasContent = true;
        }
        if (zip.getText().trim() != null && !zip.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.zip:" + zip.getText());
            hasContent = true;
        }
        // For some reason, this comes up falsely positive when the person is otherwise empty
//        if (countrySelect.getSelectedItem() != null ) {
//            OAPMetadataEditor.debugLog("PersonPanel.countrySelect:" + countrySelect.getValue());
//            hasContent = true;
//        }
        if (hasContent == true) {
            save.setEnabled(true);
        }
        OAPMetadataEditor.debugLog("PersonPanel.hasContent is " + hasContent);
        return hasContent;
    }

    public void show(Person person, boolean editable) {
        setAllEditable(editable);
        editing = editable;
        if (editable) {
            displayedPerson = person;
        } else {
            editPerson = getPerson();
        }
        show(person);
    }

    private void setAllEditable(boolean editable) {
        address1.setEnabled(editable);
        address2.setEnabled(editable);
        email.setEnabled(editable);
        firstName.setEnabled(editable);
        institution.setEnabled(editable);
        lastName.setEnabled(editable);
        mi.setEnabled(editable);
        rid0.setEnabled(editable);
        telephone.setEnabled(editable);
        extension.setEnabled(editable);
        city.setEnabled(editable);
        state.setEnabled(editable);
        zip.setEnabled(editable);
        countrySelect.setEnabled(editable);
    }

    public void show(Person person) {
        if (person == null || ! person.hasContent()) {
            reset();
            return;
        }
        if (person.getAddress1() != null)
            address1.setText(person.getAddress1());
        if (person.getAddress2() != null)
            address2.setText(person.getAddress2());
        if (person.getEmail() != null)
            email.setText(person.getEmail());
        if (person.getFirstName() != null)
            firstName.setText(person.getFirstName());
        if (person.getInstitution() != null)
            institution.setText(person.getInstitution());
        if (person.getLastName() != null)
            lastName.setText(person.getLastName());
        if (person.getMi() != null)
            mi.setText(person.getMi());
//        if (person.getRid() != null)
//            rid0.setText(person.getRid());
        if (person.getTelephone() != null)
            telephone.setText(person.getTelephone());
        if (person.getExtension() != null)
            extension.setText(person.getExtension());
        if (person.getCity() != null)
            city.setText(person.getCity());
        if (person.getState() != null)
            state.setText(person.getState().trim());
        if (person.getZip() != null)
            zip.setText(person.getZip());
        if (person.getCountry() != null)
            countrySelect.setValue(person.getCountry());
//        if (person.getIdType() != null) {
//            ridType0.setSelected(person.getIdType());
        showResearcherIds(person);
        OAPMetadataEditor.debugLog("Checking valid person");
        person.setComplete(this.validate());
    }

    @UiHandler("email")
    public void onEmailBlur(BlurEvent event) {
        OAPMetadataEditor.debugLog("onBlur source: " + event.getSource());
        boolean emailGood = emailRegex.test(email.getValue());
        setFieldError(! emailGood, email);
        save.setEnabled(emailGood);
    }
    @UiHandler({"firstName", "mi", "lastName", "address1", "address2", "city", "state", "zip", "telephone", "extension", "email", "rid0"})
    public void onChange(ChangeEvent event) {
        OAPMetadataEditor.debugLog("onChange source: " + event.getSource());
        save.setEnabled(true);
    }

    @UiHandler({"institution"})
    public void onOrgValueChange(ValueChangeEvent<String> event) {
//            Window.alert("Here be the new value:" + event.getValue());
        OAPMetadataEditor.debugLog("Here be the new value:" + event.getValue());
        save.setEnabled(true);
    }
    @UiHandler({"countrySelect"})
    public void onCountryValueChange(ValueChangeEvent<String> event) {
        OAPMetadataEditor.debugLog("Country changed: " +event.getValue());
        save.setEnabled(true);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Person> getInvestigators() {
        return peopleData.getList();
    }

    public void clearPeople() {
        peopleData.getList().clear();
        peopleData.flush();
        peoplePagination.rebuild(cellTablePager);
        setEnableTableRowButtons(true);
        setTableVisible(false);
    }

    public void addPeople(List<Person> personList) {
        for (int i = 0; i < personList.size(); i++) {
            Person p = personList.get(i);
            p.setPosition(i);
            peopleData.getList().add(p);
        }
        peopleData.flush();
        peoplePagination.rebuild(cellTablePager);
        setTableVisible(true);
    }

    public void hasRequiredFields() {
        // check if any person in peopleData is missing required fields
        GWT.log("Checking person for required fields");
        boolean meetsRequired = true;
        for (int i = 0; i < peopleData.getList().size(); i++) {
            Person p = peopleData.getList().get(i);
            OAPMetadataEditor.debugLog("@checkPeople:p.getEmail(): \"" + p.getEmail() + "\"");
            if (((p.getInstitution() == null) || (p.getInstitution().isEmpty()))
                    || ((p.getFirstName() == null) || (p.getFirstName().isEmpty()))
                    || ((p.getLastName() == null) || (p.getLastName().isEmpty()))
                    || ((!emailRegex.test(p.getEmail())) && ((p.getEmail() != null) && (!p.getEmail().isEmpty())))) {
                meetsRequired = false;
            }
            List<TypedString> resIds = p.getResearcherIds();
            if ( resIds != null && resIds.size() > 0 ) {
                for (TypedString id : resIds) {
                    if ( ! id.getValue().trim().isEmpty() &&
                           id.getType().trim().isEmpty()) {
                        meetsRequired = false;
                        break;
                    }
                }
            }
        }
        if (meetsRequired == true && peopleData.getList().size() > 0) {
            eventBus.fireEventFromSource(new SectionSave(getPerson(), this.type), PersonPanel.this);
        }
    }

    public void setTableVisible(boolean b) {
        people.setVisible(b);
        peoplePagination.setVisible(b);
        if (b) {
            if (cellTablePager.getPageCount() > 1) {
                int page = cellTablePager.getPage();
                peoplePagination.setVisible(true);
                cellTablePager.setPage(page);
            } else {
                peoplePagination.setVisible(false);
            }
        }
    }

    public void setEnableTableRowButtons(boolean b) {
        for (int i = 0; i < peopleData.getList().size(); i++) {
            setEnableButton(editButton, b);
            setEnableButton(moveUpButton, b);
            setEnableButton(moveDownButton, b);
            setEnableButton(deleteButton, b);
        }
        people.redraw();
    }

    public void setEnableButton(ButtonCell button, boolean enabled) {
        if (enabled) {
            button.setEnabled(true);
        }
        else {
            button.setEnabled(false);
        }
    }

    public void reset() {
        form.reset();
        clearRids();
        form.validate(false);
        displayedPerson = null;
        editIndex = -1;
        editing = false;
        if (editPerson != null && editPerson.hasContent()) {
            show(editPerson);
            editPerson = null;
        }
        setAllEditable(true);
        setEnableTableRowButtons(true);
    }
    @UiHandler("addRidButton")
    public void onAdd(ClickEvent clickEvent) {
        GWT.log("Add RID clicked:"+clickEvent);
        addRidRow();
    }

    private void setFormContainer() {
        formContainer = (Container)ridRow0.getParent();
//        GWT.log("parent:"+ formContainer);
        int row0idx = formContainer.getWidgetIndex(ridRow0);
        if ( row0idx != row0index ) {
            GWT.log("WARN: Row0index has changed from " + row0index + " to " + row0idx);
            row0index = row0idx;
        }
        GWT.log("researcherIdRow index:"+ row0index);
    }

    private ButtonDropDown addRowDropButton(Row newRow, ColumnSize cSize, String itemId, String title) {
        org.gwtbootstrap3.client.ui.Column theColumn = new org.gwtbootstrap3.client.ui.Column(cSize);
        FormGroup theFgrp = new FormGroup();
//        theFgrp.addStyleName("form-control");
        ButtonDropDown bdd = new ButtonDropDown();
        bdd.init("Pick an ID Type ", idNames, idValues); // TODO: Remove or disable already chosen.
        bdd.setId(itemId);
        theFgrp.add(bdd);
        theColumn.add(theFgrp);
        newRow.add(theColumn);
        return bdd;
    }
    private TextBox addRowTextField(Row newRow, ColumnSize cSize, String itemId, String title) {
        org.gwtbootstrap3.client.ui.Column theColumn = new org.gwtbootstrap3.client.ui.Column(cSize);
        FormGroup theFgrp = new FormGroup();
//        theFgrp.addStyleName("form-control");
        TextBox theTextBox = new TextBox();
        theTextBox.setPlaceholder(title);
        theTextBox.setId(itemId);
        theFgrp.add(theTextBox);
        theColumn.add(theFgrp);
        newRow.add(theColumn);
        return theTextBox;
    }

    private Row addRidRow() {
        setFormContainer();
        int addedRidIdx = addedRows.size() + 1;// so it's 1-based.
        int addedRowId = row0index + addedRidIdx;
        addedRidIdx += 1; // So count matches displayed ids.
        GWT.log("Adding RID row " + addedRowId + " with " + addedRows.size() + " already");
        org.gwtbootstrap3.client.ui.Column row0col = (org.gwtbootstrap3.client.ui.Column) ridRow0.getWidget(0);
        Row newRow = new Row();
        String row_id = RID_ROW_ + addedRowId;
        newRow.setId(row_id);

        // ID Type
        ButtonDropDown ridTypeBtn = addRowDropButton(newRow, ColumnSize.MD_4, TYPE_ID+ addedRowId, "ID Type" );
        ridIdTypeDrops.put(row_id, ridTypeBtn);
        // ID
        TextBox ridTextBox = addRowTextField(newRow, ColumnSize.MD_4, RID_ID+ addedRowId, "ID");// " + addedRidIdx);
        ridTextBoxes.put(row_id, ridTextBox);

        // remove row button
        org.gwtbootstrap3.client.ui.Column buttonColumn = new org.gwtbootstrap3.client.ui.Column(ColumnSize.SM_2);
        FormGroup buttonFgrp = new FormGroup();
        Button removeButton = new Button("REMOVE");
        removeButton.setId(RMV_BTN_ID+ addedRowId);
        removeButton.addStyleName("float_right");
        removeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                removeRid(event.getSource());
            }
        });
        buttonFgrp.add(removeButton);
        buttonColumn.add(buttonFgrp);
        newRow.add(buttonColumn);

        formContainer.insert(newRow, addedRowId);

        addedRows.add(newRow);
        if ( addedRows.size() == 2 ) {
            addRidButton.setEnabled(false);
        }
        return newRow;
    }

    private void removeRid(Object source) {
        GWT.log("remove:"+source);
        Button removeButton = (Button) source;
        Row rowToRemove = getRowFor(removeButton); // Row)bclm.getParent();
        removeRidRow(rowToRemove);
        addedRows.remove(rowToRemove);
        addRidButton.setEnabled(true);
    }

    private void removeRidRow(Row rowToRemove) {
        boolean removed = formContainer.remove(rowToRemove);
        GWT.log("removed: " + removed);
        rowToRemove.removeFromParent();
        removeRidFields(rowToRemove);
    }

    private void removeRidFields(Row rowToRemove) {
        String rowId = rowToRemove.getId();
//        String rowIdxStr = rowId.substring(rowId.lastIndexOf("_")+1);
//        int rowIdx = row0index + 1;
//        try {
//            rowIdx = Integer.parseInt(rowIdxStr);
//        } catch (NumberFormatException e) {
//            GWT.log("Failed to parse row index " + rowIdxStr);
//        }
//        int instListIdx = rowIdx - row0index;
        removeDropBox(rowId, ridIdTypeDrops);
        removeField(rowId, ridTextBoxes);
    }
    private void removeDropBox(String rowId, Map<String, ButtonDropDown> map) {
        if ( map.containsKey(rowId)) {
            map.remove(rowId);
        } else {
            GWT.log("WARN: Missing textBox for added researcherId row "+ rowId);
        }
    }

    private void removeField(String rowId, Map<String, TextBox> map) {
        if ( map.containsKey(rowId)) {
            map.remove(rowId);
        } else {
            GWT.log("WARN: Missing textBox for added researcherId row "+ rowId);
        }
    }

    private Row getRowFor(Widget widget) {
        FormGroup bfg = (FormGroup)widget.getParent();
        org.gwtbootstrap3.client.ui.Column bclm = (org.gwtbootstrap3.client.ui.Column)bfg.getParent();
        Row rowToRemove = (Row)bclm.getParent();
        return rowToRemove;
    }

    private void showResearcherIds(Person person) {
        GWT.log("showing resIds for:" + person);
        clearRids();
        List<TypedString>researcherIds = person.getResearcherIds();
        GWT.log("researcherIds:" + researcherIds);
        if ( researcherIds.isEmpty()) { return; }
        TypedString id0 = researcherIds.get(0);
        rid0.setText(id0.getValue().trim());
        ridType0.setSelected(id0.getType());
        GWT.log("rid0: " + id0);
        for (int i = 1; i<researcherIds.size(); i++) {
            TypedString id = researcherIds.get(i);
            addResearcherId(id);
        }
    }

    private void clearRids() {
        rid0.setText("");
        ridType0.reset();
        for (Row addedRow : addedRows) {
            removeRidRow(addedRow);
        }
        addedRows.clear();
    }

    private void addResearcherId(TypedString rid) {
        GWT.log("Adding rid " + rid);
        Row addedRow = addRidRow();
        String rowId = addedRow.getId();
        ridTextBoxes.get(rowId).setText(rid.getValue());
        ridIdTypeDrops.get(rowId).setSelected(rid.getType());
    }

}

