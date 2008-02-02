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
 * DiceModifier test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class DiceModifierTest {

    @Before
    void initialize() {
        new GroovyDice().initialize()
    }

    @Test
    void negativeDiceModifier() {
        assert -5.to_every_die instanceof DiceModifier
        assert (-5).to_each_die_if{it.is_even} instanceof DiceModifier

        assert -5.to_every_die.modifier == -5
        assert (-5).to_every_die.modifier == -5
        assert -5.to_each_die_if{it.is_even}.modifier == -5
        assert (-5).to_each_die_if{it.is_even}.modifier == -5
    }

    @Test
    void useADiceRollAsCondition() {
        def modifier = 2.to_each_die_if(new DefaultDiceRollingCommand(allDice:[1,3,5]))

        assert modifier.modifier == 2
        assert modifier.condition == [1,3,5]
    }

    @Test
    void applySimpleModifierToDiceRollingCommandClosure() {
        def command = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        def result = new DiceModifier(modifier:1).apply(command.&plus)

        assert command.allDice == [1,2,3,4]
        assert result.allDice == [2,3,4,5]
    }

    @Test
    void applyConditionalModifierToDiceRollingCommandClosure() {
        def command = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        def result = new DiceModifier(modifier:1, condition:{it%2 == 0})
                .apply(command.&plus)

        assert command.allDice == [1,2,3,4]
        assert result.allDice == [1,3,3,5]
    }
}