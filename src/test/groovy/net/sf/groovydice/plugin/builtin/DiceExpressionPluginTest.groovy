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
 * DiceExpressionPlugin test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class DiceExpressionPluginTest {

    @BeforeClass
    static void initialize()  {
        new GroovyDice().initialize()
    }

    @Test
    void rollDefaultDiceWithd() {
        def command = 2.d
        assert command.sides == 6
        assert command.allDice.size() == 2

        command = 3.d()
        assert command.sides == 6
        assert command.allDice.size() == 3
    }

    @Test
    void rollDefaultDiceWithD() {
        def command = 2.D
        assert command.sides == 6
        assert command.allDice.size() == 2

        command = 3.D()
        assert command.sides == 6
        assert command.allDice.size() == 3
    }

    @Test
    void rollDiceWithd() {
        def command = 2.d(10)
        assert command.sides == 10
        assert command.allDice.size() == 2
    }

    @Test
    void rollDiceWithD() {
        def command = 2.D(10)
        assert command.sides == 10
        assert command.allDice.size() == 2
    }

    @Test(expected=IllegalArgumentException)
    void rollInvalidDiceTypeWithd() {
        2.d('ten')
    }

    @Test(expected=IllegalArgumentException)
    void rollInvalidDiceTypeWithD() {
        2.D('ten')
    }

    @Test
    void rollDiceWithDiceRollAsDiceTypeWithd() {
        def type = 2.d
        def command = 3.d(type)

        assert command.sides == type.allDice.sum()
        assert command.allDice.size() == 3
    }

    @Test
    void rollDiceWithDiceRollAsDiceTypeWithD() {
        def type = 2.d
        def command = 3.D(type)

        assert command.sides == type.allDice.sum()
        assert command.allDice.size() == 3
    }

    @Test
    void rollDynamicDiceWithd() {
        def command = 2.d10
        assert command.sides == 10
        assert command.allDice.size() == 2
    }

    @Test
    void rollDynamicDiceWithD() {
        def command = 2.D10
        assert command.sides == 10
        assert command.allDice.size() == 2
    }

    @Test
    void rollPercentileDiceWithpd() {
        def command = 2.pd
        assert command.sides == 100
        assert command.allDice.size() == 2
    }

    @Test
    void rollPercentileDiceWithPd() {
        def command = 2.Pd
        assert command.sides == 100
        assert command.allDice.size() == 2
    }

    @Test
    void rollPercentileDiceWithpD() {
        def command = 2.pD
        assert command.sides == 100
        assert command.allDice.size() == 2
    }

    @Test
    void rollPercentileDiceWithPD() {
        def command = 2.PD
        assert command.sides == 100
        assert command.allDice.size() == 2
    }

    @Test
    void rollPercentileDiceWithdPercent() {
        def command = 3.'d%'
        assert command.sides == 100
        assert command.allDice.size() == 3
    }

    @Test
    void rollPercentileDiceWithDPercent() {
        def command = 3.'D%'
        assert command.sides == 100
        assert command.allDice.size() == 3
    }
}