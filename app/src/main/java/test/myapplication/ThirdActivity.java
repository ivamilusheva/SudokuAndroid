package test.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class ThirdActivity extends AppCompatActivity {
    private Button btnEasyGame;
    private Button btnMediumGame;
    private Button btnDifficultGame;
    private Button btnReturnToMainMenu;

    public static final String GAME_DIFFICULTY_KEY = "game_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        this.attachListeners();
    }

    private void attachListeners() {
        this.btnEasyGame = (Button) this.findViewById(R.id.btnEasyGame);
        this.btnMediumGame = (Button) this.findViewById(R.id.btnMediumGame);
        this.btnDifficultGame = (Button) this.findViewById(R.id.btnDifficultGame);
        this.btnReturnToMainMenu = (Button) this.findViewById(R.id.btnReturnToMainMenu);

        Intent intent = new Intent(this, GameActivity.class);

        this.btnEasyGame.setOnClickListener(view -> {
            intent.putExtra(GAME_DIFFICULTY_KEY, "Easy");
            this.startActivity(intent);
        });

        this.btnMediumGame.setOnClickListener(view -> {
            intent.putExtra(GAME_DIFFICULTY_KEY, "Medium");
            this.startActivity(intent);
        });

        this.btnDifficultGame.setOnClickListener(view -> {
            intent.putExtra(GAME_DIFFICULTY_KEY, "Difficult");
            this.startActivity(intent);
        });

        this.btnReturnToMainMenu.setOnClickListener(view -> {
            finish();
        });
    }
}
