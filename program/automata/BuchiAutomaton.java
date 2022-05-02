package automata;

import java.nio.file.Path;
import java.util.*;
import engine.*;
import tuples.*;


public class BuchiAutomaton {
	private final Set<Symbol> alphabet = new HashSet<>();
	private final SetOfStates states = new SetOfStates();
	private final State init;
	private final SetOfStates finals = new SetOfStates();
	// -- Transitions are stored through states


	// +++++++++++++++ Getters +++++++++++++++
	public Set<Symbol> getAlphabet() {return alphabet;}
	public Set<State> getFinalStates() {return finals;}
	public Set<State> getStates() {return states;}
	public State getInitialState() {return init;}
	// +++++++++++++++ Getters +++++++++++++++



	public BuchiAutomaton(Path file, final Set<Symbol> stating_alphabet) throws Exception {
		AutomatonParser parser = new AutomatonParser(file);

		// -- Symbols
		Map<String,Symbol> symbols_register = new HashMap<>();
		for (Symbol a : stating_alphabet)
			symbols_register.put(a.getName(), a);


		int symbol_id_maker = stating_alphabet.size();
		alphabet.addAll(stating_alphabet);

		for (String name : parser.getAlphabet()) {
			if (symbols_register.containsKey(name) == false) {
				Symbol a = new Symbol(name, symbol_id_maker++);
				alphabet.add(a);
				symbols_register.put(a.getName(), a);
			}
		}

		// -- States
		int state_id_maker = 0;
		Map<String,State> states_register = new HashMap<>();
		for (String name : parser.getStates()) {
			State q;
			if (parser.getFinals().contains(name)) {
				q = new State(name, state_id_maker++, true, alphabet);
				finals.add(q);
			}
			else
				q = new State(name, state_id_maker++, false, alphabet);
			states_register.put(name, q);
			states.add(q);
		}
		init = states_register.get(parser.getInitial());

		// -- Edges
		for(Triplet<String,String,String> t : parser.getTransitions()) {
			State from = states_register.get(t.getFirst());
			Symbol symbol = symbols_register.get(t.getSecond());
			State to = states_register.get(t.getThird());
			from.addSuccessor(to, symbol);
		}
	}


	public String toString() {
		StringBuilder s = new StringBuilder("Automaton:");
		s.append("\n  States: ").append(states);
		s.append("\n  Initial: ").append(init.toString());
		s.append("\n  Finals: ").append(finals);
		s.append("\n  Transitions:\n");
		for (State state : states) {
			for (Symbol symbol : alphabet) {
				if (state.getSuccessors(symbol) != null) {
					s.append("    ").append(state);
					s.append(", ").append(symbol.toString());
					s.append(" -> ").append(state.getSuccessors(symbol).toString());
					s.append("\n");
				}
			}
		}
		return s.toString();
	}


	public boolean membership (SetOfStates U, Word period) {
		Set<Triplet<State, Integer, Boolean>> S = new HashSet<>();
		SetOfStates P = new SetOfStates();
		
		for (State start : U) {
			S.clear(); P.clear();
			if (reachable_final_product(start, 0, period, S, P)) return true;
		}
		return false;
	}
	
	private boolean reachable_final_product (State from, int i, Word period, Set<Triplet<State, Integer, Boolean>> S, SetOfStates P) {
		S.add(new Triplet<State, Integer, Boolean>(from, i, false));
		P.add(from);
		
		SetOfStates posts = from.getSuccessors(period.getIndex(i));
		if (posts != null) {
			for (State to : posts) {
				int ii = (i+1 == period.getLength())?0:i+1;
				if (S.contains(new Triplet<>(to, ii, false)) == false) {
					if (reachable_final_product(to, ii, period, S, P)) return true;
				}
			}
		}
		
		if (from.isFinal()) {
			if (iterable_final_product(from, i, period, S, P)) return true;
		}
		P.remove(from);
		
		return false;
	}
	
	private boolean iterable_final_product (State from, int i, Word period,Set<Triplet<State, Integer, Boolean>> S, SetOfStates P) {
		S.add(new Triplet<State, Integer, Boolean>(from, i, true));
		
		SetOfStates posts = from.getSuccessors(period.getIndex(i));
		if (posts != null) {
			for (State to : posts) {
				int ii = (i+1 == period.getLength())?0:i+1;
				if (P.contains(to)) return true;
				if (S.contains(new Triplet<>(to, ii, true)) == false) { 
					if (iterable_final_product(to, ii, period, S, P)) return true;
				}
			}
		}
		return false;
	}

	
}




