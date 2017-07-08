package abhaykv04.quotemachine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuoteActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private String TAG = QuoteActivity.class.getSimpleName();

    private TextView quote, author;
    private String quoteString = "Check your internet connection!";
    private String authorString = "";

    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this);
        setContentView(R.layout.activity_quote);

        Toast.makeText(getApplicationContext(), "Swipe to get a new one!", Toast.LENGTH_SHORT).show();

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("id");

        /**
         * See 'Intent ID Guide' in MainActivity
         */
        if (id == 1) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.progColorPrimary));
        } else if (id == 2) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.motiColorPrimary));
        } else if (id == 3) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.randColorPrimary));
        } else if (id == 4) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.funnyColorPrimary));
        } else if (id == 5) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.startColorPrimary));
        }

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
            quote = (TextView) findViewById(R.id.quote);
            author = (TextView) findViewById(R.id.author);

            if (id == 1) {
                quote.setTextColor(getResources().getColor(R.color.progColorAccent));
                author.setTextColor(getResources().getColor(R.color.progColorAccent));
            } else if (id == 2) {
                quote.setTextColor(getResources().getColor(R.color.motiColorAccent));
                author.setTextColor(getResources().getColor(R.color.motiColorAccent));
            } else if (id == 3) {
                quote.setTextColor(getResources().getColor(R.color.randColorAccent));
                author.setTextColor(getResources().getColor(R.color.randColorAccent));
            } else if (id == 4) {
                quote.setTextColor(getResources().getColor(R.color.funnyColorAccent));
                author.setTextColor(getResources().getColor(R.color.funnyColorAccent));
            } else if (id == 5) {
                quote.setTextColor(getResources().getColor(R.color.startColorAccent));
                author.setTextColor(getResources().getColor(R.color.startColorAccent));
            }

            quote.setText("\"" + quoteString + "\"");
            author.setText(authorString);
        }
    }
}