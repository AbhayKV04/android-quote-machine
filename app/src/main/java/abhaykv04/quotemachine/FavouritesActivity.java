package abhaykv04.quotemachine;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FavouritesActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    private String userID;
    private ArrayList<String> arrayList = new ArrayList<>();

    private ListView mListView;
    private HashMap<String, String> hmq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        mListView = (ListView) findViewById(R.id.fav);

        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be usable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        myRef = mFirebaseDatabase.getReference(userID);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(FavouritesActivity.this, android.R.layout.simple_list_item_1, arrayList);
        mListView.setAdapter(arrayAdapter);

        hmq = new HashMap<>();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    // User is signed in
                    Toast.makeText(FavouritesActivity.this, "Successfully signed in with: " + user.getEmail(), Toast.LENGTH_LONG).show();
                } else {

                    // User is signed out
                    Toast.makeText(FavouritesActivity.this, "Successfully signed out with: " + user.getEmail(), Toast.LENGTH_LONG).show();
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {

            //Work Here
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                int qmax = Integer.parseInt(dataSnapshot.child("i").getValue().toString());
                List<HashMap<String, String>> listitems = new ArrayList<>();
                SimpleAdapter adapter = new SimpleAdapter(FavouritesActivity.this, listitems, R.layout.list_item, new String[]{"First", "Second"}, new int[]{R.id.tv1, R.id.tv2});
                for (int i = 0; i < qmax; i++) {
                    String quote = dataSnapshot.child("Quote" + i).child("Quote").getValue(String.class);
                    String author = dataSnapshot.child("Quote" + i).child("Author").getValue(String.class);

                    //  arrayList.add(author);
                    hmq.put(quote, author);
                }

                Iterator it = hmq.entrySet().iterator();
                while (it.hasNext()) {
                    HashMap<String, String> resmap = new HashMap<String, String>();
                    Map.Entry pair = (Map.Entry) it.next();
                    resmap.put("First", pair.getKey().toString());
                    resmap.put("Second", pair.getValue().toString());
                    listitems.add(resmap);
                }

                mListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}