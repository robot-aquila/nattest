package ru.prolib.nattest;

import java.util.Collection;

public interface Graph<VertexType> {
	Graph<VertexType> addVertex(VertexType vertex);
	Graph<VertexType> addEdge(VertexType source, VertexType target);
	Collection<VertexType> getPath(VertexType source, VertexType target);
}
