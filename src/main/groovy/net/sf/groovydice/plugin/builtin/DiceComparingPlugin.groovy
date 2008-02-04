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
 * This plugin adds a useful method to check whether the dice of a roll
 * matches the given numbers.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class DiceComparingPlugin {

    /**
     * This closure adds new methods to the API. Examples: <p/>
     * <pre>
     * 3.d.same_as([1,3,5]) [1,5,3] // -> true
     * </pre>
     * @param api GroovyDiceAPI object.
     * @see net.sf.groovydice.plugin.GroovyDiceAPI
     */
    def dynamicMethods = { api ->

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
                throw new IllegalArgumentException("Argument not expected: $other")
            }

            dice.allDice.sort() == other.sort()
        }
    }

    /**
     * Returns a String representation of this object.
     * @return Plugin name.
     */
    String toString() {
        getClass().name
    }
}