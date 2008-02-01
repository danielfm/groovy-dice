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
 * AbstractDiceRollingSpec test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class AbstractDiceRollingSpecTest {

    @Before
    void initialize() {
        new GroovyDice().initialize()
    }

    @Test
    void rollDice() {
        def spec = new DefaultDiceRollingSpec()
        assert !spec.allDice

        spec.roll(0)
        assert spec.allDice.size() == 0

        spec.roll(5)
        assert spec.allDice.size() == 5

        spec = new DefaultDiceRollingSpec(sides:10).roll(-5)
        assert spec.allDice.size() == 5

        spec = new DefaultDiceRollingSpec(sides:'%').roll()
        assert spec.allDice.size() == 1
    }

    @Test
    void rollInvalidDice() {
        try {
            new DefaultDiceRollingSpec(sides:0).roll(2)
            fail()
        }
        catch (IllegalArgumentException) {}

        try {
            new DefaultDiceRollingSpec(sides:-1).roll(2)
            fail()
        }
        catch (IllegalArgumentException) {}
    }

    @Test
    void getView() {
        def spec = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5])
        def view = spec.view

        assert spec.allDice == view
        assert !spec.allDice.is(view)
    }

    @Test
    void getTheBestDie() {
        assert !new DefaultDiceRollingSpec().best_die

        def spec = new DefaultDiceRollingSpec(allDice:[1,3,2,3,5,6,1,6])
        assert spec.best_die == 6
    }

    @Test
    void getTheWorstDie() {
        assert !new DefaultDiceRollingSpec().worst_die

        def spec = new DefaultDiceRollingSpec(allDice:[1,3,2,3,5,6,1,6])
        assert spec.worst_die == 1
    }

    @Test
    void getTheMean() {
        assert !new DefaultDiceRollingSpec().mean

        def spec = new DefaultDiceRollingSpec(allDice:[1,3,2,3,5,6,1,6])
        assert spec.mean == 3.375f
    }

    @Test
    void getTheMedian() {
        def spec = new DefaultDiceRollingSpec()
        assert !spec.median

        spec = new DefaultDiceRollingSpec(allDice:[1,2,4,7,9,10])
        assert spec.median == 5.5

        spec = new DefaultDiceRollingSpec(allDice:[1,3,5,7,9])
        assert spec.median == 5
    }

    @Test
    void getTheMode() {
        def spec = new DefaultDiceRollingSpec()
        assert !spec.mode

        spec = new DefaultDiceRollingSpec(allDice:[1,2,4,7,9,10])
        assert !spec.mode

        spec = new DefaultDiceRollingSpec(allDice:[1,2,4,7,9,10,4])
        assert spec.mode == [4]

        spec = new DefaultDiceRollingSpec(allDice:[1,2,4,7,9,10,4,7,3])
        assert spec.mode == [4,7]

        spec = new DefaultDiceRollingSpec(allDice:[1,2,4,7,9,10,4,7,3,1])
        assert spec.mode == [1,4,7]
    }

    @Test
    void sumDice() {
        assert !new DefaultDiceRollingSpec().sum

        def spec = new DefaultDiceRollingSpec(allDice:[1,3,2,3,5,6,1,6])
        assert spec.sum == 27
    }

    @Test
    void countDice() {
        assert new DefaultDiceRollingSpec(allDice:[1,2,3,4]).count == 4
    }

    @Test
    void getInvalidBestValues() {
        def spec = new DefaultDiceRollingSpec(allDice:[5,6])
        (-1..0).each {
            assert spec.best(it) == 6
        }
        assert spec.best(10).allDice == [6,5]
    }

    @Test
    void getTheDefaultBestValue() {
        def spec = new DefaultDiceRollingSpec(allDice:[5,6])
        assert spec.best() == 6
    }

    @Test
    void getTheThreeBestValues() {
        def spec = new DefaultDiceRollingSpec(allDice:[2,3,4,5,6,7])
        assert spec.best(3).allDice == [7,6,5]
    }

    @Test
    void getInvalidWorstValues() {
        def spec = new DefaultDiceRollingSpec(allDice:[6,5])
        (-1..0).each {
            assert spec.worst(it) == 5
        }
        assert spec.worst(10).allDice == spec.view.sort()
    }

    @Test
    void getTheDefaultWorstValue() {
        def spec = new DefaultDiceRollingSpec(allDice:[6,5])
        assert spec.worst() == 5
    }

    @Test
    void getTheThreeWorstValues() {
        def spec = new DefaultDiceRollingSpec(allDice:[7,6,5,4,3,2])
        assert spec.worst(3).allDice == [2,3,4]
    }

    @Test
    void createModifierFromDiceRoll() {
        def modifier = new DefaultDiceRollingSpec(allDice:[1,2,3]).to_every_die
        assert modifier.modifier == 6
    }

    @Test
    void selectBestDice() {
        def spec = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5,6,7,8,9])

        assert spec.the(2.best).allDice == [9,8]
        assert spec.the(3.0.best).allDice == [9,8,7]
    }

    @Test
    void selectWorstDice() {
        def spec = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5,6,7,8,9])

        assert spec.the(2.worst).allDice == [1,2]
        assert spec.the(3.0.worst).allDice == [1,2,3]
    }
}