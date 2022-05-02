

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
		return "\nUSAGE\n  java -jar forklift.jar <SUB Büchi automaton> <SUP Büchi automaton>\n"
				+ "\nWHERE\n  both Büchi automata are given in '.ba' format\n"
				+ "  --> http://languageinclusion.org/doku.php?id=tools#the_ba_format\n";
	}
	
	private static String getHelp () {
		return getUsage() + "\nEXIT CODE\n"
					+ "  0 if the inclusion is TRUE\n"
					+ "  1 if the inclusion is FALSE\n"
					+ "  127 when the inclusion have not been computed\n\n"
					+ "OPTIONS\n"				
					+ "  -v, --verbose\n    Details the execution steps\n"
					+ "  -q, --quiet\n    Disables output prints\n"
					+ "  -t, --time\n    Returns the run time (in milliseconds) excluding parsing time\n"
					+ "  -p, --prefix\n    Displays prefixes\n"
					+ "  -o, --optimize\n    Ignores some ultimately periodic words\n";
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
	
	
	private static void getPrefixes (BuchiAutomaton A) {
		if (flag_p) {
			System.out.print("\n¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨");
			System.out.println("\nMAXIMAL PREFIXES");
			for (State s : A.getFinalStates()) {
				System.out.println("\nAt final state " + s);
				for (Pair<SetOfStates, Word> pairW : postIrev.getSetOfT(s))
					System.out.println("  '" + pairW.getSecond() + "'\n  " + pairW.getFirst() + "\n");
			}
			System.out.print("\n¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨");
			System.out.println("\nMINIMAL PREFIXES");
			for (State s : A.getFinalStates()) {
				System.out.println("\nAt final state " + s);
				for (Pair<SetOfStates, Word> pairU : postI.getSetOfT(s))
					System.out.println("  '" + pairU.getSecond() + "'\n  " + pairU.getFirst() + "\n");
			}
		}
	}



	public static void main(String[] args) {
		try {
			ArrayList<String> names = setOptions(args);
			Path[] files = new Path[2];
			files[0] = Path.of(names.get(0));
			files[1] = Path.of(names.get(1));

			BuchiAutomaton A = new BuchiAutomaton(files[0], new HashSet<>());
			BuchiAutomaton B = new BuchiAutomaton(files[1], A.getAlphabet());

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
				getPrefixes(A);
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
		postIrev = new PostI(A.getInitialState(), B.getInitialState(), true);
		while (postIrev.apply()) iter_W++;
		if (flag_v) System.out.println("  MAX prefixes fix-point: done (" +iter_W+ " iterations, " +postIrev.size()+ " elements)");
		
		
		
		if (flag_v) System.out.print("  MIN prefixes fix-point: computing ..\r");
		int iter_U = 1;
		postI = new PostI(A.getInitialState(), B.getInitialState(), false);
		while (postI.apply()) iter_U++;
		if (flag_v) System.out.println("  MIN prefixes fix-point: done (" +iter_U+ " iterations, " +postI.size()+ " elements)");
		
		
		int final_counter=0;
		int membership_counter=0;
		int call_V = 0;
		for (State s : A.getFinalStates()) {final_counter++;
			if (flag_v) System.out.println("  FINAL STATE '"+s.getName()+"' ("+final_counter+"/"+A.getFinalStates().size()+")");
			
			int max_counter=0;
			for (Pair<SetOfStates, Word> pairW : postIrev.getSetOfT(s)) {call_V++;max_counter++;
				SetOfStates W = pairW.getFirst();
				Word word_of_W = pairW.getSecond();

				if (flag_v) System.out.println("    considering MAX prefix '"+word_of_W+"' ("+max_counter+"/"+postIrev.getSetOfT(s).size()+")");
				if (flag_v) System.out.print("    PERIOD fix-point (" +call_V+ "): computing ..\r");
				
				int iter_V_local = 1;
				PostF postF = new PostF(s, W);
				while (postF.apply()) iter_V_local++;
				
				if (flag_v) System.out.println("    PERIOD fix-point (" +call_V+ "): done (" +iter_V_local+ " iterations, " +postF.size()+ " elements)");

				int period_counter = 0;
				for (Pair<SetOfContexts, Word> pairV : postF.getSetOfT(s)) {period_counter++;
					SetOfContexts V = pairV.getFirst();
					Word word_of_V = pairV.getSecond();
					
					if (flag_v) System.out.println("    considering period '"+word_of_V+"' ("+period_counter+"/"+postF.getSetOfT(s).size()+")");
	
					// start relevant
					if (flag_o) { if (relevance_test(W, V) == false) continue;}
						for (Pair<SetOfStates, Word> pairU : postI.getSetOfT(s)) {
							SetOfStates U = pairU.getFirst();
							Word word_of_U = pairU.getSecond();
							
							if (U.smaller_than(W) == true) {membership_counter++;
								if (flag_v) System.out.println("      considering MIN prefix '"+word_of_U+"' smaller than the MAX prefix '"+word_of_W+"'");
								if (flag_v) System.out.print("      MEMBERSHIP ("+membership_counter+"): computing of "+word_of_U+" cycle{"+word_of_V+"} ..\r");
								boolean belongs = B.membership(U, word_of_V);
								if (flag_v) System.out.println("      MEMBERSHIP ("+membership_counter+"): done                                                     ");
								
								if (belongs == false) {
									witness = word_of_U + " cycle{" + word_of_V + "}";
									if (flag_v) System.out.println("      >> ABORT counter-example found: "+witness); 
									return false;
								}
							}
						}
					// end relevant
				}
			}
		}
		return true;
	}
	
	private static boolean relevance_test (SetOfStates W, SetOfContexts V) {
		for (SetOfStates set : V.getFirst().values())
			if (set.smaller_than(W) == false) return false;
		return true;
	}	
}


