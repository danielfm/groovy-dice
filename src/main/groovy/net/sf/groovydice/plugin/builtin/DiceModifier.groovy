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
 * This class is used to specify a modifier to each die of a dice rolling
 * command. A modifier can also hold a <em>condition</em> object, which is
 * used when applying the modifier in such a way that only matching dice are
 * modified.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F.
 * Martins</a>
 * @since 1.0
 * @version 4
 */
class DiceModifier {

    /** Modifier number */
    def modifier

    /** Condition used to apply the modifier selectively. */
    def condition

    /**
     * Set the condition, which restricts the modifier to be applied only to a
     * specific set of dice.
     * @param condition An object that express an condition which all dice must
     * fit in order to be 'modified'. Any object that fit into a
     * <code>grep()</code> call can be used here. You can also pass a dice
     * rolling command object to apply this modifier only to dice equals to
     * any dice of the given roll.
     */
    void setCondition(condition) {
        this.condition = condition
    }

    /**
     * Multiply the modifier value by '-1' when the unary '-' operator is used
     * in this object.
     * @return <code>this</code>.
     */
    def negative() {
        modifier *= -1
        this
    }

    /**
     * Apply this modifier using the given logic closure.
     * @param logic Closure which is used to apply this modifier. This closure
     * should be a method borrowed from a dice rolling command.
     * @return A new dice rolling command which contains the result of the
     * operation.
     */
    def apply(logic) {
        def command = logic.delegate

        command.derive(command.allDice.collect {
            if (condition && !it.grep(condition)) {
                return it
            }

            it.&"${logic.method}"(modifier) // go ahead!
        })
    }

    /**
     * String representation of this object.
     * @return Modifier number and condition.
     */
    String toString() {
        "Number:$modifier , Condition: $condition"
    }
}
