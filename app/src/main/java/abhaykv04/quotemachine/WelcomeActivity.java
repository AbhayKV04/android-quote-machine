package abhaykv04.quotemachine;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class WelcomeActivity extends AppCompatActivity {

    private String TAG = QuoteActivity.class.getSimpleName();
    private TextView welcomeTitle, welcomeSubtitle;

    // Time (in ms) for which welcome screen appears
    private int SPLASH_TIMEOUT = 5000;

    // URL to get welcome quote JSON
    private static String url = "https://talaikis.com/api/quotes/random/";

    String quoteString = "Quote Machine";
    String authorString = "Any Mood, Respective Quote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeTitle = (TextView) findViewById(R.id.welcomeTitle);
        welcomeSubtitle = (TextView) findViewById(R.id.welcomeSubtitle);

        new GetQuote().execute();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcomeIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(welcomeIntent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetQuote extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    quoteString = jsonObj.getString("quote");
                    authorString = jsonObj.getString("author");

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Check your internet connection!", Toast.LENGTH_LONG).show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            welcomeTitle.setText("\"" + quoteString + "\"");
            welcomeSubtitle.setText(authorString);
        }
    }
}
