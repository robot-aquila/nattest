package ru.prolib.nattest.impl;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GraphEdgeImpl<VertexType> implements GraphEdge<VertexType> {
	private final double weight;
	private final VertexType source, target;
	
	public GraphEdgeImpl(VertexType source, VertexType target, double weight) {
		if ( source == null ) {
			throw new NullPointerException("Source vertex cannot be null");
		}
		if ( target == null ) {
			throw new NullPointerException("Target vertex cannot be null");
		}
		this.weight = weight;
		this.source = source;
		this.target = target;
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public VertexType getSource() {
		return source;
	}

	@Override
	public VertexType getTarget() {
		return target;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(11589001, 59)
				.append(source)
				.append(target)
				.append(weight)
				.build();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("source", source)
				.append("target", target)
				.append("weight", weight)
				.build();
	}
	
	@Override
	public boolean equals(Object other) {
		if ( other == this ) {
			return true;
		}
		if ( other == null || other.getClass() != GraphEdgeImpl.class ) {
			return false;
		}
		GraphEdgeImpl<?> o = (GraphEdgeImpl<?>) other;
		return new EqualsBuilder()
				.append(o.source, source)
				.append(o.target, target)
				.append(o.weight, weight)
				.build();
	}

}
