package gov.noaa.pmel.sdig.shared.bean;

import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * Created by rhs on 6/8/17.
 */
public class NceiSuggestion implements SuggestOracle.Suggestion {
    String suggestion;
    @Override
    public String getDisplayString() {
        return suggestion;
    }

    @Override
    public String getReplacementString() {
        return suggestion;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
