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
 * DiceFilterPlugin test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class DiceFilterPluginTest {

    @BeforeClass
    static void initialize()  {
        new GroovyDice().initialize()
    }

    @Test(expected=IllegalArgumentException)
    void isInvalidNumberBest() {
        0.best
    }

    @Test
    void isNumberBest() {
        assert 6.best == 6
    }

    @Test(expected=IllegalArgumentException)
    void isInvalidNumberWorst() {
        0.worst
    }

    @Test
    void isNumberWorst() {
        assert 7.worst == -7
    }

    @Test(expected=IllegalArgumentException)
    void methodTheWithInvalidArgument() {
        new DiceRollingCommand(allDice:[1,2,3,4]).the(0)
    }

    @Test
    void theThreeBestOfEmptyDiceRoll() {
        assert new DiceRollingCommand().the(3.best) == 0
    }

    @Test
    void theThreeWorstOfEmptyDiceRoll() {
        assert new DiceRollingCommand().the(3.worst) == 0
    }

    @Test
    void theSixBestOfThreeDice() {
        assert (new DiceRollingCommand(allDice:[1,2,3])
            .the(6.best)).allDice == [3,2,1]
    }

    @Test
    void theSixWorstOfThreeDice() {
        assert (new DiceRollingCommand(allDice:[3,2,1])
            .the(6.worst)).allDice == [1,2,3]
    }

    @Test
    void theThreeBestOfSixDice() {
        assert (new DiceRollingCommand(allDice:[1,2,3,4,5,6])
            .the(3.best)).allDice == [6,5,4]
    }

    @Test
    void theBestOfSixDice() {
        assert new DiceRollingCommand(allDice:[1,2,3,4,5,6])
            .the(1.best) == 6
    }

    @Test
    void theThreeWorstOfSixDice() {
        assert (new DiceRollingCommand(allDice:[6,5,4,3,2,1])
            .the(3.worst)).allDice == [1,2,3]
    }

    @Test
    void theWorstOfSixDice() {
        assert new DiceRollingCommand(allDice:[6,5,4,3,2,1])
            .the(1.worst) == 1
    }

    @Test
    void onlyIf() {
        assert (new DiceRollingCommand(allDice:[6,5,4,3,2,1])
            .only_if{it > 2}).allDice == [6,5,4,3]
    }

    @Test
    void onlyIfUsingDiceRoll() {
        assert (new DiceRollingCommand(allDice:[6,5,4,3,2,1])
            .only_if(new DiceRollingCommand(allDice:[1,2,3]))
            ).allDice == [3,2,1]
    }
}