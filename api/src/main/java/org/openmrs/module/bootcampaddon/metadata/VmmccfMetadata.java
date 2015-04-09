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
public class VmmccfMetadata extends AbstractMetadataBundle {

    public static class _EncounterType {

        public static final String VMMCCF = "68979b6c-e4ab-11e3-a8ff-2c4138029b5d";
    }

    public static class _Form {

        public static final String VMMCCF = "6c1dbfb4-e4ab-11e3-b0e8-2c4138029b5d";
    }

    /**
     * @see org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle#install()
     */
    @Override
    public void install() {

        install(encounterType("Voluntary Medical Male Circumcision Client", "Voluntary Medical Male Circumcision Client Form", _EncounterType.VMMCCF));

        install(form("Voluntary Medical Male Circumcision Client Form", null, _EncounterType.VMMCCF, "1", _Form.VMMCCF));
    }
}