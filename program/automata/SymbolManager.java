package automata;

import java.util.*;


/**
 * Bijection between symbol names and integer identifiers 0, 1, 2, ...
 * A single instance is shared by the two automata of an inclusion query,
 * so that equal names receive equal identifiers.
 **/
public final class SymbolManager {
	private final Map<String, Integer> ids = new HashMap<>();
	private final List<String> names = new ArrayList<>();


	/** @return the identifier of the symbol, registering it if it is new **/
	public int getOrAdd (String name) {
		Integer id = ids.get(name);
		if (id != null) return id;
		ids.put(name, names.size());
		names.add(name);
		return names.size() - 1;
	}

	public String getName (int symbol) {return names.get(symbol);}
	public int size () {return names.size();}


	public String toString (Word word) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < word.getLength(); i++)
			s.append('(').append(getName(word.getIndex(i))).append(')');
		return s.toString();
	}
}
