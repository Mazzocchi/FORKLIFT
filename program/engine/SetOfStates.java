package engine;

import automata.*;

import java.util.*;
import java.util.stream.Collectors;


public class SetOfStates extends HashSet<State> implements QuasiOrder<SetOfStates> {
	public boolean smaller_than (SetOfStates set) { return set.containsAll(this); }
	
	public String toString() {
		return this.stream().map(State::toString).collect(Collectors.joining(";", "{", "}"));
	}
}

