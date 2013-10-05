/*
 * Copyright 2013 Atteo.
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
package org.atteo.moonshine.services.internal;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

import com.google.common.collect.Sets;


public class ReflectionTools {
	/**
	 * Checks whether class is marked as singleton.
	 */
	public static boolean isSingleton(Class<?> klass) {
		return klass.isAnnotationPresent(Singleton.class)
				|| klass.isAnnotationPresent(com.google.inject.Singleton.class);
	}

	/**
	 * Checks if given method is overridden.
	 * @param <T> klass type
	 * @param klass class to check method on, should be child of parentClass
	 * @param parentClass class which marks stop of the search
	 * @param methodName name of the method to search
	 * @return true, if method is overridden in klass, or any of its super classes up to (exclusive) parent class
	 */
	public static <T> boolean isMethodOverriden(Class<? extends T> klass, Class<T> parentClass, String methodName) {
		Class<?> superClass = klass;
		while (superClass != parentClass) {
			try {
				superClass.getDeclaredMethod(methodName);
			} catch (NoSuchMethodException e) {
				superClass = superClass.getSuperclass();
				continue;
			}
			return true;
		}
		return false;
	}

	/**
	 * Return all ancestors of a given class.
	 *
	 * <p>
	 * Implemented intefaces are not returned.
	 * </p>
	 *
	 * <p>
	 * The classes are returned in order from the class which has {@link Object} as a parent.
	 * Object itself is not included.
	 * </p>
	 */
	@SuppressWarnings("unchecked")
	public static <T> Iterable<Class<? super T>> getAncestors(Class<?> klass) {
		Deque<Class<? super T>> ancestors = new LinkedList<>();
		for (Class<?> ancestor = klass; ancestor != Object.class; ancestor = ancestor.getSuperclass()) {
			ancestors.addFirst((Class<? super T>) ancestor);
		}
		return ancestors;
	}

	/**
	 * Return all ancestors and all implemented interfaces of a given class.
	 *
	 * <p>
	 * The classes are returned in order from the class which has {@link Object} as a parent.
	 * Object itself is not included.
	 * </p>
	 * <p>
	 * Interface classes are returned before the first class which implement them.
	 * </p>
	 */
	public static Iterable<Class<?>> getAncestorsWithInterfaces(Class<?> klass) {
		List<Class<?>> result = new ArrayList<>();
		Deque<Class<?>> ancestors = new LinkedList<>();
		for (Class<?> ancestor = klass; ancestor != Object.class; ancestor = ancestor.getSuperclass()) {
			ancestors.addFirst(ancestor);
		}
		Set<Class<?>> implementedInterfaces = Sets.newIdentityHashSet();
		for (Class<?> ancestor : ancestors) {
			for (Class<?> interfaceClass : ancestor.getInterfaces()) {
				if (implementedInterfaces.add(interfaceClass)) {
					result.add(interfaceClass);
				}
			}
			result.add(ancestor);
		}
		return result;
	}
}