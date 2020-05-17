package us.lsi.graphs.examples;

import java.io.PrintWriter;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.alg.color.GreedyColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.io.ComponentNameProvider;
import org.jgrapht.io.DOTExporter;
import org.jgrapht.io.IntegerComponentNameProvider;

import us.lsi.colors.GraphColors;
import us.lsi.common.Files2;
import us.lsi.grafos.datos.Carretera;
import us.lsi.grafos.datos.Ciudad;
import us.lsi.graphs.Graphs2;
import us.lsi.graphs.GraphsReader;

/**
 * Resulve un problema de coloreado de grafos
 * 
 * @author Miguel Toro
 *
 */
public class ColoreadoDeGrafos {

	public static void main(String[] args) {
		
		Graph<Ciudad,Carretera> graph =  
				GraphsReader.newGraph("ficheros/andalucia.txt",
						Ciudad::ofFormat, 
						Carretera::ofFormat,
						Graphs2::simpleWeightedGraph,
						Carretera::getKm);
		
		VertexColoringAlgorithm<Ciudad> vca = new GreedyColoring<>(graph);
		VertexColoringAlgorithm.Coloring<Ciudad> vc = vca.getColoring();
		Map<Ciudad,Integer> colorDeCiudad = vc.getColors();
		System.out.println(vc.getNumberColors());
		ComponentNameProvider<Ciudad> vertexIDProvider = new IntegerComponentNameProvider<>();
		DOTExporter<Ciudad,Carretera> de = new DOTExporter<Ciudad,Carretera>(
				vertexIDProvider,
				x->x.getNombre(), 
				x->x.getNombre(),
				x->GraphColors.getFilledColor(colorDeCiudad.get(x)),
				null);
		PrintWriter f = Files2.getWriter("ficheros/coloresAndalucia.gv");
		de.exportGraph(graph, f);
	}

}
