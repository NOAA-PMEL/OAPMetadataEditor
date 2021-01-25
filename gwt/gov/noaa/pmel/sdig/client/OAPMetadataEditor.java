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
import java.util.stream.Collectors;
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

    public interface DocumentCodec extends JsonEncoderDecoder<Document> {
    }

    public interface GetDocumentService extends RestService {
        @Path("{id}")
        public void get(@PathParam("id") String id, TextCallback textCallback);
    }

    private static boolean DEBUG = true;

    public static void debugLog(String msg) {
        if (DEBUG) {
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
                if (source instanceof Button) {
                    Button b = (Button) source;
                    if (b.getText().equals("Clear All")) {
                            //#DEBUG
//                        debugLog("Called Clear ALL: ");
                        if (currentDocumentIsDirty() && !saved) {

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
                if ("Download".equals(event.getSectionContents()) ||
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
                if (link.getText().equals(Constants.SECTION_INVESTIGATOR)) {
                    topLayout.setMain(investigatorPanel);
                    topLayout.setActive(Constants.SECTION_INVESTIGATOR);
                    if (investigatorPanel.getInvestigators().size() > 0) {
                        investigatorPanel.setTableVisible(true);
                    }
                } else if (link.getText().equals(Constants.SECTION_SUBMITTER)) {
//                    if ( dataSubmitter != null ) {
//                        submitterPanel.show(dataSubmitter);
//                    }
                    topLayout.setMain(submitterPanel);
                    topLayout.setActive(Constants.SECTION_SUBMITTER);
                } else if (link.getText().equals(Constants.SECTION_CITATION)) {
//                    if ( citation != null ) {
//                        citationPanel.show(citation);
//                    }
                    topLayout.setMain(citationPanel);
                    topLayout.setActive(Constants.SECTION_CITATION);
                } else if (link.getText().equals(Constants.SECTION_TIMEANDLOCATION)) {
//                    if ( timeAndLocation != null ) {
//                        timeAndLocationPanel.show(timeAndLocation);
//                    }
                    topLayout.setMain(timeAndLocationPanel);
                    topLayout.setActive(Constants.SECTION_TIMEANDLOCATION);
                } else if (link.getText().equals(Constants.SECTION_FUNDING)) {
                    topLayout.setMain(fundingPanel);
                    topLayout.setActive(Constants.SECTION_FUNDING);
                    if (fundingPanel.getFundings().size() > 0) {
                        fundingPanel.setTableVisible(true);
                    }
                } else if (link.getText().equals(Constants.SECTION_PLATFORMS)) {
                    topLayout.setMain(platformPanel);
                    topLayout.setActive(Constants.SECTION_PLATFORMS);
                    if (platformPanel.getPlatforms().size() > 0) {
                        platformPanel.setTableVisible(true);
                    }
                } else if (link.getText().equals(Constants.SECTION_DIC)) {
                    topLayout.setMain(dicPanel);
                    topLayout.setActive(Constants.SECTION_DIC);
//                    if (dic != null) {
//                        dicPanel.show(dic);
//                    }
                } else if (link.getText().equals(Constants.SECTION_TA)) {
                    topLayout.setMain(taPanel);
                    topLayout.setActive(Constants.SECTION_TA);
//                    if (ta != null) {
//                        taPanel.show(ta);
//                    }
                } else if (link.getText().equals(Constants.SECTION_PH)) {
                    topLayout.setMain(phPanel);
                    topLayout.setActive(Constants.SECTION_PH);
//                    if (ph != null) {
//                        phPanel.show(ph);
//                    }
                } else if (link.getText().equals(Constants.SECTION_PCO2A)) {
                    topLayout.setMain(pco2aPanel);
                    topLayout.setActive(Constants.SECTION_PCO2A);
//                    if ( pco2a != null ) {
//                        pco2aPanel.show(pco2a);
//                    }
                } else if (link.getText().equals(Constants.SECTION_PCO2D)) {
                    topLayout.setMain(pco2dPanel);
                    topLayout.setActive(Constants.SECTION_PCO2D);
//                    if ( pco2d != null) {
//                        pco2dPanel.show(pco2d);
//                    }
                } else if (link.getText().equals(Constants.SECTION_GENERIC)) {
                    topLayout.setMain(genericVariablePanel);
                    topLayout.setActive(Constants.SECTION_GENERIC);
                    if (genericVariablePanel.getVariables().size() > 0) {
                        genericVariablePanel.setTableVisible(true);
                    }
                }
            }
        });
        ((RestServiceProxy) saveDocumentService).setResource(saveDocumentResource);
        ((RestServiceProxy) getDocumentService).setResource(getDocumentResource);
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
        if (docId != null) {
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
        logToConsole("saveSection:" + type + ", " + sectionContents);
        saved = false;
        if (type.equals(Constants.SECTION_INVESTIGATOR)) {
            // List is in the celltable data provider in the layout. Nothing to do here.
        } else if (type.equals(Constants.SECTION_SUBMITTER)) {
//            dataSubmitter = (Person) sectionContents;
            topLayout.setMain(investigatorPanel);
            topLayout.setActive(Constants.SECTION_INVESTIGATOR);
        } else if (type.equals(Constants.SECTION_CITATION)) {
//            citation = (Citation) sectionContents;
//            if ( timeAndLocation != null ) {
//                timeAndLocationPanel.show(timeAndLocation);
//            }
            topLayout.setMain(timeAndLocationPanel);
            topLayout.setActive(Constants.SECTION_TIMEANDLOCATION);
        } else if (type.equals(Constants.SECTION_TIMEANDLOCATION)) {
//            TimeAndLocation timeAndLocation = (TimeAndLocation) sectionContents;
//            if ( funding != null ) {
//                fundingPanel.show(funding);
//            }
            topLayout.setMain(fundingPanel);
            topLayout.setActive(Constants.SECTION_FUNDING);
        } else if (type.equals(Constants.SECTION_FUNDING)) {
            // Nothing to do here, list in the panel
        } else if (type.equals(Constants.SECTION_PLATFORMS)) {
            // List kept in the panel. Nothing to do here.
        } else if (type.equals(Constants.SECTION_DIC)) {
//            if ( dic == null ) {
//                dic = (Variable) sectionContents;
//            } else {
//                dicPanel.fill(dic);
//            }
            topLayout.setMain(taPanel);
            topLayout.setActive(Constants.SECTION_TA);
        } else if (type.equals(Constants.SECTION_TA)) {
//            if ( ta == null ) {
//                ta = (Variable) sectionContents;
//            } else {
//                taPanel.fill(ta);
//            }
            topLayout.setMain(phPanel);
            topLayout.setActive(Constants.SECTION_PH);
        } else if (type.equals(Constants.SECTION_PH)) {
//            if ( ph == null ) {
//                ph = (Variable) sectionContents;
//            } else {
//                phPanel.fill(ph);
//            }
            topLayout.setMain(pco2aPanel);
            topLayout.setActive(Constants.SECTION_PCO2A);
        } else if (type.equals(Constants.SECTION_PCO2A)) {
//            if ( pco2a == null ) {
//                pco2a = (Variable) sectionContents;
//            } else {
//                pco2aPanel.fill(pco2a);
//            }
            topLayout.setMain(pco2dPanel);
            topLayout.setActive(Constants.SECTION_PCO2D);
        } else if (type.equals(Constants.SECTION_PCO2D)) {
//            if ( pco2d == null ) {
//                pco2d = (Variable) sectionContents;
//            } else {
//                pco2dPanel.fill(pco2d);
//            }
            topLayout.setMain(genericVariablePanel);
            topLayout.setActive(Constants.SECTION_GENERIC);
        } else if (type.equals(Constants.SECTION_GENERIC)) {
            // List managed in the panel
        } else if (type.equals(Constants.SECTION_DOCUMENT)) {
            String content = (String) sectionContents;
            TextCallback callback = null;
            switch (content) {
                case "Download":
                    callback = documentSaved;
                    break;
                case "Notify":
                    callback = saveNotify;
                    break;
                case "Preview":
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
        debugLog("saveDocToServer has been called");
        if (!topLayout.isSufficientlyComplete()) {
            warn(Constants.DOCUMENT_NOT_COMPLETE);
        }
        Document doc = getDocument();
        saveDocumentService.save(getDatasetId(doc), doc, callback);
        saved = true;
    }

    public String getDatasetId(Document doc) {
        if (_datasetId != null) {
            return _datasetId;
        }
        if (doc.getDatasetIdentifier() != null) {
            return doc.getDatasetIdentifier();
        }
//        if (doc.getCitation().getExpocode() != null && doc.getCitation().getExpocode().length() > 0) {
//            return doc.getCitation().getExpocode();
//        }
        return null;
    }

    public Document getDocument() {
        debugLog("getDocument has been called");

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
        if (investigatorPanel.hasContent()) {
            Person p = investigatorPanel.getPerson();
            if (!p.isComplete()) {
                warn("Current Investigator is not complete.");
            }
            investigatorPanel.addPerson(p);
            investigatorPanel.reset();
            investigatorPanel.setEditing(false);

        }
        List<Person> investigators = investigatorPanel.getInvestigators();
        for (int i = 0; i < investigators.size(); i++) {
            Person p = investigators.get(i);
//            debugLog("getDocument: " + p.getLastName() + "'s pre-position: " + p.getPosition());
            // update position according to index
            if (p.getPosition() != i) {
                p.setPosition(i);
//                debugLog("getDocument: " + p.getLastName() + "'s post-position: " + p.getPosition());
            }
        }
        doc.setInvestigators(investigators);
        // Citation Panel
        Citation citation = citationPanel.getCitation();
        doc.setCitation(citation);
        // TimeAndLocationPanel
        TimeAndLocation timeAndLocation = timeAndLocationPanel.getTimeAndLocation();
        doc.setTimeAndLocation(timeAndLocation);

        // Funding Panel
//        List<Funding> fundings = fundingPanel.getFundings();
//        if (fundingPanel.isDirty()) {
//            Funding f = fundingPanel.getFunding();
//            fundings.add(f);
//        }
//        doc.setFunding(fundings);
        if (fundingPanel.hasContent()) {
            Funding f = fundingPanel.getFunding();
            fundingPanel.addFunding(f);
            fundingPanel.reset();
            fundingPanel.setEditing(false);
        }
        List<Funding> fundings = fundingPanel.getFundings();
        doc.setFunding(fundings);

        // Platforms Panel
//        List<Platform> platforms = platformPanel.getPlatforms();
//        if ( platformPanel.isDirty() ) {
//            Platform p = platformPanel.getPlatform();
//            platforms.add(p);
//        }
//        doc.setPlatforms(platforms);
        //
        if (platformPanel.hasContent()) {
            Platform p = platformPanel.getPlatform();
            platformPanel.addPlatform(p);
            platformPanel.reset();
            platformPanel.setEditing(false);
//            genericVariables.add(v);
        }
        List<Platform> platforms = platformPanel.getPlatforms();
        doc.setPlatforms(platforms);

        // Dic Panel
        if (dicPanel.isDirty()) {
            Variable dic = dicPanel.getDic();
            doc.setDic(dic);
        }
        // Ta
        if (taPanel.isDirty()) {
            Variable ta = taPanel.getTa();
            doc.setTa(ta);
        }
        // Ph
        if (phPanel.isDirty()) {
            Variable ph = phPanel.getPh();
            doc.setPh(ph);
        }
        // pco2a
        if (pco2aPanel.isDirty()) {
            Variable pco2a = pco2aPanel.getPco2a();
            doc.setPco2a(pco2a);
        }
        // pco2d
        if (pco2dPanel.isDirty()) {
            Variable pco2d = pco2dPanel.getPco2d();
            doc.setPco2d(pco2d);
        }
        // Generic Variables
        if (genericVariablePanel.hasContent()) {
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
        if ("closing".equalsIgnoreCase(data)) {
            logToConsole("OAME: Closing");
            boolean isDirty = currentDocumentIsDirty();
            logToConsole("isDirty:" + isDirty + ", saved:" + saved);
            if (isDirty) { //&& !saved ) {
                logToConsole("OAME: Notify save");
                saveSection(Constants.SECTION_DOCUMENT, "Notify");
                sendMessage("Roger That:" + data, origin);
            } else {
                sendMessage("Neg That:" + data, origin);
            }
        } else if ("dirty".equalsIgnoreCase(data)) {
            boolean isDirty = currentDocumentIsDirty();
            sendMessage("Roger That:" + data + ":" + String.valueOf(isDirty), origin);
        }
    }

    private native void sendMessage(String message, String origin) /*-{
        console.log("OAME: sending msg \"" + message + "\" to " + origin);
        var p = top;
        p.postMessage(message, origin);
    }-*/;

    private native void setupMessageListener(OAPMetadataEditor instance) /*-{
        console.log("OAME: Setting up message listener");

        function postMsgListener(event) {
            console.log("OAME: recv msg:" + (event.data ? event.data : event) + " from " + event.origin);
            instance.@gov.noaa.pmel.sdig.client.OAPMetadataEditor::onPostMessage(Ljava/lang/String;Ljava/lang/String;)(
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
            Window.alert("There was an error saving your document.  Please try again later.");
            String msg = "saveNotify " + method.toString() + " error : " + throwable.toString();
            logToConsole(msg);
        }

        @Override
        public void onSuccess(Method method, String response) {
            documentLocation = response;
            _datasetId = documentLocation.substring(documentLocation.lastIndexOf('/') + 1);
//            _loadedDocument = Document.copy(_savedDoc);
//            loadJsonDocument(response, true, true);
            loadDocumentId(_datasetId);
            info("Your document has been saved and is available.");
            saved = true;
        }
    };
    // "Download" button
    TextCallback documentSaved = new TextCallback() {
        @Override
        public void onFailure(Method method, Throwable throwable) {
            Window.alert("There was an error saving your document.  Please try again later.");
            String msg = "documentSaved " + method.toString() + " error : " + throwable.toString();
            logToConsole(msg);
        }

        @Override
        public void onSuccess(Method method, String response) {
            if (response.equals("failed")) {
                Window.alert("Something went wrong. Please check with the application administrators.");
            } else {
                documentLocation = response;
                _datasetId = documentLocation.substring(documentLocation.lastIndexOf('/') + 1);
//                _loadedDocument = Document.copy(_savedDoc);
//                loadJsonDocument(response, true, true);
                modalHeader.setTitle("Save XML file.");
                save.setType(ButtonType.PRIMARY);
                save.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        modal.hide();
                        saved = true;
                        Window.open(Constants.base + "document/xml/" + _datasetId, "_blank", null);
                    }
                });

                modal.show();
                loadDocumentId(_datasetId);
            }
        }
    };
    // "Preview" button
    TextCallback previewDocument = new TextCallback() {
        @Override
        public void onFailure(Method method, Throwable throwable) {
            debugLog("TextCallback previewDocument called");
            Window.alert("There was an error previewing your document.  Please try again later.");
            String msg = "previewDocument " + method.toString() + " error : " + throwable.toString();
            logToConsole(msg);
        }

        @Override
        public void onSuccess(Method method, String s) {
            debugLog("TextCallback previewDocument called");
            if (s.equals("failed")) {
                Window.alert("Something went wrong. Please check with the application administrators.");
            } else {
                documentLocation = s;
                _datasetId = documentLocation.substring(documentLocation.lastIndexOf('/') + 1);
                debugLog("TextCallback previewDocument:success: _datasetId: " + _datasetId);
                Window.open(Constants.base + "document/preview/" + _datasetId, "md_preview", null);
                loadDocumentId(_datasetId);
            }
        }
    };
    // Fetch document requested by id=? query param
    TextCallback documentFetched = new TextCallback() {
        @Override
        public void onFailure(Method method, Throwable throwable) {
            Window.alert("There was an error retrieving your document.  Please try again later.");
            String msg = "documentFetched " + method.toString() + " error : " + throwable.toString();
            logToConsole(msg);
        }

        @Override
        public void onSuccess(Method method, String s) {
            if (s.equals("failed")) {
                Window.alert("Something went wrong. Check with the application administrators.");
            } else {
                debugLog("TextCallback: onSuccess is calling loadJsonDocument true true");
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
            if (value == null || value.length() <= 0) {
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
            if (wasError(jsonString)) {
                Window.alert("There was an error processing your file:" + jsonString.substring("ERROR:".length()));
                String msg = "submitComplete failed error : " + jsonString;
                logToConsole(msg);
                return;
            }

            // prompt for merge while metadata panels have data.
            // Overwrite to overwrite the existing metadata panels.
            // Preserve exiting data and merge only empty values
//            if (currentDocumentIsDirty() && _loadedDocument != null) {
           if (currentDocumentIsDirty()) {
                 //#DEBUG
//               debugLog("currentDocumentIsDirty()@onSubmitComplete is true: " + currentDocumentIsDirty() + " --choose merge");

                final Modal mergeOptions = new Modal();
                ModalHeader header = new ModalHeader();
                Heading h = new Heading(HeadingSize.H3);
                h.setText("Merge Options");
                header.add(h);
                mergeOptions.add(header);
                ModalBody body = new ModalBody();
                HTML message = new HTML("<strong>Preserve</strong> will populate empty fields and append new data " +
                        "content from the uploaded file, but will not overwrite content in the current data panels." +
                        "<br><br><strong>Overwrite</strong> will append new data content from the uploaded file where " +
                        "applicable and will not preserve content in the current data panels.");
                ModalFooter footer = new ModalFooter();
                Button preserve = new Button("Preserve");
                preserve.setType(ButtonType.DANGER);
                Button overwrite = new Button("Overwrite");
                overwrite.setType(ButtonType.DANGER);
                Button cancel = new Button("Cancel");
                cancel.setType(ButtonType.PRIMARY);

                footer.add(preserve);
                footer.add(overwrite);
                footer.add(cancel);
                body.add(message);
                mergeOptions.add(body);
                mergeOptions.add(footer);

                mergeOptions.show();

                preserve.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        preserveMergeJsonDocument(jsonString);
                        mergeOptions.hide();
                    }
                });

                overwrite.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        mergeJsonDocument(jsonString);
                        mergeOptions.hide();
                    }
                });

                cancel.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        mergeOptions.hide();
                    }
                });

            } else {
                //#DEBUG
//               debugLog("currentDocumentIsDirty()onSubmitComplete is false: " + currentDocumentIsDirty() + " --overwrite empty doc");
                mergeJsonDocument(jsonString);
            }
        }
    };

    private boolean currentDocumentIsDirty() {
//        debugLog("_loadedDoc@currentDocumentIsDirty():" + _loadedDocument);
        Document compDoc = _loadedDocument != null ? _loadedDocument : Document.EmptyDocument();
//        debugLog("compDoc@currentDocumentIsDirty():" + compDoc);

//        debugLog("Checking dirty against " + compDoc);
//        boolean isDirty =
//                submitterPanel.isDirty(compDoc.getDataSubmitter()) ||
//                        investigatorPanel.isDirty(compDoc.getInvestigators()) ||
//                        citationPanel.isDirty(compDoc.getCitation()) ||
//                        timeAndLocationPanel.isDirty(compDoc.getTimeAndLocation()) ||
//                        fundingPanel.isDirty(compDoc.getFunding()) ||
//                        platformPanel.isDirty(compDoc.getPlatforms()) ||
//                        dicPanel.isDirty(compDoc.getDic()) ||
//                        taPanel.isDirty(compDoc.getTa()) ||
//                        phPanel.isDirty(compDoc.getPh()) ||
//                        pco2aPanel.isDirty(compDoc.getPco2a()) ||
//                        pco2dPanel.isDirty(compDoc.getPco2d()) ||
//                        genericVariablePanel.isDirty(compDoc.getVariables());
//        debugLog("Found dirty: " + isDirty);

        boolean isDirty = false;
        if (submitterPanel.isDirty(compDoc.getDataSubmitter())) {
            debugLog("submitterPanel is Dirty");
            isDirty = true;
        }
        if (citationPanel.isDirty(compDoc.getCitation())) {
            debugLog("citationPanel is Dirty");
            isDirty = true;
        }
        if (timeAndLocationPanel.isDirty(compDoc.getTimeAndLocation())) {
            debugLog("timeAndLocationPanel is Dirty");
            isDirty = true;
        }
        if (fundingPanel.isDirty(compDoc.getFunding())) {
            debugLog("fundingPanel is Dirty");
            isDirty = true;
        }
        if (platformPanel.isDirty(compDoc.getPlatforms())) {
            debugLog("platformPanel is Dirty");
            isDirty = true;
        }
        if (dicPanel.isDirty(compDoc.getDic())) {
            debugLog("dicPanel is Dirty");
            isDirty = true;
        }
        if (taPanel.isDirty(compDoc.getTa())) {
            debugLog("taPanel is Dirty");
            isDirty = true;
        }
        if (phPanel.isDirty(compDoc.getPh())) {
            debugLog("phPanel is Dirty");
            isDirty = true;
        }
        if (pco2aPanel.isDirty(compDoc.getPco2a())) {
            debugLog("pco2aPanel is Dirty");
            isDirty = true;
        }
        if (pco2dPanel.isDirty(compDoc.getPco2d())) {
            debugLog("pco2dPanel is Dirty");
            isDirty = true;
        }
        if (genericVariablePanel.isDirty(compDoc.getVariables())) {
            debugLog("genericVariablePanel is Dirty");
            isDirty = true;
        }

//        debugLog("isDirty@currentDocumentIsDirty(): " + isDirty);
        return isDirty;
    }

    private void startOver(boolean clearIds) {
        //#DEBUG
        debugLog("Called startOver()");
        // Reset containers for all information being collected to null.
        _loadedDocument = null;
        _currentDocument = null;
        if (clearIds) {
            _datasetId = null;
            _documentDbId = null;
            _documentDbVersion = null;
        }
//        dataSubmitter = null;
        if (investigatorPanel != null) investigatorPanel.clearPeople();
//        citation = null;
//        timeAndLocation = null;
//        funding = null;
        if (fundingPanel != null) fundingPanel.clearFundings();
        if (platformPanel != null) platformPanel.clearPlatforms();
//        dic = null;
//        ta = null;
//        ph = null;
//        pco2a = null;
//        pco2d = null;
        if (genericVariablePanel != null) genericVariablePanel.clearVariables();

        // Reset all forms
        if (submitterPanel != null) submitterPanel.reset();
        if (investigatorPanel != null) investigatorPanel.reset();
        if (citationPanel != null) citationPanel.reset(clearIds);
        if (timeAndLocationPanel != null) timeAndLocationPanel.reset();
        if (fundingPanel != null) fundingPanel.reset();
        if (platformPanel != null) platformPanel.reset();
        if (dicPanel != null) dicPanel.reset();
        if (taPanel != null) taPanel.reset();
        if (phPanel != null) phPanel.reset();
        if (pco2aPanel != null) pco2aPanel.reset();
        if (pco2dPanel != null) pco2dPanel.reset();
        if (genericVariablePanel != null) genericVariablePanel.reset();
        topLayout.resetFileForm();
        topLayout.removeCheckmarks();
        topLayout.removeSectionHiglightStyle("pill-warning");
        topLayout.removeSectionHiglightStyle("pill-danger");

        //#DEBUG
//        debugLog("_loadedDocument@startOver() " + _loadedDocument);

    }

    private void notSaved() {
        NotifySettings settings = NotifySettings.newSettings();
        settings.setType(NotifyType.WARNING);
        settings.setPlacement(NotifyPlacement.TOP_CENTER);
        Notify.notify(Constants.NOT_SAVED, settings);

    }

    private void loadDocumentId(String documentId) {
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

    private void preserveMergeJsonDocument(String jsonString) {
        preserveLoadJsonDocument(jsonString, false, false);
    }

    private Document documentFromJson(String jsonString) {
        // A bug discussed in various places on the 'net, but nothing specific to grails.
        // Just work around for now
        jsonString = jsonString.replace("<pre style=\"word-wrap: break-word; white-space: pre-wrap;\">", "")
                .replace("</pre>", "");
        jsonString = jsonString.replace("<pre>", "");
        GWT.log("json string:" + jsonString);
        boolean again = false;
        do {
            String newJsonString = jsonString.replaceAll("&amp;", "&");
            again = !newJsonString.equalsIgnoreCase(jsonString);
            jsonString = newJsonString;
        } while (again);
        jsonString = jsonString.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
        JSONValue json = JSONParser.parseStrict(jsonString);
        Document document = codec.decode(json);
        _loadedDocument = Document.copy(document);
        return document;
    }

    private Document loadDocumentElements(Document document) {
        debugLog("loadDocumentElements has been called");
        _datasetId = document.getDatasetIdentifier();
//        _documentDbId = document.getId();
        _documentDbVersion = document.getDbVersion();
        return document;
    }

    // overwrite data merge
    private void loadJsonDocument(String jsonString, boolean clearFirst, boolean updateIds) {
        debugLog("loadJsonDocument has been called");
        try {
            if (clearFirst) {
                startOver(updateIds);
            }

            Document document = documentFromJson(jsonString);
            if (updateIds) {
                debugLog("has updateIds: " + updateIds);
                loadDocumentElements(document);
            }
            debugLog("OAME: Setting document to: " + document);

            // get document thats currently populated
            Document originalDocument = getDocument();

            if (document.getDataSubmitter() != null) {
                Person dataSubmitter = document.getDataSubmitter();
                submitterPanel.show(dataSubmitter);
                if (!submitterPanel.valid()) {
//                    debugLog("danger submitterPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_SUBMITTER, "pill-danger");
                } else {
//                    debugLog("success submitterPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_SUBMITTER);
                    topLayout.removehighlight(Constants.SECTION_SUBMITTER, "pill-danger");
                }
            }
            if (document.getDataSubmitter() == null) {
//                debugLog("submitterPanel is null");
                topLayout.sethighlight(Constants.SECTION_SUBMITTER, "pill-danger");
            }

//            investigatorPanel.clearPeople();
            if (document.getInvestigators() != null) {
                List<Person> personList = document.getInvestigators();
//              investigatorPanel.reset(); // XXX Should this also be commented out.  What about merge?

                // Load
                investigatorPanel.addPeople(personList);

                // verify
                boolean hasValidData = false;
                boolean hasInvalidData = false;

                for (int i = 0; i < personList.size(); i++) {
//                    debugLog("current index: " + i);
                    Person p = personList.get(i);
                    investigatorPanel.show(p);
                    if (!investigatorPanel.valid()) {
                        hasInvalidData = true;
                    } else {
                        hasValidData = true;
                    }
                    investigatorPanel.reset();
                }

                if (hasValidData == true && hasInvalidData == true) {
//                    debugLog("warning investigatorPanel valid and invalid");
                    topLayout.sethighlight(Constants.SECTION_INVESTIGATOR, "pill-danger");
                }
                if (hasValidData == true && hasInvalidData == false) {
//                    debugLog("success investigatorPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_INVESTIGATOR);
                    topLayout.removehighlight(Constants.SECTION_INVESTIGATOR, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == true) {
//                    debugLog("danger investigatorPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_INVESTIGATOR, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == false) {
//                    debugLog("danger investigatorPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_INVESTIGATOR, "pill-danger");
                }

                if (personList.size() == 0) {
                    investigatorPanel.setTableVisible(false);
                }
            }

            if (document.getCitation() != null) {
                Citation citation = document.getCitation();
                citationPanel.show(citation);
                if (!citationPanel.valid()) {
//                    debugLog("danger citationPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_CITATION, "pill-danger");
                } else {
//                    debugLog("success citationPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_CITATION);
                    topLayout.removehighlight(Constants.SECTION_CITATION, "pill-danger");
                }
            }

            if (document.getTimeAndLocation() != null) {
                TimeAndLocation timeAndLocation = document.getTimeAndLocation();
                timeAndLocationPanel.show(timeAndLocation);
                if (!timeAndLocationPanel.valid()) {
//                    debugLog("danger timeAndLocationPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_TIMEANDLOCATION, "pill-danger");
                } else {
//                    debugLog("success timeAndLocationPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_TIMEANDLOCATION);
                    topLayout.removehighlight(Constants.SECTION_TIMEANDLOCATION, "pill-danger");
                }
            }

            if (document.getFunding() != null) {
                List<Funding> fundings = document.getFunding();

                // Load
                fundingPanel.addFundings(fundings);

                // verify
                boolean hasValidData = false;
                boolean hasInvalidData = false;
                for (int i = 0; i < fundings.size(); i++) {
                    Funding f = fundings.get(i);
                    fundingPanel.show(f);
                    if (!fundingPanel.valid()) {
                        hasInvalidData = true;
                    } else {
                        hasValidData = true;
                    }
                    fundingPanel.reset();
                }
                if (hasValidData == true && hasInvalidData == true) {
//                    debugLog("warning fundingPanel valid and invalid");
                    topLayout.sethighlight(Constants.SECTION_FUNDING, "pill-danger");
                }
                if (hasValidData == true && hasInvalidData == false) {
//                    debugLog("success fundingPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_FUNDING);
                    topLayout.removehighlight(Constants.SECTION_FUNDING, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == true) {
//                    debugLog("danger fundingPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_FUNDING, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == false) {
//                    debugLog("danger fundingPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_FUNDING, "pill-danger");
                }

                if (fundings.size() == 0) {
                    fundingPanel.setTableVisible(false);
                    topLayout.uncheck(Constants.SECTION_FUNDING);
                    topLayout.removehighlight(Constants.SECTION_FUNDING, "pill-danger");
                }
            }

            if (document.getPlatforms() != null) {
                List<Platform> platforms = document.getPlatforms();

                // Load
                platformPanel.addPlatforms(platforms);

                // verify
                boolean hasValidData = false;
                boolean hasInvalidData = false;
                for (int i = 0; i < platforms.size(); i++) {
                    Platform p = platforms.get(i);
                    platformPanel.show(p);
                    if (!platformPanel.valid()) {
                        hasInvalidData = true;
                    } else {
                        hasValidData = true;
                    }
                    platformPanel.reset();
                }
                if (hasValidData == true && hasInvalidData == true) {
//                    debugLog("warning platformPanel valid and invalid");
                    topLayout.sethighlight(Constants.SECTION_PLATFORMS, "pill-danger");
                }
                if (hasValidData == true && hasInvalidData == false) {
//                    debugLog("success platformPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_PLATFORMS);
                    topLayout.removehighlight(Constants.SECTION_PLATFORMS, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == true) {
//                    debugLog("danger platformPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_PLATFORMS, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == false) {
//                    debugLog("danger platformPanel has no data");
                    topLayout.sethighlight(Constants.SECTION_PLATFORMS, "pill-danger");
                }

                if (platforms.size() == 0) {
                    platformPanel.setTableVisible(false);
                    topLayout.uncheck(Constants.SECTION_PLATFORMS);
                    topLayout.removehighlight(Constants.SECTION_PLATFORMS, "pill-danger");
                }
            }

            if (document.getDic() != null) {
                Variable dic = document.getDic();
                dicPanel.show(dic);
                if (!dicPanel.valid()) {
//                    debugLog("danger dicPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_DIC, "pill-danger");
                } else {
//                    debugLog("success dicPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_DIC);
                    topLayout.removehighlight(Constants.SECTION_DIC, "pill-danger");
                }
            }
            if (document.getTa() != null) {
                Variable ta = document.getTa();
                taPanel.show(ta);
                if (!phPanel.valid()) {
//                    debugLog("danger taPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_TA, "pill-danger");
                } else {
//                    debugLog("success taPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_TA);
                    topLayout.removehighlight(Constants.SECTION_TA, "pill-danger");
                }
            }
            if (document.getPh() != null) {
                Variable ph = document.getPh();
                phPanel.show(ph);
                if (!phPanel.valid()) {
//                    debugLog("danger phPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_PH, "pill-danger");
                } else {
//                    debugLog("success phPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_PH);
                    topLayout.removehighlight(Constants.SECTION_PH, "pill-danger");
                }
            }
            if (document.getPco2a() != null) {
//                common.abbreviation.setText("pCO2a");
//                common.fullVariableName.setText("pco2 (fco2) autonomous");
                Variable pco2a = document.getPco2a();
                pco2aPanel.show(pco2a);
                if (!pco2aPanel.valid()) {
//                    debugLog("danger pco2aPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_PCO2A, "pill-danger");
                } else {
//                    debugLog("success pco2aPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_PCO2A);
                    topLayout.removehighlight(Constants.SECTION_PCO2A, "pill-danger");
                }
            }
            if (document.getPco2d() != null) {
                Variable pco2d = document.getPco2d();
                pco2dPanel.show(pco2d);
                if (!pco2dPanel.valid()) {
//                    debugLog("danger pco2dPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_PCO2D, "pill-danger");
                } else {
//                    debugLog("success pco2dPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_PCO2D);
                    topLayout.removehighlight(Constants.SECTION_PCO2D, "pill-danger");
                }
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
                List<Variable> variablesList = document.getVariables();
                debugLog("Overwrite merge");

                if (originalDocument.getVariables() != null) {
                    List<Variable> oVariablesList = originalDocument.getVariables();

                    Map<String, Variable> matchMap = new HashMap<String, Variable>();
                    for (Variable ovl : oVariablesList) {

                        // has abbreviation and units then use as key
                        if ((ovl.getAbbreviation() != null && !ovl.getAbbreviation().isEmpty())) {
//                            debugLog("**has abbreviation ovl (" + ovl.getAbbreviation() + ")");
                            if (ovl.getUnits() != null && !ovl.getUnits().isEmpty()) {
//                                debugLog("**has units ovl (" + ovl.getUnits() + ")");
                                matchMap.put(createVariableKeyIndex(ovl.getAbbreviation(), ovl.getUnits()), ovl);
                            }
                            else {
//                                debugLog("**has NO units ovl adding just abbrevation to hash");
                                matchMap.put(ovl.getAbbreviation().toLowerCase(), ovl);
                            }
                        }

                    }
//                    matchMap.forEach((key, value) -> debugLog("ovlKEY: " + key + " = ovlVALUE: " + value));

                    Iterator<Variable> variableIterator = variablesList.iterator();
                    while (variableIterator.hasNext()) {
                        Variable v = variableIterator.next();

                        // original list contains this Variable
                        if (oVariablesList.contains(v)) {
//                            debugLog("*** oVariableList contains the var v for " + v.getAbbreviation());
                            variableIterator.remove();  // is same; remove from this variableslist
                            continue;
                        }

                        if (v.getAbbreviation() != null && !v.getAbbreviation().isEmpty()) {
                            String combinedKey = null;
                            String abbrevationKey = null;

                            if (v.getUnits() != null && !v.getUnits().isEmpty()) {
//                                debugLog("*> merge looking for has units " + v.getUnits());
                                combinedKey = createVariableKeyIndex(v.getAbbreviation(), v.getUnits());
                                abbrevationKey = v.getAbbreviation().toLowerCase();

                                // has same units, merge
                                if (matchMap.containsKey(combinedKey)) {
//                                    debugLog("**> matched with combinedKey: " + combinedKey);
                                    Variable o = matchMap.get(combinedKey);
                                    oVariablesList.remove(o);
//                                    debugLog("***> DO remove o from oVarList 1");
                                }

                                // has same abrevation, check og var units
//                                debugLog("**> Check with abbrevationKey: " + abbrevationKey);
                                if (matchMap.containsKey(abbrevationKey)) {
//                                    debugLog("**> matched with abbrevationKey: " + abbrevationKey);
                                    Variable o = matchMap.get(abbrevationKey);

                                    if (o.getUnits() != null && !o.getUnits().isEmpty()) {
                                        debugLog("**> o has units (" + o.getUnits() + ")");
                                        debugLog("***> DO nothing");
                                    }
                                    else {
//                                        debugLog("**> o has no units (" + o.getUnits() + ")");
                                        oVariablesList.remove(o);
//                                        debugLog("***> DO remove o from oVarList 2");
                                    }

                                }
                            }
                            else {
//                                debugLog("****> merge looking for has NO units (" + v.getAbbreviation() + ")");

                                // has no units and has no units is a match
                                String variableKey = v.getAbbreviation().toLowerCase();
                                if (matchMap.containsKey(variableKey)) {
//                                    debugLog("***> matched withOUT units: " + variableKey);
                                    Variable o = matchMap.get(abbrevationKey);
                                    oVariablesList.remove(o);
//                                    debugLog("***> DO remove o from oVarList 3");
                                }

                                // TODO has no units and has units replace or append?

                            }

                        }
                    }
                }

                // validate
                // Load
                genericVariablePanel.addVariables(variablesList);

                // verify
                boolean hasValidData = false;
                boolean hasInvalidData = false;
                for (int i = 0; i < variablesList.size(); i++) {
                    Variable v = variablesList.get(i);
                    genericVariablePanel.show(v);
                    if (!genericVariablePanel.valid()) {
                        hasInvalidData = true;
                    } else {
                        hasValidData = true;
                    }
                    genericVariablePanel.reset();
                }

                if (hasValidData == true && hasInvalidData == true) {
//                    debugLog("warning genericVariablePanel valid and invalid");
                    topLayout.sethighlight(Constants.SECTION_GENERIC, "pill-danger");
                }
                if (hasValidData == true && hasInvalidData == false) {
//                    debugLog("success genericVariablePanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_GENERIC);
                    topLayout.removehighlight(Constants.SECTION_GENERIC, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == true) {
//                    debugLog("danger genericVariablePanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_GENERIC, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == false) {
//                    debugLog("danger genericVariablePanel has no data");
                    topLayout.sethighlight(Constants.SECTION_GENERIC, "pill-danger");
                }

                if (variablesList.size() == 0) {
                    genericVariablePanel.setTableVisible(false);
                    topLayout.uncheck(Constants.SECTION_GENERIC);
                    topLayout.removehighlight(Constants.SECTION_GENERIC, "pill-danger");
                }
            }

//            if (document.getVariables() != null) {
//                List<Variable> variablesList = document.getVariables();
//                debugLog("Overwrite merge");
//
//                if (originalDocument.getVariables() != null) {
//                    List<Variable> oVariablesList = originalDocument.getVariables();
//
//                    Map<String, Variable> matchMap = new HashMap<String, Variable>();
//                    for (Variable ovl : oVariablesList) {
//
//                        // has abbreviation and units then use as key
////                        if ((ovl.getAbbreviation() != null && !ovl.getAbbreviation().isEmpty())) {
////                            debugLog("**abbreviation is not null and is not empty");
////                        }
////                        if ((ovl.getUnits() != null && !ovl.getUnits().isEmpty())) {
////                            debugLog("**has units is not null and is not empty");
////                        }
//
//                        if ((ovl.getAbbreviation() != null && !ovl.getAbbreviation().isEmpty())
//                                && (ovl.getUnits() != null && !ovl.getUnits().isEmpty())) {
////                            debugLog("has abbreviation + units: " + ovl.getAbbreviation() + " + " + ovl.getUnits());
////                            String matchKey = createVariableKeyIndex(ovl.getAbbreviation(), ovl.getUnits());
////                            debugLog("matchKey: " + matchKey);
//
//                            String variableKey = createVariableKeyIndex(ovl.getAbbreviation(), ovl.getUnits());
//                            boolean isKeyPresent = matchMap.containsKey(variableKey);
//                            if (isKeyPresent) {
//                                debugLog("** " + variableKey + " already exists");
//                                Variable previous = matchMap.get(variableKey);
//                                if (previous.equals(ovl)) {
//                                    debugLog("Variable @" + variableKey + " is equal");
//                                }
//                                else {
//                                    debugLog("Variable @" + variableKey + " is NOT equal");
//                                }
//                            }
//                            else {
//                                matchMap.put(createVariableKeyIndex(ovl.getAbbreviation(), ovl.getUnits()), ovl);
//                            }
//
//                        }
////                        else {
////                            debugLog("missing abbreviation (" + ovl.getAbbreviation() + ") and/or units (" + ovl.getUnits() + ")");
////                        }
//
//                    }
//                    matchMap.forEach((key, value) -> debugLog("nKEY: " + key + " = nVALUE: " + value));
//
//                    Iterator<Variable> variableIterator = variablesList.iterator();
//                    while (variableIterator.hasNext()) {
//                        Variable v = variableIterator.next();
//
//                        // original list contains this Variable
//                        if (oVariablesList.contains(v)) {
//                            variableIterator.remove();  // is same; remove from this variableslist
//                            continue;
//                        }
//
//                        String variableKey = null;
//                        if ((v.getAbbreviation() != null && !v.getAbbreviation().isEmpty())
//                                && (v.getUnits() != null && !v.getUnits().isEmpty())) {
//                            variableKey = createVariableKeyIndex(v.getAbbreviation(), v.getUnits());
////                            debugLog("(jsonDoc) variableKey is " + variableKey);
//
//                            // variableKey exists in the map from the original variablelist, then remove from orignal variablelist
//                            if (matchMap.containsKey(variableKey)) {
//                                Variable o = matchMap.get(variableKey);
//                                oVariablesList.remove(o);
//                            }
//                        }
//                    }
//                }
//
//                // validate
//                // Load
//                genericVariablePanel.addVariables(variablesList);
//
//                // verify
//                boolean hasValidData = false;
//                boolean hasInvalidData = false;
//                for (int i = 0; i < variablesList.size(); i++) {
//                    Variable v = variablesList.get(i);
//                    genericVariablePanel.show(v);
//                    if (!genericVariablePanel.valid()) {
//                        hasInvalidData = true;
//                    } else {
//                        hasValidData = true;
//                    }
//                    genericVariablePanel.reset();
//                }
//
//                if (hasValidData == true && hasInvalidData == true) {
////                    debugLog("warning genericVariablePanel valid and invalid");
//                    topLayout.sethighlight(Constants.SECTION_GENERIC, "pill-danger");
//                }
//                if (hasValidData == true && hasInvalidData == false) {
////                    debugLog("success genericVariablePanel has only valid data");
//                    topLayout.setChecked(Constants.SECTION_GENERIC);
//                    topLayout.removehighlight(Constants.SECTION_GENERIC, "pill-danger");
//                }
//                if (hasValidData == false && hasInvalidData == true) {
////                    debugLog("danger genericVariablePanel has no valid data");
//                    topLayout.sethighlight(Constants.SECTION_GENERIC, "pill-danger");
//                }
//                if (hasValidData == false && hasInvalidData == false) {
////                    debugLog("danger genericVariablePanel has no data");
//                    topLayout.sethighlight(Constants.SECTION_GENERIC, "pill-danger");
//                }
//
//                if (variablesList.size() == 0) {
//                    genericVariablePanel.setTableVisible(false);
//                    topLayout.uncheck(Constants.SECTION_GENERIC);
//                    topLayout.removehighlight(Constants.SECTION_GENERIC, "pill-danger");
//                }
//            }

            if (!clearFirst) {
                _currentDocument = getDocument();
                debugLog("set _currentDocument: " + _currentDocument);
            }
        } catch (Exception e) {
            Window.alert("File not processed. e=" + e.getLocalizedMessage());
            logToConsole("File not processed. e="+e.getLocalizedMessage());
            logToConsole(jsonString);
        }
        topLayout.resetFileForm();
        debugLog("end of loadJsonDocument()");
    }

    // preserve data merge
    private void preserveLoadJsonDocument(String jsonString, boolean clearFirst, boolean updateIds) {
        try {
            if (clearFirst) {
                startOver(updateIds);
            }

            Document document = documentFromJson(jsonString);
            if (updateIds) {
                loadDocumentElements(document);
            }
            debugLog("OAME: Setting document to: " + document);

            // get document thats currently populated
            Document originalDocument = getDocument();

            if (document.getDataSubmitter() != null) {
                Person dataSubmitter = document.getDataSubmitter();

                if (originalDocument.getDataSubmitter() != null) {
                    Person initalDataSubmitter = originalDocument.getDataSubmitter();

                    if (initalDataSubmitter.getAddress1() != null && !initalDataSubmitter.getAddress1().isEmpty()) {
                        dataSubmitter.setAddress1(initalDataSubmitter.getAddress1());
                    }
                    if (initalDataSubmitter.getAddress2() != null && !initalDataSubmitter.getAddress2().isEmpty()) {
                        dataSubmitter.setAddress2(initalDataSubmitter.getAddress2());
                    }
                    if (initalDataSubmitter.getEmail() != null && !initalDataSubmitter.getEmail().isEmpty()) {
                        dataSubmitter.setEmail(initalDataSubmitter.getEmail());
                    }
                    if (initalDataSubmitter.getFirstName() != null && !initalDataSubmitter.getFirstName().isEmpty()) {
                        dataSubmitter.setFirstName(initalDataSubmitter.getFirstName());
                    }
                    if (initalDataSubmitter.getInstitution() != null && !initalDataSubmitter.getInstitution().isEmpty()) {
                        dataSubmitter.setInstitution(initalDataSubmitter.getInstitution());
                    }
                    if (initalDataSubmitter.getLastName() != null && !initalDataSubmitter.getLastName().isEmpty()) {
                        dataSubmitter.setLastName(initalDataSubmitter.getLastName());
                    }
                    if (initalDataSubmitter.getMi() != null && !initalDataSubmitter.getMi().isEmpty()) {
                        dataSubmitter.setMi(initalDataSubmitter.getMi());
                    }
                    if (initalDataSubmitter.getRid() != null && !initalDataSubmitter.getRid().isEmpty()) {
                        dataSubmitter.setRid(initalDataSubmitter.getRid());
                    }
                    if (initalDataSubmitter.getTelephone() != null && !initalDataSubmitter.getTelephone().isEmpty()) {
                        dataSubmitter.setTelephone(initalDataSubmitter.getTelephone());
                    }
                    if (initalDataSubmitter.getExtension() != null && !initalDataSubmitter.getExtension().isEmpty()) {
                        dataSubmitter.setExtension(initalDataSubmitter.getExtension());
                    }
                    if (initalDataSubmitter.getCity() != null && !initalDataSubmitter.getCity().isEmpty()) {
                        dataSubmitter.setCity(initalDataSubmitter.getCity());
                    }
                    if (initalDataSubmitter.getState() != null && !initalDataSubmitter.getState().isEmpty()) {
                        dataSubmitter.setState(initalDataSubmitter.getState());
                    }
                    if (initalDataSubmitter.getZip() != null && !initalDataSubmitter.getZip().isEmpty()) {
                        dataSubmitter.setZip(initalDataSubmitter.getZip());
                    }
                    if (initalDataSubmitter.getCountry() != null && !initalDataSubmitter.getCountry().isEmpty()) {
                        dataSubmitter.setCountry(initalDataSubmitter.getCountry());
                    }
                }

                submitterPanel.show(dataSubmitter);
                if (!submitterPanel.valid()) {
//                    debugLog("danger submitterPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_SUBMITTER, "pill-danger");
                } else {
//                    debugLog("success submitterPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_SUBMITTER);
                    topLayout.removehighlight(Constants.SECTION_SUBMITTER, "pill-danger");
                    // hasValidDataSumitter = true;
                }
            }
            if (document.getDataSubmitter() == null) {
//                debugLog("submitterPanel is null");
                topLayout.sethighlight(Constants.SECTION_SUBMITTER, "pill-danger");
            }

//            investigatorPanel.clearPeople();
            if (document.getInvestigators() != null) {
                List<Person> personList = document.getInvestigators();
//              investigatorPanel.reset(); // XXX Should this also be commented out.  What about merge?
                // Load
                investigatorPanel.addPeople(personList);

                // verify
                boolean hasValidData = false;
                boolean hasInvalidData = false;
//                int initalPageTableSize = investigatorPanel.getPageTableSize();
//                investigatorPanel.setPageTableSize(personList.size());
                for (int i = 0; i < personList.size(); i++) {
                    debugLog("current index: " + i);
                    Person p = personList.get(i);
                    investigatorPanel.show(p);
                    if (!investigatorPanel.valid()) {
                        hasInvalidData = true;
                    } else {
                        hasValidData = true;
                    }
                    investigatorPanel.reset();
                }

//                debugLog("In loadJsonDocument getInvestigators: ");
//                debugLog("hasValidData: " + hasValidData);
//                debugLog("hasInvalidData: " + hasInvalidData);
                if (hasValidData == true && hasInvalidData == true) {
//                    debugLog("warning investigatorPanel valid and invalid");
                    topLayout.sethighlight(Constants.SECTION_INVESTIGATOR, "pill-danger");
                }
                if (hasValidData == true && hasInvalidData == false) {
//                    debugLog("success investigatorPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_INVESTIGATOR);
                    topLayout.removehighlight(Constants.SECTION_INVESTIGATOR, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == true) {
//                    debugLog("danger investigatorPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_INVESTIGATOR, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == false) {
//                    debugLog("danger investigatorPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_INVESTIGATOR, "pill-danger");
                }

//                investigatorPanel.setPageTableSize(initalPageTableSize);
                if (personList.size() == 0) {
                    investigatorPanel.setTableVisible(false);
                }
            }

            if (document.getCitation() != null) {
                Citation citation = document.getCitation();

                if (originalDocument.getCitation() != null) {
                    Citation initalCitation = originalDocument.getCitation();

                    if (initalCitation.getDatasetAbstract() != null && !initalCitation.getDatasetAbstract().isEmpty()) {
                        citation.setDatasetAbstract(initalCitation.getDatasetAbstract());
                    }
                    if (initalCitation.getUseLimitation() != null && !initalCitation.getUseLimitation().isEmpty()) {
                        citation.setUseLimitation(initalCitation.getUseLimitation());
                    }
                    if (initalCitation.getPurpose() != null && !initalCitation.getPurpose().isEmpty()) {
                        citation.setPurpose(initalCitation.getPurpose());
                    }
                    if (initalCitation.getResearchProjects() != null && !initalCitation.getResearchProjects().isEmpty()) {
                        citation.setResearchProjects(initalCitation.getResearchProjects());
                    }
                    if (initalCitation.getTitle() != null && !initalCitation.getTitle().isEmpty()) {
                        citation.setTitle(initalCitation.getTitle());
                    }
                    if (initalCitation.getExpocode() != null && !initalCitation.getExpocode().isEmpty()) {
                        citation.setExpocode(initalCitation.getExpocode());
                    }
                    if (initalCitation.getCruiseId() != null && !initalCitation.getCruiseId().isEmpty()) {
                        citation.setCruiseId(initalCitation.getCruiseId());
                    }
                    if (initalCitation.getSection() != null && !initalCitation.getSection().isEmpty()) {
                        citation.setSection(initalCitation.getSection());
                    }
                    if (initalCitation.getCitationAuthorList() != null && !initalCitation.getCitationAuthorList().isEmpty()) {
                        citation.setCitationAuthorList(initalCitation.getCitationAuthorList());
                    }
                    if (initalCitation.getScientificReferences() != null && !initalCitation.getScientificReferences().isEmpty()) {
                        citation.setScientificReferences(initalCitation.getScientificReferences());
                    }
                    if (initalCitation.getSupplementalInformation() != null && !initalCitation.getSupplementalInformation().isEmpty()) {
                        citation.setSupplementalInformation(initalCitation.getSupplementalInformation());
                    }
                }

                citationPanel.show(citation);
                if (!citationPanel.valid()) {
//                    debugLog("danger citationPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_CITATION, "pill-danger");
                } else {
//                    debugLog("success citationPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_CITATION);
                    topLayout.removehighlight(Constants.SECTION_CITATION, "pill-danger");
                }
            }

            if (document.getTimeAndLocation() != null) {
                TimeAndLocation timeAndLocation = document.getTimeAndLocation();

                if (originalDocument.getCitation() != null) {
                    TimeAndLocation initalTimeAndLocation = originalDocument.getTimeAndLocation();

                    if (initalTimeAndLocation.getStartDate() != null && !initalTimeAndLocation.getStartDate().isEmpty()) {
                        timeAndLocation.setStartDate(initalTimeAndLocation.getStartDate());
                    }
                    if (initalTimeAndLocation.getEndDate() != null && !initalTimeAndLocation.getEndDate().isEmpty()) {
                        timeAndLocation.setEndDate(initalTimeAndLocation.getEndDate());
                    }
                    if (initalTimeAndLocation.getSpatialRef() != null && !initalTimeAndLocation.getSpatialRef().isEmpty()) {
                        timeAndLocation.setSpatialRef(initalTimeAndLocation.getSpatialRef());
                    }
                    if (initalTimeAndLocation.getNorthLat() != null && !initalTimeAndLocation.getNorthLat().isEmpty()) {
                        timeAndLocation.setNorthLat(initalTimeAndLocation.getNorthLat());
                    }
                    if (initalTimeAndLocation.getEastLon() != null && !initalTimeAndLocation.getEastLon().isEmpty()) {
                        timeAndLocation.setEastLon(initalTimeAndLocation.getEastLon());
                    }
                    if (initalTimeAndLocation.getWestLon() != null && !initalTimeAndLocation.getWestLon().isEmpty()) {
                        timeAndLocation.setWestLon(initalTimeAndLocation.getWestLon());
                    }
                    if (initalTimeAndLocation.getSouthLat() != null && !initalTimeAndLocation.getSouthLat().isEmpty()) {
                        timeAndLocation.setSouthLat(initalTimeAndLocation.getSouthLat());
                    }
                    if (initalTimeAndLocation.getGeoNames() != null && !initalTimeAndLocation.getGeoNames().isEmpty()) {
                        timeAndLocation.setGeoNames(initalTimeAndLocation.getGeoNames());
                    }
                    if (initalTimeAndLocation.getOrganismLoc() != null && !initalTimeAndLocation.getOrganismLoc().isEmpty()) {
                        timeAndLocation.setOrganismLoc(initalTimeAndLocation.getOrganismLoc());
                    }
                }

                timeAndLocationPanel.show(timeAndLocation);
                if (!timeAndLocationPanel.valid()) {
//                    debugLog("danger timeAndLocationPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_TIMEANDLOCATION, "pill-danger");
                } else {
//                    debugLog("success timeAndLocationPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_TIMEANDLOCATION);
                    topLayout.removehighlight(Constants.SECTION_TIMEANDLOCATION, "pill-danger");
                }
            }

            if (document.getFunding() != null) {
                List<Funding> fundings = document.getFunding();

                // Load
                fundingPanel.addFundings(fundings);

                // verify
                boolean hasValidData = false;
                boolean hasInvalidData = false;
                for (int i = 0; i < fundings.size(); i++) {
                    Funding f = fundings.get(i);
                    fundingPanel.show(f);
                    if (!fundingPanel.valid()) {
                        hasInvalidData = true;
                    } else {
                        hasValidData = true;
                    }
                    fundingPanel.reset();
                }
                if (hasValidData == true && hasInvalidData == true) {
//                    debugLog("warning fundingPanel valid and invalid");
                    topLayout.sethighlight(Constants.SECTION_FUNDING, "pill-danger");
                }
                if (hasValidData == true && hasInvalidData == false) {
//                    debugLog("success fundingPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_FUNDING);
                    topLayout.removehighlight(Constants.SECTION_FUNDING, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == true) {
//                    debugLog("danger fundingPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_FUNDING, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == false) {
//                    debugLog("danger fundingPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_FUNDING, "pill-danger");
                }

                if (fundings.size() == 0) {
                    fundingPanel.setTableVisible(false);
                    topLayout.uncheck(Constants.SECTION_FUNDING);
                    topLayout.removehighlight(Constants.SECTION_FUNDING, "pill-danger");
                }
            }

            if (document.getPlatforms() != null) {
                List<Platform> platforms = document.getPlatforms();
                // Load
                platformPanel.addPlatforms(platforms);

                // verify
                boolean hasValidData = false;
                boolean hasInvalidData = false;
                for (int i = 0; i < platforms.size(); i++) {
                    Platform p = platforms.get(i);
                    platformPanel.show(p);
                    if (!platformPanel.valid()) {
                        hasInvalidData = true;
                    } else {
                        hasValidData = true;
                    }
                    platformPanel.reset();
                }
                if (hasValidData == true && hasInvalidData == true) {
//                    debugLog("warning platformPanel valid and invalid");
                    topLayout.sethighlight(Constants.SECTION_PLATFORMS, "pill-danger");
                }
                if (hasValidData == true && hasInvalidData == false) {
//                    debugLog("success platformPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_PLATFORMS);
                    topLayout.removehighlight(Constants.SECTION_PLATFORMS, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == true) {
//                    debugLog("danger platformPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_PLATFORMS, "pill-danger");
                }
                if (hasValidData == false && hasInvalidData == false) {
//                    debugLog("danger platformPanel has no data");
                    topLayout.sethighlight(Constants.SECTION_PLATFORMS, "pill-danger");

                }

                if (platforms.size() == 0) {
                    platformPanel.setTableVisible(false);
                    topLayout.uncheck(Constants.SECTION_PLATFORMS);
                    topLayout.removehighlight(Constants.SECTION_PLATFORMS, "pill-danger");
                }
            }

            if (document.getDic() != null) {
                Variable dic = document.getDic();

                if (originalDocument.getDic() != null) {
                    Variable initalDic = originalDocument.getDic();

                    if (initalDic.getStandardizationTechnique() != null && !initalDic.getStandardizationTechnique().isEmpty()) {
                        dic.setStandardizationTechnique(initalDic.getStandardizationTechnique());
                    }
                    if (initalDic.getFreqencyOfStandardization() != null && !initalDic.getFreqencyOfStandardization().isEmpty()) {
                        dic.setFreqencyOfStandardization(initalDic.getFreqencyOfStandardization());
                    }
                    if (initalDic.getCrmManufacture() != null && !initalDic.getCrmManufacture().isEmpty()) {
                        dic.setCrmManufacture(initalDic.getCrmManufacture());
                    }
                    if (initalDic.getBatchNumber() != null && !initalDic.getBatchNumber().isEmpty()) {
                        dic.setBatchNumber(initalDic.getBatchNumber());
                    }
                    if (initalDic.getPoison() != null && !initalDic.getPoison().isEmpty()) {
                        dic.setPoison(initalDic.getPoison());
                    }
                    if (initalDic.getPoisonVolume() != null && !initalDic.getPoisonVolume().isEmpty()) {
                        dic.setPoisonVolume(initalDic.getPoisonVolume());
                    }
                    if (initalDic.getPoisonDescription() != null && !initalDic.getPoisonDescription().isEmpty()) {
                        dic.setPoisonDescription(initalDic.getPoisonDescription());
                    }
                    // commonVariables
                    if (initalDic.getObservationType() != null && !initalDic.getObservationType().isEmpty()) {
                        dic.setObservationType(initalDic.getObservationType());
                    }
                    if (initalDic.getManipulationMethod() != null && !initalDic.getManipulationMethod().isEmpty()) {
                        dic.setManipulationMethod(initalDic.getManipulationMethod());
                    }
                    if (initalDic.getObservationDetail() != null && !initalDic.getObservationDetail().isEmpty()) {
                        dic.setObservationDetail(initalDic.getObservationDetail());
                    }
                    if (initalDic.getUnits() != null && !initalDic.getUnits().isEmpty()) {
                        dic.setUnits(initalDic.getUnits());
                    }
                    if (initalDic.getMeasured() != null && !initalDic.getMeasured().isEmpty()) {
                        dic.setMeasured(initalDic.getMeasured());
                    }
                    if (initalDic.getSamplingInstrument() != null && !initalDic.getSamplingInstrument().isEmpty()) {
                        dic.setSamplingInstrument(initalDic.getSamplingInstrument());
                    }
                    if (initalDic.getAnalyzingInstrument() != null && !initalDic.getAnalyzingInstrument().isEmpty()) {
                        dic.setAnalyzingInstrument(initalDic.getAnalyzingInstrument());
                    }
                    if (initalDic.getDetailedInformation() != null & !initalDic.getDetailedInformation().isEmpty()) {
                        dic.setDetailedInformation(initalDic.getDetailedInformation());
                    }
                    if (initalDic.getFieldReplicate() != null && !initalDic.getFieldReplicate().isEmpty()) {
                        dic.setFieldReplicate(initalDic.getFieldReplicate());
                    }
                    if (initalDic.getUncertainty() != null && !initalDic.getUncertainty().isEmpty()) {
                        dic.setUncertainty(initalDic.getUncertainty());
                    }
                    if (initalDic.getQualityFlag() != null && !initalDic.getQualityFlag().isEmpty()) {
                        dic.setQualityFlag(initalDic.getQualityFlag());
                    }
                    if (initalDic.getResearcherName() != null && !initalDic.getResearcherName().isEmpty()) {
                        dic.setResearcherName(initalDic.getResearcherName());
                    }
                    if (initalDic.getResearcherInstitution() != null && !initalDic.getResearcherInstitution().isEmpty()) {
                        dic.setResearcherInstitution(initalDic.getResearcherInstitution());
                    }
                    if (initalDic.getReferenceMethod() != null && !initalDic.getReferenceMethod().isEmpty()) {
                        dic.setReferenceMethod(initalDic.getReferenceMethod());
                    }
                }

                dicPanel.show(dic);
                if (!dicPanel.valid()) {
//                    debugLog("danger dicPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_DIC, "pill-danger");
                } else {
//                    debugLog("success dicPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_DIC);
                    topLayout.removehighlight(Constants.SECTION_DIC, "pill-danger");
                }
            }

            if (document.getTa() != null) {
                Variable ta = document.getTa();

                if (originalDocument.getTa() != null) {
                    Variable initalTa = originalDocument.getTa();

                    if (initalTa.getStandardizationTechnique() != null && !initalTa.getStandardizationTechnique().isEmpty()) {
                        ta.setStandardizationTechnique(initalTa.getStandardizationTechnique());
                    }
                    if (initalTa.getFreqencyOfStandardization() != null && !initalTa.getFreqencyOfStandardization().isEmpty()) {
                        ta.setFreqencyOfStandardization(initalTa.getFreqencyOfStandardization());
                    }
                    if (initalTa.getCrmManufacture() != null && !initalTa.getCrmManufacture().isEmpty()) {
                        ta.setCrmManufacture(initalTa.getCrmManufacture());
                    }
                    if (initalTa.getBatchNumber() != null && !initalTa.getBatchNumber().isEmpty()) {
                        ta.setBatchNumber(initalTa.getBatchNumber());
                    }
                    if (initalTa.getPoison() != null && !initalTa.getPoison().isEmpty()) {
                        ta.setPoison(initalTa.getPoison());
                    }
                    if (initalTa.getPoisonVolume() != null && !initalTa.getPoisonVolume().isEmpty()) {
                        ta.setPoisonVolume(initalTa.getPoisonVolume());
                    }
                    if (initalTa.getPoisonDescription() != null && !initalTa.getPoisonDescription().isEmpty()) {
                        ta.setPoisonDescription(initalTa.getPoisonDescription());
                    }
                    if (initalTa.getCellType() != null && !initalTa.getCellType().isEmpty()) {
                        ta.setCellType(initalTa.getCellType());
                    }
                    if (initalTa.getCurveFittingMethod() != null && !initalTa.getCurveFittingMethod().isEmpty()) {
                        ta.setCurveFittingMethod(initalTa.getCurveFittingMethod());
                    }
                    if (initalTa.getMagnitudeOfBlankCorrection() != null && !initalTa.getMagnitudeOfBlankCorrection().isEmpty()) {
                        ta.setMagnitudeOfBlankCorrection(initalTa.getMagnitudeOfBlankCorrection());
                    }
                    if (initalTa.getTitrationType() != null && !initalTa.getTitrationType().isEmpty()) {
                        ta.setTitrationType(initalTa.getTitrationType());
                    }
                    // commonVariables
                    if (initalTa.getObservationType() != null && !initalTa.getObservationType().isEmpty()) {
                        ta.setObservationType(initalTa.getObservationType());
                    }
                    if (initalTa.getManipulationMethod() != null && !initalTa.getManipulationMethod().isEmpty()) {
                        ta.setManipulationMethod(initalTa.getManipulationMethod());
                    }
                    if (initalTa.getObservationDetail() != null && !initalTa.getObservationDetail().isEmpty()) {
                        ta.setObservationDetail(initalTa.getObservationDetail());
                    }
                    if (initalTa.getUnits() != null && !initalTa.getUnits().isEmpty()) {
                        ta.setUnits(initalTa.getUnits());
                    }
                    if (initalTa.getMeasured() != null && !initalTa.getMeasured().isEmpty()) {
                        ta.setMeasured(initalTa.getMeasured());
                    }
                    if (initalTa.getSamplingInstrument() != null && !initalTa.getSamplingInstrument().isEmpty()) {
                        ta.setSamplingInstrument(initalTa.getSamplingInstrument());
                    }
                    if (initalTa.getAnalyzingInstrument() != null && !initalTa.getAnalyzingInstrument().isEmpty()) {
                        ta.setAnalyzingInstrument(initalTa.getAnalyzingInstrument());
                    }
                    if (initalTa.getDetailedInformation() != null & !initalTa.getDetailedInformation().isEmpty()) {
                        ta.setDetailedInformation(initalTa.getDetailedInformation());
                    }
                    if (initalTa.getFieldReplicate() != null && !initalTa.getFieldReplicate().isEmpty()) {
                        ta.setFieldReplicate(initalTa.getFieldReplicate());
                    }
                    if (initalTa.getUncertainty() != null && !initalTa.getUncertainty().isEmpty()) {
                        ta.setUncertainty(initalTa.getUncertainty());
                    }
                    if (initalTa.getQualityFlag() != null && !initalTa.getQualityFlag().isEmpty()) {
                        ta.setQualityFlag(initalTa.getQualityFlag());
                    }
                    if (initalTa.getResearcherName() != null && !initalTa.getResearcherName().isEmpty()) {
                        ta.setResearcherName(initalTa.getResearcherName());
                    }
                    if (initalTa.getResearcherInstitution() != null && !initalTa.getResearcherInstitution().isEmpty()) {
                        ta.setResearcherInstitution(initalTa.getResearcherInstitution());
                    }
                    if (initalTa.getReferenceMethod() != null && !initalTa.getReferenceMethod().isEmpty()) {
                        ta.setReferenceMethod(initalTa.getReferenceMethod());
                    }
                }

                taPanel.show(ta);
                if (!phPanel.valid()) {
//                    debugLog("danger taPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_TA, "pill-danger");
                } else {
//                    debugLog("success taPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_TA);
                    topLayout.removehighlight(Constants.SECTION_TA, "pill-danger");
                }
            }
            if (document.getPh() != null) {
                Variable ph = document.getPh();

                if (originalDocument.getPh() != null) {
                    Variable initalPh = originalDocument.getPh();

                    if (initalPh.getStandardizationTechnique() != null && !initalPh.getStandardizationTechnique().isEmpty()) {
                        ph.setStandardizationTechnique(initalPh.getStandardizationTechnique());
                    }
                    if (initalPh.getFreqencyOfStandardization() != null && !initalPh.getFreqencyOfStandardization().isEmpty()) {
                        ph.setFreqencyOfStandardization(initalPh.getFreqencyOfStandardization());
                    }
                    if (initalPh.getPhTemperature() != null && !initalPh.getPhTemperature().isEmpty()) {
                        ph.setPhTemperature(initalPh.getPhTemperature());
                    }
                    if (initalPh.getPhScale() != null && !initalPh.getPhScale().isEmpty()) {
                        ph.setPhScale(initalPh.getPhScale());
                    }
                    if (initalPh.getPhStandards() != null && !initalPh.getPhStandards().isEmpty()) {
                        ph.setPhStandards(initalPh.getPhStandards());
                    }
                    if (initalPh.getTemperatureCorrectionMethod() != null && !initalPh.getTemperatureCorrectionMethod().isEmpty()) {
                        ph.setTemperatureCorrectionMethod(initalPh.getTemperatureCorrectionMethod());
                    }
                    if (initalPh.getTemperatureMeasurement() != null && !initalPh.getTemperatureMeasurement().isEmpty()) {
                        ph.setTemperatureMeasurement(initalPh.getTemperatureMeasurement());
                    }
                    if (initalPh.getTemperatureStandarization() != null && !initalPh.getTemperatureStandarization().isEmpty()) {
                        ph.setTemperatureStandarization(initalPh.getTemperatureCorrectionMethod());
                    }
                    // commonVariables
                    if (initalPh.getObservationType() != null && !initalPh.getObservationType().isEmpty()) {
                        ph.setObservationType(initalPh.getObservationType());
                    }
                    if (initalPh.getManipulationMethod() != null && !initalPh.getManipulationMethod().isEmpty()) {
                        ph.setManipulationMethod(initalPh.getManipulationMethod());
                    }
                    if (initalPh.getObservationDetail() != null && !initalPh.getObservationDetail().isEmpty()) {
                        ph.setObservationDetail(initalPh.getObservationDetail());
                    }
                    if (initalPh.getUnits() != null && !initalPh.getUnits().isEmpty()) {
                        ph.setUnits(initalPh.getUnits());
                    }
                    if (initalPh.getMeasured() != null && !initalPh.getMeasured().isEmpty()) {
                        ph.setMeasured(initalPh.getMeasured());
                    }
                    if (initalPh.getSamplingInstrument() != null && !initalPh.getSamplingInstrument().isEmpty()) {
                        ph.setSamplingInstrument(initalPh.getSamplingInstrument());
                    }
                    if (initalPh.getAnalyzingInstrument() != null && !initalPh.getAnalyzingInstrument().isEmpty()) {
                        ph.setAnalyzingInstrument(initalPh.getAnalyzingInstrument());
                    }
                    if (initalPh.getDetailedInformation() != null & !initalPh.getDetailedInformation().isEmpty()) {
                        ph.setDetailedInformation(initalPh.getDetailedInformation());
                    }
                    if (initalPh.getFieldReplicate() != null && !initalPh.getFieldReplicate().isEmpty()) {
                        ph.setFieldReplicate(initalPh.getFieldReplicate());
                    }
                    if (initalPh.getUncertainty() != null && !initalPh.getUncertainty().isEmpty()) {
                        ph.setUncertainty(initalPh.getUncertainty());
                    }
                    if (initalPh.getQualityFlag() != null && !initalPh.getQualityFlag().isEmpty()) {
                        ph.setQualityFlag(initalPh.getQualityFlag());
                    }
                    if (initalPh.getResearcherName() != null && !initalPh.getResearcherName().isEmpty()) {
                        ph.setResearcherName(initalPh.getResearcherName());
                    }
                    if (initalPh.getResearcherInstitution() != null && !initalPh.getResearcherInstitution().isEmpty()) {
                        ph.setResearcherInstitution(initalPh.getResearcherInstitution());
                    }
                    if (initalPh.getReferenceMethod() != null && !initalPh.getReferenceMethod().isEmpty()) {
                        ph.setReferenceMethod(initalPh.getReferenceMethod());
                    }
                }

                phPanel.show(ph);
                if (!phPanel.valid()) {
//                    debugLog("danger phPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_PH, "pill-danger");
                } else {
//                    debugLog("success phPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_PH);
                    topLayout.removehighlight(Constants.SECTION_PH, "pill-danger");
                }
            }
            if (document.getPco2a() != null) {
//                common.abbreviation.setText("pCO2a");
//                common.fullVariableName.setText("pco2 (fco2) autonomous");
                Variable pco2a = document.getPco2a();
                if (originalDocument.getPco2a() != null) {
                    Variable initalPco2a = originalDocument.getPco2a();

                    if (initalPco2a.getStandardizationTechnique() != null && !initalPco2a.getStandardizationTechnique().isEmpty()) {
                        pco2a.setStandardizationTechnique(initalPco2a.getStandardizationTechnique());
                    }
                    if (initalPco2a.getFreqencyOfStandardization() != null && !initalPco2a.getFreqencyOfStandardization().isEmpty()) {
                        pco2a.setFreqencyOfStandardization(initalPco2a.getFreqencyOfStandardization());
                    }
                    if (initalPco2a.getPco2Temperature() != null && !initalPco2a.getPco2Temperature().isEmpty()) {
                        pco2a.setPco2Temperature(initalPco2a.getPco2Temperature());
                    }
                    if (initalPco2a.getGasConcentration() != null && !initalPco2a.getGasConcentration().isEmpty()) {
                        pco2a.setGasConcentration(initalPco2a.getGasConcentration());
                    }
                    if (initalPco2a.getIntakeDepth() != null && !initalPco2a.getIntakeDepth().isEmpty()) {
                        pco2a.setIntakeDepth(initalPco2a.getIntakeDepth());
                    }
                    if (initalPco2a.getDryingMethod() != null && !initalPco2a.getDryingMethod().isEmpty()) {
                        pco2a.setDryingMethod(initalPco2a.getDryingMethod());
                    }
                    if (initalPco2a.getEquilibratorType() != null && !initalPco2a.getEquilibratorType().isEmpty()) {
                        pco2a.setEquilibratorType(initalPco2a.getEquilibratorType());
                    }
                    if (initalPco2a.getEquilibratorVolume() != null && !initalPco2a.getEquilibratorVolume().isEmpty()) {
                        pco2a.setEquilibratorVolume(initalPco2a.getEquilibratorVolume());
                    }
                    if (initalPco2a.getGasFlowRate() != null && !initalPco2a.getGasFlowRate().isEmpty()) {
                        pco2a.setGasFlowRate(initalPco2a.getGasFlowRate());
                    }
                    if (initalPco2a.getEquilibratorPressureMeasureMethod() != null && !initalPco2a.getEquilibratorPressureMeasureMethod().isEmpty()) {
                        pco2a.setEquilibratorPressureMeasureMethod(initalPco2a.getEquilibratorPressureMeasureMethod());
                    }
                    if (initalPco2a.getEquilibratorTemperatureMeasureMethod() != null && !initalPco2a.getEquilibratorTemperatureMeasureMethod().isEmpty()) {
                        pco2a.setEquilibratorTemperatureMeasureMethod(initalPco2a.getEquilibratorTemperatureMeasureMethod());
                    }
                    if (initalPco2a.getIntakeLocation() != null && !initalPco2a.getIntakeLocation().isEmpty()) {
                        pco2a.setIntakeLocation(initalPco2a.getIntakeLocation());
                    }
                    if (initalPco2a.getStandardGasManufacture() != null && !initalPco2a.getStandardGasManufacture().isEmpty()) {
                        pco2a.setStandardGasManufacture(initalPco2a.getStandardGasManufacture());
                    }
                    if (initalPco2a.getGasDetectorManufacture() != null && !initalPco2a.getGasDetectorManufacture().isEmpty()) {
                        pco2a.setGasDetectorManufacture(initalPco2a.getGasDetectorManufacture());
                    }
                    if (initalPco2a.getGasDetectorModel() != null && !initalPco2a.getGasDetectorModel().isEmpty()) {
                        pco2a.setGasDetectorModel(initalPco2a.getGasDetectorModel());
                    }
                    if (initalPco2a.getGasDectectorResolution() != null && !initalPco2a.getGasDectectorResolution().isEmpty()) {
                        pco2a.setGasDectectorResolution(initalPco2a.getGasDetectorModel());
                    }
                    if (initalPco2a.getTemperatureCorrectionMethod() != null && !initalPco2a.getTemperatureCorrectionMethod().isEmpty()) {
                        pco2a.setTemperatureCorrectionMethod(initalPco2a.getTemperatureCorrectionMethod());
                    }
                    if (initalPco2a.getStandardGasUncertainties() != null && !initalPco2a.getStandardGasUncertainties().isEmpty()) {
                        pco2a.setStandardGasUncertainties(initalPco2a.getStandardGasUncertainties());
                    }
                    if (initalPco2a.getGasDectectorUncertainty() != null && !initalPco2a.getGasDectectorUncertainty().isEmpty()) {
                        pco2a.setGasDectectorUncertainty(initalPco2a.getGasDectectorUncertainty());
                    }
                    if (initalPco2a.getVented() != null && !initalPco2a.getVented().isEmpty()) {
                        pco2a.setVented(initalPco2a.getVented());
                    }
                    if (initalPco2a.getFlowRate() != null && !initalPco2a.getFlowRate().isEmpty()) {
                        pco2a.setFlowRate(initalPco2a.getFlowRate());
                    }
                    if (initalPco2a.getVaporCorrection() != null && !initalPco2a.getVaporCorrection().isEmpty()) {
                        pco2a.setVaporCorrection(initalPco2a.getVaporCorrection());
                    }
                    // commonVariables
                    if (initalPco2a.getObservationType() != null && !initalPco2a.getObservationType().isEmpty()) {
                        pco2a.setObservationType(initalPco2a.getObservationType());
                    }
                    if (initalPco2a.getManipulationMethod() != null && !initalPco2a.getManipulationMethod().isEmpty()) {
                        pco2a.setManipulationMethod(initalPco2a.getManipulationMethod());
                    }
                    if (initalPco2a.getObservationDetail() != null && !initalPco2a.getObservationDetail().isEmpty()) {
                        pco2a.setObservationDetail(initalPco2a.getObservationDetail());
                    }
                    if (initalPco2a.getUnits() != null && !initalPco2a.getUnits().isEmpty()) {
                        pco2a.setUnits(initalPco2a.getUnits());
                    }
                    if (initalPco2a.getMeasured() != null && !initalPco2a.getMeasured().isEmpty()) {
                        pco2a.setMeasured(initalPco2a.getMeasured());
                    }
                    if (initalPco2a.getSamplingInstrument() != null && !initalPco2a.getSamplingInstrument().isEmpty()) {
                        pco2a.setSamplingInstrument(initalPco2a.getSamplingInstrument());
                    }
                    if (initalPco2a.getAnalyzingInstrument() != null && !initalPco2a.getAnalyzingInstrument().isEmpty()) {
                        pco2a.setAnalyzingInstrument(initalPco2a.getAnalyzingInstrument());
                    }
                    if (initalPco2a.getDetailedInformation() != null & !initalPco2a.getDetailedInformation().isEmpty()) {
                        pco2a.setDetailedInformation(initalPco2a.getDetailedInformation());
                    }
                    if (initalPco2a.getFieldReplicate() != null && !initalPco2a.getFieldReplicate().isEmpty()) {
                        pco2a.setFieldReplicate(initalPco2a.getFieldReplicate());
                    }
                    if (initalPco2a.getUncertainty() != null && !initalPco2a.getUncertainty().isEmpty()) {
                        pco2a.setUncertainty(initalPco2a.getUncertainty());
                    }
                    if (initalPco2a.getQualityFlag() != null && !initalPco2a.getQualityFlag().isEmpty()) {
                        pco2a.setQualityFlag(initalPco2a.getQualityFlag());
                    }
                    if (initalPco2a.getResearcherName() != null && !initalPco2a.getResearcherName().isEmpty()) {
                        pco2a.setResearcherName(initalPco2a.getResearcherName());
                    }
                    if (initalPco2a.getResearcherInstitution() != null && !initalPco2a.getResearcherInstitution().isEmpty()) {
                        pco2a.setResearcherInstitution(initalPco2a.getResearcherInstitution());
                    }
                    if (initalPco2a.getReferenceMethod() != null && !initalPco2a.getReferenceMethod().isEmpty()) {
                        pco2a.setReferenceMethod(initalPco2a.getReferenceMethod());
                    }
                }

                pco2aPanel.show(pco2a);
                if (!pco2aPanel.valid()) {
//                    debugLog("danger pco2aPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_PCO2A, "pill-danger");
                } else {
//                    debugLog("success pco2aPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_PCO2A);
                    topLayout.removehighlight(Constants.SECTION_PCO2A, "pill-danger");
                }
            }
            if (document.getPco2d() != null) {
                Variable pco2d = document.getPco2d();

                if (originalDocument.getPco2d() != null) {
                    Variable initalPco2d = originalDocument.getPco2d();

                    if (initalPco2d.getStandardizationTechnique() != null && !initalPco2d.getStandardizationTechnique().isEmpty()) {
                        pco2d.setStandardizationTechnique(initalPco2d.getStandardizationTechnique());
                    }
                    if (initalPco2d.getFreqencyOfStandardization() != null && !initalPco2d.getFreqencyOfStandardization().isEmpty()) {
                        pco2d.setFreqencyOfStandardization(initalPco2d.getFreqencyOfStandardization());
                    }
                    if (initalPco2d.getStorageMethod() != null && !initalPco2d.getStorageMethod().isEmpty()) {
                        pco2d.setStorageMethod(initalPco2d.getStorageMethod());
                    }
                    if (initalPco2d.getPco2Temperature() != null && !initalPco2d.getPco2Temperature().isEmpty()) {
                        pco2d.setPco2Temperature(initalPco2d.getPco2Temperature());
                    }
                    if (initalPco2d.getGasConcentration() != null && !initalPco2d.getGasConcentration().isEmpty()) {
                        pco2d.setGasConcentration(initalPco2d.getGasConcentration());
                    }
                    if (initalPco2d.getHeadspaceVolume() != null && !initalPco2d.getHeadspaceVolume().isEmpty()) {
                        pco2d.setHeadspaceVolume(initalPco2d.getHeadspaceVolume());
                    }
                    if (initalPco2d.getStandardGasManufacture() != null && !initalPco2d.getStandardGasManufacture().isEmpty()) {
                        pco2d.setStandardGasManufacture(initalPco2d.getStandardGasManufacture());
                    }
                    if (initalPco2d.getGasDetectorManufacture() != null && !initalPco2d.getGasDetectorManufacture().isEmpty()) {
                        pco2d.setGasDetectorManufacture(initalPco2d.getGasDetectorManufacture());
                    }
                    if (initalPco2d.getGasDetectorModel() != null && !initalPco2d.getGasDetectorModel().isEmpty()) {
                        pco2d.setGasDetectorModel(initalPco2d.getGasDetectorModel());
                    }
                    if (initalPco2d.getSeawaterVolume() != null && !initalPco2d.getSeawaterVolume().isEmpty()) {
                        pco2d.setSeawaterVolume(initalPco2d.getSeawaterVolume());
                    }
                    if (initalPco2d.getTemperatureCorrectionMethod() != null && !initalPco2d.getTemperatureCorrectionMethod().isEmpty()) {
                        pco2d.setTemperatureCorrectionMethod(initalPco2d.getTemperatureCorrectionMethod());
                    }
                    if (initalPco2d.getTemperatureMeasurement() != null && !initalPco2d.getTemperatureMeasurement().isEmpty()) {
                        pco2d.setTemperatureMeasurement(initalPco2d.getTemperatureMeasurement());
                    }
                    if (initalPco2d.getTemperatureStandarization() != null && !initalPco2d.getTemperatureStandarization().isEmpty()) {
                        pco2d.setTemperatureStandarization(initalPco2d.getTemperatureStandarization());
                    }
                    if (initalPco2d.getStandardGasUncertainties() != null && !initalPco2d.getStandardGasUncertainties().isEmpty()) {
                        pco2d.setStandardGasUncertainties(initalPco2d.getStandardGasUncertainties());
                    }
                    if (initalPco2d.getGasDectectorUncertainty() != null && !initalPco2d.getGasDectectorUncertainty().isEmpty()) {
                        pco2d.setGasDectectorUncertainty(initalPco2d.getGasDectectorUncertainty());
                    }
                    if (initalPco2d.getVaporCorrection() != null && !initalPco2d.getVaporCorrection().isEmpty()) {
                        pco2d.setVaporCorrection(initalPco2d.getVaporCorrection());
                    }
                    // commonVariables
                    if (initalPco2d.getObservationType() != null && !initalPco2d.getObservationType().isEmpty()) {
                        pco2d.setObservationType(initalPco2d.getObservationType());
                    }
                    if (initalPco2d.getManipulationMethod() != null && !initalPco2d.getManipulationMethod().isEmpty()) {
                        pco2d.setManipulationMethod(initalPco2d.getManipulationMethod());
                    }
                    if (initalPco2d.getObservationDetail() != null && !initalPco2d.getObservationDetail().isEmpty()) {
                        pco2d.setObservationDetail(initalPco2d.getObservationDetail());
                    }
                    if (initalPco2d.getUnits() != null && !initalPco2d.getUnits().isEmpty()) {
                        pco2d.setUnits(initalPco2d.getUnits());
                    }
                    if (initalPco2d.getMeasured() != null && !initalPco2d.getMeasured().isEmpty()) {
                        pco2d.setMeasured(initalPco2d.getMeasured());
                    }
                    if (initalPco2d.getSamplingInstrument() != null && !initalPco2d.getSamplingInstrument().isEmpty()) {
                        pco2d.setSamplingInstrument(initalPco2d.getSamplingInstrument());
                    }
                    if (initalPco2d.getAnalyzingInstrument() != null && !initalPco2d.getAnalyzingInstrument().isEmpty()) {
                        pco2d.setAnalyzingInstrument(initalPco2d.getAnalyzingInstrument());
                    }
                    if (initalPco2d.getDetailedInformation() != null & !initalPco2d.getDetailedInformation().isEmpty()) {
                        pco2d.setDetailedInformation(initalPco2d.getDetailedInformation());
                    }
                    if (initalPco2d.getFieldReplicate() != null && !initalPco2d.getFieldReplicate().isEmpty()) {
                        pco2d.setFieldReplicate(initalPco2d.getFieldReplicate());
                    }
                    if (initalPco2d.getUncertainty() != null && !initalPco2d.getUncertainty().isEmpty()) {
                        pco2d.setUncertainty(initalPco2d.getUncertainty());
                    }
                    if (initalPco2d.getQualityFlag() != null && !initalPco2d.getQualityFlag().isEmpty()) {
                        pco2d.setQualityFlag(initalPco2d.getQualityFlag());
                    }
                    if (initalPco2d.getResearcherName() != null && !initalPco2d.getResearcherName().isEmpty()) {
                        pco2d.setResearcherName(initalPco2d.getResearcherName());
                    }
                    if (initalPco2d.getResearcherInstitution() != null && !initalPco2d.getResearcherInstitution().isEmpty()) {
                        pco2d.setResearcherInstitution(initalPco2d.getResearcherInstitution());
                    }
                    if (initalPco2d.getReferenceMethod() != null && !initalPco2d.getReferenceMethod().isEmpty()) {
                        pco2d.setReferenceMethod(initalPco2d.getReferenceMethod());
                    }
                }

                pco2dPanel.show(pco2d);
                if (!pco2dPanel.valid()) {
//                    debugLog("danger pco2dPanel has no valid data");
                    topLayout.sethighlight(Constants.SECTION_PCO2D, "pill-danger");
                } else {
//                    debugLog("success pco2dPanel has only valid data");
                    topLayout.setChecked(Constants.SECTION_PCO2D);
                    topLayout.removehighlight(Constants.SECTION_PCO2D, "pill-danger");
                }
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
                List<Variable> variablesList = document.getVariables();
                debugLog("Preserve merge");

                if (originalDocument.getVariables() != null) {
                    List<Variable> oVariablesList = originalDocument.getVariables();

//                    Map<String, Variable> map = oVariablesList.stream().collect(
//                            Collectors.toMap(
//                                    s -> createVariableKeyIndex(s.getAbbreviation(), s.getUnits()),
//                                    s -> s,
//                                    (variable1, variable2) -> variable1)
//                    );
                    Map<String, Variable> matchMap = new HashMap<String, Variable>();
                    for (Variable ovl : oVariablesList) {

                        // has abbreviation and units then use as key
                        if ((ovl.getAbbreviation() != null && !ovl.getAbbreviation().isEmpty())) {
//                            debugLog("**has abbreviation ovl (" + ovl.getAbbreviation() + ")");
                            if (ovl.getUnits() != null && !ovl.getUnits().isEmpty()) {
//                                debugLog("**has units ovl (" + ovl.getUnits() + ")");
                                matchMap.put(createVariableKeyIndex(ovl.getAbbreviation(), ovl.getUnits()), ovl);
                            }
                            else {
//                                debugLog("**has NO units ovl adding just abbrevation to hash");
                                matchMap.put(ovl.getAbbreviation().toLowerCase(), ovl);
                            }
                        }

                    }
//                    matchMap.forEach((key, value) -> debugLog("ovlKEY: " + key + " = ovlVALUE: " + value));

                    Iterator<Variable> variableIterator = variablesList.iterator();
                    while (variableIterator.hasNext()) {
                        Variable v = variableIterator.next();

                        // original list contains this Variable
                        if (oVariablesList.contains(v)) {
                            variableIterator.remove();  // is same; remove from this variableslist
                            continue;
                        }

//                        String variableKey = createVariableKeyIndex(v.getAbbreviation(), v.getUnits());
                        if (v.getAbbreviation() != null && !v.getAbbreviation().isEmpty()) {
                            String variableKey = v.getAbbreviation().toLowerCase();
                            debugLog("(jsonDoc) variableKey is " + variableKey);

                            // variableKey exists in the hash from the original variablelist, then overwrite empty values
                            if (matchMap.containsKey(variableKey)) {
                                debugLog("containsKey: " + variableKey);
                                Variable o = matchMap.get(variableKey);

                                debugLog("begin process variables");

//                                debugLog("o.getAbbreviation(): " + o.getAbbreviation());
//                                debugLog("v.getAbbreviation(): " + v.getAbbreviation());
//                            if ( (o.getAbbreviation() == null || o.getAbbreviation().isEmpty())
//                                    && (v.getAbbreviation() != null && !v.getAbbreviation().isEmpty()) ) {
//                                o.setAbbreviation(v.getAbbreviation());
//                            }
                                debugLog("o.getFullVariableName(): " + o.getFullVariableName());
                                debugLog("v.getFullVariableName(): " + v.getFullVariableName());
                            if ( (o.getFullVariableName() == null || o.getFullVariableName().isEmpty())
//                                  || o.getFullVariableName().equalsIgnoreCase("unspecified")
//                                  || o.getFullVariableName().equalsIgnoreCase("ignored"))
                                    && (v.getFullVariableName() != null && !v.getFullVariableName().isEmpty()) ) {
                                o.setFullVariableName(v.getFullVariableName());
                            }
                                if ((o.getObservationType() == null || o.getObservationType().isEmpty())
                                        && (v.getObservationType() != null && !v.getObservationType().isEmpty())) {
                                    o.setObservationType(v.getObservationType());
                                }
                                if ((o.getSamplingInstrument() == null || o.getSamplingInstrument().isEmpty())
                                        && (v.getSamplingInstrument() != null && !v.getSamplingInstrument().isEmpty())) {
                                    o.setSamplingInstrument(v.getSamplingInstrument());
                                }
                                if ((o.getAnalyzingInstrument() == null || o.getAnalyzingInstrument().isEmpty())
                                        && (v.getAnalyzingInstrument() != null && !v.getAnalyzingInstrument().isEmpty())) {
                                    o.setAnalyzingInstrument(v.getAnalyzingInstrument());
                                }
                                debugLog("o.getUnits(): " + o.getUnits());
                                debugLog("v.getUnits(): " + v.getUnits());
                            if ( (o.getUnits() == null || o.getUnits().isEmpty() )
                                    && (v.getUnits() != null && !v.getUnits().isEmpty()) ) {
                                o.setUnits(v.getUnits());
                            }
                                if ((o.getObservationDetail() == null || o.getObservationDetail().isEmpty())
                                        && (v.getObservationDetail() != null && !v.getObservationDetail().isEmpty())) {
                                    o.setObservationDetail(v.getObservationDetail());
                                }
                                if ((o.getMeasured() == null || o.getMeasured().isEmpty())
                                        && (v.getMeasured() != null && !v.getMeasured().isEmpty())) {
                                    o.setMeasured(v.getMeasured());
                                }
                                if ((o.getManipulationMethod() == null || o.getManipulationMethod().isEmpty())
                                        && (v.getManipulationMethod() != null && !v.getManipulationMethod().isEmpty())) {
                                    o.setManipulationMethod(v.getManipulationMethod());
                                }
                                if ((o.getCalculationMethod() == null || o.getCalculationMethod().isEmpty())
                                        && (v.getCalculationMethod() != null && !v.getCalculationMethod().isEmpty())) {
                                    o.setCalculationMethod(v.getCalculationMethod());
                                }
                                if ((o.getReferenceMethod() == null || o.getReferenceMethod().isEmpty())
                                        && (v.getReferenceMethod() != null && !v.getReferenceMethod().isEmpty())) {
                                    o.setReferenceMethod(v.getReferenceMethod());
                                }
                                if ((o.getDetailedInformation() == null || o.getDetailedInformation().isEmpty())
                                        && (v.getDetailedInformation() != null && !v.getDetailedInformation().isEmpty())) {
                                    o.setDetailedInformation(v.getDetailedInformation());
                                }
                                if ((o.getUncertainty() == null || o.getUncertainty().isEmpty())
                                        && (v.getUncertainty() != null && !v.getUncertainty().isEmpty())) {
                                    o.setUncertainty(v.getUncertainty());
                                }
                                if ((o.getQualityFlag() == null || o.getQualityFlag().isEmpty())
                                        && (v.getQualityFlag() != null && !v.getQualityFlag().isEmpty())) {
                                    o.setQualityFlag(v.getQualityFlag());
                                }
                                if ((o.getResearcherName() == null || o.getResearcherName().isEmpty())
                                        && (v.getResearcherName() != null && !v.getResearcherName().isEmpty())) {
                                    o.setResearcherName(v.getResearcherName());
                                }
                                if ((o.getResearcherInstitution() == null || o.getResearcherInstitution().isEmpty())
                                        && (v.getResearcherInstitution() != null && !v.getResearcherInstitution().isEmpty())) {
                                    o.setResearcherInstitution(v.getResearcherInstitution());
                                }
                                if ((o.getFieldReplicate() == null || o.getFieldReplicate().isEmpty())
                                        && (v.getFieldReplicate() != null && !v.getFieldReplicate().isEmpty())) {
                                    o.setFieldReplicate(v.getFieldReplicate());
                                }
                                if ((o.getBiologicalSubject() == null || o.getBiologicalSubject().isEmpty())
                                        && (v.getBiologicalSubject() != null && !v.getBiologicalSubject().isEmpty())) {
                                    o.setBiologicalSubject(v.getBiologicalSubject());
                                }
                                if ((o.getDuration() == null || o.getDuration().isEmpty())
                                        && (v.getDuration() != null && !v.getDuration().isEmpty())) {
                                    o.setDuration(v.getDuration());
                                }
                                if ((o.getLifeStage() == null || o.getLifeStage().isEmpty())
                                        && (v.getLifeStage() != null && !v.getLifeStage().isEmpty())) {
                                    o.setLifeStage(v.getLifeStage());
                                }
                                if ((o.getSpeciesIdCode() == null || o.getSpeciesIdCode().isEmpty())
                                        && (v.getSpeciesIdCode() != null && !v.getSpeciesIdCode().isEmpty())) {
                                    o.setSpeciesIdCode(v.getSpeciesIdCode());
                                }

                                debugLog("end process all variables");

                                variableIterator.remove(); // remove from this variablelist
                            }
                        }
                        else {
                            debugLog("!!!has no abbreviation? ");
                        }
                    }
                }

                // validate
                if (variablesList != null && !variablesList.isEmpty()) {
                    // Load
                    genericVariablePanel.addVariables(variablesList);

                    // verify
                    boolean hasValidData = false;
                    boolean hasInvalidData = false;
                    for (int i = 0; i < variablesList.size(); i++) {
                        Variable v = variablesList.get(i);
                        genericVariablePanel.show(v);
                        if (!genericVariablePanel.valid()) {
                            hasInvalidData = true;
                        } else {
                            hasValidData = true;
                        }
                        genericVariablePanel.reset();
                    }

                    if (hasValidData == true && hasInvalidData == true) {
//                    debugLog("warning genericVariablePanel valid and invalid");
                        topLayout.sethighlight(Constants.SECTION_GENERIC, "pill-danger");
                    }
                    if (hasValidData == true && hasInvalidData == false) {
//                    debugLog("success genericVariablePanel has only valid data");
                        topLayout.setChecked(Constants.SECTION_GENERIC);
                        topLayout.removehighlight(Constants.SECTION_GENERIC, "pill-danger");
                    }
                    if (hasValidData == false && hasInvalidData == true) {
//                    debugLog("danger genericVariablePanel has no valid data");
                        topLayout.sethighlight(Constants.SECTION_GENERIC, "pill-danger");
                    }
                    if (hasValidData == false && hasInvalidData == false) {
//                    debugLog("danger genericVariablePanel has no data");
                        topLayout.sethighlight(Constants.SECTION_GENERIC, "pill-danger");
                    }

                    if (variablesList.size() == 0) {
                        genericVariablePanel.setTableVisible(false);
                        topLayout.uncheck(Constants.SECTION_PLATFORMS);
                        topLayout.removehighlight(Constants.SECTION_PLATFORMS, "pill-danger");
                    }
                }
            }

            if (!clearFirst) {
                _currentDocument = getDocument();
            }
        } catch (Exception e) {
            Window.alert("File not processed. e=" + e.getLocalizedMessage());
            logToConsole("File not processed. e=" + e.getLocalizedMessage());
            logToConsole(jsonString);
        }
        topLayout.resetFileForm();
    }

    // builds hash key for Variable type to determine sameness
    public static String createVariableKeyIndex(String abbreviation, String units) {
        StringBuilder sb = new StringBuilder();

        if (abbreviation != null && !abbreviation.isEmpty()) {
            sb.append(abbreviation.toLowerCase());
        }
        if (units != null && !units.isEmpty()) {
            sb.append(units.toLowerCase());
        }
//        debugLog(sb.toString());

        return sb.toString();
    }

}