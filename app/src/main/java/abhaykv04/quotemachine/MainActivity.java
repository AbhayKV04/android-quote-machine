package abhaykv04.quotemachine;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ImageButton funnyButton, motiButton, progButton, randButton, startButton;
    private int themeId = 1;
    private String first = "Chuck", last = "Norris";
    int logid=1;
    private  FirebaseAuth.AuthStateListener mauthlist;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));

        /**
         * Intent ID Guide
         * 1 - Programming
         * 2 - Motivational
         * 3 - Famous
         * 4 - Funny
         * 5 - Start-Up
         */
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null)
        {logid=0;}
        else{
            logid=1;
        }
        final Intent intent = new Intent(MainActivity.this, QuoteActivity.class);

        progButton = (ImageButton) findViewById(R.id.progButton);
        progButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("id", 1);
                intent.putExtra("themeId", themeId);
                intent.putExtra("url", "http://quotes.stormconsultancy.co.uk/random.json");
                startActivity(intent);
            }
        });

        motiButton = (ImageButton) findViewById(R.id.motiButton);
        motiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("id", 2);
                intent.putExtra("themeId", themeId);
                intent.putExtra("url", "https://apimk.com/motivationalquotes?get_quote=yes");
                startActivity(intent);
            }
        });

        randButton = (ImageButton) findViewById(R.id.randButton);
        randButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("id", 3);
                intent.putExtra("themeId", themeId);
                intent.putExtra("url", "https://talaikis.com/api/quotes/random");
                startActivity(intent);
            }
        });

        funnyButton = (ImageButton) findViewById(R.id.funnyButton);
        funnyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("id", 4);
                intent.putExtra("themeId", themeId);
                intent.putExtra("url", "http://api.icndb.com/jokes/random?firstName=" + first + "&lastName=" + last + "&limitTo=[nerdy]&escape=javascript");
                startActivity(intent);
            }
        });

        startButton = (ImageButton) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("id", 5);
                intent.putExtra("themeId", themeId);
                intent.putExtra("url", "https://wisdomapi.herokuapp.com/v1/random");
                startActivity(intent);
            }
        });

        // Set color filter (tint) on image buttons
        funnyButton.setColorFilter(Color.argb(31, 38, 50, 56));
        motiButton.setColorFilter(Color.argb(31, 38, 50, 56));
        progButton.setColorFilter(Color.argb(31, 38, 50, 56));
        randButton.setColorFilter(Color.argb(31, 38, 50, 56));
        startButton.setColorFilter(Color.argb(31, 38, 50, 56));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (themeId == 0) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
        } else if (themeId == 1) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
        }
        if (logid == 0) {
            menu.getItem(3).setVisible(false);
            menu.getItem(4).setVisible(true);
            menu.getItem(5).setVisible(false);
        } else if (logid == 1) {
            menu.getItem(3).setVisible(true);
            menu.getItem(4).setVisible(false);
            menu.getItem(5).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.lightTheme:
                themeId = 0;
                getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                funnyButton.clearColorFilter();
                motiButton.clearColorFilter();
                progButton.clearColorFilter();
                randButton.clearColorFilter();
                startButton.clearColorFilter();
                return true;

            case R.id.darkTheme:
                themeId = 1;
                getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                funnyButton.setColorFilter(Color.argb(31, 38, 50, 56));
                motiButton.setColorFilter(Color.argb(31, 38, 50, 56));
                progButton.setColorFilter(Color.argb(31, 38, 50, 56));
                randButton.setColorFilter(Color.argb(31, 38, 50, 56));
                startButton.setColorFilter(Color.argb(31, 38, 50, 56));
                return true;

            case R.id.tweak:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AppTheme_Dark);
                builder.setTitle("Funny Quotes Tweak");
                builder.setMessage("You can replace the default name in funny quotes with yours!");

                // Set up the input
                final EditText firstName = new EditText(MainActivity.this);
                final EditText lastName = new EditText(MainActivity.this);

                // Set up layout
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                // Specify the type and color of input
                firstName.setInputType(InputType.TYPE_CLASS_TEXT);
                firstName.setHint("First Name");
                firstName.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                firstName.setHintTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorHint));

                lastName.setInputType(InputType.TYPE_CLASS_TEXT);
                lastName.setHint("Last Name");
                lastName.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                lastName.setHintTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorHint));

                // add fields to layout
                layout.addView(firstName);
                layout.addView(lastName);

                // set layout view in dialog
                builder.setView(layout);

                // Set up the buttons
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (firstName.getText().toString().isEmpty() && lastName.getText().toString().isEmpty()) {
                            first = "Chuck";
                            last = "Norris";
                        } else {
                            first = firstName.getText().toString();
                            last = lastName.getText().toString();
                        }
                    }
                });

                builder.setNeutralButton("Default", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        first = "Chuck";
                        last = "Norris";
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.cancel();
                        }
                    }
                });

                builder.show();
                return true;

            case R.id.exit:
                finish();
                return true;
            case R.id.sin:
                Intent intent = new Intent(MainActivity.this,SignIn.class);
                startActivity(intent);
                return true;
            case R.id.sout:
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

               /* mauthlist = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {*/
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if(user==null){
                            Toast.makeText(MainActivity.this,"Sign-Out",Toast.LENGTH_LONG).show();
                            logid=0;
                           /* Intent i= new Intent(MainActivity.this,MainActivity.class);
                            startActivity(i);
                            finish();*/

                        }else
                        {Toast.makeText(MainActivity.this,"Couldn't Sign-Out",Toast.LENGTH_LONG).show();
                            logid=1;
                            }


                return true;
            case R.id.fav:
                Intent i= new Intent(MainActivity.this,FavouritesActivity.class);
                startActivity(i);
                finish();
                return  true;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
