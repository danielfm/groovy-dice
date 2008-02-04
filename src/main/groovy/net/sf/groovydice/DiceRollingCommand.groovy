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

/**
 * This class implements the basic dice rolling functionality
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class DiceRollingCommand implements Comparable {

    /** Sides of the die. */
    def sides = 6

    /** Already rolled dice. */
    def allDice = []

    /** Configuration context. */
    def config


    /**
     * Alias method that returns a copy of rolled dice.
     * @return Array of <code>Numbers</code> that represents the rolled dice.
     */
    def getView() {
        allDice.collect{it}
    }

    /**
     * Roll dice for the given number of times.
     * @param n Optional parameter that specify the number of dice to roll.
     * Defaults to 1.
     * @return <code>this</code>.
     */
    def roll(n=1) {
        n.abs().times {
            allDice << config.numberGenerator.next(sides) * ((n < 0) ? -1 : 1)
        }
        this
    }

    /**
     * Create a new dice rolling command based on this one.
     * @param allDice Specify the set of already rolled dice.
     * @param sides Optional parameter to specify the number of sides.
     * @return New dice rolling command.
     */
    def derive(allDice, sides=0) {
        /* reflection used here to work nicely with subclasses */
        def command = this.class.newInstance()

        command.sides = (sides == 0) ? this.sides : sides
        command.allDice = allDice
        command.config = config
        command
    }

    /**
     * Returns whether the given parameter is found in this dice roll.
     * @param condition Can be any object accepted by <code>grep()</code> method,
     * like a number, an array, a range, a closure etc. You can also pass a
     * dice rolling command to use its dice as the condition.
     * @return Whether the given parameter is found in the dice of this roll.
     */
    def isCase(condition) {
         if (condition instanceof DiceRollingCommand) {
             condition = condition.sum
         }
         condition in allDice
    }

    /**
     * Whether the sum of this roll is equals to the sum of the given roll.
     * @param command Dice rolling command.
     * @return Whether the sum of this roll is equals to the sum of the given
     * roll.
     */
    boolean equals(command) {
        if (command instanceof DiceRollingCommand) {
            return this.compareTo(command) == 0
        }
        false
    }

    /**
     * Compare the sum of this roll to the sum of the given roll.
     * @param command Dice rolling command.
     * @return < 0 if the sum of this roll is lesser than the sum of the given
     * roll; = 0 if the sum of this roll is equals to the sum of the given roll;
     * > 0 if the sum of this roll is greater than the sum of the given roll.
     * @throws ClassCastException if the given object is not expected.
     */
    int compareTo(command) {
        if (command instanceof DiceRollingCommand) {
            return allDice.sum() <=> command.allDice.sum()
        }
        throw new ClassCastException("Object type not expected: ${command.class}")
    }

    /**
     * Simple hashCode method.
     * @return Hashcode.
     */
    int hashCode() {
        sides.hashCode() + allDice.hashCode()
    }

    /**
     * Default string representation of this object.
     * @return The sum of all dice.
     */
    String toString() {
        sum
    }
}
