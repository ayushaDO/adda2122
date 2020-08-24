package us.lsi.problemas.reinas;

import java.util.List;
import java.util.Set;

import us.lsi.ag.SeqNormalProblemAG;
import us.lsi.ag.agchromosomes.AlgoritmoAG;
import us.lsi.ag.agchromosomes.ChromosomeFactory;
import us.lsi.ag.agchromosomes.SeqNomalChromosome;
import us.lsi.ag.agchromosomes.ChromosomeFactory.CrossoverType;
import us.lsi.ag.agstopping.StoppingConditionFactory;
import us.lsi.ag.agstopping.StoppingConditionFactory.StoppingConditionType;
import us.lsi.common.Sets2;
import us.lsi.reinas.datos.Reina;



public class TestReinasAG {

	

	public static void main(String[] args){
		
		AlgoritmoAG.ELITISM_RATE  = 0.20;
		AlgoritmoAG.CROSSOVER_RATE = 0.8;
		AlgoritmoAG.MUTATION_RATE = 0.8;
		AlgoritmoAG.POPULATION_SIZE = 40;
		
		StoppingConditionFactory.NUM_GENERATIONS = 6000;
		StoppingConditionFactory.SOLUTIONS_NUMBER_MIN = 1;
		StoppingConditionFactory.FITNESS_MIN = 0.;
		StoppingConditionFactory.stoppingConditionType = StoppingConditionType.SolutionsNumber;
		
		ChromosomeFactory.crossoverType = CrossoverType.OnePoint;
		
		ProblemaReinasAG.numeroDeReinas = 40;
		SeqNormalProblemAG<List<Reina>> p = ProblemaReinasAG.create();
		AlgoritmoAG<SeqNomalChromosome> ap = AlgoritmoAG.<SeqNomalChromosome>create(p);
		ap.ejecuta();
		System.out.println("================================");
		
		System.out.println("================================");
/*		Set<Chromosome> s = AlgoritmoAG.bestChromosomes.stream().collect(Collectors.toSet());
		for (Chromosome c: s) {
			System.out.println(ChromosomeFactory.asIndex(c).fitness()+","+p.getSolucion(ChromosomeFactory.asIndex(c)));
		}
		System.out.println("================================");
		System.out.println(s.size());
*/		SeqNomalChromosome cr = ap.getBestChromosome();
		System.out.println(p.getSolucion(cr)+","+cr.fitness()+", ");
		List<Integer> ls = cr.decode();
		Set<Integer> dp = Sets2.empty();
		Set<Integer> ds = Sets2.empty();
		for (int i = 0; i < ls.size(); i++) {
			dp.add(ls.get(i)-i);
			ds.add(ls.get(i)+i);
		}
		System.out.println(ProblemaReinasAG.numeroDeReinas+","+dp.size()+","+ds.size());
	}	

}

