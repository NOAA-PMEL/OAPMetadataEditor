package gov.noaa.pmel.sdig.shared.bean;

import com.google.gwt.core.client.GWT;
import gov.noaa.pmel.sdig.client.panels.PersonPanel;
import gov.noaa.pmel.sdig.shared.HasContent;
import gov.noaa.pmel.sdig.shared.IsValid;
import gov.noaa.pmel.sdig.shared.Stringy;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhs on 2/28/17.
 */
public class Person extends Ordered implements Comparable<Person>, Stringy, HasContent, IsValid {
    String lastName;
    String mi;
    String firstName;
    String institution;
    String address1;
    String address2;
    String telephone;
    String extension;
    String email;
    String city;
    String state;
    String zip;
    String country;

    List<TypedString> researcherIds;

    boolean complete = true;
    public boolean isComplete() { return complete; }
    public void setComplete(boolean isComplete) { complete = isComplete; }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMi() {
        return mi;
    }

    public void setMi(String mi) {
        this.mi = mi;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getAddress1() { return address1; }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public synchronized List<TypedString> getResearcherIds() {
        if ( researcherIds == null ) {
            researcherIds = new ArrayList<>();
        }
        return researcherIds;
    }

    public void addResearcherId(TypedString rid) {
        getResearcherIds().add(rid);
    }

    public void setResearcherIds(List<TypedString> researcherIds) {
        this.researcherIds = researcherIds;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int compareTo(Person other) {
        if (other == null) { return 1; }
        int z = sCompare(lastName, other.lastName);
        if ( z != 0 ) { return z; }
        z = sCompare(firstName, other.firstName);
        if ( z != 0 ) { return z; }
        z = sCompare(mi, other.mi);
        if ( z != 0 ) { return z; }
        z = sCompare(institution, other.institution);
        if ( z != 0 ) { return z; }
        z = sCompare(address1, other.address1);
        if ( z != 0 ) { return z; }
        z = sCompare(address2, other.address2);
        if ( z != 0 ) { return z; }
        z = sCompare(telephone, other.telephone);
        if ( z != 0 ) { return z; }
        z = sCompare(extension, other.extension);
        if ( z != 0 ) { return z; }
        z = sCompare(email, other.email);
        if ( z != 0 ) { return z; }
//        z = sCompare(rid, other.rid);
//        if ( z != 0 ) { return z; }
        z = sCompare(city, other.city);
        if ( z != 0 ) { return z; }
        z = sCompare(state, other.state);
        if ( z != 0 ) { return z; }
        z = sCompare(zip, other.zip);
        if ( z != 0 ) { return z; }
        z = sCompare(country, other.country);
        return z;
//        if ( z != 0 ) { return z; }
//        return sCompare(idType, other.idType);
    }
    public boolean isTheSameAs(Person other) {
        if ( other == null ) { return false; }
        return
            sAreEffectivelyTheSame(lastName, other.lastName) &&
            sAreEffectivelyTheSame(mi, other.mi) &&
            sAreEffectivelyTheSame(firstName, other.firstName) &&
            sAreEffectivelyTheSame(institution, other.institution) &&
            sAreEffectivelyTheSame(address1, other.address1) &&
            sAreEffectivelyTheSame(address2, other.address2) &&
            sAreEffectivelyTheSame(telephone, other.telephone) &&
            sAreEffectivelyTheSame(extension, other.extension) &&
            sAreEffectivelyTheSame(email, other.email) &&
//            sAreEffectivelyTheSame(rid, other.rid) &&
            sAreEffectivelyTheSame(city, other.city) &&
            sAreEffectivelyTheSame(state, other.state) &&
            sAreEffectivelyTheSame(zip, other.zip) &&
            sAreEffectivelyTheSame(country, other.country); //  &&
//            sAreEffectivelyTheSame(idType, other.idType);
    }

    @Override
    public Person sClone() {
        Person newPerson = new Person();
        newPerson.position = this.position;
//        newPerson.id = this.id;
//        newPerson.version = this.version;
        newPerson.lastName = this.lastName;
        newPerson.mi = this.mi;
        newPerson.firstName = this.firstName;
        newPerson.institution = this.institution;
        newPerson.address1 = this.address1;
        newPerson.address2 = this.address2;
        newPerson.telephone = this.telephone;
        newPerson.extension = this.extension;
        newPerson.email = this.email;
//        newPerson.rid = this.rid;
        newPerson.city = this.city;
        newPerson.state = this.state;
        newPerson.zip = this.zip;
        newPerson.country = this.country;
//        newPerson.idType = this.idType;
        return newPerson;
    }

    public boolean hasContent() {
        boolean hasContent = ! (
                    isEmpty(lastName) &&
                    isEmpty(mi) &&
                    isEmpty(firstName) &&
                    isEmpty(institution) &&
                    isEmpty(address1) &&
                    isEmpty(address2) &&
                    isEmpty(telephone) &&
                    isEmpty(extension) &&
                    isEmpty(email) &&
//                    isEmpty(rid) &&
                    isEmpty(city) &&
                    isEmpty(state) &&
                    isEmpty(zip) &&
                    isEmpty(country) &&
                    isEmpty(researcherIds));
        return hasContent;
    }

    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (IllegalStateException isx) {
            return false;
        }
    }
    public void validate()      // TODO: I guess should check empty fields, etc.
        throws IllegalStateException
    {
        GWT.log("Person validate");
        String email = getEmail();
        if ( ! (email == null || email.trim().isEmpty())) {
            if ( !PersonPanel.emailRegex.test(email)) {
                String fixedAlitte = email.replaceAll("<", "&lt;").replaceAll(">", "&gt;").trim();
                throw new IllegalStateException("Invalid email: " +fixedAlitte);
            }
        }
        for (TypedString rid : getResearcherIds()) {
            if ( (! rid.getValue().isEmpty()) && rid.getType().isEmpty() ) {
                throw new IllegalStateException("Missing type for ID " + rid.getValue());
            }
        }
    }
}

