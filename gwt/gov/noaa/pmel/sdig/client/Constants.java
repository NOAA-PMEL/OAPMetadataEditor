package gov.noaa.pmel.sdig.client;

import com.google.gwt.core.client.GWT;

/**
 * Created by rhs on 3/1/17.
 */
public class Constants {

    public static final String base = GWT.getHostPageBaseURL();


    public static String SECTION_INVESTIGATOR = "Investigators";
    public static String SECTION_SUBMITTER = "Data Submitter";
    public static String SECTION_CITATION = "Citation Information";
    public static String SECTION_TIMEANDLOCATION = "Time and Location Information";
    public static String SECTION_FUNDING = "Funding";
    public static String SECTION_PLATFORMS = "Platforms";
    public static String SECTION_DIC = "DIC";
    public static String SECTION_DIC2 = "DIC (2)";
    public static String SECTION_TA = "TA";
    public static String SECTION_TA2 = "TA (2)";
    public static String SECTION_PH = "pH";
    public static String SECTION_PH2 = "pH (2)";
    public static String SECTION_PCO2A = "pCO2A";
    public static String SECTION_PCO2A2 = "pCO2A (2)";
    public static String SECTION_PCO2D = "pCO2D";
    public static String SECTION_PCO2D2 = "pCO2D (2)";
    public static String SECTION_GENERIC = "Variable";

    public static String SECTION_DOCUMENT = " document";

    public static final String saveDocument = base + "document/saveDoc";

    public static final String DOCUMENT_NOT_COMPLETE = "You are saving an incomplete document. Don't forget to upload the document and finish providing the metadata..";

    public static final String NOT_COMPLETE = "Section is not complete! See form fields highlighted in red.";
    public static final String NO_FILE = "You must select a file to upload.";
    public static final String COMPLETE = "Section saved.";

}
