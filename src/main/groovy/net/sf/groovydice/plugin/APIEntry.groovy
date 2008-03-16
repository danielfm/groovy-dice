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

import org.codehaus.groovy.runtime.*

/**
 * Groovy Dice allows the users to extend and modify the default API by
 * providing plugins. When a plugin adds a method to the API, this new method
 * is represented by a APIEntry object.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F.
 * Martins</a>
 * @since 1.3
 * @version 1
 */
class APIEntry {

    /** Name of the method/property to be added to the dynamic API */
    def name = ''

    /** Class that will answer the dynamic method call. */
    Class clazz = NullObject

    /**
     * Closure that contains the logic to execute when this dynamic method
     * gets invoked.
     */
    Closure logic

    /**
     * String representation of this entry.
     * @return Method name, class and whether this entry is dynamic.
     */
    String toString() {
        "APIEntry[name:$name, clazz:$clazz, dynamic:$dynamic]"
    }
}
