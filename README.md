### ABOUT ###

FORKLIFT is an inclusion checker for Büchi automata.
The input comprises two automata `A` and `B` and FORKLIFT decides whether `L(A) ⊆ L(B)` holds.

The input format for the automata is described [here](https://github.com/parof/bait#the-ba-format).

Upon termination, FORKLIFT returns one of the following value:
    
- `0` if `L(A) ⊆ L(B)`
- `1` if `L(A) ⊈ L(B)` and a counterexample `u cycle {v}` such that `u v^ω ∈ L(A)` but `u v^ω ∉ L(B)`
- `127` when something goes wrong


### COMPILATION ###

Run `make` to compile.
Running `make` in the root directory compiles the source code (using `javac`), then constructs the java archive (using `jar`), and finally prints the usage of FORKLIFT.


### RUNNING ###

Run FORKLIFT as follows:
```    
java -jar forklift.jar sub.ba sup.ba
```
checks whether `L(A) ⊆ L(B)` holds for the input Büchi automata `A` given by  `sub.ba` and `B` given by `sup.ba`.

#### OPTIONS #### 
Insert options anywhere after `forklift.jar`.
For intance, running
```
java -jar forklift.jar -t sub.ba -o sup.ba -v
```
uses the options `optimization` (`-o`), `verbose` (`-v`) and `time` (`-t`).
Use the option `-h` to know more:
```
java -jar forklift.jar -h
```

### EXAMPLES ###
The folder `samples` contains examples of Büchi automata. 
For intance, running
```
java -jar forklift.jar ./samples/example_SUPERSET.ba ./samples/example_SUBSET.ba
```
checks the inclusion given in Fig.1 in our [CAV'22](https://arxiv.org/abs/2207.13549) paper.
In this case the inclusion does not hold and FORKLIFT provides the counterexample `u cycle {v}` to the inclusion.


