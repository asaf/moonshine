/*
 * Copyright 2012 Atteo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.atteo.evo.jetty;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.jetty.server.Connector;

public abstract class AbstractConnectorConfig extends ConnectorConfig {
	/**
	 * The configured port for the connector or 0 if any available port may be used.
	 */
	@XmlElement
	private Integer port;

	/**
	 * The hostname representing the interface to which this connector will bind, or null for all interfaces.
	 */
	@XmlElement
	private String host;

	abstract protected Connector createConnector();

	@Override
	public Connector getConnector() {
		Connector connector = createConnector();
		if (port != null) {
			connector.setPort(port);
		}
		if (host != null) {
			connector.setHost(host);
		}
		return connector;
	}
}
