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
 * DefaultDiceRollingCommand test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class DefaultDiceRollingCommandTest {

    @Before
    void initialize() {
        new GroovyDice().initialize()
    }

    @Test
    void createConditionalModifierFromDiceRoll() {
        def modifier = new DefaultDiceRollingCommand(allDice:[1,2,3]).to_each_die_if(2)

        assert modifier.modifier == 6
        assert modifier.condition == 2
    }

    @Test(expected=IllegalArgumentException)
    void plusInvalidObjectToDiceRoll() {
        def command = new DefaultDiceRollingCommand(sides:6, allDice:[5,6,4,2,3,1])
        command + '10'
    }

    @Test
    void plusTwoDiceRolls() {
        def command1 = new DefaultDiceRollingCommand(sides:6, allDice:[5,6,4,2,3,1])
        def command2 = new DefaultDiceRollingCommand(sides:10, allDice:[1,10,9,5,3,4])
        def command3 = command1 + command2

        assert command3.sides == 10
        assert command3.allDice == [5,6,4,2,3,1,1,10,9,5,3,4]
    }

    @Test
    void appendANumberToDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[4,3,5,5,3,1,2])
        def result = command1 + 50

        assert result.allDice == [4,3,5,5,3,1,2,50]
    }

    @Test
    void plusModifierToDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5,6,7])
        def result = command1 + 1.to_every_die

        assert result.allDice == [2,3,4,5,6,7,8]
    }

    @Test
    void plusConditionalModifierToDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5,6,7])
        def result = command1 + 1.to_each_die_if(1..4)

        assert result.allDice == [2,3,4,5,5,6,7]
    }

    @Test(expected=IllegalArgumentException)
    void subtractInvalidObjectFromDiceRoll() {
        def command = new DefaultDiceRollingCommand(sides:6, allDice:[5,6,4,2,3,1])
        command - '10'
    }

    @Test
    void subtractTwoDiceRolls() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5])
        def command2 = new DefaultDiceRollingCommand(allDice:[6,7,8,9])
        def result = command1 - command2

        assert result.allDice == [1,2,3,4,5,-6,-7,-8,-9]
    }

    @Test
    void subtractANumberFromDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5])
        def result = command1 - 50

        assert result.allDice == [1,2,3,4,5,-50]
    }

    @Test
    void subtractModifierFromDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5])
        def result = command1 - 1.to_every_die

        assert result.allDice == [0,1,2,3,4]
    }

    @Test
    void subtractConditionalModifierFromDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5,6,7,8,9])
        def result = command1 - 1.to_each_die_if{it.is_odd}

        assert result.allDice == [0,2,2,4,4,6,6,8,8]
    }

    @Test(expected=IllegalArgumentException)
    void nultiplyDiceRollByInvalidObject() {
        def command = new DefaultDiceRollingCommand(sides:6, allDice:[5,6,4,2,3,1])
        command * '10'
    }

    @Test
    void multiplyTwoDiceRolls() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        def command2 = new DefaultDiceRollingCommand(allDice:[5,6,7,8])

        assert command1 * command2 == 260
    }

    @Test
    void multiplyDiceRollByANumber() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        assert command1 * 5 == 50
    }

    @Test
    void multiplyDiceRollByModifier() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        def result = command1 * 2.to_every_die

        assert result.allDice == [2,4,6,8]
    }

    @Test
    void multiplyDiceRollByConditionalModifier() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5])
        def result = command1 * 2.to_each_die_if(4)

        assert result.allDice == [1,2,3,8,5]
    }

    @Test(expected=IllegalArgumentException)
    void divideDiceRollByInvalidObject() {
        def command = new DefaultDiceRollingCommand(sides:6, allDice:[5,6,4,2,3,1])
        command / '10'
    }

    @Test
    void divideTwoDiceRolls() {
        def command1 = new DefaultDiceRollingCommand(allDice:[5,6,7,8])
        def command2 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])

        assert command1 / command2 == 2.6
    }

    @Test
    void divideDiceRollByANumber() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        assert command1 / 5 == 2
    }

    @Test
    void divideDiceRollByModifier() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        def result = command1 / 2.to_every_die

        assert result.allDice == [0.5, 1, 1.5, 2]
    }

    @Test
    void divideDiceRollByConditionalModifier() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        def result = command1 / 2.to_each_die_if([2,4])

        assert result.allDice == [1,1,3,2]
    }

    @Test(expected=IllegalArgumentException)
    void powerDiceRollByInvalidObject() {
        def command = new DefaultDiceRollingCommand(sides:6, allDice:[5,6,4,2,3,1])
        command ** '10'
    }

    @Test
    void powerTwoDiceRolls() {
        def command1 = new DefaultDiceRollingCommand(sides:6, allDice:[1,2,3,4])
        def command2 = new DefaultDiceRollingCommand(sides:10, allDice:[5,6,7,8])

        assert command1 ** command2 == 100000000000000000000000000
    }

    @Test
    void powerNumberToDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        assert command1 ** 10 == 10000000000
    }

    @Test
    void powerModifierToDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        def result = command1 ** 10.to_every_die

        assert result.allDice == [1, 1024, 59049, 1048576]
    }

    @Test
    void powerConditionalModifierToDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        def result = command1 ** 10.to_each_die_if(3..4)

        assert result.allDice == [1,2,59049,1048576]
    }

    @Test(expected=IllegalArgumentException)
    void modDiceRollByInvalidObject() {
        def command = new DefaultDiceRollingCommand(sides:6, allDice:[5,6,4,2,3,1])
        command % '10'
    }

    @Test
    void modTwoDiceRolls() {
        def command1 = new DefaultDiceRollingCommand(sides:6, allDice:[5,6,7,8])
        def command2 = new DefaultDiceRollingCommand(sides:10, allDice:[1,2,3,4])

        assert command1 % command2 == 6
    }

    @Test
    void modANumberByDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        assert command1 % 7 == 3
    }

    @Test
    void modModifierToDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        def result = command1 % 2.to_every_die

        assert result.allDice == [1, 0, 1, 0]
    }

    @Test
    void modConditionalModifierToDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        def result = command1 % 2.to_each_die_if(3..4)

        assert result.allDice == [1,2,1,0]
    }

    @Test
    void negativeDiceRoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        def result = -command1

        assert result.allDice == [-1,-2,-3,-4]
    }

    @Test
    void isDiceSumInARoll() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3])

        def command2 = new DefaultDiceRollingCommand(allDice:[1,3,6])
        assert command1 in command2

        command2 = new DefaultDiceRollingCommand(allDice:[1,2,3,7,8,9])
        assert !(command1 in command2)
    }

    @Test
    void selectSubsetOfDiceUsingARange() {
        def command = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5,6,7,8,9])

        assert command.only_if(3..5).allDice == [3,4,5]
        assert command.only_if(3..5).count == 3
    }

    @Test
    void selectSubsetOfDiceUsingAClosure() {
        def command = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5,6,7,8,9])

        assert command.only_if{it.is_even}.allDice == [2,4,6,8]
        assert command.only_if{it.is_even}.count == 4
    }

    @Test
    void selectSubsetOfDiceUsingAnArray() {
        def command = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5,6,7,8,9])

        assert command.only_if([1,3,5,7,9]).allDice == [1,3,5,7,9]
        assert command.only_if([1,3,5,7,9]).count == 5
    }

    @Test
    void selectSubsetOfDiceUsingANumber() {
        def command = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5,6,7,8,9])

        assert command.only_if(5).allDice == [5]
        assert command.only_if(5).count == 1
    }

    @Test
    void selectSubsetOfDiceUsingDice() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4,5,6,7,8,9])
        def command2 = new DefaultDiceRollingCommand(allDice:[1,3,4])

        assert command1.only_if(command2).allDice == [1,3,4]
        assert command1.only_if(command2).count == 3
    }

    @Test
    void compareTwoDiceRolls() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,1,1,1,2,2])
        def command2 = new DefaultDiceRollingCommand(allDice:[1,3,4])

        assert (command1 <=> command2) == 0

        command1 = new DefaultDiceRollingCommand(allDice:[1,1,1,1])
        assert (command1 <=> command2) < 0

        command2 = new DefaultDiceRollingCommand(allDice:[1,1])
        assert (command1 <=> command2) > 0
    }

    @Test(expected=ClassCastException)
    void compareDiceRollWithInvalidObject() {
        def command = new DefaultDiceRollingCommand(allDice:[1,1,1,1])
        command <=> '10'
    }

    @Test
    void isDiceRollsEquals() {
        def command1 = new DefaultDiceRollingCommand(allDice:[1,1,1,1,2,2])
        def command2 = new DefaultDiceRollingCommand(allDice:[1,3,4])

        assert command1.equals(command2)

        command1 = new DefaultDiceRollingCommand(allDice:[1,1,1,1])
        assert !command1.equals(command2)

        command2 = new DefaultDiceRollingCommand(allDice:[1,1])
        assert !command1.equals(command2)
    }

    @Test(expected=ClassCastException)
    void isDiceRollEqualToInvalidObject() {
        def command = new DefaultDiceRollingCommand(allDice:[1,1,1,1])
        command.equals('10')
    }

    @Test(expected=IllegalArgumentException)
    void passInvalidArgumentToSameMethod() {
        def command1 = new DefaultDiceRollingCommand()
        assert command1.same_as('10')
    }

    @Test
    void verifyWhetherTwoDiceRollsAreTheSame() {
        def command1 = new DefaultDiceRollingCommand()
        def command2 = new DefaultDiceRollingCommand()
        assert command1.same_as(command2)

        command1 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        command2 = new DefaultDiceRollingCommand(allDice:[5,6,7,8])
        assert !command1.same_as(command2)

        command2 = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        assert command1.same_as(command2)

        command2 = new DefaultDiceRollingCommand(allDice:[4,3,2,1])
        assert command1.same_as(command2)
    }

    @Test
    void verifyWhetherADiceRollAndANumberAreTheSame() {
        def command = new DefaultDiceRollingCommand()
        assert !command.same_as(1)

        command = new DefaultDiceRollingCommand(allDice:[1])
        assert command.same_as(1)
    }

    @Test
    void verifyWhetherADiceRollAndAnArrayAreTheSame() {
        def command = new DefaultDiceRollingCommand()
        assert !command.same_as(1..3)

        command = new DefaultDiceRollingCommand(allDice:[1,2,3])
        assert command.same_as(1..3)
        assert command.same_as(3..1)
    }

    @Test
    void verifyWhetherADiceRollAndARangeAreTheSame() {
        def command = new DefaultDiceRollingCommand()
        assert !command.same_as([1,2,3,4])

        command = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        assert command.same_as([1,2,3,4])
        assert command.same_as([4,3,2,1])
    }

    @Test(expected=IllegalArgumentException)
    void verifyWhetherADIceRollAndSomethingElseAreTheSame() {
        def command = new DefaultDiceRollingCommand(allDice:[1,2,3,4])
        command.same_as([:])
    }
}