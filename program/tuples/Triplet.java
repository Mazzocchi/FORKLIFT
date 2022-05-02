package tuples;


public class Triplet<A, B, C> {
	private final A fst;
	private final B snd;
	private final C trd;

	// +++++++++++++++ Getters +++++++++++++++
	public A getFirst() {return fst;}
	public B getSecond() {return snd;}
	public C getThird() {return trd;}
	// +++++++++++++++++++++++++++++++++++++++


	public Triplet(A a, B b, C c) { fst = a; snd = b; trd = c; }
	

	public String toString() { return "(" + getFirst() + "," + getSecond() + "," + getThird() + ")"; }

	
	public int hashCode() { return 31 * (31*fst.hashCode() + snd.hashCode())+ trd.hashCode(); }

	
	public boolean equals(Object other) {
		if (other instanceof Triplet<?, ?, ?>) { // false if other == null
			return (fst.equals(((Triplet<?, ?, ?>) other).fst) && snd.equals(((Triplet<?, ?, ?>) other).snd) && trd.equals(((Triplet<?, ?, ?>) other).trd));
		}
		return false;
	}
}


