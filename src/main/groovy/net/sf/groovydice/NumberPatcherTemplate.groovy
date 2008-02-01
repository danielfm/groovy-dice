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
 * This abstract class provides a way to "patch" <code>Number</code> classes,
 * adding properties and methods to those classes which triggers the creation of
 * dice rolling specification objects.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
abstract class NumberPatcherTemplate {

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
     * Get a list of <code>Number</code> classes that will receive the
     * methods this patcher needs to provide.
     * @return List of <code>Number</code> classes.
     */
    def getNumberClasses() {
        [Integer, Long, BigInteger, Float, Double, BigDecimal]
    }

    /* please override these methods in a subclass */

    /**
     * Add the property <code>to_every_die</code> to the given class.
     * @param clazz Class where the property will be added.
     */
    void addToEveryDieProperty(clazz) {
    }

    /**
     * Add the method <code>to_each_die_if</code> to the given class.
     * @param clazz Class where the method will be added.
     */
    void addToEachDieIfMethod(clazz) {
    }

    /**
     * Add the property <code>is_even</code> to the given class.
     * @param clazz Class where the property will be added.
     */
    void addIsEvenProperty(clazz) {
    }

    /**
     * Add the property <code>is_odd</code> to the given class.
     * @param clazz Class where the property will be added.
     */
    void addIsOddProperty(clazz) {
    }

    /**
     * Add the propert <code>best</code> to the given class.
     * @param clazz Class where the property will be added.
     */
    void addBestProperty(clazz) {
    }

    /**
     * Add the propert <code>worst</code> to the given class.
     * @param clazz Class where the property will be added.
     */
    void addWorstProperty(clazz) {
    }

    /**
     * Modify the given class to make it support dice rolling methods,
     * like <code>n.dX</code>, <code>n.pd</code> and <code>n.'d%'</code>.
     * @param clazz Class that will receive such methods.
     */
    void addDiceMethods(clazz) {
    }

    /**
     * Add the method <code>d()</code> to the given class.
     * @param clazz Class where the method will be added.
     */
    void addDynamicDiceMethods(clazz) {
    }

    /**
     * Override the <code>+</code> (plus) operator of the given class.
     * @param clazz Class which plus method should be overridden.
     */
    void overridePlusOperator(clazz) {
    }

    /**
     * Override the <code>-</code> (minus) operator of the given class.
     * @param clazz Class which minus method should be overridden.
     */
    void overrideMinusOperator(clazz) {
    }

    /**
     * Override the <code>*</code> (multiply) operator of the given class.
     * @param clazz Class which multiply method should be overridden.
     */
    void overrideMultiplyOperator(clazz) {
    }

    /**
     * Override the <code>/</code> (div) operator of the given class.
     * @param clazz Class which div method should be overridden.
     */
    void overrideDivOperator(clazz) {
    }

    /**
     * Override the <code>**</code> (power) operator of the given class.
     * @param clazz Class which power method should be overridden.
     */
    void overridePowerOperator(clazz) {
    }

    /**
     * Override the <code>%</code> (mod) operator of the given class.
     * @param clazz Class which mod method should be overridden.
     */
    void overrideModOperator(clazz) {
    }
}