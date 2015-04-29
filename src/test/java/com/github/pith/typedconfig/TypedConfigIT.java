/*
 * Copyright (c) 2015 by Pierre THIROUIN. All rights reserved.
 *
 * This file is part of TypedConfig, An Nuun plugin for class based configuration.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.github.pith.typedconfig;

import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.tests.it.NuunITRunner;
import io.nuun.kernel.tests.it.annotations.WithParams;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * @author Pierre Thirouin
 *         Date: 29/04/15
 */
@WithParams({Kernel.NUUN_ROOT_PACKAGE, "com.github.pith.typedconfig"})
@RunWith(NuunITRunner.class)
public class TypedConfigIT {

    @Inject
    private Application application;

    @Test
    public void testConfigClassInitialization() {
        Assertions.assertThat(application).isNotNull();
        Assertions.assertThat(application.getName()).isEqualTo("MyApplication");
        Assertions.assertThat(application.getVersion()).isEqualTo("1.0.0");
    }
}
