package test.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String INTENT_EXTRA_KEY = "args_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGoToNext = (Button) this.findViewById(R.id.startGame);
        btnGoToNext.setOnClickListener(view -> {
            Intent newIntent = new Intent(this, SecondActivity.class);
            newIntent.putExtra(INTENT_EXTRA_KEY, 42);

            this.startActivity(newIntent);
        });
    }
}
