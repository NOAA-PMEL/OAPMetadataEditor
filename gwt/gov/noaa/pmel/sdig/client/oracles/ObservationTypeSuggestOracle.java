package gov.noaa.pmel.sdig.client.oracles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SuggestOracle;
import gov.noaa.pmel.sdig.shared.bean.NceiSuggestion;
import gov.noaa.pmel.sdig.shared.bean.SuggestQuery;
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
public class ObservationTypeSuggestOracle extends SuggestOracle {
    public interface ObservationTypeSuggesionService extends RestService {
        @POST
        public void getObservationTypeSuggestions(SuggestQuery query, MethodCallback<List<NceiSuggestion>> suggestions);
    }

    Resource observationTypeSuggestResource = new Resource("oracle/observationType");
    ObservationTypeSuggesionService observationTypeSuggesionService = GWT.create(ObservationTypeSuggesionService.class);

    Callback callback;
    Request request;

    public ObservationTypeSuggestOracle() {
        super();
        ((RestServiceProxy) observationTypeSuggesionService).setResource(observationTypeSuggestResource);
    }

    public void requestSuggestions(Request request, Callback callback) {
        this.callback = callback;
        this.request = request;
        SuggestQuery query = new SuggestQuery();
        query.setQuery(request.getQuery().toLowerCase());
        observationTypeSuggesionService.getObservationTypeSuggestions(query, processSuggestions);
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
