package gov.noaa.pmel.sdig.client.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import gov.noaa.pmel.sdig.client.Constants;
import gov.noaa.pmel.sdig.client.event.SectionSave;
import gov.noaa.pmel.sdig.shared.bean.Variable;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyPlacement;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

/**
 * Created by rhs on 3/8/17.
 */
public class TaPanel extends CommonVariablePanel {
    public TaPanel() {
        super();
        abbreviation.setText("TA");
        abbreviation.setEnabled(false);
        fullVariableName.setText("Total Alkalinity");
        fullVariableName.setEnabled(false);
        heading.setText("Enter the information for Total Alkalinity (TA)");
        save.addClickHandler(saveIt);
        abbreviationModal.setTitle("23.1 Column header name of the variable in the data files, e.g., TA, Alk, etc.");
        observationTypeModal.setTitle("23.2 How the variable is observed, e.g., surface underway, profile, time series, model output, etc. For experimental data, this could be: laboratory experiment, pelagic mesocosm, benthic mesocosm, benthic FOCE type studies, natural pertubration site studies, etc");
        manipulationMethodModal.setTitle("23.4 In perturbation experiments, seawater carbonate chemistry can be manipulated by different techniques, such as bubbling CO2, adding acids or bases, etc.");
        observationDetailModal.setTitle("23.3 Whether the variable belong to an in-situ observed variable, or a manipulation condition variable, or a response variable in a \n" +
                "biological experimental study.");
        unitsModal.setTitle("23.5 Units of the variable (e.g., μmol/kg).");
        measuredModal.setTitle("23.6 Variable is measured in-situ, or calculated from other variables.");
        calculationMethodModal.setTitle("23.7 Variables can be calculated using different sets of constants or different software.");
        samplingInstrumentModal.setTitle("23.8 Instrument that is used to collect water samples, or deploy sensors, etc. For example, a Niskin bottle, pump, CTD, etc is a\n" +
                " sampling instrument.");
        analyzingInstrumentModal.setTitle("23.9 Instrument that is used to analyze the water samples collected with the 'sampling instrument', or the sensors that are\n" +
                " mounted on the 'sampling instrument' to measure the water body continuously. For example, a coulometer, winkler titrator,\n" +
                " spectrophotometer, pH meter, thermosalinograph, oxygen sensor, YSI Multiparameter Meter, etc is an analyzing instrument.\n" +
                " We encourage you to document as much details (such as the make, model, resolution, precisions, etc) of the instrument as\n" +
                " you can here.");
        detailedInformationModal.setTitle("23.13 Detailed description of the sampling and analyzing procedures, including calibration procedures, model number of the\n" +
                " instrument, etc.");
        fieldReplicateModal.setTitle("23.14 Repetition of sample collection and measurement, e.g., triplicate samples.");
        uncertaintyModal.setTitle("23.18\n" +
                " Uncertainty of the results (e.g., 1%, 2 μmol/kg), or any pieces of information that are related to the quality control of the \n" +
                " variable.");
        qualityFlagModal.setTitle("23.19 Describe what the quality control flags stand for, e.g., 2 = good value, 3 = questionable value, 4 = bad value. The use of\n" +
                " WOCE quality flags are recommended.");
        researcherNameModal.setTitle("23.21.1 The name of the PI, whose research team measured or derived this parameter.");
        researcherInstitutionModal.setTitle("23.21.2 The institution of the PI, whose research team measured or derived this parameter.");
        fullVariableNameModal.setTitle("The full name of the variable.");
        referenceMethodModal.setTitle("23.20 Citation for the alkalinity method.");

    }
    public Variable getTa() {
        return getCommonVariable();
    }
    public Variable fill(Variable ta) {
        fillCommonVariable(ta);
        return ta;
    }
    public ClickHandler saveIt = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            // For some reason this returns a "0" in debug mode.
            String valid = String.valueOf( form.validate());
            String warning = Constants.NOT_COMPLETE;
            NotifyType type = NotifyType.WARNING;
            if ( isDirty() && measured.getValue() == null  ) {
                valid = "false";
                warning = Constants.MEASURED;
                type = NotifyType.DANGER;
            }
            if ( isDirty() && observationDetail.getValue() == null ) {
                valid="false";
                warning = Constants.DETAILS;
                type = NotifyType.DANGER;
            }
            if ( valid.equals("false") ||
                    valid.equals("0")) {
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(type);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(warning, settings);
            } else {
                eventBus.fireEventFromSource(new SectionSave(getTa(), Constants.SECTION_TA), TaPanel.this);
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.SUCCESS);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.COMPLETE, settings);
            }
        }
    };

    public void reset() {
        form.reset();
    }
}
