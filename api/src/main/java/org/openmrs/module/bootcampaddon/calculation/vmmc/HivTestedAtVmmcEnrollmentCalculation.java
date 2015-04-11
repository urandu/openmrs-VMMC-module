package org.openmrs.module.bootcampaddon.calculation.vmmc;//package org.openmrs.module.bootcampaddon.calculation.vmmc;

//import org.openmrs.Concept;
//import org.openmrs.Encounter;
//import org.openmrs.EncounterType;
//import org.openmrs.Program;
//import org.openmrs.calculation.BaseCalculation;
//import org.openmrs.calculation.patient.PatientCalculation;
//import org.openmrs.calculation.patient.PatientCalculationContext;
//import org.openmrs.calculation.result.CalculationResultMap;
//import org.openmrs.module.kenyacore.calculation.BooleanResult;
//import org.openmrs.module.kenyacore.calculation.Calculations;
//import org.openmrs.module.kenyacore.calculation.Filters;
//import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
//import org.openmrs.module.metadatadeploy.MetadataUtils;
//import org.openmrs.module.bootcampaddon.Dictionary;
//import org.openmrs.module.bootcampaddon.metadata.FollowupMetadata;
//
//import java.util.Collection;
//import java.util.Date;
//import java.util.Map;
//import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Program;
import org.openmrs.calculation.BaseCalculation;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyacore.calculation.Filters;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.bootcampaddon.MetadataUtils;
import org.openmrs.module.bootcampaddon.Dictionary;
import org.openmrs.module.bootcampaddon.metadata.FollowupMetadata;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
* Created by KIMANI on 6/6/2014.
*/
public class HivTestedAtVmmcEnrollmentCalculation extends BaseCalculation implements PatientCalculation {
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> stringObjectMap, PatientCalculationContext context) {

        Program vmmcProgram= MetadataUtils.existing(Program.class, FollowupMetadata._Program.VMMC);

        Set<Integer> alive = Filters.alive(cohort, context);
        Set<Integer> inVmmcProgram = Filters.inProgram(vmmcProgram, alive, context);

        CalculationResultMap hivStatusObs = Calculations.lastObs(Dictionary.getConcept(Dictionary.HIV_STATUS), inVmmcProgram, context);
        CalculationResultMap hivTestDateObs = Calculations.lastObs(Dictionary.getConcept(Dictionary.DATE_OF_HIV_DIAGNOSIS), inVmmcProgram, context);

        Concept notHivTestedConcept = Dictionary.getConcept(Dictionary.NOT_HIV_TESTED);

        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap crm = Calculations.lastEncounter(MetadataUtils.existing(EncounterType.class, FollowupMetadata._EncounterType.ENROLL), cohort, context);
        for (Integer ptId : cohort) {
            // Is patient alive and in MCH program?
            boolean hivTestedAtEnrollment = false;
            if (inVmmcProgram.contains(ptId)) {
                Concept hivStatus = EmrCalculationUtils.codedObsResultForPatient(hivStatusObs, ptId);
                Date hivTestDate = EmrCalculationUtils.datetimeObsResultForPatient(hivTestDateObs, ptId);
                if (hivStatus != null && !hivStatus.equals(notHivTestedConcept)) {
                    if (hivTestDate != null) {
                        Date enrollmentDate = ((Encounter) crm.get(ptId).getValue()).getDateCreated();
                        hivTestedAtEnrollment = (hivTestDate.before(enrollmentDate)
                                || hivTestDate.equals(enrollmentDate));
                    }
                }
            }
            ret.put(ptId, new BooleanResult(hivTestedAtEnrollment, this, context));
        }
        return ret;
    }
}
