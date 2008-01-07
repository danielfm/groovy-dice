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
 * DiceRollingSpec test cases.
 *
 * @author Daniel F. Martins
 */
class DiceRollingSpecTest {

    @Before
    void initialize() {
        new GroovyDice().initialize()
    }

    @Test
    void rollDice() {
        def spec = new DiceRollingSpec()
        assert !spec.allDice

        spec.roll(0)
        assert spec.allDice.size() == 0

        spec.roll(5)
        assert spec.allDice.size() == 5

        spec = new DiceRollingSpec(sides:10).roll(-5)
        assert spec.allDice.size() == 5

        spec = new DiceRollingSpec(sides:'%').roll()
        assert spec.allDice.size() == 1
    }

    @Test
    void rollInvalidDice() {
        try {
            new DiceRollingSpec(sides:0).roll(2)
            fail()
        }
        catch (IllegalArgumentException) {}

        try {
            new DiceRollingSpec(sides:-1).roll(2)
            fail()
        }
        catch (IllegalArgumentException) {}
    }

    @Test
    void getView() {
        def spec = new DiceRollingSpec(allDice:[1,2,3,4,5])
        def view = spec.view

        assert spec.allDice == view
        assert !spec.allDice.is(view)
    }

    @Test
    void getTheHighestValue() {
        assert !new DiceRollingSpec().highest

        def spec = new DiceRollingSpec(allDice:[1,3,2,3,5,6,1,6])
        assert spec.highest == 6
    }

    @Test
    void getTheLowestValue() {
        assert !new DiceRollingSpec().lowest

        def spec = new DiceRollingSpec(allDice:[1,3,2,3,5,6,1,6])
        assert spec.lowest == 1
    }

    @Test
    void getTheMean() {
        assert !new DiceRollingSpec().mean

        def spec = new DiceRollingSpec(allDice:[1,3,2,3,5,6,1,6])
        assert spec.mean == 3.375f
    }

    @Test
    void getTheMedian() {
        def spec = new DiceRollingSpec()
        assert !spec.median

        spec = new DiceRollingSpec(allDice:[1,2,4,7,9,10])
        assert spec.median == 5.5
        
        spec = new DiceRollingSpec(allDice:[1,3,5,7,9])
        assert spec.median == 5
    }

    @Test
    void getTheMode() {
    	def spec = new DiceRollingSpec()
        assert !spec.mode

        spec = new DiceRollingSpec(allDice:[1,2,4,7,9,10])
        assert !spec.mode
        
        spec = new DiceRollingSpec(allDice:[1,2,4,7,9,10,4])
        assert spec.mode == [4]

        spec = new DiceRollingSpec(allDice:[1,2,4,7,9,10,4,7,3])
        assert spec.mode == [4,7]

    	spec = new DiceRollingSpec(allDice:[1,2,4,7,9,10,4,7,3,1])
        assert spec.mode == [1,4,7]
    }

    @Test
    void sumDice() {
        assert !new DiceRollingSpec().sum

        def spec = new DiceRollingSpec(allDice:[1,3,2,3,5,6,1,6])
        assert spec.sum == 27
    }

    @Test
    void countDice() {
        assert new DiceRollingSpec(allDice:[1,2,3,4]).count == 4
    }

    @Test
    void getInvalidBestValues() {
        def spec = new DiceRollingSpec(allDice:[5,6])
        (-1..0).each {
            assert spec.best(it) == 6
        }
        assert spec.best(10).allDice == [6,5]
    }

    @Test
    void getTheDefaultBestValue() {
        def spec = new DiceRollingSpec(allDice:[5,6])
        assert spec.best() == 6
    }

    @Test
    void getTheThreeBestValues() {
        def spec = new DiceRollingSpec(allDice:[2,3,4,5,6,7])
        assert spec.best(3).allDice == [7,6,5]
    }

    @Test
    void getInvalidWorstValues() {
        def spec = new DiceRollingSpec(allDice:[6,5])
        (-1..0).each {
            assert spec.worst(it) == 5
        }
        assert spec.worst(10).allDice == spec.view.sort()
    }

    @Test
    void getTheDefaultWorstValue() {
        def spec = new DiceRollingSpec(allDice:[6,5])
        assert spec.worst() == 5
    }

    @Test
    void getTheThreeWorstValues() {
        def spec = new DiceRollingSpec(allDice:[7,6,5,4,3,2])
        assert spec.worst(3).allDice == [2,3,4]
    }

    @Test
    void createModifierFromDiceRoll() {
        def modifier = new DiceRollingSpec(allDice:[1,2,3]).on_each_die
        assert modifier.modifier == 6
    }

    @Test
    void createConditionalModifierFromDiceRoll() {
        def modifier = new DiceRollingSpec(allDice:[1,2,3]).on_each_die.when(2)

        assert modifier.modifier == 6
        assert modifier.condition == 2
    }

    @Test
    void plusTwoDiceRolls() {
        def spec1 = new DiceRollingSpec(sides:6, allDice:[5,6,4,2,3,1])
        def spec2 = new DiceRollingSpec(sides:10, allDice:[1,10,9,5,3,4])
        def spec3 = spec1 + spec2

        assert spec3.sides == 10
        assert spec3.allDice == [5,6,4,2,3,1,1,10,9,5,3,4]
    }

    @Test
    void appendANumberToDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[4,3,5,5,3,1,2])
        def result = spec1 + 50

        assert result.allDice == [4,3,5,5,3,1,2,50]
    }

    @Test
    void plusModifierToDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4,5,6,7])
        def result = spec1 + 1.on_each_die

        assert result.allDice == [2,3,4,5,6,7,8]
    }

    @Test
    void plusConditionalModifierToDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4,5,6,7])
        def result = spec1 + 1.on_each_die.when(1..4)

        assert result.allDice == [2,3,4,5,5,6,7]
    }

    @Test
    void subtractTwoDiceRolls() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4,5])
        def spec2 = new DiceRollingSpec(allDice:[6,7,8,9])
        def result = spec1 - spec2

        assert result.allDice == [1,2,3,4,5,-6,-7,-8,-9]
    }

    @Test
    void subtractANumberFromDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4,5])
        def result = spec1 - 50

        assert result.allDice == [1,2,3,4,5,-50]
    }

    @Test
    void subtractModifierFromDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4,5])
        def result = spec1 - 1.on_each_die

        assert result.allDice == [0,1,2,3,4]
    }

    @Test
    void subtractConditionalModifierFromDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4,5,6,7,8,9])
        def result = spec1 - 1.on_each_die.when{it.is_odd}

        assert result.allDice == [0,2,2,4,4,6,6,8,8]
    }

    @Test
    void multiplyTwoDiceRolls() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        def spec2 = new DiceRollingSpec(allDice:[5,6,7,8])

        assert spec1 * spec2 == 260
    }

    @Test
    void multiplyDiceRollByANumber() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        assert spec1 * 5 == 50
    }

    @Test
    void multiplyDiceRollByModifier() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        def result = spec1 * 2.on_each_die

        assert result.allDice == [2,4,6,8]
    }

    @Test
    void multiplyDiceRollByConditionalModifier() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4,5])
        def result = spec1 * 2.on_each_die.when(4)

        assert result.allDice == [1,2,3,8,5]
    }

    @Test
    void divideTwoDiceRolls() {
        def spec1 = new DiceRollingSpec(allDice:[5,6,7,8])
        def spec2 = new DiceRollingSpec(allDice:[1,2,3,4])

        assert spec1 / spec2 == 2.6
    }

    @Test
    void divideDiceRollByANumber() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        assert spec1 / 5 == 2
    }

    @Test
    void divideDiceRollByModifier() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        def result = spec1 / 2.on_each_die

        assert result.allDice == [0.5, 1, 1.5, 2]
    }

    @Test
    void divideDiceRollByConditionalModifier() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        def result = spec1 / 2.on_each_die.when([2,4])

        assert result.allDice == [1,1,3,2]
    }

    @Test
    void powerTwoDiceRolls() {
        def spec1 = new DiceRollingSpec(sides:6, allDice:[1,2,3,4])
        def spec2 = new DiceRollingSpec(sides:10, allDice:[5,6,7,8])

        assert spec1 ** spec2 == 100000000000000000000000000
    }

    @Test
    void powerANumberToDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        assert spec1 ** 10 == 10000000000
    }

    @Test
    void powerModifierToDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        def result = spec1 ** 10.on_each_die

        assert result.allDice == [1, 1024, 59049, 1048576]
    }

    @Test
    void powerConditionalModifierToDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        def result = spec1 ** 10.on_each_die.when(3..4)

        assert result.allDice == [1,2,59049,1048576]
    }

    @Test
    void modTwoDiceRolls() {
        def spec1 = new DiceRollingSpec(sides:6, allDice:[5,6,7,8])
        def spec2 = new DiceRollingSpec(sides:10, allDice:[1,2,3,4])

        assert spec1 % spec2 == 6
    }

    @Test
    void modANumberByDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        assert spec1 % 7 == 3
    }

    @Test
    void modModifierToDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        def result = spec1 % 2.on_each_die

        assert result.allDice == [1, 0, 1, 0]
    }

    @Test
    void modConditionalModifierToDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        def result = spec1 % 2.on_each_die.when(3..4)

        assert result.allDice == [1,2,1,0]
    }

    @Test
    void negativeDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        def result = -spec1

        assert result.allDice == [-1,-2,-3,-4]
    }

    @Test
    void isDiceSumInARoll() {
    	def spec1 = new DiceRollingSpec(allDice:[1,2,3])

    	def spec2 = new DiceRollingSpec(allDice:[1,3,6])
    	assert spec1 in spec2

    	spec2 = new DiceRollingSpec(allDice:[1,2,3,7,8,9])
    	assert !(spec1 in spec2)
    }

    @Test
    void selectSubsetOfDiceUsingARangeInWhereMethod() {
        def spec = new DiceRollingSpec(allDice:[1,2,3,4,5,6,7,8,9])

        assert spec.where(3..5).allDice == [3,4,5]
        assert spec.count_where(3..5) == 3
    }

    @Test
    void selectSubsetOfDiceUsingAClosureInWhereMethod() {
        def spec = new DiceRollingSpec(allDice:[1,2,3,4,5,6,7,8,9])

        assert spec.where{it.is_even}.allDice == [2,4,6,8]
        assert spec.count_where{it.is_even} == 4
    }

    @Test
    void selectSubsetOfDiceUsingAnArrayInWhereMethod() {
        def spec = new DiceRollingSpec(allDice:[1,2,3,4,5,6,7,8,9])

        assert spec.where([1,3,5,7,9]).allDice == [1,3,5,7,9]
        assert spec.count_where([1,3,5,7,9]) == 5
    }

    @Test
    void selectSubsetOfDiceUsingANumberInWhereMethod() {
        def spec = new DiceRollingSpec(allDice:[1,2,3,4,5,6,7,8,9])

        assert spec.where(5).allDice == [5]
        assert spec.count_where(5) == 1
    }

    @Test
    void selectSubsetOfDiceUsingDiceInWhereMethod() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4,5,6,7,8,9])
        def spec2 = new DiceRollingSpec(allDice:[1,3,4])

        assert spec1.where(spec2).allDice == [1,3,4]
        assert spec1.count_where(spec2) == 3
    }

    @Test
    void compareTwoDiceRolls() {
    	def spec1 = new DiceRollingSpec(allDice:[1,1,1,1,2,2])
        def spec2 = new DiceRollingSpec(allDice:[1,3,4])

    	assert (spec1 <=> spec2) == 0

    	spec1 = new DiceRollingSpec(allDice:[1,1,1,1])
    	assert (spec1 <=> spec2) < 0

    	spec2 = new DiceRollingSpec(allDice:[1,1])
    	assert (spec1 <=> spec2) > 0
    }

    @Test
    void isDiceRollsEquals() {
    	def spec1 = new DiceRollingSpec(allDice:[1,1,1,1,2,2])
        def spec2 = new DiceRollingSpec(allDice:[1,3,4])

        assert spec1 == spec2
        assert spec1 >= spec2
        assert spec1 <= spec2

        spec1 = new DiceRollingSpec(allDice:[1,1,1,1])
        assert spec1 < spec2
        assert spec1 <= spec2

        spec2 = new DiceRollingSpec(allDice:[1,1])
        assert spec1 > spec2
        assert spec1 >= spec2
    }

    @Test
    void iterateThroughEachDieOfDiceRoll() {
        def count = 0
        new DiceRollingSpec(allDice:[1,2,3,4]).for_each_die{count += it}

        assert count == 10
    }

    @Test
    void verifyWhetherTwoDiceRollsAreTheSame() {
        def spec1 = new DiceRollingSpec()
        def spec2 = new DiceRollingSpec()
        assert spec1.same_as(spec2)

        spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        spec2 = new DiceRollingSpec(allDice:[5,6,7,8])
        assert !spec1.same_as(spec2)

        spec2 = new DiceRollingSpec(allDice:[1,2,3,4])
        assert spec1.same_as(spec2)

        spec2 = new DiceRollingSpec(allDice:[4,3,2,1])
        assert spec1.same_as(spec2)
    }

    @Test
    void verifyWhetherADiceRollAndANumberAreTheSame() {
        def spec = new DiceRollingSpec()
    	assert !spec.same_as(1)

    	spec = new DiceRollingSpec(allDice:[1])
    	assert spec.same_as(1)
    }

    @Test
    void verifyWhetherADiceRollAndAnArrayAreTheSame() {
        def spec = new DiceRollingSpec()
        assert !spec.same_as(1..3)

        spec = new DiceRollingSpec(allDice:[1,2,3])
        assert spec.same_as(1..3)
        assert spec.same_as(3..1)
    }

    @Test
    void verifyWhetherADiceRollAndARangeAreTheSame() {
        def spec = new DiceRollingSpec()
        assert !spec.same_as([1,2,3,4])

        spec = new DiceRollingSpec(allDice:[1,2,3,4])
        assert spec.same_as([1,2,3,4])
        assert spec.same_as([4,3,2,1])
    }

    @Test(expected=IllegalArgumentException)
    void verifyWhetherADIceRollAndSomethingElseAreTheSame() {
        def spec = new DiceRollingSpec()
        spec.same_as([:])
    }
}