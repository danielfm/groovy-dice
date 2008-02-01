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
 * Groovy Dice main class.
 * 
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.0
 * @version 2
 */
class GroovyDice {

    /** Random number generator to use */
    def numberGenerator = new SimpleRandomNumberGenerator()

    /** Dice rolling specification implementation to use */
    def specClass = DefaultDiceRollingSpec

    /** Die modifier implementation to use */
    def modifierClass = DieModifier

    /** Number patcher to use */
    def numberPatcher = new DefaultNumberPatcher()

    /**
     * Initialize the Groovy Dice engine.
     */
    void initialize() {
        numberPatcher.config = this
        numberPatcher.addMethods()
    }
}