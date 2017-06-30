package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhs on 3/8/17.
 */
public class Dic2Panel extends Composite {
    /*

    012 DIC: Standardization technique description
013 DIC: Frequency of standardization
014 DIC: CRM manufacturer
015 DIC: Batch number
017 DIC: Poison used to kill the sample
018 DIC: Poison volume
019 DIC Poisoning correction description

     */

    @UiField
    Heading heading;

    @UiField
    Button save;

    @UiField
    Form form;

    // 012 Standardization technique description
    @UiField
    TextBox standardizationTechnique;

    // 013 Frequency of standardization
    @UiField
    TextBox freqencyOfStandardization;

    // 014 CRM manufacturer
    @UiField
    TextBox crmManufacture;

    // 015 Batch Number
    @UiField
    TextBox batchNumber;

    // 017 Poison used to kill the sample
    @UiField
    TextBox poison;

    // 018 Poison volume
    @UiField
    TextBox poisonVolume;

    // 019 Poisoning correction description
    @UiField
    TextBox poisonDescription;


    // The form groups that hold the labels and form entry widgets (textbox, textarea and dropdowns).


    // 012 Standardization technique description
    @UiField
    FormGroup standardizationTechniqueForm;

    // 013 Frequency of standardization
    @UiField
    FormGroup freqencyOfStandardizationForm;

    // 014 CRM manufacturer
    @UiField
    FormGroup crmManufactureForm;

    // 015 Batch Number
    @UiField
    FormGroup batchNumberForm;

    // 017 Poison used to kill the sample
    @UiField
    FormGroup poisonForm;

    // 018 Poison volume
    @UiField
    FormGroup poisonVolumeForm;

    // 019 Poisoning correction description
    @UiField
    FormGroup poisonDescriptionForm;

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();


//TODO initialize the cell type dropdown.

    interface Dic2PanelUiBinder extends UiBinder<HTMLPanel, Dic2Panel> {
    }

    private static Dic2PanelUiBinder ourUiBinder = GWT.create(Dic2PanelUiBinder.class);

    public Dic2Panel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        heading.setText("Continue entering information for Dissolved Inorganic Carbon (DIC)");
        save.addClickHandler(saveIt);
    }
    public void show(Variable variable) {

        if ( variable.getStandardizationTechnique() != null ) {
            standardizationTechnique.setText(variable.getStandardizationTechnique());
        }

        if ( variable.getFreqencyOfStandardization() != null ) {
            freqencyOfStandardization.setText(variable.getFreqencyOfStandardization());
        }

        if ( variable.getCrmManufacture() != null ) {
            crmManufacture.setText(variable.getCrmManufacture());
        }

        if ( variable.getBatchNumber() != null ) {
            batchNumber.setText(variable.getBatchNumber());
        }

        if ( variable.getPoison() != null ) {
            poison.setText(variable.getPoison());
        }

        if ( variable.getPoisonVolume() != null ) {
            poisonVolume.setText(variable.getPoisonVolume());
        }

        if (variable.getPoisonDescription() != null ) {
            poisonDescription.setText(variable.getPoisonDescription());
        }

    }

    public Variable fill(Variable variable) {

        variable.setStandardizationTechnique(standardizationTechnique.getText().trim());
        variable.setFreqencyOfStandardization(freqencyOfStandardization.getText().trim());
        variable.setCrmManufacture(crmManufacture.getText().trim());
        variable.setBatchNumber(batchNumber.getText());
        variable.setPoison(poison.getText());
        variable.setPoisonVolume(poisonVolume.getText());
        variable.setPoisonDescription(poisonDescription.getText());

        return variable;

    }

    public boolean isDirty() {

        if (standardizationTechnique.getText() != null && !standardizationTechnique.getText().isEmpty() ) {
            return true;
        }
        if (freqencyOfStandardization.getText() != null && !freqencyOfStandardization.getText().isEmpty() ) {
            return true;
        }
        if (crmManufacture.getText() != null && !crmManufacture.getText().isEmpty() ) {
            return true;
        }
        if (batchNumber != null && !batchNumber.getText().isEmpty() ) {
            return true;
        }
        if (poison != null && !poison.getText().isEmpty() ) {
            return true;
        }
        if (poisonVolume != null && !poisonVolume.getText().isEmpty() ) {
            return true;
        }
        if (poisonDescription != null && !poisonDescription.getText().isEmpty() ) {
            return true;
        }
        return false;
    }

    ClickHandler saveIt = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            String valid = String.valueOf( form.validate());
            if ( valid.equals("false") ||
                    valid.equals("0")) {
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.WARNING);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.NOT_COMPLETE, settings);
            } else {
                eventBus.fireEventFromSource(new SectionSave(null, Constants.SECTION_DIC2), Dic2Panel.this);
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.SUCCESS);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.COMPLETE, settings);
            }
        }
    };
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