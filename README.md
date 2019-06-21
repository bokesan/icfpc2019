# icfpc2019

Team "" ICFP programming contest 2019 entry.

Team members:

- Christoph Breitkopf <chbreitkopf@gmail.com>
- Jan Dreske <jandreske@web.de>
- Daniel Kauke <daniel.kauke@gmx.de>

## Building and running

Run `./gradlew build` in this folder to build the application.

Run `./gradlew run` to execute the application.
To pass command line arguments to the application, pass them in the `--args` argument,
for example:

    ./gradlew run --args='--file="foo.json" --solver=best --who-rules=we'

or

    ./gradlew run --args=$HOME/Dropbox/ICFP/2019/task/part-1/prob-001.desc

To build a distribution archive use `./gradlew distZip`. The resulting archive
is placed in `build/distributions`.
