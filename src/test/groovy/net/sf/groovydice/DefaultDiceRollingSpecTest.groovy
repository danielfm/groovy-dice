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
 * DefaultDiceRollingSpec test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class DefaultDiceRollingSpecTest {

    @Before
    void initialize() {
        new GroovyDice().initialize()
	}

    @Test
    void createConditionalModifierFromDiceRoll() {
        def modifier = new DefaultDiceRollingSpec(allDice:[1,2,3]).to_each_die_if(2)

        assert modifier.modifier == 6
        assert modifier.condition == 2
    }

    @Test
    void plusTwoDiceRolls() {
        def spec1 = new DefaultDiceRollingSpec(sides:6, allDice:[5,6,4,2,3,1])
        def spec2 = new DefaultDiceRollingSpec(sides:10, allDice:[1,10,9,5,3,4])
        def spec3 = spec1 + spec2

        assert spec3.sides == 10
        assert spec3.allDice == [5,6,4,2,3,1,1,10,9,5,3,4]
    }

    @Test
    void appendANumberToDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[4,3,5,5,3,1,2])
        def result = spec1 + 50

        assert result.allDice == [4,3,5,5,3,1,2,50]
    }

    @Test
    void plusModifierToDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5,6,7])
        def result = spec1 + 1.to_every_die

        assert result.allDice == [2,3,4,5,6,7,8]
    }

    @Test
    void plusConditionalModifierToDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5,6,7])
        def result = spec1 + 1.to_each_die_if(1..4)

        assert result.allDice == [2,3,4,5,5,6,7]
    }

    @Test
    void subtractTwoDiceRolls() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5])
        def spec2 = new DefaultDiceRollingSpec(allDice:[6,7,8,9])
        def result = spec1 - spec2

        assert result.allDice == [1,2,3,4,5,-6,-7,-8,-9]
    }

    @Test
    void subtractANumberFromDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5])
        def result = spec1 - 50

        assert result.allDice == [1,2,3,4,5,-50]
    }

    @Test
    void subtractModifierFromDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5])
        def result = spec1 - 1.to_every_die

        assert result.allDice == [0,1,2,3,4]
    }

    @Test
    void subtractConditionalModifierFromDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5,6,7,8,9])
        def result = spec1 - 1.to_each_die_if{it.is_odd}

        assert result.allDice == [0,2,2,4,4,6,6,8,8]
    }

    @Test
    void multiplyTwoDiceRolls() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        def spec2 = new DefaultDiceRollingSpec(allDice:[5,6,7,8])

        assert spec1 * spec2 == 260
    }

    @Test
    void multiplyDiceRollByANumber() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        assert spec1 * 5 == 50
    }

    @Test
    void multiplyDiceRollByModifier() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        def result = spec1 * 2.to_every_die

        assert result.allDice == [2,4,6,8]
    }

    @Test
    void multiplyDiceRollByConditionalModifier() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5])
        def result = spec1 * 2.to_each_die_if(4)

        assert result.allDice == [1,2,3,8,5]
    }

    @Test
    void divideTwoDiceRolls() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[5,6,7,8])
        def spec2 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])

        assert spec1 / spec2 == 2.6
    }

    @Test
    void divideDiceRollByANumber() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        assert spec1 / 5 == 2
    }

    @Test
    void divideDiceRollByModifier() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        def result = spec1 / 2.to_every_die

        assert result.allDice == [0.5, 1, 1.5, 2]
    }

    @Test
    void divideDiceRollByConditionalModifier() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        def result = spec1 / 2.to_each_die_if([2,4])

        assert result.allDice == [1,1,3,2]
    }

    @Test
    void powerTwoDiceRolls() {
        def spec1 = new DefaultDiceRollingSpec(sides:6, allDice:[1,2,3,4])
        def spec2 = new DefaultDiceRollingSpec(sides:10, allDice:[5,6,7,8])

        assert spec1 ** spec2 == 100000000000000000000000000
    }

    @Test
    void powerANumberToDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        assert spec1 ** 10 == 10000000000
    }

    @Test
    void powerModifierToDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        def result = spec1 ** 10.to_every_die

        assert result.allDice == [1, 1024, 59049, 1048576]
    }

    @Test
    void powerConditionalModifierToDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        def result = spec1 ** 10.to_each_die_if(3..4)

        assert result.allDice == [1,2,59049,1048576]
    }

    @Test
    void modTwoDiceRolls() {
        def spec1 = new DefaultDiceRollingSpec(sides:6, allDice:[5,6,7,8])
        def spec2 = new DefaultDiceRollingSpec(sides:10, allDice:[1,2,3,4])

        assert spec1 % spec2 == 6
    }

    @Test
    void modANumberByDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        assert spec1 % 7 == 3
    }

    @Test
    void modModifierToDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        def result = spec1 % 2.to_every_die

        assert result.allDice == [1, 0, 1, 0]
    }

    @Test
    void modConditionalModifierToDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        def result = spec1 % 2.to_each_die_if(3..4)

        assert result.allDice == [1,2,1,0]
    }

    @Test
    void negativeDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        def result = -spec1

        assert result.allDice == [-1,-2,-3,-4]
    }

    @Test
    void isDiceSumInARoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3])

        def spec2 = new DefaultDiceRollingSpec(allDice:[1,3,6])
        assert spec1 in spec2

        spec2 = new DefaultDiceRollingSpec(allDice:[1,2,3,7,8,9])
        assert !(spec1 in spec2)
    }

    @Test
    void selectSubsetOfDiceUsingARange() {
        def spec = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5,6,7,8,9])

        assert spec.only_if(3..5).allDice == [3,4,5]
        assert spec.only_if(3..5).count == 3
    }

    @Test
    void selectSubsetOfDiceUsingAClosure() {
        def spec = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5,6,7,8,9])

        assert spec.only_if{it.is_even}.allDice == [2,4,6,8]
        assert spec.only_if{it.is_even}.count == 4
    }

    @Test
    void selectSubsetOfDiceUsingAnArray() {
        def spec = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5,6,7,8,9])

        assert spec.only_if([1,3,5,7,9]).allDice == [1,3,5,7,9]
        assert spec.only_if([1,3,5,7,9]).count == 5
    }

    @Test
    void selectSubsetOfDiceUsingANumber() {
        def spec = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5,6,7,8,9])

        assert spec.only_if(5).allDice == [5]
        assert spec.only_if(5).count == 1
    }

    @Test
    void selectSubsetOfDiceUsingDice() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4,5,6,7,8,9])
        def spec2 = new DefaultDiceRollingSpec(allDice:[1,3,4])

        assert spec1.only_if(spec2).allDice == [1,3,4]
        assert spec1.only_if(spec2).count == 3
    }

    @Test
    void compareTwoDiceRolls() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,1,1,1,2,2])
        def spec2 = new DefaultDiceRollingSpec(allDice:[1,3,4])

        assert (spec1 <=> spec2) == 0

        spec1 = new DefaultDiceRollingSpec(allDice:[1,1,1,1])
        assert (spec1 <=> spec2) < 0

        spec2 = new DefaultDiceRollingSpec(allDice:[1,1])
        assert (spec1 <=> spec2) > 0
    }

    @Test
    void isDiceRollsEquals() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,1,1,1,2,2])
        def spec2 = new DefaultDiceRollingSpec(allDice:[1,3,4])

        assert spec1 == spec2
        assert spec1 >= spec2
        assert spec1 <= spec2

        spec1 = new DefaultDiceRollingSpec(allDice:[1,1,1,1])
        assert spec1 < spec2
        assert spec1 <= spec2

        spec2 = new DefaultDiceRollingSpec(allDice:[1,1])
        assert spec1 > spec2
        assert spec1 >= spec2
    }

    @Test
    void verifyWhetherTwoDiceRollsAreTheSame() {
        def spec1 = new DefaultDiceRollingSpec()
        def spec2 = new DefaultDiceRollingSpec()
        assert spec1.same_as(spec2)

        spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        spec2 = new DefaultDiceRollingSpec(allDice:[5,6,7,8])
        assert !spec1.same_as(spec2)

        spec2 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        assert spec1.same_as(spec2)

        spec2 = new DefaultDiceRollingSpec(allDice:[4,3,2,1])
        assert spec1.same_as(spec2)
    }

    @Test
    void verifyWhetherADiceRollAndANumberAreTheSame() {
        def spec = new DefaultDiceRollingSpec()
        assert !spec.same_as(1)

        spec = new DefaultDiceRollingSpec(allDice:[1])
        assert spec.same_as(1)
    }

    @Test
    void verifyWhetherADiceRollAndAnArrayAreTheSame() {
        def spec = new DefaultDiceRollingSpec()
        assert !spec.same_as(1..3)

        spec = new DefaultDiceRollingSpec(allDice:[1,2,3])
        assert spec.same_as(1..3)
        assert spec.same_as(3..1)
    }

    @Test
    void verifyWhetherADiceRollAndARangeAreTheSame() {
        def spec = new DefaultDiceRollingSpec()
        assert !spec.same_as([1,2,3,4])

        spec = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        assert spec.same_as([1,2,3,4])
        assert spec.same_as([4,3,2,1])
    }

    @Test(expected=IllegalArgumentException)
    void verifyWhetherADIceRollAndSomethingElseAreTheSame() {
        def spec = new DefaultDiceRollingSpec()
        spec.same_as([:])
    }
}