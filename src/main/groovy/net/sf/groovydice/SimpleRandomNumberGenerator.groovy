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

/**
 * Simple implementation of the <code>RandomNumberGenerator</code> interface.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class SimpleRandomNumberGenerator implements RandomNumberGenerator {

    /** Simple random number generator. */
    def generator = new Random()

    def generateNumber(number) {
        if (!(number == '%' || number > 0)) {
            throw new IllegalArgumentException("Invalid number: $number")
        }

        if (number == '%') {
            number = 100
            def result = ''

            2.times {
                result += generator.nextInt(10)
            }
            return (result == '00') ? 100 : result.toInteger()
        }
        else {
            return generator.nextInt(number) + 1
        }
    }
}