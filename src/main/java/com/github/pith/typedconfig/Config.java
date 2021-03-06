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

import java.lang.annotation.*;

/**
 * @author Pierre Thirouin
 *         Date: 29/04/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Config {
}
