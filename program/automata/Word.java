package automata;


public class Word {
	private final Symbol[] array;
	private final int length;
	
	public Word () {
		length = 0;
		array = null;
	}
	
	public Word (Symbol symbol) {
		length = 1;
		array = new Symbol[1];
		array[0] = symbol;
	}
	
	public Word (Word word, Symbol symbol) {
		length = word.length + 1;
		array = new Symbol[length];
		for (int i = 0; i < word.length; i++)
			array[i] = word.array[i];
		array[word.length] = symbol;
	}
	
	public int getLength () {
		return length;
	}
	
	public Symbol getIndex (int i) {
		return array[i];
	}
 	

	public String toString() {
		String string = "";
		for (int i = 0; i < length; i++)
			string = string + array[i].toString();
		return string;
	}
}
