package org.openmrs.module.bootcampaddon.calculation.vmmc;

import org.openmrs.EncounterType;
import org.openmrs.calculation.BaseCalculation;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.bootcampaddon.MetadataUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Created by agnes on 6/5/14.
 */
public class EnrollmentDateCalculation extends BaseCalculation implements PatientCalculation {


    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> stringObjectMap, PatientCalculationContext context) {
        EncounterType encType = MetadataUtils.existing(EncounterType.class, "37e1bf6e-3365-4b5a-a4e7-908f4f129b95");
        CalculationResultMap lastEnc = Calculations.lastEncounter(encType, cohort, context);


        CalculationResultMap result = new CalculationResultMap();
        for (Integer ptId : cohort) {
                //if(lastEnc != null) {
                    Date tDate = EmrCalculationUtils.encounterResultForPatient(lastEnc,ptId).getEncounterDatetime();
               // }

            result.put(ptId, tDate == null ? null : new SimpleResult(tDate, null));

        }
        return result;
    }
}

//    //    @Override
//    public String toString() {
//        ret += this.getEncounterDatetime() == null ? "(no Date) " : this.getEncounterDatetime().toString() + " ";
//    }
//
//    private SingleValueConverter getEncounterDatetime() {
//    }
//}

//
//}

