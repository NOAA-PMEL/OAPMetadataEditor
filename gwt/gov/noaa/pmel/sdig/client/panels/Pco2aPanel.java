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
 * Created by rhs on 3/22/17.
 */
public class Pco2aPanel extends CommonVariablePanel {
    public Pco2aPanel() {
        super();
        abbreviation.setText("pCO2");
        abbreviation.setEnabled(false);
        fullVariableName.setText("pCO2");
        fullVariableName.setEnabled(false);
        heading.setText("Enter the information for pCO2A.");
        fieldReplicate.setAllowBlank(true);
        fieldReplicateForm.setVisible(false);
        save.addClickHandler(saveIt);

        abbreviationPopover.setContent("25.1 Column header name of the variable in the data files, e.g., pCO2, etc.");
        observationTypePopover.setContent("25.2 How the variable is observed, e.g., surface underway, profile, time series, model output, etc. For experimental data, this could be: laboratory experiment, pelagic mesocosm, benthic mesocosm, benthic FOCE type studies, natural pertubration site studies, etc");
        manipulationMethodPopover.setContent("25.4 In perturbation experiments, seawater carbonate chemistry can be manipulated by different techniques, such as bubbling CO2, adding acids or bases, etc.");
        observationDetailPopover.setContent("25.3 Whether the variable belong to an in-situ observed variable, or a manipulation condition variable, or a response variable in a biological experimental study");
        measuredPopover.setContent("25.6 Whether the variable is measured in-situ, or calculated from other variables");
        calculationMethodPopover.setContent("25.7 Variables can be calculated using different sets of constants or different software.");
        samplingInstrumentPopover.setContent("25.8 Instrument that is used to collect water samples, or deploy sensors, etc. For example, a Niskin bottle, pump, CTD, etc is a sampling instrument.");
        analyzingInstrumentPopover.setContent("25.11 Instrument that is used to analyze the water samples collected with the sampling instrument , or the sensors that are mounted on the 'sampling instrument' to measure the water body continuously. For example, a coulometer, winkler titrator, spectrophotometer, pH meter, thermosalinograph, oxygen sensor, YSI Multiparameter Meter, etc is an analyzing instrument. We encourage you to document as much details (such as the make, model, resolution, precisions, etc) of the instrument as you can here");
        detailedInformationPopover.setContent("25.12 Detailed description of the sampling and analyzing procedures, including calibration procedures, model number of the instrument, etc.");
        fieldReplicatePopover.setContent("??? Does apply here ???");
        uncertaintyPopover.setContent("25.20 Uncertainty of the results (e.g., 1%, 2 μmol/kg), or any pieces of information that are related to the quality control of the variable.");
        qualityFlagPopover.setContent("25.21 Describe what the quality control flags stand for, e.g., 2 = good value, 3 = questionable value, 4 = bad value. The use of WOCE quality flags are recommended.");
        researcherNamePopover.setContent("25.23.1 The name of the PI, whose research team measured or derived this parameter.");
        researcherInstitutionPopover.setContent("25.23.2 The institution of the PI, whose research team measured or derived this parameter.");
        fullVariableNamePopover.setContent("Full variable name.");
        referenceMethodPopover.setContent("25.22 Citation for the pCO2 method.");

    }
    public Variable getPco2a() {
        return getCommonVariable();
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
                eventBus.fireEventFromSource(new SectionSave(getPco2a(), Constants.SECTION_PCO2A), Pco2aPanel.this);
                NotifySettings settings = NotifySettings.newSettings();
                settings.setType(NotifyType.SUCCESS);
                settings.setPlacement(NotifyPlacement.TOP_CENTER);
                Notify.notify(Constants.COMPLETE, settings);
            }        }
    };

    public void reset() {
        form.reset();
    }
}
