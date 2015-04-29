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

/**
 * @author Pierre Thirouin
 *         Date: 30/04/15
 */
@Config
public class Application {

    private String name;

    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
