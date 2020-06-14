package ru.prolib.nattest.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

import ru.prolib.nattest.Graph;

public class GraphImpl<VertexType> implements Graph<VertexType> {
	public static final GraphType DEFAULT_GRAPH_TYPE = GraphType.UNDIRECTED;
	public static final double DEFAULT_WEIGHT = 1.0d;
	
	private final boolean undirected;
	private final double defaultWeight;
	private final Map<VertexType, GraphNodeImpl<VertexType>> nodes;
	
	GraphImpl(GraphType type, double defaultWeight, Map<VertexType, GraphNodeImpl<VertexType>> nodes) {
		this.undirected = type == GraphType.UNDIRECTED;
		this.defaultWeight = defaultWeight;
		this.nodes = nodes;
	}
	
	public GraphImpl(GraphType type, double defaultWeight) {
		this(type, defaultWeight, new ConcurrentHashMap<>());
	}
	
	public GraphImpl(GraphType type) {
		this(type, DEFAULT_WEIGHT);
	}
	
	public GraphImpl() {
		this(DEFAULT_GRAPH_TYPE, DEFAULT_WEIGHT);
	}
	
	protected GraphNodeImpl<VertexType> produceNode(VertexType vertex) {
		return new GraphNodeImpl<>(vertex);
	}
	
	protected GraphNodeImpl<VertexType> getNode(VertexType vertex) {
		GraphNodeImpl<VertexType> node = nodes.get(vertex);
		if ( node == null ) {
			throw new IllegalArgumentException("Vertex not found: " + vertex);
		}
		return node;
	}
	
	public boolean isUndirected() {
		return undirected;
	}
	
	public boolean isDirected() {
		return ! undirected;
	}
	
	public double getDefaultWeight() {
		return defaultWeight;
	}
	
	@Override
	public GraphImpl<VertexType> addVertex(VertexType vertex) {
		GraphNodeImpl<VertexType> old_node, new_node;
		old_node = nodes.putIfAbsent(vertex, new_node = produceNode(vertex));
		if ( old_node != null && old_node != new_node ) {
			throw new IllegalArgumentException("Vertex already exists: " + vertex);
		}
		return this;
	}

	@Override
	public GraphImpl<VertexType> addEdge(VertexType source, VertexType target) {
		return addEdge(source, target, defaultWeight);
	}

	public GraphImpl<VertexType> addEdge(VertexType source, VertexType target, double weight) {
		GraphNodeImpl<VertexType> source_node = getNode(source), target_node = getNode(target);
		source_node.addEdge(target, weight);
		// Inconsistent state possible when concurrent adding edges. Possible solution is to use
		// addition map for the pairs of vertices to lock each pair during modifications.
		// Deeper investigation needed for the best solution.
		if ( undirected == true ) {
			target_node.addEdge(source, weight);
		}
		return this;
	}
	
	static class PathInfo<VertexType> implements Comparable<PathInfo<VertexType>> {
		private final double cost;
		private final VertexType vertex;
		
		PathInfo(double cost, VertexType vertex) {
			this.cost = cost;
			this.vertex = vertex;
		}

		@Override
		public int compareTo(PathInfo<VertexType> other) {
			return Double.compare(cost, other.cost);
		}
		
		@Override
		public String toString() {
			return new StringBuilder()
					.append("PathInfo[vertex=")
					.append(vertex)
					.append(",cost=")
					.append(cost)
					.append("]")
					.toString();
		}
		
	}

	@Override
	public Collection<VertexType> getPath(VertexType source, VertexType target) {
		getNode(source);
		getNode(target);
		PriorityQueue<PathInfo<VertexType>> front = new PriorityQueue<>();
		Map<VertexType, VertexType> from = new HashMap<>();
		Map<VertexType, Double> cost = new HashMap<>();
		front.add(new PathInfo<>(0.0d, source));
		from.put(source, source);
		cost.put(source, 0.0d);
		VertexType next;
		while ( front.isEmpty() == false ) {
			PathInfo<VertexType> curr = front.poll();
			if ( curr.vertex.equals(target) ) {
				break;
			}
			for ( GraphEdge<VertexType> edge : getNode(curr.vertex).getEdges() ) {
				double new_cost = cost.get(curr.vertex) + edge.getWeight();
				Double old_cost = cost.get(next = edge.getTarget());
				if ( old_cost == null || new_cost < old_cost ) {
					cost.put(next, new_cost);
					from.put(next, curr.vertex);
					front.add(new PathInfo<>(new_cost, next));
				}
			}
		}
		//System.out.println(from);
		//System.out.println(cost);
		LinkedList<VertexType> path = new LinkedList<>();
		path.addFirst(next = target);
		while ( source.equals(next) == false ) {
			next = from.get(next);
			if ( next == null ) {
				throw new IllegalStateException("Path not found: " + source + " -> " + target);
			}
			path.addFirst(next);
		}
		return path;
	}

}
