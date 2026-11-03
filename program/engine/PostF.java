package engine;

import automata.*;


public final class PostF extends Post<SetOfContexts> {	
	private final SetOfStates finalsB;

	/** initA is a state of A, setB a set of states of B **/
	public PostF (BuchiAutomaton A, BuchiAutomaton B, int initA, SetOfStates setB) {
		super(A, B);
		finalsB = B.getFinalStates();
		initialize(initA, setB);
	}
	
	
	protected void initialize(int initA, SetOfStates setB) {
		int n = B.getStateManager().size();
		for (int symbol : A.getSymbols(initA)) {
			SetOfContexts contexts = new SetOfContexts(n);
			for (int fromB = setB.nextState(0); fromB >= 0; fromB = setB.nextState(fromB + 1)) {
				SetOfStates succ = B.getSuccessors(fromB, symbol);
				if (succ == null) continue;
				contexts.getFirst().getOrCreate(fromB).addAll(succ);
				SetOfStates acc = contexts.getSecond().getOrCreate(fromB);
				if (B.isFinal(fromB)) acc.addAll(succ);
				else { acc.addAll(succ); acc.retainAll(finalsB); }
			}
			SetOfStates postsA = A.getSuccessors(initA, symbol);
			for (int toA = postsA.nextState(0); toA >= 0; toA = postsA.nextState(toA + 1)) { 
				updates.add(toA, contexts, new Word(symbol));
				content.add(toA, contexts, new Word(symbol));
			}
		}
	}
	
	
	/** row by row: post(first) and post(second) ∪ (post(first) ∩ finals of B) **/
	protected SetOfContexts post (SetOfContexts current, int symbol) {
		int n = current.getFirst().size();
		SetOfContexts postB = new SetOfContexts(n);
		for (int initB = 0; initB < n; initB++) {
			SetOfStates row = current.getFirst().get(initB);
			if (row == null) continue;
			SetOfStates image = image(row, symbol);
			if (image.isEmpty()) continue;
			SetOfStates acc = image(current.getSecond().get(initB), symbol);
			SetOfStates hit = new SetOfStates(); hit.addAll(image); hit.retainAll(finalsB);
			acc.addAll(hit);
			postB.getFirst().getOrCreate(initB).addAll(image);
			if (acc.isEmpty() == false) postB.getSecond().getOrCreate(initB).addAll(acc);
		}
		return postB;
	}

	private SetOfStates image (SetOfStates from, int symbol) {
		SetOfStates to = new SetOfStates();
		if (from == null) return to;
		for (int q = from.nextState(0); q >= 0; q = from.nextState(q + 1)) {
			SetOfStates succ = B.getSuccessors(q, symbol);
			if (succ != null) to.addAll(succ);
		}
		return to;
	}
	
	
	protected boolean addExtreme(int stateA, SetOfContexts postB, Word word) {
		return content.addIfMin(stateA, postB, word);
	}
}
