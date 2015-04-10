package org.openmrs.module.bootcampaddon.calculation;

import org.openmrs.Program;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.bootcampaddon.MetadataUtils;
import org.openmrs.module.bootcampaddon.metadata.FollowupMetadata;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyacore.calculation.Filters;

import java.util.Collection;
import java.util.Map;

/**
 * Created by bildad on 09/06/14.
 */
public class InVmmcProgramCalculation extends AbstractPatientCalculation  {
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params, PatientCalculationContext context) {
        return passing(Calculations.activeEnrollment(MetadataUtils.existing(Program.class, FollowupMetadata._Program.VMMC), Filters.alive(cohort, context), context));
    }
}
