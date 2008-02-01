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
 * This interface represents the behavior of being capable to generate random
 * numbers.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
interface RandomNumberGenerator {

    /**
     * Generate a random number between 1 and the given number.
     * @param number Object that represents the higher number. Usually the number
     * is represented by a <code>Number</code> instance or by a "%" which represents
     * a percentile dice (two 10-sided dice that represents a number between 1 and
     * 100.
     * @return A random-generated number.
     */
    def generateNumber(number)
}