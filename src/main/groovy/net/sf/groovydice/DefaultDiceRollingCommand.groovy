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
 * This class adds some goodies to the <code>AbstractDiceRollingCommand</code>
 * class, like arithmetic operators support, dice comparison and modifiers.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class DefaultDiceRollingCommand extends AbstractDiceRollingCommand {

    /**
     * Create a simple die modifier object using the value returned
     * by <code>getSum()</code> method.
     * @return A die modifier object, which is used to apply a
     * modifier to rolled dice.
     */
    def getTo_every_die() {
        sum.to_every_die
    }

    /**
     * Create a conditional die modifier object using the value
     * returned by <code>getSum()</code> method.
     * @param condition Can be any object accepted by <code>grep()</code>
     * method, like a number, an array, a range, a closure etc. You can also
     * pass a dice rolling command to use its dice as the condition.
     * @return A die modifier object, which is used to apply a modifier to
     * dice that matches the given condition.
     */
    def to_each_die_if(condition) {
        sum.to_each_die_if(condition)
    }

    /**
     * Returns a new dice rolling command that contains only the dice that
     * match the given condition.
     * @param condition Can be any object accepted by <code>grep()</code> method,
     * like a number, an array, a range, a closure etc. You can also pass a dice
     * rolling command to use its dice as the condition.
     * @return New dice rolling command that contains only the dice that match
     * the given condition.
     */
    def only_if(condition) {
        if (condition instanceof AbstractDiceRollingCommand) {
            condition = condition.allDice
        }
        deriveCommand(allDice.grep(condition))
    }

    /**
     * Whether this roll contains the same results of the given roll regardless
     * its order.
     * @param command Dice rolling specification, <code>Number</code>,
     * <code>List</code> or <code>Range</code> object, which contains the results
     * to be checked.
     * @return Whether this roll contains the same results of the given roll
     * regardless its order.
     */
    def same_as(command) {
        def dice

        switch(command) {
        case Number:
            dice = [command]
            break
        case List:
            dice = command.collect{it}
            break
        case AbstractDiceRollingCommand:
            dice = command.allDice
            break
        default:
            throw new IllegalArgumentException("${command?.getClass()} not supported")
        }

        allDice.sort() == dice.sort()
    }

    /**
     * Add support to the '+' operator, allowing this object to be used in such
     * expression with other objects, like numbers, die modifiers and other dice
     * rolling command.
     * @param value Object.
     * @return A dice rolling command that contains the calculation result.
     * @throws IllegalArgumentException if this object can't handle the given
     * object in such operation.
     */
    def plus(value) {
        switch(value) {
        case Number:
            return deriveCommand(allDice + value)
        case AbstractDiceRollingCommand:
            return deriveCommand(allDice + value.allDice,
                    (sides > value.sides) ? 0 : value.sides)
        case DiceModifier:
            return value.apply(this.&plus)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '-' operator, allowing this object to be used in such
     * expression with other objects, like numbers, die modifiers and other
     * dice rolling commands.
     * @param value Object.
     * @return A dice rolling command that contains the calculation result.
     * @throws IllegalArgumentException if this object can't handle the given object in
     * such operation.
     */
    def minus(value) {
        switch(value) {
        case Number:
        case DiceModifier:
            return plus(-value)
        case AbstractDiceRollingCommand:
            return deriveCommand(allDice + -value.allDice)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '*' operator, allowing this object to be used in such
     * expression with other objects, like numbers, die modifiers and other
     * dice rolling command.
     * @param value Object.
     * @return A dice rolling command when this object is being multiplied
     * by a die modifier object. Otherwise, returns a plain number containing the
     * result.
     * @throws IllegalArgumentException if this object can't handle the given
     * object in such operation.
     */
    def multiply(value) {
        switch(value) {
        case Number:
            return sum * value
        case AbstractDiceRollingCommand:
            return sum * value.sum
        case DiceModifier:
            return value.apply(this.&multiply)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '/' operator, allowing this object to be used in such
     * expression with other objects, like numbers, die modifiers and other
     * dice rolling command.
     * @param value Object.
     * @return A dice rolling command when this object is being divided by a
     * die modifier object. Otherwise, returns a plain number containing the result.
     * @throws IllegalArgumentException if this object can't handle the given object
     * in such operation.
     */
    def div(value) {
        switch(value) {
        case Number:
        case AbstractDiceRollingCommand:
            return sum / value
        case DiceModifier:
            return value.apply(this.&div)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '**' operator, allowing this object to be used in such
     * expression with other objects, like numbers, die modifiers and
     * other dice rolling command.
     * @param value Object.
     * @return A dice rolling command when this object is being powered by a
     * die modifier object. Otherwise, returns a plain number containing the result.
     * @throws IllegalArgumentException if this object can't handle the given object
     * in such operation.
     */
    def power(value) {
        switch(value) {
        case Number:
            return sum ** value
        case AbstractDiceRollingCommand:
            return sum ** value.sum
        case DiceModifier:
            return value.apply(this.&power)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '%' operator, allowing this object to be used in such
     * expression with other objects, like numbers, die modifiers and
     * other dice rolling command.
     * @param value Object.
     * @return A dice rolling command when this object is being mod'ed by a
     * die modifier object. Otherwise, returns a plain number containing the
     * result.
     * @throws IllegalArgumentException if this object can't handle the given object
     * in such operation.
     */
    def mod(value) {
        switch(value) {
        case Number:
            return sum % value
        case AbstractDiceRollingCommand:
            return sum % value.sum
        case DiceModifier:
            return value.apply(this.&mod)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Returns a new dice rolling command with all dice multiplied by -1.
     * @Return A new dice rolling command with all dice multiplied by -1.
     */
    def negative() {
        deriveCommand(allDice.collect{-it})
    }

    /**
     * Returns whether the given parameter is found in the dice of this roll.
     * @param condition Can be any object accepted by <code>grep()</code> method,
     * like a number, an array, a range, a closure etc. You can also pass a
     * dice rolling command to use its dice as the condition.
     * @return Whether the given parameter is found in the dice of this roll.
     */
    def isCase(condition) {
         if (condition instanceof AbstractDiceRollingCommand) {
             condition = condition.sum
         }
         condition in allDice
    }

    /**
     * Whether the sum of this roll is equals to the sum of the given roll.
     * @param command Dice rolling command.
     * @return Whether the sum of this roll is equals to the sum of the given
     * roll.
     * @throws ClassCastException if the given object is unexpected.
     */
    boolean equals(command) {
        this.compareTo(command) == 0
    }

    /**
     * Compare the sum of this roll to the sum of the given roll.
     * @param command Dice rolling command.
     * @return < 0 if the sum of this roll is lesser than the sum of the given
     * roll; = 0 if the sum of this roll is equals to the sum of the given roll;
     * > 0 if the sum of this roll is greater than the sum of the given roll.
     * @throws ClassCastException if the given object is unexpected.
     */
    int compareTo(command) {
        if (command instanceof AbstractDiceRollingCommand) {
            return sum <=> command.sum
        }
        throw new ClassCastException('Expecting an instance of AbstractDiceRollingCommand')
    }

    /**
     * Default string representation of this object.
     * @return The sum of all dice.
     */
    String toString() {
        sum
    }
}