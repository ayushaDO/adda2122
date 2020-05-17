package us.lsi.graphs.virtual;

import java.util.List;

import org.jgrapht.Graph;

import us.lsi.path.EGraphPath;
import us.lsi.path.EGraphPath.PathType;

public interface EGraph<V, E> extends Graph<V,E> {
	/**
	 * @param v Un vertice
	 * @return Una lista con los vecinos del vertice
	 */
	List<E> edgesListOf(V v);
	/**
	 * @param vertex es el v�rtice actual
	 * @return El peso de vertex
	 */
	double getVertexWeight(V vertex);
	/**
	 * @param vertex El v�rtice actual
	 * @param edgeIn Una arista entrante o incidente en el v�rtice actual. Es null en el v�rtice inicial.
	 * @param edgeOut Una arista saliente o incidente en el v�rtice actual. Es null en el v�rtice final.
	 * @return El peso asociado al v�rtice suponiendo las dos aristas dadas. 
	 */
	double getVertexPassWeight(V vertex, E edgeIn, E edgeOut); 
	
	
	/**
	 * @return Devuelve un EGraphPath del tipo establecido con el v�rtice inicial indicado
	 */
	EGraphPath<V,E> initialPath();
	
	
	/**
	 * @return El vertice inicial
	 */
	V startVertex(); 
	
	
	/**
	 * @return El tipo de camino
	 */
	PathType pathType();
	
}
