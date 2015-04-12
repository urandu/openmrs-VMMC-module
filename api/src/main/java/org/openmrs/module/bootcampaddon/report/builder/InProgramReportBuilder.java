package org.openmrs.module.bootcampaddon.report.builder.report.builder;

import org.openmrs.PatientIdentifierType;
import org.openmrs.module.bootcampaddon.MetadataUtils;
import org.openmrs.module.bootcampaddon.calculation.vmmc.DateofEnrollmentCalculation;
import org.openmrs.module.bootcampaddon.metadata.FollowupMetadata;
import org.openmrs.module.kenyacore.report.CohortReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.builder.CalculationReportBuilder;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.kenyaemr.reporting.data.converter.CalculationResultConverter;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ConvertedPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by KIMANI on 6/11/2014.
 */
@Component
@Builds({"bootcampaddon.common.report.inprogram","bootcampaddon.common.report.testedAtEnrollment","bootcampaddon.common.report.testedAtClinic"})
public class InProgramReportBuilder extends CalculationReportBuilder{

//    @Autowired
//    private VmmcCohortsLibrary vmmcCohorts;


    @Override
    protected List<Parameter> getParameters(ReportDescriptor descriptor) {
        return Arrays.asList(
                new Parameter("startDate", "Start Date", Date.class),
                new Parameter("endDate", "End Date", Date.class)
        );
    }


    @Override
    protected void addColumns (CohortReportDescriptor report, PatientDataSetDefinition dsd){
//        DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
//        DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);
//        dsd.addColumn("id", new PatientIdDataDefinition(), "");
//        dsd.addColumn("Name", nameDef, "");
//
//        dsd.addColumn("Birth date", new BirthdateDataDefinition(), "", new BirthdateConverter());
//        dsd.addColumn("Sex", new GenderDataDefinition(), "");
//        dsd.addColumn("Enrollment Date", new CalculationDataDefinition("Enrollment Date", new EnrollmentDateCalculation()), "", new CalculationResultConverter());
//        dsd.addColumn("");

        PatientIdentifierType cno = MetadataUtils.existing(PatientIdentifierType.class, FollowupMetadata._PatientIdentifierType.CLIENTNUMBER);
        DataConverter identifierFormatter = new ObjectFormatter("{clientIdentifier}");
        DataDefinition identifier = new ConvertedPatientDataDefinition("clientIdentifier", new PatientIdentifierDataDefinition(cno.getName(), cno), identifierFormatter);

        PatientIdentifierType tno = MetadataUtils.existing(PatientIdentifierType.class, FollowupMetadata._PatientIdentifierType.THEATRE_REGISTER_NUMBER);
        DataConverter identifierFormatters = new ObjectFormatter("{identifier}");
        DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(tno.getName(), tno), identifierFormatters);

        DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
        DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);

        dsd.addColumn("id", new PatientIdDataDefinition(), "");
        dsd.addColumn("Name", nameDef, "");
        dsd.addColumn(cno.getName(), identifier, "");
        dsd.addColumn(tno.getName(), identifierDef, "");
        dsd.addColumn("Birth date", new BirthdateDataDefinition(), "", new BirthdateConverter());
        dsd.addColumn("Sex", new GenderDataDefinition(), "");
        dsd.addColumn("Enrollment Date",new CalculationDataDefinition("Enrollment Date", new DateofEnrollmentCalculation()),"",new CalculationResultConverter());
    }

//    @Override
//    protected Mapped<CohortDefinition> buildCohort(CohortReportDescriptor descriptor, PatientDataSetDefinition dsd) {
//        int months = Integer.parseInt(descriptor.getId().split("\\.")[4]);
//        CohortDefinition cd = vmmcCohorts.netCohortMonths(months);
//        return ReportUtils.map(cd, "onDate=${endDate}");
//    }
}
