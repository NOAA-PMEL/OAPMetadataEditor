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
public class Ta2Panel extends Composite {

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

    // 027 Cell type (open or closed)
    @UiField
    ButtonDropDown cellType;

    // 029 Curve fitting method
    @UiField
    TextBox curveFittingMethod;

    // 042 Magnitude of blank correction
    @UiField
    TextBox magnitudeOfBlankCorrection;

    // 055 Type of titration
    @UiField
    TextBox titrationType;


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

    // 027 Cell type (open or closed)
    @UiField
    FormGroup cellTypeForm;

    // 029 Curve fitting method
    @UiField
    FormGroup curveFittingMethodForm;

    // 042 Magnitude of blank correction
    @UiField
    FormGroup magnitudeOfBlankCorrectionForm;

    // 055 Type of titration
    @UiField
    FormGroup titrationTypeForm;

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    public void reset() {
        form.reset();
    }


//TODO initialize the cell type dropdown.

    interface VariablePanelUiBinder extends UiBinder<HTMLPanel, Ta2Panel> {
    }

    private static VariablePanelUiBinder ourUiBinder = GWT.create(VariablePanelUiBinder.class);

    public Ta2Panel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        heading.setText("Continue entering the information for Total Alkalinity (TA)");
        save.addClickHandler(saveIt);
        List<String> name = new ArrayList<String>();
        List<String> value = new ArrayList<String>();
        name.add("Open");
        value.add("open");
        name.add("Closed");
        value.add("closed");
        cellType.init("Cell Type: Open or Closed", name, value);
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

        if (variable.getBatchNumber() != null ) {
            batchNumber.setText(variable.getBatchNumber());
        }

        if ( variable.getPoison() != null ) {
            poison.setText(variable.getPoison());
        }

        if ( variable.getPoisonVolume() != null ) {
            poisonVolume.setText(variable.getPoisonVolume());
        }

        if ( variable.getPoisonDescription() != null ) {
            poisonDescription.setText(variable.getPoisonDescription());
        }

        if ( variable.getCellType() != null ) {
            cellType.setSelected(variable.getCellType());
        }

        if ( variable.getCurveFittingMethod() != null ) {
            curveFittingMethod.setText(variable.getCurveFittingMethod());
        }

        if ( variable.getMagnitudeOfBlankCorrection() != null ) {
            magnitudeOfBlankCorrection.setText(variable.getMagnitudeOfBlankCorrection());
        }

        if ( variable.getTitrationType() != null ) {
            titrationType.setText(variable.getTitrationType());
        }

    }
    public Variable fill(Variable ta) {
        ta.setStandardizationTechnique(standardizationTechnique.getText());
        ta.setFreqencyOfStandardization(freqencyOfStandardization.getText());
        ta.setCrmManufacture(crmManufacture.getText());
        ta.setBatchNumber(batchNumber.getText());
        ta.setPoison(poison.getText());
        ta.setPoisonVolume(poisonVolume.getText());
        ta.setPoisonDescription(poisonDescription.getText());
        ta.setCellType(cellType.getValue());
        ta.setCurveFittingMethod(curveFittingMethod.getText());
        ta.setMagnitudeOfBlankCorrection(magnitudeOfBlankCorrection.getText());
        ta.setTitrationType(titrationType.getText());
        return ta;
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
        if (batchNumber.getText() != null && !batchNumber.getText().isEmpty() ) {
            return true;
        }
        if (poison.getText() != null && !poison.getText().isEmpty() ) {
            return true;
        }
        if (poisonVolume.getText() != null && !poisonVolume.getText().isEmpty() ) {
            return true;
        }
        if (poisonDescription.getText() != null && !poisonDescription.getText().isEmpty() ) {
            return true;
        }
        if (curveFittingMethod.getText() != null && !curveFittingMethod.getText().isEmpty() ) {
            return true;
        }
        if (magnitudeOfBlankCorrection.getText() != null && !magnitudeOfBlankCorrection.getText().isEmpty() ) {
            return true;
        }
        if (titrationType.getText() != null && !titrationType.getText().isEmpty() ) {
            return true;
        }
        return false;
    }
    public ClickHandler saveIt = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            // For some reason this returns a "0" in debug mode.
            String valid = String.valueOf(form.validate());
            if (valid.equals("false") ||
                    valid.equals("0")) {
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.WARNING);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.NOT_COMPLETE, settings);
            } else {
                eventBus.fireEventFromSource(new SectionSave(null, Constants.SECTION_TA2), Ta2Panel.this);
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
}