package gov.noaa.pmel.sdig.client;

import com.google.gwt.core.client.GWT;

/**
 * Created by rhs on 3/1/17.
 */
public class Constants {

    public static final String base = GWT.getHostPageBaseURL();

    public static String NOT_FOUND = "Not Found";

    public static String SECTION_INVESTIGATOR = "Investigators";
    public static String SECTION_SUBMITTER = "Data Submitter";
    public static String SECTION_CITATION = "Citation Information";
    public static String SECTION_TIMEANDLOCATION = "Time and Location Information";
    public static String SECTION_FUNDING = "Funding";
    public static String SECTION_PLATFORMS = "Platforms";
    public static String SECTION_CO2 = "CO2 Variable Information";
    public static String SECTION_DIC = "DIC";
    public static String SECTION_TA = "TA";
    public static String SECTION_PH = "pH";
    public static String SECTION_PCO2A = "pCO2A";
    public static String SECTION_PCO2D = "pCO2D";
    public static String SECTION_GENERIC = "Other Variables";

    public static String SECTION_DOCUMENT = " document";

    public static final String saveDocument = base + "document/saveDoc";
    public static final String saveChange = base + "document/saveChange";
    public static final String getDocument = base + "document/getDoc";
    public static final String getFunding = base + "funding/getGrantInfo";

    public static final String DOCUMENT_NOT_COMPLETE = "You are saving an incomplete document. The dataset cannot be submitted with incomplete metadata.";

    public static final String NOT_COMPLETE = "Section is not complete! See form fields highlighted in red.";
    public static final String NO_FILE = "You must select a file to upload.";
    public static final String NOT_SAVED = "You may have unsaved changes. Save the section you are working on and save your document again.";
    public static final String COMPLETE = "Section saved.";
    public static final String MEASURED = "Use menu to pick 'measured' or 'calculated'";
    public static final String DETAILS = "Pick from the details menu.";

}
