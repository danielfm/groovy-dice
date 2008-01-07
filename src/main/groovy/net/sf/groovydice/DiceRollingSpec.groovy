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
 * This class represents a dice rolling specification and its core logic.
 *
 * @author Daniel F. Martins
 */
class DiceRollingSpec implements Comparable {

    /** Sides of the die or '%' to represent a percentile die. */
    def sides = 6

    /** Already rolled dice. */
    def allDice = []

    /** Random number generator. */
    static final def generator = new Random()


    /**
     * Alias method that returns a copy of rolled dice.
     * @return Array of <code>Numbers</code> that represents the rolled dice.
     */
    def getView() {
        allDice.collect{it}
    }

    /**
     * Roll dice for the given number of times.
     * @param Optional parameter that specify the number of dice to roll. Defaults
     * to 1.
     * @return <code>this</code>.
     */
    def roll(n=1) {
        n.abs().times {
            allDice << generateNumber() * ((n < 0) ? -1 : 1)
        }
        this
    }

    /**
     * Generate a random number, considering the constraints specified by
     * this object.
     * @return Random <code>Integer</code> instance.
     * @throws IllegalArgumentException if the 'sides' field is invalid.
     */
    private def generateNumber() {
        if (!(sides == '%' || sides > 0)) {
            throw new IllegalArgumentException("Invalid side value: $sides")
        }

        if (sides == '%') {
            sides = 100
            def result = ''

            2.times {
                result += generator.nextInt(10)
            }
            return (result == '00') ? 100 : result.toInteger()
        }
        else {
            return generator.nextInt(sides) + 1
        }
    }

    /**
     * Get the sum of all dice.
     * @return A <code>Number</code> which represents the sum of all dice or
     * null if no die has been rolled yet.
     */
    def getSum() {
        allDice?.sum()
    }

    /**
     * Get the highest die.
     * @return A <code>Number</code> which represents the highest die or null
     * if no die has been rolled yet.
     */
    def getHighest() {
        allDice?.max()
    }

    /**
     * Get the lowest die.
     * @return A <code>Number</code> which represents the lowest die or null if
     * no die has been rolled yet.
     */
    def getLowest() {
        allDice?.min()
    }

    /**
     * Calculate the mean value.
     * @return A <code>Number</code> which represents the mean or null if no
     * die has been rolled yet.
     */
    def getMean() {
        if (allDice) {
            return sum / count
        }
    }

    /**
     * Calculate the median.
     * @return A <code>Number</code> which represents the median or null if no
     * die has been rolled yet.
     */
    def getMedian() {
        if(allDice) {
            def dice = allDice.sort()
            def n = dice.size()
            def h = n / 2 as int

            return !(n % 2) ? (dice[h-1..h].sum() / 2) : dice[h]
        }
    }

    /**
     * Calculate the mode.
     * @return A <code>Number</code> which represents the median or null if no
     * die has been rolled yet.
     */
    def getMode() {
        if (allDice) {
            def map = [:]
            allDice.each {
                map[it] = map[it] ? map[it] + 1 : 1
            }

            def keys = map.keySet().sort{-map[it]}
            if (map.values().sum() == map.keySet().size()) {
                return null
            }

            def idx = 0
            for (key in keys) {
                if (idx++ == 0) continue
                if (map[keys[idx - 2]] > map[keys[idx - 1]]) break
            }

            def result = keys[0..idx-2]
            if (result instanceof Number) {
                return [result]
            }
            else {
                return result
            }
        }
    }

    /**
     * Get the best 'n' dice.
     * @param n Optional parameter that specify the number of best dice to get.
     * Defaults to 1.
     * @return If the given parameter is 1, the method returns a <code>Number</code>
     * that represents the best die. Otherwise, a new <code>DiceRollingSpec</code> object
     * containing the best 'n' dice is returned. If no dice has been rolled yet, this method
     * returns zero.
     */
    def best(n=1) {
        if (!allDice) return 0
        n = (n > 0) ? n : 1

        def last = count - 1

        def first = last - (n-1)
        first = (first < 0) ? 0 : first
        
        if (n == 1) {
            return allDice.sort()[last]
        }
        else {
            return deriveSpec(allDice.sort()[last..first])
        }
    }

     /**
      * Get the worst 'n' dice.
      * @param n Optional parameter that specify the number of worst dice to get.
      * Defaults to 1.
      * @return If the given parameter is 1, the method returns an <code>Number</code>
      * that represents the worst die. Otherwise, a new <code>DiceRollingSpec</code> object
      * containing the worst 'n' dice is returned. If no dice has been rolled yet, this method
      * returns zero.
      */
    def worst(n=1) {
        if (!allDice) return 0

        def last = (n > 0) ? (n - 1) : 0
        last = last >= count ? count - 1 : last

        if (last == 0) {
            return allDice.sort()[0]
        }
        else {
            return deriveSpec(allDice.sort()[0..last])
        }
    }

    /**
     * Create a <code>DieModifier</code> object using the value returned by
     * <code>getSum()</code> method.
     * @return A <code>DieModifier</code> object, which is used to apply modifier
     * to rolled dice.
     */
    def getOn_each_die() {
        sum.on_each_die
    }

    /**
     * Returns a new <code>DiceRollingSpec</code> that contains only the dice that
     * match the given condition.
     * @param condition Can be any object accepted by <code>grep()</code> method,
     * like a number, an array, a range, a closure etc. You can also pass a
     * <code>DiceRollingSpec</code> object to use its dice as the condition.
     * @return New <code>DiceRollingSpec</code> object that contains only the dice
     * that match the given condition.
     */
    def where(condition) {
        if (condition instanceof DiceRollingSpec) {
            condition = condition.allDice
        }
        deriveSpec(allDice.grep(condition))
    }

    /**
     * Return the number of dice that match the given condition.
     * @param condition Can be any object accepted by <code>grep</code> method,
     * like a number, an array, a range, a closure etc. You can also pass a
     * <code>DiceRollingSpec</code> object to use its dice as the condition.
     * @return A <code>Number</code> that represents the number of dice that
     * match the given condition.
     */
    def count_where(condition) {
        if (condition instanceof DiceRollingSpec) {
            condition = condition.allDice
        }
        allDice.grep(condition).size()
    }

    /**
     * Get the number of rolled dice.
     * @return A <code>Number</code> that represents the number of rolled dice.
     */
    def getCount() {
        allDice.size()
    }

    /**
     * Create a new <code>DiceRollingSpec</code> object based on this one.
     * @param allDice Specify the set of already rolled dice.
     * @param sides Optional parameter to specify the number of sides.
     * @return New DiceRollingSpec object.
     */
    def deriveSpec(allDice, sides=0) {
        new DiceRollingSpec(sides:(sides == 0) ? this.sides : sides, allDice:allDice)
    }

    /**
     * Add support to the '+' operator, allowing this object to be used in such
     * expression with other objects, like numbers, <code>DieModifier</code>s and other
     * <code>DiceRollingSpec</code> objects.<p/> 
     * @param value Object.
     * @return A <code>DiceRollingSpec</code> that contains the calculation result.
     * @throws IllegalArgumentException if this object can't handle the given object in
     * such operation.
     */
    def plus(value) {
        switch(value) {
        case Number:
            return deriveSpec(allDice + value)
        case DiceRollingSpec:
            return deriveSpec(allDice + value.allDice,
                    (sides > value.sides) ? 0 : value.sides)
        case DieModifier:
            return value.apply(this.&plus)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '-' operator, allowing this object to be used in such
     * expression with other objects, like numbers, <code>DieModifier</code>s and other
     * <code>DiceRollingSpec</code> objects.
     * @param value Object.
     * @return A <code>DiceRollingSpec</code> that contains the calculation result.
     * @throws IllegalArgumentException if this object can't handle the given object in
     * such operation.
     */
    def minus(value) {
        switch(value) {
        case Number:
        case DieModifier:
            return plus(-value)
        case DiceRollingSpec:
            return deriveSpec(allDice + -value.allDice)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '*' operator, allowing this object to be used in such
     * expression with other objects, like numbers, <code>DieModifier</code>s and other
     * <code>DiceRollingSpec</code> objects.
     * @param value Object.
     * @return A <code>DiceRollingSpec</code> when this object is being multiplied by a
     * <code>DieModifier</code> object. Otherwise, returns a plain number containing the 
     * result.
     * @throws IllegalArgumentException if this object can't handle the given object in
     * such operation.
     */
    def multiply(value) {
        switch(value) {
        case Number:
            return sum * value
        case DiceRollingSpec:
            return sum * value.sum
        case DieModifier:
            return value.apply(this.&multiply)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '/' operator, allowing this object to be used in such
     * expression with other objects, like numbers, <code>DieModifier</code>s and other
     * <code>DiceRollingSpec</code> objects.
     * @param value Object.
     * @return A <code>DiceRollingSpec</code> when this object is being divided by a
     * <code>DieModifier</code> object. Otherwise, returns a plain number containing the
     * result.
     * @throws IllegalArgumentException if this object can't handle the given object in
     * such operation.
     */
    def div(value) {
        switch(value) {
        case Number:
        case DiceRollingSpec:
            return sum / value
        case DieModifier:
            return value.apply(this.&div)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '**' operator, allowing this object to be used in such
     * expression with other objects, like numbers, <code>DieModifier</code>s and
     * other <code>DiceRollingSpec</code> objects.
     * @param value Object.
     * @return A <code>DiceRollingSpec</code> when this object is being powered
     * by a <code>DieModifier</code> object. Otherwise, returns a plain number
     * containing the result.
     * @throws IllegalArgumentException if this object can't handle the given object in
     * such operation.
     */
    def power(value) {
        switch(value) {
        case Number:
            return sum ** value
        case DiceRollingSpec:
            return sum ** value.sum
        case DieModifier:
            return value.apply(this.&power)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Add support to the '%' operator, allowing this object to be used in such
     * expression with other objects, like numbers, <code>DieModifier</code>s and
     * other <code>DiceRollingSpec</code> objects.
     * @param value Object.
     * @return A <code>DiceRollingSpec</code> when this object is being mod'ed
     * by a <code>DieModifier</code> object. Otherwise, returns a plain number
     * containing the result.
     * @throws IllegalArgumentException if this object can't handle the given object in
     * such operation.
     */
    def mod(value) {
        switch(value) {
        case Number:
            return sum % value
        case DiceRollingSpec:
            return sum % value.sum
        case DieModifier:
            return value.apply(this.&mod)
        }
        throw new IllegalArgumentException("Invalid argument: $value")
    }

    /**
     * Returns a new <code>DiceRollingSpec</code> object with all dice multiplied by -1.
     * @Return A new <code>DiceRollingSpec</code> object with all dice multiplied by -1.
     */
    def negative() {
        deriveSpec(allDice.collect{-it})
    }

    /**
     * Returns whether the given parameter is found in the dice of this roll.
     * @param condition Can be any object accepted by <code>grep()</code> method,
     * like a number, an array, a range, a closure etc. You can also pass a
     * <code>DiceRollingSpec</code> object to use its dice as the condition.
     * @return Whether the given parameter is found in the dice of this roll.
     */
    def isCase(condition) {
         if (condition instanceof DiceRollingSpec) {
             condition = condition.sum
         }
         condition in allDice
    }

    /**
     * Iterate through the dice of this roll.
     * @param closure Closure to call for each die.
     * @return <code>this</code>.
     */
    void for_each_die(closure) {
        allDice.each(closure)
    }

    /**
     * Whether the sum of this roll is equals to the sum of the given roll.
     * @param spec <code>DiceRollingSpec</code> object.
     * @return Whether the sum of this roll is equals to the sum of the given
     * roll.
     */
    boolean equals(spec) {
        this.compareTo(spec) == 0
    }

    /**
     * Whether this roll contains the same results of the given roll regardless
     * its order.
     * @param spec <code>DiceRollingSpec</code>, <code>Number</code>,
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
        case DiceRollingSpec:
            dice = spec.allDice
            break
        default:
            throw new IllegalArgumentException("${spec?.getClass()} not supported")
        }

        allDice.sort() == dice.sort()
    }

    /**
     * Compare the sum of this roll to the sum of the given roll.
     * @param spec <code>DiceRollingSpec</code> object.
     * @return <0 if the sum of this roll is lesser than the sum of the given
     * roll; ==0 if the sum of this roll is equals to the sum of the given roll;
     * >0 if the sum of this roll is greater than the sum of the given roll.
     */
    int compareTo(spec) {
        if (spec instanceof DiceRollingSpec) {
            return sum <=> spec.sum
        }
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