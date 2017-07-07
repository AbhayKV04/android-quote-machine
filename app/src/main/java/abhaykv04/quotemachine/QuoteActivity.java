package abhaykv04.quotemachine;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuoteActivity extends AppCompatActivity {

    private String TAG = QuoteActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private TextView quote, author;
    private String quoteString, authorString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("id");

        /**
         * Set different background color for different categories
         */
        if (id == 1) {
            getWindow().getDecorView().setBackgroundColor(Color.parseColor("#8bc34a"));
        }

        new GetQuote().execute();
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
            // Showing progress dialog
            pDialog = new ProgressDialog(QuoteActivity.this);
            pDialog.setMessage("Fetching");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    /**
                     * See 'Intent ID Guide' in MainActivity
                     */
                    if (id == 1) {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        quoteString = jsonObject.getString("quote");
                        authorString = jsonObject.getString("author");
                    } else if (id == 2) {
                        JSONArray jsonArray = new JSONArray(jsonStr);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        quoteString = jsonObject.getString("quote");
                        authorString = jsonObject.getString("author_name");
                    } else if (id == 3) {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        quoteString = jsonObject.getString("quote");
                        authorString = jsonObject.getString("author");
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
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into TextView
             * */
            quote = (TextView) findViewById(R.id.quote);
            author = (TextView) findViewById(R.id.author);

            if (id == 1) {
                quote.setTextColor(Color.parseColor("#ffffff"));
                author.setTextColor(Color.parseColor("#ffffff"));
            }

            quote.setText(quoteString);
            author.setText(authorString);
        }
    }
}