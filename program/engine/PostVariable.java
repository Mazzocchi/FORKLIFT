package engine;

import java.util.*;

import automata.*;
import tuples.*;


class PostVariable<T extends QuasiOrder<T>> {
	private final Map<State, Set<Pair<T, Word>>> content = new HashMap<>();



	//+++++++++++++++ Getters +++++++++++++++
	public int size () {
		int count = 0;
		for (State key : content.keySet()) {
			if (key.isFinal())
				count += content.get(key).size();
		}
		return count;
	}
	boolean isEmpty () {return content.isEmpty();}
	Set<State> keySet () {return content.keySet();}
	Set<Pair<T, Word>> get (State stateA) {return content.get(stateA);}
	Set<Pair<T, Word>> getNotNull (State stateA) { return content.getOrDefault(stateA, new HashSet<>()); }	
	//+++++++++++++++ Getters +++++++++++++++
	

	void add (State q, T set, Word w) {
		Set<Pair<T, Word>> set_set = content.computeIfAbsent(q, k -> new HashSet<>());
		set_set.add(new Pair<>(set, w));
	}
	
	
	boolean addIfMin (State q, T new_set, Word w) {
		Set<Pair<T, Word>> set_set = content.computeIfAbsent(q, k -> new HashSet<>());
		for (Iterator<Pair<T, Word>> i = set_set.iterator(); i.hasNext(); ) {
			T set = i.next().getFirst();
			if (set.smaller_than(new_set) == true) return false;
			if (new_set.smaller_than(set) == true) i.remove();
		}
		set_set.add(new Pair<>(new_set, w));
		return true;
	}

	
	boolean addIfMax (State q, T new_set, Word w) {
		Set<Pair<T, Word>> set_set = content.computeIfAbsent(q, k -> new HashSet<>());
		for (Iterator<Pair<T, Word>> i = set_set.iterator(); i.hasNext(); ) {
			T set =  i.next().getFirst();
			if (new_set.smaller_than(set) == true) return false;
			if (set.smaller_than(new_set) == true) i.remove();
		}
		set_set.add(new Pair<>(new_set, w));
		return true;
	}


	public String toString() {
		if (this.isEmpty())
			return "$$ empty post variable $$";
		else {
			StringBuilder s = new StringBuilder();
			for (State state : keySet()) {
				s.append("AT ").append(state.toString());
				StringJoiner outsj = new StringJoiner("\n  ", ":\n  ", "\n");
				for (Pair<T, Word> pair : this.get(state)) {
					StringJoiner sj = new StringJoiner("\n  ", "> ", "");
					sj.add(pair.getSecond().toString());
					sj.add(pair.getFirst().toString());
					outsj.add(sj.toString());
				}
				s.append(outsj).append("\n");
			}
			return s.toString();
		}
	}
}


