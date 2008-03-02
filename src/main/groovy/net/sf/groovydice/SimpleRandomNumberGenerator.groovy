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
 * Simple random number generation implementation that rely on the Java's
 * <code>Random</code> class do to its job.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F.
 * Martins</a>
 * @since 1.3
 * @version 1
 */
class SimpleRandomNumberGenerator {

    /** Simple random number generator. */
    Random generator = new Random()

    /**
     * This closure is called in order to generate a random number between 1
     * and the given number.
     * @see net.sf.groovydice.DiceRollingCommand#roll(int)
     */
    def next = { number ->
        generator.nextInt(number) + 1
    }
}
