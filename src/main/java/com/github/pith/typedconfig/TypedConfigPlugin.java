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


import com.google.common.collect.Lists;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.core.AbstractPlugin;
import io.nuun.plugin.configuration.common.NuunCommonConfigurationPlugin;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pierre Thirouin
 *         Date: 29/04/15
 */
public class TypedConfigPlugin extends AbstractPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(TypedConfigPlugin.class);

    private Map<Class<Object>, Object> configClassesMap;

    @Override
    public String pluginPackageRoot() {
        return "com.github.pith.typedconfig";
    }

    @Override
    public String name() {
        return "typed-config-plugin";
    }

    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        return classpathScanRequestBuilder().annotationType(Config.class).build();
    }

    @Override
    public Collection<Class<? extends Plugin>> dependentPlugins() {
        //noinspection unchecked
        return (Collection) Lists.newArrayList(NuunCommonConfigurationPlugin.class);
    }

    @Override
    public InitState init(InitContext initContext) {
        NuunCommonConfigurationPlugin propsPlugin = (NuunCommonConfigurationPlugin) initContext.dependentPlugins().iterator().next();
        Configuration configuration = propsPlugin.getConfiguration();

        Collection<Class<?>> configClasses = initContext.scannedClassesByAnnotationClass().get(Config.class);
        if (configuration != null) {
            configClassesMap = initConfigClasses(configClasses, configuration);
        } else if (!configClasses.isEmpty()) {
            LOGGER.warn("Some configuration classes need to be initialized but no configuration is found.");
        }

        return InitState.INITIALIZED;
    }

    private Map<Class<Object>, Object> initConfigClasses(Collection<Class<?>> configClasses, Configuration configuration) {
        Map<Class<Object>, Object> configClassesMap = new HashMap<Class<Object>, Object>();

        for (Class<?> configClass : configClasses) {
            try {
                Object configObject = configClass.newInstance();
                for (Method method : configClass.getDeclaredMethods()) {
                    if (method.getName().startsWith("get")) {
                        Object property = configuration.getProperty(configClass.getSimpleName().toLowerCase() + method.getName().substring(3).toLowerCase());
                        if (property != null) {
                            try {
                                Method setter = configClass.getDeclaredMethod("set" + method.getName().substring(3), method.getReturnType());
                                setter.setAccessible(true);
                                setter.invoke(configObject, property);
                            } catch (NoSuchMethodException e) {
                                throw new IllegalStateException("The TypedConfigPlugin can't initialize " + method.getName() + " because there is no associated setter.");
                            } catch (InvocationTargetException e) {
                                if (e.getCause() != null) {
                                    throw new IllegalStateException("Failed to initialize " + method.getName(), e.getCause());
                                }
                            }
                        }
                    }
                }
                //noinspection unchecked
                configClassesMap.put((Class<Object>) configClass, configObject);
            } catch (InstantiationException e) {
                throw new IllegalStateException("Failed to instantiate " + configClass, e.getCause());
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Failed to access the constructor of " + configClass, e.getCause());
            }
        }
        return configClassesMap;
    }

    @Override
    public Object nativeUnitModule() {
        if (configClassesMap != null && !configClassesMap.isEmpty()) {
            return new TypedConfigModule(configClassesMap);
        } else {
            return null;
        }
    }
}
