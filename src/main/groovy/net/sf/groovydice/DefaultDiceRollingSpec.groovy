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
 * This class adds some goodies to the AbstractDiceRollingSpec class.
 *
 * @author Daniel F. Martins
 */
class DefaultDiceRollingSpec extends AbstractDiceRollingSpec {

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
     * pass a dice rolling specification object to use its dice as the
     * condition.
     * @return A die modifier object, which is used to apply
     * a modifier to dice that matches the given condition.
     */
    def to_each_die_if(condition) {
        sum.to_each_die_if(condition)
    }

    /**
     * Returns a new dice rolling spec object that contains only the dice
     * that match the given condition.
     * @param condition Can be any object accepted by <code>grep()</code> method,
     * like a number, an array, a range, a closure etc. You can also pass a dice
     * rolling spec object to use its dice as the condition.
     * @return New dice rolling spec object that contains only the dice that
     * match the given condition.
     */
    def only_if(condition) {
        if (condition instanceof AbstractDiceRollingSpec) {
            condition = condition.allDice
        }
        deriveSpec(allDice.grep(condition))
    }

    /**
     * Whether this roll contains the same results of the given roll regardless
     * its order.
     * @param spec Dice rolling specification, <code>Number</code>,
     * <code>List</code> or <code>Range</code> object, which contains the results
     * to be checked.
     * @return Whether this roll contains the same results of the given roll
     * regardless its order.
     */
    def same_as(spec) {
        def dice

        switch(spec) {
        case Number:
            dice = [spec]
            break
        case List:
            dice = spec.collect{it}
            break
        case AbstractDiceRollingSpec:
            dice = spec.allDice
            break
        default:
            throw new IllegalArgumentException("${spec?.getClass()} not supported")
        }

        allDice.sort() == dice.sort()
    }

    /**
     * Add support to the '+' operator, allowing this object to be used in such
     * expression with other objects, like numbers, die modifiers and other dice
     * rolling specification objects.
     * @param value Object.
     * @return A dice rolling specification object that contains the calculation
     * result.
     * @throws IllegalArgumentException if this object can't handle the given
     * object in such operation.
     */
    def plus(value) {
        switch(value) {
        case Number:
            return deriveSpec(allDice + value)
        case AbstractDiceRollingSpec:
            return deriveSpec(allDice + value.allDice,
                    (sides > value.sides) ? 0 : value.sides)
        case DieModifier:
            return value.apply(this.&plus)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '-' operator, allowing this object to be used in such
     * expression with other objects, like numbers, die modifiers and other
     * dice rolling specification objects.
     * @param value Object.
     * @return A dice rolling specification that contains the calculation result.
     * @throws IllegalArgumentException if this object can't handle the given object in
     * such operation.
     */
    def minus(value) {
        switch(value) {
        case Number:
        case DieModifier:
            return plus(-value)
        case AbstractDiceRollingSpec:
            return deriveSpec(allDice + -value.allDice)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '*' operator, allowing this object to be used in such
     * expression with other objects, like numbers, die modifiers and other
     * dice rolling specification objects.
     * @param value Object.
     * @return A dice rolling specification when this object is being multiplied
     * by a die modifier object. Otherwise, returns a plain number containing the
     * result.
     * @throws IllegalArgumentException if this object can't handle the given
     * object in such operation.
     */
    def multiply(value) {
        switch(value) {
        case Number:
            return sum * value
        case AbstractDiceRollingSpec:
            return sum * value.sum
        case DieModifier:
            return value.apply(this.&multiply)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '/' operator, allowing this object to be used in such
     * expression with other objects, like numbers, die modifiers and other
     * dice rolling specification objects.
     * @param value Object.
     * @return A dice rolling specification when this object is being divided by a
     * die modifier object. Otherwise, returns a plain number containing the result.
     * @throws IllegalArgumentException if this object can't handle the given object
     * in such operation.
     */
    def div(value) {
        switch(value) {
        case Number:
        case AbstractDiceRollingSpec:
            return sum / value
        case DieModifier:
            return value.apply(this.&div)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '**' operator, allowing this object to be used in such
     * expression with other objects, like numbers, die modifiers and
     * other dice rolling specification objects.
     * @param value Object.
     * @return A dice rolling specification when this object is being powered by a
     * die modifier object. Otherwise, returns a plain number containing the result.
     * @throws IllegalArgumentException if this object can't handle the given object
     * in such operation.
     */
    def power(value) {
        switch(value) {
        case Number:
            return sum ** value
        case AbstractDiceRollingSpec:
            return sum ** value.sum
        case DieModifier:
            return value.apply(this.&power)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '%' operator, allowing this object to be used in such
     * expression with other objects, like numbers, die modifiers and
     * other dice rolling specification objects.
     * @param value Object.
     * @return A dice rolling specification when this object is being mod'ed by a
     * die modifier object. Otherwise, returns a plain number containing the
     * result.
     * @throws IllegalArgumentException if this object can't handle the given object
     * in such operation.
     */
    def mod(value) {
        switch(value) {
        case Number:
            return sum % value
        case AbstractDiceRollingSpec:
            return sum % value.sum
        case DieModifier:
            return value.apply(this.&mod)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Returns a new dice rolling specification object with all dice multiplied by -1.
     * @Return A new dice rolling specification object with all dice multiplied by -1.
     */
    def negative() {
        deriveSpec(allDice.collect{-it})
    }

    /**
     * Returns whether the given parameter is found in the dice of this roll.
     * @param condition Can be any object accepted by <code>grep()</code> method,
     * like a number, an array, a range, a closure etc. You can also pass a
     * dice rolling specification object to use its dice as the condition.
     * @return Whether the given parameter is found in the dice of this roll.
     */
    def isCase(condition) {
         if (condition instanceof AbstractDiceRollingSpec) {
             condition = condition.sum
         }
         condition in allDice
    }

    /**
     * Whether the sum of this roll is equals to the sum of the given roll.
     * @param spec Dice rolling specification object.
     * @return Whether the sum of this roll is equals to the sum of the given
     * roll.
     */
    boolean equals(spec) {
        this.compareTo(spec) == 0
    }


    /**
     * Compare the sum of this roll to the sum of the given roll.
     * @param spec Dice rolling specification object.
     * @return < 0 if the sum of this roll is lesser than the sum of the given
     * roll; = 0 if the sum of this roll is equals to the sum of the given roll;
     * > 0 if the sum of this roll is greater than the sum of the given roll.
     */
    int compareTo(spec) {
        if (spec instanceof AbstractDiceRollingSpec) {
            return sum <=> spec.sum
        }
    }

    /**
     * Default string representation of this object.
     * @return The sum of all dice.
     */
    String toString() {
        sum
    }
}