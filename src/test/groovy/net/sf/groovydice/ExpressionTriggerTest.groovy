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
 * ExpressionTrigger test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class DefaultNumberPatcherTest {

    def config

    @Before
    void initialize()  {
        config = new GroovyDice(numberGenerator:new StubGenerator())
        config.initialize()
    }

    /**
     * Assert that the given dice rolling command is valid.
     * @param command Dice rolling command.
     */
    private void assertDice(command) {
        assert command instanceof AbstractDiceRollingCommand
        command.view.each {
            assert it == command.sides
        }
    }

    /**
     * Create a new <code>ExpressionTrigger</code> object.
     * @return A new <code>ExpressionTrigger</code> object.
     */
    private def getTrigger() {
        new ExpressionTrigger(config:config)
    }

    @Test
    void rollCommand() {
        def command = getTrigger().rollCommand(5, 6)

        assert command.sides == 5
        assert command.count == 6
    }

    @Test
    void createSimpleModifier() {
        def m = getTrigger().createModifier(-5)
        assert m.modifier == -5
    }

    @Test
    void createConditionalModifier() {
        def m = getTrigger().createModifier(-5, 'condition')

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
        def command1 = (-5).d10
        def command2 = -5.d10

        assert command1 instanceof DefaultDiceRollingCommand
        assert command1.view.size() == 5
        assert command1.view.findAll{it > 0}.size == 0

        assert command2 instanceof DefaultDiceRollingCommand
        assert command2.view.size() == 5
        assert command2.view.findAll{it > 0}.size == 0
    }

    @Test
    void rollDiceZeroTimes() {
        def command = 0.d10

        assert command.allDice == []
        assert command.count == 0

        assert !command.worst_die
        assert !command.best_die
        assert !command.mean
        assert !command.median
        assert !command.mode
        assert !command.sum

        assert command.best(3) == 0
        assert command.worst(3) == 0
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
        def command = new DefaultDiceRollingCommand(allDice:[1,2,4])
        assertDice(1.d(command))
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
        def command1 = 10.d20
        def result = 5 + command1

        assert result.sum == 5 + command1.sum
    }

    @Test
    void subtractANumberFromDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        def result = 5 - command1

        assert result.sum == -5
    }

    @Test
    void multiplyANumberByDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        assert 5 * command1 == 50
    }

    @Test
    void divideANumberByDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        assert 5 / command1 == 0.5
    }

    @Test
    void powerANumberByDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        assert 5 ** command1 == 9765625
    }

    @Test
    void modANumberByDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        assert 14 % command1 == 4
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
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])

        assert 2 in command1
        assert !(5 in command1)
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