package test.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    private Button btnNewGame;
    private Button btnHelp;
    private Button btnAboutTheGame;
    private Button btnExitGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        this.attachListeners();
    }

    private void attachListeners() {
        this.btnNewGame = (Button) this.findViewById(R.id.btnNewGame);
        this.btnHelp = (Button) this.findViewById(R.id.btnHelp);
        this.btnAboutTheGame = (Button) this.findViewById(R.id.btnAboutTheGame);
        this.btnExitGame = (Button) this.findViewById(R.id.btnExit);

        this.btnNewGame.setOnClickListener(view -> {
            Intent newIntent = new Intent(this, ThirdActivity.class);

            this.startActivity(newIntent);
        });

        this.btnHelp.setOnClickListener(view -> {
            final Dialog messageWindow = new Dialog(this);
            messageWindow.setContentView(R.layout.message);
            messageWindow.setTitle("Help");

            TextView messageHelp = (TextView) messageWindow.findViewById(R.id.message);
            messageHelp.setPadding(30, 30, 30, 30);
            messageHelp.setText("Click on a number from your right "
                    + "in the block of numbers under the title \"Select number\" \n"
                    + "and then click on the box you want to fill the number in. In "
                    + "every row, column and mini grid \n there should not be any same "
                    + "numbers.");


            Button okButton = (Button) messageWindow.findViewById(R.id.btnOK);
            okButton.setOnClickListener(v1 -> messageWindow.dismiss());

            messageWindow.show();
        });

        this.btnAboutTheGame.setOnClickListener(view -> {
            final Dialog messageWindow = new Dialog(this);
            messageWindow.setContentView(R.layout.message);
            messageWindow.setTitle("About the Game");

            TextView messageAboutTheGame = (TextView) messageWindow.findViewById(R.id.message);
            messageAboutTheGame.setPadding(30, 30, 30, 30);
            messageAboutTheGame.setText("The name Sudoku comes from the "
                    + "Japanese word that means “number place.” \n The first Sudoku"
                    + " puzzle was published in the United States, but Sudoku"
                    + " initially \n became popular"
                    + "in Japan, in 1986, and did not attain international popularity "
                    + "until 2005.");


            Button okButton = (Button) messageWindow.findViewById(R.id.btnOK);
            okButton.setOnClickListener(v12 -> messageWindow.dismiss());

            messageWindow.show();
        });

        this.btnExitGame.setOnClickListener(view -> {
            this.finishAffinity();
        });
    }
}
