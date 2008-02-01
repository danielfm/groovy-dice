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
package net.sf.groovydice.integration;

import static org.junit.Assert.assertTrue;
import groovy.lang.GroovyShell;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Before;
import org.junit.Test;

import net.sf.groovydice.GroovyDice;

/**
 * Test the Groovy Dice integration with Java applications.
 * 
 * @author <a href="mailto:daniel_martins@users.sourceforge.net">Daniel F. Martins</a>
 */
public class GroovyDiceJavaIntegrationTest {

    @Before
    public void initialize() {
        new GroovyDice().initialize();
    }

    @Test
    public void javaScriptingAPIIntegration() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager()
                .getEngineByName("groovy");

        assertTrue(engine.eval("2.d5 * -1") instanceof Number);
    }

    @Test
    public void groovyShellIntegration() {
        assertTrue(new GroovyShell().evaluate("2.d5 * -1") instanceof Number);
    }
}
