package ru.prolib.nattest;

import static ru.prolib.nattest.impl.GraphType.*;

import ru.prolib.nattest.impl.GraphImpl;

public class GraphFactory {
	private static final GraphFactory instance = new GraphFactory();
	
	public static GraphFactory getInstance() {
		return instance;
	}

	public <T> Graph<T> createDirectedGraph() {
		return new GraphImpl<>(DIRECTED);
	}
	
	public <T> Graph<T> createUndirectedGraph() {
		return new GraphImpl<>(UNDIRECTED);
	}
	
	protected <T> Graph<T> createDirectedGraph(double defaultWeight) {
		return new GraphImpl<>(DIRECTED, defaultWeight);
	}
	
	protected <T> Graph<T> createUndirectedGraph(double defaultWeight) {
		return new GraphImpl<>(UNDIRECTED, defaultWeight);
	}
	
}
