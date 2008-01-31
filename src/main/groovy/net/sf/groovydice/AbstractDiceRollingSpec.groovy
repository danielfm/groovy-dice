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
 * This abstract class represents the main functions of a dice rolling specification.
 *
 * @author Daniel F. Martins
 */
abstract class AbstractDiceRollingSpec implements Comparable {

    /** Sides of the die or '%' to represent a percentile die. */
    def sides = 6

    /** Already rolled dice. */
    def allDice = []

    /** Random number generator. */
    def numberGenerator = new SimpleRandomNumberGenerator()


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
            allDice << numberGenerator.generateNumber(sides) * ((n < 0) ? -1 : 1)
        }
        if (sides == '%') sides = 100
        this
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
     * Get the number of rolled dice.
     * @return A <code>Number</code> that represents the number of rolled dice.
     */
    def getCount() {
        allDice.size()
    }

    /**
     * Get the best die.
     * @return A <code>Number</code> which represents the best die or null
     * if no die has been rolled yet.
     */
    def getBest_die() {
        allDice?.max()
    }

    /**
     * Get the worst die.
     * @return A <code>Number</code> which represents the worst die or null if
     * no die has been rolled yet.
     */
    def getWorst_die() {
        allDice?.min()
    }

    /**
     * Calculate the mean of all rolled dice.
     * @return A <code>Number</code> which represents the mean of all rolled
     * dice or null if no die has been rolled yet.
     */
    def getMean() {
        if (allDice) {
            return sum / count
        }
    }

    /**
     * Calculate the median of all rolled dice.
     * @return A <code>Number</code> which represents the median of all rolled
     * dice or null if no die has been rolled yet.
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
     * Calculate the mode of all rolled dice.
     * @return A <code>Number</code> which represents the median of all rolled
     * dice or null if no die has been rolled yet.
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
     * Create a new dice rolling specification object based on this one.
     * @param allDice Specify the set of already rolled dice.
     * @param sides Optional parameter to specify the number of sides.
     * @return New DiceRollingSpec object.
     */
    def deriveSpec(allDice, sides=0) {
        /* reflection used here to work nicely with subclasses */
        def spec = this.class.newInstance()

        spec.sides = (sides == 0) ? this.sides : sides
        spec.allDice = allDice
        spec.numberGenerator = numberGenerator
        spec
    }

    /**
     * Get the best 'n' dice.
     * @param n Optional parameter that specify the number of best dice to get.
     * Defaults to 1.
     * @return If the given parameter is 1, the method returns a <code>Number</code>
     * that represents the best die. Otherwise, a new dice rolling specification
     * object containing the best 'n' dice is returned. If no dice has been rolled
     * yet, this method returns zero.
     */
    def best(n=1) {
        if (!allDice) return 0
        n = (n > 0) ? (n > count ? count : n) : 1
        n == 1 ? allDice.sort()[-n] : deriveSpec(allDice.sort()[-1..-n])
    }

    /**
     * Get the worst 'n' dice.
     * @param n Optional parameter that specify the number of worst dice to get.
     * Defaults to 1.
     * @return If the given parameter is 1, the method returns an <code>Number</code>
     * that represents the worst die. Otherwise, a new dice rolling spec object
     * containing the worst 'n' dice is returned. If no dice has been rolled yet,
     * this method returns zero.
     */
    def worst(n=1) {
        if (!allDice) return 0
        n = (n > 0) ? (n >= count ? (count - 1) : (n - 1)) : 0
        n == 0 ? allDice.sort()[n] : deriveSpec(allDice.sort()[0..n])
    }

    /**
     * Select the N best or worst dice.
     * @param condition This parameter is a number. If it is a positive number,
     * get the N best dice. If this is a negative number, get the |N| worst dice.
     * @return New dice rolling specification object that contains only the N
     * best or worst dice if N > 1 or N < -1, respectively. If N == 1 or
     * N == -1, this method returns a Number which represents the best or worst
     * value, respectively. If N == 0, this method returns null.
     */
    def the(condition) {
        condition == 0 ? null : (condition < 0 ? worst(-condition) : best(condition))
    }

    /**
     * Simple hashCode method.
     * @return Hashcode.
     */
    int hashCode() {
        sides.hashCode() + allDice.hashCode()
    }
}