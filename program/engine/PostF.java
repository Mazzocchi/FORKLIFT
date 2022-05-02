package engine;

import java.util.Set;
import automata.*;
import tuples.*;


public final class PostF extends Post<SetOfContexts> {	
	public PostF (State initA, Set<State> initB) { initialize(initA, initB); }
	
	
	protected void initialize(State initA, Set<State> setB) {
		for (Symbol symbol : initA.getSymbols()) {
			if (initA.getSuccessors(symbol) != null) {
				SetOfContexts contexts = new SetOfContexts();
				for (State fromB : setB) {
					if (fromB.getSuccessors(symbol) != null) {
						for (State toB : fromB.getSuccessors(symbol))
							contexts.add(fromB, toB, fromB.isFinal() || toB.isFinal());
					}
				}
				for (State toA : initA.getSuccessors(symbol)) { 
					updates.add(toA, contexts, new Word(symbol));
					content.add(toA, contexts, new Word(symbol));
				}
			}
		}
	}
	
	
	protected SetOfContexts post (SetOfContexts current, Symbol symbol) {
		SetOfContexts postB = new SetOfContexts();
		for (Pair<Pair<State, State>, Boolean> pair : current) {
			State initB = pair.getFirst().getFirst();
			State fromB = pair.getFirst().getSecond();
			Boolean accept = pair.getSecond();
			if (fromB.getSuccessors(symbol) != null) {
				for (State toB : fromB.getSuccessors(symbol))
					postB.add(initB, toB, accept || toB.isFinal());
			}
		}
		return postB;
	}
	
	
	protected boolean addExtreme(State stateA, SetOfContexts postB, Word word) {
		return content.addIfMin(stateA, postB, word);
	}
	
}

