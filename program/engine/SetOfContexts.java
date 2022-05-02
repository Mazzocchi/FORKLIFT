package engine;

import automata.*;
import tuples.*;
import java.util.*;

public class SetOfContexts
extends Pair<StateRelation, StateRelation>
implements QuasiOrder<SetOfContexts>, Iterable<Pair<Pair<State, State>, Boolean>>{
	
	public SetOfContexts() {
		super(new StateRelation(), new StateRelation());
	}
	
	public SetOfContexts(StateRelation fst, StateRelation snd) {
		super(fst, snd);
	}
	
	public int size() {
		return this.getFirst().size() + this.getSecond().size();
	}
	
	public void add (State initB, State stateB, Boolean accept) {
		this.getFirst().add(initB, stateB);
		if (accept) this.getSecond().add(initB, stateB);
	}	
	
	public boolean contains (State initB, State stateB, Boolean accept) {
		if (accept) return this.getSecond().contains(initB, stateB);
		return this.getFirst().contains(initB, stateB);
	}

	public boolean smaller_than (SetOfContexts cxt) {
		return getFirst().smaller_than(cxt.getFirst()) && getSecond().smaller_than(cxt.getSecond());
	}
	
	public String toString() {
		StringJoiner joiner = new StringJoiner("... ", "<", ">" );
		joiner.add(this.getFirst().toString());
		joiner.add(this.getSecond().toString());
		return joiner.toString();
	}

	
	public Iterator<Pair<Pair<State, State>, Boolean>> iterator() {
		return new ContextIterator(this);
	}
}