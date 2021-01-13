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
public class FundingSuggestOracle extends SuggestOracle {
    public interface FundingSuggesionService extends RestService {
        @POST
        public void getFundingSuggestions(SuggestQuery query, MethodCallback<List<NceiSuggestion>> suggestions);
    }

    Resource fundingSuggestResource = new Resource("oracle/funding");
    FundingSuggesionService fundingSuggesionService = GWT.create(FundingSuggesionService.class);

    Callback callback;
    Request request;

    public FundingSuggestOracle() {
        super();
        ((RestServiceProxy) fundingSuggesionService).setResource(fundingSuggestResource);
    }

    public void requestDefaultSuggestions(Request request, Callback callback) {
        this.callback = callback;
        this.request = request;
        SuggestQuery query = new SuggestQuery();
        query.setQuery("");
        fundingSuggesionService.getFundingSuggestions(query, processSuggestions);
    }
    public void requestSuggestions(Request request, Callback callback) {
        this.callback = callback;
        this.request = request;
        SuggestQuery query = new SuggestQuery();
        query.setQuery(request.getQuery().toLowerCase());
        fundingSuggesionService.getFundingSuggestions(query, processSuggestions);
    }

    MethodCallback<List<NceiSuggestion>> processSuggestions = new MethodCallback<List<NceiSuggestion>>() {
        @Override
        public void onFailure(Method method, Throwable throwable) {
            GWT.log(throwable.toString());
        }
        @Override
        public void onSuccess(Method method, List<NceiSuggestion> suggestions) {
            Response response= new Response();
            response.setSuggestions(suggestions);
            callback.onSuggestionsReady(request, response);
        }
    };
}
