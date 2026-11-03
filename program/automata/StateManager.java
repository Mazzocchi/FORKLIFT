package automata;

import java.util.*;
import engine.SetOfStates;


/**
 * Storage of the states of one automaton. States are the integers 0, ..., size()-1.
 * For every state it records its name, whether it is final, and its successors
 * per symbol (an array indexed by symbol identifiers).
 **/
public final class StateManager {
	private static final int[] NO_SYMBOLS = new int[0];

	private final List<String> names = new ArrayList<>();
	private final BitSet finals = new BitSet();
	private final List<SetOfStates[]> successors = new ArrayList<>();
	private final List<int[]> symbols = new ArrayList<>();	// symbols with a non-empty successor set
	private final int alphabetSize;


	public StateManager (int alphabetSize) {this.alphabetSize = alphabetSize;}


	/** @return the identifier of the new state **/
	public int addState (String name, boolean isFinal) {
		int id = names.size();
		names.add(name);
		if (isFinal) finals.set(id);
		successors.add(new SetOfStates[alphabetSize]);
		symbols.add(NO_SYMBOLS);
		return id;
	}


	public void addSuccessor (int from, int symbol, int to) {
		SetOfStates[] succ = successors.get(from);
		if (succ[symbol] == null) {
			succ[symbol] = new SetOfStates();
			int[] old = symbols.get(from);
			int[] extended = Arrays.copyOf(old, old.length + 1);
			extended[old.length] = symbol;
			symbols.set(from, extended);
		}
		succ[symbol].add(to);
	}


	public int size () {return names.size();}
	public String getName (int state) {return names.get(state);}
	public boolean isFinal (int state) {return finals.get(state);}

	/** @return the symbols labelling an outgoing transition of the state (do not modify) **/
	public int[] getSymbols (int state) {return symbols.get(state);}

	/** @return the successors of the state by the symbol, or null if there are none **/
	public SetOfStates getSuccessors (int state, int symbol) {
		SetOfStates[] succ = successors.get(state);
		return (symbol < succ.length) ? succ[symbol] : null;
	}
}
