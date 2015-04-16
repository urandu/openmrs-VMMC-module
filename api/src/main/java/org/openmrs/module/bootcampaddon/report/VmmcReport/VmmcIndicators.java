package org.openmrs.module.bootcampaddon.report.builder.report.VmmcReport;//package org.openmrs.module.moh362.report.VmmcReport;
//
//import org.openmrs.module.reporting.indicator.CohortIndicator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import static org.openmrs.module.kenyacore.report.ReportUtils.map;
//import static org.openmrs.module.kenyaemr.reporting.EmrReportingUtils.cohortIndicator;
///**
// * Created by KIMANI on 6/10/2014.
// */
//@Component
//public class VmmcIndicators {
//    @Autowired
//    private VmmcCohortLibrary vmmcCohortlibrary;
//
//    public CohortIndicator vmmcIndicator(){
//        return cohortIndicator("testing indicator",
//                map(vmmcCohortlibrary.vmmccohort(), "onOrBefore=${startDate},onOrAfter=${endDate}"));
//    }
//
//}
