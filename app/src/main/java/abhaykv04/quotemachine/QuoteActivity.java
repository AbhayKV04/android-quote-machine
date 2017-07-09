package abhaykv04.quotemachine;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class QuoteActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private String TAG = QuoteActivity.class.getSimpleName();

    private TextView quote, author, newText;
    private String quoteString = "Check your internet connection!";
    private String authorString = "";
    private String[] primaryColors = {"#C62828", "#AD1457", "#6A1B9A", "#4527A0", "#283593", "#1565C0", "#0277BD", "#00838F", "#00695C", "#2E7D32", "#558B2F", "#9E9D24", "#F9A825", "#FF8F00", "#EF6C00", "#D84315", "#4E342E"};
    private String[] accentColors = {"#FFCDD2", "#F8BBD0", "#E1BEE7", "#D1C4E9", "#C5CAE9", "#BBDEFB", "#B3E5FC", "#B2EBF2", "#B2DFDB", "#C8E6C9", "#DCEDC8", "#F0F4C3", "#FFF9C4", "#FFECB3", "#FFE0B2", "#FFCCBC", "#D7CCC8"};

    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(QuoteActivity.this);
        setContentView(R.layout.activity_quote);

        quote = (TextView) findViewById(R.id.quote);
        author = (TextView) findViewById(R.id.author);
        newText = (TextView) findViewById(R.id.newText);

        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(QuoteActivity.this, R.color.colorPrimary));
        newText.setTextColor(ContextCompat.getColor(QuoteActivity.this, R.color.colorAccent));

        new GetQuote().execute();
    }

    /**
     * Single touch detection
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        new GetQuote().execute();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetQuote extends AsyncTask<Void, Void, Void> {

        Bundle extras = getIntent().getExtras();

        // URL to get quote JSON
        private String url = extras.getString("url");
        private int id = extras.getInt("id");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            newText.setText("Fetching");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    if (id == 1 || id == 3) {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        quoteString = jsonObject.getString("quote");
                        authorString = jsonObject.getString("author");
                    } else if (id == 2) {
                        JSONArray jsonArray = new JSONArray(jsonStr);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        quoteString = jsonObject.getString("quote");
                        authorString = jsonObject.getString("author_name");
                    } else if (id == 4) {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        JSONObject quoteObject = jsonObject.getJSONObject("value");
                        quoteString = quoteObject.getString("joke");
                        authorString = "";
                    } else if (id == 5) {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        JSONObject authorObject = jsonObject.getJSONObject("author");
                        quoteString = jsonObject.getString("content");
                        authorString = authorObject.getString("name");
                    }
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

            /**
             * Updating parsed JSON data into TextView
             * */

            int randomIndex, themeId;
            themeId = extras.getInt("themeId");

            if (themeId == 0) {
                randomIndex = new Random().nextInt(primaryColors.length);
                String randomPrimary = (primaryColors[randomIndex]);
                String randomAccent = (accentColors[randomIndex]);

                getWindow().getDecorView().setBackgroundColor(Color.parseColor(randomPrimary));
                quote.setTextColor(Color.parseColor(randomAccent));
                author.setTextColor(Color.parseColor(randomAccent));
            } else if (themeId == 1) {
                getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(QuoteActivity.this, R.color.colorPrimary));
                quote.setTextColor(ContextCompat.getColor(QuoteActivity.this, R.color.colorAccent));
                author.setTextColor(ContextCompat.getColor(QuoteActivity.this, R.color.colorAccent));
            }

            newText.setTextColor(ContextCompat.getColor(QuoteActivity.this, R.color.colorAccent));
            quote.setText("\"" + quoteString + "\"");
            author.setText(authorString);
            newText.setText("New");
        }
    }
}