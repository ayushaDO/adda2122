package us.lsi.flowgraph;

import java.util.stream.Collectors;

import us.lsi.common.Strings2;
import us.lsi.flowgraph.FlowGraph.FGType;
import us.lsi.lpsolve.AlgoritmoPLI;
import us.lsi.lpsolve.SolutionPLI;

public class TestFlow3 {

	public static void main(String[] args) {
		FlowGraph g = FlowGraph.newGraph("ficheros/andaluciaFlow1.txt", FGType.Max);
		System.out.println(g);
		System.out.println(g.getMinCutConstraints());
		g.exportToDot("ficheros/andaluciaflow1.gv");
		String constraints = g.getMinCutConstraints();
		Strings2.toFile(constraints, "ficheros/andaluciaConstraints1.txt");
		SolutionPLI s = AlgoritmoPLI.getSolution(constraints);	
		FlowGraphSolution fs = FlowGraphSolution.create(g, s);
//		fs.exportToDot("ficheros/andaluciaMinCutSolution.gv");
		System.out.println(fs.getGoal());
		System.out.println(fs.getFlowEdges().entrySet().stream()
				.filter(x->x.getValue()==1)
				.map(x->x.getKey())
				.collect(Collectors.toList()));
		System.out.println(fs.getFlowVertices().entrySet().stream()
				.filter(x->x.getValue()==1)
				.map(x->x.getKey())
				.collect(Collectors.toList()));
		System.out.println(fs.getFlowVertices().entrySet().stream()
				.filter(x->x.getValue()==0)
				.map(x->x.getKey())
				.collect(Collectors.toList()));
	}

}
