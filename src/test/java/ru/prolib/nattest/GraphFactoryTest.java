package ru.prolib.nattest;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

import ru.prolib.nattest.impl.GraphImpl;

public class GraphFactoryTest {
	GraphFactory service;
	
	@Before
	public void setUp() throws Exception {
		service = new GraphFactory();
	}
	
	@Test
	public void testGetInstance() {
		GraphFactory actual = GraphFactory.getInstance();
		
		assertNotNull(actual);
		assertSame(actual, GraphFactory.getInstance());
	}
	
	@Test
	public void testCreateDirectedGraph() {
		
		Graph<Integer> actual = service.createDirectedGraph();
		
		assertNotNull(actual);
		assertThat(actual, instanceOf(GraphImpl.class));
		GraphImpl<Integer> o = (GraphImpl<Integer>) actual;
		assertFalse(o.isUndirected());
		assertEquals(GraphImpl.DEFAULT_WEIGHT, o.getDefaultWeight(), 0.01d);
	}
	
	@Test
	public void testCreateDirectedGraph1() {
		
		Graph<Integer> actual = service.createDirectedGraph(0.35d);
		
		assertNotNull(actual);
		assertThat(actual, instanceOf(GraphImpl.class));
		GraphImpl<Integer> o = (GraphImpl<Integer>) actual;
		assertFalse(o.isUndirected());
		assertEquals(0.35d, o.getDefaultWeight(), 0.01d);
	}
	
	@Test
	public void testCreateUndirectedGraph() {
		
		Graph<Integer> actual = service.createUndirectedGraph();
		
		assertNotNull(actual);
		assertThat(actual, instanceOf(GraphImpl.class));
		GraphImpl<Integer> o = (GraphImpl<Integer>) actual;
		assertTrue(o.isUndirected());
		assertEquals(GraphImpl.DEFAULT_WEIGHT, o.getDefaultWeight(), 0.01d);
	}
	
	@Test
	public void testCreateUndirectedGraph1() {
		
		Graph<Integer> actual = service.createUndirectedGraph(756.95d);
		
		assertNotNull(actual);
		assertThat(actual, instanceOf(GraphImpl.class));
		GraphImpl<Integer> o = (GraphImpl<Integer>) actual;
		assertTrue(o.isUndirected());
		assertEquals(756.95d, o.getDefaultWeight(), 0.01d);
	}

}
