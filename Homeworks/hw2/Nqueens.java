import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Board {
    int N;
    int[] queens;
    int[] occupiedRows;
    int[] occupiedMainDiagonals;
    int[] occupiedAntiDiagonals;
    boolean hasConflicts;
    Random random = new Random();

    public Board(int N) {
        this.N = N;
        hasConflicts = true;
    }

    public void initializeBoard() {
        queens= new int[N];
        occupiedRows = new int[N];
        occupiedMainDiagonals = new int[2*N-1];
        occupiedAntiDiagonals = new int[2*N-1];
        queens[0] = random.nextInt(N);
        updateConflictArrays(queens[0], 0, 1);
        int minConflictRow;
        for (int col = 1; col < N; col++) {
            minConflictRow = getRowWithMinConflict(col);
            queens[col] = minConflictRow;
            updateConflictArrays(minConflictRow, col, 1);
        }
    }

    public int[] solve() {
        initializeBoard();

        int k = 1, iter = 0, col, row;
        while(iter++ <= k * N){
            // Randomly if two or more!
            col = getColWithQueenWithMaxConflicts();
            if (!hasConflicts)
                return queens;
            // Randomly if two or more!
            row = getRowWithMinConflict(col);

            updateConflictArrays(queens[col], col, -1);
            queens[col] = row;
            updateConflictArrays(queens[col], col, 1);
        }
        if (hasConflicts) {
            solve();
        }
        return queens;
    }

    private int getRowWithMinConflict(int col) {
        int conflicts, minConflicts = Integer.MAX_VALUE;
        List<Integer> conflictedRows = new ArrayList<>();
        for (int row = 0; row < N; row++) {
            conflicts = getConflicts(col, row);
            if (conflicts < minConflicts) {
                minConflicts = conflicts;
                conflictedRows.clear();
                conflictedRows.add(row);
            } else if (conflicts == minConflicts) {
                conflictedRows.add(row);
            }
        }
        return  conflictedRows.get(random.nextInt(conflictedRows.size()));
    }

    private int getColWithQueenWithMaxConflicts() {
        int maxConflicts = -1, conflicts;
        List<Integer> conflictedCols = new ArrayList<>();
        for (int col = 0; col < N; col++) {
            conflicts = getConflicts(col, queens[col]);
            if (conflicts > maxConflicts) {
                maxConflicts = conflicts;
                conflictedCols.clear();
                conflictedCols.add(col);
            } else if (conflicts == maxConflicts) {
                conflictedCols.add(col);
            }
        }
        if (maxConflicts == 0) {
            hasConflicts = false;
        }
        return  conflictedCols.get(random.nextInt(conflictedCols.size()));
    }

    private int getConflicts(int col, int row) {
        // don't remove 3 from the conflicts if there isn't a queen on this place
        int d1, d2;
        d1 = col - row;
        d2 = col + row;
        int conflicts = occupiedRows[row] + occupiedMainDiagonals[d1 + N - 1] + occupiedAntiDiagonals[d2];
        return queens[col] == row ? conflicts - 3 : conflicts;
    }

    private void updateConflictArrays(int row, int col, int value) {
        int d1 = col - row;
        int d2 = col + row;
        occupiedRows[row] += value;
        occupiedMainDiagonals[d1 + N - 1] += value;
        occupiedAntiDiagonals[d2] += value;
    }

    public void printBoard() {
        for (int col = 0; col < N; col++) {
            for (int row = 0; row < N; row++) {
                if (queens[col] == row) {
                    System.out.print("* ");
                } else {
                    System.out.print("_ ");
                }
            }
            System.out.println();
        }
    }
}

public class Nqueens {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();

        if (N == 2 || N == 3) {
            System.out.println(-1);
        }

        long startTime = System.nanoTime();
        Board board = new Board(N);
        board.solve();
        long endTime = System.nanoTime();

        double time = (endTime - startTime) / 1_000_000_000.0;

        if (N > 100) {
            System.out.printf("%.2f", time);
        } else {
            board.printBoard();
        }
    }
}
