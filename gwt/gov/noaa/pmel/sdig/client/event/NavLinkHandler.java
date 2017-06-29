package gov.noaa.pmel.sdig.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created by rhs on 3/1/17.
 */
public interface NavLinkHandler extends EventHandler {
    void onNavLink(NavLink event);
}
