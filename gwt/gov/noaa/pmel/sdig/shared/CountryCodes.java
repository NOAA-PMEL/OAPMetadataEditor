/**
 * 
 */
package gov.noaa.pmel.sdig.shared;

import java.io.Serializable;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * @author burger
 *
 */
public class CountryCodes implements Serializable{

	 SortedMap<String, String> codeList;
	 
	 public CountryCodes() {
	}

	/**
	 * @param inputMap
	 */
	public void setCountryCodes(SortedMap<String, String> inputMap) {
		 this.codeList = inputMap;
	 }
	 
	 /**
	 * @return
	 */
	public SortedMap<String, String> getCountryList() {
		 return codeList;
	 }
	
}
