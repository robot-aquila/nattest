package ru.prolib.nattest.impl;

import static org.junit.Assert.*;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GraphEdgeImplTest {
	@Rule public ExpectedException eex = ExpectedException.none();
	GraphEdgeImpl<Integer> service;
	
	@Before
	public void setUp() throws Exception {
		service = new GraphEdgeImpl<Integer>(504, 256, 0.75d);
	}
	
	@Test
	public void testCtor_ThrowsIfNullSource() {
		eex.expect(NullPointerException.class);
		eex.expectMessage("Source vertex cannot be null");
		
		new GraphEdgeImpl<>(null, 256, 0.75d);
	}
	
	@Test
	public void testCtor_ThrowsIfNullTarget() {
		eex.expect(NullPointerException.class);
		eex.expectMessage("Target vertex cannot be null");
		
		new GraphEdgeImpl<>(504, null, 0.75d);
	}
	
	@Test
	public void testGetters() {
		assertEquals(0.75d, service.getWeight(), 0.001d);
		assertEquals(Integer.valueOf(504), service.getSource());
		assertEquals(Integer.valueOf(256), service.getTarget());
	}
	
	@Test
	public void testToString() {
		String expected = "GraphEdgeImpl[source=504,target=256,weight=0.75]";
		
		assertEquals(expected, service.toString());
	}
	
	@Test
	public void testHashCode() {
		int expected = new HashCodeBuilder(11589001, 59)
				.append(Integer.valueOf(504))
				.append(Integer.valueOf(256))
				.append(0.75d)
				.build();
		
		assertEquals(expected, service.hashCode());
	}
	
	@Test
	public void testEquals_SpecialCases() {
		assertTrue(service.equals(service));
		assertFalse(service.equals(null));
		assertFalse(service.equals(this));
	}

	@Test
	public void testEquals() {
		assertTrue(service.equals(new GraphEdgeImpl<>(504, 256, 0.75d)));
		assertFalse(service.equals(new GraphEdgeImpl<>(111, 256, 0.75d)));
		assertFalse(service.equals(new GraphEdgeImpl<>(504, 111, 0.75d)));
		assertFalse(service.equals(new GraphEdgeImpl<>(504, 256, 1.11d)));
		assertFalse(service.equals(new GraphEdgeImpl<>(111, 111, 1.11d)));
	}

}
