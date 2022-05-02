package engine;


import automata.*;


public final class PostI extends Post<SetOfStates> {
	private final boolean reversed_inclusion;
	
	public PostI (State initA, State initB, boolean rev) {
		initialize(initA, initB);
		reversed_inclusion = rev;
	}

	
	protected void initialize(State initA, State initB) {
		SetOfStates set_qI = new SetOfStates();
		set_qI.add(initB);
		updates.add(initA, set_qI, new Word());
		content.add(initA, set_qI, new Word());
	}
	
	
	protected SetOfStates post (SetOfStates current, Symbol symbol) {
		SetOfStates postB = new SetOfStates();
		for (State fromB : current) {
			if (fromB.getSuccessors(symbol) != null)
				postB.addAll(fromB.getSuccessors(symbol));
		}
		return postB;
	}
	
	
	protected boolean addExtreme(State stateA, SetOfStates postB, Word word) {
		if (reversed_inclusion) return content.addIfMax(stateA, postB, word);
		return content.addIfMin(stateA, postB, word);
	}
}


