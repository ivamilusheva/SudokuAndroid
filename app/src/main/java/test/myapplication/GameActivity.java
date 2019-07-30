package test.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Stack;

public class GameActivity extends AppCompatActivity {
    Button undo, redo, exit, checkPuzzle, solvePuzzle, loadNewGame;
    Stack<String> undoStack = new Stack<>();
    Stack<String> redoStack = new Stack<>();
    Stack<TextView> undoButtons = new Stack<>();
    Stack<TextView> redoButtons = new Stack<>();

    private TextView[][] sudokuBoard;
    private Button[] keyboard;
    private Server server;
    private String difficulty;
    private TextView selectedView;

    // Variables for solving sudoku
    private String EmptyCell = "0";
    private boolean solutionFound = false;

    public GameActivity() {
        this.sudokuBoard = new TextView[9][9];
        this.keyboard = new Button[9];
        this.server = new Server(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set current content
        setContentView(R.layout.activity_game);

        Intent intent = this.getIntent();
        this.difficulty = intent.getStringExtra(ThirdActivity.GAME_DIFFICULTY_KEY);

        GridLayout numbersLayout = (GridLayout) findViewById(R.id.numbers);

        GridLayout childLayoutBoard = new GridLayout(this);
        childLayoutBoard.setColumnCount(9);
        childLayoutBoard.setRowCount(9);

        this.initSudokuBoard(childLayoutBoard);
        numbersLayout.addView(childLayoutBoard);

        this.callServer();
        this.addKeyBoardListeners();
        this.addListenerOnButtons();
    }

    private void initSudokuBoard(GridLayout childLayoutBoard) {
        int[][] numbers = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuBoard[i][j] = new TextView(this);
                sudokuBoard[i][j].setText(numbers[i][j] + "");
                sudokuBoard[i][j].setBackground(getDrawable(R.drawable.border));
                sudokuBoard[i][j].setTextSize(30);
                sudokuBoard[i][j].setPadding(30, 0, 30, 0);
                sudokuBoard[i][j].setTextColor(Color.parseColor("#000000"));

                sudokuBoard[i][j].setOnClickListener(v -> {
                    v.setBackground(getDrawable(R.drawable.selected_border));

                    if (this.selectedView != null) {
                        this.selectedView.setBackground(getDrawable(R.drawable.border));
                    }

                    this.selectedView = (TextView)v;
                });

                childLayoutBoard.addView(sudokuBoard[i][j]);
            }
        }
    }

    private void updateTextViews(int[][] numbers) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.sudokuBoard[i][j].setText(numbers[i][j] + "");
            }
        }
    }

    private void addKeyBoardListeners() {
        for (int i = 0 ; i < 9; i++){
            String btnId = "btn" + Integer.valueOf(i + 1);
            Resources res = getResources();
            int id = res.getIdentifier(btnId, "id", this.getPackageName());
            Button currentBtn = (Button)this.findViewById(id);
            this.keyboard[i] = currentBtn;

            currentBtn.setOnClickListener(v -> {
                if (this.selectedView == null) {
                    Toast.makeText(this, "No selected square!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String previous = (String) selectedView.getText();
                    undoStack.push(previous);
                    undoButtons.push(selectedView);

                    String newText = (String) ((Button) v).getText();
                    this.selectedView.setText(newText);
                }
            });
        }
    }

    private void callServer() {
        // Call the server with callback and param(difficulty)
        this.server.requestOnlineSudoku(() -> {
            String json = server.getResponse();
            if (json != null) {

                Gson gson = new Gson();
                int[][] numbers = gson.fromJson(json, int[][].class);
                this.updateTextViews(numbers);

                // clear selected element from sudoku grid
                if (this.selectedView != null)
                {
                    this.selectedView.setBackground(getDrawable(R.drawable.border));
                }
                this.selectedView = null;

                // clear undo and redo operations
                this.undoStack = new Stack<>();
                this.redoStack = new Stack<>();
                this.undoButtons = new Stack<>();
                this.redoButtons = new Stack<>();
            }
            else {
                Toast.makeText(this, "Error while loading sudoku!", Toast.LENGTH_LONG).show();
            }
        }, difficulty);
    }

    private void addListenerOnButtons() {

        undo = (Button) findViewById(R.id.btnUndo);
        redo = (Button) findViewById(R.id.btnRedo);
        exit = (Button) findViewById(R.id.btnExitGame);
        checkPuzzle = (Button) findViewById(R.id.btnCheck);
        solvePuzzle = (Button) findViewById(R.id.btnSolve);
        loadNewGame = (Button) findViewById(R.id.btnLoadNewGame);

        undo.setOnClickListener(v -> {
            if (undoStack.empty()) {
                Toast.makeText(this, "There is no previous move", Toast.LENGTH_SHORT).show();
            }
            else {
                String undoMove = undoStack.pop();
                TextView field = undoButtons.pop();

                String nextNumber = (String) field.getText();
                field.setText(undoMove);

                redoStack.push(nextNumber);
                redoButtons.push(field);
            }

        });

        redo.setOnClickListener(v -> {
            if (redoStack.empty()) {
                Toast.makeText(this, "There is no sequent move", Toast.LENGTH_SHORT).show();
            }
            else {
                String redoMove = redoStack.pop();
                TextView field = redoButtons.pop();

                String previousNumber = (String) field.getText();
                field.setText(redoMove);

                undoStack.push(previousNumber);
                undoButtons.push(field);
            }

        });

        exit.setOnClickListener(v -> {
            this.finish();
        });

        checkPuzzle.setOnClickListener(v -> {
            String getBoard = "";

            if (checkRows(0) && checkRows(1) && checkRows(2) && checkRows(3)
                    && checkRows(4) && checkRows(5) && checkRows(6) && checkRows(7)
                    && checkRows(8) && checkColumns(0) && checkColumns(1)
                    && checkColumns(2) && checkColumns(3) && checkColumns(4)
                    && checkColumns(5) && checkColumns(6) && checkColumns(7)
                    && checkColumns(8) && checkSumOfRows(0) == 45
                    && checkSumOfRows(1) == 45 && checkSumOfRows(2) == 45
                    && checkSumOfRows(3) == 45 && checkSumOfRows(4) == 45
                    && checkSumOfRows(5) == 45 && checkSumOfRows(6) == 45
                    && checkSumOfRows(7) == 45 && checkSumOfRows(8) == 45
                    && checkSumOfColumns(0) == 45 && checkSumOfColumns(1) == 45
                    && checkSumOfColumns(2) == 45 && checkSumOfColumns(3) == 45
                    && checkSumOfColumns(4) == 45 && checkSumOfColumns(5) == 45
                    && checkSumOfColumns(6) == 45 && checkSumOfColumns(7) == 45
                    && checkSumOfColumns(8) == 45 && checkUniqueSquares()
                    && checkSumSquares()
                    ) {

                Toast.makeText(this, "The sudoku is correct!",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "There is a mistake!",Toast.LENGTH_SHORT).show();
            }

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    getBoard += (String) sudokuBoard[i][j].getText();
                }
                getBoard += "\n";
            }
        });

        solvePuzzle.setOnClickListener(v -> {
            int[][] numbers = new int[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    String text = (String) this.sudokuBoard[i][j].getText();
                    numbers[i][j] = Integer.parseInt(text);
                }
            }

            SudokuSolver solver = new SudokuSolver(numbers);
            solver.solve();

            int[][] newNumbers = solver.getBoard();
            this.updateTextViews(newNumbers);
        });

        loadNewGame.setOnClickListener(v -> {
            this.callServer();
        });
    }

    // ----------------------------------------
    // From here: functions for checking sudoku
    // ----------------------------------------

    //function to check rows of the grid
    private boolean checkRows(int numOfRow) {
        for (int i = 0; i < 9; i++) {
            for (int j = i + 1; j < 9; j++) {
                String first = (String)sudokuBoard[numOfRow][i].getText();
                String second = (String) sudokuBoard[numOfRow][j].getText();

                if (first.equals(second)) {
                    return false;
                }
            }
        }
        return true;
    }

    //function to check the columns of the grid
    private boolean checkColumns(int numOfCol) {
        for (int i = 0; i < 9; i++) {
            for (int j = i + 1; j < 9; j++) {
                if (sudokuBoard[i][numOfCol].getText().equals(sudokuBoard[j][numOfCol].getText())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkUniqueSquares() {
        for(int i=1;i<9;i+=3) {
            for (int j = 1; j < 9; j += 3) {
                boolean case0 = sudokuBoard[i - 1][j - 1].getText().equals(sudokuBoard[i][j].getText());
                boolean case1 = sudokuBoard[i - 1][j].getText().equals(sudokuBoard[i][j].getText());
                boolean case2 = sudokuBoard[i][j - 1].getText().equals(sudokuBoard[i][j].getText());
                boolean case3 = sudokuBoard[i + 1][j].getText().equals(sudokuBoard[i][j].getText());
                boolean case4 = sudokuBoard[i][j + 1].getText().equals(sudokuBoard[i][j].getText());
                boolean case5 = sudokuBoard[i + 1][j + 1].getText().equals(sudokuBoard[i][j].getText());
                boolean case6 = sudokuBoard[i - 1][j + 1].getText().equals(sudokuBoard[i][j].getText());
                boolean case7 = sudokuBoard[i + 1][j - 1].getText().equals(sudokuBoard[i][j].getText());

                if (case0 || case1 || case2 || case3 || case4 || case5 || case6 || case7)
                    return false;

            }
        }
        return true;
    }

    private boolean checkSumSquares() {
        for(int i=1;i<9;i+=3)
            for(int j=1;j<9;j+=3)
            {
                String c = (String) sudokuBoard[i-1][j-1].getText();

                int sum = Integer.parseInt(((String) sudokuBoard[i-1][j-1].getText())) +
                        Integer.parseInt(((String) sudokuBoard[i][j].getText())) +
                        Integer.parseInt(((String) sudokuBoard[i-1][j].getText())) +
                        Integer.parseInt(((String) sudokuBoard[i][j-1].getText())) +
                        Integer.parseInt(((String) sudokuBoard[i+1][j].getText())) +
                        Integer.parseInt(((String) sudokuBoard[i][j+1].getText())) +
                        Integer.parseInt(((String) sudokuBoard[i+1][j+1].getText())) +
                        Integer.parseInt(((String) sudokuBoard[i-1][j+1].getText())) +
                        Integer.parseInt(((String) sudokuBoard[i+1][j-1].getText()));
                if(sum!=45)
                    return false;

            }
        return true;
    }

    //function to check the sum of a row
    private int checkSumOfRows(int numOfRow) {
        int result = 0;
        for (int i = numOfRow; i <= numOfRow; i++) {
            for (int j = 0; j < 9; j++) {
                result += Integer.parseInt((String) sudokuBoard[i][j].getText());
            }
        }
        return result;
    }

    //function to check the sum of a column
    private int checkSumOfColumns(int numOfCol) {
        int result = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = numOfCol; j <= numOfCol; j++) {
                result += Integer.parseInt((String)sudokuBoard[i][j].getText());
            }
        }
        return result;
    }
}
