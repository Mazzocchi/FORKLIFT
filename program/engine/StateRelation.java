package engine;

import java.util.*;
import tuples.*;


public final class StateRelation
extends HashMap<Integer, SetOfStates>
implements QuasiOrder<StateRelation>, Iterable<Pair<Integer, Integer>> {

	public void add (int initB, int stateB) {
		computeIfAbsent(initB, k -> new SetOfStates()).add(stateB);
	}
	
	public boolean contains (int initB, int stateB) {
		SetOfStates set = get(initB);
		return set != null && set.contains(stateB);
	}

	public boolean smaller_than (StateRelation rel) {
		for (Map.Entry<Integer, SetOfStates> entry : entrySet()) {
			SetOfStates other = rel.get(entry.getKey());
			if (other == null || entry.getValue().smaller_than(other) == false) return false;
		}
		return true;
	}

	public String toString() {
		StringJoiner outersj = new StringJoiner(";", "{", "}" );
		for (Pair<Integer, Integer> pair : this)
			outersj.add("(" + pair.first() + "," + pair.second() + ")");
		return outersj.toString();
	}


	public Iterator<Pair<Integer, Integer>> iterator() {
		return new StateRelationIterator(this);
	}
}
