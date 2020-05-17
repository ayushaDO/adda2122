package us.lsi.pli;

import java.util.List;

public interface SolutionPLI {
	/**
	 * @return El coste total del objetivo
	 */
	public double getGoal();
	/**
	 * @return El punto soluci�n
	 */
	public double[] getSolution();	

	/**
	 * @param i El valor de la variable i
	 * @return La soluci�n de la variable i
	 */
	public double getSolution(int i); 
		
	/**
	 * @return Los identificadores de las variables
	 */
	public List<String> getNames(); 

	/**
	 * @param i Un �ndice de variable en el rango 0..getNumVar()-1
	 * @return El identificador de la variable i
	 */
	public String getName(int i); 
	
	/**
	 * @return N�mero de variables
	 */
	public int getNumVar();
	
}
