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
 * This class overrides the template methods defined by its superclass.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class DefaultNumberPatcher extends NumberPatcherTemplate {

    /** GroovyDice instance. */
    def config


    /**
     * Create and roll a dice rolling specification object.
     * @param sides Number of sides of the dice.
     * @param rolls Number of dice to roll.
     * @return The dice rolling specification object.
     */
    def rollSpec(sides, rolls) {
        def spec = config.specClass.newInstance()
        spec.sides = sides

        spec.numberGenerator = config.numberGenerator
        spec.roll(rolls)
    }

    /**
     * Create a die modifier object.
     * @param number Modifier number
     * @param condition Modifier condition.
     * @return Die modifier object.
     */
    def createModifier(number, condition = null) {
        def m = config.modifierClass.newInstance()

        m.modifier = number
        m.condition = condition
        m
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

    void overridePlusOperator(clazz) {
        clazz.metaClass.plus = { n ->
            if (n instanceof AbstractDiceRollingSpec) {
                return n + delegate
            }
        }
    }

    void overrideMinusOperator(clazz) {
        clazz.metaClass.minus = { n ->
            if (n instanceof AbstractDiceRollingSpec) {
                return -n + delegate
            }
        }
    }

    void overrideMultiplyOperator(clazz) {
        clazz.metaClass.multiply = { n ->
            if (n instanceof AbstractDiceRollingSpec) {
                return n * delegate
            }
        }
    }

    void overrideDivOperator(clazz) {
        clazz.metaClass.div = { n ->
            if (n instanceof AbstractDiceRollingSpec) {
                return delegate / n.sum
            }
        }
    }

    void overridePowerOperator(clazz) {
        clazz.metaClass.power = { n ->
            if (n instanceof AbstractDiceRollingSpec) {
                return delegate ** n.sum
            }
        }
    }

    void overrideModOperator(clazz) {
        clazz.metaClass.mod = { n ->
            if (n instanceof AbstractDiceRollingSpec) {
                return delegate % n.sum
            }
        }
    }
}