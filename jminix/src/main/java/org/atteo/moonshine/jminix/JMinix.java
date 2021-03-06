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
package org.atteo.moonshine.jminix;

import javax.inject.Singleton;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import org.atteo.moonshine.TopLevelService;
import org.atteo.moonshine.services.ImportService;
import org.atteo.moonshine.webserver.ServletContainer;
import org.jminix.console.servlet.MiniConsoleServlet;

import com.google.inject.Module;
import com.google.inject.PrivateModule;

/**
 * JMX web console.
 * <p>
 * The console is based on <a href="http://code.google.com/p/jminix/">JMiniX</a>.
 * </p>
 * <p>
 * Due to the limitation of JMiniX you need to append trailing '/' in the URL
 * to access it.
 * </p>
 */
@XmlRootElement(name = "jminix")
public class JMinix extends TopLevelService {
	@XmlElement
	@XmlIDREF
	@ImportService
	private ServletContainer servletContainer;

	@XmlElement
	private String pattern = "/jmx/*";

	@Override
	public Module configure() {
		return new PrivateModule() {
			@Override
			protected void configure() {
				bind(MiniConsoleServlet.class).in(Singleton.class);
				servletContainer.addServlet(getProvider(MiniConsoleServlet.class), pattern);
			}
		};
	}
}
