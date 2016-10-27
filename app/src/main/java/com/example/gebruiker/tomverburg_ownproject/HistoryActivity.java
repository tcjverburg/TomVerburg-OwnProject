package com.example.gebruiker.tomverburg_ownproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * HistoryActivity.java
 * TomVerburg-OwnProject
 *
 * Activity which shows your previous searches or the searches of another user whom
 * you can select in the OtherUserActivity.
 **/

public class HistoryActivity extends BaseActivity implements View.OnClickListener{
    private ListView theListView;
    private DatabaseReference myRef;
    private ListAdapter theAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Intent previous activity.
        Intent activityThatCalled = getIntent();
        final String userUid = activityThatCalled.getExtras().getString("userUid");
        final String userName = activityThatCalled.getExtras().getString("userName");

        //Buttons.
        findViewById(R.id.search_nav).setOnClickListener(this);
        findViewById(R.id.history_nav).setOnClickListener(this);
        findViewById(R.id.favorites_nav).setOnClickListener(this);
        findViewById(R.id.other_users_button).setOnClickListener(this);
        Button Nav = (Button)findViewById(R.id.history_nav);

        //List and text views.
        theListView = (ListView)findViewById(R.id.historyListView);
        TextView textViewUserName = (TextView)findViewById(R.id.user_name);

        //Sets the color of the navigation button of current activity.
        int myColor = getResources().getColor(R.color.colorButtonPressed);
        Nav.setBackgroundColor(myColor);

        //Sets text for text view.
        textViewUserName.setText(userName);

        //Firebase database, database reference and authentication.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        assert userUid != null;
        myRef = database.getReference("users").child(userUid).child("search_history");

        myRef.addValueEventListener(new ValueEventListener() {

            //Database listener which fires when the database changes and updates
            // the list view.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> oldSearchTerms = new ArrayList<>();

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String oldSearchTerm = child.getValue().toString();
                    oldSearchTerms.add(oldSearchTerm);
                }
                theAdapter = adapter(oldSearchTerms);
                theListView.setAdapter(theAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        clickSelectOldSearchTerm();
    }

    public void clickSelectOldSearchTerm() {
        //Starts SearchResultActivity after clicking a previous search term in the list view.
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String oldSearchTerm = String.valueOf(adapterView.getItemAtPosition(position));
                myRef.child(oldSearchTerm).setValue(oldSearchTerm);
                oldSearchTerm = oldSearchTerm.replaceAll(" ", "%20");

                Intent getNameScreen = new Intent(getApplicationContext(),SearchResultActivity.class);
                getNameScreen.putExtra("search", oldSearchTerm);
                startActivity(getNameScreen);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        //On click method for the navigation bar and other buttons.
        int i = v.getId();
        if (i == R.id.search_nav) {
            Intent getNameScreen = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(getNameScreen);
            finish();
        }
        else if (i == R.id.favorites_nav) {
            Intent getNameScreen = new Intent(getApplicationContext(), FavoritesActivity.class);
            startActivity(getNameScreen);
            finish();
        }
        else if (i == R.id.other_users_button) {
            Intent getNameScreen = new Intent(getApplicationContext(),OtherUsersActivity.class);
            startActivity(getNameScreen);
        }
    }
}
