package org.openmrs.module.bootcampaddon;


import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.MissingMetadataException;

/**
 * Created by KIMANI on 6/3/2014.
 */
public class Dictionary extends Metadata.Concept{
    public static Concept getConcept(String identifier) {
        Concept concept;

        if (identifier.contains(":")) {
            String[] tokens = identifier.split(":");
            concept = Context.getConceptService().getConceptByMapping(tokens[1].trim(), tokens[0].trim());
        }
        else {
            // Assume it's a UUID
            concept = Context.getConceptService().getConceptByUuid(identifier);
        }

        if (concept == null) {
            throw new MissingMetadataException(Concept.class, identifier);
        }

        // getConcept doesn't always return ConceptNumeric for numeric concepts
        if (concept.getDatatype().isNumeric() && !(concept instanceof ConceptNumeric)) {
            concept = Context.getConceptService().getConceptNumeric(concept.getId());

            if (concept == null) {
                throw new MissingMetadataException(ConceptNumeric.class, identifier);
            }
        }

        return concept;
    }

    /**
     * Convenience method to fetch a list of concepts
     * @param identifiers the concept identifiers
     * @return the concepts
     * @throws org.openmrs.module.metadatadeploy.MissingMetadataException if a concept could not be found
     */

}
