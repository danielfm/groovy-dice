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
 * SimpleRandomNumberGenerator test cases.
 *
 * @author Daniel F. Martins
 */
class SimpleRandomNumberGeneratorTest {

    private def generator

    @Before
    void setUp() {
        generator = new SimpleRandomNumberGenerator()
    }

    @Test
    void checkRandomNumberInterval() {
        def n = 1000
        n.times {
            generator.generateNumber(100) in 1..n
        }
    }
}