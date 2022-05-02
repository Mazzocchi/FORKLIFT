package engine;

import java.util.Set;
import automata.*;
import tuples.*;

public abstract class Post<T extends QuasiOrder<T>> {
	protected final PostVariable<T> content = new PostVariable<>();
	protected PostVariable<T> updates = new PostVariable<>();

	//+++++++++++++++ Getters +++++++++++++++
	public int size () {return content.size();}
	public Set<Pair<T, Word>> getSetOfT(State stateA) { return content.getNotNull(stateA); }
	// +++++++++++++++ Getters +++++++++++++++
	
	
	abstract protected T post(T current, Symbol symbol);
	abstract protected boolean addExtreme(State stateA, T postB, Word word);

	
	public boolean apply () {
		PostVariable<T> buffer = new PostVariable<>();

		for (State fromA : updates.keySet()) {
			for (Symbol symbol : fromA.getSymbols()) {
				if (fromA.getSuccessors(symbol) != null) {// FIXME: test might not be needed > IT IS NEEDED
					for (Pair<T, Word> pair : updates.get(fromA)) {
						T postB = post(pair.getFirst(), symbol);
						Word word = new Word(pair.getSecond(), symbol);
						for (State toA : fromA.getSuccessors(symbol)) {
							if (addExtreme(toA, postB, word))
								buffer.add(toA, postB, word);
						}
					}
				}
			}
		}
		
		updates = buffer;
		return (updates.isEmpty() == false); 
	}
}
