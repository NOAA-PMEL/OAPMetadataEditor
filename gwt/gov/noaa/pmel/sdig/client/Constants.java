package gov.noaa.pmel.sdig.client;

import com.google.gwt.core.client.GWT;

/**
 * Created by rhs on 3/1/17.
 */
public class Constants {

    public static final String base = GWT.getHostPageBaseURL();

    public static String NOT_FOUND = "Not Found";

    public static final String SECTION_INVESTIGATOR = "Investigators";
    public static final String SECTION_SUBMITTER = "Data Submitter";
    public static final String SECTION_CITATION = "Citation Information";
    public static final String SECTION_TIMEANDLOCATION = "Time and Location Information";
    public static final String SECTION_FUNDING = "Funding";
    public static final String SECTION_PLATFORMS = "Platforms";
    public static final String SECTION_DIC = "DIC";
//    public static final String SECTION_DIC2 = "DIC (2)";
    public static final String SECTION_TA = "TA";
//    public static final String SECTION_TA2 = "TA (2)";
    public static final String SECTION_PH = "pH";
//    public static final String SECTION_PH2 = "pH (2)";
    public static final String SECTION_PCO2A = "pCO2A";
//    public static final String SECTION_PCO2A2 = "pCO2A (2)";
    public static final String SECTION_PCO2D = "pCO2D";
//    public static final String SECTION_PCO2D2 = "pCO2D (2)";
    public static final String SECTION_VARIABLES = "Variable";

    public static final String SECTION_DOCUMENT = " document";

    public static final String saveDocument = base + "document/saveDoc";
    public static final String savePartial = base + "document/savePartial";
    public static final String getDocument = base + "document/getDoc";
    public static final String getFunding = base + "funding/getGrantInfo";

    public static final String DOCUMENT_NOT_COMPLETE = "You are saving an incomplete document. The dataset cannot be submitted with incomplete metadata.";

    public static final String NOT_COMPLETE = "Section is not complete! See form fields highlighted in red.";
    public static final String NO_FILE = "You must select a file to upload.";
    public static final String NOT_SAVED = "You may have unsaved changes. Save the section you are working on and save your document again.";
    public static final String COMPLETE = "Section saved.";
    public static final String MEASURED = "Use menu to pick 'measured' or 'calculated'";
    public static final String DETAILS = "Pick from the details menu.";

//    public static final List<String> COUNTRIES = Arrays.asList(new String[] {
    public static final String[] COUNTRIES = new String[] {
            "",
            "Afghanistan",
            "Åland Islands",
            "Albania",
            "Algeria",
            "American Samoa",
            "Andorra",
            "Angola",
            "Anguilla",
            "Antarctica",
            "Antigua and Barbuda",
            "Argentina",
            "Armenia",
            "Aruba",
            "Australia",
            "Austria",
            "Azerbaijan",
            "Bahamas",
            "Bahrain",
            "Bangladesh",
            "Barbados",
            "Belarus",
            "Belgium",
            "Belize",
            "Benin",
            "Bermuda",
            "Bhutan",
            "Bolivia (Plurinational State of)",
            "Bonaire, Sint Eustatius and Saba",
            "Bosnia and Herzegovina",
            "Botswana",
            "Bouvet Island",
            "Brazil",
            "British Indian Ocean Territory",
            "Brunei Darussalam",
            "Bulgaria",
            "Burkina Faso",
            "Burundi",
            "Cambodia",
            "Cameroon",
            "Canada",
            "Cabo Verde",
            "Cayman Islands",
            "Central African Republic",
            "Chad",
            "Chile",
            "China",
            "Christmas Island",
            "Cocos (Keeling) Islands",
            "Colombia",
            "Comoros",
            "Congo",
            "Congo (Democratic Republic of the)",
            "Cook Islands",
            "Costa Rica",
            "Côte d\u0027Ivoire",
            "Croatia",
            "Cuba",
            "Curaçao",
            "Cyprus",
            "Czech Republic",
            "Denmark",
            "Djibouti",
            "Dominica",
            "Dominican Republic",
            "Ecuador",
            "Egypt",
            "El Salvador",
            "Equatorial Guinea",
            "Eritrea",
            "Estonia",
            "Ethiopia",
            "Falkland Islands (Malvinas)",
            "Faroe Islands",
            "Fiji",
            "Finland",
            "France",
            "French Guiana",
            "French Polynesia",
            "French Southern Territories",
            "Gabon",
            "Gambia",
            "Georgia",
            "Germany",
            "Ghana",
            "Gibraltar",
            "Greece",
            "Greenland",
            "Grenada",
            "Guadeloupe",
            "Guam",
            "Guatemala",
            "Guernsey",
            "Guinea",
            "Guinea-Bissau",
            "Guyana",
            "Haiti",
            "Heard Island and McDonald Islands",
            "Holy See",
            "Honduras",
            "Hong Kong",
            "Hungary",
            "Iceland",
            "India",
            "Indonesia",
            "Iran (Islamic Republic of)",
            "Iraq",
            "Ireland",
            "Isle of Man",
            "Israel",
            "Italy",
            "Jamaica",
            "Japan",
            "Jersey",
            "Jordan",
            "Kazakhstan",
            "Kenya",
            "Kiribati",
            "Korea (Democratic People\u0027s Republic of)",
            "Korea (Republic of)",
            "Kuwait",
            "Kyrgyzstan",
            "Lao People\u0027s Democratic Republic",
            "Latvia",
            "Lebanon",
            "Lesotho",
            "Liberia",
            "Libya",
            "Liechtenstein",
            "Lithuania",
            "Luxembourg",
            "Macao",
            "Macedonia (the former Yugoslav Republic of)",
            "Madagascar",
            "Malawi",
            "Malaysia",
            "Maldives",
            "Mali",
            "Malta",
            "Marshall Islands",
            "Martinique",
            "Mauritania",
            "Mauritius",
            "Mayotte",
            "Mexico",
            "Micronesia (Federated States of)",
            "Moldova (Republic of)",
            "Monaco",
            "Mongolia",
            "Montenegro",
            "Montserrat",
            "Morocco",
            "Mozambique",
            "Myanmar",
            "Namibia",
            "Nauru",
            "Nepal",
            "Netherlands",
            "New Caledonia",
            "New Zealand",
            "Nicaragua",
            "Niger",
            "Nigeria",
            "Niue",
            "Norfolk Island",
            "Northern Mariana Islands",
            "Norway",
            "Oman",
            "Pakistan",
            "Palau",
            "Palestine, State of",
            "Panama",
            "Papua New Guinea",
            "Paraguay",
            "Peru",
            "Philippines",
            "Pitcairn",
            "Poland",
            "Portugal",
            "Puerto Rico",
            "Qatar",
            "Réunion",
            "Romania",
            "Russian Federation",
            "Rwanda",
            "Saint Barthélemy",
            "Saint Helena, Ascension and Tristan da Cunha",
            "Saint Kitts and Nevis",
            "Saint Lucia",
            "Saint Martin (French part)",
            "Saint Pierre and Miquelon",
            "Saint Vincent and the Grenadines",
            "Samoa",
            "San Marino",
            "Sao Tome and Principe",
            "Saudi Arabia",
            "Senegal",
            "Serbia",
            "Seychelles",
            "Sierra Leone",
            "Singapore",
            "Sint Maarten (Dutch part)",
            "Slovakia",
            "Slovenia",
            "Solomon Islands",
            "Somalia",
            "South Africa",
            "South Georgia and the South Sandwich Islands",
            "South Sudan",
            "Spain",
            "Sri Lanka",
            "Sudan",
            "Suriname",
            "Svalbard and Jan Mayen",
            "Swaziland",
            "Sweden",
            "Switzerland",
            "Syrian Arab Republic",
            "Taiwan, Province of China",
            "Tajikistan",
            "Tanzania, United Republic of",
            "Thailand",
            "Timor-Leste",
            "Togo",
            "Tokelau",
            "Tonga",
            "Trinidad and Tobago",
            "Tunisia",
            "Turkey",
            "Turkmenistan",
            "Turks and Caicos Islands",
            "Tuvalu",
            "Uganda",
            "Ukraine",
            "United Arab Emirates",
            "United Kingdom of Great Britain and Northern Ireland",
            "United States of America",
            "United States Minor Outlying Islands",
            "Uruguay",
            "Uzbekistan",
            "Vanuatu",
            "Venezuela (Bolivarian Republic of)",
            "Viet Nam",
            "Virgin Islands (British)",
            "Virgin Islands (U.S.)",
            "Wallis and Futuna",
            "Western Sahara",
            "Yemen",
            "Zambia",
            "Zimbabwe"
    };
}
