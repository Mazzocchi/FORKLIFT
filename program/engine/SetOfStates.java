package engine;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;


/** A set of states (integer identifiers), stored as a bit vector. **/
public final class SetOfStates implements QuasiOrder<SetOfStates>, Iterable<Integer> {
	private final BitSet bits = new BitSet();

	public void add (int state) {bits.set(state);}
	public void addAll (SetOfStates set) {bits.or(set.bits);}
	public boolean contains (int state) {return bits.get(state);}
	public boolean isEmpty () {return bits.isEmpty();}
	public int size () {return bits.cardinality();}

	/** first element greater or equal than from, or -1; use it to iterate without boxing **/
	public int nextState (int from) {return bits.nextSetBit(from);}

	/** inclusion test: this is a subset of set **/
	public boolean smaller_than (SetOfStates set) {
		BitSet diff = (BitSet) bits.clone();
		diff.andNot(set.bits);
		return diff.isEmpty();
	}

	public Iterator<Integer> iterator () {return bits.stream().iterator();}

	public boolean equals (Object other) {
		return (other instanceof SetOfStates set) && bits.equals(set.bits);
	}
	public int hashCode () {return bits.hashCode();}

	public String toString (IntFunction<String> name) {
		return bits.stream().mapToObj(name).collect(Collectors.joining(";", "{", "}"));
	}
	public String toString () {return toString(Integer::toString);}
}
