package us.lsi.alg.mochila;

import us.lsi.graphs.virtual.ActionSimpleEdge;
import us.lsi.mochila.datos.DatosMochila;

public class MochilaEdge extends ActionSimpleEdge<MochilaVertex,Integer> {
	
	public static MochilaEdge of(MochilaVertex v1, MochilaVertex v2, Integer a) {		
		return new MochilaEdge(v1, v2, a);
	}

	public Integer a;
	
	private MochilaEdge(MochilaVertex v1, MochilaVertex v2, Integer a) {
		super(v1, v2);
		this.a = a;
		super.weight = a*DatosMochila.getValor(v1.index).doubleValue();
//		System.out.println(this);
	}

	@Override
	public String toString() {
		return String.format("(%d,%d,%d,%.2f)",this.source.index,this.target.index,a,this.getEdgeWeight());
	}
	
}

