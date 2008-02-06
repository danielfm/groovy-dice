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
 * This plugin provides a simple way to perform dice filtering.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class DiceFilterPlugin {

    /**
     * This closure adds new methods to the API. Examples: <p/>
     * <pre>
     * 3.d.the(2.best)   // [1,3,5] -> Sides: 6 , Dice: [5,3] , Sum: 8
     * 3.d.only_if{it>3} // [1,3,5] -> Sides: 6 , Dice: [5] , Sum: 5
     * 3.best            // -> 3
     * 3.worst           // -> -3
     * 5.is_even         // -> false
     * 5.is_odd          // -> true
     * </pre>
     * @param api GroovyDiceAPI object.
     * @see net.sf.groovydice.plugin.GroovyDiceAPI
     */
    def dynamicMethods = { api ->

        /* 3.d.the(xxx) */
        api.add(method:'the') { dice, n ->
            if (n == 0) {
                throw new IllegalArgumentException("Invalid value. Value: $n")
            }

            if (n > 0) { // best
                if (!dice.allDice) return 0
                n = ((n > dice.allDice.size()) ? dice.allDice.size() : n)

                if (n == 1) {
                    return dice.allDice.sort()[-n]
                }
                else {
                    return dice.derive(dice.allDice.sort()[-1..-n])
                }
            }
            else { // worst
                if (!dice.allDice) return 0
                n = n.abs()
                n = ((n >= dice.allDice.size()) ? dice.allDice.size()-1 : n-1)

                if (n == 0) {
                    return dice.allDice.sort()[n]
                }
                else {
                    return dice.derive(dice.allDice.sort()[0..n])
                }
            }
        }

        /* 3.d.only_if(xxx) */
        api.add(method:'only_if') { dice, condition ->
            if (api.isCommand(condition)) {
                condition = condition.allDice
            }
            dice.derive(dice.allDice.grep(condition))
        }

        /* 3.best */
        api.add(method:'best', to:api.numberClasses) { number ->
            if (number < 1) {
                throw new IllegalArgumentException("You can only use the 'best' property on positive numbers. Value: $number")
            }
            number
        }

        /* 3.worst */
        api.add(method:'worst', to:api.numberClasses) { number ->
            if (number < 1) {
                throw new IllegalArgumentException("You can only use the 'worst' property on positive numbers. Value: $number")
            }
            -number
        }

        /* 5.is_even */
        api.add(method:'is_even', to:api.numberClasses) { number ->
            (number as int) % 2 == 0
        }

        /* 5.is_odd */
        api.add(method:'is_odd', to:api.numberClasses) { number ->
            (number as int) % 2 != 0
        }
    }
}