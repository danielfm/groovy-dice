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
 * This class only contains one method, which is used to "patch"
 * <code>Number</code> classes, adding properties and methods which
 * triggers the creation of dice rolling specification objects.
 *
 * @author Daniel F. Martins
 */
abstract class NumberPatcherTemplate {

    /**
     * Call this method to modify <code>Number</code> instances at runtime,
     * enabling its instances to respond to lots of new methods and properties.
     */
    void addMethods() {
        numberClasses.each {
            addOnEveryDieProperty(it)
            addOnEachDieIfMethod(it)
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

    void addOnEveryDieProperty(clazz) {}
    void addOnEachDieIfMethod(clazz) {}
    void addIsEvenProperty(clazz) {}
    void addIsOddProperty(clazz) {}

    void addBestProperty(clazz) {}
    void addWorstProperty(clazz) {}

    void addDiceMethods(clazz) {}
    void addDynamicDiceMethods(clazz) {}

    void overridePlusOperator(clazz) {}
    void overrideMinusOperator(clazz) {}
    void overrideMultiplyOperator(clazz) {}
    void overrideDivOperator(clazz) {}

    void overridePowerOperator(clazz) {}
    void overrideModOperator(clazz) {}
}