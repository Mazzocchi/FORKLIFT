### ABOUT ###

The program FORKLIFT is an inclusion checker for Büchi automata.
It takes two automata as input, presented the format described here:
    http://languageinclusion.org/doku.php?id=tools#the_ba_format
Let us call the first given automaton A and B the second one.
Upon termination, FORKLIFT exit code is:
    0 if L(A) is subset of L(B)
    1 if L(A) is NOT subset of L(B)
    127 when the inclusion have not been computed


### COMPILATION ###

The compilation is performed thanks to the command:
    make
Note that, the makefile will simply call another one located in the folder `program`.
The slave makefile will:
    compile the source code (using `javac`)
    then construct the java archive (using `jar`)
    and finally print the usage of FORKLIFT


### RUNNING ###

To run FORKLIFT without options execute the command:
    java -jar forklift.jar sub.ba sup.ba
where both `sub.ba` and `sup.ba` encode some Büchi automaton.
Please find several encoding of Büchi automata in the folder `samples`.
For intance running 
    java -jar forklift.jar ./samples/example_SUPERSET.ba ./samples/example_SUBSET.ba
checks whether L(A) is included in L(B) where A and B are given in Fig.1 in the paper.
Since this inclusion does not holds, FORKLIFT provides an non-inclusion witness `u cycle {v}`.


### OPTIONS ### 

Options can be inserted anywhere after `forklift.jar` in the run command.
For intance running
    java -jar forklift.jar -t ./samples/example_SUPERSET.ba -o ./samples/example_SUBSET.ba -v
enables the options `optimization`, the option `verbose` and the option `time`.
For further information about options use the command:
    java -jar forklift.jar -h


