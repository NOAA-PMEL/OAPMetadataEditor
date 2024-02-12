package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.shared.bean.Citation;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

/**
 * Created by rhs on 3/3/17.
 */
public class CitationPanel extends FormPanel<Citation> implements GetsDirty<Citation> {

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    @UiField
    TextBox title;
    @UiField
    TextArea datasetAbstract;
    @UiField
    TextArea useLimitation;
    @UiField
    TextArea purpose;
    @UiField
    TextBox researchProjects;
    @UiField
    TextBox expocode;
    @UiField
    TextBox cruiseId;
    @UiField
    TextBox section;
    @UiField
    TextBox citationAuthorList;
    @UiField
    TextBox references;
    @UiField
    TextArea supplementalInformation;

    @UiField
    Button save;
    @UiField
    Button clear;

    String type = Constants.SECTION_CITATION;

    interface CitationUiBinder extends UiBinder<HTMLPanel, CitationPanel> {
    }

    private static CitationUiBinder ourUiBinder = GWT.create(CitationUiBinder.class);

    public CitationPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        clear.addClickHandler(clearIt);
    }

    public Citation getCitation() {
        Citation citation = dbItem != null ? (Citation)dbItem : new Citation();
        citation.setDatasetAbstract(datasetAbstract.getText().trim());
        citation.setUseLimitation(useLimitation.getText().trim());
        citation.setPurpose(purpose.getText().trim());
        citation.setResearchProjects(researchProjects.getText().trim());
        citation.setTitle(title.getText().trim());
        citation.setExpocode(expocode.getText().trim());
        citation.setCruiseId(cruiseId.getText().trim());
        citation.setSection(section.getText().trim());
        citation.setCitationAuthorList(citationAuthorList.getText().trim());
        citation.setScientificReferences(references.getText().trim());
        citation.setSupplementalInformation(supplementalInformation.getText().trim());
        return citation;
    }
    public boolean isDirty(Citation original) {
        OAPMetadataEditor.debugLog("CitationPanel.isDirty("+original+")");
        boolean isDirty = false;
        isDirty =
            original == null ?
            this.hasContent() :
            isDirty(datasetAbstract, original.getDatasetAbstract() ) ||
            isDirty(useLimitation, original.getUseLimitation() ) ||
            isDirty(purpose, original.getPurpose() ) ||
            isDirty(researchProjects, original.getResearchProjects() ) ||
            isDirty(title, original.getTitle() ) ||
            isDirty(expocode, original.getExpocode() ) ||
            isDirty(cruiseId, original.getCruiseId() ) ||
            isDirty(section, original.getSection() ) ||
            isDirty(citationAuthorList, original.getCitationAuthorList() ) ||
            isDirty(references, original.getScientificReferences() ) ||
            isDirty(supplementalInformation, original.getSupplementalInformation() );
        OAPMetadataEditor.debugLog("CitationPanel.isDirty:"+isDirty);
        return isDirty;
    }
    public boolean isDirty() {
        return isDirty(getDbItem());
    }
    public boolean hasContent() {
        if (datasetAbstract.getText().trim() != null && !datasetAbstract.getText().isEmpty() ) {
            return true;
        }
        if (useLimitation.getText().trim() != null && !useLimitation.getText().isEmpty() ) {
            return true;
        }
        if (purpose.getText().trim() != null && !purpose.getText().isEmpty() ) {
            return true;
        }
        if (researchProjects.getText().trim() != null && !researchProjects.getText().isEmpty() ) {
            return true;
        }
        if (title.getText().trim() != null && !title.getText().isEmpty() ) {
            return true;
        }
        if (expocode.getText().trim() != null && !expocode.getText().isEmpty() ) {
            return true;
        }
        if (cruiseId.getText().trim() != null && !cruiseId.getText().isEmpty() ) {
            return true;
        }
        if (section.getText().trim() != null && !section.getText().isEmpty() ) {
            return true;
        }
        if (citationAuthorList.getText().trim() != null && !citationAuthorList.getText().isEmpty() ) {
            return true;
        }
        if (references.getText().trim() != null && !references.getText().isEmpty() ) {
            return true;
        }
        if (supplementalInformation.getText().trim() != null && !supplementalInformation.getText().isEmpty() ) {
            return true;
        }
        return false;
    }
    public void show(Citation citation) {
        setDbItem(citation);
        if ( citation.getTitle() != null ) {
            title.setText(citation.getTitle());
        }
        if ( citation.getDatasetAbstract() != null ) {
            datasetAbstract.setText(citation.getDatasetAbstract() );
        }
        if ( citation.getUseLimitation() != null ) {
            useLimitation.setText(citation.getUseLimitation() );
        }
        if ( citation.getPurpose() != null ) {
            purpose.setText(citation.getPurpose() );
        }
        if ( citation.getResearchProjects() != null ) {
            researchProjects.setText(citation.getResearchProjects());
        }
        if ( citation.getExpocode() != null ) {
            expocode.setText(citation.getExpocode());
        }
        if ( citation.getCruiseId() != null ) {
            cruiseId.setText(citation.getCruiseId());
        }
        if ( citation.getSection() != null ) {
            section.setText(citation.getSection());
        }
        if ( citation.getCitationAuthorList() != null ) {
            citationAuthorList.setText(citation.getCitationAuthorList());
        }
        if ( citation.getScientificReferences() != null ) {
            references.setText(citation.getScientificReferences());
        }
        if ( citation.getSupplementalInformation() != null ) {
            supplementalInformation.setText(citation.getSupplementalInformation());
        }
    }
    @UiHandler("save")
    public void onSave(ClickEvent clickEvent) {
        // For some reason this returns a "0" in debug mode.
        String valid = String.valueOf(form.validate());
        if (valid.equals("false") || valid.equals("0")) {
            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.WARNING);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.NOT_COMPLETE, settings);
        } else {
            NotifySettings settings = NotifySettings.newSettings();
            settings.setType(NotifyType.SUCCESS);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.COMPLETE, settings);
            eventBus.fireEventFromSource(new SectionSave(getCitation(), this.type), CitationPanel.this);
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
}