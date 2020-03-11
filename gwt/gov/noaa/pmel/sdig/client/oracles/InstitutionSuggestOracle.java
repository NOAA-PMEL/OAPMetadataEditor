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
public class InstitutionSuggestOracle extends SuggestOracle {

    Resource institutionSuggestionResource = new Resource("oracle/institution");
    InstitutionSuggesionService institutionSuggesionService = GWT.create(InstitutionSuggesionService.class);

    Callback callback;
    Request request;

    public InstitutionSuggestOracle() {
        super();
        ((RestServiceProxy)institutionSuggesionService).setResource(institutionSuggestionResource);
    }

    public interface InstitutionSuggesionService extends RestService {
        @POST
        public void getInstitutionSuggestions(SuggestQuery query, MethodCallback<List<NceiSuggestion>> suggestions);
    }

    @Override
    public void requestSuggestions(Request request, Callback callback) {
        this.callback = callback;
        this.request = request;
        SuggestQuery query = new SuggestQuery();
        query.setQuery(request.getQuery().toLowerCase());
        institutionSuggesionService.getInstitutionSuggestions(query, processSuggestions);
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
