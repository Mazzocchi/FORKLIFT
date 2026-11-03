package engine;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.IntStream;


/**
 * A set of states (integer identifiers), stored as a bit vector: state q is
 * in the set iff bit (q mod 64) of words[q / 64] is set. Missing words are 0.
 * Unlike java.util.BitSet, the inclusion test needs no temporary copy.
 **/
public final class SetOfStates implements QuasiOrder<SetOfStates>, Iterable<Integer> {
	private static final long[] EMPTY = new long[0];
	private long[] words = EMPTY;


	private long word (int i) {return (i < words.length) ? words[i] : 0L;}

	private void ensureWord (int i) {
		if (i >= words.length) words = Arrays.copyOf(words, Math.max(i + 1, 2 * words.length));
	}


	public void add (int state) {
		ensureWord(state >>> 6);
		words[state >>> 6] |= 1L << state;
	}

	public void addAll (SetOfStates set) {
		int n = set.words.length;
		while (n > 0 && set.words[n - 1] == 0) n--;
		if (n == 0) return;
		ensureWord(n - 1);
		for (int i = 0; i < n; i++) words[i] |= set.words[i];
	}

	public void retainAll (SetOfStates set) {
		for (int i = 0; i < words.length; i++) words[i] &= set.word(i);
	}

	public boolean contains (int state) {
		return (word(state >>> 6) & (1L << state)) != 0;
	}

	public boolean isEmpty () {
		for (long w : words) if (w != 0) return false;
		return true;
	}

	public int size () {
		int count = 0;
		for (long w : words) count += Long.bitCount(w);
		return count;
	}

	/** @return the least state greater or equal than from, or -1; use it to iterate without boxing **/
	public int nextState (int from) {
		int i = from >>> 6;
		if (i >= words.length) return -1;
		long w = words[i] & (-1L << from);
		while (true) {
			if (w != 0) return i * 64 + Long.numberOfTrailingZeros(w);
			if (++i == words.length) return -1;
			w = words[i];
		}
	}


	/** inclusion test: this is a subset of set **/
	public boolean smaller_than (SetOfStates set) {
		for (int i = 0; i < words.length; i++)
			if ((words[i] & ~set.word(i)) != 0) return false;
		return true;
	}


	/** no nested class here: the makefile only packages the top-level classes **/
	public Iterator<Integer> iterator () {
		return IntStream.iterate(nextState(0), q -> q >= 0, q -> nextState(q + 1)).iterator();
	}


	public boolean equals (Object other) {
		if (other instanceof SetOfStates set) {
			int n = Math.max(words.length, set.words.length);
			for (int i = 0; i < n; i++)
				if (word(i) != set.word(i)) return false;
			return true;
		}
		return false;
	}

	/** trailing zero words do not contribute, consistently with equals **/
	public int hashCode () {
		long h = 1234;
		for (int i = 0; i < words.length; i++) h ^= words[i] * (i + 1);
		return (int) ((h >> 32) ^ h);
	}


	public String toString (IntFunction<String> name) {
		StringJoiner sj = new StringJoiner(";", "{", "}");
		for (int q = nextState(0); q >= 0; q = nextState(q + 1)) sj.add(name.apply(q));
		return sj.toString();
	}
	public String toString () {return toString(Integer::toString);}
}
