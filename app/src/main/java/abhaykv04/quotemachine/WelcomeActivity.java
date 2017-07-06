package abhaykv04.quotemachine;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    private static int SPLASH_TIMEOUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent welcomeIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(welcomeIntent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
