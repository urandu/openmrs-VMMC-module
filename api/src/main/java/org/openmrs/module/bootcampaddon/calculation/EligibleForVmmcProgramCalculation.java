package org.openmrs.module.bootcampaddon.calculation;

import org.openmrs.calculation.BaseCalculation;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.Calculations;

import java.util.Collection;
import java.util.Map;

//import org.openmrs.module.kenyacore.calculation.BooleanResult;

/**
 * Created by bildad on 04/06/14.
 */
public class EligibleForVmmcProgramCalculation extends BaseCalculation implements PatientCalculation{
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> stringObjectMap, PatientCalculationContext context) {
        CalculationResultMap el =new CalculationResultMap();
        CalculationResultMap genders = Calculations.genders(cohort, context);
        for (Integer ptid: cohort){

            boolean eligible ="M".equals(genders.get(ptid).getValue());
            el.put(ptid, new BooleanResult(eligible,this));
        }



        return el;
    }
}
