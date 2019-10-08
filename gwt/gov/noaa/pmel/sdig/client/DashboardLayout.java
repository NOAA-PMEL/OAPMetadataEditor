package gov.noaa.pmel.sdig.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gov.noaa.pmel.sdig.client.event.NavLink;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.event.SectionSaveHandler;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.base.form.AbstractForm;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

/**
 * Created by rhs on 2/27/17.
 */
public class DashboardLayout extends Composite {

    @UiField
    Button saveNotify;

    @UiField
    Button reset;

//    @UiField
//    Button toggle;

    @UiField
    Column nav;

    @UiField
    Column mainColumn;

    @UiField
    Button preview;

    @UiField
    Button download;

    @UiField
    AnchorListItem investigatorsLink;

    @UiField
    AnchorListItem submittersLink;

    @UiField
    AnchorListItem citationLink;

    @UiField
    AnchorListItem timeAndLocationLink;

    @UiField
    AnchorListItem fundingLink;

    @UiField
    AnchorListItem platformsLink;

    @UiField
    AnchorListItem dicLink;

    @UiField
    AnchorListItem taLink;;

    @UiField
    AnchorListItem phLink;

    @UiField
    AnchorListItem pco2aLink;

    @UiField
    AnchorListItem pco2dLink;

    @UiField
    AnchorListItem genericVariableLink;

    @UiField
    Form uploadForm;

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    @UiField
    HTMLPanel main;

    @UiField
    Input filename;

    @UiField
    Button filebutton;

    @UiField
    HTMLPanel fakeinput;

    @UiField
    TextBox faketextbox;

    interface DashboardLayoutUiBinder extends UiBinder<HTMLPanel, DashboardLayout> {
    }

    private static DashboardLayoutUiBinder ourUiBinder = GWT.create(DashboardLayoutUiBinder.class);

    public DashboardLayout() {
        initWidget(ourUiBinder.createAndBindUi(this));

        filename.addStyleName("disappear");
        filename.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                faketextbox.setValue("");
                if( event.getValue() != null && event.getValue().length() > 0 ){
                    String file = event.getValue();
                    if ( file.contains("/") )
                        file = file.substring(file.lastIndexOf("/")+1, file.length());
                    if ( file.contains("\\") )
                        file = file.substring(file.lastIndexOf("\\")+1, file.length());
                    faketextbox.setValue(file);
                }
            }
        });
        filebutton.addStyleName("over");
        fakeinput.addStyleName("fakeinputposition");
        faketextbox.addStyleName("overright");
        uploadForm.addSubmitHandler(submitHandler);

        investigatorsLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEventFromSource(new NavLink(), investigatorsLink);
            }
        });
        submittersLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEventFromSource(new NavLink(), submittersLink);
            }
        });
        citationLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEventFromSource(new NavLink(), citationLink);
            }
        });
        timeAndLocationLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEventFromSource(new NavLink(), timeAndLocationLink);
            }
        });
        fundingLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEventFromSource(new NavLink(), fundingLink);
            }
        });
        platformsLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEventFromSource(new NavLink(), platformsLink);
            }
        });
        dicLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEventFromSource(new NavLink(), dicLink);
            }
        });
        taLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEventFromSource(new NavLink(), taLink);
            }
        });
        phLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEventFromSource(new NavLink(), phLink);
            }
        });
        pco2aLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEventFromSource(new NavLink(), pco2aLink);
            }
        });
        pco2dLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEventFromSource(new NavLink(), pco2dLink);
            }
        });
        genericVariableLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEventFromSource(new NavLink(), genericVariableLink);
            }
        });
        eventBus.addHandler(SectionSave.TYPE, new SectionSaveHandler() {
            @Override
            public void onSectionSave(SectionSave event) {
                String section = event.getType();
                setChecked(section);
            }
        });

        preview.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                OAPMetadataEditor.logToConsole("preview:"+event);
                eventBus.fireEventFromSource(new SectionSave("Preview", Constants.SECTION_DOCUMENT), preview);
            }
        });
        download.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                OAPMetadataEditor.logToConsole("download:"+event);
                eventBus.fireEventFromSource(new SectionSave("Download", Constants.SECTION_DOCUMENT), download);
            }
        });
        saveNotify.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                OAPMetadataEditor.logToConsole("saveNotify:"+event);
                eventBus.fireEventFromSource(new SectionSave("Notify", Constants.SECTION_DOCUMENT), saveNotify);
            }
        });
        reset.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                OAPMetadataEditor.logToConsole("reset:"+event);
                showConfirmationDialog("This will clear all metadata fields.");
                eventBus.fireEventFromSource(event, reset);
            }
        });

//        toggle.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                if ( nav.isVisible() ) {
//                    nav.setVisible(false);
//                    mainColumn.setSize("SM_12");
//                } else {
//                    nav.setVisible(true);
//                    mainColumn.setSize("SM_9");
//                }
//
//            }
//        });
    }

    public static boolean showConfirmationDialog(String msg) {
        return false;
    }
    public void uncheck(String section) {
        if (section.equals(Constants.SECTION_DOCUMENT)) {
            return;
        } else if ( section.equals(Constants.SECTION_INVESTIGATOR) ) {
            investigatorsLink.setIcon(null);
        } else if ( section.equals(Constants.SECTION_SUBMITTER) ) {
            submittersLink.setIcon(null);
        } else if ( section.equals(Constants.SECTION_CITATION) ) {
            citationLink.setIcon(null);
        } else if ( section.equals(Constants.SECTION_TIMEANDLOCATION) ) {
            timeAndLocationLink.setIcon(null);
        } else if ( section.equals(Constants.SECTION_FUNDING) ) {
            fundingLink.setIcon(null);
        } else if ( section.equals(Constants.SECTION_PLATFORMS) ) {
            platformsLink.setIcon(null);
        } else if ( section.equals(Constants.SECTION_DIC)) {
            dicLink.setIcon(null);
        } else if ( section.equals(Constants.SECTION_TA) ) {
            taLink.setIcon(null);
        } else if ( section.equals(Constants.SECTION_PH) ) {
            phLink.setIcon(null);
        } else if ( section.equals(Constants.SECTION_PCO2A) ) {
            pco2aLink.setIcon(null);
        } else if ( section.equals(Constants.SECTION_PCO2D) ) {
            pco2dLink.setIcon(null);
        } else if ( section.equals(Constants.SECTION_GENERIC) ) {
            genericVariableLink.setIcon(null);
        }
    }
    public void setChecked(String section) {
        setIcon(section, IconType.CHECK);
    }

    public void setIcon(String section, IconType icon) {
        if (section.equals(Constants.SECTION_DOCUMENT)) {
            return;
        } else if ( section.equals(Constants.SECTION_INVESTIGATOR) ) {
            investigatorsLink.setIcon(IconType.CHECK);
        } else if ( section.equals(Constants.SECTION_SUBMITTER) ) {
            submittersLink.setIcon(IconType.CHECK);
        } else if ( section.equals(Constants.SECTION_CITATION) ) {
            citationLink.setIcon(IconType.CHECK);
        } else if ( section.equals(Constants.SECTION_TIMEANDLOCATION) ) {
            timeAndLocationLink.setIcon(IconType.CHECK);
        } else if ( section.equals(Constants.SECTION_FUNDING) ) {
            fundingLink.setIcon(IconType.CHECK);
        } else if ( section.equals(Constants.SECTION_PLATFORMS) ) {
            platformsLink.setIcon(IconType.CHECK);
        } else if ( section.equals(Constants.SECTION_DIC)) {
            dicLink.setIcon(IconType.CHECK);
        } else if ( section.equals(Constants.SECTION_TA) ) {
            taLink.setIcon(IconType.CHECK);
        } else if ( section.equals(Constants.SECTION_PH) ) {
            phLink.setIcon(IconType.CHECK);
        } else if ( section.equals(Constants.SECTION_PCO2A) ) {
            pco2aLink.setIcon(IconType.CHECK);
        } else if ( section.equals(Constants.SECTION_PCO2D) ) {
            pco2dLink.setIcon(IconType.CHECK);
        } else if ( section.equals(Constants.SECTION_GENERIC) ) {
            genericVariableLink.setIcon(IconType.CHECK);
        }
    }
    public boolean isComplete() {
        if (investigatorsLink.getIcon() == null ) {
            return false;
        }
        if ( submittersLink.getIcon() == null ) {
            return false;
        }
        if ( citationLink.getIcon() == null ) {
            return false;
        }
        if ( timeAndLocationLink.getIcon() == null ) {
            return false;
        }
        if ( fundingLink.getIcon() == null ) {
            return false;
        }
        if ( platformsLink.getIcon() == null ) {
            return false;
        }
        if ( dicLink.getIcon() == null ) {
            return false;
        }
        if ( taLink.getIcon() == null ) {
            return false;
        }
        if ( phLink.getIcon() == null ) {
            return false;
        }
        if ( pco2aLink.getIcon() == null ) {
            return false;
        }
        if ( pco2dLink.getIcon() == null ) {
            return false;
        }
        return true;
    }
    // clickHandlers added directly
//    @UiHandler("saveNotify")
//    public void saveNotifyClick(ClickEvent event) {
//        OAPMetadataEditor.logToConsole("saveNotify:"+event);
//    }
//    @UiHandler("reset")
//    public void onClick(ClickEvent event) {
//        eventBus.fireEventFromSource(event, reset);
//    }

    public void removeCheckmarks() {
        investigatorsLink.setIcon(null);
        submittersLink.setIcon(null);
        citationLink.setIcon(null);
        timeAndLocationLink.setIcon(null);
        fundingLink.setIcon(null);
        platformsLink.setIcon(null);
        dicLink.setIcon(null);
        taLink.setIcon(null);
        phLink.setIcon(null);
        pco2aLink.setIcon(null);
        pco2dLink.setIcon(null);
    }
//    public void setSaveEnabled(boolean enabled) {
//        save.setEnabled(enabled);
//    }
    public void setMain(Widget widget) {
        main.clear();
        main.add(widget);
    }
    public Widget getMain() {
        return main.getWidget(0);
    }
    public void addUploadSuccess(Form.SubmitCompleteHandler handler) {
        uploadForm.addSubmitCompleteHandler(handler);
    }

    public void resetFileForm() {
        uploadForm.reset();
    }
    AbstractForm.SubmitHandler submitHandler = new AbstractForm.SubmitHandler() {
        @Override
        public void onSubmit(AbstractForm.SubmitEvent submitEvent) {
            String value = filename.getValue();
            if ( value == null || value.length() <= 0 ) {
                submitEvent.cancel();
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.WARNING);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.NO_FILE, settings);
            }
        }
    };
    public void setActive(String section) {
        investigatorsLink.setActive(false);
        submittersLink.setActive(false);
        citationLink.setActive(false);
        timeAndLocationLink.setActive(false);
        fundingLink.setActive(false);
        platformsLink.setActive(false);
        dicLink.setActive(false);
        taLink.setActive(false);
        phLink.setActive(false);
        pco2aLink.setActive(false);
        pco2dLink.setActive(false);
        genericVariableLink.setActive(false);
        if ( section.equals(Constants.SECTION_INVESTIGATOR) ) {
            investigatorsLink.setActive(true);
        } else if ( section.equals(Constants.SECTION_SUBMITTER) ) {
            submittersLink.setActive(true);
        } else if ( section.equals(Constants.SECTION_CITATION) ) {
            citationLink.setActive(true);
        } else if ( section.equals(Constants.SECTION_TIMEANDLOCATION) ) {
            timeAndLocationLink.setActive(true);
        } else if ( section.equals(Constants.SECTION_FUNDING) ) {
            fundingLink.setActive(true);
        } else if ( section.equals(Constants.SECTION_PLATFORMS) ) {
            platformsLink.setActive(true);
        } else if ( section.equals(Constants.SECTION_DIC)) {
            dicLink.setActive(true);
        } else if ( section.equals(Constants.SECTION_TA) ) {
            taLink.setActive(true);
        } else if ( section.equals(Constants.SECTION_PH) ) {
            phLink.setActive(true);
        } else if ( section.equals(Constants.SECTION_PCO2A) ) {
            pco2aLink.setActive(true);
        } else if ( section.equals(Constants.SECTION_PCO2D) ) {
            pco2dLink.setActive(true);
        } else if ( section.equals(Constants.SECTION_GENERIC) ) {
            genericVariableLink.setActive(true);
        }
    }
    public String getActive() {
        if ( investigatorsLink.isActive() ) return Constants.SECTION_INVESTIGATOR;
        if ( submittersLink.isActive() ) return Constants.SECTION_SUBMITTER;
        if ( citationLink.isActive() ) return Constants.SECTION_CITATION;
        if ( timeAndLocationLink.isActive() ) return Constants.SECTION_TIMEANDLOCATION;
        if ( fundingLink.isActive() ) return Constants.SECTION_FUNDING;
        if ( platformsLink.isActive() ) return Constants.SECTION_PLATFORMS;
        if ( dicLink.isActive() ) return Constants.SECTION_DIC;
        if ( taLink.isActive() ) return Constants.SECTION_TA;
        if ( phLink.isActive() ) return Constants.SECTION_PH;
        if ( pco2aLink.isActive() ) return Constants.SECTION_PCO2A;
        if ( pco2dLink.isActive() ) return Constants.SECTION_PCO2D;
        if ( genericVariableLink.isActive() ) return Constants.SECTION_GENERIC;
        return null;
    }

}