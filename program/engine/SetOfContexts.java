package engine;

import tuples.*;
import java.util.*;

public class SetOfContexts
implements QuasiOrder<SetOfContexts>, Iterable<Pair<Pair<Integer, Integer>, Boolean>>{
	
	private final StateRelation first;
	private final StateRelation second;

	public SetOfContexts() {
		this.first = new StateRelation();
		this.second = new StateRelation();
	}
	
	public SetOfContexts(StateRelation first, StateRelation second) {
		this.first = first;
		this.second = second;
	}
	
	public StateRelation getFirst() { return first; }
	public StateRelation getSecond() { return second; }
	
	public int size() {
		return this.first.size() + this.second.size();
	}
	
	public void add (int initB, int stateB, boolean accept) {
		this.first.add(initB, stateB);
		if (accept) this.second.add(initB, stateB);
	}	
	
	public boolean contains (int initB, int stateB, boolean accept) {
		if (accept) return this.second.contains(initB, stateB);
		return this.first.contains(initB, stateB);
	}

	public boolean smaller_than (SetOfContexts cxt) {
		return first.smaller_than(cxt.first) && second.smaller_than(cxt.second);
	}
	
	public String toString() {
		StringJoiner joiner = new StringJoiner("... ", "<", ">" );
		joiner.add(this.first.toString());
		joiner.add(this.second.toString());
		return joiner.toString();
	}
	
	
	public Iterator<Pair<Pair<Integer, Integer>, Boolean>> iterator() {
		return new ContextIterator(this);
	}
}
