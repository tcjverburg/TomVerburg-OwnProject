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

        findViewById(R.id.search_nav).setOnClickListener(this);
        findViewById(R.id.history_nav).setOnClickListener(this);
        findViewById(R.id.favorites_nav).setOnClickListener(this);
        findViewById(R.id.other_users_button).setOnClickListener(this);
        theListView = (ListView)findViewById(R.id.historyListView);
        TextView textViewHistoryId = (TextView)findViewById(R.id.history_id);
        TextView textViewUserName = (TextView)findViewById(R.id.user_name);
        Button Nav = (Button)findViewById(R.id.history_nav);

        Intent activityThatCalled = getIntent();
        final String userUid = activityThatCalled.getExtras().getString("userUid");
        final String userName = activityThatCalled.getExtras().getString("userName");

        int myColor = getResources().getColor(R.color.colorButtonPressed);
        Nav.setBackgroundColor(myColor);
        textViewHistoryId.setText(R.string.history_id);
        textViewUserName.setText(userName);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        assert userUid != null;
        myRef = database.getReference("users").child(userUid).child("search_history");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> list = new ArrayList<>();

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String oldQuery = child.getValue().toString();
                    list.add(oldQuery);
                }
                theAdapter = adapter(list);
                theListView.setAdapter(theAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        clickSelectOldQuery();
    }

    public void clickSelectOldQuery() {
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String oldQuery = String.valueOf(adapterView.getItemAtPosition(position));
                Intent getNameScreen = new Intent(getApplicationContext(),SearchResultActivity.class);
                myRef.child(oldQuery).setValue(oldQuery);
                oldQuery = oldQuery.replaceAll(" ", "%20");
                getNameScreen.putExtra("query", oldQuery);
                startActivity(getNameScreen);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.search_nav) {
            Intent getNameScreen = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(getNameScreen);
            finish();
        } else if (i == R.id.history_nav) {
            Intent getNameScreen = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(getNameScreen);
            finish();
        } else if (i == R.id.favorites_nav) {
            Intent getNameScreen = new Intent(getApplicationContext(), FavoritesActivity.class);
            startActivity(getNameScreen);
            finish();
        }else if (i == R.id.other_users_button) {
            Intent getNameScreen = new Intent(getApplicationContext(),OtherUsersActivity.class);
            startActivity(getNameScreen);
        }
    }
}
