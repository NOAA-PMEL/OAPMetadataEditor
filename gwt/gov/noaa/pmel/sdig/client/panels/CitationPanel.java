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
public class CitationPanel extends Composite {

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    @UiField
    TextBox title;
    @UiField
    TextArea platformAbstract;
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
    Form form;

    String type = Constants.SECTION_CITATION;

    public void reset() {
        form.reset();
    }

    interface CitationUiBinder extends UiBinder<HTMLPanel, CitationPanel> {
    }

    private static CitationUiBinder ourUiBinder = GWT.create(CitationUiBinder.class);

    public CitationPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public Citation getCitation() {
        Citation citation = new Citation();
        citation.setPlatformAbstract(platformAbstract.getText().trim());
        citation.setPurpose(purpose.getText().trim());
        citation.setProjects(researchProjects.getText().trim());
        citation.setTitle(title.getText().trim());
        citation.setExpocode(expocode.getText().trim());
        citation.setCruiseId(cruiseId.getText().trim());
        citation.setSection(section.getText().trim());
        citation.setCitationAuthorList(citationAuthorList.getText().trim());
        citation.setScientificReferences(references.getText().trim());
        citation.setSupplementalInformation(supplementalInformation.getText().trim());
        return citation;
    }
    public boolean isDirty() {
        if (platformAbstract.getText() != null && !platformAbstract.getText().isEmpty() ) {
            return true;
        }
        if (purpose.getText() != null && !purpose.getText().isEmpty() ) {
            return true;
        }
        if (researchProjects.getText() != null && !researchProjects.getText().isEmpty() ) {
            return true;
        }
        if (title.getText() != null && !title.getText().isEmpty() ) {
            return true;
        }
        if (expocode.getText() != null && !expocode.getText().isEmpty() ) {
            return true;
        }
        if (cruiseId.getText() != null && !cruiseId.getText().isEmpty() ) {
            return true;
        }
        if (section.getText() != null && !section.getText().isEmpty() ) {
            return true;
        }
        if (citationAuthorList.getText() != null && !citationAuthorList.getText().isEmpty() ) {
            return true;
        }
        if (references.getText() != null && !references.getText().isEmpty() ) {
            return true;
        }
        if (supplementalInformation.getText() != null && !supplementalInformation.getText().isEmpty() ) {
            return true;
        }
        return false;
    }
    public void show(Citation citation) {
        if ( citation.getTitle() != null ) {
            title.setText(citation.getTitle());
        }
        if ( citation.getPlatformAbstract() != null ) {
            platformAbstract.setText(citation.getPlatformAbstract() );
        }
        if ( citation.getPurpose() != null ) {
            purpose.setText(citation.getPurpose() );
        }
        if ( citation.getProjects() != null ) {
            researchProjects.setText(citation.getProjects());
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