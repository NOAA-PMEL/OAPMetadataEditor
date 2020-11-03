package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.AbstractSafeHtmlCell;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.RangeChangeEvent;

import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

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
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.form.error.BasicEditorError;
import org.gwtbootstrap3.client.ui.form.validator.Validator;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhs on 2/27/17.
 */
public class PersonPanel extends Composite implements GetsDirty<Person> {

    @UiField
    Form form;
    @UiField
    ButtonDropDown idType;
    @UiField
    Button save;
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
    SuggestBox country;
    @UiField
    FormLabel emailLabel;
    @UiField
    TextBox email;
    @UiField
    TextBox rid;
    @UiField
    Heading heading;

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
    Person editPerson;
    String type;
    int editIndex = -1;
    int pageSize = 4;

    @UiField
    Pagination peoplePagination;

    ButtonCell editButton = new ButtonCell(IconType.EDIT, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL);
    ButtonCell moveUpButton = new ButtonCell(IconType.ARROW_UP, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL);
    ButtonCell moveDownButton = new ButtonCell(IconType.ARROW_DOWN, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL);
    ButtonCell deleteButton = new ButtonCell(IconType.TRASH, ButtonType.DANGER, ButtonSize.EXTRA_SMALL);

    RegExp emailRegex = RegExp.compile("(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

    InstitutionSuggestOracle institutionSuggestOracle = new InstitutionSuggestOracle();
    CountrySuggestionOracle countrySuggestionOracle = new CountrySuggestionOracle();

    ListDataProvider<Person> peopleData = new ListDataProvider<Person>();

    private SimplePager cellTablePager = new SimplePager();

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    interface PersonUiBinder extends UiBinder<HTMLPanel, PersonPanel> {
    }

    private static PersonUiBinder ourUiBinder = GWT.create(PersonUiBinder.class);

    public PersonPanel(String personType) {
        institution = new SuggestBox(institutionSuggestOracle);
        country = new SuggestBox(countrySuggestionOracle);

        initWidget(ourUiBinder.createAndBindUi(this));
        List<String> idNames = new ArrayList<String>();
        List<String> idValues = new ArrayList<String>();
        idNames.add("ORCID ");
        idValues.add("orcid");
        idNames.add("Researcher ID ");
        idValues.add("researcherId");
        idType.init("Pick and ID Type ", idNames, idValues);

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
                List<EditorError> result = new ArrayList<EditorError>();
                String valueStr = value == null ? "" : value.toString();
                if (valueStr.length() == 0) {
                    return result;
                }
                // from http://emailregex.com/
                //RegExp p = RegExp.compile("(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                if (!emailRegex.test(valueStr)) {
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
//                OAPMetadataEditor.logToConsole("event:"+ event.getNativeEvent().getType());
                if (!editing && "mouseover".equals(event.getNativeEvent().getType())) {
                    show(event.getValue(), false);
                } else if (!editing && "mouseout".equals(event.getNativeEvent().getType())) {
                    reset();
                }
            }

        });

//        Column<Person, String> edit = new Column<Person, String>(new ButtonCell(IconType.EDIT, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL)) {
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

        // Add a date column to show the birthday.
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


//            String valueStr = value == null ? "" : value.toString();
//                if (valueStr.length() == 0) {
//                return result;
//            }


//            public String getStyleNames(Person row, int rowIndex) {
//                boolean issue = false;
//                if (((row.getInstitution() == null) || (row.getInstitution().isEmpty()))
//                        || ((row.getFirstName() == null) || (row.getFirstName().isEmpty()))
//                        || ((row.getLastName() == null) || (row.getLastName().isEmpty()))) {
//                    issue = true;
//                }
//
//                OAPMetadataEditor.debugLog("row.getEmail(): " + row.getEmail());
//                OAPMetadataEditor.debugLog("row.getEmail() string length: " + row.getEmail().toString().length());
//                // has email then check if valid
//                if (row.getEmail().toString().length() != 0) {
//                    OAPMetadataEditor.debugLog("email is not null or empty?");
//                    if (!emailRegex.test(row.getEmail())) {
//                        issue = true;
//                    }
//                }
//
//                if (issue == true) {
//                    OAPMetadataEditor.debugLog("getCssName(TableContextualType.DANGER): " + TableContextualType.DANGER.getCssName());
//                    return TableContextualType.DANGER.getCssName();
//                } else {
//                    return "";
//                }
//            }
//        });
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

//        country.addValueChangeHandler(new ValueChangeHandler<String>() {
//                  @Override
//                  public void onValueChange(ValueChangeEvent<String> event) {
//                      modified = true;
//                  }
//              }
//        );
    }

    public boolean valid() {
        // For some reason this returns a "0" in debug mode.
        String valid = String.valueOf(form.validate());
//        OAPMetadataEditor.debugLog("person-valid; String.valueOf: " + valid);
        return !(valid.equals("false") || valid.equals("0"));
    }

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

        if (!valid()) {
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
            boolean meetsRequired = true;
            for (int i = 0; i < peopleData.getList().size(); i++) {
                Person p = peopleData.getList().get(i);
                if (((p.getInstitution() == null) || (p.getInstitution().isEmpty()))
                        || ((p.getFirstName() == null) || (p.getFirstName().isEmpty()))
                        || ((p.getLastName() == null) || (p.getLastName().isEmpty()))
                        || ((!emailRegex.test(p.getEmail())) && ((p.getEmail() != null) || (!p.getEmail().isEmpty())))) {
                    meetsRequired = false;
                }
            }
            if (meetsRequired == true && peopleData.getList().size() > 0) {
                eventBus.fireEventFromSource(new SectionSave(getPerson(), this.type), PersonPanel.this);
            }

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
        person.setRid(rid.getText().trim());
        person.setTelephone(telephone.getText().trim());
        person.setExtension(extension.getText().trim());
        person.setCity(city.getText().trim());
        person.setState(state.getText().trim());
        person.setZip(zip.getText().trim());
        person.setCountry(country.getText().trim());
        person.setIdType(idType.getValue());
        person.setComplete(this.valid());
        person.setPosition(editIndex);
        return person;
    }

    @Override
    public boolean isDirty(Person original) {
        OAPMetadataEditor.debugLog("PersonPanel.isDirty(" + original + ")");
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
                        isDirty(rid, original.getRid()) ||
                        isDirty(telephone, original.getTelephone()) ||
                        isDirty(extension, original.getExtension()) ||
                        isDirty(city, original.getCity()) ||
                        isDirty(state, original.getState()) ||
                        isDirty(zip, original.getZip()) ||
                        isDirty(country, original.getCountry());
        OAPMetadataEditor.debugLog("PersonPanel.isDirty:" + isDirty);
        return isDirty;
    }

    //    public boolean hasBeenModified() {
//        return modified;
//    }
    public boolean hasContent() {
        OAPMetadataEditor.debugLog("PersonPanel.hasContent()");
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
        if (rid.getText().trim() != null && !rid.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.rid:" + rid.getText());
            hasContent = true;
        }
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
        if (country.getText().trim() != null && !country.getText().isEmpty()) {
            OAPMetadataEditor.debugLog("PersonPanel.country:" + country.getText());
            hasContent = true;
        }
        if (hasContent == true) {
            save.setEnabled(true);
        }
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
        rid.setEnabled(editable);
        telephone.setEnabled(editable);
        extension.setEnabled(editable);
        city.setEnabled(editable);
        state.setEnabled(editable);
        zip.setEnabled(editable);
        country.setEnabled(editable);
    }

    public void show(Person person) {
        if (person == null) {
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
        if (person.getRid() != null)
            rid.setText(person.getRid());
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
            country.setText(person.getCountry());
        if (person.getIdType() != null) {
            idType.setSelected(person.getIdType());
        }
//        modified = false;
//        editing = false;
    }

    //    @UiHandler({"firstName","mi","lastName", "address1","address2","city","state","zip","telephone","email","rid"})
//    // can't include "idType", because removed ButtonDropDown addChangeHandler because it was causing other problems...
//    // can't include "institution" because SuggestionBox doesn't have addHandler method.
//    public void elementChanged(ChangeEvent changeEvent) {
//        OAPMetadataEditor.logToConsole("event:"+ changeEvent.getSource()); // .getNativeEvent().getType());
////        modified = true;
////        editing = true;
//    }
    @UiHandler({"firstName", "mi", "lastName", "address1", "address2", "city", "state", "zip", "telephone", "email", "rid"})
    public void onChange(ChangeEvent event) {
        OAPMetadataEditor.debugLog("getsource: " + event.getSource());
        save.setEnabled(true);
    }

    @UiHandler({"institution", "country"})
    public void onValueChange(ValueChangeEvent<String> event) {
//            Window.alert("Here be the new value:" + event.getValue());
        OAPMetadataEditor.debugLog("Here be the new value:" + event.getValue());
        save.setEnabled(true);
    }

//    @UiHandler("idType")
//    public void onClick(ClickEvent event) {
//        OAPMetadataEditor.debugLog("Here be the new value:" + event.getSource());
//    }
//    public void onDropDownButtonValueChange(@SuppressWarnings("unused") ValueChangeEvent<String> event) {
//        OAPMetadataEditor.debugLog("Here be the new value:" + event.getValue());
//    }

//    public void onClick(ClickEvent event) {
//    public void onClick(ValueChangeEvent event) {
//        OAPMetadataEditor.debugLog("getsource: "+event.getSource());
//        save.setEnabled(true);
//    }
//    public void onChange(ChangeEvent event) {
//        OAPMetadataEditor.debugLog("getsource: "+event.getSource());
//        save.setEnabled(true);
//    }

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

//    public int getPageTableSize() {
//        if (people.getPageSize() > 0) {
//            return people.getPageSize();
//        }
//        return pageSize;
//    }
//
//    public void setPageTableSize(int size) {
//        int min = 0;
//        int max = 255;
//        if ((size > min) && (size < max)) {
//            people.setPageSize(size);
//        } else {
//            OAPMetadataEditor.debugLog("Out of specified range bounds [" + min + ", " + max + "]: " + size);
//        }
//    }
//
//    public void setRowContextualClass(int index, String contextualClass) {
//        if (index >= 0) {
//            OAPMetadataEditor.debugLog("passed this index: " + index);
//            OAPMetadataEditor.debugLog("passed this style: " + contextualClass);
//            OAPMetadataEditor.debugLog("people row element is: " + people.getRowElement(index));
//            people.getRowElement(index).addClassName(contextualClass);
//        }
//    }
//
//    public void removeRowContextualClass(int index, String contextualClass) {
//        if (index >= 0) {
//            people.getRowElement(index).removeClassName(contextualClass);
//        }
//    }
//
//    public boolean hasRowContextualClass(int index, String contextualClass) {
//        OAPMetadataEditor.debugLog("passed this index: " + index);
//        OAPMetadataEditor.debugLog("passed this style: " + contextualClass);
//        OAPMetadataEditor.debugLog("people row element is: " + people.getRowElement(index));
//        OAPMetadataEditor.debugLog("contextualClass is: " + people.getRowElement(index).getClassName());
//        OAPMetadataEditor.debugLog("contextualClass has: " + people.getRowElement(index).hasClassName(contextualClass));
//        return true;
//    }

    public void reset() {
        form.reset();
        displayedPerson = null;
        editIndex = -1;
        editing = false;
        if (editPerson != null) {
            show(editPerson);
            editPerson = null;
        }
        setAllEditable(true);
        setEnableTableRowButtons(true);
    }
}

