package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
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
        person.setCity(city.getText().trim());
        person.setState(state.getText().trim());
        person.setZip(zip.getText().trim());
        person.setIdType(idType.getValue());
        return person;
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
        if ( person.getCity() != null )
            city.setText(person.getCity());
        if ( person.getState().trim() != null )
            state.setText(person.getState());
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
        peoplePagination.setVisible(b);
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