package ru.prolib.nattest.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GraphNodeImplTest {
	@Rule public ExpectedException eex = ExpectedException.none();
	Map<Integer, GraphEdge<Integer>> edges1, edges2, edges3;
	GraphNodeImpl<Integer> service;

	@Before
	public void setUp() throws Exception {
		edges1 = new HashMap<>();
		edges2 = new HashMap<>();
		edges3 = new HashMap<>();
		service = new GraphNodeImpl<>(726, edges1);
	}
	
	@Test
	public void testCtor1_ThrowsIfNullVertex() {
		eex.expect(NullPointerException.class);
		eex.expectMessage("Vertex cannot be null");
		
		new GraphNodeImpl<>(null);
	}
	
	@Test
	public void testCtor2_ThrowsIfNullVertex() {
		eex.expect(NullPointerException.class);
		eex.expectMessage("Vertex cannot be null");
		
		new GraphNodeImpl<>(null, edges1);
	}
	
	@Test
	public void testGetters() {
		assertEquals(Integer.valueOf(726), service.getVertex());
	}
	
	@Test
	public void testAddEdge() {
		assertSame(service, service.addEdge(345, 24.19d));
		assertSame(service, service.addEdge(778,  5.45d));
		assertSame(service, service.addEdge(115,  7.62d));
		
		assertEquals(new GraphEdgeImpl<>(726, 345, 24.19d), edges1.get(345));
		assertEquals(new GraphEdgeImpl<>(726, 778,  5.45d), edges1.get(778));
		assertEquals(new GraphEdgeImpl<>(726, 115,  7.62d), edges1.get(115));
	}
	
	@Test
	public void testAddEdge_ThrowsIfAlreadyDefined() {
		edges1.put(882, new GraphEdgeImpl<>(726, 882, 0.5d));
		eex.expect(IllegalArgumentException.class);
		eex.expectMessage("Edge already defined: 726 -> 882");
		
		service.addEdge(882, 0.27d);
	}
	
	@Test
	public void testIsHeighbor() {
		edges1.put(882, new GraphEdgeImpl<>(726, 882, 0.5d));
		edges1.put(345, new GraphEdgeImpl<>(726, 345, 0.3d));

		assertTrue(service.isNeighbor(882));
		assertTrue(service.isNeighbor(345));
		assertFalse(service.isNeighbor(866));
		assertFalse(service.isNeighbor(701));
	}
	
	@Test
	public void testGetNeighbors() {
		edges1.put(345, new GraphEdgeImpl<>(726, 345, 24.19d));
		edges1.put(778, new GraphEdgeImpl<>(726, 778,  5.45d));
		edges1.put(115, new GraphEdgeImpl<>(726, 115,  7.62d));
		
		Set<Integer> actual = service.getNeighbors().stream().collect(Collectors.toSet());
		
		Set<Integer> expected = Arrays.asList(345, 778, 115).stream().collect(Collectors.toSet());
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetEdges() {
		edges1.put(345, new GraphEdgeImpl<>(726, 345, 24.19d));
		edges1.put(778, new GraphEdgeImpl<>(726, 778,  5.45d));
		edges1.put(115, new GraphEdgeImpl<>(726, 115,  7.62d));

		Set<GraphEdge<Integer>> actual = service.getEdges().stream().collect(Collectors.toSet());
		
		Set<GraphEdge<Integer>> expected = Arrays.asList(
				new GraphEdgeImpl<>(726, 345, 24.19d),
				new GraphEdgeImpl<>(726, 778,  5.45d),
				new GraphEdgeImpl<>(726, 115,  7.62d)
			).stream().collect(Collectors.toSet());
		assertEquals(expected, actual);
	}
	
	@Test
	public void testToString() {
		edges1.put(345, new GraphEdgeImpl<>(726, 345, 24.19d));
		edges1.put(778, new GraphEdgeImpl<>(726, 778,  5.45d));
		edges1.put(115, new GraphEdgeImpl<>(726, 115,  7.62d));
		String expected = new StringBuilder()
			.append("GraphNodeImpl[vertex=726,edges=")
			.append(edges1.values().stream().map(x -> x.getTarget() + "(" + x.getWeight() + ")")
					.collect(Collectors.toList()))
			.append("]")
			.toString();
		
		assertEquals(expected, service.toString());
	}
	
	@Test
	public void testEquals_SpecialCases() {
		assertTrue(service.equals(service));
		assertFalse(service.equals(this));
		assertFalse(service.equals(null));
	}

	@Test
	public void testEquals() {
		service.addEdge(345, 24.19d).addEdge(778, 5.45d).addEdge(115, 7.62d);
		
		edges2.put(345, new GraphEdgeImpl<>(726, 345, 24.19d));
		edges2.put(778, new GraphEdgeImpl<>(726, 778,  5.45d));
		edges2.put(115, new GraphEdgeImpl<>(726, 115,  7.62d));
		edges3.put(889, new GraphEdgeImpl<>(333, 889,  1.00d));
		edges3.put(204, new GraphEdgeImpl<>(333, 204,  1.00d));
		edges3.put(144, new GraphEdgeImpl<>(333, 144,  1.00d));
		
		assertTrue(service.equals(new GraphNodeImpl<>(726, edges2)));
		assertFalse(service.equals(new GraphNodeImpl<>(333, edges2)));
		assertFalse(service.equals(new GraphNodeImpl<>(726, edges3)));
		assertFalse(service.equals(new GraphNodeImpl<>(333, edges3)));
	}

}
