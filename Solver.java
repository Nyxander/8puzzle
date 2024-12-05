import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    private final List<Board> solution;
    private final int moves;

    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode previous;
        private final int priority;


        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.priority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(SearchNode other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("Bordi eshte NULL. Nuk duhet te jete null.");
        if (!initial.isSolvable()) throw new IllegalArgumentException("Puzzle-i nuk zgjidhet!");

        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));

        SearchNode goalNode = null;

        while (!pq.isEmpty()) {
            SearchNode currentNode = pq.delMin();

            if (currentNode.board.isGoal()) {
                goalNode = currentNode;
                break;
            }

            // shton neighbor e sakte ne pq
            for (Board neighbor : currentNode.board.neighbors()) {
                if (currentNode.previous == null || !neighbor.equals(currentNode.previous.board)) {
                    pq.insert(new SearchNode(neighbor, currentNode.moves + 1, currentNode));
                }
            }


        }

        if (goalNode != null) {
            solution = new ArrayList<>();
            moves = goalNode.moves;
            for (SearchNode node = goalNode; node != null; node = node.previous) {
                solution.add(0, node.board);
            }
        }
        else {
            solution = null;
            moves = -1;
        }


    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return solution;
    }

    public static void main(String[] args) {

        for (String filename : args) {

            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            Board initial = new Board(tiles);


            try {
                Solver solver = new Solver(initial);

                if (solver.solution() == null) {
                    System.out.println("Puzzle-i nuk zgjidhet!");
                }


                for (Board board : solver.solution()) {
                    System.out.println(board);
                }

                System.out.println("Minimum number of moves = " + solver.moves());
            }
            catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}