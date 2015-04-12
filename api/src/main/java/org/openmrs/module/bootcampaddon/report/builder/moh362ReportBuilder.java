package org.openmrs.module.bootcampaddon.report.builder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyaemr.reporting.ColumnParameters;
import org.openmrs.module.kenyaemr.reporting.EmrReportingUtils;
import org.openmrs.module.bootcampaddon.report.VmmcReport.VmmcIndicatorLibrary;
import org.openmrs.module.bootcampaddon.report.common.VmmcCommonDimensionLibrary;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.kenyacore.report.ReportUtils.map;

/**
 * Created by KIMANI on 6/11/2014.
 */
@Component
@Builds({"bootcampaddon.common.report.indicator.moh362"})
public class moh362ReportBuilder extends AbstractReportBuilder{
    protected static final Log log = LogFactory.getLog(moh362ReportBuilder.class);

    @Autowired
//    private VmmcCommonDimensionLibrary vmmccommmonDimensions;
    private VmmcCommonDimensionLibrary commonDimensions;

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
                ReportUtils.map(createVmmcDataSet(), "startDate=${startDate},endDate=${endDate}")
//                ReportUtils.map(createDataSet(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    private DataSetDefinition createVmmcDataSet() {
        CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
        dsd.setName("G");
        dsd.setDescription("VMMC");
        dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
        dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
        dsd.addDimension("gender", map(commonDimensions.gender()));

        List<ColumnParameters> columns = new ArrayList<ColumnParameters>();
//        columns.add(new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15"));
        columns.add(new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15"));
//        columns.add(new ColumnParameters("FM", "15-25 years, female", "gender=F|age=<25"));
        columns.add(new ColumnParameters("MM", "15-25 years, male", "gender=M|age=<25"));
        columns.add(new ColumnParameters("MA", ">25 years, male", "gender=M|age=25+"));
//        columns.add(new ColumnParameters("FA", ">25 years, female", "gender=F|age=25+"));
        columns.add(new ColumnParameters("T", "total", ""));

        String indParams = "startDate=${startDate},endDate=${endDate}";

        EmrReportingUtils.addRow(dsd, "G1", "No. of Males Enrolled", ReportUtils.map(vmmcIndicators.enrolled(), indParams), columns);

        EmrReportingUtils.addRow(dsd, "G2", "No. of Males who completed treatment", ReportUtils.map(vmmcIndicators.completedVMMCTreatment(), indParams), columns);
        EmrReportingUtils.addRow(dsd, "G3", "No. of Enrollment cummulatively)", ReportUtils.map(vmmcIndicators.enrolledCumulative(), indParams), columns);
        EmrReportingUtils.addRow(dsd, "G4", "No. of Patients transferred to program)", ReportUtils.map(vmmcIndicators.enrolledExcludingTransfers(), indParams), columns);
        EmrReportingUtils.addRow(dsd, "G5", "No. of Patients tested for HIV and in VMMC Program)", ReportUtils.map(vmmcIndicators.testedForHivAndInVmmcProgram(), indParams), columns);
        EmrReportingUtils.addRow(dsd, "G6", "No. of TB and Tested for HIV (who are in VMMC program and tested for HIV)", ReportUtils.map(vmmcIndicators.inHivandTbProgramsandonVmmc(), indParams), columns);
        EmrReportingUtils.addRow(dsd, "G7", "No. of Patients Tested for HIV (whose HIV result is positive)", ReportUtils.map(vmmcIndicators.testedHivPositiveAndInVMMCProgram(), indParams), columns);
        EmrReportingUtils.addRow(dsd, "G9", "No. of VMMC defaulters (who defaulted or missed appointments)", ReportUtils.map(vmmcIndicators.defaulted(), indParams), columns);
//        EmrReportingUtils.addRow(dsd, "G10", "No. of TB completes (who Completed Tb Treatment)", ReportUtils.map(tbIndicators.completedTbTreatment(), indParams), columns);
//        EmrReportingUtils.addRow(dsd, "G11", "No. of TB deaths (who started tx this month last year)", ReportUtils.map(tbIndicators.diedAndStarted12MonthsAgo(), indParams), columns);

        return dsd;
    }


}
