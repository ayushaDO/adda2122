package us.lsi.astar;

import us.lsi.graphs.virtual.EGraph;

public interface AStarGraph<V, E> extends EGraph<V,E> {
	
	/**
	 * @param vertex es el v�rtice actual
	 * @return El peso de vertex
	 */
	default double getVertexWeight(V vertex) {
		return 0.;
	}
	/**
	 * @param vertex El v�rtice actual
	 * @param edgeIn Una arista entrante o incidente en el v�rtice actual. Es null en el v�rtice inicial.
	 * @param edgeOut Una arista saliente o incidente en el v�rtice actual. Es null en el v�rtice final.
	 * @return El peso asociado al v�rtice suponiendo las dos aristas dadas. 
	 */
	default double getVertexPassWeight(V vertex, E edgeIn, E edgeOut) {
		return 0.;
	}
	
	
}
