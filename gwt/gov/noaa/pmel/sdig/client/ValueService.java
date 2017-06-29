package gov.noaa.pmel.sdig.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import gov.noaa.pmel.sdig.shared.CountryCodes;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("values")
public interface ValueService extends RemoteService {
	// String greetServer(String name) throws IllegalArgumentException;
	CountryCodes getCountryCodes() throws IllegalArgumentException;
}
