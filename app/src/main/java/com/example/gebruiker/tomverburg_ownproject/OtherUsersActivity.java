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
 * OtherUsersActivity.java
 * TomVerburg-OwnProject
 *
 * Activity which shows all the other users which use this application. By clicking
 * on a specific user, you will get to see their search history in HistoryActivity.
 */

public class OtherUsersActivity extends BaseActivity {
    private ListView theListView;
    private ArrayList<String> listUid;
    private ListAdapter theAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_users);

        //List view.
        theListView = (ListView)findViewById(R.id.otherUsersListView);

        //Firebase database and reference for getting the user name and user id.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("username");

        myRef.addValueEventListener(new ValueEventListener() {
            //Database listener which fires when the database changes and saves the
            // names and user id of all the users of the application in two array lists.
            // The array list of the names is then entered into the list view.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> listName = new ArrayList<>();
                listUid = new ArrayList<>();

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String name = child.getValue().toString();
                    String uid = child.getKey();
                    listName.add(name);
                    listUid.add(uid);
                }
                theAdapter = adapter(listName);
                theListView.setAdapter(theAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        clickSelectOtherUserHistory();
    }

    public void clickSelectOtherUserHistory() {
        //Starts HistoryActivity and adds the user id and name of the selected user.
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String userName = String.valueOf(adapterView.getItemAtPosition(position));

                Intent getNameScreen = new Intent(getApplicationContext(),HistoryActivity.class);
                getNameScreen.putExtra("userUid", listUid.get(position));
                getNameScreen.putExtra("userName", userName);
                startActivity(getNameScreen);
                finish();
            }
        });
    }
}
