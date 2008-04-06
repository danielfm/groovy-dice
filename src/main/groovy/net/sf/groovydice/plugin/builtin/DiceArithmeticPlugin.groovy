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
 * This plugin enables the use of dice rolling commands within arithmetic
 * expressions.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F.
 * Martins</a>
 * @since 1.3
 * @version 1
 */
class DiceArithmeticPlugin {

    /**
     * This closure adds new methods to the API. Examples: <p/>
     * <pre>
     * 3.d + 3.d          // [1,3,5], [2,4,6] -> Sides: 6 , Dice: [1,3,5,2,4,6] , Sum: 21
     * 3.d - 3.d          // [1,3,5], [2,4,6] -> Sides: 6 , Dice: [1,3,5,-2,-4,-6] , Sum: 3
     * 3.d * 3.d          // [1,3,5], [2,4,6] -> 108
     * 3.d / 3.d          // [1,3,5], [2,4,6] -> 0.75
     * 3.d ** 3.d         // [1,3,5], [2,4,6] -> 282429536481
     * 3.d % 3.d          // [1,3,5], [2,4,6] -> 9
     * 3.d + 5            // [1,3,5] -> Sides: 6 , Dice: [1,3,5,5] , Sum: 14
     * 3.d - 5            // [1,3,5] -> Sides: 6 , Dice: [1,3,5,-5] , Sum: 4
     * 3.d * 5            // [1,3,5] -> 45
     * 3.d / 5            // [1,3,5] -> 4.5
     * 3.d ** 5           // [1,3,5] -> 59049
     * 3.d % 5            // [1,3,5] -> 4
     * -3.d               // [1,3,5] -> Sides: 6 , Dice: [-1,-3,-5] , Sum: -9
     * 5 + 3.d            // [1,3,5] -> 14
     * 5 - 3.d            // [1,3,5] -> -4
     * 5 * 3.d            // [1,3,5] -> 45
     * 5 / 3.d            // [1,3,5] -> 0.555556
     * 5 ** 3.d           // [1,3,5] -> 1953125
     * 5 % 3.d            // [1,3,5] -> 5
     * 2.d.same_as([1,3]) // [1,5,3] -> true
     * </pre>
     * @param api GroovyDiceAPI object.
     * @see net.sf.groovydice.plugin.GroovyDiceAPI
     */
    def dynamicMethods = { api ->

        /* 1.d + 5, 3.d + 3.d */
        api.add(method:'plus') { dice, other ->
            if (api.isCommand(other)) {
                return dice.derive(dice.allDice + other.allDice,
                    (dice.sides > other.sides) ? 0 : other.sides)
            }
            else if (api.isNumber(other)) {
                return dice.derive(dice.allDice + other)
            }
        }

        /* 1.d - 5, 3.d - 3.d */
        api.add(method:'minus') { dice, other ->
            if (api.isCommand(other)) {
                return dice.derive(dice.allDice + -other.allDice)
            }
            else if (api.isNumber(other)) {
                return dice + -other
            }
        }

        /* 1.d * 5, 3.d * 3.d */
        api.add(method:'multiply') { dice, other ->
            if (api.isCommand(other)) {
                return dice.allDice.sum() * other.allDice.sum()
            }
            else if (api.isNumber(other)) {
                return dice.allDice.sum() * other
            }
        }

        /* 1.d / 5, 3.d / 3.d */
        api.add(method:'div') { dice, other ->
            if (api.isCommand(other) || api.isNumber(other)) {
                return dice.allDice.sum() / other
            }
        }

        /* 1.d ** 5, 3.d ** 3.d */
        api.add(method:'power') { dice, other ->
            if (api.isCommand(other)) {
                return dice.allDice.sum() ** other.allDice.sum()
            }
            else if (api.isNumber(other)) {
                return dice.allDice.sum() ** other
            }
        }

        /* 1.d % 5, 3.d % 3.d */
        api.add(method:'mod') { dice, other ->
            if (api.isCommand(other)) {
                return dice.allDice.sum() % other.allDice.sum()
            }
            else if (api.isNumber(other)) {
                return dice.allDice.sum() % other
            }
        }

        /* -1.d */
        api.add(method:'negative') { dice ->
            dice.derive(dice.allDice.collect{-it})
        }

        /* 5 + 1.d */
        api.add(method:'plus', to:api.numberClasses) { number, other ->
            if (api.isCommand(other)) {
                return other + number
            }
        }

        /* 5 - 1.d */
        api.add(method:'minus', to:api.numberClasses) { number, other ->
            if (api.isCommand(other)) {
                return -other + number
            }
        }

        /* 5 * 1.d */
        api.add(method:'multiply', to:api.numberClasses) { number, other ->
            if (api.isCommand(other)) {
                return other * number
            }
        }

        /* 5 / 1.d */
        api.add(method:'div', to:api.numberClasses) { number, other ->
            if (api.isCommand(other)) {
                return number / other.allDice.sum()
            }
        }

        /* 5 ** 1.d */
        api.add(method:'power', to:api.numberClasses) { number, other ->
            if (api.isCommand(other)) {
                return number ** other.allDice.sum()
            }
        }

        /* 5 % 1.d */
        api.add(method:'mod', to:api.numberClasses) { number, other ->
            if (api.isCommand(other)) {
                return number % other.allDice.sum()
            }
        }

        /* 1.d.same_as(xxx) */
        api.add(method:'same_as') { dice, other ->
            if (api.isCommand(other)) {
                other = other.allDice
            }
            else if (api.isNumber(other)) {
                other = [other]
            }
            else if (other instanceof List) {
                other = other.collect{it}
            }
            else {
                throw new IllegalArgumentException(
                      "Argument not expected: $other")
            }

            dice.allDice.sort() == other.sort()
        }
    }
}
