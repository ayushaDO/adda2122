package us.lsi.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import us.lsi.flujosparalelos.Spliterators2;
import us.lsi.math.Math2;



/**
 * <p> Esta clase proporciona m�todos de extensi�n del tipo <code> Stream </code> proporcionado por el API de Java. 
 * </p>
 * 
 * 
 * * @author Miguel Toro
 */

public class Streams2 {

	
	/**
	 * @param a Un entero
	 * @param b Un entero
	 * @param c Un entero
	 * @return Un stream de enteros que forman una progresi�n 
	 * aritm�tica desde a hasta b con raz�n c sin incluir el valor b.
	 */
	public static IntStream range(Integer a, Integer b, Integer c){
		Preconditions.checkArgument(a==b ||(b-a)*c > 0);
		Integer hasta = (b-a)/c;
		if((b-a)%c == 0){
			hasta = hasta - 1;
		} 
		return IntStream.rangeClosed(0, hasta).map(x->a+c*x);
	}
	/**
	 * @param a Un entero
	 * @param b Un entero
	 * @param c Un entero
	 * @return Un stream de enteros que forman una progresi�n 
	 * aritm�tica desde a hasta b con raz�n c incluyendo el valor b se es posible
	 */
	public static IntStream rangeClosed(Integer a, Integer b, Integer c){		
		Preconditions.checkArgument(a==b || (b-a)*c > 0);
		Integer hasta = (b-a)/c;
		return IntStream.rangeClosed(0, hasta).map(x->a+c*x);
	}
	/**
	 * @param a Un entero
	 * @param b Un entero
	 * @param c Un entero
	 * @return Un stream de enteros que forman una progresi�n 
	 * aritm�tica desde a hasta b con raz�n c sin incluir el valor b.
	 */
	public static LongStream range(Long a, Long b, Long c){
		Preconditions.checkArgument((b-a)*c > 0);
		Long hasta = (b-a)/c;
		if((b-a)%c == 0){
			hasta = hasta - 1;
		} 
		return LongStream.rangeClosed(0, hasta).map(x->a+c*x);
	}
	/**
	 * @param a Un entero
	 * @param b Un entero
	 * @param c Un entero
	 * @return Un stream de enteros que forman una progresi�n 
	 * aritm�tica desde a hasta b con raz�n c incluyendo el valor b se es posible
	 */
	public static LongStream rangeClosed(Long a, Long b, Long c){
		Preconditions.checkArgument((b-a)*c > 0);
		Long hasta = (b-a)/c;
		return LongStream.rangeClosed(0, hasta).map(x->a+c*x);
	}
	
	/**
	 * @param <T> Tipo de los elementos del stream
	 * @param st Un stream
	 * @return Un elemento del stream escogido aleatoriamente
	 */
	public static <T> T elementRandom(Stream<T> st){
		List<T> ls = st.collect(Collectors.toList());
		Integer n = ls.size();
		return ls.get(Math2.getEnteroAleatorio(0, n));
	}
	
	public static <T> List<T> toList(Stream<T> s){
		return s.collect(Collectors.toList());
	}
	
	public static <T> Set<T> toSet(Stream<T> s){
		return s.collect(Collectors.toSet());
	}
	
	public static <T> SortedSet<T> toSortedSet(Stream<T> s, Comparator<T> cmp){
		return s.collect(Collectors.toCollection(()-> new TreeSet<>(cmp)));
	}
	
	public static <T> Multiset<T> toMultiSet(Stream<T> s){
		Multiset<T> m = Multiset.empty();
		s.forEach(x->m.add(x));
		return m;
	}
	
	/**
	 * @param <T> Tipo de los elementos del primer stream
	 * @param <U> Tipo de los elementos del segundo stream
	 * @param <R> Tipo de los elementos del stream resultado
	 * @param s1 Un Stream
	 * @param s2 Un Segundo Stream
	 * @param f Una Bifunction que opera un elemento del primer stream con un del segundo para 
	 * dar un resultado
	 * @return El resultado de operar los pares posibles de s1 y s2 con la bifunci�n f
	 */
	public static <T, U, R> Stream<R> cartesianProduct(Stream<T> s1, Stream<U> s2, BiFunction<T, U, R> f) {
		List<U> ls = s2.collect(Collectors.toList());
		return s1.flatMap(x->ls.stream().map(y->f.apply(x,y)));
	}

	/**
	 * @param <T> Tipo de los elementos del primer stream
	 * @param <U> Tipo de los elementos del segundo stream
	 * @param <R> Tipo de los elementos del stream resultado
	 * @param s1 Una colecci�n
	 * @param s2 Una segunda colecci�n
	 * @param f Una Bifunction que opera un elemento del primer stream con un del segundo para 
	 * dar un resultado
	 * @return El resultado de operar los pares posibles de s1 y s2 con la bifunci�n f
	 */
	public static <T, U, R> Stream<R> cartesianProduct(Collection<T> s1, Collection<U> s2, BiFunction<T, U, R> f) {
		return s1.stream().flatMap(x->s2.stream().map(y->f.apply(x,y)));
	}

	public static <T> Stream<Tuple2<T,T>> cartesianProduct(Collection<T> s1) {
		return cartesianProduct(s1,s1,(x,y)->Tuple.create(x, y));
	}
	
	/**
	 * @param s Una stream
	 * @param file Un fichero donde guardar los elementos de la stream
	 */
	public static void toFile(Stream<String> s, String file) {
		try {
			final PrintWriter f = new PrintWriter(new BufferedWriter(
					new FileWriter(file)));
			s.forEach(x -> {
				f.println(x);
			});
			f.close();
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"No se ha podido crear el fichero " + file);
		}
	}
	
	/**
	 * @param file Un fichero
	 * @return Un stream formado por las l�neas del fichero
	 * @exception IllegalArgumentException si no se encucntra el fichero
	 */
	
	public static Stream<String> fromFile(String file) {
		Stream<String> r = null;
		try {
			r = Files.lines(Paths.get(file), Charset.defaultCharset());
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"No se ha encontrado el fichero " + file);
		}
		return r;
	}
	/**
	 * @return Un stream formado por las l�neas escritas en la consola
	 */
	public static Stream<String> fromConsole() {
		BufferedReader f = new BufferedReader(new InputStreamReader(System.in));
		return f.lines();
	}

	/**
	 * @param s Un String
	 * @param delim Unos delimitadores
	 * @return Un stream formado por los elementos resultantes de separar el String original
	 * por los delimitadores
	 */
	public static Stream<String> fromString(String s, String delim) {
		String[] r = s.split(delim);
		return Arrays.stream(r).<String> map(x -> x.trim())
				.filter(x-> x.length() > 0);
	}
	
	/**
	 * @param sm Un String
	 * @param <E> El tipo de los elementos de la secuencia
	 * @return Un stream formado por los pares de elementos formados por un elemento y el siguiente en sm
	 */
	public static <E> Stream<Tuple2<E,E>> consecutivePairs(Stream<E> sm) {
		MutableType<E> rf = MutableType.of(null);
		Stream<Tuple2<E,E>> r = sm
					.map(e->Tuple.create(rf.newValue(e), e))	
					.filter(p-> p.v1!=null);
		return r;
	}
	
		
	/**
	 * @param n
	 * @param m
	 * @return Un stream con todos los pares de enteros (i,j) con i en 0 a n-1 y j en 0 a m-1
	 */
	public static Stream<IntPair> allPairs(Integer n, Integer m) {
		return IntStream.range(0, n).boxed()
				.flatMap(i -> IntStream.range(0, m).boxed().map(j -> IntPair.of(i, j)));
	}
	
	public static Stream<IntPair> allPairs(Integer n1, Integer n2, Integer m1, Integer m2){
		return IntStream.range(n1, n2).boxed()
				.flatMap(x->IntStream.range(m1, m2).boxed().map(y->IntPair.of(x,y)));
	}
	
	/**
	 * @param s Un stream 
	 * @param s1 Una secuencia adicional de stream
	 * @param <T> El tipo de los elementos de la secuencia
	 * @return Un stream fromado con los par�metros concatenados
	 */
	@SafeVarargs
	public static <T> Stream<T> concat(Stream<T> s, Stream<T>... s1) {
		return Stream.<T>concat(s, Arrays.stream(s1).flatMap(Function.<Stream<T>>identity()));
	}
	
	
	
	/**
	 * @param first Un stream 
	 * @param second Una secuencia adicional de stream
	 * @param <A> El tipo de los elementos de la secuencia
	 * @param <B> El tipo de los elementos de la secuencia
	 * @param <C> El tipo de los elementos de la secuencia resultante
	 * @param zipper Fuci�n de combinaci�n
	 * @return Un stream formado por los elementos obtenidos combinando 
	 * uno a uno los elementos de las secuencias de entrada
	 */	
	public static <L, R, T> Stream<T> zip(Stream<L> leftStream, Stream<R> rightStream, BiFunction<L, R, T> combiner) {
		Spliterator<L> lefts = leftStream.spliterator();
		Spliterator<R> rights = rightStream.spliterator();
		return StreamSupport.stream(Spliterators2.zip(lefts, rights, combiner), leftStream.isParallel() || rightStream.isParallel());
	}
	
	/**
	 * @param sm Un String
	 * @param start Primer indice de la enumrracion
	 * @param <E> El tipo de los elementos de la secuencia
	 * @return Un stream formado por los pares de elementos 
	 * formados por un elemento y el entero que indica su posici�n
	 */


	public static <E> Stream<Enumerate<E>> enumerate(Stream<E> stream, Integer start) {
		Stream<Integer> st = Stream.iterate(start, e -> e + 1);
		return zip(st, stream, (n, e) -> Enumerate.of(n, e));
	}
	
	/**
	 * @param sm Un String
	 * @param <E> El tipo de los elementos de la secuencia
	 * @return Un stream formado por los pares de elementos 
	 * formados por un elemento y el entero que indica su posici�n
	 */

	public static <E> Stream<Enumerate<E>> enumerate(Stream<E> stream) {
		return enumerate(stream, 0);
	}
	
	

	/**
	 * @param stream1 Un stream
	 * @param stream2 Un segundo stream
	 * @param f1 Una funci�n que calcula una clave para los elementos de stream1
	 * @param f2 Una funci�n que calcula una clave para los elementos de stream2
	 * @param fr Una funci�n que calcula un nuevo valor a partir  de uno procedente de stream1 y otro del stream2
	 * @param <T> El tipo de los elementos de la primera secuencia
	 * @param <U> El tipo de los elementos de la segunda secuencia
	 * @param <K> El tipo de los elementos de la clave
	 * @param <R> El tipo de los elementos de la secuencia resultante
	 * @return Un stream resultado del joint de stream1 y stream2
	 */
	public static <T, U, K, R> Stream<R> joint(
			Stream<T> stream1,
			Stream<U> stream2,
			Function<? super T, ? extends K> f1,
			Function<? super U, ? extends K> f2, 
			BiFunction<T, U, R> fr) {

		final Map<K, List<T>> map1 = stream1.collect(Collectors.groupingBy(f1));
		final Map<K, List<U>> map2 = stream2.collect(Collectors.groupingBy(f2));

		Set<K> sk = new HashSet<K>(map1.keySet());
		sk.retainAll(map2.keySet());
		Stream<R> r = sk.stream()
				.<R>flatMap(x -> Streams2.cartesianProduct(map1.get(x), map2.get(x), fr));
		return r;
	}
	
	
	
	
	
	public static void main(String[] args) {
		var s1 = Stream.of(1,2,3,4,5,6,7);
		var s2 = Stream.of(11,12,13,14,15,16);
		var s = "Antonio sa-lio al patio";
		Streams2.zip(s1, s2,(x,y)->Tuple.create(x, y));
		s1 = Stream.of(1,2,3,4,5,6,7);
		Streams2.consecutivePairs(s1).forEach(x->System.out.print(x));
		System.out.println("\n_______");
		Streams2.enumerate(Arrays.asList(s.split("[ -]")).stream()).forEach(x->System.out.println(x));
		System.out.println("\n_______");
		Streams2.fromFile("ficheros/acciones.txt").forEach(System.out::println);
		var n = 14L;			   
		var s10 =  Stream.iterate(n,x->x>0,x->x/2);
		System.out.println(s10);
	}
}
