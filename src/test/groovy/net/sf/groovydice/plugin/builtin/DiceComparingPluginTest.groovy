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
package net.sf.groovydice.plugin.builtin

import org.junit.*

import net.sf.groovydice.*

/**
 * OddEvenPlugin test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class DiceComparingPluginTest {

    @BeforeClass
    static void initialize()  {
        new GroovyDice().initialize()
    }

    @Test
    void isDiceRollTheSameAsNumber() {
        assert new DiceRollingCommand(allDice:[1]).same_as(1)
        assert !new DiceRollingCommand(allDice:[1]).same_as(2)
    }

    @Test
    void isDiceRollTheSameAsList() {
        assert new DiceRollingCommand(allDice:[1,2,3]).same_as([1,2,3])
        assert !new DiceRollingCommand(allDice:[1,2,3]).same_as([1,2,3,4])

        assert new DiceRollingCommand(allDice:[1,2,3]).same_as(1..3)
        assert !new DiceRollingCommand(allDice:[1,2,3]).same_as(1..4)
    }

    @Test
    void isDiceRollTheSameAsAnother() {
        assert new DiceRollingCommand(allDice:[1,2,3]).same_as(
            new DiceRollingCommand(allDice:[3,2,1]))

        assert !new DiceRollingCommand(allDice:[1,2,3]).same_as(
            new DiceRollingCommand(allDice:[4,2,1]))
    }

    @Test(expected=IllegalArgumentException)
    void isDiceRollTheSameAsInvalidObject() {
        new DiceRollingCommand(allDice:[1,2,3]).same_as('hey')
    }
}