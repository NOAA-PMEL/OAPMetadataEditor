package gov.noaa.pmel.sdig.client.oracles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SuggestOracle;
import gov.noaa.pmel.sdig.shared.bean.SuggestQuery;
import gov.noaa.pmel.sdig.shared.bean.NceiSuggestion;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.RestServiceProxy;

import javax.ws.rs.POST;
import java.util.List;

/**
 * Created by rhs on 6/8/17.
 */
public class VariableSuggestOracle extends SuggestOracle {

    Resource variableSuggestionResource = new Resource("oracle/variable");
    VariableSuggesionService variableSuggesionService = GWT.create(VariableSuggesionService.class);

    SuggestOracle.Callback callback;
    Request request;

    public VariableSuggestOracle() {
        super();
        ((RestServiceProxy)variableSuggesionService).setResource(variableSuggestionResource);
    }

    public interface VariableSuggesionService extends RestService {
        @POST
        public void getVariableSuggestions(SuggestQuery query, MethodCallback<List<NceiSuggestion>> suggestions);
    }
    @Override
    public void requestDefaultSuggestions(Request request, Callback callback) {
        this.callback = callback;
        this.request = request;
        SuggestQuery query = new SuggestQuery();
        query.setQuery("");
        variableSuggesionService.getVariableSuggestions(query, processSuggestions);
    }
    @Override
    public void requestSuggestions(Request request, SuggestOracle.Callback callback) {
        this.callback = callback;
        this.request = request;
        SuggestQuery query = new SuggestQuery();
        query.setQuery(request.getQuery().toLowerCase());
        variableSuggesionService.getVariableSuggestions(query, processSuggestions);
    }

    MethodCallback<List<NceiSuggestion>> processSuggestions = new MethodCallback<List<NceiSuggestion>>() {
        @Override
        public void onFailure(Method method, Throwable throwable) {

        }

        @Override
        public void onSuccess(Method method, List<NceiSuggestion> suggestions) {
            SuggestOracle.Response response= new SuggestOracle.Response();
            response.setSuggestions(suggestions);
            callback.onSuggestionsReady(request, response);
        }
    };

}
