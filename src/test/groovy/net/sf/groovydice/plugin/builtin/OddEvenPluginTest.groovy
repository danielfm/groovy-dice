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
 * OddEvenPlugin test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class OddEvenPluginTest {

    @BeforeClass
    static void initialize()  {
        new GroovyDice().initialize()
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
}