package us.lsi.astar;

import java.util.function.Function;

import us.lsi.common.TriFunction;
import us.lsi.graphs.SimpleEdge;
import us.lsi.graphs.virtual.SimpleVirtualGraph;
import us.lsi.graphs.virtual.VirtualVertex;
import us.lsi.path.EGraphPath.PathType;

public class AStarSimpleVirtualGraph<V extends VirtualVertex<V,E>, E extends SimpleEdge<V>> extends SimpleVirtualGraph<V, E> implements AStarGraph<V, E> {

	public static <V extends VirtualVertex<V,E>, E extends SimpleEdge<V>> AStarSimpleVirtualGraph<V, E> create(V startVertex) {
		return new AStarSimpleVirtualGraph<V,E>(startVertex,PathType.Sum,null, null, null);
	}
	
	public static <V extends VirtualVertex<V,E>, E extends SimpleEdge<V>> AStarSimpleVirtualGraph<V, E> create(
			V startVertex,
			Function<E, Double> edgeWeight) {
		return new AStarSimpleVirtualGraph<V,E>(startVertex, PathType.Sum,edgeWeight, null, null);
	}
	
	public static <V extends VirtualVertex<V,E>, E extends SimpleEdge<V>> AStarSimpleVirtualGraph<V, E> create(
			V startVertex,
			Function<E, Double> edgeWeight, Function<V, Double> vertexWeight,
			TriFunction<V, E, E, Double> vertexPassWeight) {
		return new AStarSimpleVirtualGraph<V,E>(startVertex, PathType.Sum, edgeWeight, vertexWeight, vertexPassWeight);
	}

	private Function<E,Double> edgeWeight = null;
	private Function<V,Double> vertexWeight = null;
	private TriFunction<V,E,E,Double> vertexPassWeight= null;
	
	
	
	private AStarSimpleVirtualGraph(V startVertex, PathType type, Function<E, Double> edgeWeight,
			Function<V, Double> vertexWeight, TriFunction<V, E, E, Double> vertexPassWeight) {
		super(startVertex,type, edgeWeight, vertexWeight, vertexPassWeight);
		this.edgeWeight = edgeWeight;
		this.vertexWeight = vertexWeight;
		this.vertexPassWeight = vertexPassWeight;
	}
	/**
	 * @param edge es una arista
	 * @return El peso de edge
	 */
	public double getEdgeWeight(E edge) {
		Double r;
		if(edgeWeight != null) r = edgeWeight.apply(edge);
		else r = super.getEdgeWeight(edge);
		return r;
	}
	/**
	 * @param vertex es el v�rtice actual
	 * @return El peso de vertex
	 */
	public double getVertexWeight(V vertex) {
		Double r = 0.;
		if(vertexWeight != null) r = vertexWeight.apply(vertex);
		return r;
	}
	/**
	 * @param vertex El v�rtice actual
	 * @param edgeIn Una arista entrante o incidente en el v�rtice actual. Es null en el v�rtice inicial.
	 * @param edgeOut Una arista saliente o incidente en el v�rtice actual. Es null en el v�rtice final.
	 * @return El peso asociado al v�rtice suponiendo las dos aristas dadas. 
	 */
	public double getVertexPassWeight(V vertex, E edgeIn, E edgeOut) {
		Double r = 0.;
		if(vertexPassWeight != null) r = vertexPassWeight.apply(vertex,edgeIn,edgeOut);
		return r;
	}
	
}
