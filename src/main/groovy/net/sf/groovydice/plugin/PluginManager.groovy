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
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F.
 * Martins</a>
 * @since 1.3
 * @version 1
 */
class PluginManager {

    /** List of registered plugins. */
    final List pluginStack = []

    /**
     * Register the given plugin instances. A plugin is always added to
     * the top of the plugin stack to allow newly registered plugins to bypass
     * operations defined by previously registered plugins.
     * @param plugins Plugin instances to register.
     */
    void register(plugins) {
        if (plugins instanceof List) {
            pluginStack.addAll(0, plugins)
        }
        else {
            pluginStack.add(0, plugins)
        }
    }

    /**
     * Unregister plugins.
     * @param plugins If this parameter is null, then all registered plugins
     * are removed. If this parameter is a Class, then all plug-ins that
     * shares an is-a relationship with that class are removed. Otherwise,
     * the plug-in instance represented by this parameter is removed from
     * the stack.
     */
    void unregister(plugins=null) {
        if (!plugins) { // clear the plug-in stack
            pluginStack.clear()
        }
        else if (plugins instanceof Class) {
            pluginStack.collect{it}.each {
                if (plugins.isInstance(it)) {
                    pluginStack.remove(it)
                }
            }
        }
        else {
            pluginStack.remove(plugins)
        }
    }

    /**
     * Triggers the initialization of all registered plugins.
     * @param config Configuration context.
     * @see net.sf.groovydice.GroovyDice#initialize()
     */
    void onInitialize(config) {
        config.api.injectAspects()

        pluginStack.each { plugin ->
            try {
                plugin.dynamicMethods(config.api)
                plugin.onInitialize(config)
            }
            catch (MissingMethodException) { // no big deal
            }
        }
    }
}
