package org.openmrs.module.bootcampaddon.report.VmmcReport;

import org.openmrs.Concept;
import org.openmrs.Program;
import org.openmrs.module.bootcampaddon.MetadataUtils;
import org.openmrs.module.bootcampaddon.metadata.FollowupMetadata;
import org.openmrs.module.bootcampaddon.report.cohort.VmmcCohortsLibrary;
import org.openmrs.module.kenyaemr.reporting.library.shared.common.CommonIndicatorLibrary;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.openmrs.module.kenyacore.report.ReportUtils.map;
import static org.openmrs.module.kenyaemr.reporting.EmrReportingUtils.cohortIndicator;

/**
 * Created by KIMANI on 6/9/2014.
 */
@Component
public class VmmcIndicatorLibrary {
    @Autowired
    private CommonIndicatorLibrary commonIndicators;

    @Autowired
    private VmmcCohortsLibrary vmmcCohorts;

    public CohortIndicator enrolled(){
        return commonIndicators.enrolled(MetadataUtils.existing(Program.class, FollowupMetadata._Program.VMMC));
    }

    public CohortIndicator completedVMMCTreatment() {
        return cohortIndicator("patients who completed VMMC treatment",
                map(vmmcCohorts.completedTreatment(), "onOrAfter=${startDate},onOrBefore=${endDate}")
        );
    }

    public CohortIndicator defaulted(){
        return cohortIndicator("patients who defaulted treatment",
                map(vmmcCohorts.defaulted(),"onDate"));
    }

    public CohortIndicator enrolledExcludingTransfers() {
        return commonIndicators.enrolledExcludingTransfers(MetadataUtils.existing(Program.class, FollowupMetadata._Program.VMMC));
    }

    public CohortIndicator enrolledCumulative() {
        return commonIndicators.enrolledCumulative(MetadataUtils.existing(Program.class, FollowupMetadata._Program.VMMC));
    }

    public CohortIndicator enrolledExcludingTransfersAndReferredFrom(Concept... entryPoints) {
        return cohortIndicator("newly enrolled patients referred from",
                map(vmmcCohorts.enrolledExcludingTransfersAndReferredFrom(entryPoints), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }

    public CohortIndicator enrolledExcludingTransfersAndNotReferredFrom(Concept... entryPoints) {
        return cohortIndicator("newly enrolled patients referred from",
                map(vmmcCohorts.enrolledExcludingTransfersAndNotReferredFrom(entryPoints), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }

    public CohortIndicator inHivandTbProgramsandonVmmc(){
        return cohortIndicator("inHivProgram AND inTbProgram AND onVmmc",
                map(vmmcCohorts.inHivandTbProgramsandonVmmc(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
    }

    public CohortIndicator testedForHivAndInVmmcProgram(){
        return cohortIndicator("inVMMCProgram AND hivTested",
                map(vmmcCohorts.testedForHivAndInVmmcProgram(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
    }

    public CohortIndicator testedHivPositiveAndInVMMCProgram(){
        return cohortIndicator("inVMMCProgram AND hivTestedPositive",
                map(vmmcCohorts.testedHivPositiveAndInVMMCProgram(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
    }

    public CohortIndicator testedForHiv(){
        return cohortIndicator("resultOfHivTest OR testedForHivHivInfected",
                map(vmmcCohorts.testedForHiv(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore"));
    }

    public CohortIndicator testedHivPositive(){
        return cohortIndicator("resultOfHivTestPositive OR testedForHivHivInfectedPositive",
                map(vmmcCohorts.testedHivPositive(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
    }

    public CohortIndicator testedHivNegative(){
        return cohortIndicator("resultOfHivTestNegative OR testedForHivHivInfectedNegative",
                map(vmmcCohorts.testedHivNegative(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
    }

    public CohortIndicator testedHivStatusUnknown(){
        return cohortIndicator("resultOfHivStatusUnknown OR testedForHivHivStatusUnknown",
                map(vmmcCohorts.testedHivStatusUnknown(),"onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
    }

    public CohortIndicator discordantCouples(){
        return cohortIndicator("resultOfHivTest OR discordant couple",map(vmmcCohorts.discordantCouples(),"onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));

    }

    public CohortIndicator firstTest(){
        return cohortIndicator("firstTest Or firstTestResult",map(vmmcCohorts.firstTest(),"onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
    }

    public CohortIndicator secondTest(){
        return cohortIndicator("secondTest Or secondTestResult",map(vmmcCohorts.secondTest(),"onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
    }
}
