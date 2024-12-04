package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import gov.noaa.pmel.sdig.client.ClientFactory;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.OAPMetadataEditor;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.shared.Stringy;
import gov.noaa.pmel.sdig.shared.bean.Citation;
import gov.noaa.pmel.sdig.shared.bean.DescribedValue;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.*;

/**
 * Created by rhs on 3/3/17.
 */
public class CitationPanel extends BasicPanel<Citation>  {

    private static final String RELATED_ROW_ID_PFX_ = "related_row_";
    private static final String RELATED_URI_ID_PFX_ = "related_uri_";
    private static final String RELATED_DESC_ID_PFX_ = "related_desc_";
    private static final String RELATED_RMV_BTN_ID_PFX_ = "related_rmv_";

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    @UiField
    TextBox title;
    @UiField
    TextArea datasetAbstract;
    @UiField
    CheckBox isNOAAData;
    @UiField
    VerticalButtonGroup licenseBtnGroup;
    @UiField
    RadioButton lic_cc0;
    @UiField
    RadioButton lic_ccBy;
    @UiField
    FormGroup licenseFormGroup;
//    @UiField
//    FormGroup licTextGroup;
    @UiField
    TextArea licenseText;
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
    Container formContainer;
    @UiField
    Row relatedRow0;
    @UiField
    TextBox relatedDatasetUri0;
    @UiField
    TextBox relatedDatasetDesc0;
    @UiField
    Button addRelatedButton;
    @UiField
    TextArea supplementalInformation;

    @UiField
    Button save;
    @UiField
    Button clear;

    String type = Constants.SECTION_CITATION;

    int relatedRow0index;
    DescribedValue relatedDataset0;
    List<Row> relatedRows = new ArrayList<>();
    List<DescribedValue> relatedDatasets = new ArrayList<>();
    Map<String, TextBox> relatedDatasetUris = new HashMap<>();
    Map<String, TextBox> relatedDatasetDescriptions = new HashMap<>();

    private static final String NOAA_SOURCED_DATA_EXPLANATION =
            "Data that was collected by a NOAA Scientist and/or funded by a NOAA grant."; // TODO: FIX
    private static final String NOAA_INTERNAL_TEXT = "Internal NOAA Source Data refers to data that that have been generated" +
                                    " by NOAA-owned sensors or systems or NOAA Federal employees, including:" +
                                    "<ul>" +
                                        "<li>NESDIS satellite data and data products, climatologies, and atlases</ul>" +
                                        "<li>NWS radar data and model output</ul>" +
                                        "<li>NMFS habitat surveys, coral ecosystems analyses</ul>" +
                                        "<li>OAR carbon mooring arrays, atmospheric chemistry monitoring stations</ul>" +
                                        "<li>NOS bathymetry data collected by NOAA ships</ul>" +
                                        "<li>Derived products developed by NOAA systems or NOAA Federal employees using internal" +
                                            " and/or external data, when the agreement with the external data provider confirms that" +
                                            " they do not maintain ownership of or restrict the use of derived products created with" +
                                            " their data (e.g. NWS model output)</ul>" +
                                    "</ul>";

    private static final String CC0_EXPLANATION = "Create Commons Zero 1.0"; // TODO: FIX
    private static final String CC0_URL = "https://creativecommons.org/publicdomain/zero/1.0/";
    private static final String CC0_EXT_TEXT = "This dataset has been dedicated to the public domain" +
                                               " under a Creative Commons CC0 1.0 Universal (CC0 1.0) Public Domain Dedication.";
    private static final String CC0_NOAA_TEXT = "These data were produced by NOAA and are not subject to copyright protection" +
                                                " in the United States. NOAA waives any potential copyright and related rights" +
                                                " in these data worldwide through the Creative Commons Zero 1.0" +
                                                " Universal (CC0-1.0) Public Domain Dedication.";
    private static final String CCBY_EXPLANATION = "Creative Commons International 4.0"; // TODO: FIX
    private static final String CCBY_URL = "https://creativecommons.org/licenses/by/4.0/";
    private static final String CCBY_TEXT = "This dataset is licensed under a Creative Commons Attribution" +
                                            " 4.0 International (CC BY 4.0) License.";
    private RadioButton lastLicense = null;
    private boolean licenseChecked = false;

    interface CitationUiBinder extends UiBinder<HTMLPanel, CitationPanel> {
    }

    private static final CitationUiBinder ourUiBinder = GWT.create(CitationUiBinder.class);

    public CitationPanel() {
        super(Constants.SECTION_CITATION);
        initWidget(ourUiBinder.createAndBindUi(this));
        resetRelated();

        clear.addClickHandler(clearIt);
        lic_cc0.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                GWT.log("cc0.onChange:" + lic_cc0.getValue());
                GWT.log("Setting license text to: " + CC0_EXT_TEXT);
                licenseText.setText(CC0_EXT_TEXT);
            }
        });
        lic_ccBy.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                GWT.log("ccBy.onChange:" + lic_ccBy.getValue());
                GWT.log("Setting license text to: " + CCBY_TEXT);
                licenseText.setText(CCBY_TEXT);
            }
        });
        lic_cc0.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                Boolean isSet = event.getValue();
                GWT.log("cc0 value changed to: " + String.valueOf(isSet));
                if (isSet) {
                    String text = isNOAAData.getValue() ? CC0_NOAA_TEXT : CC0_EXT_TEXT;
                    GWT.log("Setting lic text to : "+ text);
                    licenseText.setText(text);
                }
            }
        });
        lic_ccBy.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                Boolean isSet = event.getValue();
                GWT.log("ccBy value changed to: " + String.valueOf(isSet));
                if (isSet) {
                    GWT.log("Setting lic text to : "+ CCBY_TEXT);
                    licenseText.setText(CCBY_TEXT);
                }
            }
        });
        isNOAAData.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                GWT.log("isNOAA changed to: " + event.getValue());
                if ( ! licenseChecked ) {
                    onCheck(null);
                    licenseChecked = false;
                }
            }
        });
    }

    private void resetRelated() {
        relatedRow0index = formContainer.getWidgetIndex(relatedRow0);
        relatedDataset0 = new DescribedValue();
    }

    @UiHandler("isNOAAData")
    public void onCheck(ClickEvent clickEvent) {
        licenseChecked = true;
        Boolean checked = isNOAAData.getValue();
        GWT.log("isNOAAData:" + checked);
        lic_cc0.setEnabled(!checked);
        lic_ccBy.setEnabled(!checked);
        if ( checked ) {
            lastLicense = lic_cc0.getValue() ? lic_cc0 :
                            lic_ccBy.getValue() ? lic_ccBy : null;
            lic_cc0.setValue(checked, Boolean.TRUE);
            // This is redundant in some cases (when the above line selects the CC0 license),
            // but necessary when the CC0 is already selected.
            licenseText.setText(CC0_NOAA_TEXT);
        } else if (lastLicense != null) {
            lastLicense.setValue(true, true);
            lastLicense = null;
        } else if (lic_cc0.getValue()) {
            licenseText.setText(CC0_EXT_TEXT);
        }
    }
//    @UiHandler({"lic_cc0", "lic_ccBy"})
//    public void onChange(ChangeEvent changeEvent) {
//        CheckBox source = (CheckBox) changeEvent.getSource();
//        GWT.log("onChange:"+ String.valueOf(source));
//    }
    @UiHandler("save")
    public void onSave(ClickEvent clickEvent) {
        boolean isValid = validate();
        NotifySettings settings = NotifySettings.newSettings();
        if (isValid) {
            settings.setType(NotifyType.SUCCESS);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.COMPLETE, settings);
            eventBus.fireEventFromSource(new SectionSave(getCitation(), this.type), CitationPanel.this);
        } else {
            settings.setType(NotifyType.WARNING);
            settings.setPlacement(NotifyPlacement.TOP_CENTER);
            Notify.notify(Constants.NOT_COMPLETE, settings);
        }
    }
    public boolean validate() {
        GWT.log("Citation validate");
        boolean formValid = validateForm();
        boolean hasLicense = ! licenseText.getText().trim().isEmpty();
        if ( hasLicense ) { // assuming lic text is set by one of the licenses being picked...
            if ( ! lic_cc0.getValue() && ! lic_ccBy.getValue() ) {
                GWT.log("Aaaack!  License Text w/o a chosen license!");
                licenseFormGroup.addStyleName("has-error");
            } else {
                licenseFormGroup.removeStyleName("has-error");
            }
        } else {
            licenseFormGroup.addStyleName("has-error");
        }
        return formValid && hasLicense;
    }

    @Override
    public void reset(boolean clearIds) {
        GWT.log("Citation reset: "  + clearIds);
        super.reset(clearIds);
        isNOAAData.setValue(Boolean.FALSE);
        lic_ccBy.setValue(Boolean.FALSE);
        lic_cc0.setValue(Boolean.FALSE);
//        resetRelated();
        removeRelatedDatasets();
    }
    @Override
    public boolean isValid() {
        GWT.log("Citation isValid?");
        return validate();
    }

    void setLicense(Citation citation) {
        String license = citation.getLicenseUrl() != null ? citation.getLicenseUrl() : "";
        String  text = citation.getLicenseText() != null ? citation.getLicenseText() : "";
        boolean isNOAA = citation.getNoaaData() != null ? citation.getNoaaData() : false;
        isNOAAData.setValue(isNOAA, true);
//        if ( ! isNOAA) {
            String url = license.toLowerCase();
            lic_cc0.setValue(CC0_URL.equals(url), true);
            lic_ccBy.setValue(CCBY_URL.equals(url), true);
            licenseText.setText(text);
//        }

    }
    String getLicenseUrl() {
        if ( lic_cc0.getValue()) {
            return CC0_URL;
        } else if ( lic_ccBy.getValue()) {
            return CCBY_URL;
        } else {
            return "";
        }
    }
    public Citation getCitation() {
        Citation citation = getDbItem() != null ? (Citation)getDbItem() : new Citation();
        citation.setDatasetAbstract(datasetAbstract.getText().trim());
        citation.setNoaaData(isNOAAData.getValue());
        citation.setLicenseUrl(getLicenseUrl());
        citation.setLicenseText(licenseText.getText().trim());
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
        citation.setRelatedDatasets(getRelatedDatasets());
        return citation;
    }
    public boolean isDirty(Citation original) {
        OAPMetadataEditor.debugLog("CitationPanel.isDirty("+original+")");
        boolean isDirty =
            original == null ?
            this.hasContent() :
            isDirty(datasetAbstract, original.getDatasetAbstract() ) ||
            isDirty(getLicenseUrl(), original.getLicenseUrl()) ||
            isDirty(licenseText, original.getLicenseText()) ||
            isDirty(useLimitation, original.getUseLimitation() ) ||
            isDirty(purpose, original.getPurpose() ) ||
            isDirty(researchProjects, original.getResearchProjects() ) ||
            isDirty(title, original.getTitle() ) ||
            isDirty(expocode, original.getExpocode() ) ||
            isDirty(cruiseId, original.getCruiseId() ) ||
            isDirty(section, original.getSection() ) ||
            isDirty(citationAuthorList, original.getCitationAuthorList() ) ||
            isDirty(references, original.getScientificReferences() ) ||
            relatedDatasetsChanged(original.getRelatedDatasets()) ||
            isDirty(supplementalInformation, original.getSupplementalInformation() );
        OAPMetadataEditor.debugLog("CitationPanel.isDirty:"+isDirty);
        return isDirty;
    }

    private boolean relatedDatasetsChanged(List<DescribedValue> relatedDatasets) {
        List<DescribedValue> related = getRelatedDatasets();
        if ( related.size() != relatedDatasets.size() ) {
            return true;
        }
        Iterator<DescribedValue> relatedIter = relatedDatasets.iterator();
        for ( DescribedValue relatedDataset : related ) {
             if ( !relatedIter.hasNext() ) { return true; }
             if ( ! relatedDataset.equals(relatedIter.next()) ) { return true; }
        }
        return false;
    }

    public boolean hasContent() {
        if (datasetAbstract.getText() != null && !datasetAbstract.getText().trim().isEmpty() ) {
            return true;
        }
        if (licenseText.getText() != null && !licenseText.getText().trim().isEmpty() ) {
            return true;
        }
        if (useLimitation.getText() != null && !useLimitation.getText().trim().isEmpty() ) {
            return true;
        }
        if (purpose.getText() != null && !purpose.getText().trim().isEmpty() ) {
            return true;
        }
        if (researchProjects.getText() != null && !researchProjects.getText().trim().isEmpty() ) {
            return true;
        }
        if (title.getText() != null && !title.getText().trim().isEmpty() ) {
            return true;
        }
        if (expocode.getText() != null && !expocode.getText().trim().isEmpty() ) {
            return true;
        }
        if (cruiseId.getText() != null && !cruiseId.getText().trim().isEmpty() ) {
            return true;
        }
        if (section.getText() != null && !section.getText().trim().isEmpty() ) {
            return true;
        }
        if (citationAuthorList.getText() != null && !citationAuthorList.getText().trim().isEmpty() ) {
            return true;
        }
        if (references.getText() != null && !references.getText().trim().isEmpty() ) {
            return true;
        }
        if (supplementalInformation.getText() != null && !supplementalInformation.getText().trim().isEmpty() ) {
            return true;
        }
        if ( ! getRelatedDatasets().isEmpty()) {
            return true;
        }
        return false;
    }
    public void show(Citation citation) {
        GWT.log("show: " + citation);
        reset();
        setDbItem(citation);
        if ( citation.getTitle() != null ) {
            title.setText(citation.getTitle());
        }
        if ( citation.getDatasetAbstract() != null ) {
            datasetAbstract.setText(citation.getDatasetAbstract() );
        }
//        if ( citation.getLicenseUrl() != null ) {
            setLicense(citation);
//        }
//        if ( citation.getLicenseText() != null ) {
//            licenseText.setText(citation.getLicenseText());
//        }
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
        showRelatedDatasets(citation);
    }
    @Override
    public void reset() {
        GWT.log("reset()");
        reset(true);
    }

    private void removeRelatedDatasets() {
        GWT.log("removeRelatedDatasets()");
        relatedDatasetUri0.clear();
        relatedDatasetDesc0.clear();
        for (Row row : relatedRows) {
            GWT.log("removing " + row.getId());
            removeRelatedRow(row);
        }
        relatedRows.clear();
    }

    private void showRelatedDatasets(Citation citation) {
        if (citation.getRelatedDatasets() != null) {
            List<DescribedValue> relatedDatasets = citation.getRelatedDatasets();
            if (relatedDatasets.isEmpty()) { return; }
            DescribedValue relatedDataset0 = relatedDatasets.get(0);
            relatedDatasetUri0.setText(relatedDataset0.getValue());
            relatedDatasetDesc0.setText(relatedDataset0.getDescription());
            for (int i = 1; i < relatedDatasets.size(); i++) {
                DescribedValue dv = relatedDatasets.get(i);
                addRelatedDataset(dv);
            }
        }
    }

    private List<DescribedValue> getRelatedDatasets() {
        List<DescribedValue> related = new ArrayList<>();
        if (!Stringy.isEmpty(relatedDatasetUri0)) {
            related.add(new DescribedValue(relatedDatasetUri0.getText(),
                                           relatedDatasetDesc0.getText()));
        }
        for (String rowid : relatedDatasetUris.keySet()) {
            TextBox uriField = relatedDatasetUris.get(rowid);
            TextBox descField = relatedDatasetDescriptions.get(rowid);
            if ( ! Stringy.isEmpty(uriField)) {
                String uri = uriField.getText();
                String desc = Stringy.isEmpty(descField) ? "" : descField.getText();
                DescribedValue dataset = new DescribedValue(uri, desc);
                related.add(dataset);
            }
        }
        return related;
    }

    @UiHandler("addRelatedButton")
    void onAdd(ClickEvent clickEvent) {
        GWT.log("Add related clicked:"+clickEvent);
        addRelatedRow();
    }

    private void addRelatedDataset(DescribedValue dataset) {
        GWT.log("Add related dataset:"+dataset);
        Row addedRow = addRelatedRow();
        String rowId = addedRow.getId();
        relatedDatasetUris.get(rowId).setText(dataset.getValue());
        relatedDatasetDescriptions.get(rowId).setText(dataset.getDescription());
    }

    private Row addRelatedRow() {
        int addedRelatedIdx = relatedRows.size() + 1; // so it's 1-based
        int addedRowId = relatedRow0index + addedRelatedIdx;
        GWT.log("Adding related row " + addedRowId + " with " + relatedRows.size() + " already");
        String row_id = RELATED_ROW_ID_PFX_ + addedRowId;
        Row newRow = new Row();
        newRow.setId(row_id);

        TextBox uriTextBox = addRowTextField(newRow, ColumnSize.SM_3, RELATED_URI_ID_PFX_+ addedRowId, "URI or accession");
        relatedDatasetUris.put(row_id, uriTextBox);

        TextBox descTextBox = addRowTextField(newRow, ColumnSize.SM_7, RELATED_DESC_ID_PFX_+ addedRowId, "Dataset description");
        relatedDatasetDescriptions.put(row_id, descTextBox);

        Column buttonColumn = getRemoveButtonColumn(RELATED_RMV_BTN_ID_PFX_+addedRowId, event -> removeRelated(event.getSource()));
        newRow.add(buttonColumn);

        formContainer.insert(newRow, addedRowId);

        relatedRows.add(newRow);

        return newRow;
    }

    private void removeRelated(Object source) {
        Button removeButton = (Button) source;
        Row rowToRemove = getRowFor(removeButton);
        removeRelatedRow(rowToRemove);
        relatedRows.remove(rowToRemove);
    }

    private void removeRelatedRow(Row rowToRemove) {
        GWT.log("removing: " + rowToRemove);
        boolean removed = formContainer.remove(rowToRemove);
        GWT.log("removed: " + removed);
        rowToRemove.removeFromParent();
        removeRelatedFields(rowToRemove);
    }

    private void removeRelatedFields(Row rowToRemove) {
        String rowId = rowToRemove.getId();
        removeWidget(rowId, relatedDatasetUris);
        removeWidget(rowId, relatedDatasetDescriptions);
    }
}