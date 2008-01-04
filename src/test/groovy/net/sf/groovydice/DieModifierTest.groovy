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

import org.junit.*

/**
 * DieModifier test cases.
 *
 * @author Daniel F. Martins
 */
class DieModifierTest {

    @Before
    void initialize() {
        new GroovyDice().initialize()
    }

    @Test
    void negativeDieModifier() {
        assert -5.on_each_die instanceof DieModifier
        assert (-5).on_each_die.when{it.is_even} instanceof DieModifier

        assert -5.on_each_die.modifier == -5
        assert (-5).on_each_die.modifier == -5
        assert -5.on_each_die.when{it.is_even}.modifier == -5
        assert (-5).on_each_die.when{it.is_even}.modifier == -5
    }

    @Test
    void useADiceRollAsCondition() {
        def modifier = 2.on_each_die.when(new DiceRollingSpec(allDice:[1,3,5]))

        assert modifier.modifier == 2
        assert modifier.condition == [1,3,5]
    }
}