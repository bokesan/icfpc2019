# icfpc2019

Team "Rotten Lambdas" ICFP programming contest 2019 entry.

Team members:

- Christoph Breitkopf <chbreitkopf@gmail.com>
- Jan Dreske <jandreske@web.de>
- Daniel Kauke <daniel.kauke@gmx.de>

## Building and running

Prerequisites: a Java 8 JDK.

Run `./gradlew build` in this folder to build the task solver.

Use

    java -Xms2g -jar build/libs/icfpc2019.jar --target-dir solutions problems/prob-*.desc

to generate solutions for all problems in the `problems` folder and write them
into the `solutions` folder.

See puzzle/README.md for the description of the puzzle solver.

## Our Solution approach

Out task solver is written in Java. The puzzle solver is written in Haskell.

We use a "hug the wall" approach and go to the nearest unwrapped field if
nore is reachable in the next move.

Teleporters are placed where found. Clones used as soon as possible and
manipulators are attached to the sides of the existing ones.

See puzzle/README.md for the description of the puzzle solver.
