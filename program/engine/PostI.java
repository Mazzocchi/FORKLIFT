package engine;


import automata.*;


public final class PostI extends Post<SetOfStates> {
	private final boolean reversed_inclusion;
	
	public PostI (BuchiAutomaton A, BuchiAutomaton B, boolean rev) {
		super(A, B);
		initialize(A.getInitialState(), B.getInitialState());
		reversed_inclusion = rev;
	}

	
	protected void initialize(int initA, int initB) {
		SetOfStates set_qI = new SetOfStates();
		set_qI.add(initB);
		updates.add(initA, set_qI, new Word());
		content.add(initA, set_qI, new Word());
	}
	
	
	protected SetOfStates post (SetOfStates current, int symbol) {
		SetOfStates postB = new SetOfStates();
		for (int fromB = current.nextState(0); fromB >= 0; fromB = current.nextState(fromB + 1)) {
			SetOfStates succ = B.getSuccessors(fromB, symbol);
			if (succ != null) postB.addAll(succ);
		}
		return postB;
	}
	
	
	protected boolean addExtreme(int stateA, SetOfStates postB, Word word) {
		if (reversed_inclusion) return content.addIfMax(stateA, postB, word);
		return content.addIfMin(stateA, postB, word);
	}
}
