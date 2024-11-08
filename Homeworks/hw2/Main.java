import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

import static java.lang.Math.abs;

public class Main {
    public static int[] initializeBoard(int N) {
        int[] arr = new int[N];
        Random random = new Random();

        arr[0] = random.nextInt(N);
        int minConflictRow;
        for (int col = 1; col < N; col++) {
            minConflictRow = getRowWithMinConflict(col, Arrays.copyOfRange(arr, 0, col - 1), N, col - 1);
            arr[col] = minConflictRow;
        }

        return arr;
    }
    public static int[] solve(int N){
        int k = 1;

        int[] queens;
        queens = initializeBoard(N);

        int iter = 0;
        Integer col;
        int row;
        while(iter++ <= k*N){
            // Randomly if two or more!
            col = getColWithQueenWithMaxConflicts(queens, N);
            if (col == null)
                return queens;
            // Randomly if two or more!
            row = getRowWithMinConflict(col, queens, N, N);

            // index=col & value=row
            queens[col] = row;
        }
        if(hasConflicts(queens, N)){
            // Restart
            solve(N);
        }
        return queens;
    }
    private static Integer getRowWithMinConflict(int col, int[] queens, int rows, int cols) {
        int conflicts, minConflicts = Integer.MAX_VALUE;
        List<Integer> conflictedRows = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            conflicts = getConflicts(col, row, queens, cols);
            if (conflicts < minConflicts) {
                minConflicts = conflicts;
                conflictedRows.clear();
                conflictedRows.add(row);
            } else if (conflicts == minConflicts) {
                conflictedRows.add(row);
            }
        }
        return getRandomElement(conflictedRows);
    }
    private static Integer getColWithQueenWithMaxConflicts(int[] queens, int N) {
        int maxConflicts = -1, conflicts;
        List<Integer> conflictedCols = new ArrayList<>();
        for (int col = 0; col < N; col++) {
            conflicts = getConflicts(col, queens[col], queens, N);
            if (conflicts > maxConflicts) {
                maxConflicts = conflicts;
                conflictedCols.clear();
                conflictedCols.add(col);
            } else if (conflicts == maxConflicts) {
                conflictedCols.add(col);
            }
        }
        if (maxConflicts == 0) {
            return null;
        }
        return !conflictedCols.isEmpty() ? getRandomElement(conflictedCols) : null;
    }

    private static int getConflicts(int col, int row, int[] queens, int cols) {
        int conflicts = 0, r;
        for (int c = 0; c < cols; c++) {
            if (c == col)
                continue;
            r = queens[c];

            if (r == row || abs(c - col) == abs(r - row))
                conflicts += 1;
        }
        return conflicts;
    }

    private static int getRandomElement(List<Integer> arr) {
        int index = (int)(Math.random() * arr.size());
        return arr.get(index);
    }
    private static boolean hasConflicts(int[] queens, int N) {
        boolean[] occupiedRows = new boolean[N];
        boolean[] occupiedMainDiagonals = new boolean[2*N-1];
        boolean[] occupiedAntiDiagonals = new boolean[2*N-1];

        int row, d1, d2;
        for (int col = 0; col < N; col++) {
            row = queens[col];
            d1 = col - row;
            d2 = col + row;
            if (occupiedRows[row] || occupiedMainDiagonals[d1 + N - 1] || occupiedAntiDiagonals[d2]) {
                return true;
            }
            occupiedRows[row] = true;
            occupiedMainDiagonals[d1 + N - 1] = true;
            occupiedAntiDiagonals[d2] = true;
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();

        if (N == 2 || N == 3) {
            System.out.println(-1);
        }

        long startTime = System.nanoTime();
        int[] queens = solve(N);
        long endTime = System.nanoTime();

        double elapsedTime = (endTime - startTime) / 1_000_000_000.0;

        if (queens != null) {
            if (N > 100) {
                System.out.printf("%.2f", elapsedTime);
            } else {
                System.out.println(Arrays.toString(queens));
            }
        } else {
            System.out.println(-1);
        }
    }
}