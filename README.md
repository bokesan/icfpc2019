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
into the `solutions` folder. .buy files should be in the target dir if required.

See puzzle/README.md for the description of the puzzle solver.

## Our Solution approach

Out task solver is written in Java. The puzzle solver is written in Haskell.

We use a "hug the wall" approach and go to the nearest unwrapped field if
nore is reachable in the next move. Manipulators are attached at one side only,
to maximize the reach into the field when following walls or already wrapped fields.

Teleporters are placed near the center of the map but with a minimum distance from each other.
An A* algorithm treats teleporter fields as adjacent to all other fields.
Clones are used as soon as possible and manipulators are distributed evenly and attached to the sides of the existing ones.
The first robot is a dedicated collector of boosters and will first take care of getting everything valuable.
Spawned replicas go to work immediately.
Drills and wheels are ignored, they offered to little advantages over other features.

See puzzle/README.md for the description of the puzzle solver.
