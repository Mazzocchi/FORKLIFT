#!/usr/bin/awk -f
#
# This AWK script takes in input a file in HOAF format assuming it is a Buchi
# automata with a state-based acceptance and, this is very important, it has
# been preprocessed with the `--split-edges` transform of `autfilt` (one of the
# command line tools from SPOT). The documentation for `--split-edges` says the
# following: "split edges into transitions labeled by conjunctions of all
# atomic propositions, so they can be read as letters"
#
# Below (and commented) is the most basic version that complies with the BA file format.
#
# BEGIN { idx=1; }
# /^Start:/ { print $2; } #print the initial state
# /^State:/ {
#         st = $2;
#         if (match($0, /\{0\}/) != 0) {
#                 finals[idx]=st;
#                 idx = idx+1;
#         }
# } 
# /^\[/ { 
#         print $1","st"->"$2; # label is what's between `[` and `]` in the HOA file
# } 
# END { 
#         for (x = 1; x < idx; x++)
#                 print finals[x] 
# } # print the final states
#
# The most sophisticated version below associates to each valuation 
# of the APs its corresponding decimal number.
#
# Of course, improvements are welcome
BEGIN { idx=1; }
/^Start:/ { print $2; } #print the initial state
/^State:/ {
        st = $2; # record the source state in st to be used for transitions (next pattern/action)
        if (match($0, /\{0\}/) != 0) {
                finals[idx]=st; #record that st is accepting
                idx = idx+1;
        }
} 
/^\[/ { 
        AP=split(substr($1,2,length($1)-2), literals, "\&"); #first remove `[` and `]` then split into the array literals using & as separator
        bintodec=0;
        for(j=1; j<=AP; j++) # most significant digit first
                bintodec=(bintodec*2) + (substr(literals[j], 1, 1) == "!" ? 0 : 1);
        print bintodec","st"->"$2; # print the transition: "label,src->tgt"
} 
END { 
        for (x = 1; x < idx; x++)
                print finals[x] 
} # print the accepting states
