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

import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.stereotype.Component;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.encounterType;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.form;

/**
 * Example metadata bundle
 */
@Component
public class MedicalHistoryMetadata extends AbstractMetadataBundle {

    public static class _EncounterType {

        public static final String MEDICALHISTORY = "b02d9618-eae4-11e3-8861-2c4138029b5d";
        public static final String ELIGIBILITY = "7bb615c6-eaea-11e3-82f2-2c4138029b5d";
    }

    public static class _Form {

        public static final String MEDICALHISTORY = "bd964390-eae4-11e3-8ad5-2c4138029b5d";
        public static final String ELIGIBILITY = "7cbb0c06-eaea-11e3-8624-2c4138029b5d";
    }

    /**
     * @see org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle#install()
     */
    @Override
    public void install() {

        install(encounterType("VMMCC medical history", "VMMCC medical history Form", _EncounterType.MEDICALHISTORY));

        install(form("VMMCC medical history Form", null, _EncounterType.MEDICALHISTORY, "1", _Form.MEDICALHISTORY));

        install(encounterType("VMMCC eligibility and procedure", "VMMCC eligibility and procedure Form", _EncounterType.ELIGIBILITY));

        install(form("VMMCC eligibility and procedure Form", null, _EncounterType.MEDICALHISTORY, "1", _Form.ELIGIBILITY));
    }
}