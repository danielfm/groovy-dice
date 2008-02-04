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
 * This class is responsible to manage the Groovy Dice plugins.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class PluginManager {

    /** List of registered plugins. */
    def pluginList = []

    /** Groovy Dice's dynamic methods dictionary. */
    def api = new GroovyDiceAPI()

    /**
     * Register the built-in Groovy Dice plugins.
     */
    void registerBuiltInPlugins() {
        register([new OddEvenPlugin(),
                  new DiceStatisticsPlugin(),
                  new DiceModifierPlugin(),
                  new DiceFilterPlugin(),
                  new DiceComparingPlugin(),
                  new DiceArithmeticPlugin(),
                  new DiceExpressionPlugin()])
    }

    /**
     * Register the given plugins.
     * @param plugins Plugins to register.
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
     */
    void onInitialize(config) {
        api.injectAspects()

        pluginList.each { plugin ->
            try {
                plugin.dynamicMethods(api)
                plugin.onInitialize(config)
            }
            catch (MissingMethodException) { // no big deal
            }
        }
    }
}