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
 * DiceRollin test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class DiceRollingCommandTest {

    def config

    @Before
    void initialize() {
        config = new GroovyDice(numberGenerator:new GeneratorStub())
    }

    @Test
    void getView() {
        def c = new DiceRollingCommand(allDice:[1,2,3,4])
        def result = c.view
        assert result == c.allDice
        assert !result.is(c.allDice)
    }

    @Test
    void rollOneDie() {
        assert new DiceRollingCommand(config:config, sides:6).roll().allDice == [6]
        assert new DiceRollingCommand(config:config, sides:6).roll(1).allDice == [6]
    }

    @Test
    void rollThreeDice() {
        assert new DiceRollingCommand(config:config, sides:6).roll(3).allDice == [6,6,6]
    }

    @Test
    void rollMinusThreeDice() {
        assert new DiceRollingCommand(config:config, sides:6).roll(-3).allDice == [-6,-6,-6]
    }

    @Test
    void isEqualsInvalidObject() {
        assert !new DiceRollingCommand(allDice:[1,2,3,4]).equals('other')
    }

    @Test
    void isEquals() {
        assert new DiceRollingCommand(allDice:[1,2,3,4]).equals(
            new DiceRollingCommand(allDice:[5,4,1]))

        assert !new DiceRollingCommand(allDice:[1,2,3,4]).equals(
            new DiceRollingCommand(allDice:[5,4,2]))
    }

    @Test(expected=ClassCastException)
    void isComparableWithInvalidObject() {
        new DiceRollingCommand(allDice:[1,2,3,4]).compareTo('something')
    }

    @Test
    void isComparable() {
        assert new DiceRollingCommand(allDice:[1,2,3,4]).compareTo(
            new DiceRollingCommand(allDice:[1,2,3,4])) == 0

        assert new DiceRollingCommand(allDice:[1,2,3,4]).compareTo(
            new DiceRollingCommand(allDice:[1,2,3])) > 0

        assert new DiceRollingCommand(allDice:[1,2,3]).compareTo(
            new DiceRollingCommand(allDice:[1,2,3,4])) < 0
    }

    @Test
    void getHashCode() {
        assert new DiceRollingCommand().hashCode()
        assert new DiceRollingCommand(allDice:[1,2,3,4]).hashCode()
    }
}

/**
 * Dumb number generator.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class GeneratorStub {
    def next = {it}
}