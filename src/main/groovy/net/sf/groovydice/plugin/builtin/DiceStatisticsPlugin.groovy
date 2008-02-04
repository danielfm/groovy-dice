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
 * This plugin provides statistical methods to analise the dice from a roll.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 * @since 1.3
 * @version 1
 */
class DiceStatisticsPlugin {

    /**
     * This closure adds new methods to the API. Examples: <p/>
     * <pre>
     * 10.d.sum       // [1,6,5,4,3,1,2,6,4,2] -> 34
     * 10.d.count     // -> 10
     * 10.d.best_die  // [1,6,5,4,3,1,2,6,4,2] -> 6
     * 10.d.worst_die // [1,6,5,4,3,1,2,6,4,2] -> 1
     * 10.d.mean      // [1,6,5,4,3,1,2,6,4,2] -> 3.4
     * 10.d.median    // [1,6,5,4,3,1,2,6,4,2] -> 3.5
     * 10.d.mode      // [1,6,5,4,3,1,2,6,4,2] -> [1,2,4,6]
     * </pre>
     * @param api GroovyDiceAPI object.
     * @see net.sf.groovydice.plugin.GroovyDiceAPI
     */
    def dynamicMethods = { api ->

        /* 10.d.sum */
        api.add(method:'sum') { dice ->
            if (dice.allDice) {
                return dice.allDice.sum()
            }
            0
        }

        /* 10.d.count */
        api.add(method:'count') { dice ->
            dice.allDice.size()
        }

        /* 10.d.best_die */
        api.add(method:'best_die') { dice ->
            if (dice.allDice) {
                return dice.allDice.max()
            }
            0
        }

        /* 10.d.worst_die */
        api.add(method:'worst_die') { dice ->
            if (dice.allDice) {
                return dice.allDice.min()
            }
            0
        }

        /* 10.d.mean */
        api.add(method:'mean') { dice ->
            if (dice.allDice) {
                return dice.sum / dice.count
            }
            0
        }

        /* 10.d.median */
        api.add(method:'median') { dice ->
            if (dice.allDice) {
                def sorted = dice.allDice.sort()
                def n = sorted.size()
                def h = n / 2 as int

                return !(n % 2) ? (sorted[h-1..h].sum() / 2) : sorted[h]
            }
            0
        }

        /* 10.d.mode */
        api.add(method:'mode') { dice ->
            if (dice.count == 0) {
                return []
            }
            else if (dice.count <= 1) {
                return [dice.allDice[0]]
            }
            else {
                def map = [:]
                dice.allDice.each {
                    map[it] = map[it] ? map[it] + 1 : 1
                }

                def keys = map.keySet().sort{-map[it]}
                if (map.values().sum() == map.keySet().size()) {
                    return []
                }

                def last = 0
                def keyList = keys.toList()

                for (int i = 0; i < keyList.size() - 1; i++) {
                    if (map[keyList[i]] > map[keyList[i+1]]) {
                        break
                    }
                    else {
                        last++
                    }
                }

                return keys[0..last]
            }
        }
    }
}