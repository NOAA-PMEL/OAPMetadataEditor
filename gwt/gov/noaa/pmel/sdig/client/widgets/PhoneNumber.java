package gov.noaa.pmel.sdig.client.widgets;

import java.util.SortedMap;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

import gov.noaa.pmel.sdig.shared.CountryCodes;


public class PhoneNumber extends Composite {
	
	private TextBox areaCode = null;
	private TextBox number = null;
	private ListBox country = null;
	private HorizontalPanel panel = null;

	public @UiConstructor PhoneNumber() {
		this.areaCode = new TextBox();
		this.number = new TextBox();
		this.country = new ListBox();

		//Todo - populate listbox with country codes
		panel = new HorizontalPanel();
		
		areaCode.setMaxLength(4);
		number.setMaxLength(7);
		
		panel.add(country);
		panel.add(areaCode);
		panel.add(number);
		
		initWidget(panel);
		}
	
	/**
	 * @param codes
	 */
	public void setCountryCodes(CountryCodes codes) {
		GWT.log("____________________Got country codes");
		SortedMap<String, String> codeList = codes.getCountryList();
		for (SortedMap.Entry<String, String> entry : codeList.entrySet()) {
			String key = entry.getKey();
			country.addItem(key, codeList.get(key));
		}
	}
	
}

