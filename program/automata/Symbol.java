package automata;



public final class Symbol {
	private final int id;
	private final String name;


	Symbol(String name, int id) { this.name = name; this.id = id; }


	//+++++++++++++++ Getters +++++++++++++++
	public String getName() {return name;}
	// +++++++++++++++ Getters +++++++++++++++


	public String toString() {return name;}
	
	
	public int hashCode() {return 31 * id;}
	
	
	public boolean equals (Object other) {
		if (other instanceof Symbol) return id == ((Symbol) other).id;
		return false;
	}
}
