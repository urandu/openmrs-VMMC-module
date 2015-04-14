package org.openmrs.module.bootcampaddon.report.builder.report.common;//package org.openmrs.module.moh362.report.common;
//
//import org.openmrs.Program;
//import org.openmrs.module.kenyacore.report.ReportUtils;
//import org.openmrs.module.reporting.indicator.CohortIndicator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import static org.openmrs.module.kenyaemr.reporting.EmrReportingUtils.cohortIndicator;
//
///**
// * Created by KIMANI on 6/9/2014.
// */
//@Component
//public class VmmcCommonIndicatorLibrary {
//    @Autowired
//    private VmmcCommonCohortLibrary vmmccommonCohorts;
//
//    /**
//     * Number of patients enrolled in the given program (including transfers)
//     * @param program the program
//     * @return the indicator
//     */
//    public CohortIndicator enrolled(Program program) {
//        return cohortIndicator("new patients enrolled in " + program.getName() + " including transfers",
//                ReportUtils.map(vmmccommonCohorts.enrolledExcludingTransfers(program), "onOrAfter=${startDate},onOrBefore=${endDate}"));
//    }
//
//    /**
//     * Number of patients enrolled in the given program (excluding transfers)
//     * @param program the program
//     * @return the indicator
//     */
//    public CohortIndicator enrolledExcludingTransfers(Program program) {
//        return cohortIndicator("new patients enrolled in " + program.getName() + " excluding transfers",
//                ReportUtils.map(commonCohorts.enrolledExcludingTransfers(program), "onOrAfter=${startDate},onOrBefore=${endDate}"));
//    }
//
//    /**
//     * Number of patients ever enrolled in the given program (including transfers) up to ${endDate}
//     * @param program the program
//     * @return the indicator
//     */
//    public CohortIndicator enrolledCumulative(Program program) {
//        return cohortIndicator("total patients ever enrolled in " + program.getName() + " excluding transfers",
//                ReportUtils.map(commonCohorts.enrolledExcludingTransfersOnDate(program), "onOrBefore=${endDate}"));
//    }
//}
