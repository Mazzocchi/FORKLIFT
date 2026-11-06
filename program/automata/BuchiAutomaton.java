package automata;

import java.nio.file.Path;
import java.util.*;
import engine.*;
import tuples.*;


/**
 * A Buchi automaton whose states are integers (see StateManager) and whose
 * symbols are integers (see SymbolManager, shared with the other automaton).
 **/
public class BuchiAutomaton {
	private final SymbolManager symbols;
	private final StateManager states;
	private final int init;
	private final SetOfStates finals = new SetOfStates();


	// +++++++++++++++ Getters +++++++++++++++
	public SymbolManager getSymbolManager() {return symbols;}
	public StateManager getStateManager() {return states;}
	public SetOfStates getFinalStates() {return finals;}
	public int getInitialState() {return init;}
	public int[] getSymbols(int state) {return states.getSymbols(state);}
	public SetOfStates getSuccessors(int state, int symbol) {return states.getSuccessors(state, symbol);}
	public boolean isFinal(int state) {return states.isFinal(state);}
	public String getName(int state) {return states.getName(state);}
	public String toString(SetOfStates set) {return set.toString(states::getName);}
	public String toString(Word word) {return symbols.toString(word);}
	// +++++++++++++++ Getters +++++++++++++++



	/** symbols are registered in (and possibly added to) the given SymbolManager **/
	public BuchiAutomaton(Path file, SymbolManager symbols) throws Exception {
		AutomatonParser parser = new AutomatonParser(file);
		this.symbols = symbols;

		// -- Symbols
		for (String name : parser.getAlphabet())
			symbols.getOrAdd(name);
		states = new StateManager(symbols.size());

		// -- States
		Map<String,Integer> states_register = new HashMap<>();
		for (String name : parser.getStates()) {
			boolean is_final = parser.getFinals().contains(name);
			int q = states.addState(name, is_final);
			if (is_final) finals.add(q);
			states_register.put(name, q);
		}
		init = states_register.get(parser.getInitial());

		// -- Edges
		for(Triplet<String,String,String> t : parser.getTransitions()) {
			int from = states_register.get(t.first());
			int symbol = symbols.getOrAdd(t.second());
			int to = states_register.get(t.third());
			states.addSuccessor(from, symbol, to);
		}
	}


	public String toString() {
		StringBuilder s = new StringBuilder("Automaton:");
		SetOfStates all = new SetOfStates();
		for (int q = 0; q < states.size(); q++) all.add(q);
		s.append("\n  States: ").append(toString(all));
		s.append("\n  Initial: ").append(getName(init));
		s.append("\n  Finals: ").append(toString(finals));
		s.append("\n  Transitions:\n");
		for (int q = 0; q < states.size(); q++) {
			for (int symbol : getSymbols(q)) {
				s.append("    ").append(getName(q));
				s.append(", (").append(symbols.getName(symbol)).append(')');
				s.append(" -> ").append(toString(getSuccessors(q, symbol)));
				s.append("\n");
			}
		}
		return s.toString();
	}


	public boolean membership (SetOfStates U, Word word_of_period) {
		int[] period = word_of_period.toArray();
		Set<Triplet<Integer, Integer, Boolean>> S = new HashSet<>();
		Set<Pair<Integer, Integer>> P = new HashSet<>();
		
		for (int start = U.nextState(0); start >= 0; start = U.nextState(start + 1)) {
			S.clear(); P.clear();
			if (reachable_final_product(start, 0, period, S, P)) return true;
		}
		return false;
	}
	
	private boolean reachable_final_product (int from, int i, int[] period, Set<Triplet<Integer, Integer, Boolean>> S, Set<Pair<Integer, Integer>> P) {
		S.add(new Triplet<>(from, i, false));
		P.add(new Pair<>(from, i));

		SetOfStates posts = getSuccessors(from, period[i]);
		if (posts != null) {
			int ii = (i+1 == period.length)?0:i+1;
			for (int to = posts.nextState(0); to >= 0; to = posts.nextState(to + 1)) {
				if (S.contains(new Triplet<>(to, ii, false)) == false) {
					if (reachable_final_product(to, ii, period, S, P)) return true;
				}
			
				if (isFinal(to)) {	
					if (P.contains(new Pair<>(to, ii))) return true;
					if (S.contains(new Triplet<>(to, ii, true)) == false) {
						if (iterable_final_product(to, ii, period, S, P)) return true;
					}
				}
			}
		}

		P.remove(new Pair<>(from, i));
		return false;
	}
	
	private boolean iterable_final_product (int from, int i, int[] period, Set<Triplet<Integer, Integer, Boolean>> S, Set<Pair<Integer, Integer>> P) {
		S.add(new Triplet<>(from, i, true));
		
		SetOfStates posts = getSuccessors(from, period[i]);
		if (posts != null) {
			int ii = (i+1 == period.length)?0:i+1;
			for (int to = posts.nextState(0); to >= 0; to = posts.nextState(to + 1)) {
				if (P.contains(new Pair<>(to, ii))) return true;
				if (S.contains(new Triplet<>(to, ii, true)) == false) { 
					if (iterable_final_product(to, ii, period, S, P)) return true;
				}
			}
		}
		return false;
	}
}
