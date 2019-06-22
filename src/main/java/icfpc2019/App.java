package icfpc2019;

import icfpc2019.pathfinder.Pathfinder;
import icfpc2019.pathfinder.StarNode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("usage: solver problem-file [target-file]");
            System.exit(1);
        }

        if (args[0].equals("--target-dir")) {
            String targetDir = args[1];
            Arrays.stream(args, 2, args.length)
                    .parallel()
                    .forEach(p -> safeSolveProblem(p, replaceDir(p, targetDir)));
        } else if (args.length > 1) {
            solveProblem(args[0], args[1]);
        } else {
            solveProblem(args[0], null);
        }
    }

    private static void safeSolveProblem(String problemFile, String solutionFile) {
        try {
            solveProblem(problemFile, solutionFile);
        } catch (IOException e) {
            System.err.println("Failed to solve " + problemFile + " because of " + e);
        }
    }


    private static void solveProblem(String problemFile, String solutionFile) throws IOException {
        String desc = readFile(problemFile, StandardCharsets.UTF_8);
        ProblemDesc problem = ProblemDesc.of(desc);
        Grid grid = Grid.of(problem);
        Pathfinder finder = new Pathfinder();
        finder.initNodes(grid);
        Robot initRobot = new Robot(problem.getInitialWorkerLocation());
        State state = new State(grid, initRobot, problem.getBoosters(), finder);
        while (!state.mapFinished()) {
            int numRobots = state.getNumRobots();
            for (int i = 0; i < numRobots; i++) {
                Robot r = state.getRobot(i);
                Point next = state.getNextPointToVisit(r);
                if (next == null) continue;
                List<StarNode> starPath = finder.findPath(state.getCurrentPosition(r), next, 10);
                if (numRobots > 1) {
                    //only one field at a time when there are multiple robots, to keep them in sync
                    state.move(r, starPath.get(0), true);
                } else {
                    state.move(r, starPath);
                }
            }
        }
        System.out.println("Solution length: " + combineResults(state).length());

        if (solutionFile != null) {
            writeFile(solutionFile, combineResults(state));
            System.out.println("Solution written to: " + solutionFile);
        }
    }

    private static String combineResults(State state) {
        StringBuilder builder = new StringBuilder();
        builder.append(state.getResult(state.getRobot(0)));
        for (int i = 1; i < state.getNumRobots(); i++) {
            builder.append("#");
            builder.append(state.getResult(state.getRobot(i)));
        }
        return builder.toString();
    }

    private static void writeFile(String path, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(content);
        writer.close();
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private static String replaceDir(String path, String dir) {
        int p = path.lastIndexOf('/');
        if (p < 0) {
            return dir + "/" + path;
        } else {
            return dir + path.substring(p);
        }
    }

}
