package automata;


/**
 * A finite word of symbol identifiers (see SymbolManager), stored as a linked
 * list from its last symbol back to the empty word. Extending a word by one
 * symbol takes constant time and memory, and words share their common prefixes.
 * Equality is identity, as for the former array-based representation.
 **/
public final class Word {
	private final Word prefix;		// null for the empty word
	private final int last;			// last symbol (unused for the empty word)
	private final int length;


	/** the empty word **/
	public Word () {
		this.prefix = null;
		this.last = -1;
		this.length = 0;
	}

	/** the word of length one **/
	public Word (int symbol) {
		this(new Word(), symbol);
	}

	/** the word followed by the symbol **/
	public Word (Word word, int symbol) {
		this.prefix = word;
		this.last = symbol;
		this.length = word.length + 1;
	}


	public int getLength () {
		return length;
	}

	/** @return the symbols of the word, in order **/
	public int[] toArray () {
		int[] symbols = new int[length];
		Word w = this;
		for (int i = length - 1; i >= 0; i--) {
			symbols[i] = w.last;
			w = w.prefix;
		}
		return symbols;
	}


	/** Without a SymbolManager only identifiers can be printed; see SymbolManager.toString(Word). **/
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int symbol : toArray())
			s.append('(').append(symbol).append(')');
		return s.toString();
	}
}
