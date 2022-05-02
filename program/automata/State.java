package automata;


import java.util.*;



import engine.*;
import tuples.Pair;


public class State {
	private final int id;
	private final String name;
	private final boolean is_final;
	private Map<Symbol, SetOfStates> succ = new HashMap<>();


	// +++++++++++++++ Getters +++++++++++++++
	public boolean isFinal() {return is_final;}
	public String getName() {return name;}
	public int getId() {return id;}
	public Set<Symbol> getSymbols () {return succ.keySet();}
	public SetOfStates getSuccessors (Symbol symbol) { return succ.get(symbol); }
	public HashSet<Pair<State, Boolean>> getSuccessorsFNotNull (Word word)  {
		HashSet<Pair<State, Boolean>> set = new HashSet<>();
		HashSet<Pair<State, Boolean>> set_next = new HashSet<>();
		HashSet<Pair<State, Boolean>> set_tmp;
		set.add(new Pair<>(this, this.isFinal()));
		for (int index = 0; index < word.getLength(); index++) {			
			for (Pair<State, Boolean> pair : set) {
				SetOfStates posts = pair.getFirst().getSuccessors(word.getIndex(index));
				if (posts != null) {
					for (State state : posts)
						set_next.add(new Pair<>(state, pair.getSecond() || state.isFinal()));
				}
			}
			set_tmp = set;
			set = set_next;
			set_next = set_tmp;
		}
		return set;
	}
	public SetOfStates getSuccessorsNotNull (Word word)  {
		SetOfStates set = new SetOfStates();
		SetOfStates set_next = new SetOfStates();
		SetOfStates set_tmp;
		set.add(this);
		for (int index = 0; index < word.getLength(); index++) {			
			for (State from : set) {
				SetOfStates posts = from.getSuccessors(word.getIndex(index));
				if (posts != null) set_next.addAll(posts);
			}
			set_tmp = set;
			set = set_next;
			set_next = set_tmp;
		}
		return set;
	}
	// +++++++++++++++ Getters +++++++++++++++


	State (String name, int id, boolean acceptance, Set<Symbol> alphabet) {
		this.name = name;
		this.id = id;
		is_final = acceptance;
	}


	void addSuccessor(State q, Symbol a) {
		SetOfStates set = succ.get(a);

		if (set == null) {
			SetOfStates singleton = new SetOfStates();
			singleton.add(q);
			succ.put(a,singleton);
		}
		else set.add(q);
	}

	
	public boolean equals (State other) {
		if (other instanceof State) return id == ((State) other).id;
		return false;
	}
	

	public String toString() {return name;}
	public int hashCode() {return 31 * id;} //FIXME
}
