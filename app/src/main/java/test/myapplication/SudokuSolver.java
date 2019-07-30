package test.myapplication;

public class SudokuSolver {
    //variable for the sudoku board
    private int[][] board;

    //variable for the empty cells
    public final int EmptyCell = 0;

    //variable showing whetherr a solution has been found
    boolean solutionFound = false;

    //default constructor
    public SudokuSolver(int[][] board) {
        this.board = board;
    }

    //return the board
    public int[][] getBoard() {
        return board;
    }

    //function to ge the possible digits
    private boolean[] getPossibleDigits(int i, int j) {
        if (board[i][j] != EmptyCell) {
            return new boolean[]{false, false, false, false, false, false, false, false, false};
        }
        // Trying to find the only solution for the current cell
        boolean[] isPossibleNumber = new boolean[]{true, true, true, true, true, true, true, true, true};

        // horizontal
        for (int k = 0; k < 9; k++) {
            if (board[i][k] != EmptyCell) {
                isPossibleNumber[board[i][k] - 1] = false;
            }
        }

        // vertical
        for (int k = 0; k < 9; k++) {
            if (board[k][j] != EmptyCell) {
                isPossibleNumber[board[k][j] - 1] = false;
            }
        }

        // mini grid
        int topLeftRow = (i - i % 3); // biggest integer that is lower than i and is divisible by 3
        int topLeftCol = (j - j % 3); // biggest integer that is lower than j and is divisible by 3
        for (int x = topLeftRow; x < topLeftRow + 3; x++) {
            for (int y = topLeftCol; y < topLeftCol + 3; y++) {
                if (board[x][y] != EmptyCell) {
                    isPossibleNumber[board[x][y] - 1] = false;
                }
            }
        }
        return isPossibleNumber;
    }

    //function to count the possible digits
    private int countOfPossibleDigits(boolean[] possibleDigits) {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            if (possibleDigits[i]) {
                count++;
            }
        }

        return count;
    }

    //function to optimize the board before solvation
    private boolean optimizeBeforeTheRecursion() {
        boolean optimized = false;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boolean[] possibleDigits = getPossibleDigits(i, j);
                int countOfPossible = this.countOfPossibleDigits(possibleDigits);
                if (countOfPossible == 1) {
                    for (int k = 0; k < 9; k++) {
                        if (possibleDigits[k]) {
                            board[i][j] = k + 1;
                            optimized = true;
                        }
                    }
                }
            }
        }
        return optimized;
    }

    //function to return the number of the empty cells
    private int getEmptyCellsCount() {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    //function to solve the sudoku with a recursion
    private void solveWithRecursion(int i, int j, int empty) {
        if (empty == 0) {
            solutionFound = true;
            return;
        }

        boolean[] possibleDigits = getPossibleDigits(i, j); // candidates for the current cell

        for (int k = 0; k < 9; k++) {
            if (!possibleDigits[k]) {
                continue;
            }

            board[i][j] = k + 1;

            // Find the first empty cell:
            int topLeftEmptyX = 0;
            int topLeftEmptyY = 0;
            boolean foundEmpty = false;
            for (int x = i; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    if (board[x][y] == EmptyCell) {
                        topLeftEmptyX = x;
                        topLeftEmptyY = y;
                        foundEmpty = true;
                        break;
                    }
                }
                if (foundEmpty) {
                    break;
                }
            }

            solveWithRecursion(topLeftEmptyX, topLeftEmptyY, empty - 1);
            if (solutionFound) {
                return;
            }
        }
        board[i][j] = 0;
    }

    //function to solve the sudoku
    public void solve() {
        while (optimizeBeforeTheRecursion()) {
        }
        int emptyAfter = getEmptyCellsCount();

        int topLeftEmptyX = 0;
        int topLeftEmptyY = 0;
        boolean foundEmpty = false;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == EmptyCell) {
                    topLeftEmptyX = i;
                    topLeftEmptyY = j;
                    foundEmpty = true;
                    break;
                }
            }
            if (foundEmpty) {
                break;
            }
        }

        if (!foundEmpty) {
            return; // the sudoku is already solved
        }
        this.solveWithRecursion(topLeftEmptyX, topLeftEmptyY, emptyAfter);
    }
}