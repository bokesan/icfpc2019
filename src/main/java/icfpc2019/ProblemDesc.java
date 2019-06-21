package icfpc2019;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProblemDesc {

    private final List<Point> map;

    private final List<List<Point>> obstacles;

    private final List<BoosterLocation> boosters;

    private final Point initialWorkerLocation;

    private ProblemDesc(List<Point> map, List<List<Point>> obstacles, List<BoosterLocation> boosters, Point initialPos) {
        this.map = map;
        this.obstacles = obstacles;
        this.boosters = boosters;
        this.initialWorkerLocation = initialPos;
    }

    public static ProblemDesc of(String desc) {
        return new Parser(desc).parse();
    }


    public List<Point> getMap() {
        return map;
    }

    public List<List<Point>> getObstacles() {
        return obstacles;
    }

    public List<BoosterLocation> getBoosters() {
        return boosters;
    }

    public Point getInitialWorkerLocation() {
        return initialWorkerLocation;
    }

    private static class Parser {

        private final String s;
        private final int length;
        private int pos;

        Parser(String s) {
            this.s = s;
            length = s.length();
        }

        ProblemDesc parse() {
            pos = 0;
            List<Point> map = parseMap();
            skip('#');
            Point initial = parsePoint();
            skip('#');
            List<List<Point>> obstacles = parseObstacles();
            skip('#');
            List<BoosterLocation> boosters = parseBoosters();
            return new ProblemDesc(map, obstacles, boosters, initial);
        }

        private Point parsePoint() {
            skip('(');
            int x = parseNat();
            skip(',');
            int y = parseNat();
            skip(')');
            return Point.of(x, y);
        }

        private int parseNat() {
            int n = 0;
            for (;;) {
                int c = s.charAt(pos);
                if (!Character.isDigit(c)) {
                    break;
                }
                n = 10 * n + (c - 48);
                pos++;
            }
            return n;
        }

        private boolean hasMore() {
            return pos < length;
        }

        private List<Point> parseMap() {
            List<Point> vertices = new ArrayList<>();
            vertices.add(parsePoint());
            while (s.charAt(pos) == ',') {
                pos++;
                vertices.add(parsePoint());
            }
            return vertices;
        }

        private List<List<Point>> parseObstacles() {
            if (s.charAt(pos) == '#') {
                return Collections.emptyList();
            }
            List<List<Point>> obstacles = new ArrayList<>();
            obstacles.add(parseMap());
            while (s.charAt(pos) == ';') {
                pos++;
                obstacles.add(parseMap());
            }
            return obstacles;
        }

        private List<BoosterLocation> parseBoosters() {
            if (!(hasMore() && s.charAt(pos) != '#')) {
                return Collections.emptyList();
            }
            List<BoosterLocation> locs = new ArrayList<>();
            locs.add(parseBooster());
            while (hasMore() && s.charAt(pos) == ';') {
                pos++;
                locs.add(parseBooster());
            }
            return locs;
        }

        private BoosterLocation parseBooster() {
            BoosterCode code = BoosterCode.valueOf(s.substring(pos, pos+1));
            pos++;
            Point loc = parsePoint();
            return new BoosterLocation(code, loc);
        }


        private void skip(char ch) {
            if (s.charAt(pos) != ch) {
                throw new ParseError("expected '" + ch + "'", pos);
            }
            pos++;
        }

    }

    public static class ParseError extends RuntimeException {

        public ParseError(String msg) {
            super(msg);
        }

        public ParseError(String problem, int pos) {
            super(problem + " at character " + pos);
        }
    }
}
