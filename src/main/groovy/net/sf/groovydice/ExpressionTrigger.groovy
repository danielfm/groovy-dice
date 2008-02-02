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
 * This class dinamically adds methods and properties to the Groovy standard
 * API in order to intercept expressions that looks like a dice rolling
 * command.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class ExpressionTrigger {

    /** GroovyDice instance. */
    def config

    /**
     * Modify <code>Number</code> instances at runtime, enabling its instances
     * to respond to new methods and properties.
     */
    void addMethods() {
        numberClasses.each {
            addToEveryDieProperty(it)
            addToEachDieIfMethod(it)
            addIsEvenProperty(it)
            addIsOddProperty(it)

            addBestProperty(it)
            addWorstProperty(it)

            addDiceMethods(it)
            addDynamicDiceMethods(it)

            overridePlusOperator(it)
            overrideMinusOperator(it)
            overrideMultiplyOperator(it)
            overrideDivOperator(it)

            overridePowerOperator(it)
            overrideModOperator(it)
        }
    }

    /**
     * Check whether the given object is a dice rolling command.
     * @param obj Object to check.
     */
    def isCommand(obj) {
        obj instanceof AbstractDiceRollingCommand
    }

    /**
     * Get a list of <code>Number</code> classes that will receive the
     * methods this patcher needs to provide.
     * @return List of <code>Number</code> classes.
     */
    def getNumberClasses() {
        [Integer, Long, BigInteger, Float, Double, BigDecimal]
    }

    /**
     * Create and roll a dice rolling command.
     * @param sides Number of sides of the dice.
     * @param rolls Number of dice to roll.
     * @return The dice rolling command .
     */
    def rollCommand(sides, rolls) {
        def command = config.commandClass.newInstance()
        command.sides = sides

        command.numberGenerator = config.numberGenerator
        command.roll(rolls)
    }

    /**
     * Create a dice modifier object.
     * @param number Modifier number
     * @param condition Modifier condition.
     * @return Die modifier object.
     */
    def createModifier(number, condition = null) {
        new DiceModifier(modifier:number, condition:condition)
    }

    void addToEveryDieProperty(clazz) {
        clazz.metaClass.getTo_every_die = { ->
            createModifier(delegate)
        }
    }

    void addToEachDieIfMethod(clazz) {
        clazz.metaClass.to_each_die_if = { condition ->
            createModifier(delegate, condition)
        }
    }

    void addIsEvenProperty(clazz) {
        clazz.metaClass.getIs_even = { ->
            (delegate as int) % 2 == 0
        }
    }

    void addIsOddProperty(clazz) {
        clazz.metaClass.getIs_odd = { ->
            (delegate as int) % 2 != 0
        }
    }

    void addBestProperty(clazz) {
        clazz.metaClass.getBest = { ->
            if (delegate < 1) {
                throw new IllegalArgumentException("You can only use the getBest() property on positive numbers. Value: $delegate")
            }
            delegate
        }
    }

    void addWorstProperty(clazz) {
        clazz.metaClass.getWorst = { ->
            if (delegate < 1) {
                throw new IllegalArgumentException("You can only use the getWorst() property on positive numbers. Value: $delegate")
            }
            -delegate
        }
    }

    void addDiceMethods(clazz) {
        clazz.metaClass.propertyMissing = { name ->

            if (name =~ /^(d|D)$/) {
                name = 'd6' // 6-sided dice is the default dice type
            }

            /* intercept calls to 'dX' properties */
            if (name =~ /^(d|D)\d+$/) {
                return delegate.d(name.substring(1).toInteger())
            }
            if (name =~ /^((p|P)(d|D)|(d|D)%)$/) {
                return delegate.d('%')
            }

            throw new MissingPropertyException(
                    "No such property: $name for class: ${delegate.class}")
        }
    }

    void addDynamicDiceMethods(clazz) {
        clazz.metaClass.d = { sides ->
            try {
                if (isCommand(sides)) {
                    sides = sides.sum
                }
                sides = sides.toFloat() as int
            }
            catch (NumberFormatException) {}
            rollCommand(sides, delegate)
        }

        clazz.metaClass.D = { sides ->
            delegate.d(sides)
        }
    }

    void overridePlusOperator(clazz) {
        clazz.metaClass.plus = { n ->
            if (isCommand(n)) {
                return n + delegate
            }
        }
    }

    void overrideMinusOperator(clazz) {
        clazz.metaClass.minus = { n ->
            if (isCommand(n)) {
                return -n + delegate
            }
        }
    }

    void overrideMultiplyOperator(clazz) {
        clazz.metaClass.multiply = { n ->
            if (isCommand(n)) {
                return n * delegate
            }
        }
    }

    void overrideDivOperator(clazz) {
        clazz.metaClass.div = { n ->
            if (isCommand(n)) {
                return delegate / n.sum
            }
        }
    }

    void overridePowerOperator(clazz) {
        clazz.metaClass.power = { n ->
            if (isCommand(n)) {
                return delegate ** n.sum
            }
        }
    }

    void overrideModOperator(clazz) {
        clazz.metaClass.mod = { n ->
            if (isCommand(n)) {
                return delegate % n.sum
            }
        }
    }
}