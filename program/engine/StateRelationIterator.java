package engine;

import java.util.Iterator;

import automata.State;
import tuples.Pair;

public class StateRelationIterator
implements Iterator<Pair<State, State>> {

	private final StateRelation rel;
	private State state_key = null;
	Iterator<State> iter_key;
	Iterator<State> iter_value;

	public StateRelationIterator (StateRelation rel) {
		this.rel = rel;
		iter_key = rel.keySet().iterator();
		iter_value = new EmptyIterator<>();
	}

	public boolean hasNext() {
		if (iter_value.hasNext()) return true;
		if (iter_key.hasNext()) {
			state_key = iter_key.next();
			iter_value = rel.get(state_key).iterator();
			return hasNext();
		}				
		return false;
	}

	public Pair<State, State> next() {
		if (this.hasNext()) return new Pair<>(state_key, iter_value.next());
		return null;
	}
}