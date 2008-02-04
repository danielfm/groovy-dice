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
 * This class represents an API entry added dinamically by a plugin.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class APIEntry {

    /** Name of the method/property to be added to the API */
    def name = ''

    /** Class that will answer the method call. */
    Class clazz = NullObject

    /**
     * Whether this method is dynamic. If you use a regex pattern as the
     * method name, probably you would like to set this attribute to
     * <code>true</code>. Doing this, when the given API entry is invoked,
     * the requested method name will be passed to the matching closure.
     * @see net.sf.groovydice.plugin.builtin.DiceExpressionPlugin
     */
    Boolean dynamic = false

    /** Closure that contains the logic to execute. */
    Closure logic

    /**
     * String representation of this entry.
     */
    String toString() {
        "APIEntry[name:$name, clazz:$clazz, dynamic:$dynamic]"
    }
}