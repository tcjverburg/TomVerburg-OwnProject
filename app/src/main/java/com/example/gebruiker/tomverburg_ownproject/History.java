package com.example.gebruiker.tomverburg_ownproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Gebruiker on 20-10-2016.
 */

public class History extends Activity {
    private ListView theListView;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        theListView = (ListView)findViewById(R.id.historyListView);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(user.getUid()).child("search_history");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String oldQuery = child.getValue().toString();
                    list.add(oldQuery);
                }
                adapter(list);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        clickSelectOldQuery();
    }
    public void adapter(ArrayList<String> historyTitles){
        ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, historyTitles);
        theListView.setAdapter(theAdapter);
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
