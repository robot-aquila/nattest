package ru.prolib.nattest.impl;

import static org.junit.Assert.*;
import static ru.prolib.nattest.impl.GraphType.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GraphImplTest {
	
	static GraphImpl<Integer> loadTestVertices(GraphImpl<Integer> graph) {
		for ( int i = 1; i <= 12; i ++ ) {
			graph.addVertex(i);
		}
		return graph;
	}
	
	static GraphImpl<Integer> loadTestGraph(GraphImpl<Integer> graph) {
		return loadTestVertices(graph)
			.addEdge( 1,  3)
			.addEdge( 1, 10)
			.addEdge( 2,  4)
			.addEdge( 2,  5)
			.addEdge( 2,  6)
			.addEdge( 2, 11)
			.addEdge( 3,  8)
			.addEdge( 3, 12)
			.addEdge( 4,  5)
			.addEdge( 4,  6)
			.addEdge( 5,  6)
			.addEdge( 5, 12)
			.addEdge( 6,  7)
			.addEdge( 6,  8)
			.addEdge( 7,  8)
			.addEdge( 7,  9)
			.addEdge( 7, 10)
			.addEdge( 7, 11)
			.addEdge( 8, 10)
			.addEdge( 8, 11)
			.addEdge( 9, 10);
	}
	
	static GraphImpl<Integer>  loadTestGraphWithWeights(GraphImpl<Integer> graph) {
		return loadTestVertices(graph)
			.addEdge( 1,  3, 0.9d)
			.addEdge( 1, 10, 0.7d)
			.addEdge( 2,  4, 0.9d)
			.addEdge( 2,  5, 0.8d)
			.addEdge( 2,  6, 0.5d)
			.addEdge( 2, 11, 0.5d)
			.addEdge( 3,  8, 0.6d)
			.addEdge( 3, 12, 0.7d)
			.addEdge( 4,  5, 0.7d)
			.addEdge( 4,  6, 0.3d)
			.addEdge( 5,  6, 0.4d)
			.addEdge( 5, 12, 0.7d)
			.addEdge( 6,  7, 0.3d)
			.addEdge( 6,  8, 0.5d)
			.addEdge( 7,  8, 0.8d)
			.addEdge( 7,  9, 0.5d)
			.addEdge( 7, 10, 0.3d)
			.addEdge( 7, 11, 0.2d)
			.addEdge( 8, 10, 0.8d)
			.addEdge( 8, 11, 0.1d)
			.addEdge( 9, 10, 0.6d);
	}
	
	@Rule public ExpectedException eex = ExpectedException.none();
	ConcurrentHashMap<Integer, GraphNodeImpl<Integer>> nodes, nodes_d;
	GraphImpl<Integer> service, service_d;

	@Before
	public void setUp() throws Exception {
		service = new GraphImpl<>(UNDIRECTED, 2.0d, nodes = new ConcurrentHashMap<>());
		service_d = new GraphImpl<>(DIRECTED, 3.0d, nodes_d = new ConcurrentHashMap<>());
	}
	
	@Test
	public void testCtor3() {
		assertTrue(service.isUndirected());
		assertEquals(2.0d, service.getDefaultWeight(), 0.01d);
		
		assertFalse(service_d.isUndirected());
		assertEquals(3.0d, service_d.getDefaultWeight(), 0.01d);
	}
	
	@Test
	public void testCtor2() {
		service = new GraphImpl<>(UNDIRECTED, 5.0d);
		
		assertTrue(service.isUndirected());
		assertEquals(5.0d, service.getDefaultWeight(), 0.01d);
	}
	
	@Test
	public void testCtor1() {
		service = new GraphImpl<>(UNDIRECTED);
		
		assertTrue(service.isUndirected());
		assertEquals(1.0d, service.getDefaultWeight(), 0.01d);
	}
	
	@Test
	public void testCtor0() {
		service = new GraphImpl<>();
		
		assertTrue(service.isUndirected());
		assertEquals(1.0d, service.getDefaultWeight(), 0.01d);
	}
	
	@Test
	public void testAddVertex() {
		assertSame(service, service.addVertex(856));
		assertSame(service, service.addVertex(127));
		assertSame(service, service.addVertex(296));
		
		assertEquals(3, nodes.size());
		assertEquals(new GraphNodeImpl<>(856), nodes.get(856));
		assertEquals(new GraphNodeImpl<>(127), nodes.get(127));
		assertEquals(new GraphNodeImpl<>(296), nodes.get(296));
	}
	
	@Test
	public void testAddVertex_ThrowsIfAlreadyExists() {
		nodes.put(127, new GraphNodeImpl<>(127));
		eex.expect(IllegalArgumentException.class);
		eex.expectMessage("Vertex already exists: 127");
		
		service.addVertex(127);
	}
	
	@Test
	public void testAddEdge3_Undirected() {
		assertTrue(service.isUndirected());
		service.addVertex(504).addVertex(256);
		
		assertSame(service, service.addEdge(504, 256, 104.0d));
		
		assertEquals(new GraphNodeImpl<>(504).addEdge(256, 104.0d), nodes.get(504));
		assertEquals(new GraphNodeImpl<>(256).addEdge(504, 104.0d), nodes.get(256));
	}
	
	@Test
	public void testAddEdge3_Directed() {
		assertFalse(service_d.isUndirected());
		service_d.addVertex(504).addVertex(256);
		
		assertSame(service_d, service_d.addEdge(504, 256, 104.0d));
		
		assertEquals(new GraphNodeImpl<>(504).addEdge(256, 104.0d), nodes_d.get(504));
		assertEquals(new GraphNodeImpl<>(256), nodes_d.get(256));
	}
	
	@Test
	public void testAddEdge3_ThrowsIfSourceNotExists() {
		service.addVertex(256);
		eex.expect(IllegalArgumentException.class);
		eex.expectMessage("Vertex not found: 504");
		
		service.addEdge(504, 256, 104.0d);
	}
	
	@Test
	public void testAddEdge3_ThrowsIfTargetNotExists() {
		service.addVertex(504);
		
		eex.expect(IllegalArgumentException.class);
		eex.expectMessage("Vertex not found: 256");
		
		service.addEdge(504, 256, 104.0d);
	}
	
	@Test
	public void testAddEdge3_ThrowsIfTargetSameAsSource() {
		service.addVertex(504).addVertex(256);
		eex.expect(IllegalArgumentException.class);
		eex.expectMessage("Cannot use same vertex as source and target: " + 504);
		
		service.addEdge(504, 504, 0.95d);
	}
	
	@Test
	public void testAddEdge2_Undirected() {
		service.addVertex(504).addVertex(256);
		
		assertSame(service, service.addEdge(504, 256));
		
		assertEquals(new GraphNodeImpl<>(504).addEdge(256, 2.0d), nodes.get(504));
		assertEquals(new GraphNodeImpl<>(256).addEdge(504, 2.0d), nodes.get(256));
	}
	
	@Test
	public void testAddEdge2_Directed() {
		service_d.addVertex(504).addVertex(256);
		
		assertSame(service_d, service_d.addEdge(504, 256));
		
		assertEquals(new GraphNodeImpl<>(504).addEdge(256, 3.0d), nodes_d.get(504));
		assertEquals(new GraphNodeImpl<>(256), nodes_d.get(256));
	}
	
	@Test
	public void testAddEdge2_ThrowsIfSourceNotExists() {
		service.addVertex(256);
		eex.expect(IllegalArgumentException.class);
		eex.expectMessage("Vertex not found: 504");
		
		service.addEdge(504, 256);
	}
	
	@Test
	public void testAddEdge2_ThrowsIfTargetNotExists() {
		service.addVertex(504);
		eex.expect(IllegalArgumentException.class);
		eex.expectMessage("Vertex not found: 256");
		
		service.addEdge(504, 256);
	}
	
	@Test
	public void testAddEdge2_ThrowsIfTargetSameAsSource() {
		service.addVertex(504).addVertex(256);
		eex.expect(IllegalArgumentException.class);
		eex.expectMessage("Cannot use same vertex as source and target: " + 504);
		
		service.addEdge(504, 504);
	}
	
	@Test
	public void testGetPath_UndirectedWoWeights() {
		assertTrue(service.isUndirected());
		loadTestGraph(service);
		
		Collection<Integer> actual = service.getPath(5, 9);
		
		Collection<Integer> expected = Arrays.asList(5, 6, 7, 9);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetPath_UndirectedWithWeights() {
		assertTrue(service.isUndirected());
		loadTestGraphWithWeights(service);
		
		Collection<Integer> actual = service.getPath(12, 10);
		
		Collection<Integer> expected = Arrays.asList(12, 5, 6, 7, 10);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetPath_DirectedWoWeights() {
		assertTrue(service_d.isDirected());
		loadTestGraph(service_d);
		
		Collection<Integer> actual = service_d.getPath(3, 10);
		
		Collection<Integer> expected = Arrays.asList(3, 8, 10);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetPath_DirectedWithWeights() {
		assertTrue(service_d.isDirected());
		loadTestGraphWithWeights(service_d);
		
		Collection<Integer> actual = service_d.getPath(4, 11);
		
		Collection<Integer> expected = Arrays.asList(4, 6, 7, 11);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetPath_ThrowsIfSourceNotFound() {
		loadTestGraph(service);
		eex.expect(IllegalArgumentException.class);
		eex.expectMessage("Vertex not found: 911");

		service.getPath(911, 1);
	}
	
	@Test
	public void testGetPath_ThrowsIfTargetNotFound() {
		loadTestGraph(service);
		eex.expect(IllegalArgumentException.class);
		eex.expectMessage("Vertex not found: 112");

		service.getPath(1, 112);
	}
	
	@Test
	public void testGetPath_UndirectedThrowsIfPathNotFound() {
		loadTestGraph(service).addVertex(13);
		eex.expect(IllegalStateException.class);
		eex.expectMessage("Path not found: 1 -> 13");
		assertTrue(service.isUndirected());
		
		service.getPath(1, 13);		
	}
	
	@Test
	public void testGetPath_DirectedThrowsIfPathNotFound() {
		loadTestGraph(service_d);
		eex.expect(IllegalStateException.class);
		eex.expectMessage("Path not found: 10 -> 4");
		assertTrue(service_d.isDirected());
		
		service_d.getPath(10, 4);
	}
	
	@Test
	public void testGetPath_JustCase1() {
		service.addVertex(1)
			.addVertex( 2).addVertex( 3).addVertex( 4).addVertex( 5)
			.addVertex( 6).addVertex( 7).addVertex( 8).addVertex( 9)
			.addVertex(10).addVertex(11).addVertex(12).addVertex(13)
			.addVertex(14)
			.addEdge( 1,  2, 0.5d).addEdge( 1,  3, 0.5d).addEdge( 1,  4, 0.5d).addEdge( 1,  5, 0.5d)
			.addEdge( 2,  6, 0.1d).addEdge( 3,  7, 0.6d).addEdge( 4,  8, 0.3d).addEdge( 5,  9, 1.2d)
			.addEdge( 6, 10, 0.9d).addEdge( 7, 11, 0.5d).addEdge( 8, 12, 0.7d).addEdge( 9, 13, 0.4d)
			.addEdge(10, 14, 0.2d).addEdge(11, 14, 0.8d).addEdge(12, 14, 0.1d).addEdge(13, 14, 0.6d);
		
		Collection<Integer> actual = service.getPath(1, 14);
		
		Collection<Integer> expected = Arrays.asList(1, 4, 8, 12, 14).stream().collect(Collectors.toList());
		assertEquals(expected, actual);
	}
	
	@Test
	public void testTraverseVertices_UsingStreams() {
		// Let's sum up all weights of all edges of directed graph
		
		double actual = loadTestGraphWithWeights(service_d).stream()
				.mapToDouble(x -> x.getEdges().stream().mapToDouble(e -> e.getWeight()).sum())
				.sum();

		double expected = 11.8;
		assertEquals(expected, actual, 0.001d);
	}
	
	@Test
	public void testTraverseVertices_UsingUserFunction() {
		
		class TestUserFunction implements Consumer<GraphNode<Integer>> {
			private double sum = 0.0d;
			
			@Override public void accept(GraphNode<Integer> node) {
				for ( GraphEdge<?> edge : node.getEdges() ) {
					sum += edge.getWeight();
				}
			}
		}
		TestUserFunction userFunction = new TestUserFunction();
		
		loadTestGraphWithWeights(service_d).stream().forEach(userFunction);
		
		assertEquals(11.8d, userFunction.sum, 0.001d);
	}

}
