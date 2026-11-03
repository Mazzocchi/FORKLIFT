package engine;

import java.util.*;
import java.util.function.IntPredicate;

import automata.*;
import tuples.*;


class PostVariable<T extends QuasiOrder<T>> {
	private final Map<Integer, Set<Pair<T, Word>>> content = new HashMap<>();



	//+++++++++++++++ Getters +++++++++++++++
	/** number of elements stored at keys satisfying the predicate **/
	public int size (IntPredicate keep) {
		int count = 0;
		for (Map.Entry<Integer, Set<Pair<T, Word>>> entry : content.entrySet()) {
			if (keep.test(entry.getKey()))
				count += entry.getValue().size();
		}
		return count;
	}
	boolean isEmpty () {return content.isEmpty();}
	Set<Integer> keySet () {return content.keySet();}
	Set<Pair<T, Word>> get (int stateA) {return content.get(stateA);}
	Set<Pair<T, Word>> getNotNull (int stateA) { return content.getOrDefault(stateA, new HashSet<>()); }	
	//+++++++++++++++ Getters +++++++++++++++
	

	void add (int q, T set, Word w) {
		Set<Pair<T, Word>> set_set = content.computeIfAbsent(q, k -> new HashSet<>());
		set_set.add(new Pair<>(set, w));
	}
	
	
	boolean addIfMin (int q, T new_set, Word w) {
		Set<Pair<T, Word>> set_set = content.computeIfAbsent(q, k -> new HashSet<>());
		for (Iterator<Pair<T, Word>> i = set_set.iterator(); i.hasNext(); ) {
			T set = i.next().first();
			if (set.smaller_than(new_set) == true) return false;
			if (new_set.smaller_than(set) == true) i.remove();
		}
		set_set.add(new Pair<>(new_set, w));
		return true;
	}

	
	boolean addIfMax (int q, T new_set, Word w) {
		Set<Pair<T, Word>> set_set = content.computeIfAbsent(q, k -> new HashSet<>());
		for (Iterator<Pair<T, Word>> i = set_set.iterator(); i.hasNext(); ) {
			T set =  i.next().first();
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
			for (Integer stateId : keySet()) {
				s.append("AT ").append(stateId);
				StringJoiner outsj = new StringJoiner("\n  ", ":\n  ", "\n");
				for (Pair<T, Word> pair : this.get(stateId)) {
					StringJoiner sj = new StringJoiner("\n  ", "> ", "");
					sj.add(pair.second().toString());
					sj.add(pair.first().toString());
					outsj.add(sj.toString());
				}
				s.append(outsj).append("\n");
			}
			return s.toString();
		}
	}
}


