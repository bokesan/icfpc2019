package icfpc2019;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class App {

    private static final boolean PARALLEL = true;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("usage: solver problem-file [target-file]");
            System.exit(1);
        }

        if (args[0].equals("--target-dir")) {
            String targetDir = args[1];
            if (PARALLEL) {
                Arrays.stream(args, 2, args.length)
                        .parallel()
                        .forEach(p -> safeSolveProblem(p, targetFile(p, targetDir)));
            } else {
                SortedSet<String> problems = new TreeSet<>(Arrays.asList(args).subList(2, args.length));
                for (String p : problems) {
                    safeSolveProblem(p, targetFile(p, targetDir));
                }
            }
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
        long startTime = System.nanoTime();
        String desc = readFile(problemFile);
        ProblemDesc problem = ProblemDesc.of(desc);
        System.out.format("Starting solver for %s ...\n", problemFile);
        Solver solver = new CounterClockwiseSolver();
        solver.init(problem);
        String result = solver.solve();
        writeResult(solutionFile, startTime, result);
    }

    private static void writeResult(String solutionFile, long startTime, String result) throws IOException {
        if (solutionFile == null) {
            System.out.format("Solution length: %d\n", result.length());
        } else {
            writeFile(solutionFile, result);
            long elapsed = System.nanoTime() - startTime;
            System.out.format("Solution written to %s, length: %d, elapsed: %.3fs\n",
                    solutionFile, result.length(), elapsed / 1.0e9);
        }
    }

    private static void writeFile(String path, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(content);
        writer.close();
    }

    private static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    private static String targetFile(String path, String dir) {
        if (path.endsWith(".desc")) {
            path = path.substring(0, path.length() - 4) + "sol";
        }
        int p = path.lastIndexOf(File.separatorChar);
        if (p < 0) {
            return dir + File.separator + path;
        } else {
            return dir + path.substring(p);
        }
    }

}
