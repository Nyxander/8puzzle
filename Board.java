import java.util.ArrayList;

public class Board {
    private final int[][] tiles;
    private final int n;
    private final int manhattanDistance;
    private final int hammingDistance;

    // konstruktori i board
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException("Tiles cannot be null");
        this.n = tiles.length;
        this.tiles = copyTiles(tiles);
        this.manhattanDistance = calculateManhattan();
        this.hammingDistance = calculateHamming();
    }


    private int[][] copyTiles(int[][] original) {
        int[][] copy = new int[original.length][original.length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }

    // per te ndertuar stringen qe sherben si boardi gjate printimit
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append("\n");
        for (int[] row : tiles) {
            for (int tile : row) {
                sb.append(tile).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int tileAt(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n)
            throw new IllegalArgumentException("Invalid row or column");
        return tiles[row][col];
    }

    public int size() {
        return n;
    }


    // llogarit distancen hamming e board-it fillestar me ate te zgjidhur perfundimtar
    private int calculateHamming() {
        int hamming = 0;
        int value = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != 0 && tiles[i][j] != value) {
                    hamming++;
                }
                value++;
            }
        }
        return hamming;
    }

    // llogarit distancen manhatan
    // ajo mat sa larg eshte cdo tile nga pozicioni i pikesynuar.
    // eshte shume e dy diferencave absolute e:
    // 1.rreshtit te tanishem me ate te synuar.
    // 2.kolones se tanishme me ate te synuar
    private int calculateManhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = tiles[i][j];
                if (tile != 0) {
                    int goalRow = (tile - 1) / n;
                    int goalCol = (tile - 1) % n;
                    manhattan += Math.abs(goalRow - i) + Math.abs(goalCol - j);
                }
            }
        }
        return manhattan;
    }

    public int hamming() {
        return hammingDistance;
    }

    public int manhattan() {
        return manhattanDistance;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    // per te krahasuar boardet
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.n != that.n) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) return false;
            }
        }
        return true;
    }

    // nderton nje arraylist ku mbahen neighbors board
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();
        int blankRow = -1, blankCol = -1;

        outer:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                    break outer;
                }
            }
        } // gjejme koordinatat e 0

        int[][] moves = {
                { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }
        }; // levizjet e mundshme majtas, djathtas, posht lart
        for (int[] move : moves) {
            int newRow = blankRow + move[0];
            int newCol = blankCol + move[1];
            if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < n) {
                int[][] newTiles = copyTiles(tiles);
                newTiles[blankRow][blankCol] = newTiles[newRow][newCol];
                newTiles[newRow][newCol] = 0;
                neighbors.add(new Board(newTiles));
            }
        }
        return neighbors;
    }

    // provon nese puzzle zgjidhet dot apo jo
    // rregulli numri i inversion(8>1)+rreshti i 0)%2=1
    public boolean isSolvable() {
        int inversions = 0;
        int[] flatTiles = new int[n * n];
        int idx = 0;
        int blankRow = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                flatTiles[idx++] = tiles[i][j];
                if (tiles[i][j] == 0) blankRow = i;
            }
        }

        for (int i = 0; i < flatTiles.length; i++) {
            for (int j = i + 1; j < flatTiles.length; j++) {
                if (flatTiles[i] > flatTiles[j] && flatTiles[j] != 0) {
                    inversions++;
                }
            }
        }

        if (n % 2 != 0) return inversions % 2 == 0;
        return (inversions + blankRow) % 2 != 0;
    }


}
