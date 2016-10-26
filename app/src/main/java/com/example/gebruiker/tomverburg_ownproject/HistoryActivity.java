package com.example.gebruiker.tomverburg_ownproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class HistoryActivity extends BaseActivity {
    private ListView theListView;
    private DatabaseReference myRef;
    private ListAdapter theAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        theListView = (ListView)findViewById(R.id.historyListView);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Intent activityThatCalled = getIntent();
        String userUid = activityThatCalled.getExtras().getString("userUid");
        myRef = database.getReference("users").child(userUid).child("search_history");

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
                Intent getNameScreen = new Intent(getApplicationContext(),SearchListActivity.class);
                myRef.child(oldQuery).setValue(oldQuery);
                oldQuery = oldQuery.replaceAll(" ", "%20");
                getNameScreen.putExtra("query", oldQuery);
                startActivity(getNameScreen);
                finish();
            }
        });
    }
}
