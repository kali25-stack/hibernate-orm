/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.sql.results.graph.instantiation.internal;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.sql.results.graph.DomainResultAssembler;
import org.hibernate.sql.results.jdbc.spi.JdbcValuesSourceProcessingOptions;
import org.hibernate.sql.results.jdbc.spi.RowProcessingState;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;

import org.jboss.logging.Logger;

/**
 * A QueryResultAssembler implementation representing handling for dynamic-
 * instantiations targeting a List (per-"row"),
 *
 * E.g.`select new list( pers.name, pers.dateOfBirth ) ...`
 *
 * @author Steve Ebersole
 */
public class DynamicInstantiationAssemblerListImpl implements DomainResultAssembler<List> {
	private static final Logger log = Logger.getLogger( DynamicInstantiationAssemblerListImpl.class );

	private final JavaTypeDescriptor<List> listJavaDescriptor;
	private final List<ArgumentReader<?>> argumentReaders;

	public DynamicInstantiationAssemblerListImpl(
			JavaTypeDescriptor<List> listJavaDescriptor,
			List<ArgumentReader<?>> argumentReaders) {
		this.listJavaDescriptor = listJavaDescriptor;
		this.argumentReaders = argumentReaders;
	}

	@Override
	public JavaTypeDescriptor<List> getAssembledJavaTypeDescriptor() {
		return listJavaDescriptor;
	}

	@Override
	public List assemble(
			RowProcessingState rowProcessingState,
			JdbcValuesSourceProcessingOptions options) {
		final ArrayList<Object> result = new ArrayList<>();
		for ( ArgumentReader argumentReader : argumentReaders ) {
			result.add( argumentReader.assemble( rowProcessingState, options ) );
		}
		return result;
	}
}