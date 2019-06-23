#!/bin/bash

puzzle=$HOME/Dropbox/ICFP/2019/puzzles

solver="stack run --"

for n in $* ; do
    echo "Solving puzzle $n ..."
    time $solver $puzzle/puzzle_$n.cond > task_$n.desc
done
