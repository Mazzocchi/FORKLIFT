package engine;

import java.util.*;

/** Pairs (initB, stateB) reachable on a word, and those reachable through a final state. **/
public class SetOfContexts implements QuasiOrder<SetOfContexts> {
	private final StateRelation first;
	private final StateRelation second;	// subset of first

	public SetOfContexts(int size) {
		this.first = new StateRelation(size);
		this.second = new StateRelation(size);
	}
	
	public StateRelation getFirst() { return first; }
	public StateRelation getSecond() { return second; }
	
	public void add (int initB, int stateB, boolean accept) {
		this.first.add(initB, stateB);
		if (accept) this.second.add(initB, stateB);
	}	

	public boolean smaller_than (SetOfContexts cxt) {
		return first.smaller_than(cxt.first) && second.smaller_than(cxt.second);
	}
	
	public String toString() {
		return "<" + first + "... " + second + ">";
	}
}
