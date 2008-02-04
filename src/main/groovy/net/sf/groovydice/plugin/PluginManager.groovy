/*
 * Copyright 2008 Daniel F. Martins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.groovydice.plugin

import net.sf.groovydice.plugin.builtin.*

/**
 * This class is responsible to manage the registered plugin instances.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class PluginManager {

    /** List of registered plugins. */
    final List pluginList = []

    /**
     * Register the given plugin instances. A plugin is always added to
     * the head of the plugin list to allow newly registered plugins to bypass
     * operations defined by previously registered plugins.
     * @param plugins Plugin instances to register.
     */
    void register(plugins) {
        if (plugins instanceof List) {
            pluginList.addAll(0, plugins)
        }
        else {
            pluginList.add(0, plugins)
        }
    }

    /**
     * Triggers the initialization of all registered plugins.
     * @param config Configuration context.
     * @see net.sf.groovydice.GroovyDice#initialize()
     */
    void onInitialize(config) {
        config.api.injectAspects()

        pluginList.each { plugin ->
            try {
                plugin.dynamicMethods(config.api)
                plugin.onInitialize(config)
            }
            catch (MissingMethodException) { // no big deal
            }
        }
    }
}