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

/**
 * This plugin adds methods to make easier to the user specify constraints
 * based on odd/even numbers.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class OddEvenPlugin {

    /**
     * This closure adds new methods to the API. Examples: <p/>
     * <pre>
     * 5.is_even // -> false
     * 5.is_odd // -> true
     * </pre>
     * @param api GroovyDiceAPI object.
     * @see net.sf.groovydice.plugin.GroovyDiceAPI
     */
    def dynamicMethods = { api ->

        /* 5.is_even */
        api.add(method:'is_even', to:api.numberClasses) { number ->
            (number as int) % 2 == 0
        }

        /* 5.is_odd */
        api.add(method:'is_odd', to:api.numberClasses) { number ->
            (number as int) % 2 != 0
        }
    }

    /**
     * Returns a String representation of this object.
     * @return Plugin name.
     */
    String toString() {
        getClass().name
    }
}