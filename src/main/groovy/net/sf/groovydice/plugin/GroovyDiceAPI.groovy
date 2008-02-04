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

import net.sf.groovydice.*

/**
 * This class represents the Groovy Dice's dynamic methods dictionary. It also
 * provides a simple DSL to enable the users to extend the default API by
 * implementing plugins.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class GroovyDiceAPI {

    /** API entries. */
    def methods = []

    /**
     * This method is executed when some missing property or method was
     * called.
     * @param target Called object.
     * @param method Method name.
     * @param params Optional parameters array.
     * @return
     * @throws MissingPropertyException if the given property couldn't be found.
     * @throws MissingMethodException if the given method couldn't be found
     */
    def invoke(target, method, params=null) {
        def result = null
        def entries = methods.findAll {
            target.getClass() == it.clazz && method ==~ it.name
        }

        for (entry in entries) {
            def args = []
            if (entry.dynamic) {
                args << method
            }
            args << target
            if (params) {
                args.addAll(params.toList())
            }

            args.addAll([null] * (entry.logic.maximumNumberOfParameters - args.size()))
            result = entry.logic.call(args.size() > 1 ? args as Object[] : args[0])

            if (result != null) {
                break
            }
        }

        if (result == null) {
            if (!params) {
                throw new MissingPropertyException(method, target.getClass())
            }
            throw new MissingMethodException(method, target.getClass(), params)
        }
        result
    }

    /**
     * Add an entry to the API.
     * @param entryMap Map that contains information about the entry.
     * @param logic Closure to call when the method gets invoked.
    */
    void add(entryMap, logic) {

        /* default 'to:' attribute defaults to the dice rolling command */
        if (!entryMap.to) {
            entryMap.to = DiceRollingCommand
        }

        entryMap.to.each { clazz ->
            if (entryMap.method) {
                methods << new APIEntry(name:entryMap.method, clazz:clazz,
                    logic:logic)
            }
            else if (entryMap.dynamicMethod) {
                methods << new APIEntry(name:entryMap.dynamicMethod,
                        clazz:clazz, dynamic:true, logic:logic)
            }
        }
    }

    /**
     * Util method to get the list of number classes.
     * @return The list of number classes.
     */
    def getNumberClasses() {
        [Byte, Short, Integer, Long, Float, Double, BigInteger, BigDecimal]
    }

    /**
     * Check whether the given object is a dice rolling command.
     * @param obj Object to check.
     * @return Whether the given object is a dice rolling command.
     */
    def isCommand(obj) {
        obj instanceof DiceRollingCommand
    }

    /**
     * Whether the given object is a number.
     * @param obj Object to check
     * @return Whether the given object is a number.
     */
    def isNumber(obj) {
        obj instanceof Number
    }

    /**
     * Inject code to some Groovy classes to enable the method/property
     * interception.
     */
    void injectAspects() {
        numberClasses.each {
            injectAspect(it)
        }
        injectAspect(DiceRollingCommand)
    }

    /**
     * Inject code to the given Groovy class to enable the method/property
     * interception.
     * @param clazz Class to modify.
     */
    void injectAspect(clazz) {
        clazz.metaClass.propertyMissing = { String name ->
            invoke(delegate, name)
        }

        clazz.metaClass.methodMissing = { String name, args ->
            invoke(delegate, name, args)
        }
    }
}