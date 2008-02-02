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
 * AbstractDiceRollingCommand test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class AbstractDiceRollingCommandTest {

    @Before
    void initialize() {
        new GroovyDice().initialize()
    }

    @Test
    void rollDice() {
        def command = new DefaultDiceRollingCommand()
        assert !command.allDice

        command.roll(0)
        assert command.allDice.size() == 0

        command.roll(5)
        assert command.allDice.size() == 5

        command = new DefaultDiceRollingCommand(sides:10).roll(-5)
        assert command.allDice.size() == 5

        command = new DefaultDiceRollingCommand(sides:'%').roll()
        assert command.allDice.size() == 1
    }

    @Test
    void rollInvalidDice() {
        try {
            new DefaultDiceRollingCommand(sides:0).roll(2)
            fail()
        }
        catch (IllegalArgumentException) {}

        try {
            new DefaultDiceRollingCommand(sides:-1).roll(2)
            fail()
        }
        catch (IllegalArgumentException) {}
    }

    @Test
    void getView() {
        def command = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5])
        def view = command.view

        assert command.allDice == view
        assert !command.allDice.is(view)
    }

    @Test
    void getTheBestDie() {
        assert !new DefaultDiceRollingCommand().best_die

        def command = new DefaultDiceRollingCommand(allDice:[1,3,2,3,5,6,1,6])
        assert command.best_die == 6
    }

    @Test
    void getTheWorstDie() {
        assert !new DefaultDiceRollingCommand().worst_die

        def command = new DefaultDiceRollingCommand(allDice:[1,3,2,3,5,6,1,6])
        assert command.worst_die == 1
    }

    @Test
    void getTheMean() {
        assert !new DefaultDiceRollingCommand().mean

        def command = new DefaultDiceRollingCommand(allDice:[2])
        assert command.mean == 2

        command = new DefaultDiceRollingCommand(allDice:[1,3,2,3,5,6,1,6])
        assert command.mean == 3.375f
    }

    @Test
    void getTheMedian() {
        def command = new DefaultDiceRollingCommand()
        assert !command.median

        command = new DefaultDiceRollingCommand(allDice:[2])
        assert command.median == 2

        command = new DefaultDiceRollingCommand(allDice:[1,2,4,7,9,10])
        assert command.median == 5.5

        command = new DefaultDiceRollingCommand(allDice:[1,3,5,7,9])
        assert command.median == 5
    }

    @Test
    void getTheMode() {
        def command = new DefaultDiceRollingCommand()
        assert command.mode == []

        command = new DefaultDiceRollingCommand(allDice:[1,2])
        assert command.mode == []

        command = new DefaultDiceRollingCommand(allDice:[1])
        assert command.mode == [1]

        command = new DefaultDiceRollingCommand(allDice:[1,2,1])
        assert command.mode == [1]

        command = new DefaultDiceRollingCommand(allDice:[1,2,1,2])
        assert command.mode == [1,2]

        command = new DefaultDiceRollingCommand(allDice:[1,2,1,2,3])
        assert command.mode == [1,2]
    }

    @Test
    void sumDice() {
        assert !new DefaultDiceRollingCommand().sum

        def command = new DefaultDiceRollingCommand(allDice:[1,3,2,3,5,6,1,6])
        assert command.sum == 27
    }

    @Test
    void countDice() {
        assert new DefaultDiceRollingCommand(allDice:[1,2,3,4]).count == 4
    }

    @Test
    void getInvalidBestValues() {
        def command = new DefaultDiceRollingCommand(allDice:[5,6])
        (-1..0).each {
            assert command.best(it) == 6
        }
        assert command.best(10).allDice == [6,5]
    }

    @Test
    void getTheDefaultBestValue() {
        def command = new DefaultDiceRollingCommand(allDice:[5,6])
        assert command.best() == 6
    }

    @Test
    void getTheThreeBestValues() {
        def command = new DefaultDiceRollingCommand(allDice:[2,3,4,5,6,7])
        assert command.best(3).allDice == [7,6,5]
    }

    @Test
    void getInvalidWorstValues() {
        def command = new DefaultDiceRollingCommand(allDice:[6,5])
        (-1..0).each {
            assert command.worst(it) == 5
        }
        assert command.worst(10).allDice == command.view.sort()
    }

    @Test
    void getTheDefaultWorstValue() {
        def command = new DefaultDiceRollingCommand(allDice:[6,5])
        assert command.worst() == 5
    }

    @Test
    void getTheThreeWorstValues() {
        def command = new DefaultDiceRollingCommand(allDice:[7,6,5,4,3,2])
        assert command.worst(3).allDice == [2,3,4]
    }

    @Test
    void createModifierFromDiceRoll() {
        def modifier = new DefaultDiceRollingCommand(allDice:[1,2,3]).to_every_die
        assert modifier.modifier == 6
    }

    @Test
    void selectBestDice() {
        def command = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5,6,7,8,9])

        assert command.the(2.best).allDice == [9,8]
        assert command.the(3.0.best).allDice == [9,8,7]
    }

    @Test
    void selectWorstDice() {
        def command = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5,6,7,8,9])

        assert command.the(2.worst).allDice == [1,2]
        assert command.the(3.0.worst).allDice == [1,2,3]
    }

    @Test
    void getDiceRollingHashCode() {
        assert new DefaultDiceRollingCommand(allDice:[1,2,3,4,5,6]).hashCode()
    }
}