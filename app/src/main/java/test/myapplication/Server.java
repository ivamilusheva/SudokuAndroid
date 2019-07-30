package test.myapplication;

import android.support.v7.app.AppCompatActivity;

import okhttp3.OkHttpClient;

public class Server {
    private final String apiUrl = "http://192.168.8.100:49722/api/";

    private AppCompatActivity context;
    private OkHttpClient client;
    private String json;

    public Server(AppCompatActivity context) {
        this.context = context;
        this.client = new OkHttpClient();
    }

    public void requestOnlineSudoku(Runnable function, String difficulty) {
        String url = this.apiUrl + "sudoku?code=" + difficulty;

        new HttpAsyncService((String result) -> {
            this.context.runOnUiThread(() -> {
                this.json = result;
                function.run();
            });
        }).execute(url);
    }

    public String getResponse() {
        return this.json;
    }
}
