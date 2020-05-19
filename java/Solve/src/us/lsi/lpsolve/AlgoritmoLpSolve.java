package us.lsi.lpsolve;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;
import us.lsi.common.Preconditions;


/**
 * @author Miguel Toro
 * 
 * <p> Un algoritmo de Programaci�n Lineal Entera. El algoritmo lee un fichero en formato 
 * <a href="http://lpsolve.sourceforge.net/5.5/" target="_blank">LpSolve</a> y calcula la soluci�n. 
 * Se pueden declarar tres tipos de variables: enteras (int), binarias (bin) y reales (por defecto).
 * Hay otros tipos de variables posibles (libres, semicontinuas) que pueden consultarse en la documentaci�n de LpSolve. </p>
 * 
 * 
 * <p> La implementaci�n reutiliza el algortimo LpSolve. </p>
 */
public class AlgoritmoLpSolve implements SolutionLpSolve {
	
	/**
	 * @param constraints Restricciones que especifican el problema
	 * @return La solucion del problema
	 */
	public static SolutionLpSolve getSolution(String constraints) {
		try {
			final PrintWriter f = 
					new PrintWriter(
							new BufferedWriter(
									new FileWriter("ficheros/intermedio.txt")));
			f.println(constraints);
			f.close();
		} catch (Exception e) {		
			throw new IllegalArgumentException(
					e.toString() + "ficheros/intermedio.txt");
		}
		return getSolutionFromFile("ficheros/intermedio.txt");
	}
	
	/**
	 * @param file Un fichero con las restricciones del problema
	 * @return La soluci�n del problema
	 */
	public static SolutionLpSolve getSolutionFromFile(String file) {
		AlgoritmoLpSolve a = new AlgoritmoLpSolve(file);
		a.ejecuta();
		return a;
	}
	
	private double[] solutionPoint;
	private double solutionValue;
	private List<String> names;
	
	private String fichero;
	
	private LpSolve solver;

	private AlgoritmoLpSolve(String fichero) {
		super();
		this.fichero = fichero;
	}
	
	/**
	 * @return El coste total del objetivo
	 */
	public double getGoal(){
		return solutionValue;
	}

	/**
	 * @return El punto soluci�n
	 */
	public double[] getSolution() {
		return solutionPoint;
	}

	/**
	 * @param i El valor de la variable i
	 * @return La soluci�n de la variable i
	 */
	public double getSolution(int i) {
		return solutionPoint[i];
	}
		
	public List<String> getNames() {
		return names;
	}

	/**
	 * @param i Un �ndice de variable en el rango 0..getNumVar()-1
	 * @return El identificador de la variable i
	 */
	public String getName(int i) {
		return names.get(i);
	}
	
	/**
	 * @return N�mero de variables
	 */
	public int getNumVar() {
		return solutionPoint.length;
	}
	
	
	/**
	 * @return El fichero de entrada
	 */
	public String getFichero() {
		return fichero;
	}
	
	/**
	 * Ejecuta el algoritmo
	 */
	public void ejecuta(){
		
		try {		
			solver = LpSolve.readLp(this.fichero, 1, "Problema");
		} catch (LpSolveException e) {
			throw new IllegalStateException("Se ha producido una excepci�n en LpSolve "+e+" = "+this.fichero);
		}
		try {
			solver.solve();
			solutionPoint = solver.getPtrVariables();
			solutionValue = solver.getObjective();
			Preconditions.checkState(!solver.isFeasible(solutionPoint,solutionValue));
			names = new ArrayList<>();
			for (int j = 1; j <= solutionPoint.length; j++) {
				names.add(solver.getOrigcolName(j));
			}
			solver.deleteLp();
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("El modelo no tiene soluci�n = "+e);
		} catch (LpSolveException e) {
			throw new IllegalStateException("Se ha producido una excepci�n en LpSolve = "+e);
		}

	}
	
}
