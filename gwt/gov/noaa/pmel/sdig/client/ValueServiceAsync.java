package gov.noaa.pmel.sdig.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import gov.noaa.pmel.sdig.shared.CountryCodes;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ValueServiceAsync {
	// void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
	void getCountryCodes(AsyncCallback<CountryCodes> callback) throws IllegalArgumentException;
}
