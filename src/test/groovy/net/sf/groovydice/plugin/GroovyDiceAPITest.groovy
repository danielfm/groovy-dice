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
package net.sf.groovydice.plugin

import org.junit.*

import net.sf.groovydice.*

/**
 * GroovyDiceAPI test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class GroovyDiceAPITest {

    @BeforeClass
    static void initialize() {
        new GroovyDice().initialize()
    }

    @Test(expected=MissingPropertyException)
    void invokeMissingProperty() {
        42.is_the_answer_to_life_the_universe_and_everything
    }

    @Test(expected=MissingPropertyException)
    void invokeAnotherMissingProperty() {
        /* no-arg methods is recognized as properties */
        42.is_the_answer_to_life_the_universe_and_everything()
    }

    @Test(expected=MissingMethodException)
    void invokeMissingMethod() {
        42.is_equals_to(42)
    }
}