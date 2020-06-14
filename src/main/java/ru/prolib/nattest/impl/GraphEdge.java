package ru.prolib.nattest.impl;

public interface GraphEdge<VertexType> {
	double getWeight();
	VertexType getSource();
	VertexType getTarget();
}
