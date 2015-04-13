package org.openmrs.module.bootcampaddon.report.cohort;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Program;
import org.openmrs.api.PatientSetService;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.cohort.definition.CalculationCohortDefinition;
import org.openmrs.module.kenyaemr.calculation.library.MissedLastAppointmentCalculation;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.kenyaemr.metadata.TbMetadata;
import org.openmrs.module.kenyaemr.reporting.library.shared.hiv.HivCohortLibrary;
import org.openmrs.module.bootcampaddon.MetadataUtils;
import org.openmrs.module.bootcampaddon.Dictionary;
import org.openmrs.module.bootcampaddon.metadata.FollowupMetadata;
import org.openmrs.module.bootcampaddon.report.common.VmmcCommonCohortLibrary;
import org.openmrs.module.reporting.cohort.definition.CodedObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.common.SetComparator;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Created by KIMANI on 6/11/2014.
 */
@Component
public class VmmcCohortsLibrary {
    @Autowired
    private VmmcCommonCohortLibrary commonCohorts;

    @Autowired
    private HivCohortLibrary hivCohortLibrary;

    public CohortDefinition enrolled() {
        return commonCohorts.enrolled(MetadataUtils.existing(Program.class, FollowupMetadata._Program.VMMC));
    }

    public CohortDefinition completedTreatment() {
        Concept vmmcTreatmentOutcome = Dictionary.getConcept(Dictionary._VMMC);
        Concept complete = Dictionary.getConcept(Dictionary.TREATMENT_COMPLETE);
        return commonCohorts.hasObs(vmmcTreatmentOutcome, complete);
    }

    public CohortDefinition defaulted() {
        CalculationCohortDefinition cd = new CalculationCohortDefinition(new MissedLastAppointmentCalculation());
        cd.setName("defaulted");
        cd.addParameter(new Parameter("onDate", "On Date", Date.class));
        return cd;
    }

    public CohortDefinition inHivandTbProgramsandonVmmc(){
        Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);
        Program tbProgram = MetadataUtils.existing(Program.class, TbMetadata._Program.TB);
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        Concept[] combo = { Dictionary.getConcept(Dictionary._VMMC) };
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("inHivProgram", ReportUtils.map(commonCohorts.inProgram(hivProgram), "onDate=${onOrBefore}"));
        cd.addSearch("inTbProgram", ReportUtils.map(commonCohorts.inProgram(tbProgram), "onDate=${onOrBefore}"));
        cd.addSearch("onVmmc", ReportUtils.map(commonCohorts.medicationDispensed(combo), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("inHivProgram AND inTbProgram AND onVmmc");
        return cd;

    }

    public CohortDefinition hasHivVisit() {
        EncounterType vmmcEnrollment = MetadataUtils.existing(EncounterType.class, FollowupMetadata._EncounterType.ENROLL);
        EncounterType vmmcConsultation = MetadataUtils.existing(EncounterType.class, FollowupMetadata._EncounterType.CIRCUMCISION);
        return commonCohorts.hasEncounter(vmmcEnrollment, vmmcConsultation);
    }


    public CohortDefinition referredFrom(Concept... entryPoints) {
        EncounterType vmmcEnrollEncType = MetadataUtils.existing(EncounterType.class, FollowupMetadata._EncounterType.ENROLL);
        Concept methodOfEnrollment = Dictionary.getConcept(Dictionary.METHOD_OF_ENROLLMENT);

        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
        cd.setName("referred from");
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.setQuestion(methodOfEnrollment);
        cd.setValueList(Arrays.asList(entryPoints));
        cd.setOperator(SetComparator.IN);
        cd.setEncounterTypeList(Collections.singletonList(vmmcEnrollEncType));
        return cd;
    }

    public CohortDefinition referredNotFrom(Concept... entryPoints) {
        EncounterType vmmcEnrollEncType = MetadataUtils.existing(EncounterType.class, FollowupMetadata._EncounterType.ENROLL);
        Concept methodOfEnrollment = Dictionary.getConcept(Dictionary.METHOD_OF_ENROLLMENT);

        CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
        cd.setName("referred not from");
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
        cd.setQuestion(methodOfEnrollment);
        cd.setValueList(Arrays.asList(entryPoints));
        cd.setOperator(SetComparator.NOT_IN);
        cd.setEncounterTypeList(Collections.singletonList(vmmcEnrollEncType));
        return cd;
    }


    public CohortDefinition enrolledExcludingTransfers() {
        return commonCohorts.enrolledExcludingTransfers(MetadataUtils.existing(Program.class, FollowupMetadata._Program.VMMC));
    }

    public CohortDefinition enrolledExcludingTransfersAndReferredFrom(Concept... entryPoints) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("enrolled excluding transfers in VMMC care from entry points");
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("enrolledExcludingTransfers", ReportUtils.map(enrolledExcludingTransfers(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("referredFrom", ReportUtils.map(referredFrom(entryPoints), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("enrolledExcludingTransfers AND referredFrom");
        return cd;
    }

    public CohortDefinition enrolledExcludingTransfersAndNotReferredFrom(Concept... entryPoints) {
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("enrolled excluding transfers in VMMC care not from entry points");
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("enrolledExcludingTransfers", ReportUtils.map(enrolledExcludingTransfers(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("referredNotFrom", ReportUtils.map(referredNotFrom(entryPoints), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("enrolledExcludingTransfers AND referredNotFrom");
        return cd;
    }

    public CohortDefinition testedForHivAndInVmmcProgram() {

        Program vmmcProgram = MetadataUtils.existing(Program.class, FollowupMetadata._Program.VMMC);
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("inVMMCProgram", ReportUtils.map(commonCohorts.inProgram(vmmcProgram), "onDate=${onOrBefore}"));
        cd.addSearch("hivTested", ReportUtils.map(hivCohortLibrary.testedForHiv(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("inVMMCProgram AND hivTested");
        return cd;
    }

    public CohortDefinition testedHivPositiveAndInVMMCProgram() {
        Program tbProgram = MetadataUtils.existing(Program.class, FollowupMetadata._Program.VMMC);
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("inVMMCProgram", ReportUtils.map(commonCohorts.inProgram(tbProgram), "onDate=${onOrBefore}"));
        cd.addSearch("hivTestedPositive", ReportUtils.map(hivCohortLibrary.testedHivPositive(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("inVMMCProgram AND hivTestedPositive");
        return cd;
    }

    public  CohortDefinition testedForHiv() {
        Concept hivStatus = Dictionary.getConcept(Dictionary.HIV_STATUS);
        Concept indeterminate = Dictionary.getConcept(Dictionary.INDETERMINATE);
        Concept hivInfected = Dictionary.getConcept(Dictionary.HIV_INFECTED);
        Concept unknown = Dictionary.getConcept(Dictionary.UNKNOWN);
        Concept positive = Dictionary.getConcept(Dictionary.POSITIVE);
        Concept negative = Dictionary.getConcept(Dictionary.NEGATIVE);
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("tested for HIV");
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("resultOfHivTest", ReportUtils.map(commonCohorts.hasObs(hivStatus, unknown, positive, negative), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("testedForHivHivInfected", ReportUtils.map(commonCohorts.hasObs(hivInfected, indeterminate,positive,negative), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("resultOfHivTest OR testedForHivHivInfected");
        return cd;
    }

    public  CohortDefinition testedHivPositive() {
        Concept hivStatus = Dictionary.getConcept(Dictionary.HIV_STATUS);
        Concept hivInfected = Dictionary.getConcept(Dictionary.HIV_INFECTED);
        Concept positive = Dictionary.getConcept(Dictionary.POSITIVE);
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("tested for positive for HIV");
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("resultOfHivTestPositive", ReportUtils.map(commonCohorts.hasObs(hivStatus, positive), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("testedForHivHivInfectedPositive", ReportUtils.map(commonCohorts.hasObs(hivInfected ,positive), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("resultOfHivTestPositive OR testedForHivHivInfectedPositive");
        return cd;
    }

    public  CohortDefinition testedHivNegative() {
        Concept hivStatus = Dictionary.getConcept(Dictionary.HIV_STATUS);
        Concept hivInfected = Dictionary.getConcept(Dictionary.HIV_INFECTED);
        Concept negative = Dictionary.getConcept(Dictionary.NEGATIVE);
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("tested negative for HIV");
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("resultOfHivTestNegative", ReportUtils.map(commonCohorts.hasObs(hivStatus, negative), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("testedForHivHivInfectedNegative", ReportUtils.map(commonCohorts.hasObs(hivInfected ,negative), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("resultOfHivTestNegative OR testedForHivHivInfectedNegative");
        return cd;
    }

    public  CohortDefinition testedHivStatusUnknown() {
        Concept hivStatus = Dictionary.getConcept(Dictionary.HIV_STATUS);
        Concept hivInfected = Dictionary.getConcept(Dictionary.HIV_INFECTED);
        Concept unknown = Dictionary.getConcept(Dictionary.UNKNOWN);
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.setName("tested unknown for HIV");
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("resultOfHivStatusUnknown", ReportUtils.map(commonCohorts.hasObs(hivStatus, unknown), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("testedForHivHivStatusUnknown", ReportUtils.map(commonCohorts.hasObs(hivInfected ,unknown), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("resultOfHivStatusUnknown OR testedForHivHivStatusUnknown");
        return cd;
    }

    public CohortDefinition discordantCouples(){
        Concept hivStatus=Dictionary.getConcept(Dictionary.HIV_STATUS);
        Concept hivInfected=Dictionary.getConcept(Dictionary.HIV_INFECTED);
        Concept partner=Dictionary.getConcept(Dictionary.PARTNER_HIV_STATUS);
        Concept couple=Dictionary.getConcept(Dictionary.DISCORDANT_COUPLE);
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.setName("discordant couple");
        cd.addSearch("resultOfHivTest", ReportUtils.map(commonCohorts.hasObs(hivStatus, partner, couple), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("discordant couple", ReportUtils.map(commonCohorts.hasObs(hivInfected, partner,couple), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setCompositionString("resultOfHivTest OR discordant couple");
        return cd;
    }

    public CohortDefinition firstTest(){
        Concept hivStatus=Dictionary.getConcept(Dictionary.HIV_STATUS);
        Concept result=Dictionary.getConcept(Dictionary.HIV_TEST_RESULT);
        Concept firstTest=Dictionary.getConcept(Dictionary.HIV_RAPID_TEST_1_QUALITATIVE);

        Concept unknown = Dictionary.getConcept(Dictionary.UNKNOWN);
        Concept positive = Dictionary.getConcept(Dictionary.POSITIVE);
        Concept negative = Dictionary.getConcept(Dictionary.NEGATIVE);
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd .addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("firstTest", ReportUtils.map(commonCohorts.hasObs(hivStatus,unknown,positive,negative),"onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("firstTestResult", ReportUtils.map(commonCohorts.hasObs(result, firstTest,positive,negative),"onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setName("firstHIVTest");
        cd.setCompositionString("firstTest Or firstTestResult");
        return cd;
    }

    public CohortDefinition secondTest(){
        Concept hivStatus=Dictionary.getConcept(Dictionary.HIV_STATUS);
        Concept result=Dictionary.getConcept(Dictionary.HIV_TEST_RESULT);

        Concept secondTest=Dictionary.getConcept(Dictionary.HIV_RAPID_TEST_2_QUALITATIVE);
        Concept unknown = Dictionary.getConcept(Dictionary.UNKNOWN);
        Concept positive = Dictionary.getConcept(Dictionary.POSITIVE);
        Concept negative = Dictionary.getConcept(Dictionary.NEGATIVE);
        CompositionCohortDefinition cd = new CompositionCohortDefinition();
        cd .addParameter(new Parameter("onOrAfter", "After Date", Date.class));
        cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
        cd.addSearch("secondTest", ReportUtils.map(commonCohorts.hasObs(hivStatus,unknown,positive,negative),"onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.addSearch("secondTestResult", ReportUtils.map(commonCohorts.hasObs(result, secondTest,positive,negative),"onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
        cd.setName("secondHIVTest");
        cd.setCompositionString("secondTest Or secondTestResult");
        return cd;
    }

}
