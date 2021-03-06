package us.lsi.ag.agchromosomes;

import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;
import org.apache.commons.math3.genetics.RandomKey;

import us.lsi.ag.Chromosome;
import us.lsi.ag.Data;
import us.lsi.ag.ValuesInRangeData;
import us.lsi.ag.agchromosomes.ChromosomeFactory.ChromosomeType;


/**
 * @author Miguel Toro
 * 
 * 
 * <p> Una implementaci?n del tipo ValuesInRangeCromosome&lt;Integer&gt;. Toma como informaci?n la definici?n de un problema que implementa el interfaz 
 * ValuesInRangeProblemAG. </p>
 * 
 * <p> Asumimos que el n?mero de variables es n. La lista decodificada est? formada por una lista de  
 * enteros de tama?o n cuyos elementos para cada i son 
 * valores en en rango [getMin(i),getMax(i)]. </p>
 * 
 * <p> La implementaci?n usa un cromosoma RandomKey del tama?o n.  </p>
 * 
 * <p> Es un cromosoma adecuado para codificar problemas de subconjuntos de multiconjuntos</p>
 *
 */
public class RangeChromosome extends RandomKey<Integer>  
             implements ValuesInRangeData<Integer,Object>, Chromosome<List<Integer>> {
	
	public static ValuesInRangeData<Integer,Object> data;
	
	/**
	 * Dimensi?n del cromosoma igual a size()
	 */
	
	public static int DIMENSION;
	
	@SuppressWarnings("unchecked")
	public static void iniValues(Data data){
		RangeChromosome.data = (ValuesInRangeData<Integer,Object>) data; 
		RangeChromosome.DIMENSION = RangeChromosome.data.size();
	}
	
	public RangeChromosome(Double[] representation) throws InvalidRepresentationException {
		super(representation);
		this.ft = this.calculateFt();
	}

	public RangeChromosome(List<Double> representation) throws InvalidRepresentationException {
		super(representation);
		this.ft = this.calculateFt();
	}

	@Override
	public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> ls) {
		return new RangeChromosome(ls);
	}
	
	private Integer convert(Double e, Integer i) {
//		System.out.printf("%.2f,%d,%d,%d,%d\n",e,i,RangeChromosome.DIMENSION,this.getMin(i),this.getMax(i));
		return (int) (this.getMin(i) + (this.getMax(i)-this.getMin(i))*e);
	}
	
	public List<Integer> decode() {
		List<Double> ls = super.getRepresentation();
		return IntStream.range(0,ls.size()).boxed().map(i->this.convert(ls.get(i),i)).toList();
	}
	
	public static RangeChromosome getInitialChromosome() {
		List<Double> ls = RandomKey.randomPermutation(RangeChromosome.DIMENSION);
		return new RangeChromosome(ls);
	}

	@Override
	public double fitness() {
		return ft;
	}
	
	private double ft;
	
	protected double calculateFt(){
		return RangeChromosome.data.fitnessFunction(this.decode());
	}

	@Override
	public Integer getMax(Integer i) {
		return RangeChromosome.data.getMax(i);
	}

	@Override
	public Integer getMin(Integer i) {
		return RangeChromosome.data.getMin(i);
	}

	@Override
	public ChromosomeType getType() {
		return ChromosomeFactory.ChromosomeType.Range;
	}

	@Override
	public Integer size() {
		return RangeChromosome.data.size();
	}

	@Override
	public Double fitnessFunction(List<Integer> dc) {
		return RangeChromosome.data.fitnessFunction(dc);
	}

	@Override
	public Object getSolucion(List<Integer> dc) {
		return RangeChromosome.data.getSolucion(dc);
	}

	
}

