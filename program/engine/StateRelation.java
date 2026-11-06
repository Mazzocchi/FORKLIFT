package engine;

import java.util.*;


/** A relation on states of B, stored as one row (a set of states) per left element. **/
public final class StateRelation implements QuasiOrder<StateRelation> {
	private final SetOfStates[] rows;

	public StateRelation (int size) {rows = new SetOfStates[size];}

	public int size () {return rows.length;}
	/** @return the row of initB, or null if empty **/
	public SetOfStates get (int initB) {return rows[initB];}
	public SetOfStates getOrCreate (int initB) {
		if (rows[initB] == null) rows[initB] = new SetOfStates();
		return rows[initB];
	}
	public void add (int initB, int stateB) {getOrCreate(initB).add(stateB);}

	public boolean smaller_than (StateRelation rel) {
		for (int i = 0; i < rows.length; i++) {
			if (rows[i] == null || rows[i].isEmpty()) continue;
			if (rel.rows[i] == null || rows[i].smaller_than(rel.rows[i]) == false) return false;
		}
		return true;
	}

	public String toString() {
		StringJoiner sj = new StringJoiner(";", "{", "}");
		for (int i = 0; i < rows.length; i++)
			if (rows[i] != null)
				for (int j = rows[i].nextState(0); j >= 0; j = rows[i].nextState(j + 1)) sj.add("(" + i + "," + j + ")");
		return sj.toString();
	}
}
