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

import com.google.inject.AbstractModule;

import java.util.Map;

/**
 * @author Pierre Thirouin
 *         Date: 29/04/15
 */
public class TypedConfigModule extends AbstractModule {

    private final Map<Class<Object>, Object> configClasses;

    public TypedConfigModule(Map<Class<Object>, Object> configClasses) {
        this.configClasses = configClasses;
    }

    @Override
    protected void configure() {
        for (Map.Entry<Class<Object>, Object> entry : configClasses.entrySet()) {
            bind(entry.getKey()).toInstance(entry.getValue());
        }
    }
}
