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

/**
 * This plugin provides a way to use modifiers to change the dice of a roll.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class DiceModifierPlugin {

    /**
     * Whether the given object is a dice modifier.
     * @param obj Object to check.
     * @return Whether the given object is a dice modifier.
     */
    private def isModifier(obj) {
        obj instanceof DiceModifier
    }

    /**
     * This closure adds new methods to the API.
     * @param api GroovyDiceAPI object.
     * @see net.sf.groovydice.plugin.GroovyDice.API
     */
    def dynamicMethods = { api ->

        /* 3.to_every_die */
        api.add(method:'to_every_die') { dice ->
            dice.allDice.sum().to_every_die
        }

        /* 3.to_each_die_if(xxx) */
        api.add(method:'to_each_die_if') { dice, condition ->
            dice.allDice.sum().to_each_die_if(condition)
        }

        /* 10.d + 1.to_every_die */
        api.add(method:'plus') { dice, other ->
            if (isModifier(other)) {
                return other.apply(dice.&plus)
            }
        }

        /* 10.d - 1.to_every_die */
        api.add(method:'minus') { dice, other ->
            if (isModifier(other)) {
                return dice + -other
            }
        }

        /* 10.d * 2.to_every_die */
        api.add(method:'multiply') { dice, other ->
            if (isModifier(other)) {
                return other.apply(dice.&multiply)
            }
        }

        /* 10.d / 2.to_every_die */
        api.add(method:'div') { dice, other ->
            if (isModifier(other)) {
                return other.apply(dice.&div)
            }
        }

        /* 10.d ** 2.to_every_die */
        api.add(method:'power') { dice, other ->
            if (isModifier(other)) {
                return other.apply(dice.&power)
            }
        }

        /* 10.d % 2.to_every_die */
        api.add(method:'mod') { dice, other ->
            if (isModifier(other)) {
                return other.apply(dice.&mod)
            }
        }

        /* 1.to_every_die */
        api.add(method:'to_every_die', to:api.numberClasses) { number ->
            new DiceModifier(modifier:number)
        }

        /* 1.to_each_die_if(...) */
        api.add(method:'to_each_die_if', to:api.numberClasses) { number, condition ->
            if (api.isCommand(condition)) {
                condition = condition.allDice
            }
            new DiceModifier(modifier:number, condition:condition)
        }
    }
}