package com.example.gebruiker.tomverburg_ownproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Gebruiker on 20-10-2016.
 */

public class Favorites extends Activity {
    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<String> urls = new ArrayList<String>();
    private SharedPreferences pref;
    private ListView theListView;
    private ListAdapter theAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        theListView = (ListView)findViewById(R.id.favoriteListView);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        reader();
        adapter();
        clickDeleteItem();
    }

    public void reader(){
        titles.clear();
        urls.clear();
        Map<String, ?> allEntries = pref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            titles.add((entry.getKey()));
            urls.add(entry.getValue().toString());}
    }

    public void adapter(){
        theAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,  titles);
        theListView.setAdapter(theAdapter);
    }

    public void clickDeleteItem() {
        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String title = String.valueOf(adapterView.getItemAtPosition(position));
                SharedPreferences.Editor editor = pref.edit();
                editor.remove(title);
                editor.commit();
                Toast.makeText(Favorites.this, "You have removed " + title + " from your favorites", Toast.LENGTH_SHORT).show();
                reader();
                adapter();
                return true;
            }
        });
    }

}
