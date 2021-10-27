package gov.noaa.pmel.sdig.client.oracles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SuggestOracle;
import gov.noaa.pmel.sdig.shared.bean.NceiSuggestion;
import gov.noaa.pmel.sdig.shared.bean.SuggestQuery;
import org.fusesource.restygwt.client.*;

import javax.ws.rs.POST;
import java.util.List;

/**
 * Created by rhs on 6/8/17.
 */
public class PlatformSuggestOracle extends SuggestOracle {

    Resource platformSuggestionResource = new Resource("oracle/platform");
    PlatformSuggesionService platformSuggesionService = GWT.create(PlatformSuggesionService.class);

    Callback callback;
    Request request;

    public PlatformSuggestOracle() {
        super();
        ((RestServiceProxy)platformSuggesionService).setResource(platformSuggestionResource);
    }

    public interface PlatformSuggesionService extends RestService {
        @POST
        public void getPlatformSuggestions(SuggestQuery query, MethodCallback<List<NceiSuggestion>> suggestions);
    }

    @Override
    public void requestDefaultSuggestions(Request request, Callback callback) {
        this.callback = callback;
        this.request = request;
        SuggestQuery query = new SuggestQuery();
        query.setQuery("");
        platformSuggesionService.getPlatformSuggestions(query, processSuggestions);
    }

    @Override
    public void requestSuggestions(Request request, Callback callback) {
        this.callback = callback;
        this.request = request;
        SuggestQuery query = new SuggestQuery();
        query.setQuery(request.getQuery().toLowerCase());
        platformSuggesionService.getPlatformSuggestions(query, processSuggestions);
    }

    MethodCallback<List<NceiSuggestion>> processSuggestions = new MethodCallback<List<NceiSuggestion>>() {
        @Override
        public void onFailure(Method method, Throwable throwable) {

        }

        @Override
        public void onSuccess(Method method, List<NceiSuggestion> suggestions) {
            Response response= new Response();
            response.setSuggestions(suggestions);
            callback.onSuggestionsReady(request, response);
        }
    };

}
