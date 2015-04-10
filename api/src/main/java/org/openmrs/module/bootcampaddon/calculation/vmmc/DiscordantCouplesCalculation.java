package org.openmrs.module.bootcampaddon.calculation.vmmc;//package org.openmrs.module.bootcampaddon.calculation.vmmc;
//
//import org.openmrs.Program;
//import org.openmrs.calculation.patient.PatientCalculationContext;
//import org.openmrs.calculation.result.CalculationResultMap;
//import org.openmrs.module.bootcampaddon.Dictionary;
//import org.openmrs.module.bootcampaddon.calculation.VmmcAbtractPatientCalculation;
//import org.openmrs.module.bootcampaddon.metadata.FollowupMetadata;
//import org.openmrs.module.kenyacore.calculation.Calculations;
//import org.openmrs.module.kenyacore.calculation.Filters;
//import org.openmrs.module.kenyacore.report.cohort.definition.CalculationCohortDefinition;
//import org.openmrs.module.metadatadeploy.MetadataUtils;
//
//import java.util.Collection;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by KIMANI on 6/9/2014.
// */
//public class DiscordantCouplesCalculation extends VmmcAbtractPatientCalculation {
//    @Override
//    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> stringObjectMap, PatientCalculationContext context) {
//
//        Program vmmcProgram= MetadataUtils.existing(Program.class, FollowupMetadata._Program.VMMC);
//        Set<Integer> alive = Filters.alive(cohort, context);
//        Set<Integer> invmmcProgram = Filters.inProgram(vmmcProgram, alive, context);
//
////        CalculationResultMap discordant= C(Dictionary.getConcept(Dictionary.DISCORDANT_COUPLE));
//        CalculationResultMap partnerResult=Calculations.lastObs(Dictionary.getConcept(Dictionary.PARTNER_HIV_STATUS));
//
//        return coup;
//    }
//}
