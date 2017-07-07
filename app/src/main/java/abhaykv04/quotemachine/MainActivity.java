package abhaykv04.quotemachine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton progButton, motiButton, famousButton, funnyButton, startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Intent ID Guide
         * 1 - Programming
         * 2 - Motivational
         * 3 - Famous
         * 4 - Funny
         * 5 - Start-Up
         */

        progButton = (ImageButton) findViewById(R.id.progButton);
        progButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuoteActivity.class);
                intent.putExtra("id", 1);
                intent.putExtra("url", "http://quotes.stormconsultancy.co.uk/random.json");
                startActivity(intent);
            }
        });

        motiButton = (ImageButton) findViewById(R.id.motiButton);
        motiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuoteActivity.class);
                intent.putExtra("id", 2);
                intent.putExtra("url", "https://apimk.com/motivationalquotes?get_quote=yes");
                startActivity(intent);
            }
        });

        famousButton = (ImageButton) findViewById(R.id.famousButton);
        famousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuoteActivity.class);
                intent.putExtra("id", 3);
                intent.putExtra("url", "https://talaikis.com/api/quotes/random/");
                startActivity(intent);
            }
        });

        funnyButton = (ImageButton) findViewById(R.id.funnyButton);
        funnyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuoteActivity.class);
                intent.putExtra("id", 4);
                intent.putExtra("url", "http://api.icndb.com/jokes/random?firstName=Rajinikanth&lastName=&limitTo=[nerdy]");
                startActivity(intent);
            }
        });

        startButton = (ImageButton) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuoteActivity.class);
                intent.putExtra("id", 5);
                intent.putExtra("url", "https://wisdomapi.herokuapp.com/v1/random");
                startActivity(intent);
            }
        });
    }
}
