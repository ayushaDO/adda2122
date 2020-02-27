package us.lsi.graphs.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.graph.GraphWalk;

import us.lsi.common.Preconditions;
import us.lsi.common.TriFunction;
import us.lsi.flujossecuenciales.Iterators;
import us.lsi.graphs.virtual.ActionSimpleEdge;

public interface Search<V,E> extends Iterator<V>, Iterable<V> {
	
	public static <V, E> Search<V, E> greedy(Graph<V, E> graph,
			V initialVertex, Function<V, V> nextVertex) {
		return new GreedySearchOnGraph<V, E>(graph, initialVertex, nextVertex);
	}

	public static <V, E extends ActionSimpleEdge<V, A>, A> Search<V, E> greedy(V initialVertex,
			Function<V, A> nextAction, BiFunction<V, A, V> nextVertex,
			TriFunction<V, V, A, E> factory) {
		return new GreedySearch<V, E, A>(initialVertex, nextAction, nextVertex, factory);
	}
	
	public static <V, E> Search<V, E> depth(Graph<V, E> g, V startVertex) {
		return new DephtSearch<V, E>(g, startVertex);
	}
	
	public static <V, E> Search<V, E> breadth(Graph<V, E> g, V startVertex) {
		return new BreadthSearch<V, E>(g, startVertex);
	}
	
	public static <V, E> Search<V, E> dijsktra(Graph<V, E> graph, V initial) {
		return new AStarSearch<V, E>(graph, initial, null, (v1,v2)->0.);
	}
	
	public static <V, E> Search<V, E> aStar(Graph<V, E> graph, V initial, V end,
			BiFunction<V, V, Double> heuristic) {
		return new AStarSearch<V, E>(graph, initial, end, heuristic);
	}
	
	E getEdgeToOrigin(V v);
	boolean isSeenVertex(V v);
	Graph<V, E> getGraph();
	Iterator<V> iterator();
	
	
	default V getParent(V v) {
		return Graphs.getOppositeVertex(this.getGraph(), this.getEdgeToOrigin(v), v);
	}
	
	/**
	 * @return Un flujo con los v&eacute;rtices del grafo recorridos en anchura
	 */
	default public Stream<V> stream(){
		return Iterators.asStream(this.iterator());
	}
	
	/**
	 * @param p Un predicado 
	 * @return Encuentra el priemr v&eacute;rtice que cumple el predicado en una b&uacute;squeda en anchura
	 */
	default public V find(Predicate<V> p) {
		Optional<V> r = this.stream().filter(p).findFirst();
		Preconditions.checkArgument(r.isPresent(), "No se ha encontrado un v�rtice que cumpla el predicado");
		return r.get();
	}
	
	/**
	 * @pre El v&eacute;rtice v tiene que haber sido visitado
	 * @param v Un v&eacute;rtice
	 * @return El camino m&aacute; corto en pasos hacia el origen 
	 */
	default public GraphPath<V,E> pathToOrigin(V v){
		Preconditions.checkArgument(this.getEdgeToOrigin(v)!=null,String.format("El v�rtice %s no ha sido visitado",v));
		E edge = this.getEdgeToOrigin(v);
		List<V> path = new ArrayList<>();
		path.add(v);
		Double w = 0.;
		while(edge!=null) {	
			w = w+this.getGraph().getEdgeWeight(edge);
			v = this.getParent(v);	
			path.add(v);
			edge = this.getEdgeToOrigin(v);			
		}
		return new GraphWalk<V,E>(this.getGraph(),path,w);
	}
	
	default public GraphPath<V,E> pathFromOrigin(V v){
		GraphPath<V,E> path = pathToOrigin(v);
		List<V> vertices = path.getVertexList();
		Collections.reverse(vertices);
		return new GraphWalk<V,E>(this.getGraph(),vertices,path.getWeight());
	}
}
