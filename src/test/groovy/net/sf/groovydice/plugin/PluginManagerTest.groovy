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

import org.junit.*

/**
 * PluginManager test cases.
 *
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
class PluginManagerTest {

    @Test
    void register() {
        def manager = new PluginManager()
        manager.register('plugin')
        assert manager.pluginStack[0] == 'plugin'

        manager.register('other')
        assert manager.pluginStack[0] == 'other'
        assert manager.pluginStack[1] == 'plugin'
    }

    @Test
    void unregisterAll() {
        def manager = new PluginManager()
        manager.unregister()
        assert manager.pluginStack == []
    }

    @Test
    void unregisterByClass() {
        def manager = new PluginManager()
        manager.register('plugin')
        manager.register(123)
        manager.register('other')
        assert manager.pluginStack == ['other',123,'plugin']

        manager.unregister(String)
        assert manager.pluginStack == [123]
    }

    @Test
    void unregisterInstance() {
        def manager = new PluginManager()
        manager.register('plugin')
        manager.register('other')
        assert manager.pluginStack == ['other','plugin']

        manager.unregister('other')
        assert manager.pluginStack == ['plugin']
    }
}