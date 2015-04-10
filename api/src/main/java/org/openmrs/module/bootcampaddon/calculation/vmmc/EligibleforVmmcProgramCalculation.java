package org.openmrs.module.bootcampaddon.calculation.vmmc;

import org.openmrs.calculation.BaseCalculation;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.Calculations;

import java.util.Collection;
import java.util.Map;

/**
 * Created by KIMANI on 6/6/2014.
 */
public class EligibleforVmmcProgramCalculation extends BaseCalculation implements PatientCalculation {


    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> stringObjectMap, PatientCalculationContext context) {
        CalculationResultMap ret = new CalculationResultMap();

        CalculationResultMap genders = Calculations.genders(cohort, context);

        for (int ptId : cohort) {
            boolean eligible = "M".equals(genders.get(ptId).getValue());

            ret.put(ptId, new BooleanResult(eligible, this));
        }
        return ret;
    }
}
