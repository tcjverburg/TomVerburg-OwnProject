package com.example.gebruiker.tomverburg_ownproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * HistoryAc.java
 * TomVerburg-OwnProject
 *
 * Activity which shows all the other users which use this application. By clicking
 * on a specific user, you will get to see their search history.
 */

public class HistoryActivity extends BaseActivity implements View.OnClickListener{
    private ListView theListView;
    private DatabaseReference myRef;
    private ListAdapter theAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        findViewById(R.id.search_nav).setOnClickListener(this);
        findViewById(R.id.history_nav).setOnClickListener(this);
        findViewById(R.id.favorites_nav).setOnClickListener(this);

        Button Nav = (Button)findViewById(R.id.history_nav);
        int myColor = getResources().getColor(R.color.colorButtonPressed);
        Nav.setBackgroundColor(myColor);

        theListView = (ListView)findViewById(R.id.historyListView);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        myRef = database.getReference("users").child(user.getUid()).child("search_history");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList();
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
        } else if (i == R.id.history_nav) {
            Intent getNameScreen = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(getNameScreen);
        } else if (i == R.id.favorites_nav) {
            Intent getNameScreen = new Intent(getApplicationContext(), FavoritesActivity.class);
            startActivity(getNameScreen);
        }
    }
}
