package us.lsi.flowgraph.examples;


import us.lsi.flowgraph.FlowGraphSolution;
import us.lsi.pli.AlgoritmoPLI;
import us.lsi.pli.SolutionPLI;

import us.lsi.common.Files2;
import us.lsi.flowgraph.FlowGraph;
import us.lsi.flowgraph.FlowGraph.FGType;



/**
 * Un ejemplo de red de flujo
 * 
 * @author Miguel Toro
 *
 */
public class Flow1 {

	/**
	 * @param args Argumentos
	 */
	public static void main(String[] args) {
		
		FlowGraph fg = FlowGraph.newGraph("ficheros/flow1.txt",FGType.Max);
		String constraints = fg.getConstraints();
		Files2.toFile(constraints,"ficheros/flow1Constraints.txt");		
		SolutionPLI s = AlgoritmoPLI.getSolutionFromFile("ficheros/flow1Constraints.txt");	
		FlowGraphSolution fs = FlowGraphSolution.create(fg,s);
		fs.exportToDot("ficheros/flow1Soluciones.gv");
	}
	
	
	
}
