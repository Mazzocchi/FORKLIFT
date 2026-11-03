package engine;

import automata.*;
import tuples.*;


public final class PostF extends Post<SetOfContexts> {	
	/** initA is a state of A, setB a set of states of B **/
	public PostF (BuchiAutomaton A, BuchiAutomaton B, int initA, SetOfStates setB) {
		super(A, B);
		initialize(initA, setB);
	}
	
	
	protected void initialize(int initA, SetOfStates setB) {
		for (int symbol : A.getSymbols(initA)) {
			SetOfContexts contexts = new SetOfContexts();
			for (int fromB = setB.nextState(0); fromB >= 0; fromB = setB.nextState(fromB + 1)) {
				SetOfStates succ = B.getSuccessors(fromB, symbol);
				if (succ != null) {
					for (int toB = succ.nextState(0); toB >= 0; toB = succ.nextState(toB + 1))
						contexts.add(fromB, toB, B.isFinal(fromB) || B.isFinal(toB));
				}
			}
			SetOfStates postsA = A.getSuccessors(initA, symbol);
			for (int toA = postsA.nextState(0); toA >= 0; toA = postsA.nextState(toA + 1)) { 
				updates.add(toA, contexts, new Word(symbol));
				content.add(toA, contexts, new Word(symbol));
			}
		}
	}
	
	
	protected SetOfContexts post (SetOfContexts current, int symbol) {
		SetOfContexts postB = new SetOfContexts();
		for (Pair<Pair<Integer, Integer>, Boolean> pair : current) {
			int initB = pair.first().first();
			int fromB = pair.first().second();
			boolean accept = pair.second();
			SetOfStates succ = B.getSuccessors(fromB, symbol);
			if (succ != null) {
				for (int toB = succ.nextState(0); toB >= 0; toB = succ.nextState(toB + 1))
					postB.add(initB, toB, accept || B.isFinal(toB));
			}
		}
		return postB;
	}
	
	
	protected boolean addExtreme(int stateA, SetOfContexts postB, Word word) {
		return content.addIfMin(stateA, postB, word);
	}
}
