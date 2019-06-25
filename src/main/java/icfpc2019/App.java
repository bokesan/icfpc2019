package icfpc2019;

import icfpc2019.pathfinder.DistanceMap;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class App {

    private static final boolean PARALLEL = true;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("usage: solver problem-file [target-file]");
            System.exit(1);
        }

        if (args[0].equals("--target-dir")) {
            String targetDir = args[1];
            List<IdAndScore> scores;
            if (PARALLEL) {
                scores = Arrays.stream(args, 2, args.length)
                        .parallel()
                        .map(p -> safeSolveProblem(p, targetFile(p, targetDir), buyoutFile(p, targetDir)))
                        .collect(Collectors.toList());
            } else {
                SortedSet<String> problems = new TreeSet<>(Arrays.asList(args).subList(2, args.length));
                scores = new ArrayList<>();
                for (String p : problems) {
                    scores.add(safeSolveProblem(p, targetFile(p, targetDir), buyoutFile(p, targetDir)));
                }
            }
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(targetDir + File.separator + "scores.csv")))) {
                for (IdAndScore s : scores) {
                    writer.format("%d;%d\n", s.id, s.score);
                }
            }
        } else if (args[0].equals("--show")) {
            show(args[1]);
        } else if (args[0].equals("--random")) {
            randomTest(args[1]);
        } else if (args.length > 2) {
            solveProblem(args[0], args[1], args[2]);
        } else if (args.length > 1) {
            solveProblem(args[0], args[1], "");
        } else {
            solveProblem(args[0], null, "");
        }
    }

    private static void randomTest(String problemFile) {
        int bestScore = Integer.MAX_VALUE;
        int bestIter = 0;
        for (int i = 1; i < 1000; i++) {
            String solution = String.format("%s.%03d.sol", problemFile, i);
            IdAndScore s = safeSolveProblem(problemFile, solution, "");
            if (s.score < bestScore) {
                bestScore = s.score;
                bestIter = i;
            }
            System.out.format("%3d: %d. Best %d: %d\n", i, s.score, bestIter, bestScore);
        }
    }


    private static IdAndScore safeSolveProblem(String problemFile, String solutionFile, String shoppinglistFile) {
        try {
            return solveProblem(problemFile, solutionFile, shoppinglistFile);
        } catch (IOException e) {
            System.err.println("Failed to solve " + problemFile + " because of " + e);
            return new IdAndScore(problemFile, Integer.MAX_VALUE);
        }
    }

    private static void show(String problemFile) throws IOException {
        String desc = readFile(problemFile);
        ProblemDesc problem = ProblemDesc.of(desc);
        Grid grid = Grid.of(problem);
        System.out.println(grid.toString());
        DistanceMap d = new DistanceMap(grid);
        d.from(Point.of(100,100));
        System.out.println(d.toString());
    }

    private static IdAndScore solveProblem(String problemFile, String solutionFile, String shoppingListFile) throws IOException {
        long startTime = System.nanoTime();
        String desc = readFile(problemFile);
        String shoppingList = getShoppinglist(shoppingListFile);
        ProblemDesc problem = ProblemDesc.of(desc);
        System.out.format("Starting solver for %s ...\n", problemFile);
        Solver solver = new CounterClockwiseSolverWithFallbackTarget();
        if(shoppingList.length() > 0)
            solver.init(problem, shoppingList);
        else
            solver.init(problem);
        List<ActionSequence> result = solver.solve();
        String actions = result.stream().map(ActionSequence::getEncoding).collect(Collectors.joining("#"));
        int score = result.get(0).getLength();
        writeResult(solutionFile, startTime, actions, score);
        return new IdAndScore(problemFile, score);
    }

    private static String getShoppinglist(String path) throws IOException{
        String shoppingList  = "";
        if(fileExists(path)){
            shoppingList = readFile(path);
        }        
        return shoppingList;
    }

    private static void writeResult(String solutionFile, long startTime, String result, int score) throws IOException {
        if (solutionFile == null) {
            System.out.format("Solution length: %d, actions: %d\n", result.length(), score);
        } else {
            writeFile(solutionFile, result);
            long elapsed = System.nanoTime() - startTime;
            System.out.format("Solution written to %s, length: %d, actions: %d, elapsed: %.3fs\n",
                    solutionFile, result.length(), score, elapsed / 1.0e9);
        }
    }

    private static void writeFile(String path, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(content);
        }
    }

    private static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }
    private static boolean fileExists(String path){
        return new File(path).exists();
    }

    private static String targetFile(String path, String dir) {
        if (path.endsWith(".desc")) {
            path = path.substring(0, path.length() - 4) + "sol";
        }
        int p = path.lastIndexOf(File.separatorChar);      
        if (p < 0) {
            return dir + File.separatorChar + path;
        } else {
            return dir + path.substring(p);
        }
    }

    private static String buyoutFile(String path, String dir) {
        if (path.endsWith(".desc")) {
            path = path.substring(0, path.length() - 4) + "buy";
        }
        int p = path.lastIndexOf(File.separatorChar);
        if (p < 0) {
            return dir + File.separatorChar + path;
        } else {
            return dir + path.substring(p);
        }
    }

    private static int getProblemId(String file) {
        int p = file.lastIndexOf('.');
        String numPart = file.substring(p - 3, p);
        return Integer.parseInt(numPart, 10);
    }

    private static class IdAndScore {
        final int id;
        final int score;

        IdAndScore(int id, int score) {
            this.id = id;
            this.score = score;
        }

        IdAndScore(String problemFile, int score) {
            this(getProblemId(problemFile), score);
        }

    }

}
