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
package net.sf.groovydice.plugin.builtin

import net.sf.groovydice.*

/**
 * This plugin adds support to dice expressions like '1.d6'.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class DiceExpressionPlugin {

    /** Configuration context. */
    def config

    /**
     * This closure is called when the context is initialized.
     * @param config Configuration context.
     */
    def onInitialize = { config ->
        this.config = config
    }

    /**
     * Create and roll a dice rolling command.
     * @param sides Number of sides of the dice.
     * @param rolls Number of dice to roll.
     * @return The dice rolling command .
     */
    def rollCommand(sides, rolls) {
        new DiceRollingCommand(config:config, sides:sides).roll(rolls)
    }

    /**
     * This closure adds new methods to the API.
     * @param api GroovyDiceAPI object.
     * @see net.sf.groovydice.plugin.GroovyDice.API
     */
    def dynamicMethods = { api ->

        /* 1.d(5) */
        api.add(method:/^(d|D)$/, to:api.numberClasses) { number, sides ->
            sides = sides ?: 6

            if (api.isNumber(sides)) {
                return rollCommand(sides.toFloat() as int, number)
            }
            else if (sides.equals('%')) {
                return rollCommand(100, number)
            }
            else if (api.isCommand(sides)) {
                return rollCommand(sides.sum, number)
            }
            throw new IllegalArgumentException("Invalid dice type: $sides")
        }

        /* 1.d5 */
        api.add(dynamicMethod:/^(d|D)\d+$/, to:api.numberClasses) { method, number ->
            number.d(method.substring(1).toInteger())
        }

        /* 1.'d%' and 1.pd */
        api.add(method:/^((p|P)(d|D)|(d|D)%)$/, to:api.numberClasses) { number ->
            number.d('%')
        }
    }
}