package com.example.gebruiker.tomverburg_ownproject;

import android.app.Activity;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gebruiker on 20-10-2016.
 */

public class History extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference myRef;
    private ListView theListView;
    private ListAdapter theAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        theListView = (ListView)findViewById(R.id.historyListView);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(user.getUid()).child("search_history");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String x = "1";
                Map<String,String> map = new HashMap<String, String>();
                ArrayList<String> list = new ArrayList();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String name = child.getValue().toString();
                    System.out.println(name);
                    map.put(x, name);
                    list.add(name);
                    x=x+1;
                }
                adapter(list);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void adapter(ArrayList<String> historyTitles){
        theAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,  historyTitles);
        theListView.setAdapter(theAdapter);
    }
}
