package us.lsi.alg.puzzle;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import us.lsi.common.Arrays2;
import us.lsi.common.IntPair;
import us.lsi.common.Preconditions;
import us.lsi.flujosparalelos.Streams2;
import us.lsi.graphs.virtual.ActionVirtualVertex;



public class VertexPuzzle implements ActionVirtualVertex<VertexPuzzle, EdgePuzzle, ActionPuzzle> {
	
	
	public static VertexPuzzle copy(VertexPuzzle m) {
		return of(m.datos, m.blackPosition);
	}

	/**
	 * @param d Lista de valores del puzzle dados por filas de abajo arriba
	 * @return Un EstadoPuzzle
	 */
	
	public static VertexPuzzle of(Integer... d) {				
		return new VertexPuzzle(d);
	}
	
	public static VertexPuzzle of(Integer[][] datos, IntPair blackPosition) {
		return new VertexPuzzle(datos, blackPosition);
	}
	
	public static int getInvCount(Integer[][] dt) {
		List<Integer> d = Streams2.allPairs(0,3,0,3).map(p->dt[p.first][p.second]).collect(Collectors.toList());
	    int inv_count = 0;
	    for (int i = 0; i < 9 - 1; i++)
	        for (int j = i + 1; j < 9; j++)
	            if (d.get(j) >0 && d.get(i) >0 &&  d.get(i) > d.get(j))
	                inv_count++;
	    return inv_count;
	}
	
	public static boolean isSolvable2(Integer[][] d1, Integer[][] d2) {
	    Integer n1 = getInvCount(d1)%2;
	    Integer n2 = getInvCount(d2)%2;
	    return n1 == n2;
	}
	 
	public static boolean isSolvable(Integer[][] puzzle) {
	    int invCount = getInvCount(puzzle);
	    return (invCount % 2 == 0);
	}
	
	public final Integer[][] datos; 
	public final IntPair blackPosition;
	public final Map<Integer,IntPair> positions;
	public static final int numFilas = 3;
	
	private static boolean validDato(Integer d) {
		return 0<=d && d < VertexPuzzle.numFilas*VertexPuzzle.numFilas;
	}
	
	public boolean validPosition(IntPair p) {
		return p.first>=0 && p.first< VertexPuzzle.numFilas && p.second>=0 && p.second<VertexPuzzle.numFilas;
	}
	
	private VertexPuzzle(Integer...datos) {
		Integer n = VertexPuzzle.numFilas;
		Integer dt[][] = Arrays2.toMultiArray(datos, n, n);	
		IntPair bp = Arrays2.findPosition(dt, e->e==0);
		this.datos = dt;
		this.blackPosition = bp;
		if (!this.isValid()) {System.out.println("No es valido");}
		this.positions = Streams2.allPairs(0,n,0,n).collect(Collectors.toMap(p->dt[p.first][p.second],p->p));
	}
	
	private VertexPuzzle(Integer[][] datos, IntPair blackPosition) {
		super();
		Integer n = VertexPuzzle.numFilas;
		this.datos = Arrays2.copyArray(datos);
		this.blackPosition = blackPosition;
		this.positions = Streams2.allPairs(0,n,0,n).collect(Collectors.toMap(p->datos[p.first][p.second],p->p));
	}
	
	@Override
	public Boolean isValid() {
		Integer n = VertexPuzzle.numFilas;
		Set<Integer> s = Arrays.stream(this.datos)
				.flatMap(f->Arrays.stream(f))
				.filter(e->VertexPuzzle.validDato(e))
				.collect(Collectors.toSet());
		return s.size()== n*n;
	}	

	public IntPair getBlackPosition() {
		return blackPosition;
	}

	@Override
	public List<ActionPuzzle> actions() {
//		if(this.equals(VertexPuzzle.lastVertex())) return new ArrayList<>();
    	return ActionPuzzle.actions.stream()
				.filter(a->a.isApplicable(this))
				.collect(Collectors.toList());
	}
	
	@Override
	public VertexPuzzle neighbor(ActionPuzzle a) {
		Preconditions.checkArgument(a.isApplicable(this), String.format("La acci�n %s no es aplicable",a.toString()));
		IntPair np = this.blackPosition.add(a.direction);
		IntPair op = this.blackPosition;
		Integer dd[][] = Arrays2.copyArray(this.datos);
		Integer value = dd[np.first][np.second];
		dd[op.first][op.second] = value;
		dd[np.first][np.second] = 0;
		VertexPuzzle v  = VertexPuzzle.of(dd,np);
		Preconditions.checkState(!this.equals(v),String.format("No deben ser iguales %s \n %s \n %s",a.toString(),this.toString(),v.toString()));
		return v;
	}
	
	
	
	public Integer getDato(int x, int y) {
		return getDato(IntPair.of(x, y));
	}
	
	public Integer getDato(IntPair p) {
		Preconditions.checkArgument(validPosition(p),"No se cumple la precondici�n");
		return datos[p.first][p.second];
	}
	
	public Integer getNumDiferentes(VertexPuzzle e){
		Integer n = VertexPuzzle.numFilas;
		Long s = IntStream.range(0,n).boxed()
				.flatMap(f->IntStream.range(0,n).boxed().map(c->IntPair.of(f, c)))
				.filter(p->this.getDato(p) != e.getDato(p))
				.count();
		return s.intValue();
	}

	@Override
	public String toString() {
		String s = IntStream.range(0,VertexPuzzle.numFilas).boxed()
				.map(y->fila(y))
				.collect(Collectors.joining("\n", "", ""));
		return s;
	}

	private String fila(int y) {
		Integer n = VertexPuzzle.numFilas;
		return IntStream.range(0,n).boxed()
				.map(j->datos[y][j].toString()).collect(Collectors.joining("|", "|", "|"));
	}

	@Override
	public EdgePuzzle edge(ActionPuzzle a) {
		return  EdgePuzzle.of(this, this.neighbor(a), a);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blackPosition == null) ? 0 : blackPosition.hashCode());
		result = prime * result + Arrays.deepHashCode(datos);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VertexPuzzle other = (VertexPuzzle) obj;
		if (blackPosition == null) {
			if (other.blackPosition != null)
				return false;
		} else if (!blackPosition.equals(other.blackPosition))
			return false;
		if (!Arrays.deepEquals(datos, other.datos))
			return false;
		return true;
	}

	
	


	
}
