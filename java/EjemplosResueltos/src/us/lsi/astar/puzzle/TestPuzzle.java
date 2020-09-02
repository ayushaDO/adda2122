package us.lsi.astar.puzzle;

import us.lsi.graphs.virtual.ActionSimpleEdge;

public class TestPuzzle {

	public static void main(String[] args) {
		
		VertexPuzzle e = VertexPuzzle.of(0,1,2,3,4,5,6,7,8);
		VertexPuzzle e1 = VertexPuzzle.of(1,2,3,4,0,5,6,7,8);
		VertexPuzzle e2 = VertexPuzzle.of(1,2,3,4,0,5,6,7,8);
		System.out.println(e);
		System.out.println("--------------");
		System.out.println(e1);
		System.out.println(e1.equals(e2));
		System.out.println(e.equals(e2));
		System.out.println("--------------");
		System.out.println(e);
		System.out.println("Vecinos");
		for (VertexPuzzle v: e.getNeighborListOf()) {
			System.out.println(v+"\n");
		}
		System.out.println("--------------");
		System.out.println(e1);
		System.out.println("Vecinos");
		for (VertexPuzzle v: e1.getNeighborListOf()) {
			System.out.println(v+"\n");
		}
		System.out.println("--------------");
		System.out.println(e);
		System.out.println("Aristas");
		for (ActionSimpleEdge<VertexPuzzle, ActionPuzzle> ed: e.edgesOf()) {
			System.out.println(ed+"\n");
		}
		System.out.println("--------------");
		System.out.println(e);
		System.out.println("Acciones");
		for (ActionSimpleEdge<VertexPuzzle, ActionPuzzle> ed: e.edgesOf()) {
			System.out.println(ActionPuzzle.actions().get(ed.action.getIndex()));
		}
	}

}
