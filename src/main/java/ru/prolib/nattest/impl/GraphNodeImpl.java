package ru.prolib.nattest.impl;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GraphNodeImpl<VertexType> implements GraphNode<VertexType> {
	private final VertexType vertex;
	private final Map<VertexType, GraphEdge<VertexType>> edges;
	
	GraphNodeImpl(VertexType vertex, Map<VertexType, GraphEdge<VertexType>> edges) {
		if ( vertex == null ) {
			throw new NullPointerException("Vertex cannot be null");
		}
		this.vertex = vertex;
		this.edges = edges;
	}
	
	public GraphNodeImpl(VertexType vertex) {
		this(vertex, new ConcurrentHashMap<>());
	}
	
	protected GraphEdge<VertexType> produceEdge(VertexType target, double weight) {
		return new GraphEdgeImpl<>(this.vertex, target, weight);
	}
	
	public GraphNodeImpl<VertexType> addEdge(VertexType target, double weight) {
		GraphEdge<VertexType> new_edge, old_edge;
		old_edge = edges.putIfAbsent(target, new_edge = produceEdge(target, weight));
		if ( old_edge != null && old_edge != new_edge ) {
			throw new IllegalArgumentException("Edge already defined: " + vertex + " -> " + target);
		}
		return this;
	}

	@Override
	public VertexType getVertex() {
		return vertex;
	}

	@Override
	public boolean isNeighbor(VertexType vertex) {
		return edges.containsKey(vertex);
	}

	@Override
	public Collection<VertexType> getNeighbors() {
		return edges.keySet();
	}
	
	@Override
	public Collection<GraphEdge<VertexType>> getEdges() {
		return edges.values();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("vertex", vertex)
				.append("edges", getEdges().stream()
						.map(x -> x.getTarget() + "(" + x.getWeight() + ")")
						.collect(Collectors.toList()))
				.build();
	}
	
	@Override
	public boolean equals(Object other) {
		if ( other == this ) {
			return true;
		}
		if ( other == null || other.getClass() != GraphNodeImpl.class ) {
			return false;
		}
		GraphNodeImpl<?> o = (GraphNodeImpl<?>) other;
		return new EqualsBuilder()
				.append(o.vertex, vertex)
				.append(o.edges, edges)
				.build();
	}

}
