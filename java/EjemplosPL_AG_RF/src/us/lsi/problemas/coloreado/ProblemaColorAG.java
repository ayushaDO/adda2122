package us.lsi.problemas.coloreado;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import org.jgrapht.graph.SimpleWeightedGraph;

import us.lsi.ag.ValuesInRangeProblemAG;
import us.lsi.ag.agchromosomes.ChromosomeFactory.ChromosomeType;
import us.lsi.ag.agchromosomes.ValuesInRangeChromosome;
import us.lsi.grafos.datos.Carretera;
import us.lsi.grafos.datos.Ciudad;
import us.lsi.graphs.GraphsReader;
import us.lsi.graphs.Graphs2;


public class ProblemaColorAG implements ValuesInRangeProblemAG<Integer,Map<Ciudad,Integer>> {

	
	private static SimpleWeightedGraph<Ciudad,Carretera> grafo; 
	private static List<Ciudad> ciudades;
	
	public ProblemaColorAG(String ficheroGrafo) { //"./ficheros/Andalucia.txt"
		grafo = cargaGrafo(ficheroGrafo);
		ciudades = new ArrayList<>(grafo.vertexSet());
	}
	private static SimpleWeightedGraph<Ciudad, Carretera> cargaGrafo(String f) {
		return GraphsReader.newGraph(f, 
				Ciudad::ofFormat,
				Carretera::ofFormat, 
				Graphs2::simpleWeightedGraph,
				Carretera::getKm);
	}

	@Override
	public Integer getCellsNumber() {
		return ciudades.size();
	}
	
	@Override	
    public Integer getMax(Integer index){
		return ciudades.size()-1;		
	}

	@Override	
    public Integer getMin(Integer index){
		return 0;		
	}

	@Override
	public Map<Ciudad,Integer> getSolucion(ValuesInRangeChromosome<Integer> cromosoma) {
		List<Integer> solucion = cromosoma.decode();
		Map<Ciudad,Integer> res = new HashMap<>();
		IntStream.range(0, solucion.size())
		         .boxed()
		         .forEach(i ->res.put(ciudades.get(i), solucion.get(i)+1));
		return res;		
	}
	
	@Override
	public Double fitnessFunction(ValuesInRangeChromosome<Integer> cromosoma) {
		Map<Ciudad,Integer> m = getSolucion(cromosoma);
		
		long rko = grafo.edgeSet().stream()
				.filter(c -> m.get(c.getSource())== m.get(c.getTarget()))
				.count();		
  		int N = ciudades.size();
  		int CU = m.values().stream()
  				.collect(Collectors.toSet())
  				.size();
		double fitness = -((rko * N * N) + CU);
		
		return fitness;		
	}

	public Set<Set<Ciudad>> getComponentes(ValuesInRangeChromosome<Integer> cromosoma) {
		Map<Ciudad,Integer> m = getSolucion(cromosoma);
			
		return m.entrySet().stream()
				   .collect(Collectors.groupingBy(e -> e.getValue(),Collectors.mapping(e->e.getKey(),Collectors.toSet())))
				   .values().stream()
				   .collect(Collectors.toSet());
	}
	@Override
	public ChromosomeType getType() {
		return ChromosomeType.Range;
	}

}
