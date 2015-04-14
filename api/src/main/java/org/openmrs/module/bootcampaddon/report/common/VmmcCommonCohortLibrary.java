package org.openmrs.module.bootcampaddon.report.common;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Program;
import org.openmrs.api.PatientSetService;
import org.openmrs.module.bootcampaddon.Dictionary;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.cohort.definition.CalculationCohortDefinition;
import org.openmrs.module.kenyacore.report.cohort.definition.DateObsValueBetweenCohortDefinition;
import org.openmrs.module.kenyaemr.calculation.library.InProgramCalculation;
import org.openmrs.module.reporting.cohort.definition.*;
import org.openmrs.module.reporting.common.SetComparator;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Created by KIMANI on 6/9/2014.
 */
@Component
public class VmmcCommonCohortLibrary {



    public CohortDefinition females() {
        GenderCohortDefinition cd = new GenderCohortDefinition();
        cd.setName("females");
        cd.setFemaleIncluded(true);
        return cd;
    }
    /**
     * Patients who are male
     * @return the cohort definition
     */
    public CohortDefinition males() {
        GenderCohortDefinition cd = new GenderCohortDefinition();
        cd.setName("males");
        cd.setMaleIncluded(true);
        return cd;
    }

    /**
     * Patients who at most maxAge years old on ${effectiveDate}
     * @return the cohort definition
     */
    public CohortDefinition agedAtMost(int maxAge) {
        AgeCohortDefinition cd = new AgeCohortDefinition();
        cd.setName("aged at most " + maxAge);
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.setMaxAge(maxAge);
        return cd;
    }

    /**
     * Patients who are at least minAge years old on ${effectiveDate}
     * @return the cohort definition
     */
    public CohortDefinition agedAtLeast(int minAge) {
        AgeCohortDefinition cd = new AgeCohortDefinition();
        cd.setName("aged at least " + minAge);
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.setMinAge(minAge);
        return cd;
    }

    /**
     * Patients who are male and at least 18 years old on ${effectiveDate}
     * @return the cohort definition
     */
    public CohortDefinition malesAgedAtLeast18() {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("Males aged at least 18");
        cd.addParameter(new Parameter("effectiveDate", "Effective Date", Date.class));
        cd.addSearch("males", ReportUtils.map(males()));
        cd.addSearch("agedAtLeast18", ReportUtils.map(agedAtLeast(18), "effectiveDate=${effectiveDate}"));
        cd.setCompositionString("males AND agedAtLeast18");
        return cd;
    }

    /**
     * Patients who have an encounter between ${onOrAfter} and ${onOrBefore}
     * @param types the encounter types
     * @return the cohort definition
     */
    public CohortDefinition hasEncounter(EncounterType... types) {
        EncounterCohortDefinition cd = new EncounterCohortDefinition();
        cd.setName("has encounter between dates");
        cd.setTimeQualifier(TimeQualifier.ANY);
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        if (types.length > 0) {
            cd.setEncounterTypeList(Arrays.asList(types));
        }
        return cd;
    }

    /**
     * Patients who have an obs between ${onOrAfter} and ${onOrBefore}
     * @param question the question concept
     * @param answers the answers to include
     * @return the cohort definition
     */
    public static CohortDefinition hasObs(Concept question, Concept... answers) {
        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
        cd.setName("has obs between dates");
        cd.setQuestion(question);
        cd.setOperator(SetComparator.IN);
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        if (answers.length > 0) {
            cd.setValueList(Arrays.asList(answers));
        }
        return cd;
    }

    /**
     * Patients who transferred in between ${onOrAfter} and ${onOrBefore}
     * @return the cohort definition
     */
    public CohortDefinition transferredIn() {
        Concept transferInDate = Dictionary.getConcept(Dictionary.TRANSFER_IN_DATE);

        DateObsValueBetweenCohortDefinition cd = new DateObsValueBetweenCohortDefinition();
        cd.setName("transferred in between dates");
        cd.setQuestion(transferInDate);
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        return cd;
    }

    /**
     * Patients who transferred in between ${onOrAfter} and ${onOrBefore}
     * @return the cohort definition
     */
    public CohortDefinition transferredOut() {
        Concept reasonForDiscontinue = Dictionary.getConcept(Dictionary.REASON_FOR_PROGRAM_DISCONTINUATION);
        Concept transferredOut = Dictionary.getConcept(Dictionary.TRANSFERRED_OUT);

        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.setName("transferred out between dates");
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.setQuestion(reasonForDiscontinue);
        cd.setOperator(SetComparator.IN);
        cd.setValueList(Collections.singletonList(transferredOut));
        return cd;
    }

    /**
     * Patients who were enrolled on the given programs between ${enrolledOnOrAfter} and ${enrolledOnOrBefore}
     * @param programs the programs
     * @return the cohort definition
     */
    public CohortDefinition enrolled(Program... programs) {
        ProgramEnrollmentCohortDefinition cd = new ProgramEnrollmentCohortDefinition();
        cd.setName("enrolled in program between dates");
        cd.addParameter(new Parameter("enrolledOnOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("enrolledOnOrBefore", "Before Date", Date.class));
        if (programs.length > 0) {
            cd.setPrograms(Arrays.asList(programs));
        }
        return cd;
    }

    /**
     * Patients who were enrolled on the given programs (excluding transfers) between ${onOrAfter} and ${onOrBefore}
     * @param programs the programs
     * @return the cohort definition
     */
    public CohortDefinition enrolledExcludingTransfers(Program... programs) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("enrolled excluding transfers in program between dates");
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("enrolled", ReportUtils.map(enrolled(programs), "enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
        cd.addSearch("transferIn", ReportUtils.map(transferredIn(), "onOrBefore=${onOrBefore}"));
        cd.setCompositionString("enrolled AND NOT transferIn");
        return cd;
    }

    /**
     * Patients who were enrolled on the given programs (excluding transfers) on ${onOrBefore}
     * @param programs the programs
     * @return the cohort definition
     */
    public CohortDefinition enrolledExcludingTransfersOnDate(Program... programs) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("enrolled excluding transfers in program on date in this facility");
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("enrolled", ReportUtils.map(enrolled(programs), "enrolledOnOrBefore=${onOrBefore}"));
        cd.addSearch("transferIn", ReportUtils.map(transferredIn(), "onOrBefore=${onOrBefore}"));
        cd.setCompositionString("enrolled AND NOT transferIn");
        return cd;

    }

    /**
     * Patients who are in the specified program on ${onDate}
     * @param program the program
     * @return
     */
    public CohortDefinition inProgram(Program program) {
        CalculationCohortDefinition cd = new CalculationCohortDefinition(new InProgramCalculation());
        cd.setName("in " + program.getName() + " on date");
        cd.addParameter(new Parameter("onDate", "On Date", Date.class));
        cd.addCalculationParameter("program", program);
        return cd;
    }

    /**
     * Patients who were dispensed the given medications between ${onOrAfter} and ${onOrBefore}
     * @param concepts the drug concepts
     * @return the cohort definition
     */
    public CohortDefinition medicationDispensed(Concept... concepts) {
        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
        cd.setName("dispensed medication between");
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.setQuestion(Dictionary.getConcept(Dictionary.MEDICATION_ORDERS));
        cd.setValueList(Arrays.asList(concepts));
        cd.setOperator(SetComparator.IN);
        return cd;
    }

}
