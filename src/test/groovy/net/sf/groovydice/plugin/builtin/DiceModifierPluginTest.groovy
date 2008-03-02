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
 * DiceModifierPlugin test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F.
 * Martins</a>
 */
class DiceModifierPluginTest {

    @BeforeClass
    static void initialize()  {
        new GroovyDice().initialize()
    }

    @Test
    void numberOnEveryDie() {
        def modifier = 1.on_every_die

        assert modifier.modifier == 1
        assert !modifier.condition
    }

    @Test
    void diceRollOnEveryDie() {
        def modifier = new DiceRollingCommand(allDice:[1,2,3,4]).on_every_die

        assert modifier.modifier == 10
        assert !modifier.condition
    }

    @Test
    void numberOnEachDieIf() {
        def modifier = 1.on_each_die_if('condition')

        assert modifier.modifier == 1
        assert modifier.condition == 'condition'
    }

    @Test
    void diceRollOnEachDieIf() {
        def modifier = new DiceRollingCommand(allDice:[1,2,3,4])
            .on_each_die_if('condition')

        assert modifier.modifier == 10
        assert modifier.condition == 'condition'
    }

    @Test
    void numberOnEachDieIfUsingDiceRoll() {
        def modifier = 1.on_each_die_if(new DiceRollingCommand(allDice:[1,2,3]))

        assert modifier.modifier == 1
        assert modifier.condition == [1,2,3]
    }

    @Test
    void diceRollOnEachDieIfUsingDiceRoll() {
        def modifier = new DiceRollingCommand(allDice:[1,2,3,4])
            .on_each_die_if(new DiceRollingCommand(allDice:[1,2,3]))

        assert modifier.modifier == 10
        assert modifier.condition == [1,2,3]
    }

    @Test
    void plusSimpleModifierToDiceRoll() {
        assert (new DiceRollingCommand(allDice:[1,2,3,4]) +
            2.on_every_die).allDice == [3,4,5,6]
    }

    @Test
    void plusConditionalModifierToDiceRoll() {
        assert (new DiceRollingCommand(allDice:[1,2,3,4]) +
            2.on_each_die_if{it>2}).allDice == [1,2,5,6]
    }

    @Test
    void subtractSimpleModifierFromDiceRoll() {
        assert (new DiceRollingCommand(allDice:[1,2,3,4]) -
            2.on_every_die).allDice == [-1,0,1,2]
    }

    @Test
    void subtractConditionalModifierFromDiceRoll() {
        assert (new DiceRollingCommand(allDice:[1,2,3,4]) -
            2.on_each_die_if{it>2}).allDice == [1,2,1,2]
    }

    @Test
    void multiplyDiceRollBySimpleModifier() {
        assert (new DiceRollingCommand(allDice:[1,2,3,4]) *
            2.on_every_die).allDice == [2,4,6,8]
    }

    @Test
    void multiplyDiceRollByConditionalModifier() {
        assert (new DiceRollingCommand(allDice:[1,2,3,4]) *
            2.on_each_die_if{it>2}).allDice == [1,2,6,8]
    }

    @Test
    void divideDiceRollBySimpleModifier() {
        assert (new DiceRollingCommand(allDice:[1,2,3,4]) /
            2.on_every_die).allDice == [0.5,1,1.5,2]
    }

    @Test
    void divideDiceRollByConditionalModifier() {
        assert (new DiceRollingCommand(allDice:[1,2,3,4]) /
            2.on_each_die_if{it>2}).allDice == [1,2,1.5,2]
    }

    @Test
    void diceRollRaisedToThePowerOfSimpleModifier() {
        assert (new DiceRollingCommand(allDice:[1,2,3,4]) **
            2.on_every_die).allDice == [1,4,9,16]
    }

    @Test
    void diceRollRaisedToThePowerOfConditionalModifier() {
        assert (new DiceRollingCommand(allDice:[1,2,3,4]) **
            2.on_each_die_if{it>2}).allDice == [1,2,9,16]
    }

    @Test
    void diceRollModuloSimpleModifier() {
        assert (new DiceRollingCommand(allDice:[1,2,3,4]) %
            2.on_every_die).allDice == [1,0,1,0]
    }

    @Test
    void diceRollModuloConditionalModifier() {
        assert (new DiceRollingCommand(allDice:[1,2,3,4]) %
            2.on_each_die_if{it>2}).allDice == [1,2,1,0]
    }
}
