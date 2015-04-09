/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.bootcampaddon.metadata;

import org.openmrs.PatientIdentifierType;
import org.openmrs.module.bootcampaddon.Dictionary;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.stereotype.Component;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.*;

/**
 * Example metadata bundle
 */
@Component
public class FollowupMetadata extends AbstractMetadataBundle {

    public static class _EncounterType {


        public static final String EXAMPLE = "d69dedbd-3933-4e44-8292-bea939ce980a";
        public static final String VMMCCF = "68979b6c-e4ab-11e3-a8ff-2c4138029b5d";
        public static final String  ENROLL = "37e1bf6e-3365-4b5a-a4e7-908f4f129b95";
        public static final String FOLLOWUP = "aff22592-e6f2-11e3-8ea7-2c4138029b5d";
        public static final String  CIRCUMCISION = "b26ae96f-8deb-4f2e-bf82-be9bcea40350";

        public static final String  COMPLETE = "f1e8fe2d-531b-486b-b88a-8343a7385d4b";
    }

    public static class _Form {

        public static final String FOLLOWUP = "b3ac7520-e6f2-11e3-b973-2c4138029b5d";
        public static final String EXAMPLE = "b694b1bc-2086-47dd-a4ad-ba48f9471e4b";
        public static final String VMMCCF = "6c1dbfb4-e4ab-11e3-b0e8-2c4138029b5d";
    }

    public static class _PatientIdentifierType{
        public static final String CLIENTNUMBER ="1175607e-e7dc-11e3-95fe-2c4138029b5d";
        public static final String THEATRE_REGISTER_NUMBER ="53917e7e-eafd-11e3-af5d-2c4138029b5d";
    }

    public static class _Program{
        public static final String VMMC = "55d37fdf-3d76-4c1a-b3c8-e14906a586c8";
    }
    /**
     * @see org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle#install()
     */
    @Override
    public void install() {

       install(encounterType("VMMCC follow up", "VMMCC follow up Form", _EncounterType.FOLLOWUP));

        install(form("VMMCC follow up Form", null, _EncounterType.FOLLOWUP, "1", _Form.FOLLOWUP));

        install(patientIdentifierType("client number", "Assigned to every VMMCC patient",  null, "Facility code followed by sequential number",
                null, PatientIdentifierType.LocationBehavior.NOT_USED, false, _PatientIdentifierType.CLIENTNUMBER));
        install(patientIdentifierType("Theatre Register Number", "Assigned to every VMMCC patient",null, "as per facility",
                null, PatientIdentifierType.LocationBehavior.NOT_USED, false, _PatientIdentifierType.THEATRE_REGISTER_NUMBER));

        install(program("VMMC Program","Circumcision Enroll",Dictionary._VMMC, _Program.VMMC));

        install(encounterType("Example encounter", "Just an example", _EncounterType.EXAMPLE));

        install(form("Example form", null, _EncounterType.EXAMPLE, "1", _Form.EXAMPLE));

        install(encounterType("Voluntary Medical Male Circumcision Client", "Voluntary Medical Male Circumcision Client Form", _EncounterType.VMMCCF));

        install(form("Voluntary Medical Male Circumcision Client Form", null, _EncounterType.VMMCCF, "1", _Form.VMMCCF));

    }
}