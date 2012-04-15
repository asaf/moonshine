/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.atteo.evo.jersey;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.google.inject.Singleton;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
@Singleton
public final class JAXBContextResolver implements ContextResolver<JAXBContext> {

	public JAXBContextResolver() {
	}

	@Override
	public JAXBContext getContext(Class<?> objectType) {
		try {
			return new JSONJAXBContext(JSONConfiguration.natural().usePrefixesAtNaturalAttributes()
					.rootUnwrapping(false).build(), objectType);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
}