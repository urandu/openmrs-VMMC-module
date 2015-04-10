package org.openmrs.module.bootcampaddon.calculation.vmmc;

import org.openmrs.Concept;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.bootcampaddon.Dictionary;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Created by KIMANI on 6/11/2014.
 */
public class DateofEnrollmentCalculation extends AbstractPatientCalculation {
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> stringObjectMap, PatientCalculationContext context) {
        Concept enrollDate= Dictionary.getConcept(Dictionary.DATE_OF_CONFINEMENT);
        CalculationResultMap enrollDates= Calculations.lastObs(enrollDate,cohort,context);

        CalculationResultMap result = new CalculationResultMap();
        for (Integer ptId : cohort) {

            Date tDate = EmrCalculationUtils.datetimeObsResultForPatient(enrollDates, ptId);

            result.put(ptId, tDate == null ? null : new SimpleResult(tDate, null));

        }
        return result;
    }
}
