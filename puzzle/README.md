# Puzzle solver

Written in a hurry in Haskell.

To build it you need stack: https://docs.haskellstack.org

The run `stack build` in the current folder.

The generate a task for a puzzle, use

    stack run -- puzzle_nnn.cond > task_nnn.desc

## Approach

We start with a one square wide wall along the borders of the map and
connect all outside squares to the nearest wall. If we have too few
vertices after that, more outside squares are randomly generated and
connected until there are enough vertices.
