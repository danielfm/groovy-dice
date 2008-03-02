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
 * DiceArithmeticPlugin test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F.
 * Martins</a>
 */
class DiceArithmeticPluginTest {

    @BeforeClass
    static void initialize()  {
        new GroovyDice().initialize()
    }

    @Test
    void negativeDiceRoll() {
        assert -new DiceRollingCommand(allDice:[1,2,3]).allDice == [-1,-2,-3]
    }

    @Test
    void plusTwoDiceRolls() {
        def c1 = new DiceRollingCommand(allDice:[1,2,3])
        def c2 = new DiceRollingCommand(allDice:[1,2,3])
        assert (c1+c2).allDice == [1,2,3,1,2,3]
    }

    @Test
    void plusNumberToDiceRoll() {
        def c = new DiceRollingCommand(allDice:[1,2,3])
        assert (c+4).allDice == [1,2,3,4]
    }

    @Test
    void plusDiceRollToNumber() {
        def c = new DiceRollingCommand(allDice:[1,2,3])
        assert (4+c).allDice == [1,2,3,4]
    }

    @Test
    void subtractTwoDiceRolls() {
        def c1 = new DiceRollingCommand(allDice:[1,2,3])
        def c2 = new DiceRollingCommand(allDice:[1,2,3])
        assert (c1-c2).allDice == [1,2,3,-1,-2,-3]
    }

    @Test
    void subtractNumberFromDiceRoll() {
        def c = new DiceRollingCommand(allDice:[1,2,3])
        assert (c-4).allDice == [1,2,3,-4]
    }

    @Test
    void subtractDiceRollFromNumber() {
        def c = new DiceRollingCommand(allDice:[1,2,3])
        assert (4-c).allDice == [-1,-2,-3,4]
    }

    @Test
    void multiplyTwoDiceRolls() {
        def c1 = new DiceRollingCommand(allDice:[1,2,3])
        def c2 = new DiceRollingCommand(allDice:[1,2,3])
        assert c1*c2 == 36
    }

    @Test
    void multiplyNumberByDiceRoll() {
        def c = new DiceRollingCommand(allDice:[1,2,3])
        assert 4*c == 24
    }

    @Test
    void multiplyDiceRollByNumber() {
        def c = new DiceRollingCommand(allDice:[1,2,3])
        assert c*4 == 24
    }

    @Test
    void divideTwoDiceRolls() {
        def c1 = new DiceRollingCommand(allDice:[1,2,3])
        def c2 = new DiceRollingCommand(allDice:[1,2,3])
        assert c1/c2 == 1
    }

    @Test
    void divideNumberByDiceRoll() {
        def c = new DiceRollingCommand(allDice:[1,2,3])
        assert 3/c == 0.5
    }

    @Test
    void divideDiceRollByNumber() {
        def c = new DiceRollingCommand(allDice:[1,2,3])
        assert c/3 == 2
    }

    @Test
    void diceRollRaisedToThePowerOfAnother() {
        def c1 = new DiceRollingCommand(allDice:[1,2,3])
        def c2 = new DiceRollingCommand(allDice:[1,2,3])
        assert c1**c2 == 46656
    }

    @Test
    void numberRaisedToThePowerOfDiceRoll() {
        def c = new DiceRollingCommand(allDice:[1,2,3])
        assert 3**c == 729
    }

    @Test
    void diceRollRaisedToThePowerOfNumber() {
        def c = new DiceRollingCommand(allDice:[1,2,3])
        assert c**3 == 216
    }

    @Test
    void moduloTwoDiceRolls() {
        def c1 = new DiceRollingCommand(allDice:[1,2,3])
        def c2 = new DiceRollingCommand(allDice:[2,3,4])
        assert c1%c2 == 6
    }

    @Test
    void numberModuloDiceRoll() {
        def c = new DiceRollingCommand(allDice:[1,2,3])
        assert 10%c == 4
    }

    @Test
    void diceRollModuloNumber() {
        def c = new DiceRollingCommand(allDice:[1,2,3])
        assert c%4 == 2
    }

    @Test
    void isDiceRollTheSameAsNumber() {
        assert new DiceRollingCommand(allDice:[1]).same_as(1)
        assert !new DiceRollingCommand(allDice:[1]).same_as(2)
    }

    @Test
    void isDiceRollTheSameAsList() {
        assert new DiceRollingCommand(allDice:[1,2,3]).same_as([1,2,3])
        assert !new DiceRollingCommand(allDice:[1,2,3]).same_as([1,2,3,4])

        assert new DiceRollingCommand(allDice:[1,2,3]).same_as(1..3)
        assert !new DiceRollingCommand(allDice:[1,2,3]).same_as(1..4)
    }

    @Test
    void isDiceRollTheSameAsAnother() {
        assert new DiceRollingCommand(allDice:[1,2,3]).same_as(
            new DiceRollingCommand(allDice:[3,2,1]))

        assert !new DiceRollingCommand(allDice:[1,2,3]).same_as(
            new DiceRollingCommand(allDice:[4,2,1]))
    }

    @Test(expected=IllegalArgumentException)
    void isDiceRollTheSameAsInvalidObject() {
        new DiceRollingCommand(allDice:[1,2,3]).same_as('hey')
    }
}
