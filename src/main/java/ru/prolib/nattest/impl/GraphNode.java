package ru.prolib.nattest.impl;

import java.util.Collection;

public interface GraphNode<VertexType> {
	VertexType getVertex();
	boolean isNeighbor(VertexType vertex);
	Collection<VertexType> getNeighbors();
	Collection<GraphEdge<VertexType>> getEdges();
}
