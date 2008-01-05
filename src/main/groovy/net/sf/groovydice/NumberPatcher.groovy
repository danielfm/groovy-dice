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
 * This class only contains one method, which is used to "patch" the number
 * classes, adding properties and methods to them.
 *
 * @author Daniel F. Martins
 */
class NumberPatcher {

    /**
     * Call this method to modify Number instances at runtime, enabling its instances
     * to respond to lots of new methods and properties.
     */
    static void addMethods() {

        [Integer, Long, BigInteger, Float, Double, BigDecimal].each {

            /* 1.on_each_die */
            it.metaClass.getOn_each_die = { ->
                new DieModifier(modifier:delegate)
            }

            /* 1.is_even */
            it.metaClass.getIs_even = { ->
                (delegate as int) % 2 == 0
            }

            /* 1.is_odd */
            it.metaClass.getIs_odd = { ->
                (delegate as int) % 2 != 0
            }

            /* 1.d(10) */
            it.metaClass.d = { sides ->
                try {
                    /* workaround to support things like 1.d('6.5') */
                    sides = sides.toFloat() as int
                }
                catch (NumberFormatException) {}
                new DiceRollingSpec(sides:sides).roll(delegate)
            }

            /* 1.D(10) */
            it.metaClass.D = { sides ->
                delegate.d(sides)
            }

            it.metaClass.plus = { n ->
                if (n instanceof DiceRollingSpec) {
                    return n + delegate
                }
            }

            it.metaClass.minus = { n ->
                if (n instanceof DiceRollingSpec) {
                    return -n + delegate
                }
            }

            it.metaClass.multiply = { n ->
                if (n instanceof DiceRollingSpec) {
                    return n * delegate
                }
            }

            it.metaClass.div = { n ->
                if (n instanceof DiceRollingSpec) {
                    return delegate / n.sum
                }
            }

            it.metaClass.power = { n ->
                if (n instanceof DiceRollingSpec) {
                    return delegate ** n.sum
                }
            }

            it.metaClass.mod = { n ->
                if (n instanceof DiceRollingSpec) {
                    return delegate % n.sum
                }
            }

            it.metaClass.propertyMissing = { name ->

                if (name =~ /^(d|D)$/) {
                    name = 'd6' // 6-sided dice is the default dice type
                }

                /* intercept calls to 'dX' properties */
                if (name =~ /^(d|D)\d+$/) {
                    def sides = name.substring(1).toInteger()
                    return new DiceRollingSpec(sides:sides).roll(delegate)
                }
                if (name =~ /^((p|P)(d|D)|(d|D)%)$/) {
                    return new DiceRollingSpec(sides:'%').roll(delegate)
                }

                throw new MissingPropertyException(
                        "No such property: $name for class: ${delegate.class}")
            }
        }
    }
}
