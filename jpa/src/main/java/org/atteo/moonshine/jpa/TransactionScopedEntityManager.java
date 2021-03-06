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
package org.atteo.moonshine.jpa;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TransactionRequiredException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

public class TransactionScopedEntityManager extends DelegatingEntityManager {

	@Inject
	private EntityManagerFactory factory;

	@Inject
	private TransactionManager transactionManager;

	private final ThreadLocal<EntityManager> entityManagerHolder = new ThreadLocal<>();

	@Override
	protected EntityManager getEntityManager() {
		EntityManager entityManager = entityManagerHolder.get();

		if (entityManager == null) {
			try {
				Transaction transaction = transactionManager.getTransaction();
				if (transaction == null) {
					throw new TransactionRequiredException("Not in transaction. Initiate transaction in JTA.");
				}
				entityManager = factory.createEntityManager();

				final EntityManager entityManagerToClose = entityManager;
				entityManagerHolder.set(entityManager);
				transaction.registerSynchronization(new Synchronization() {
					@Override
					public void beforeCompletion() {
					}

					@Override
					public void afterCompletion(int status) {
						if (entityManagerHolder.get() != entityManagerToClose) {
							throw new RuntimeException("Synchronization called from different thread");
						}
						if (entityManagerToClose != null) {
							entityManagerToClose.close();
						}
						entityManagerHolder.set(null);
					}
				});
			} catch (SystemException | RollbackException e) {
				throw new RuntimeException(e);
			}
		}

		return entityManager;
	}

	@Override
	public boolean isOpen() {
		try {
			return super.isOpen();
		} catch (TransactionRequiredException e) {
			return false;
		}
	}

	@Override
	public void close() {
		throw new IllegalStateException("Cannot close container managed entity manager");
	}

	@Override
	public EntityTransaction getTransaction() {
		throw new IllegalStateException("Not allowed to create transaction on shared EntityManager");
	}

	@Override
	public void joinTransaction() {
		throw new IllegalStateException("Not allowed to create transaction on shared EntityManager");
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return factory;
	}

	@Override
	public Metamodel getMetamodel() {
		return factory.getMetamodel();
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return factory.getCriteriaBuilder();
	}

	@Override
	public String toString() {
		return "Shared EntityManager proxy for target factory [" + factory + "]";
	}
}
