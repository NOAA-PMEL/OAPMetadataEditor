package gov.noaa.pmel.sdig.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by rhs on 3/1/17.
 */
public class NavLink extends GwtEvent<NavLinkHandler> {
    public static Type<NavLinkHandler> TYPE = new Type<NavLinkHandler>();

    public Type<NavLinkHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(NavLinkHandler handler) {
        handler.onNavLink(this);
    }
}
