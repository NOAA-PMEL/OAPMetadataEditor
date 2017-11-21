package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.Person;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.Popover;
import org.gwtbootstrap3.client.ui.TextBox;
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
public class PersonPanel extends Composite {
    @UiField
    ButtonDropDown idType;
    @UiField
    Button save;
    @UiField
    Form form;

    @UiField
    TextBox lastName;
    @UiField
    TextBox mi;
    @UiField
    TextBox firstName;

    @UiField
    TextBox institution;
    @UiField
    TextBox address1;
    @UiField
    TextBox address2;
    @UiField
    TextBox telephone;
    @UiField
    TextBox extension;
    @UiField
    TextBox city;
    @UiField
    TextBox state;
    @UiField
    TextBox zip;
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

    String type;

    boolean dirty = false;

    int editIndex = -1;

    @UiField
    Pagination peoplePagination;

    ListDataProvider<Person> peopleData = new ListDataProvider<Person>();

    private SimplePager cellTablePager = new SimplePager();

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    interface PersonUiBinder extends UiBinder<HTMLPanel, PersonPanel> {
    }

    private static PersonUiBinder ourUiBinder = GWT.create(PersonUiBinder.class);

    public PersonPanel() {


        initWidget(ourUiBinder.createAndBindUi(this));
        List<String> idNames = new ArrayList<String>();
        List<String> idValues = new ArrayList<String>();
        idNames.add("ORCID ");
        idValues.add("orcid");
        idNames.add("Researcher ID ");
        idValues.add("researcherId");
        idType.init("Pick and ID Type ", idNames, idValues);

        telephone.addValidator(new Validator() {
            @Override
            public List<EditorError> validate(Editor editor, Object value) {
                List<EditorError> result = new ArrayList<EditorError>();
                String valueStr = value == null ? "" : value.toString();
                // The more complex of the two answers here:
                // https://stackoverflow.com/questions/16699007/regular-expression-to-match-standard-10-digit-phone-number
                //RegExp p = RegExp.compile("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$");
                RegExp p = RegExp.compile("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$");
                if ( !p.test(valueStr) ) {
                    result.add(new BasicEditorError(telephone, value, "Does not look like a phone number to me."));
                }
                return result;
            }

            @Override
            public int getPriority() {
                return Priority.HIGH;
            }
        });

        email.addValidator(new Validator() {
            @Override
            public List<EditorError> validate(Editor editor, Object value) {
                List<EditorError> result = new ArrayList<EditorError>();
                String valueStr = value == null ? "" : value.toString();
                // from http://emailregex.com/
                RegExp p = RegExp.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                if ( !p.test(valueStr) ) {
                    result.add(new BasicEditorError(telephone, value, "Does not look like a phone number to me."));
                }
                return result;
            }

            @Override
            public int getPriority() {
                return Priority.HIGH;
            }
        });

        people.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);


        Column<Person, String> edit = new Column<Person, String>(new ButtonCell(IconType.EDIT, ButtonType.PRIMARY, ButtonSize.EXTRA_SMALL)) {
            @Override
            public String getValue(Person object) {
                return "Edit";
            }
        };
        edit.setFieldUpdater(new FieldUpdater<Person, String>() {
            @Override
            public void update(int index, Person person, String value) {
                editIndex = peopleData.getList().indexOf(person);
                if ( editIndex < 0 ) {
                    Window.alert("Edit failed.");
                } else {
                    show(person);
                    peopleData.getList().remove(person);
                    peopleData.flush();
                    peoplePagination.rebuild(cellTablePager);
                }

            }
        });
        people.addColumn(edit);

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

        Column<Person, String> delete = new Column<Person, String>(new ButtonCell(IconType.TRASH, ButtonType.DANGER, ButtonSize.EXTRA_SMALL)) {
            @Override
            public String getValue(Person object) {
                return "Delete";
            }
        };
        delete.setFieldUpdater(new FieldUpdater<Person, String>() {
            @Override
            public void update(int index, Person person, String value) {
                peopleData.getList().remove(person);
                peopleData.flush();
                peoplePagination.rebuild(cellTablePager);
                if ( peopleData.getList().size() == 0 ) {
                    setTableVisible(false);
                } else {
                    setTableVisible(true);
                }
            }
        });
        people.addColumn(delete);

        people.addRangeChangeHandler(new RangeChangeEvent.Handler() {

            @Override
            public void onRangeChange(final RangeChangeEvent event) {
                peoplePagination.rebuild(cellTablePager);
            }
        });

        cellTablePager.setDisplay(people);

        people.setPageSize(4);

        peopleData.addDataDisplay(people);

    }

    @UiHandler("save")
    public void onSave(ClickEvent clickEvent) {


        // For some reason this returns a "0" in debug mode.
        String valid = String.valueOf( form.validate());
        if ( valid.equals("false") ||
             valid.equals("0")) {
            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.WARNING);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.NOT_COMPLETE, settings);
        } else {
            eventBus.fireEventFromSource(new SectionSave(getPerson(), this.type), PersonPanel.this);
            peopleData.getList().add(getPerson());
            peopleData.flush();
            peoplePagination.rebuild(cellTablePager);
            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.SUCCESS);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.COMPLETE, settings);
            if ( showTable ) {
                setTableVisible(true);
                form.reset();
            }
        }

    }
    public Person getPerson() {
        Person person = new Person();
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
        person.setIdType(idType.getValue());
        return person;
    }
    public boolean isDirty() {
        if (address1.getText().trim() != null && !address1.getText().isEmpty() ) {
            return true;
        }
        if (address2.getText().trim() != null && !address1.getText().isEmpty() ) {
            return true;
        }
        if (email.getText().trim() != null && !email.getText().isEmpty() ) {
            return true;
        }
        if (firstName.getText().trim() != null && !firstName.getText().isEmpty() ) {
            return true;
        }
        if (institution.getText().trim() != null && !institution.getText().isEmpty() ) {
            return true;
        }
        if (lastName.getText().trim() != null && !lastName.getText().isEmpty() ) {
            return true;
        }
        if (mi.getText().trim() != null && !mi.getText().isEmpty() ) {
            return true;
        }
        if (rid.getText().trim() != null && !rid.getText().isEmpty() ) {
            return true;
        }
        if (telephone.getText().trim() != null && !telephone.getText().isEmpty() ) {
            return true;
        }
        if (extension.getText().trim() != null && !extension.getText().isEmpty() ) {
            return true;
        }
        if (city.getText().trim() != null && !city.getText().isEmpty() ) {
            return true;
        }
        if (state.getText().trim() != null && !state.getText().isEmpty() ) {
            return true;
        }
        if (zip.getText().trim() != null && !zip.getText().isEmpty() ) {
            return true;
        }
        return false;
    }
    public void show(Person person) {
        if ( person.getAddress1() != null )
            address1.setText(person.getAddress1());
        if ( person.getAddress2() != null )
            address2.setText(person.getAddress2());
        if ( person.getEmail() != null )
            email.setText(person.getEmail());
        if ( person.getFirstName() != null )
            firstName.setText(person.getFirstName());
        if ( person.getInstitution() != null )
            institution.setText(person.getInstitution());
        if ( person.getLastName() != null )
            lastName.setText(person.getLastName());
        if ( person.getTelephone() != null )
            mi.setText(person.getMi());
        if ( person.getRid() != null )
            rid.setText(person.getRid());
        if ( person.getTelephone() != null )
            telephone.setText(person.getTelephone());
        if (person.getExtension() != null )
            extension.setText(person.getExtension());
        if ( person.getCity() != null )
            city.setText(person.getCity());
        if ( person.getState() != null )
            state.setText(person.getState().trim());
        if ( person.getZip() != null )
            zip.setText(person.getZip());
        if ( person.getIdType() != null ) {
            idType.setSelected(person.getIdType());
        }

    }
    @UiHandler("lastName")
    public void lastNameChanged(ChangeEvent changeEvent) {
        dirty = true;
    }
    @UiHandler("mi")
    public void miChanged(ChangeEvent changeEvent) {
        dirty = true;
    }
    @UiHandler("firstName")
    public void firstNameChanged(ChangeEvent changeEvent) {
        dirty = true;
    }
    @UiHandler("institution")
    public void institutionChanged(ChangeEvent changeEvent) {
        dirty = true;
    }
    @UiHandler("address1")
    public void address1Changed(ChangeEvent changeEvent) {
        dirty = true;
    }
    @UiHandler("address2")
    public void address2Changed(ChangeEvent changeEvent) {
        dirty = true;
    }
    @UiHandler("telephone")
    public void telephoneChanged(ChangeEvent changeEvent) {
        dirty = true;
    }
    @UiHandler("email")
    public void emailChanged(ChangeEvent changeEvent) {
        dirty = true;
    }
    @UiHandler("rid")
    public void ridNameChanged(ChangeEvent changeEvent) {
        dirty = true;
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
        setTableVisible(false);
    }

    public void addPeople(List<Person> personList) {

        for (int i = 0; i < personList.size(); i++) {
            Person p = personList.get(i);
            peopleData.getList().add(p);
        }
        peopleData.flush();
        peoplePagination.rebuild(cellTablePager);
        setTableVisible(true);

    }

    public void setTableVisible(boolean b) {
        people.setVisible(b);
        if ( b ) {
            int page = cellTablePager.getPage();
            if ( cellTablePager.getPageCount() > 1 ) {
                peoplePagination.setVisible(true);
                cellTablePager.setPage(page);
            } else {
                peoplePagination.setVisible(false);
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
    }
}