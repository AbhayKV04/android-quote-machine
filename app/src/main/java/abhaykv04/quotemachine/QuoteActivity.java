package abhaykv04.quotemachine;

import android.app.ProgressDialog;
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
    private String quoteString = "Check your internet connection!";
    private String authorString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

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
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.famousColorPrimary));
        } else if (id == 4) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.funnyColorPrimary));
        } else if (id == 5) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.startColorPrimary));
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
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
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
                quote.setTextColor(getResources().getColor(R.color.famousColorAccent));
                author.setTextColor(getResources().getColor(R.color.famousColorAccent));
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