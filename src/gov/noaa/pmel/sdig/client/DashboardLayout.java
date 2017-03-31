package gov.noaa.pmel.sdig.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gov.noaa.pmel.sdig.client.event.NavLink;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.client.event.SectionSaveHandler;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Created by rhs on 2/27/17.
 */
public class DashboardLayout extends Composite {

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
    AnchorListItem taLink;

    @UiField
    AnchorListItem phLink;

    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    EventBus eventBus = clientFactory.getEventBus();

    @UiField
    HTMLPanel main;
    interface DashboardLayoutUiBinder extends UiBinder<HTMLPanel, DashboardLayout> {
    }

    private static DashboardLayoutUiBinder ourUiBinder = GWT.create(DashboardLayoutUiBinder.class);

    public DashboardLayout() {
        initWidget(ourUiBinder.createAndBindUi(this));
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
        eventBus.addHandler(SectionSave.TYPE, new SectionSaveHandler() {
            @Override
            public void onSectionSave(SectionSave event) {
                if ( event.getType().equals(Constants.SECTION_INVESTIGATOR) ) {
                    investigatorsLink.setIcon(IconType.CHECK);
                } else if ( event.getType().equals(Constants.SECTION_SUBMITTER) ) {
                    submittersLink.setIcon(IconType.CHECK);
                } else if ( event.getType().equals(Constants.SECTION_CITATION) ) {
                    citationLink.setIcon(IconType.CHECK);
                } else if ( event.getType().equals(Constants.SECTION_TIMEANDLOCATION) ) {
                    timeAndLocationLink.setIcon(IconType.CHECK);
                } else if ( event.getType().equals(Constants.SECTION_FUNDING) ) {
                    fundingLink.setIcon(IconType.CHECK);
                } else if ( event.getType().equals(Constants.SECTION_PLATFORMS) ) {
                    platformsLink.setIcon(IconType.CHECK);
                }
            }
        });
    }

    public void setMain(Widget widget) {
        main.clear();
        main.add(widget);
    }
}