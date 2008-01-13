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
 * NumberPatcher test cases.
 *
 * @author Daniel F. Martins
 */
class NumberPatcherTest {

    @Before
    void initialize()  {
        new GroovyDice().initialize()
    }

    /**
     * Check whether the DiceRollingSpec dice are inside the expected range.
     * @param spec A DiceRollingSpec object.
     * @param range Range of values which all dice must fit in.
     * @return Whether the given DiceRollingSpec dice are valid.
     */
    private assertDiceInRange(spec, range) {
        assert spec.view.size() > 0
        spec.view.each {
            assert it in range
        }
    }

    @Test
    void numberIsEven() {
        assert !5.is_even
        assert !5.5.is_even

        assert 6.is_even
        assert 6.6.is_even
    }

    @Test
    void numberIsOdd() {
        assert !10.is_odd
        assert !10.10.is_odd

        assert 13.is_odd
        assert 13.13.is_odd
    }

    @Test
    void rollOneDefaultDie() {
        (0..50).each {
            assertDiceInRange(1.d, (1..6))
            assertDiceInRange(1.D, (1..6))
        }
    }

    @Test
    void rollOneTenSidedDie() {
        (0..50).each {
            assertDiceInRange(1.d10, (1..10))
            assertDiceInRange(1.D10, (1..10))
        }
    }

    @Test
    void rollTwoTwelveSidedDice() {
        (0..100).each {
            assertDiceInRange(2.d12, (1..12))
        }
    }

    @Test
    void rollDiceWithNegativeMultiplier() {
        def spec1 = (-5).d10
        def spec2 = -5.d10

        assert spec1 instanceof DiceRollingSpec
        assert spec1.view.size() == 5
        assert spec1.view.findAll{it > 0}.size == 0

        assert spec2 instanceof DiceRollingSpec
        assert spec2.view.size() == 5
        assert spec2.view.findAll{it > 0}.size == 0
    }

    @Test
    void rollDiceZeroTimes() {
        def spec = 0.d10

        assert spec.allDice == []
        assert spec.count == 0

        assert !spec.worst_die
        assert !spec.best_die
        assert !spec.mean
        assert !spec.median
        assert !spec.mode
        assert !spec.sum

        assert spec.best(3) == 0
        assert spec.worst(3) == 0
    }

    @Test(expected=IllegalArgumentException)
    void rollZeroSidedDie() {
        5.d0
    }

    @Test(expected=MissingPropertyException)
    void rollXSidedDie() {
        5.dx
    }

    @Test
    void rollDynamicDice() {
        (0..50).each {
            assertDiceInRange(1.d(6), (1..6))
            assertDiceInRange(1.D(6.5), (1..6))
        }
    }

    @Test
    void rollDynamicDiceUsingStringAsSideNumber() {
        (0..50).each {
            assertDiceInRange(1.d('6'), (1..6))
            assertDiceInRange(1.D('6.5'), (1..6))
        }
    }

    @Test
    void rollDynamicPercentileDice() {
    	(0..50).each {
            assertDiceInRange(1.d('%'), (1..100))
            assertDiceInRange(1.D('%'), (1..100))
        }
    }

    @Test
    void rollDynamicDiceUsingADiceRollAsSideNumber() {
        def spec = new DiceRollingSpec(allDice:[1,2,4])
        (0..50).each {
            assertDiceInRange(1.d(spec), (1..7))
        }
    }

    @Test(expected=IllegalArgumentException)
    void rollZeroSidedDynamicDie() {
        5.d(0)
    }

    @Test(expected=IllegalArgumentException)
    void rollMinusOneSidedDynamicDie() {
        5.D(-1)
    }

    @Test(expected=MissingPropertyException)
    void accessNumberMissingProperty() {
        5.foo
    }

    @Test
    void rollAPercentileDie() {
        assert 2.'D%'.sides == 100
        assert 2.pd.sides == 100

        assert 2.pD
        assert 2.Pd
        assert 2.PD

        (0..50).each {
            assert assertDiceInRange(1.'d%', 1..100)
            assert assertDiceInRange(1.'D%', 1..100)
        }

        (0..50).each {
            assert assertDiceInRange(2.pd, 1..100)
        }
    }

    @Test
    void plusANumberToDiceRoll() {
        def spec1 = 10.d20
        def result = 5 + spec1

        assert result.sum == 5 + spec1.sum
    }

    @Test
    void subtractANumberFromDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        def result = 5 - spec1

        assert result.sum == -5
    }

    @Test
    void multiplyANumberByDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        assert 5 * spec1 == 50
    }

    @Test
    void divideANumberByDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        assert 5 / spec1 == 0.5
    }

    @Test
    void powerANumberByDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        assert 5 ** spec1 == 9765625
    }

    @Test
    void modANumberByDiceRoll() {
        def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])
        assert 14 % spec1 == 4
    }

    @Test
    void createModifierFromANumber() {
        def modifier = 5.on_every_die
        assert modifier.modifier == 5
    }

    @Test
    void createConditionalModifierFromANumber() {
        def modifier = 5.on_every_die.when(2)

        assert modifier.modifier == 5
        assert modifier.condition == 2
    }

    @Test
    void isNumberInDiceRoll() {
    	def spec1 = new DiceRollingSpec(allDice:[1,2,3,4])

    	assert 2 in spec1
    	assert !(5 in spec1)
    }
}