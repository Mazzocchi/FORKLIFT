package engine;

import java.util.Set;
import automata.*;
import tuples.*;

/**
 * Fixpoint computation over the product of the automata A and B: keys are
 * states of A, values are elements of T built from states of B.
 * Transitions of A are taken from A and transitions of B are taken from B.
 **/
public abstract sealed class Post<T extends QuasiOrder<T>> permits PostI, PostF {
	protected final BuchiAutomaton A, B;
	protected final PostVariable<T> content = new PostVariable<>();
	protected PostVariable<T> updates = new PostVariable<>();

	protected Post (BuchiAutomaton A, BuchiAutomaton B) {
		this.A = A;
		this.B = B;
	}

	//+++++++++++++++ Getters +++++++++++++++
	/** number of elements stored at final states of A **/
	public int size () {return content.size(A::isFinal);}
	public Set<Pair<T, Word>> getSetOfT(int stateA) { return content.getNotNull(stateA); }
	// +++++++++++++++ Getters +++++++++++++++
	
	
	abstract protected T post(T current, int symbol);
	abstract protected boolean addExtreme(int stateA, T postB, Word word);

	
	public boolean apply () {
		PostVariable<T> buffer = new PostVariable<>();

		for (int fromA : updates.keySet()) {
			for (int symbol : A.getSymbols(fromA)) {
				SetOfStates postsA = A.getSuccessors(fromA, symbol);
				for (Pair<T, Word> pair : updates.get(fromA)) {
					T postB = post(pair.first(), symbol);
					Word word = new Word(pair.second(), symbol);
					for (int toA = postsA.nextState(0); toA >= 0; toA = postsA.nextState(toA + 1)) {
						if (addExtreme(toA, postB, word))
							buffer.add(toA, postB, word);
					}
				}
			}
		}
		
		updates = buffer;
		return (updates.isEmpty() == false); 
	}
}
