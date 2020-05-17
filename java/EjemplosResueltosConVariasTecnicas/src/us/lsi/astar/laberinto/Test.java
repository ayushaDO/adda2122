package us.lsi.astar.laberinto;


import java.util.function.BiFunction;

import org.jgrapht.GraphPath;

import us.lsi.astar.AStarAlgorithm;
import us.lsi.astar.AStarGraph;
import us.lsi.common.IntPair;
import us.lsi.graphs.Graphs2;

public class Test {

	public static void main(String[] args) {
		BiFunction<Casilla,Casilla,Double> heuristic = (c1,c2)->1.*c1.getPosition().manhattan(c2.getPosition());
		Laberinto.leeDatos("ficheros/laberinto.txt", 6, 8);
		System.out.println(Laberinto.showLaberinto());
		System.out.println("______________");
		Casilla c1 = Laberinto.get(IntPair.of(0,0));		
		Casilla c2 = Laberinto.get(IntPair.of(5,7));
		AStarGraph<Casilla,CasillaEdge> graph = Graphs2.astarSimpleVirtualGraph(c1,x->1.);
		AStarAlgorithm<Casilla, CasillaEdge> a = AStarAlgorithm.of(graph,c1,c2,heuristic);
		GraphPath<Casilla, CasillaEdge> path = a.getPath();
		System.out.println(Laberinto.showSolucionLaberinto(path));
		
	}

}
