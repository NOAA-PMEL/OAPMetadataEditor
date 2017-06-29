package gov.noaa.pmel.sdig.client.widgets;

import java.util.SortedMap;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;

import gov.noaa.pmel.sdig.shared.CountryCodes;

/**
 * @author burger
 *
 */
public class CustomSelectionList extends Composite {

	private HorizontalPanel panel;
	private InlineLabel listLabel;
	private ListBox list;

	/**
	 * @param listSelection
	 * @param label
	 */
	public @UiConstructor CustomSelectionList(String listSelection, String label) {
		panel = new HorizontalPanel();
		this.listLabel = new InlineLabel(label);
		list = new ListBox();
		// list.addItem("Test", "Value");
		panel.add(list);
		panel.add(listLabel);
		initWidget(panel);
	}
	
	/**
	 * @param codes
	 */
	public void setLabelValue(CountryCodes codes) {
		SortedMap<String, String> codeList = codes.getCountryList();
		for (SortedMap.Entry<String, String> entry : codeList.entrySet()) {
			String key = entry.getKey();
			list.addItem(key, codeList.get(key));
		}
	}

}
