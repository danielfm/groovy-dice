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
 * This class implements the template methods used by its superclass.
 *
 * @author Daniel F. Martins
 */
class DefaultNumberPatcher extends NumberPatcherTemplate {

    /** Dice rolling specification implementation to use */
    def specClass = DefaultDiceRollingSpec

    /** Die modifier implementation to use */
    def modifierClass = DieModifier


    /**
     * Create and roll a dice rolling specification object.
     * @param sides Number of sides of the dice.
     * @param rolls Number of dice to roll.
     * @return The dice rolling specification object.
     */
    def rollSpec(sides, rolls) {
        def spec = specClass.newInstance()
        spec.sides = sides
        spec.roll(rolls)
    }

    /**
     * Create a die modifier object.
     * @param number Modifier number
     * @param condition Modifier condition.
     * @return Die modifier object.
     */
    def createModifier(number, condition = null) {
        def m = modifierClass.newInstance()

        m.modifier = number
        m.condition = condition
        m
    }

    /**
     * Add the property <code>to_every_die</code> to the given class.
     * @param clazz Class where the property will be added.
     */
    void addOnEveryDieProperty(clazz) {
        clazz.metaClass.getTo_every_die = { ->
            createModifier(delegate)
        }
    }

    /**
     * Add the method <code>to_each_die_if</code> to the given class.
     * @param clazz Class where the method will be added.
     */
    void addOnEachDieIfMethod(clazz) {
        clazz.metaClass.to_each_die_if = { condition ->
            createModifier(delegate, condition)
        }
    }

    /**
     * Add the property <code>is_even</code> to the given class.
     * @param clazz Class where the property will be added.
     */
    void addIsEvenProperty(clazz) {
        clazz.metaClass.getIs_even = { ->
            (delegate as int) % 2 == 0
        }
    }

    /**
     * Add the property <code>is_odd</code> to the given class.
     * @param clazz Class where the property will be added.
     */
    void addIsOddProperty(clazz) {
        clazz.metaClass.getIs_odd = { ->
            (delegate as int) % 2 != 0
        }
    }

    /**
     * Add the propert <code>best</code> to the given class.
     * @param clazz Class where the property will be added.
     */
    void addBestProperty(clazz) {
        clazz.metaClass.getBest = { ->
            if (delegate < 1) {
                throw new IllegalArgumentException("You can only use the getBest() property on positive numbers. Value: $delegate")
            }
            delegate
        }
    }

    /**
     * Add the propert <code>worst</code> to the given class.
     * @param clazz Class where the property will be added.
     */
    void addWorstProperty(clazz) {
        clazz.metaClass.getWorst = { ->
            if (delegate < 1) {
                throw new IllegalArgumentException("You can only use the getWorst() property on positive numbers. Value: $delegate")
            }
            -delegate
        }
    }

    /**
     * Modify the given class to make it support dice rolling methods,
     * like <code>n.dX</code>, <code>n.pd</code> and <code>n.'d%'</code>.
     * @param clazz Class that will receive such methods.
     */
    void addDiceMethods(clazz) {
        clazz.metaClass.propertyMissing = { name ->

            if (name =~ /^(d|D)$/) {
                name = 'd6' // 6-sided dice is the default dice type
            }

            /* intercept calls to 'dX' properties */
            if (name =~ /^(d|D)\d+$/) {
                return rollSpec(name.substring(1).toInteger(), delegate)
            }
            if (name =~ /^((p|P)(d|D)|(d|D)%)$/) {
                return rollSpec('%', delegate)
            }

            throw new MissingPropertyException(
                    "No such property: $name for class: ${delegate.class}")
        }
    }

    /**
     * Add the method <code>d()</code> to the given class.
     * @param clazz Class where the method will be added.
     */
    void addDynamicDiceMethods(clazz) {
        clazz.metaClass.d = { sides ->
            try {
                if (sides instanceof AbstractDiceRollingSpec) {
                    sides = sides.sum
                }
                sides = sides.toFloat() as int
            }
            catch (NumberFormatException) {}
            rollSpec(sides, delegate)
        }

        clazz.metaClass.D = { sides ->
            delegate.d(sides)
        }
    }

    /**
     * Override the <code>+</code> (plus) operator of the given class.
     * @param clazz Class which plus method should be overridden.
     */
    void overridePlusOperator(clazz) {
        clazz.metaClass.plus = { n ->
            if (n instanceof AbstractDiceRollingSpec) {
                return n + delegate
            }
        }
    }

    /**
     * Override the <code>-</code> (minus) operator of the given class.
     * @param clazz Class which minus method should be overridden.
     */
    void overrideMinusOperator(clazz) {
        clazz.metaClass.minus = { n ->
            if (n instanceof AbstractDiceRollingSpec) {
                return -n + delegate
            }
        }
    }

    /**
     * Override the <code>*</code> (multiply) operator of the given class.
     * @param clazz Class which multiply method should be overridden.
     */
    void overrideMultiplyOperator(clazz) {
        clazz.metaClass.multiply = { n ->
            if (n instanceof AbstractDiceRollingSpec) {
                return n * delegate
            }
        }
    }

    /**
     * Override the <code>/</code> (div) operator of the given class.
     * @param clazz Class which div method should be overridden.
     */
    void overrideDivOperator(clazz) {
        clazz.metaClass.div = { n ->
            if (n instanceof AbstractDiceRollingSpec) {
                return delegate / n.sum
            }
        }
    }

    /**
     * Override the <code>**</code> (power) operator of the given class.
     * @param clazz Class which power method should be overridden.
     */
    void overridePowerOperator(clazz) {
        clazz.metaClass.power = { n ->
            if (n instanceof AbstractDiceRollingSpec) {
                return delegate ** n.sum
            }
        }
    }

    /**
     * Override the <code>%</code> (mod) operator of the given class.
     * @param clazz Class which mod method should be overridden.
     */
    void overrideModOperator(clazz) {
        clazz.metaClass.mod = { n ->
            if (n instanceof AbstractDiceRollingSpec) {
                return delegate % n.sum
            }
        }
    }
}