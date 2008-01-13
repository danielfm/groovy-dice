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
 * This class only contains one method, which is used to "patch" the number
 * classes, adding properties and methods to them.
 *
 * @author Daniel F. Martins
 */
class NumberPatcher {

    /**
     * Call this method to modify Number instances at runtime, enabling its
     * instances to respond to lots of new methods and properties.
     */
    void addMethods() {

        [Integer, Long, BigInteger, Float, Double, BigDecimal].each {
            addOnEveryDieProperty(it)
            addIsEvenProperty(it)
            addIsOddProperty(it)

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
     * Add the property <code>on_every_die</code> to the given class.
     * @param clazz Class where the property will be added.
     */
    void addOnEveryDieProperty(clazz) {
        clazz.metaClass.getOn_every_die = { ->
            new DieModifier(modifier:delegate)
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
                def sides = name.substring(1).toInteger()
                return new DiceRollingSpec(sides:sides).roll(delegate)
            }
            if (name =~ /^((p|P)(d|D)|(d|D)%)$/) {
                return new DiceRollingSpec(sides:'%').roll(delegate)
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
                if (sides instanceof DiceRollingSpec) {
                    sides = sides.sum
                }
                sides = sides.toFloat() as int                      
            }
            catch (NumberFormatException) {}
            new DiceRollingSpec(sides:sides).roll(delegate)
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
            if (n instanceof DiceRollingSpec) {
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
            if (n instanceof DiceRollingSpec) {
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
            if (n instanceof DiceRollingSpec) {
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
            if (n instanceof DiceRollingSpec) {
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
            if (n instanceof DiceRollingSpec) {
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
            if (n instanceof DiceRollingSpec) {
                return delegate % n.sum
            }
        }
    }
}