package engine;

import automata.*;
import tuples.*;
import java.util.*;


public final class StateRelation
extends HashMap<State, SetOfStates>
implements QuasiOrder<StateRelation>, Iterable<Pair<State, State>> {

	public void add (State initB, State stateB) {
		Set<State> set = computeIfAbsent(initB, k -> new SetOfStates());
		set.add(stateB);	
	}
	
	public boolean addAll (State initB, Collection<State> stateB) {
		Set<State> set = computeIfAbsent(initB, k -> new SetOfStates());
		return set.addAll(stateB);
	}
	
	public boolean contains (State initB, State stateB) {
		Set<State> set = get(initB);
		if (set == null) return false;
		return set.contains(stateB);
	}

	public boolean smaller_than (StateRelation rel) {
		for (State fromB : keySet()) {
			if (rel.get(fromB) == null) return false;
			for (State state : get(fromB))
				if (rel.get(fromB).contains(state) == false) return false;
		}
		return true;
	}

	public String toString() {
		StringJoiner outersj = new StringJoiner(";", "{", "}" );
		for (Pair<State, State> pair : this) {
			StringJoiner sj = new StringJoiner(",", "(", ")" );
			sj.add(pair.getFirst().toString());
			sj.add(pair.getSecond().toString());
			outersj.add(sj.toString());
		}
		return outersj.toString();
	}


	public Iterator<Pair<State, State>> iterator() {
		return new StateRelationIterator(this);
	}
}

	
	