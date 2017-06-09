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
public class InstrumentSuggestOracle extends SuggestOracle {
    public interface InstrumentSuggesionService extends RestService {
        @POST
        public void getInstrumentSuggestions(SuggestQuery query, MethodCallback<List<NceiSuggestion>> suggestions);
    }

    Resource instrumentSuggestResource = new Resource("oracle/instrument");
    InstrumentSuggesionService instrumentSuggesionService = GWT.create(InstrumentSuggesionService.class);

    SuggestOracle.Callback callback;
    Request request;

    public InstrumentSuggestOracle() {
        super();
        ((RestServiceProxy) instrumentSuggesionService).setResource(instrumentSuggestResource);
    }

    public void requestSuggestions(Request request, SuggestOracle.Callback callback) {
        this.callback = callback;
        this.request = request;
        SuggestQuery query = new SuggestQuery();
        query.setQuery(request.getQuery().toLowerCase());
        instrumentSuggesionService.getInstrumentSuggestions(query, processSuggestions);
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
