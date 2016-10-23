package com.example.gebruiker.tomverburg_ownproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * OtherUsersActivity.java
 * TomVerburg-OwnProject
 *
 * Activity which shows all the other users which use this application. By clicking
 * on a specific user, you will get to see their search history.
 */

public class OtherUsersActivity extends Activity {
    private ListView theListView;
    private DatabaseReference myRef;
    private ArrayList<String> listUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_users);

        theListView = (ListView)findViewById(R.id.otherUsersListView);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("email");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> listName = new ArrayList();
                listUid = new ArrayList();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String name = child.getKey();
                    String uid = child.getValue().toString();
                    listName.add(name);
                    listUid.add(uid);
                }
                adapter(listName);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        clickSelectOtherUserHistory();
    }
    public void adapter(ArrayList<String> historyTitles){
        ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, historyTitles);
        theListView.setAdapter(theAdapter);
    }
    public void clickSelectOtherUserHistory() {
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent getNameScreen = new Intent(getApplicationContext(),HistoryActivity.class);
                getNameScreen.putExtra("userUid", listUid.get(position));
                startActivity(getNameScreen);
                finish();
            }
        });
    }
}
