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
 * DefaultNumberPatcher test cases.
 *
 * @author Daniel F. Martins
 */
class DefaultNumberPatcherTest {

    def config

    @Before
    void initialize()  {
        config = new GroovyDice()
        config.numberGenerator = new StubGenerator()
        config.initialize()
    }

    private void assertDice(spec) {
        assert spec instanceof AbstractDiceRollingSpec
        spec.view.each {
            assert it == spec.sides
        }
    }

    @Test
    void rollSpec() {
        def spec = new DefaultNumberPatcher(config:config).rollSpec(5, 6)

        assert spec.sides == 5
        assert spec.count == 6
    }

    @Test
    void createSimpleModifier() {
        def m = new DefaultNumberPatcher(config:config).createModifier(-5)
        assert m.modifier == -5
    }

    @Test
    void createConditionalModifier() {
        def m = new DefaultNumberPatcher(config:config).createModifier(-5, 'condition')

        assert m.modifier == -5
        assert m.condition == 'condition'
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
        assertDice(1.d)
        assertDice(1.D)
    }

    @Test
    void rollOneTenSidedDie() {
        assertDice(1.d10)
        assertDice(1.D10)
    }

    @Test
    void rollTwoTwelveSidedDice() {
        assertDice(2.d12)
    }

    @Test
    void rollDiceWithNegativeMultiplier() {
        def spec1 = (-5).d10
        def spec2 = -5.d10

        assert spec1 instanceof DefaultDiceRollingSpec
        assert spec1.view.size() == 5
        assert spec1.view.findAll{it > 0}.size == 0

        assert spec2 instanceof DefaultDiceRollingSpec
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

    @Test(expected=MissingPropertyException)
    void rollXSidedDie() {
        5.dx
    }

    @Test
    void rollDynamicDice() {
        assertDice(1.d(6))
        assertDice(1.D(6.5))
    }

    @Test
    void rollDynamicDiceUsingStringAsSideNumber() {
        assertDice(1.d('6'))
        assertDice(1.D('6.5'))
    }

    @Test
    void rollDynamicPercentileDice() {
        assertDice(1.d('%'))
        assertDice(1.D('%'))
    }

    @Test
    void rollDynamicDiceUsingADiceRollAsSideNumber() {
        def spec = new DefaultDiceRollingSpec(allDice:[1,2,4])
        assertDice(1.d(spec))
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

        assertDice(1.'d%')
        assertDice(1.'D%')
        assertDice(2.pd)
    }

    @Test
    void plusANumberToDiceRoll() {
        def spec1 = 10.d20
        def result = 5 + spec1

        assert result.sum == 5 + spec1.sum
    }

    @Test
    void subtractANumberFromDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        def result = 5 - spec1

        assert result.sum == -5
    }

    @Test
    void multiplyANumberByDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        assert 5 * spec1 == 50
    }

    @Test
    void divideANumberByDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        assert 5 / spec1 == 0.5
    }

    @Test
    void powerANumberByDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        assert 5 ** spec1 == 9765625
    }

    @Test
    void modANumberByDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])
        assert 14 % spec1 == 4
    }

    @Test
    void createModifierFromANumber() {
        def modifier = 5.to_every_die
        assert modifier.modifier == 5
    }

    @Test
    void createConditionalModifierFromANumber() {
        def modifier = 5.to_each_die_if(2)

        assert modifier.modifier == 5
        assert modifier.condition == 2
    }

    @Test
    void isNumberInDiceRoll() {
        def spec1 = new DefaultDiceRollingSpec(allDice:[1,2,3,4])

        assert 2 in spec1
        assert !(5 in spec1)
    }

    @Test
    void createBestFilterFromANumber() {
        assert 2.best == 2
        assert 2.0.best == 2.0

        try {
            0.best
            fail()
        }
        catch (IllegalArgumentException) {}
    }

    @Test
    void createWorstFilterFromANumber() {
        assert 2.worst == -2
        assert 2.0.worst == -2.0

        try {
            0.worst
            fail()
        }
        catch (IllegalArgumentException) {}
    }
}

/**
 * Dumb number generator.
 *
 * @author Daniel F. Martins
 */
class StubGenerator implements RandomNumberGenerator {
    def generateNumber(number) {
        number == '%' ? 100 : number
    }
}