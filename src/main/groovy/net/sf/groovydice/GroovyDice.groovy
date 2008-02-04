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
package net.sf.groovydice

import net.sf.groovydice.plugin.*
import net.sf.groovydice.plugin.builtin.*

/**
 * Groovy Dice configuration context. This class contains references to other
 * important objects, like the plugin manager, the dynamic API and the random
 * number generator.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.0
 * @version 3
 */
class GroovyDice {

    /** Random number generator. */
    def numberGenerator = new SimpleRandomNumberGenerator()

    /** PluginManager instance. */
    final PluginManager pluginManager = new PluginManager()

    /** Groovy Dice's dynamic API. */
    final GroovyDiceAPI api = new GroovyDiceAPI()

    /**
     * Initialize the Groovy Dice engine.
     */
    void initialize() {
        preInitialize()
        pluginManager.onInitialize(this)
        postInitialize()
    }

    /**
     * Pre-initialization callback method. This implementation registers the
     * built-in plugins.
     */
    void preInitialize() {
        registerBuiltInPlugins()
    }

    /**
     * Post-initialization callback method. This implementation does nothing.
     */
    void postInitialize() {
        /* no-op */
    }

    /**
     * Register the built-in Groovy Dice plugins.
     * @see net.sf.groovydice.GroovyDice#registerPlugins(java.lang.Object)
     */
    void registerBuiltInPlugins() {
        registerPlugins([new DiceStatisticsPlugin(),
                         new DiceExpressionPlugin(),
                         new DiceArithmeticPlugin(),
                         new DiceModifierPlugin(),
                         new DiceFilterPlugin(),
                         new DiceComparingPlugin(),
                         new OddEvenPlugin()])
    }

    /**
     * Register the given plugin instances within the
     * <code>PluginManager</code> object.
     * @param plugins Plugin instances to register.
     * @see net.sf.groovydice.plugin.PluginManager#register(java.lang.Object)
     */
    void registerPlugins(plugins) {
        pluginManager.register(plugins)
    }
}