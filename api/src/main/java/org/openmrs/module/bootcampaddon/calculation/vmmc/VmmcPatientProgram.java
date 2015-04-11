package org.openmrs.module.bootcampaddon.calculation.vmmc;

/**
 * Created by KIMANI on 6/11/2014.
 */

import org.openmrs.Program;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyacore.calculation.Filters;

import java.util.Collection;
import java.util.Map;

public class VmmcPatientProgram extends AbstractPatientCalculation{

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params, PatientCalculationContext context) {
        Program program = (params != null && params.containsKey("program")) ? (Program) params.get("program") : null;

        return passing(Calculations.activeEnrollment(program, Filters.alive(cohort, context), context));
    }
}

