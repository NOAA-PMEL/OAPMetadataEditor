package gov.noaa.pmel.sdig.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import gov.noaa.pmel.sdig.client.event.NavLink;
import gov.noaa.pmel.sdig.client.event.NavLinkHandler;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.event.SectionSaveHandler;
import gov.noaa.pmel.sdig.client.panels.CitationPanel;
import gov.noaa.pmel.sdig.client.panels.DataSubmitterPanel;
import gov.noaa.pmel.sdig.client.panels.DicPanel;
import gov.noaa.pmel.sdig.client.panels.FundingPanel;
import gov.noaa.pmel.sdig.client.panels.GenericVariablePanel;
import gov.noaa.pmel.sdig.client.panels.InvestigatorPanel;
import gov.noaa.pmel.sdig.client.panels.Pco2aPanel;
import gov.noaa.pmel.sdig.client.panels.Pco2dPanel;
import gov.noaa.pmel.sdig.client.panels.PhPanel;
import gov.noaa.pmel.sdig.client.panels.PlatformPanel;
import gov.noaa.pmel.sdig.client.panels.TaPanel;
import gov.noaa.pmel.sdig.client.panels.TimeAndLocationPanel;
import gov.noaa.pmel.sdig.shared.bean.*;
import org.fusesource.restygwt.client.JsonEncoderDecoder;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.RestServiceProxy;
import org.fusesource.restygwt.client.TextCallback;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.base.form.AbstractForm;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import javax.print.Doc;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.*;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class OAPMetadataEditor implements EntryPoint {

    boolean saved = false;

    public interface SaveDocumentService extends RestService {
        @POST
        @Path("{id}")
        public void save(@PathParam("id") String datasetId, Document document, TextCallback textCallback);
    }
    public interface DocumentCodec extends JsonEncoderDecoder<Document> {}

    public interface GetDocumentService extends RestService {
        @Path("{id}")
        public void get(@PathParam("id") String id, TextCallback textCallback);
    }

    private static boolean DEBUG = true;
    public static void debugLog(String msg) {
        if ( DEBUG ) {
            logToConsole(msg);
        }
    }
    public static native void logToConsole(String msg) /*-{
        console.log(msg);
    }-*/;

    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network " + "connection and try again.";

    Resource saveDocumentResource = new Resource(Constants.saveDocument);
    SaveDocumentService saveDocumentService = GWT.create(SaveDocumentService.class);

    Resource getDocumentResource = new Resource(Constants.getDocument);
    GetDocumentService getDocumentService = GWT.create(GetDocumentService.class);

    DocumentCodec codec = GWT.create(DocumentCodec.class);

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    Document _loadedDocument = null;
    Document _currentDocument = null;
    String _requestedDocumentId = null;
    String _datasetId = null;
    Long _documentDbId = null;
    Long _documentDbVersion = null;

//    Person dataSubmitter = null;
    DataSubmitterPanel submitterPanel = new DataSubmitterPanel();
    InvestigatorPanel investigatorPanel = new InvestigatorPanel();

    CitationPanel citationPanel = new CitationPanel();
//    Citation citation = null;

    TimeAndLocationPanel timeAndLocationPanel = new TimeAndLocationPanel();
//    TimeAndLocation timeAndLocation = null;

    FundingPanel fundingPanel = new FundingPanel();
//    Funding funding = null;

    PlatformPanel platformPanel = new PlatformPanel();

    DicPanel dicPanel = new DicPanel();
//    Variable dic = null;

    TaPanel taPanel = new TaPanel();
//    Variable ta = null;

    PhPanel phPanel = new PhPanel();
//    Variable ph = null;

    Pco2aPanel pco2aPanel = new Pco2aPanel();
//    Variable pco2a = null;

    Pco2dPanel pco2dPanel = new Pco2dPanel();
//    Variable pco2d = null;

    GenericVariablePanel genericVariablePanel = new GenericVariablePanel();

    // Save XML dialog, should be its own widget.
    Modal modal = new Modal();
    ModalHeader modalHeader = new ModalHeader();
    ModalBody modalBody = new ModalBody();
    Button save = new Button("Save");
    String documentLocation = null;
    String currentIndex = null; // XXX shouldn't use this. Leaving it for now ...

    final DashboardLayout topLayout = new DashboardLayout();

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        RootPanel.get("load").getElement().setInnerHTML("");
        RootPanel.get().add(topLayout);
        topLayout.addUploadSuccess(completeHandler);
        topLayout.addUploadSubmitHandler(submitHandler);
        topLayout.setMain(submitterPanel);
        topLayout.setActive(Constants.SECTION_SUBMITTER);
        modalBody.add(save);
        modal.add(modalHeader);
        modal.add(modalBody);
        eventBus.addHandler(ClickEvent.getType(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Object source = event.getSource();
                if ( source instanceof Button ) {
                    Button b = (Button) source;
                    if ( b.getText().equals("Clear All") ) {

                        if ( currentDocumentIsDirty() && !saved ) {

                            final Modal sure = new Modal();
                            ModalHeader header = new ModalHeader();
                            Heading h = new Heading(HeadingSize.H3);
                            h.setText("WARNING!");
                            header.add(h);
                            sure.add(header);
                            ModalBody body = new ModalBody();
                            HTML message = new HTML("You appear to have made changes but have not saved them to your local disk." +
                                    "<br><strong>Click OK to reset the form and LOSE ALL YOUR DATA.</strong>" +
                                    "<br>Click Canel to go back to what you were doing.");
                            ModalFooter footer = new ModalFooter();
                            Button ok = new Button("OK");
                            ok.setType(ButtonType.DANGER);
                            Button cancel = new Button("Cancel");
                            cancel.setType(ButtonType.PRIMARY);

                            footer.add(ok);
                            footer.add(cancel);
                            body.add(message);
                            sure.add(body);
                            sure.add(footer);

                            sure.show();

                            ok.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    startOver(false);
                                    sure.hide();
                                }
                            });

                            cancel.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    sure.hide();
                                }
                            });

                        } else {
                            startOver(false);
                        }
                    }
                }
            }
        });
        eventBus.addHandler(SectionSave.TYPE, new SectionSaveHandler() {
            @Override
            public void onSectionSave(SectionSave event) {
//                if ( event.getType().equals(Constants.SECTION_DOCUMENT )) { // &&  event.getSectionContents().equals("saveNotify") {
//                    saveMultiItemPanels();
//                }
                if ( "Download".equals(event.getSectionContents()) ||
                     "Preview".equals(event.getSectionContents()) || // TODO:  ... but not empty document.
                      currentDocumentIsDirty()) {
                    saveSection(event.getType(), event.getSectionContents());
                }
            }
        });
        eventBus.addHandler(NavLink.TYPE, new NavLinkHandler() {
            @Override
            public void onNavLink(NavLink event) {
                AnchorListItem link = (AnchorListItem) event.getSource();
                if ( link.getText().equals(Constants.SECTION_INVESTIGATOR) ) {
                    topLayout.setMain(investigatorPanel);
                    topLayout.setActive(Constants.SECTION_INVESTIGATOR);
                    if ( investigatorPanel.getInvestigators().size() > 0 ) {
                        investigatorPanel.setTableVisible(true);
                    }
                } else if ( link.getText().equals(Constants.SECTION_SUBMITTER) ) {
//                    if ( dataSubmitter != null ) {
//                        submitterPanel.show(dataSubmitter);
//                    }
                    topLayout.setMain(submitterPanel);
                    topLayout.setActive(Constants.SECTION_SUBMITTER);
                } else if ( link.getText().equals(Constants.SECTION_CITATION) ) {
//                    if ( citation != null ) {
//                        citationPanel.show(citation);
//                    }
                    topLayout.setMain(citationPanel);
                    topLayout.setActive(Constants.SECTION_CITATION);
                }  else if ( link.getText().equals(Constants.SECTION_TIMEANDLOCATION) ) {
//                    if ( timeAndLocation != null ) {
//                        timeAndLocationPanel.show(timeAndLocation);
//                    }
                    topLayout.setMain(timeAndLocationPanel);
                    topLayout.setActive(Constants.SECTION_TIMEANDLOCATION);
                } else if ( link.getText().equals(Constants.SECTION_FUNDING) ) {
                    topLayout.setMain(fundingPanel);
                    topLayout.setActive(Constants.SECTION_FUNDING);
                    if ( fundingPanel.getFundings().size() > 0 ) {
                        fundingPanel.setTableVisible(true);
                    }
                } else if ( link.getText().equals(Constants.SECTION_PLATFORMS) ) {
                    topLayout.setMain(platformPanel);
                    topLayout.setActive(Constants.SECTION_PLATFORMS);
                    if ( platformPanel.getPlatforms().size() > 0 ) {
                        platformPanel.setTableVisible(true);
                    }
                } else if ( link.getText().equals(Constants.SECTION_DIC) ) {
                    topLayout.setMain(dicPanel);
                    topLayout.setActive(Constants.SECTION_DIC);
//                    if (dic != null) {
//                        dicPanel.show(dic);
//                    }
                } else if ( link.getText().equals(Constants.SECTION_TA) ) {
                    topLayout.setMain(taPanel);
                    topLayout.setActive(Constants.SECTION_TA);
//                    if (ta != null) {
//                        taPanel.show(ta);
//                    }
                } else if ( link.getText().equals(Constants.SECTION_PH) ) {
                    topLayout.setMain(phPanel);
                    topLayout.setActive(Constants.SECTION_PH);
//                    if (ph != null) {
//                        phPanel.show(ph);
//                    }
                } else if ( link.getText().equals(Constants.SECTION_PCO2A) ) {
                    topLayout.setMain(pco2aPanel);
                    topLayout.setActive(Constants.SECTION_PCO2A);
//                    if ( pco2a != null ) {
//                        pco2aPanel.show(pco2a);
//                    }
                } else if ( link.getText().equals(Constants.SECTION_PCO2D) ) {
                    topLayout.setMain(pco2dPanel);
                    topLayout.setActive(Constants.SECTION_PCO2D);
//                    if ( pco2d != null) {
//                        pco2dPanel.show(pco2d);
//                    }
                } else if ( link.getText().equals(Constants.SECTION_GENERIC) ) {
                    topLayout.setMain(genericVariablePanel);
                    topLayout.setActive(Constants.SECTION_GENERIC);
                    if (genericVariablePanel.getVariables().size() > 0 ) {
                        genericVariablePanel.setTableVisible(true);
                    }
                }
            }
        });
        ((RestServiceProxy)saveDocumentService).setResource(saveDocumentResource);
        ((RestServiceProxy)getDocumentService).setResource(getDocumentResource);
//        Window.addWindowClosingHandler(new Window.ClosingHandler() {
//            @Override
//            public void onWindowClosing(Window.ClosingEvent event) {
//                System.out.println("is about to close dirty: " + isDirty());
//                if ( isDirty() && !saved ) {
//                    event.setMessage("It appears you have made changes that you have not saved. Are you sure?");
//                }
//            }
//        });
//
//        Window.addCloseHandler(new CloseHandler<Window>() {
//            @Override
//            public void onClose(CloseEvent<Window> event) {
//                System.out.println("closing dirty: " + isDirty());
//            }
//        });

        setupMessageListener(this);

        String docId = Window.Location.getParameter("id");
        if ( docId != null ) {
            debugLog("OAME: Loading document " + docId);
            loadDocumentId(docId);
        }
    }

//    private void saveMultiItemPanels() {
//        String activePanel = topLayout.getActive();
//        if ( activePanel.equals(Constants.SECTION_INVESTIGATOR) ) {
//            if ( investigatorPanel.isDirty()) {
//                investigatorPanel.savePerson();
//            }
//        } else if ( activePanel.equals(Constants.SECTION_FUNDING) ) {
//            if ( fundingPanel.isDirty()) {
//                fundingPanel.saveFunding();
//            }
//        } else if ( activePanel.equals(Constants.SECTION_PLATFORMS) ) {
//            if ( platformPanel.isDirty()) {
//                platformPanel.savePlatform();
//            }
//        } else if ( activePanel.equals(Constants.SECTION_GENERIC) ) {
//            if ( genericVariablePanel.isDirty()) {
//                genericVariablePanel.saveVariable();
//            }
//
//        }
//
//    }

    private void saveSection(String type, Object sectionContents) {
        logToConsole("saveSection:"+type+", " + sectionContents);
        saved = false;
        if (type.equals(Constants.SECTION_INVESTIGATOR)) {
            // List is in the celltable data provider in the layout. Nothing to do here.
        } else if ( type.equals(Constants.SECTION_SUBMITTER) ) {
//            dataSubmitter = (Person) sectionContents;
            topLayout.setMain(investigatorPanel);
            topLayout.setActive(Constants.SECTION_INVESTIGATOR);
        } else if ( type.equals(Constants.SECTION_CITATION) ) {
//            citation = (Citation) sectionContents;
//            if ( timeAndLocation != null ) {
//                timeAndLocationPanel.show(timeAndLocation);
//            }
            topLayout.setMain(timeAndLocationPanel);
            topLayout.setActive(Constants.SECTION_TIMEANDLOCATION);
        } else if ( type.equals(Constants.SECTION_TIMEANDLOCATION) ) {
//            TimeAndLocation timeAndLocation = (TimeAndLocation) sectionContents;
//            if ( funding != null ) {
//                fundingPanel.show(funding);
//            }
            topLayout.setMain(fundingPanel);
            topLayout.setActive(Constants.SECTION_FUNDING);
        } else if ( type.equals(Constants.SECTION_FUNDING) ) {
            // Nothing to do here, list in the panel
        } else if ( type.equals(Constants.SECTION_PLATFORMS) ) {
            // List kept in the panel. Nothing to do here.
        } else if ( type.equals(Constants.SECTION_DIC) ) {
//            if ( dic == null ) {
//                dic = (Variable) sectionContents;
//            } else {
//                dicPanel.fill(dic);
//            }
            topLayout.setMain(taPanel);
            topLayout.setActive(Constants.SECTION_TA);
        } else if ( type.equals(Constants.SECTION_TA) ) {
//            if ( ta == null ) {
//                ta = (Variable) sectionContents;
//            } else {
//                taPanel.fill(ta);
//            }
            topLayout.setMain(phPanel);
            topLayout.setActive(Constants.SECTION_PH);
        } else if ( type.equals(Constants.SECTION_PH) ) {
//            if ( ph == null ) {
//                ph = (Variable) sectionContents;
//            } else {
//                phPanel.fill(ph);
//            }
            topLayout.setMain(pco2aPanel);
            topLayout.setActive(Constants.SECTION_PCO2A);
        } else if ( type.equals(Constants.SECTION_PCO2A) ) {
//            if ( pco2a == null ) {
//                pco2a = (Variable) sectionContents;
//            } else {
//                pco2aPanel.fill(pco2a);
//            }
            topLayout.setMain(pco2dPanel);
            topLayout.setActive(Constants.SECTION_PCO2D);
        } else if ( type.equals(Constants.SECTION_PCO2D) ) {
//            if ( pco2d == null ) {
//                pco2d = (Variable) sectionContents;
//            } else {
//                pco2dPanel.fill(pco2d);
//            }
            topLayout.setMain(genericVariablePanel);
            topLayout.setActive(Constants.SECTION_GENERIC);
        } else if ( type.equals(Constants.SECTION_GENERIC) ) {
            // List managed in the panel
        } else if ( type.equals(Constants.SECTION_DOCUMENT) )  {
            String content = (String)sectionContents;
            TextCallback callback = null;
            switch (content) {
                case "Download" :
                    callback = documentSaved;
                    break;
                case "Notify" :
                    callback = saveNotify;
                    break;
                case "Preview" :
                    callback = previewDocument;
                    break;
            }
            saveDocToServer(callback);
        }
    }

    private static void warn(String msg) {
        NotifySettings settings = NotifySettings.newSettings();
        settings.setType(NotifyType.WARNING);
        settings.setPlacement(NotifyPlacement.TOP_CENTER);
        Notify.notify(msg, settings);
    }
    private static void info(String msg) {
        NotifySettings settings = NotifySettings.newSettings();
        settings.setType(NotifyType.INFO);
        settings.setPlacement(NotifyPlacement.TOP_CENTER);
        Notify.notify(msg, settings);
    }

    private void saveDocToServer(TextCallback callback) {


        // isComplete vs isvalid foreach
//        investigatorPanel.isValidTest();


//        if (document.getInvestigators() != null) {
//            List<Person> personList = document.getInvestigators();
//            for (int i = 0; i < personList.size(); i++) {
//                Person p = personList.get(i);
//                investigatorPanel.show(p);
//                if (investigatorPanel.valid()) {
//                    topLayout.setChecked(Constants.SECTION_INVESTIGATOR);
//                    topLayout.removehighlight(Constants.SECTION_INVESTIGATOR, "pill-warning");
//                }
//                investigatorPanel.reset();
//            }
//            investigatorPanel.addPeople(personList);
//
//        }

        if ( !topLayout.isComplete() ) {
            warn(Constants.DOCUMENT_NOT_COMPLETE);

            // highlight panel issues
//            topLayout.sethighlight(Constants.SECTION_INVESTIGATOR, "pill-warning");
//            topLayout.sethighlight(Constants.SECTION_SUBMITTER, "pill-warning");
//            topLayout.sethighlight(Constants.SECTION_CITATION, "pill-warning");
//            topLayout.sethighlight(Constants.SECTION_TIMEANDLOCATION, "pill-warning");
//            topLayout.sethighlight(Constants.SECTION_FUNDING, "pill-warning");
//            topLayout.sethighlight(Constants.SECTION_PLATFORMS, "pill-warning");
//            topLayout.sethighlight(Constants.SECTION_DIC, "pill-warning");
//            topLayout.sethighlight(Constants.SECTION_TA, "pill-warning");
//            topLayout.sethighlight(Constants.SECTION_PH, "pill-warning");
//            topLayout.sethighlight(Constants.SECTION_PCO2A, "pill-warning");
//            topLayout.sethighlight(Constants.SECTION_PCO2D, "pill-warning");
//            topLayout.sethighlight(Constants.SECTION_GENERIC, "pill-warning");
        }
        Document doc = getDocument();
        checkStuff(doc);
        saveDocumentService.save(getDatasetId(doc), doc, callback);
        saved = true;
    }

    public void checkStuff(Document doc) {
        debugLog("checkStuff: " + doc);

        if (doc.getDataSubmitter() != null) {
            Person dataSubmitter = doc.getDataSubmitter();
            submitterPanel.show(dataSubmitter);
            if (! submitterPanel.valid()) {
                debugLog("danger submitterPanel has no valid data");
                topLayout.sethighlight(Constants.SECTION_SUBMITTER, "pill-danger");
            }
            else {
                debugLog("success submitterPanel has only valid data");
                topLayout.setChecked(Constants.SECTION_SUBMITTER);
                topLayout.removehighlight(Constants.SECTION_SUBMITTER, "pill-warning");
                topLayout.removehighlight(Constants.SECTION_SUBMITTER, "pill-danger");
            }
        }
        if (doc.getDataSubmitter() == null) {
            debugLog("submitterPanel is null");
            topLayout.sethighlight(Constants.SECTION_SUBMITTER, "pill-danger");
        }

        if (doc.getInvestigators() != null) {
            boolean hasValidData = false;
            boolean hasInvalidData = false;
            List<Person> personList = doc.getInvestigators();
            for (int i = 0; i < personList.size(); i++) {
                Person p = personList.get(i);
                investigatorPanel.show(p);
                if (! investigatorPanel.valid()) {
                    hasInvalidData = true;
                }
                else {
                    hasValidData = true;
                }
                investigatorPanel.reset();
            }
            if (hasValidData == true && hasInvalidData == true) {
                debugLog("warning investigatorPanel valid and invalid");
                topLayout.setChecked(Constants.SECTION_INVESTIGATOR);
                topLayout.sethighlight(Constants.SECTION_INVESTIGATOR, "pill-warning");
            }
            if (hasValidData == true && hasInvalidData == false) {
                debugLog("success investigatorPanel has only valid data");
                topLayout.setChecked(Constants.SECTION_INVESTIGATOR);
                topLayout.removehighlight(Constants.SECTION_INVESTIGATOR, "pill-warning");
                topLayout.removehighlight(Constants.SECTION_INVESTIGATOR, "pill-danger");
            }
            if (hasValidData == false && hasInvalidData == true){
                debugLog("danger investigatorPanel has no valid data");
                topLayout.sethighlight(Constants.SECTION_INVESTIGATOR, "pill-danger");
            }
        }
        if (doc.getInvestigators() == null) {
            debugLog("investigatorPanel is null");
            topLayout.sethighlight(Constants.SECTION_SUBMITTER, "pill-danger");
        }

        if (doc.getCitation() != null) {
            Citation citation = doc.getCitation();
            citationPanel.show(citation);
            if (! citationPanel.valid()) {
                debugLog("danger citationPanel has no valid data");
                topLayout.sethighlight(Constants.SECTION_CITATION, "pill-danger");
            }
            else {
                debugLog("success citationPanel has only valid data");
                topLayout.setChecked(Constants.SECTION_CITATION);
                topLayout.removehighlight(Constants.SECTION_CITATION, "pill-warning");
                topLayout.removehighlight(Constants.SECTION_CITATION, "pill-danger");
            }
        }
        if (doc.getCitation() == null) {
            debugLog("citationPanel is null");
            topLayout.sethighlight(Constants.SECTION_CITATION, "pill-danger");
        }

        if (doc.getTimeAndLocation() != null) {
            TimeAndLocation timeAndLocation = doc.getTimeAndLocation();
            timeAndLocationPanel.show(timeAndLocation);
            if (! timeAndLocationPanel.valid()) {
                debugLog("danger timeAndLocationPanel has no valid data");
                topLayout.sethighlight(Constants.SECTION_PCO2A, "pill-danger");
            }
            else {
                debugLog("success timeAndLocationPanel has only valid data");
                topLayout.setChecked(Constants.SECTION_TIMEANDLOCATION);
                topLayout.removehighlight(Constants.SECTION_TIMEANDLOCATION, "pill-warning");
                topLayout.removehighlight(Constants.SECTION_TIMEANDLOCATION, "pill-danger");
            }
        }
        if (doc.getTimeAndLocation() == null) {
            debugLog("timeAndLocationPanel is null");
            topLayout.sethighlight(Constants.SECTION_TIMEANDLOCATION, "pill-danger");
        }

        if (doc.getFunding() != null) {
            boolean hasValidData = false;
            boolean hasInvalidData = false;
            List<Funding> fundings = doc.getFunding();
            for( int i = 0; i < fundings.size(); i++ ) {
                Funding f = fundings.get(i);
                fundingPanel.show(f);
                if (! fundingPanel.valid() ) {
                    hasInvalidData = true;
                }
                else {
                    hasValidData = true;
                }
                fundingPanel.reset();
            }
            if (hasValidData == true && hasInvalidData == true) {
                debugLog("warning fundingPanel valid and invalid");
                topLayout.setChecked(Constants.SECTION_FUNDING);
                topLayout.sethighlight(Constants.SECTION_FUNDING, "pill-warning");
            }
            if (hasValidData == true && hasInvalidData == false) {
                debugLog("success fundingPanel has only valid data");
                topLayout.setChecked(Constants.SECTION_FUNDING);
                topLayout.removehighlight(Constants.SECTION_FUNDING, "pill-warning");
                topLayout.removehighlight(Constants.SECTION_FUNDING, "pill-danger");
            }
            if (hasValidData == false && hasInvalidData == true){
                debugLog("danger fundingPanel has no valid data");
                topLayout.sethighlight(Constants.SECTION_FUNDING, "pill-danger");
            }
        }
        debugLog("funding not null block done");
        if (doc.getFunding() == null) {
            debugLog("funding is null");
            topLayout.sethighlight(Constants.SECTION_FUNDING, "pill-danger");
        }

        if (doc.getPlatforms() != null) {
            boolean hasValidData = false;
            boolean hasInvalidData = false;
            List<Platform> platformsList = doc.getPlatforms();
            for (int i = 0; i < platformsList.size(); i++) {
                Platform p = platformsList.get(i);
                platformPanel.show(p);
                if (! platformPanel.valid()) {
                    hasInvalidData = true;
                }
                else {
                    hasValidData = true;
                }
                platformPanel.reset();
            }
            if (hasValidData == true && hasInvalidData == true) {
                debugLog("warning platformPanel valid and invalid");
                topLayout.setChecked(Constants.SECTION_PLATFORMS);
                topLayout.sethighlight(Constants.SECTION_PLATFORMS, "pill-warning");
            }
            if (hasValidData == true && hasInvalidData == false) {
                debugLog("success platformPanel has only valid data");
                topLayout.setChecked(Constants.SECTION_PLATFORMS);
                topLayout.removehighlight(Constants.SECTION_PLATFORMS, "pill-warning");
                topLayout.removehighlight(Constants.SECTION_PLATFORMS, "pill-danger");
            }
            if (hasValidData == false && hasInvalidData == true){
                debugLog("danger platformPanel has no valid data");
                topLayout.sethighlight(Constants.SECTION_PLATFORMS, "pill-danger");
            }
        }

        if (doc.getPco2a() != null) {
            Variable pco2a = doc.getPco2a();
            pco2aPanel.show(pco2a);
            if (! pco2aPanel.valid()) {
                debugLog("danger pco2aPanel has no valid data");
                topLayout.sethighlight(Constants.SECTION_PCO2A, "pill-danger");
            }
            else {
                debugLog("success pco2aPanel has only valid data");
                topLayout.setChecked(Constants.SECTION_PCO2A);
                topLayout.removehighlight(Constants.SECTION_PCO2A, "pill-warning");
                topLayout.removehighlight(Constants.SECTION_PCO2A, "pill-danger");
            }
        }
        if (doc.getPco2a() == null) {
            debugLog("pco2a is null");
            topLayout.sethighlight(Constants.SECTION_PCO2A, "pill-danger");
        }

        if (doc.getPco2d() != null) {
            Variable pco2d = doc.getPco2d();
            pco2dPanel.show(pco2d);
            if (! pco2dPanel.valid()) {
                debugLog("danger pco2dPanel has no valid data");
                topLayout.sethighlight(Constants.SECTION_PCO2D, "pill-danger");
            }
            else {
                debugLog("success pco2dPanel has only valid data");
                topLayout.setChecked(Constants.SECTION_PCO2D);
                topLayout.removehighlight(Constants.SECTION_PCO2D, "pill-warning");
                topLayout.removehighlight(Constants.SECTION_PCO2D, "pill-danger");
            }
        }
        if (doc.getPco2d() == null) {
            debugLog("pco2d is null");
            topLayout.sethighlight(Constants.SECTION_PCO2D, "pill-danger");
        }
    }

    public String getDatasetId(Document doc) {
        if ( _datasetId != null ) {
            return _datasetId;
        }
        if ( doc.getDatasetIdentifier() != null ) {
            return doc.getDatasetIdentifier();
        }
        if ( doc.getCitation().getExpocode() != null && doc.getCitation().getExpocode().length() > 0 ) {
            return doc.getCitation().getExpocode();
        }
        return null;
    }
    public Document getDocument() {
        Document doc = new Document();
        doc.setDatasetIdentifier(_datasetId);
//        doc.setId(_documentDbId);
//        doc.setVersion(_documentDbVersion);
        doc.setDbId(_documentDbId);
        doc.setDbVersion(_documentDbVersion);
        // Data Submitter Panel
        Person dataSubmitter = submitterPanel.getPerson();
        doc.setDataSubmitter(dataSubmitter);
        // Investigators Panel
        if ( investigatorPanel.hasContent() ) {
            Person p = investigatorPanel.getPerson();
            if ( !p.isComplete()) {
                warn("Current Investigator is not complete.");
            }
            investigatorPanel.addPerson(p);
            investigatorPanel.reset();
            investigatorPanel.setEditing(false);
        }
        List<Person> investigators = investigatorPanel.getInvestigators();
        doc.setInvestigators(investigators);
        // Citation Panel
        Citation citation = citationPanel.getCitation();
        doc.setCitation(citation);
        // TimeAndLocationPanel
        TimeAndLocation timeAndLocation = timeAndLocationPanel.getTimeAndLocation();
        doc.setTimeAndLocation(timeAndLocation);
        // Funding Panel
        List<Funding> fundings = fundingPanel.getFundings();
        if ( fundingPanel.isDirty() ) {
            Funding f = fundingPanel.getFunding();
            fundings.add(f);
        }
        doc.setFunding(fundings);
        // Platforms Panel
//        List<Platform> platforms = platformPanel.getPlatforms();
//        if ( platformPanel.isDirty() ) {
//            Platform p = platformPanel.getPlatform();
//            platforms.add(p);
//        }
//        doc.setPlatforms(platforms);
        //
        if ( platformPanel.hasContent() ) {
            Platform p = platformPanel.getPlatform();
            platformPanel.addPlatform(p);
            platformPanel.reset();
            platformPanel.setEditing(false);
//            genericVariables.add(v);
        }
        List<Platform> platforms = platformPanel.getPlatforms();
        doc.setPlatforms(platforms);



        // Dic Panel
        if ( dicPanel.isDirty()) {
            Variable dic = dicPanel.getDic();
            doc.setDic(dic);
        }
        // Ta
        if ( taPanel.isDirty()) {
            Variable ta = taPanel.getTa();
            doc.setTa(ta);
        }
        // Ph
        if ( phPanel.isDirty()) {
            Variable ph = phPanel.getPh();
            doc.setPh(ph);
        }
        // pco2z
        if ( pco2aPanel.isDirty()) {
            Variable pco2a = pco2aPanel.getPco2a();
            doc.setPco2a(pco2a);
        }
        // pco2d
        if ( pco2dPanel.isDirty()) {
            Variable pco2d = pco2dPanel.getPco2d();
            doc.setPco2d(pco2d);
        }
        // Generic Variables
        if ( genericVariablePanel.hasContent() ) {
            Variable v = genericVariablePanel.getGenericVariable();
            genericVariablePanel.addVariable(v);
            genericVariablePanel.reset();
            genericVariablePanel.setEditing(false);
//            genericVariables.add(v);
        }
        List<Variable> genericVariables = genericVariablePanel.getVariables();
        doc.setVariables(genericVariables);

        return doc;
    }

    private void onPostMessage(String data, String origin) {
//        Notify.notify("PostMessage: ", "received \"" + data + "\" from " + origin);
//        logToConsole("OAME: PostMessage: received \"" + data + "\" from " + origin);
        if ( "closing".equalsIgnoreCase(data)) {
            logToConsole("OAME: Closing");
            boolean isDirty = currentDocumentIsDirty();
            logToConsole("isDirty:"+isDirty + ", saved:"+saved);
            if ( isDirty ) { //&& !saved ) {
                logToConsole("OAME: Notify save");
                saveSection(Constants.SECTION_DOCUMENT, "Notify");
                sendMessage("Roger That:"+data, origin);
            } else {
                sendMessage("Neg That:"+data, origin);
            }
        } else if ( "dirty".equalsIgnoreCase(data)) {
            boolean isDirty = currentDocumentIsDirty();
            sendMessage("Roger That:"+data+":"+String.valueOf(isDirty), origin);
        }
    }

    private native void sendMessage(String message, String origin) /*-{
        console.log("OAME: sending msg \""+message+"\" to " + origin);
        var p = top;
        p.postMessage(message, origin);
    }-*/;

    private native void setupMessageListener(OAPMetadataEditor instance) /*-{
        console.log("OAME: Setting up message listener");
        function postMsgListener(event) {
            console.log("OAME: recv msg:" + ( event.data ? event.data : event ) + " from " + event.origin);
            instance.@gov.noaa.pmel.sdig.client.OAPMetadataEditor::onPostMessage(Ljava/lang/String;Ljava/lang/String;) (
                            event.data, event.origin
            );
            // var p = top;
            // p.postMessage("Roger That:"+event.data, event.origin);
        }
//        postMsgListener( "setup message")
        $wnd.addEventListener('message', postMsgListener, false);
        // ie
//        $wnd.attachEvent('onmessage', postMsgListener);
    }-*/;

    // "Save" button
    TextCallback saveNotify = new TextCallback() {
        @Override
        public void onFailure(Method method, Throwable throwable) {
            String msg = "saveNotify " + method.toString() + " error : " + throwable.toString();
            Window.alert("There was an error saving your document.  Please try again later.");
            logToConsole(msg);
        }
        @Override
        public void onSuccess(Method method, String response) {
//            documentLocation = response;
//            _datasetId = documentLocation.substring(documentLocation.lastIndexOf('/')+1);
//            _loadedDocument = Document.copy(_savedDoc);
            loadJsonDocument(response, true, true);
            info("Your document has been saved and is available.");
            saved = true;
        }
    };
    // "Download" button
    TextCallback documentSaved = new TextCallback() {
        @Override
        public void onFailure(Method method, Throwable throwable) {
            Window.alert("There was an error saving your document.  Please try again later.");
        }
        @Override
        public void onSuccess(Method method, String response) {
            if ( response.equals("failed") ) {
                Window.alert("Something went wrong. Check with your server administrators.");
            } else {
//                documentLocation = s;
//                _datasetId = documentLocation.substring(documentLocation.lastIndexOf('/')+1);
//                _loadedDocument = Document.copy(_savedDoc);
                loadJsonDocument(response, true, true);
                modalHeader.setTitle("Save XML file.");
                save.setType(ButtonType.PRIMARY);
                save.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        modal.hide();
                        saved = true;
                        Window.open(Constants.base+"document/xml/"+ _datasetId,"_blank", null);
                    }
                });

                modal.show();
            }
        }
    };
    // "Preview" button
    TextCallback previewDocument = new TextCallback() {
        @Override
        public void onFailure(Method method, Throwable throwable) {
            Window.alert("There was an error previewing your document.  Please try again later.");
        }
        @Override
        public void onSuccess(Method method, String s) {
            if ( s.equals("failed") ) {
                Window.alert("Something went wrong. Check with your server administrators.");
            } else {
                documentLocation = s;
                _datasetId = documentLocation.substring(documentLocation.lastIndexOf('/')+1);
                Window.open(Constants.base+"document/preview/"+ _datasetId,"md_preview", null);
            }
        }
    };
    // Fetch document requested by id=? query param
    TextCallback documentFetched = new TextCallback() {
        @Override
        public void onFailure(Method method, Throwable throwable) {
            Window.alert("There was an error retrieving your document.  Please try again later.");
        }
        @Override
        public void onSuccess(Method method, String s) {
            if ( s.equals("failed") ) {
                Window.alert("Something went wrong. Check with your server administrators.");
            } else {
                loadJsonDocument(s, true, true);
                topLayout.setMain(submitterPanel);
                topLayout.setActive(Constants.SECTION_SUBMITTER);
            }
        }
    };
    // Upload file pre-action
    AbstractForm.SubmitHandler submitHandler = new AbstractForm.SubmitHandler() {
        @Override
        public void onSubmit(AbstractForm.SubmitEvent submitEvent) {
            String value = topLayout.filename.getValue();
            if ( value == null || value.length() <= 0 ) {
                submitEvent.cancel();
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.WARNING);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.NO_FILE, settings);
            }
            String postDocId = _datasetId != null ?
                                _datasetId :
                                _requestedDocumentId != null ?
                                    _requestedDocumentId :
                                    "";
            topLayout.uploadForm.setAction("document/upload/" + postDocId);
        }
    };
    // Upload file process results (uploaded file Document)
    Form.SubmitCompleteHandler completeHandler = new Form.SubmitCompleteHandler() {
        @Override
        public void onSubmitComplete(AbstractForm.SubmitCompleteEvent submitCompleteEvent) {
            String jsonString = submitCompleteEvent.getResults();
            if ( wasError(jsonString )) {
                Window.alert("There was an error processing your file:" + jsonString.substring("ERROR:".length()));
                return;
            }
            mergeJsonDocument(jsonString);
        }
    };

    private boolean currentDocumentIsDirty() {
        debugLog("_loadedDoc:"+_loadedDocument);
        Document compDoc = _loadedDocument != null ? _loadedDocument : Document.EmptyDocument();
        debugLog("Checking dirty against " + compDoc);
//        Window.alert("Checking dirty against " + compDoc);
        boolean isDirty =
            submitterPanel.isDirty(compDoc.getDataSubmitter()) ||
            investigatorPanel.isDirty(compDoc.getInvestigators()) ||
            citationPanel.isDirty(compDoc.getCitation()) ||
            timeAndLocationPanel.isDirty(compDoc.getTimeAndLocation()) ||
            fundingPanel.isDirty(compDoc.getFunding()) ||
            platformPanel.isDirty(compDoc.getPlatforms()) ||
            dicPanel.isDirty(compDoc.getDic()) ||
            taPanel.isDirty(compDoc.getTa()) ||
            phPanel.isDirty(compDoc.getPh()) ||
            pco2aPanel.isDirty(compDoc.getPco2a()) ||
            pco2dPanel.isDirty(compDoc.getPco2d()) ||
            genericVariablePanel.isDirty(compDoc.getVariables());
        debugLog("Found dirty: " + isDirty);
//        Window.alert("Found dirty: " + isDirty);
        return isDirty;
    }
    private void startOver(boolean clearIds) {
        // Reset containers for all information being collected to null.
        _loadedDocument = null;
        if ( clearIds ) {
            _datasetId = null;
            _documentDbId = null;
            _documentDbVersion = null;
        }
//        dataSubmitter = null;
        if ( investigatorPanel != null ) investigatorPanel.clearPeople();
//        citation = null;
//        timeAndLocation = null;
//        funding = null;
        if ( fundingPanel != null ) fundingPanel.clearFundings();
        if ( platformPanel != null ) platformPanel.clearPlatforms();
//        dic = null;
//        ta = null;
//        ph = null;
//        pco2a = null;
//        pco2d = null;
        if ( genericVariablePanel != null ) genericVariablePanel.clearVariables();

        // Reset all forms
        if (submitterPanel != null ) submitterPanel.reset();
        if (investigatorPanel != null ) investigatorPanel.reset();
        if ( citationPanel != null ) citationPanel.reset(clearIds);
        if ( timeAndLocationPanel != null ) timeAndLocationPanel.reset();
        if ( fundingPanel != null ) fundingPanel.reset();
        if ( platformPanel != null ) platformPanel.reset();
        if ( dicPanel != null ) dicPanel.reset();
        if ( taPanel != null ) taPanel.reset();
        if ( phPanel != null ) phPanel.reset();
        if ( pco2aPanel != null ) pco2aPanel.reset();
        if ( pco2dPanel != null ) pco2dPanel.reset();
        if ( genericVariablePanel != null ) genericVariablePanel.reset();
        topLayout.resetFileForm();
        topLayout.removeCheckmarks();
        topLayout.removeSectionHiglightStyle("pill-warning");
        topLayout.removeSectionHiglightStyle("pill-danger");
    }
    private void notSaved() {
        NotifySettings settings = NotifySettings.newSettings();
        settings.setType(NotifyType.WARNING);
        settings.setPlacement(NotifyPlacement.TOP_CENTER);
        Notify.notify(Constants.NOT_SAVED, settings);

    }
    private void loadDocumentId(String documentId)
    {
        GWT.log("loadDocument " + documentId);
        _requestedDocumentId = documentId;
        getDocumentService.get(documentId, documentFetched);
    }
    private boolean wasError(String response) {
        return response != null && response.trim().startsWith("ERROR:");
    }
    private void mergeJsonDocument(String jsonString) {
        loadJsonDocument(jsonString, false, false);
    }
    private Document documentFromJson(String jsonString) {
        // A bug discussed in various places on the 'net, but nothing specific to grails.
        // Just work around for now
        jsonString = jsonString.replace("<pre style=\"word-wrap: break-word; white-space: pre-wrap;\">", "")
                .replace("</pre>", "");
        jsonString = jsonString.replace("<pre>", "");

        JSONValue json = JSONParser.parseStrict(jsonString);
        Document document = codec.decode(json);
        _loadedDocument = Document.copy(document);
        return document;
    }
    private Document loadDocumentElements(Document document) {
        _datasetId = document.getDatasetIdentifier();
//        _documentDbId = document.getId();
        _documentDbVersion = document.getDbVersion();
        return document;
    }
    private void loadJsonDocument(String jsonString, boolean clearFirst, boolean updateIds) {
        try {
            if ( clearFirst ) {
                startOver(updateIds);
            }
            Document document = documentFromJson(jsonString);
            if ( updateIds ) {
                loadDocumentElements(document);
            }
            debugLog("OAME: Setting document to: " + document );
//            investigatorPanel.clearPeople();
//            if (document.getInvestigators() != null) {
//                List<Person> personList = document.getInvestigators();
//                investigatorPanel.reset(); // XXX Should this also be commented out.  What about merge?
//                investigatorPanel.addPeople(personList);
//            }
            if (document.getInvestigators() != null) {
                boolean hasValidData = false;
                boolean hasInvalidData = false;
                List<Person> personList = document.getInvestigators();
                for (int i = 0; i < personList.size(); i++) {
                    Person p = personList.get(i);
                    investigatorPanel.show(p);
                    if (! investigatorPanel.valid()) {
                        hasInvalidData = true;
//                        topLayout.setChecked(Constants.SECTION_INVESTIGATOR);
//                        topLayout.removehighlight(Constants.SECTION_INVESTIGATOR, "pill-warning");
                    }
                    else {
                        hasValidData = true;
                    }
                    investigatorPanel.reset();
                }
                if (hasValidData == true && hasInvalidData == true) {
                    debugLog("warning investigatorPanel valid and invalid");
                    topLayout.setChecked(Constants.SECTION_INVESTIGATOR);
                    topLayout.sethighlight(Constants.SECTION_INVESTIGATOR, "pill-warning");
                }
                if (hasValidData == true && hasInvalidData == false) {
                    debugLog("success investigatorPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_INVESTIGATOR);
                    topLayout.removehighlight(Constants.SECTION_INVESTIGATOR, "pill-warning");
                    topLayout.removehighlight(Constants.SECTION_INVESTIGATOR, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == true){
                    debugLog("danger investigatorPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_INVESTIGATOR, "pill-danger");
                }
//                if (investigatorPanel.valid()) {
//                    debugLog("investigatorPanel valid is true? " + p);
//                    topLayout.setChecked(Constants.SECTION_INVESTIGATOR);
//                    topLayout.removehighlight(Constants.SECTION_INVESTIGATOR, "pill-warning");
//                }
//                else {
//                    hasValidData = false;
//                    debugLog("investigatorPanel valid is false? " + p);
//                    topLayout.sethighlight(Constants.SECTION_INVESTIGATOR, "pill-warning");
//                }
                investigatorPanel.addPeople(personList);

            }

            // TODO this has to be redone to work with the data provider
            // what does that mean?
//            List<Variable> variablesList = document.getVariables();
//            if (variablesList != null) {
//                genericVariablePanel.addVariables(variablesList);
//            }


//
//            if (document.getVariables() != null) {
//                List<Variable> variablesList = document.getVariables();
//                for (int i = 0; i < variablesList.size(); i++) {
//                    Variable v = variablesList.get(i);
//                    genericVariablePanel.show(v);
//                    if (genericVariablePanel.valid()) {
//                        topLayout.setChecked(Constants.SECTION_GENERIC);
//                        topLayout.removehighlight(Constants.SECTION_GENERIC, "pill-warning");
//                    }
//                    genericVariablePanel.reset();
//                }
//                genericVariablePanel.addVariables(variablesList);
//
//            }



            if (document.getVariables() != null) {
                boolean hasValidData = false;
                boolean hasInvalidData = false;
                List<Variable> variablesList = document.getVariables();
                for (int i = 0; i < variablesList.size(); i++) {
                    Variable v = variablesList.get(i);
                    genericVariablePanel.show(v);
                    if (! genericVariablePanel.valid()) {
                        hasInvalidData = true;
                    }
                    else {
                        hasValidData = true;
                    }
                    genericVariablePanel.reset();
                }
                if (hasValidData == true && hasInvalidData == true) {
                    debugLog("warning investigatorPanel valid and invalid");
                    topLayout.setChecked(Constants.SECTION_GENERIC);
                    topLayout.sethighlight(Constants.SECTION_GENERIC, "pill-warning");
                }
                if (hasValidData == true && hasInvalidData == false) {
                    debugLog("success investigatorPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_GENERIC);
                    topLayout.removehighlight(Constants.SECTION_GENERIC, "pill-warning");
                    topLayout.removehighlight(Constants.SECTION_GENERIC, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == true){
                    debugLog("danger investigatorPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_GENERIC, "pill-danger");
                }
                genericVariablePanel.addVariables(variablesList);
            }


            if (document.getPlatforms() != null) {
                debugLog("got Platforms?");
                List<Platform> platforms = document.getPlatforms();
                debugLog("# of Platforms? " + platforms.size());
                for (int i = 0; i < platforms.size(); i++) {
                    debugLog("Platforms index: " + i);
                    Platform p = platforms.get(i);
                    debugLog("Platform p: " + p);
                    platformPanel.show(p);
                    if (platformPanel.valid()) {
                        topLayout.setChecked(Constants.SECTION_PLATFORMS);
                        topLayout.removehighlight(Constants.SECTION_PLATFORMS, "pill-warning");
                    }
                    platformPanel.reset();
                }
//                platformPanel.addPlatforms(document.getPlatforms());
                platformPanel.addPlatforms(platforms);

            }
//            Person dataSubmitter = null;
            if (document.getDataSubmitter() != null) {
                Person dataSubmitter = document.getDataSubmitter();
                submitterPanel.show(dataSubmitter);
                if (submitterPanel.valid()) {
                    debugLog("success submitterPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_SUBMITTER);
                    topLayout.removehighlight(Constants.SECTION_SUBMITTER, "pill-warning");
                }
                else {
                    debugLog("danger submitterPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_SUBMITTER, "pill-danger");
                }
            }
            if (document.getCitation() != null) {
                Citation citation = document.getCitation();
                citationPanel.show(citation);
                if (citationPanel.valid()) {
                    topLayout.setChecked(Constants.SECTION_CITATION);
                    topLayout.removehighlight(Constants.SECTION_CITATION, "pill-warning");
                }
            }
            if (document.getTimeAndLocation() != null) {
                TimeAndLocation timeAndLocation = document.getTimeAndLocation();
                timeAndLocationPanel.show(timeAndLocation);
                if (timeAndLocationPanel.valid()) {
                    topLayout.setChecked(Constants.SECTION_TIMEANDLOCATION);
                    topLayout.removehighlight(Constants.SECTION_TIMEANDLOCATION, "pill-warning");
                }
            }
            if (document.getFunding() != null) {
                List<Funding> fundings = document.getFunding();
                for( int i = 0; i < fundings.size(); i++ ) {
                    Funding f = fundings.get(i);
                    fundingPanel.show(f);
                    if ( fundingPanel.valid() ) {
                        topLayout.setChecked(Constants.SECTION_FUNDING);
                        topLayout.removehighlight(Constants.SECTION_FUNDING, "pill-warning");
                    }
                    fundingPanel.reset();
                }
                fundingPanel.addFundings(fundings);
            }
            if (document.getDic() != null) {
                Variable dic = document.getDic();
                dicPanel.show(dic);
                if (dicPanel.valid()) {
                    topLayout.setChecked(Constants.SECTION_DIC);
                    topLayout.removehighlight(Constants.SECTION_DIC, "pill-warning");
                }
            }
            if (document.getTa() != null) {
                Variable ta = document.getTa();
                taPanel.show(ta);
                if (taPanel.valid()) {
                    topLayout.setChecked(Constants.SECTION_TA);
                    topLayout.removehighlight(Constants.SECTION_TA, "pill-warning");
                }
            }
            if (document.getPh() != null) {
                Variable ph = document.getPh();
                phPanel.show(ph);
                if (phPanel.valid()) {
                    topLayout.setChecked(Constants.SECTION_PH);
                    topLayout.removehighlight(Constants.SECTION_PH, "pill-warning");
                }
            }
//            Variable pco2a = null;
//            pco2aPanel.isDirty
            if (document.getPco2a() != null) {
                debugLog("pco2a is not null");

//                common.abbreviation.setText("pCO2a");
//                common.fullVariableName.setText("pco2 (fco2) autonomous");
                Variable pco2a = document.getPco2a();
//                pco2a = document.getPco2a();
                pco2aPanel.show(pco2a);
                if (! pco2aPanel.valid()) {
                    debugLog("danger pco2aPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_PCO2A, "pill-danger");
                }
                else {
                    debugLog("success pco2aPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_PCO2A);
                    topLayout.removehighlight(Constants.SECTION_PCO2A, "pill-danger");
                }
            }
            if (document.getPco2a() == null) {
                debugLog("pco2a is null");
                topLayout.sethighlight(Constants.SECTION_PCO2A, "pill-danger");
            }

            Variable pco2d = null;
            if (document.getPco2d() != null) {
//                Variable pco2d = document.getPco2d();
                pco2d = document.getPco2d();
                pco2dPanel.show(pco2d);
                if (pco2dPanel.valid()) {
                    debugLog("success pco2dPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_PCO2D);
                    topLayout.removehighlight(Constants.SECTION_PCO2D, "pill-danger");
                }
                else {
                    debugLog("danger pco2dPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_PCO2D, "pill-danger");
                }
            }
            if (document.getPco2d() == null) {
                debugLog("pco2d is null");
                topLayout.sethighlight(Constants.SECTION_PCO2D, "pill-danger");
            }


            if ( !clearFirst ) {
                _currentDocument = getDocument();
            }
        } catch (Exception e) {
            Window.alert("File not processed. e="+e.getLocalizedMessage());
            logToConsole(jsonString);
        }
        topLayout.resetFileForm();
    }
}
