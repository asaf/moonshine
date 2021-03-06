/*
 * Copyright 2011 Atteo.
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
package org.atteo.moonshine.liquibase;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import org.atteo.moonshine.TopLevelService;
import org.atteo.moonshine.database.DatabaseMigration;
import org.atteo.moonshine.database.DatabaseService;
import org.atteo.moonshine.services.ImportService;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Scopes;

/**
 * Liquibase service.
 * <p>
 * Binds {@link LiquibaseFacade} with the specified database.
 * </p>
 * <p>
 * The preferred way it to use {@link LiquibaseFacade} manually and use it to register migration
 * using {@link DatabaseService#registerMigration(DatabaseMigration)}.
 * </p>
 */
@XmlRootElement(name = "liquibase")
public class LiquibaseService extends TopLevelService {
	@XmlElement
	@XmlIDREF
	@ImportService
	private DatabaseService database;

	@Override
	public Module configure() {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(LiquibaseFacade.class).in(Scopes.SINGLETON);
			}
		};
	}
}
