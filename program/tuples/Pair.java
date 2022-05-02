package tuples;

public class Pair<A, B> {
	private final A fst;
	private final B snd;

	// +++++++++++++++ Getters +++++++++++++++
	public A getFirst() {return fst;}
	public B getSecond() {return snd;}
	// +++++++++++++++++++++++++++++++++++++++

	
	public Pair(A a, B b) { fst = a; snd = b; }

	
	public String toString() { return "(" + getFirst() + "," + getSecond() + ")"; }
	
	
	public int hashCode() { return 31 * fst.hashCode() + snd.hashCode(); }

	
	public boolean equals(Object other) {
		if (other instanceof Pair<?, ?>) // false if other == null
			return fst.equals(((Pair<?, ?>) other).fst) && snd.equals(((Pair<?, ?>) other).snd);
		return false;
	}
}

