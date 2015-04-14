package org.openmrs.module.bootcampaddon.report.common;

import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.openmrs.module.kenyacore.report.ReportUtils.map;

/**
 * Created by KIMANI on 6/9/2014.
 */
@Component
public class VmmcCommonDimensionLibrary {
    @Autowired
    private VmmcCommonCohortLibrary commonCohortLibrary;

    /**
     * Gender dimension
     * @return the dimension
     */
    public CohortDefinitionDimension gender() {
        CohortDefinitionDimension dim = new CohortDefinitionDimension();
        dim.setName("gender");
        dim.addCohortDefinition("M", map(commonCohortLibrary.males()));
        dim.addCohortDefinition("F", map(commonCohortLibrary.females()));
        return dim;
    }

    /**
     * Dimension of age using the 3 standard age groups
     * @return the dimension
     */
    public CohortDefinitionDimension standardAgeGroups() {
        CohortDefinitionDimension dim = new CohortDefinitionDimension();
        dim.setName("age groups (<15, <25, 25+)");
        dim.addParameter(new Parameter("onDate", "Date", Date.class));
        dim.addCohortDefinition("<15", map(commonCohortLibrary.agedAtMost(14), "effectiveDate=${onDate}"));
        dim.addCohortDefinition("<25", map(commonCohortLibrary.agedAtMost(24), "effectiveDate=${onDate}"));
        dim.addCohortDefinition("25+", map(commonCohortLibrary.agedAtLeast(25), "effectiveDate=${onDate}"));
        return dim;
    }
}
