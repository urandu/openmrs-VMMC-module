package org.openmrs.module.bootcampaddon.report.VmmcReport;

import org.openmrs.Concept;
import org.openmrs.module.bootcampaddon.Dictionary;
import org.openmrs.module.bootcampaddon.report.common.VmmcCommonCohortLibrary;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by KIMANI on 6/10/2014.
 */
@Component
public class VmmcCohortLibrary {
    @Autowired
    private VmmcCommonCohortLibrary vmmcCommonCohortLibrary;

    public CohortDefinition vmmccohort(){
        Concept enrollment = Dictionary.getConcept(Dictionary.METHOD_OF_ENROLLMENT);
        Concept vmmcProgram = Dictionary.getConcept(Dictionary._VMMC);
        Concept aps = Dictionary.getConcept(Dictionary.ADULT_INPATIENT_SERVICE);
        return vmmcCommonCohortLibrary.hasObs(enrollment,vmmcProgram,aps);


    }
}
