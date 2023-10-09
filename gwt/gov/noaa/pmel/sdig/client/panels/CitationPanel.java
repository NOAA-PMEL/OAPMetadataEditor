package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.widgets.ButtonDropDown;
import gov.noaa.pmel.sdig.shared.bean.Citation;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhs on 3/3/17.
 */
public class CitationPanel extends FormPanel<Citation> implements GetsDirty<Citation> {

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    @UiField
    TextArea title;
    @UiField
    TextArea datasetAbstract;
    @UiField
    TextArea useLimitation;
    // SDG 14.3 / SOCAT puts purpose in Abstract
//    @UiField
//    TextArea purpose;
    @UiField
    TextArea researchProjects;
    @UiField
    TextBox expocode;
    @UiField
    TextBox cruiseId;
    @UiField
    ButtonDropDown cruiseIdType;
    @UiField
    TextBox section;
    @UiField
    TextArea citationAuthorList;
    @UiField
    TextArea methodsApplied;
    @UiField
    TextArea references;
    @UiField
    TextArea supplementalInformation;

    @UiField
    Button save;

    @UiField
    FormLabel researchProjectsLabel;
    @UiField
    Modal researchProjectsPopover;

    // SDG 14.3 / SOCAT puts purpose in Abstract
//    @UiField
//    FormLabel purposeLabel;
//    @UiField
//    Modal purposePopover;

    @UiField
    FormLabel referencesLabel;
    @UiField
    Modal referencesPopover;

    String type = Constants.SECTION_CITATION;

    interface CitationUiBinder extends UiBinder<HTMLPanel, CitationPanel> {
    }

    private static CitationUiBinder ourUiBinder = GWT.create(CitationUiBinder.class);

    public CitationPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));

        List<String> idNames = new ArrayList<String>();
        List<String> idValues = new ArrayList<String>();
        idNames.add("ICES ");
        idValues.add("ICES");
        idNames.add("IMO ");
        idValues.add("IMO");
        idNames.add("WMO ");
        idValues.add("WMO");
        cruiseIdType.init("Select ID Type ", idNames, idValues);

//        if (OAPMetadataEditor.getIsSocatParam()) {
//            // researchProjects -> Name of sampling site or title of related research project
//            // No.
//            researchProjectsPopover.setTitle("8 Provide the name of the sampling site/related research project, e.g. BATS, CARIACO.");
//            researchProjectsLabel.setText("Name of sampling site or title of related research project");
//            researchProjects.getElement().setAttribute("placeHolder", "Name of sampling site or title of related research project");
//
//            // purpose (or abstract?) -> Short description including purpose of observation
//            // SDG 14.3 / SOCAT puts purpose into abstract.
//            purposePopover.setTitle("9 A narrative summary of the data set, including a description of the purpose of the observations.");
//            purposeLabel.setText("Short description including purpose of observation");
//            purpose.getElement().setAttribute("placeHolder", "Short description including purpose of observation");
//
//            referencesPopover.setTitle("10 Specify the methodologies applied to charactarize the carbonate system, including references/citations. Please describe if you made any changes to the method as it is described in the literature, e.g. modications of sampling procedures, different bottles used for storage of samples, changes to the dye, etc. Describe precisely how the method differed and what was done instead.");
//            referencesLabel.setText("Method(s) Applied");
//            references.getElement().setAttribute("placeHolder", "Method(s) Applied");
//        }
    }

    public Citation getCitation() {
        Citation citation = dbItem != null ? (Citation)dbItem : new Citation();
        citation.setDatasetAbstract(datasetAbstract.getText().trim());
        citation.setUseLimitation(useLimitation.getText().trim());
//        citation.setPurpose(purpose.getText().trim());
        citation.setResearchProjects(researchProjects.getText().trim());
        citation.setTitle(title.getText().trim());
        citation.setExpocode(expocode.getText().trim());
        String cruiseText = cruiseId.getText().trim();
        String[] cruises = cruiseText.split(" ");
        String idType = cruiseIdType.getValue();
        cruiseText = "";
        for (String cruise : cruises) {
            if ( cruise.trim().isEmpty() ) { continue; }
            if ( idType != null && ! idType.isEmpty()) {
                cruise = cruise + ":" + idType;
            }
            cruiseText += cruise + " ";
        }
        citation.setCruiseId(cruiseText);
        citation.setSection(section.getText().trim());
        citation.setCitationAuthorList(citationAuthorList.getText().trim());
        citation.setScientificReferences(references.getText().trim());
        citation.setSupplementalInformation(supplementalInformation.getText().trim());
        citation.setMethodsApplied(methodsApplied.getText().trim());
//        citation.setCruiseIdType(cruiseIdType.getValue);
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
//            isDirty(purpose, original.getPurpose() ) ||
            isDirty(researchProjects, original.getResearchProjects() ) ||
            isDirty(title, original.getTitle() ) ||
            isDirty(expocode, original.getExpocode() ) ||
//            isDirty(cruiseId, original.getCruiseId() ) ||
            isDirty(cruiseId, ((original.getCruiseId().split(":", 2)[0] != null)
                    ? original.getCruiseId().split(":", 2)[0] : original.getCruiseId() )) ||
            isDirty(section, original.getSection() ) ||
            isDirty(citationAuthorList, original.getCitationAuthorList() ) ||
            isDirty(references, original.getScientificReferences() ) ||
            isDirty(methodsApplied, original.getMethodsApplied() ) ||
            isDirty(supplementalInformation, original.getSupplementalInformation() );
        OAPMetadataEditor.debugLog("CitationPanel.isDirty:"+isDirty);
        return isDirty;
    }
    public boolean hasContent() {
        if (!datasetAbstract.getText().isEmpty() ) {
            return true;
        }
        if (!useLimitation.getText().isEmpty() ) {
            return true;
        }
//        if (!purpose.getText().isEmpty() ) {
//            return true;
//        }
        if (!researchProjects.getText().isEmpty() ) {
            return true;
        }
        if (!title.getText().isEmpty() ) {
            return true;
        }
        if (!expocode.getText().isEmpty() ) {
            return true;
        }
        if (!cruiseId.getText().isEmpty() ) {
            return true;
        }
        if (!section.getText().isEmpty() ) {
            return true;
        }
        if (!citationAuthorList.getText().isEmpty() ) {
            return true;
        }
        if (!methodsApplied.getText().isEmpty() ) {
            return true;
        }
        if (!references.getText().isEmpty() ) {
            return true;
        }
        if (!supplementalInformation.getText().isEmpty() ) {
            return true;
        }
        return false;
    }
    public void show(Citation citation) {
        if (citation == null) {
            reset();
            return;
        }

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
//        if ( citation.getPurpose() != null ) {
//            purpose.setText(citation.getPurpose() );
//        }
        if ( citation.getResearchProjects() != null &&
             ! citation.getResearchProjects().trim().equals("null")) { // XXX TODO: workaround for oads xml issue
            researchProjects.setText(citation.getResearchProjects());
        }
        if ( citation.getExpocode() != null ) {
            expocode.setText(citation.getExpocode());
        }
        if ( citation.getCruiseId() != null ) {
            String cruiseText = "";
            String cruiseInfo = citation.getCruiseId();
            String[] cruises = cruiseInfo.split(" ");
            for (String cruise : cruises) {
                if (cruise.trim().isEmpty()) { continue; }
                int idx = cruiseInfo.indexOf(':');
                if (idx > 0) {
                    String id = cruise.substring(0, idx);
                    cruiseText += id + " ";
                    String idType = cruise.substring(idx + 1);
                    cruiseIdType.setSelected(idType);
                } else {
                    cruiseText += cruise + " ";
                }
            }
            cruiseId.setText(cruiseText);
        }
        if ( citation.getSection() != null &&
             ! citation.getSection().trim().equals("null")) { // XXX TODO: workaround for oads xml issue
            section.setText(citation.getSection());
        }
        if ( citation.getCitationAuthorList() != null ) {
            citationAuthorList.setText(citation.getCitationAuthorList());
        }
        if ( citation.getMethodsApplied() != null ) {
            methodsApplied.setText(citation.getMethodsApplied());
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
    public void reset() {
        form.reset();
        cruiseIdType.reset();
    }
}