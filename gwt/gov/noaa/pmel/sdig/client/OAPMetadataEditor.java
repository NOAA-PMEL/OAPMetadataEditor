package gov.noaa.pmel.sdig.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import gov.noaa.pmel.sdig.client.event.NavLink;
import gov.noaa.pmel.sdig.client.event.NavLinkHandler;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.event.SectionSaveHandler;
import gov.noaa.pmel.sdig.client.panels.CitationPanel;
import gov.noaa.pmel.sdig.client.panels.Dic2Panel;
import gov.noaa.pmel.sdig.client.panels.DicPanel;
import gov.noaa.pmel.sdig.client.panels.FundingPanel;
import gov.noaa.pmel.sdig.client.panels.GenericVariablePanel;
import gov.noaa.pmel.sdig.client.panels.InvestigatorPanel;
import gov.noaa.pmel.sdig.client.panels.Pco2a2Panel;
import gov.noaa.pmel.sdig.client.panels.Pco2aPanel;
import gov.noaa.pmel.sdig.client.panels.Pco2d2Panel;
import gov.noaa.pmel.sdig.client.panels.Pco2dPanel;
import gov.noaa.pmel.sdig.client.panels.Ph2Panel;
import gov.noaa.pmel.sdig.client.panels.PhPanel;
import gov.noaa.pmel.sdig.client.panels.PlatformPanel;
import gov.noaa.pmel.sdig.client.panels.DataSubmitterPanel;
import gov.noaa.pmel.sdig.client.panels.Ta2Panel;
import gov.noaa.pmel.sdig.client.panels.TaPanel;
import gov.noaa.pmel.sdig.client.panels.TimeAndLocationPanel;
import gov.noaa.pmel.sdig.shared.bean.Citation;
import gov.noaa.pmel.sdig.shared.bean.Document;
import gov.noaa.pmel.sdig.shared.bean.Funding;
import gov.noaa.pmel.sdig.shared.bean.Person;
import gov.noaa.pmel.sdig.shared.bean.Platform;
import gov.noaa.pmel.sdig.shared.bean.TimeAndLocation;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.fusesource.restygwt.client.JsonEncoderDecoder;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.RestServiceProxy;
import org.fusesource.restygwt.client.TextCallback;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.base.form.AbstractForm;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import javax.ws.rs.POST;
import java.util.List;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class OAPMetadataEditor implements EntryPoint {

    boolean saved = false;

    public interface SaveDocumentService extends RestService {
        @POST
        public void save(Document document, TextCallback textCallback);
    }
    public interface DocumentCodec extends JsonEncoderDecoder<Document> {}

    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network " + "connection and try again.";

    Resource saveDocumentResource = new Resource(Constants.saveDocument);
    SaveDocumentService saveDocumentService = GWT.create(SaveDocumentService.class);

    DocumentCodec codec = GWT.create(DocumentCodec.class);

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    Person dataSubmitter = null;

    InvestigatorPanel investigatorPanel = new InvestigatorPanel();
    DataSubmitterPanel submitterPanel = new DataSubmitterPanel();

    CitationPanel citationPanel = new CitationPanel();
    Citation citation = null;

    TimeAndLocationPanel timeAndLocationPanel = new TimeAndLocationPanel();
    TimeAndLocation timeAndLocation = null;

    FundingPanel fundingPanel = new FundingPanel();
    Funding funding = null;

    PlatformPanel platformPanel = new PlatformPanel();

    DicPanel dicPanel = new DicPanel();
    Variable dic = null;

    Dic2Panel dic2Panel = new Dic2Panel();

    TaPanel taPanel = new TaPanel();
    Variable ta = null;

    Ta2Panel ta2Panel = new Ta2Panel();

    PhPanel phPanel = new PhPanel();
    Variable ph = null;

    Ph2Panel ph2Panel = new Ph2Panel();

    Pco2aPanel pco2aPanel = new Pco2aPanel();
    Variable pco2a = null;

    Pco2a2Panel pco2a2Panel = new Pco2a2Panel();

    Pco2dPanel pco2dPanel = new Pco2dPanel();
    Variable pco2d = null;

    Pco2d2Panel pco2d2Panel = new Pco2d2Panel();

    GenericVariablePanel genericVariablePanel = new GenericVariablePanel();

    // Save XML dialog, should be its own widget.
    Modal modal = new Modal();
    ModalHeader modalHeader = new ModalHeader();
    ModalBody modalBody = new ModalBody();
    Button save = new Button("Save");
    String currentIndex;

    final DashboardLayout topLayout = new DashboardLayout();

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        RootPanel.get().add(topLayout);
        topLayout.addUploadSuccess(completeHandler);
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
                    if ( b.getText().equals("Start Over") ) {
                        // Reset containers for all information being collected to null.
                        dataSubmitter = null;
                        investigatorPanel.clearPeople();
                        citation = null;
                        timeAndLocation = null;
                        funding = null;
                        platformPanel.clearPlatforms();
                        dic = null;
                        ta = null;
                        ph = null;
                        pco2a = null;
                        pco2d = null;
                        genericVariablePanel.clearVariables();

                        // Reset all forms
                        submitterPanel.reset();
                        investigatorPanel.reset();
                        citationPanel.reset();
                        fundingPanel.reset();
                        platformPanel.reset();
                        dicPanel.reset();
                        dic2Panel.reset();
                        taPanel.reset();
                        ta2Panel.reset();
                        phPanel.reset();
                        ph2Panel.reset();
                        pco2aPanel.reset();
                        pco2a2Panel.reset();
                        pco2dPanel.reset();
                        pco2d2Panel.reset();
                        genericVariablePanel.reset();

                    }
                }
            }
        });
        eventBus.addHandler(SectionSave.TYPE, new SectionSaveHandler() {
            @Override
            public void onSectionSave(SectionSave event) {
                String type = event.getType();
                saved = false;
                if (type.equals(Constants.SECTION_INVESTIGATOR)) {
                    // List is in the celltable data provider in the layout. Nothing to do here.
                } else if ( type.equals(Constants.SECTION_SUBMITTER) ) {
                    dataSubmitter = (Person) event.getSectionContents();
                    topLayout.setMain(investigatorPanel);
                    topLayout.setActive(Constants.SECTION_INVESTIGATOR);
                } else if ( type.equals(Constants.SECTION_CITATION) ) {
                    citation = (Citation) event.getSectionContents();
                    if ( timeAndLocation != null ) {
                        timeAndLocationPanel.show(timeAndLocation);
                    }
                    topLayout.setMain(timeAndLocationPanel);
                    topLayout.setActive(Constants.SECTION_TIMEANDLOCATION);
                } else if ( type.equals(Constants.SECTION_TIMEANDLOCATION) ) {
                    timeAndLocation = (TimeAndLocation) event.getSectionContents();
                    if ( funding != null ) {
                        fundingPanel.show(funding);
                    }
                    topLayout.setMain(fundingPanel);
                    topLayout.setActive(Constants.SECTION_FUNDING);
                } else if ( type.equals(Constants.SECTION_FUNDING) ) {
                    funding = (Funding) event.getSectionContents();
                    topLayout.setMain(platformPanel);
                    topLayout.setActive(Constants.SECTION_PLATFORMS);
                } else if ( type.equals(Constants.SECTION_PLATFORMS) ) {
                    // List kept in the panel. Nothing to do here.
                } else if ( type.equals(Constants.SECTION_DIC) ) {
                    if ( dic == null ) {
                        dic = (Variable) event.getSectionContents();
                    } else {
                        dicPanel.fill(dic);
                    }
                    topLayout.setMain(dic2Panel);
                    topLayout.setActive(Constants.SECTION_DIC2);
                } else if ( type.equals(Constants.SECTION_DIC2) ) {
                    if ( dic == null ) {
                        dic = new Variable();;
                    }
                    dic2Panel.fill(dic);
                    topLayout.setMain(taPanel);
                    topLayout.setActive(Constants.SECTION_TA);
                } else if ( type.equals(Constants.SECTION_TA) ) {
                    if ( ta == null ) {
                        ta = (Variable) event.getSectionContents();
                    } else {
                        taPanel.fill(ta);
                    }
                    topLayout.setMain(ta2Panel);
                    topLayout.setActive(Constants.SECTION_TA2);
                } else if ( type.equals(Constants.SECTION_TA2) ) {
                    if ( ta == null ) {
                        ta = new Variable();
                    }
                    ta2Panel.fill(ta);
                    topLayout.setMain(phPanel);
                    topLayout.setActive(Constants.SECTION_PH);
                } else if ( type.equals(Constants.SECTION_PH) ) {
                    if ( ph == null ) {
                        ph = (Variable) event.getSectionContents();
                    } else {
                        phPanel.fillCommonVariable(ph);
                    }
                    topLayout.setMain(ph2Panel);
                    topLayout.setActive(Constants.SECTION_PH2);
                } else if ( type.equals(Constants.SECTION_PH2) ) {
                    if ( ph == null ) {
                        ph = new Variable();
                    }
                    ph2Panel.fill(ph);
                    topLayout.setMain(pco2aPanel);
                    topLayout.setActive(Constants.SECTION_PCO2A);
                } else if ( type.equals(Constants.SECTION_PCO2A) ) {
                    if ( pco2a == null ) {
                        pco2a = (Variable) event.getSectionContents();
                    } else {
                        pco2aPanel.fillCommonVariable(pco2a);
                    }
                    topLayout.setMain(pco2a2Panel);
                    topLayout.setActive(Constants.SECTION_PCO2A2);
                }
                else if ( type.equals(Constants.SECTION_PCO2A2) ) {
                    if ( pco2a == null ) {
                        pco2a = new Variable();
                    }
                    pco2a2Panel.fill(pco2a);
                    topLayout.setMain(pco2dPanel);
                    topLayout.setActive(Constants.SECTION_PCO2D);
                } else if ( type.equals(Constants.SECTION_PCO2D) ) {
                    if ( pco2d == null ) {
                        pco2d = (Variable) event.getSectionContents();
                    } else {
                        pco2dPanel.fillCommonVariable(pco2d);
                    }
                    topLayout.setMain(pco2d2Panel);
                    topLayout.setActive(Constants.SECTION_PCO2D2);
                }
                else if ( type.equals(Constants.SECTION_PCO2D2) ) {
                    if ( pco2d == null ) {
                        pco2d = new Variable();
                    }
                    pco2d2Panel.fill(pco2d);
                    topLayout.setMain(genericVariablePanel);
                    topLayout.setActive(Constants.SECTION_GENERIC);
                } else if ( type.equals(Constants.SECTION_GENERIC) ) {
                    // List managed in the panel
                } else if ( type.equals(Constants.SECTION_DOCUMENT) )  {
                    if ( !topLayout.isComplete() ) {
                        NotifySettings settings = NotifySettings.newSettings();
                        settings.setType(NotifyType.WARNING);
                        settings.setPlacement(NotifyPlacement.TOP_CENTER);
                        Notify.notify(Constants.DOCUMENT_NOT_COMPLETE, settings);
                    }
                    Document document = new Document();
                    document.setTimeAndLocation(timeAndLocation);
                    document.setCitation(citation);
                    document.setDataSubmitter(dataSubmitter);
                    document.setDic(dic);
                    List<Person> investigators = investigatorPanel.getInvestigators();
                    document.setInvestigators(investigators);
                    document.setPco2a(pco2a);
                    document.setPco2d(pco2d);
                    document.setPh(ph);
                    document.setTa(ta);
                    document.setPlatforms(platformPanel.getPlatforms());
                    document.setFunding(funding);
                    List<Variable> variables = genericVariablePanel.getVariables();
                    if (variables != null && variables.size() > 0)
                        document.setVariables(variables);

                    saveDocumentService.save(document, documentSaved);

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
                    if ( dataSubmitter != null ) {
                        submitterPanel.show(dataSubmitter);
                    }
                    topLayout.setMain(submitterPanel);
                    topLayout.setActive(Constants.SECTION_SUBMITTER);
                } else if ( link.getText().equals(Constants.SECTION_CITATION) ) {
                    if ( citation != null ) {
                        citationPanel.show(citation);
                    }
                    topLayout.setMain(citationPanel);
                }  else if ( link.getText().equals(Constants.SECTION_TIMEANDLOCATION) ) {
                    if ( timeAndLocation != null ) {
                        timeAndLocationPanel.show(timeAndLocation);
                    }
                    topLayout.setMain(timeAndLocationPanel);
                    topLayout.setActive(Constants.SECTION_TIMEANDLOCATION);
                } else if ( link.getText().equals(Constants.SECTION_FUNDING) ) {
                    if ( funding != null ) {
                        fundingPanel.show(funding);
                    }
                    topLayout.setMain(fundingPanel);
                    topLayout.setActive(Constants.SECTION_FUNDING);
                } else if ( link.getText().equals(Constants.SECTION_PLATFORMS) ) {
                    topLayout.setMain(platformPanel);
                    topLayout.setActive(Constants.SECTION_PLATFORMS);
                    if ( platformPanel.getPlatforms().size() > 0 ) {
                        platformPanel.setTableVisible(true);
                    }
                } else if ( link.getText().equals(Constants.SECTION_DIC) ) {
                    topLayout.setMain(dicPanel);
                    topLayout.setActive(Constants.SECTION_DIC);
                    if (dic != null) {
                        dicPanel.show(dic);
                    }
                } else if ( link.getText().equals(Constants.SECTION_DIC2) ) {
                    topLayout.setMain(dic2Panel);
                    topLayout.setActive(Constants.SECTION_DIC2);
                    if ( dic != null ) {
                        dic2Panel.show(dic);
                    }
                } else if ( link.getText().equals(Constants.SECTION_TA) ) {
                    topLayout.setMain(taPanel);
                    topLayout.setActive(Constants.SECTION_TA);
                    if (ta != null) {
                        taPanel.show(ta);
                    }
                } else if ( link.getText().equals(Constants.SECTION_TA2) ) {
                    topLayout.setMain(ta2Panel);
                    topLayout.setActive(Constants.SECTION_TA2);
                    if ( ta != null ) {
                        ta2Panel.show(ta);
                    }
                } else if ( link.getText().equals(Constants.SECTION_PH) ) {
                    topLayout.setMain(phPanel);
                    topLayout.setActive(Constants.SECTION_PH);
                    if (ph != null) {
                        phPanel.show(ph);
                    }
                }  else if ( link.getText().equals(Constants.SECTION_PH2) ) {
                    topLayout.setMain(ph2Panel);
                    topLayout.setActive(Constants.SECTION_PH2);
                    if (ph != null) {
                        ph2Panel.show(ph);
                    }
                } else if ( link.getText().equals(Constants.SECTION_PCO2A) ) {
                    topLayout.setMain(pco2aPanel);
                    topLayout.setActive(Constants.SECTION_PCO2A);
                    if ( pco2a != null ) {
                        pco2aPanel.show(pco2a);
                    }
                } else if ( link.getText().equals(Constants.SECTION_PCO2A2) ) {
                    topLayout.setMain(pco2a2Panel);
                    topLayout.setActive(Constants.SECTION_PCO2A2);
                    if ( pco2a != null ) {
                        pco2a2Panel.show(pco2a);
                    }
                } else if ( link.getText().equals(Constants.SECTION_PCO2D) ) {
                    topLayout.setMain(pco2dPanel);
                    topLayout.setActive(Constants.SECTION_PCO2D);
                    if ( pco2d != null) {
                        pco2dPanel.show(pco2d);
                    }
                } else if ( link.getText().equals(Constants.SECTION_PCO2D2) ) {
                    topLayout.setMain(pco2d2Panel);
                    topLayout.setActive(Constants.SECTION_PCO2D2);
                    if ( pco2d != null ) {
                        pco2d2Panel.show(pco2d);
                    }
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
        Window.addWindowClosingHandler(new Window.ClosingHandler() {

            @Override
            public void onWindowClosing(Window.ClosingEvent event) {
                if ( isDirty() && !saved ) {
                    event.setMessage("It appears you have made changes that you have not saved. Are you sure?");
                }
            }
        });

        Window.addCloseHandler(new CloseHandler<Window>() {

            @Override
            public void onClose(CloseEvent<Window> event) {
                //Execute code when window closes!
            }
        });
    }
    TextCallback documentSaved = new TextCallback() {
        @Override
        public void onFailure(Method method, Throwable throwable) {
            Window.alert(throwable.getMessage());
        }

        @Override
        public void onSuccess(Method method, String s) {
            if ( s.equals("failed") ) {
                Window.alert("Something went wrong. Check with your server administrators.");
            } else {
                currentIndex = s;
                modalHeader.setTitle("Save XML file.");
                save.setType(ButtonType.PRIMARY);
                save.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        modal.hide();
                        saved = true;
                        Window.open(Constants.base+"/document/xml/"+currentIndex,"_blank", null);
                    }
                });

                modal.show();
            }
        }
    };
    Form.SubmitCompleteHandler completeHandler = new Form.SubmitCompleteHandler() {
        @Override
        public void onSubmitComplete(AbstractForm.SubmitCompleteEvent submitCompleteEvent) {
            String jsonString = submitCompleteEvent.getResults();

            // A bug discussed in various places on the 'net, but nothing specific to grails.
            // Just work around for now
            jsonString = jsonString.replace("<pre style=\"word-wrap: break-word; white-space: pre-wrap;\">", "")
                                   .replace("</pre>","");

            jsonString = jsonString.replace("<pre>","");

            try {
                JSONValue json = JSONParser.parseStrict(jsonString);
                Document document = codec.decode(json);

                investigatorPanel.clearPeople();

                if (document.getInvestigators() != null) {
                    List<Person> personList = document.getInvestigators();
                    for (int i = 0; i < personList.size(); i++) {
                        Person p = personList.get(i);
                        if (p != null) {
                            investigatorPanel.show(p);
                            if (investigatorPanel.valid()) {
                                topLayout.setChecked(Constants.SECTION_INVESTIGATOR);
                            }
                        }
                    }
                    investigatorPanel.addPeople(personList);
                    investigatorPanel.reset();
                }

                // TODO this has to be redone to work with the data provider
                List<Variable> variablesList = document.getVariables();
                if (variablesList != null) {
                    genericVariablePanel.addVariables(variablesList);
                }

                if (document.getPlatforms() != null) {
                    List<Platform> platforms = document.getPlatforms();
                    for (int i = 0; i < platforms.size(); i++) {
                        Platform p = platforms.get(i);
                        platformPanel.show(p);
                        if (platformPanel.valid()) {
                            topLayout.setChecked(Constants.SECTION_PLATFORMS);
                        }
                    }
                    platformPanel.reset();
                    platformPanel.addPlatforms(document.getPlatforms());

                }
                if (document.getDataSubmitter() != null) {
                    dataSubmitter = document.getDataSubmitter();
                    submitterPanel.show(dataSubmitter);
                    if (submitterPanel.valid()) {
                        topLayout.setChecked(Constants.SECTION_SUBMITTER);
                    }
                }
                if (document.getCitation() != null) {
                    citation = document.getCitation();
                    citationPanel.show(citation);
                    if (citationPanel.valid()) {
                        topLayout.setChecked(Constants.SECTION_CITATION);
                    }
                }
                if (document.getTimeAndLocation() != null) {
                    timeAndLocation = document.getTimeAndLocation();
                    timeAndLocationPanel.show(timeAndLocation);
                    if (timeAndLocationPanel.valid()) {
                        topLayout.setChecked(Constants.SECTION_TIMEANDLOCATION);
                    }
                }
                if (document.getFunding() != null) {
                    funding = document.getFunding();
                    fundingPanel.show(funding);
                    if (fundingPanel.valid()) {
                        topLayout.setChecked(Constants.SECTION_FUNDING);
                    }
                }
                if (document.getDic() != null) {
                    dic = document.getDic();
                    dicPanel.show(dic);
                    if (dicPanel.valid()) {
                        topLayout.setChecked(Constants.SECTION_DIC);
                    }
                    dic2Panel.show(dic);
                    if (dic2Panel.valid()) {
                        topLayout.setChecked(Constants.SECTION_DIC2);
                    }
                }
                if (document.getTa() != null) {
                    ta = document.getTa();
                    taPanel.show(ta);
                    if (taPanel.valid()) {
                        topLayout.setChecked(Constants.SECTION_TA);
                    }
                    ta2Panel.show(ta);
                    if (ta2Panel.valid()) {
                        topLayout.setChecked(Constants.SECTION_TA2);
                    }
                }
                if (document.getPh() != null) {
                    ph = document.getPh();
                    phPanel.show(ph);
                    if (phPanel.valid()) {
                        topLayout.setChecked(Constants.SECTION_PH);
                    }
                    ph2Panel.show(ph);
                    if (ph2Panel.valid()) {
                        topLayout.setChecked(Constants.SECTION_PH2);
                    }
                }
                if (document.getPco2a() != null) {
                    pco2a = document.getPco2a();
                    pco2aPanel.show(pco2a);
                    if (pco2aPanel.valid()) {
                        topLayout.setChecked(Constants.SECTION_PCO2A);
                    }
                    pco2a2Panel.show(pco2a);
                    if (pco2a2Panel.valid()) {
                        topLayout.setChecked(Constants.SECTION_PCO2A2);
                    }
                }
                if (document.getPco2d() != null) {
                    pco2d = document.getPco2d();
                    pco2dPanel.show(pco2d);
                    if (pco2dPanel.valid()) {
                        topLayout.setChecked(Constants.SECTION_PCO2D);
                    }
                    pco2d2Panel.show(pco2d);
                    if (pco2a2Panel.valid()) {
                        topLayout.setChecked(Constants.SECTION_PCO2D2);
                    }
                }
                if ( dataSubmitter != null ) {
                    submitterPanel.show(dataSubmitter);
                }
                topLayout.setMain(submitterPanel);


            } catch (Exception e) {
                Window.alert("File not processed. e="+e.getLocalizedMessage());
            }
            topLayout.resetFileForm();
        }
    };

    private boolean isDirty() {
        return
        investigatorPanel.isDirty() ||
        submitterPanel.isDirty() ||
        citationPanel.isDirty() ||
        timeAndLocationPanel.isDirty() ||
        fundingPanel.isDirty() ||
        platformPanel.isDirty() ||
        dicPanel.isDirty() ||
        dic2Panel.isDirty() ||
        taPanel.isDirty() ||
        ta2Panel.isDirty() ||
        phPanel.isDirty() ||
        ph2Panel.isDirty() ||
        pco2aPanel.isDirty() ||
        pco2a2Panel.isDirty() ||
        pco2dPanel.isDirty() ||
        pco2d2Panel.isDirty() ||
        genericVariablePanel.isDirty();
    }
}
