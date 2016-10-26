package com.example.gebruiker.tomverburg_ownproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

/**
 * FavoritesActivity.java
 * TomVerburg-OwnProject
 *
 * Activity which shows all the favorite articles you have added. By selecting them,
 * you can read them again.
 */

public class FavoritesActivity extends BaseActivity {
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
        theAdapter = adapter(titles);
        theListView.setAdapter(theAdapter);
        clickDeleteItem();
        clickSelectFavorite();
    }

    public void reader(){
        titles.clear();
        urls.clear();
        Map<String, ?> allEntries = pref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            titles.add((entry.getKey()));
            urls.add(entry.getValue().toString());}
    }



    public void clickDeleteItem() {
        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String title = String.valueOf(adapterView.getItemAtPosition(position));
                SharedPreferences.Editor editor = pref.edit();
                editor.remove(title);
                editor.commit();
                Toast.makeText(FavoritesActivity.this, "You have removed " + title + " from your favorites", Toast.LENGTH_SHORT).show();
                reader();
                theListView.setAdapter(theAdapter);
                return true;
            }
        });
    }

    public void clickSelectFavorite() {
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                goToUrl(urls.get(position));
            }
        });
    }



}
