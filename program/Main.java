

import java.nio.file.Path;
import java.util.*;

import automata.*;
import engine.*;
import tuples.*;



public class Main {
	private static long time_start = 0L, time_end = 0L;
	private static boolean flag_q, flag_v, flag_p, flag_t, flag_o;
	private static PostI postI = null, postIrev = null;
	private static String witness = "";
	
	private static String getUsage () {
		return """
				USAGE
				  java -jar forklift.jar <SUB Buchi automaton> <SUP Buchi automaton>
				
				WHERE
				  both Buchi automata are given in '.ba' format
				  --> http://languageinclusion.org/doku.php?id=tools#the_ba_format
				""";
	}
	
	private static String getHelp () {
		return """
				USAGE
				  java -jar forklift.jar <SUB Buchi automaton> <SUP Buchi automaton>
				
				WHERE
				  both Buchi automata are given in '.ba' format
				  --> http://languageinclusion.org/doku.php?id=tools#the_ba_format

				EXIT CODE
				  0 if the inclusion is TRUE
				  1 if the inclusion is FALSE
				  127 the inclusion check was aborted

				OPTIONS
				  -v, --verbose
				      Details the execution steps
				  -q, --quiet
				      Disables output prints
				  -t, --time
				      Returns the run time (in milliseconds) excluding parsing time
				  -p, --prefix
				      Displays prefixes
				  -o, --optimize
				      Ignores some ultimately periodic words
				""";
	}

	private static ArrayList<String> setOptions (String[] args) throws Exception {
		ArrayList<String> files = new ArrayList<>(2);

		for (String arg : args) {
			if (arg.equals("-v") || arg.equals("--verbose")) flag_v = true;
			else if (arg.equals("-t") || arg.equals("--time")) flag_t = true;
			else if (arg.equals("-p") || arg.equals("--prefix")) flag_p = true;
			else if (arg.equals("-q") || arg.equals("--quiet")) flag_q = true;
			else if (arg.equals("-o") || arg.equals("--optimize")) flag_o = true;
			else if (arg.equals("-h") || arg.equals("--help") || arg.equals("-help")) {
				System.out.println(getHelp());
				System.exit(127);
			}
			else if (!arg.startsWith("-")) files.add(arg);
			else throw new Exception("The option " + arg + " is not supported!");
		}

		if (files.size() != 2) {
			System.out.println(getUsage());
			System.exit(127);
		}	
		return files;
	}
	
	
	private static void getPrefixes (BuchiAutomaton A, BuchiAutomaton B) {
		if (flag_p) {
			System.out.print("\n¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨");
			System.out.println("\nMAXIMAL PREFIXES");
			for (int s : A.getFinalStates()) {
				System.out.println("\nAt final state " + A.getName(s));
				for (Pair<SetOfStates, Word> pairW : postIrev.getSetOfT(s))
					System.out.println("  '" + A.toString(pairW.second()) + "'\n  " + B.toString(pairW.first()) + "\n");
			}
			System.out.print("\n¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨");
			System.out.println("\nMINIMAL PREFIXES");
			for (int s : A.getFinalStates()) {
				System.out.println("\nAt final state " + A.getName(s));
				for (Pair<SetOfStates, Word> pairU : postI.getSetOfT(s))
					System.out.println("  '" + A.toString(pairU.second()) + "'\n  " + B.toString(pairU.first()) + "\n");
			}
		}
	}



	public static void main(String[] args) {
		try {
			ArrayList<String> names = setOptions(args);
			Path[] files = new Path[2];
			files[0] = Path.of(names.get(0));
			files[1] = Path.of(names.get(1));

			// -- both automata share the same symbol identifiers
			SymbolManager symbols = new SymbolManager();
			BuchiAutomaton A = new BuchiAutomaton(files[0], symbols);
			BuchiAutomaton B = new BuchiAutomaton(files[1], symbols);

			if (flag_q == false) {
				System.out.println("################\n\nFORQ-based Language Inclusion Formal Testing");
				System.out.println("\n¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨\nFILES");
				System.out.println("  sub-automaton:   " + files[0]);
				System.out.println("  super-automaton: " + files[1]);
			}
			

			time_start = System.currentTimeMillis();
			boolean output = inclusion(A, B);
			time_end = System.currentTimeMillis();
			if (flag_t) {
				System.out.print("\n¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨");
				System.out.println("\nTIMING (ms):" + (time_end - time_start));
			}

			
			if (flag_q == false) {
				getPrefixes(A, B);
				if (witness != "") {
					System.out.print("\n¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨\n");
					System.out.println("NON-INCLUSION WITNESS");
					System.out.println("  " + witness);
				}
				System.out.println("\n¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨\nOUTPUT:" + output + "\n################");
			}

			System.exit(output?0:1);
		}
		catch (Exception e) {
			System.out.println("\nERROR\n  " + e.getMessage() + "\n");
			System.exit(127);
		}
	}


	private static boolean inclusion (BuchiAutomaton A, BuchiAutomaton B) {
		if (flag_v) System.out.println("\n¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨\nINCLUSION");
		
		
		if (flag_v) System.out.print("  MAX prefixes fix-point: computing ..\r");
		int iter_W = 1;
		postIrev = new PostI(A, B, true);
		while (postIrev.apply()) iter_W++;
		if (flag_v) System.out.println("  MAX prefixes fix-point: done (" +iter_W+ " iterations, " +postIrev.size()+ " elements)");
		
		
		
		if (flag_v) System.out.print("  MIN prefixes fix-point: computing ..\r");
		int iter_U = 1;
		postI = new PostI(A, B, false);
		while (postI.apply()) iter_U++;
		if (flag_v) System.out.println("  MIN prefixes fix-point: done (" +iter_U+ " iterations, " +postI.size()+ " elements)");
		
		
		int final_counter=0;
		int membership_counter=0;
		int call_V = 0;
		for (int s : A.getFinalStates()) {final_counter++;
			if (flag_v) System.out.println("  FINAL STATE '"+A.getName(s)+"' ("+final_counter+"/"+A.getFinalStates().size()+")");
			
			int max_counter=0;
			for (Pair<SetOfStates, Word> pairW : postIrev.getSetOfT(s)) {call_V++;max_counter++;
				SetOfStates W = pairW.first();
				Word word_of_W = pairW.second();

				if (flag_v) System.out.println("    considering MAX prefix '"+A.toString(word_of_W)+"' ("+max_counter+"/"+postIrev.getSetOfT(s).size()+")");

				// -- the MIN prefixes U with U ⊑ W do not depend on the period: select them once
				List<Pair<SetOfStates, Word>> prefixes_U = new ArrayList<>();
				for (Pair<SetOfStates, Word> pairU : postI.getSetOfT(s))
					if (pairU.first().smaller_than(W)) prefixes_U.add(pairU);

				if (flag_v) System.out.print("    PERIOD fix-point (" +call_V+ "): computing ..\r");
				
				int iter_V_local = 1;
				PostF postF = new PostF(A, B, s, W);
				while (postF.apply()) iter_V_local++;
				
				if (flag_v) System.out.println("    PERIOD fix-point (" +call_V+ "): done (" +iter_V_local+ " iterations, " +postF.size()+ " elements)");

				int period_counter = 0;
				for (Pair<SetOfContexts, Word> pairV : postF.getSetOfT(s)) {period_counter++;
					SetOfContexts V = pairV.first();
					Word word_of_V = pairV.second();
					
					if (flag_v) System.out.println("    considering period '"+A.toString(word_of_V)+"' ("+period_counter+"/"+postF.getSetOfT(s).size()+")");
	
					// start relevant
					if (flag_o) { if (relevance_test(W, V) == false) continue;}
						for (Pair<SetOfStates, Word> pairU : prefixes_U) {membership_counter++;
							SetOfStates U = pairU.first();
							Word word_of_U = pairU.second();
							
							if (flag_v) System.out.println("      considering MIN prefix '"+A.toString(word_of_U)+"' smaller than the MAX prefix '"+A.toString(word_of_W)+"'");
							if (flag_v) System.out.print("      MEMBERSHIP ("+membership_counter+"): computing of "+A.toString(word_of_U)+" cycle{"+A.toString(word_of_V)+"} ..\r");
							boolean belongs = B.membership(U, word_of_V);
							if (flag_v) System.out.println("      MEMBERSHIP ("+membership_counter+"): done                                                     ");
							
							if (belongs == false) {
								witness = A.toString(word_of_U) + " cycle{" + A.toString(word_of_V) + "}";
								if (flag_v) System.out.println("      >> ABORT counter-example found: "+witness); 
								return false;
							}
						}
					// end relevant
				}
			}
		}
		return true;
	}
	
	private static boolean relevance_test (SetOfStates W, SetOfContexts V) {
		StateRelation first = V.getFirst();
		for (int i = 0; i < first.size(); i++)
			if (first.get(i) != null && first.get(i).smaller_than(W) == false) return false;
		return true;
	}	
}


