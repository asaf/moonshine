/*
 * Copyright 2012 Atteo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.atteo.evo.jminix;

import javax.inject.Singleton;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.atteo.evo.services.TopLevelService;
import org.jminix.console.servlet.MiniConsoleServlet;

import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;

/**
 * Simple JMX console based on <a href="http://code.google.com/p/jminix/">JMiniX</a>.
 * <p>
 * Due to the limitation of JMiniX you need to append trailing '/' in the URL
 * to access it.
 * </p>
 */
@XmlRootElement(name = "jminix")
public class JMinix extends TopLevelService {
	@XmlElement
	private String prefix = "/jmx";

	@Override
	public Module configure() {
		return new ServletModule() {
			@Override
			protected void configureServlets() {
				bind(MiniConsoleServlet.class).in(Singleton.class);
				serve(prefix + "/*").with(MiniConsoleServlet.class);
			}
		};
	}
}
