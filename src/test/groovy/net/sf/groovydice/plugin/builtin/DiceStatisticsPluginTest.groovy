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
 * DiceStatisticsPlugin test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class DiceStatisticsPluginTest {

    @BeforeClass
    static void initialize()  {
        new GroovyDice().initialize()
    }

    @Test
    void emptyDiceRollSum() {
        assert new DiceRollingCommand().sum == 0
    }

    @Test
    void diceRollSum() {
        assert new DiceRollingCommand(allDice:[1,2,3,4]).sum == 10
    }

    @Test
    void countDice() {
        assert new DiceRollingCommand().count == 0
        assert new DiceRollingCommand(allDice:[1,2,3,4]).count == 4
    }

    @Test
    void bestDieFromEmptyDiceRoll() {
        assert new DiceRollingCommand().best_die == 0
    }

    @Test
    void bestDie() {
        assert new DiceRollingCommand(allDice:[1,2,3,4]).best_die == 4
    }

    @Test
    void worstDieFromEmptyDiceRoll() {
        assert new DiceRollingCommand().worst_die == 0
    }

    @Test
    void worstDie() {
        assert new DiceRollingCommand(allDice:[1,2,3,4]).worst_die == 1
    }

    @Test
    void emptyDiceRollMean() {
        assert new DiceRollingCommand().mean == 0
    }

    @Test
    void mean() {
        assert new DiceRollingCommand(allDice:[1,2,3,4]).mean == 2.5
    }

    @Test
    void emptyDiceRollMedian() {
        assert new DiceRollingCommand().median == 0
    }

    @Test
    void median() {
        assert new DiceRollingCommand(allDice:[5,4,3,2,1]).median == 3
        assert new DiceRollingCommand(allDice:[4,3,2,1]).median == 2.5
    }

    @Test
    void emptyDiceRollMode() {
        assert new DiceRollingCommand().mode == []
    }

    @Test
    void mode() {
        assert new DiceRollingCommand(allDice:[1,2,3,4,5]).mode == []
        assert new DiceRollingCommand(allDice:[1]).mode == [1]
        assert new DiceRollingCommand(allDice:[1,2,3,4,5,4]).mode == [4]
        assert new DiceRollingCommand(allDice:[1,2,3,4,5,4,3]).mode == [3,4]
    }
}