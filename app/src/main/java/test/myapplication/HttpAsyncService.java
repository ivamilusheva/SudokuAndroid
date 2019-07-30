package test.myapplication;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpAsyncService extends AsyncTask<String, String, String> {
    private final OkHttpClient okHttpClient;
    private final OnPostExecutedFinished callback;

    public HttpAsyncService(OnPostExecutedFinished callback) {
        this.okHttpClient = new OkHttpClient();
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        Request request =
            new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = this.okHttpClient.newCall(request)
                .execute();
            return response.body().string();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String json) {
        this.callback.call(json);
    }
}
