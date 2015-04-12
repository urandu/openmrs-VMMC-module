package org.openmrs.module.bootcampaddon.report.builder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyaemr.reporting.ColumnParameters;
import org.openmrs.module.kenyaemr.reporting.EmrReportingUtils;
import org.openmrs.module.kenyaemr.reporting.library.shared.common.CommonDimensionLibrary;
import org.openmrs.module.bootcampaddon.report.VmmcReport.VmmcIndicatorLibrary;
import org.openmrs.module.bootcampaddon.report.cohort.VmmcCohortsLibrary;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.openmrs.module.bootcampaddon.report.cohort.VmmcCohortsLibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.kenyacore.report.ReportUtils.map;

/**
 * Created by KIMANI on 6/11/2014.
 */
@Component
@Builds({"bootcampaddon.common.report.indicator.httcSummary"})
public class HtcVmmcReportBuilder extends AbstractReportBuilder {
    protected static final Log log = LogFactory.getLog(HtcVmmcReportBuilder.class);

    @Autowired
    private CommonDimensionLibrary commonDimensions;

    @Autowired
    private VmmcIndicatorLibrary vmmcIndicators;

    @Override
    protected List<Parameter> getParameters(ReportDescriptor descriptor) {
        return Arrays.asList(
                new Parameter("startDate", "Start Date", Date.class),
                new Parameter("endDate", "End Date", Date.class)
        );
    }

    @Override
    protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {
        return Arrays.asList(
                ReportUtils.map(createHtcDataSet(), "startDate=${startDate},endDate=${endDate}")
//                ReportUtils.map(createDataSet(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    private DataSetDefinition createHtcDataSet(){
        CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
        dsd.setName("H");
        dsd.setDescription("HTC and VMMC Department Facility Report");
        dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
        dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
        dsd.addDimension("gender", map(commonDimensions.gender()));

        List<ColumnParameters> columns = new ArrayList<ColumnParameters>();

        columns.add(new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15"));
        columns.add(new ColumnParameters("MM", "15-25 years, male", "gender=M|age=<25"));
        columns.add(new ColumnParameters("MA", ">25 years, male", "gender=M|age=25+"));
        columns.add(new ColumnParameters("T", "total", ""));

        String indParams = "startDate=${startDate},endDate=${endDate}";

        EmrReportingUtils.addRow(dsd,"H","First HIV Test Done",ReportUtils.map(vmmcIndicators.firstTest(), indParams),columns);
        EmrReportingUtils.addRow(dsd,"H","Second HIV Test Done",ReportUtils.map(vmmcIndicators.secondTest(), indParams),columns);
        EmrReportingUtils.addRow(dsd,"H","Discordant Couples",ReportUtils.map(vmmcIndicators.discordantCouples(), indParams),columns);
        EmrReportingUtils.addRow(dsd,"H","Males Tested for HIV at VMMC Clinic",ReportUtils.map(vmmcIndicators.testedForHiv(), indParams),columns);
        EmrReportingUtils.addRow(dsd,"H","Males who Completed VMMC Treatment",ReportUtils.map(vmmcIndicators.completedVMMCTreatment(), indParams),columns);
        EmrReportingUtils.addRow(dsd,"H","Males who Defaulted VMMC Treatment",ReportUtils.map(vmmcIndicators.defaulted(), indParams),columns);
        EmrReportingUtils.addRow(dsd,"H","Males who Tested HIV Positive at VMMC CLINIC",ReportUtils.map(vmmcIndicators.testedHivPositive(), indParams),columns);
        EmrReportingUtils.addRow(dsd,"H","Males who Tested HIV Negative at VMMC CLINIC",ReportUtils.map(vmmcIndicators.testedHivNegative(), indParams),columns);
        EmrReportingUtils.addRow(dsd,"H","Males whose HIV Status is Unknown at VMMC CLINIC",ReportUtils.map(vmmcIndicators.testedHivStatusUnknown(), indParams),columns);








        return dsd;
    }
}
