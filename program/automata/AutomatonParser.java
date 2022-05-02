package automata;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import tuples.Triplet;


/** Only used in BuchiAutomaton **/
final class AutomatonParser {
	private final Path file;				// given as parameter
	private final BufferedReader source;	// automaton source code, created from path
	private int line_counter = 0;
	
	private String initial = null;
	private final Set<String> alphabet = new HashSet<>();
	private final Set<String> states = new HashSet<>();
	private final Set<String> finals = new HashSet<>();
	private final Set<Triplet<String, String, String>> transitions = new HashSet<>();
	
	
	// +++++++++++++++ Getters +++++++++++++++
	String getInitial() {return initial;}
	Set<String> getStates() {return states;}
	Set<String> getAlphabet() {return alphabet;}
	Set<Triplet<String, String, String>> getTransitions() {return transitions;}
	Set<String> getFinals() {return finals;}
	// +++++++++++++++++++++++++++++++++++++++

	
	
	AutomatonParser (Path file) throws Exception {
		this.file = file;
		
		if (Files.exists(file) == false) throw new Exception("The file \"" + file + "\" does not exists");
		if (Files.isReadable(file) == false) throw new Exception("The file \"" + file + "\" cannot be opened with read access");
		
		try {
			source = Files.newBufferedReader(file);
		} catch (IOException e) {
			throw new Exception("Parsing!\n" + e.getMessage());
		}
		
		
		// -- first line defines the (unique) initial state
		String line = readLine();
		if (line == null) throw abort("empty file");
		readInitial(line);
		
		// -- other lines define transitions and acceptance
		line = readLine();
		while (line != null) {
			if (line.matches(".*->.*")) readEdges(line);
			else readFinals(line);
			line = readLine();
		}
	
		// -- if the acceptance is unspecified then all states are final
		if (finals.isEmpty()) finals.addAll(states);
	}
	
	
	/** handles the first line **/
	private void readInitial(String line) throws Exception {
		// -- if 'init' unspecified then take the first defined state  
		if (line.matches(".*->.*")) initial = readEdges(line);
		else {
			initial = line.strip();
			states.add(initial);
		}
	}
	
	
	/** handles lines with the substring "->" **/
	private String readEdges(String line) throws Exception {
		// -- Transition: symbol, from -> to
		String[] names = line.split(",|->");
		
		if (names.length != 3) throw abort("transition format");
		for (int i = 0; i < names.length; i++)
			names[i] = names[i].strip();
		
		// -- Symbol
		String symbol = names[0].strip();
		if (symbol.length() < 1) throw abort("transition symbol missing (" + symbol + ")");
		alphabet.add(symbol);
		
		// -- From
		String from = names[1].strip();
		if (from.length() < 1) throw abort("transition origin state missing (" + from + ")");
		states.add(from);
		
		// -- To
		String to = names[2].strip();
		if (to.length() < 1) abort("transition target state missing (" + to + ")");
		states.add(to);
		
		transitions.add(new Triplet<>(from, symbol, to));
		return from;
	}
	
	
	/** handles lines without the substring "->" **/
	private void readFinals(String line) {
		states.add(line.strip());
		finals.add(line.strip());
	}
	
	
	
	private Exception abort(String message) {
		return new Exception("Parse error: " + message + "\n  File: " + file + "\n  Line: " + line_counter);
	}
	
	
	
	/**
	 * @return A String of the contents of a line without termination characters.
	 * Returns null if EOF has been reached without reading any characters
	 * @throws Error on IOException
	 **/
	private String readLine () throws Exception {
		line_counter++;
		try {
			return source.readLine();
		} catch (IOException e) {
			throw new Exception("Parsing!\n" + e.getMessage());
		}
	}
}
