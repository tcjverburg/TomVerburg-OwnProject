package com.example.gebruiker.tomverburg_ownproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Map;

/**
 * FavoritesActivity.java
 * TomVerburg-OwnProject
 *
 * Activity which shows all the articles you have added to your favorites. By selecting them,
 * you can read them again.
 */

public class FavoritesActivity extends BaseActivity implements View.OnClickListener{
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();
    private SharedPreferences pref;
    private ListView theListView;
    private ListAdapter theAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        pref = getApplicationContext().getSharedPreferences("MyPref" + getUser().getUid(), 0);

        findViewById(R.id.search_nav).setOnClickListener(this);
        findViewById(R.id.history_nav).setOnClickListener(this);
        findViewById(R.id.favorites_nav).setOnClickListener(this);
        theListView = (ListView)findViewById(R.id.favoriteListView);

        Button Nav = (Button)findViewById(R.id.favorites_nav);
        int myColor = getResources().getColor(R.color.colorButtonPressed);
        Nav.setBackgroundColor(myColor);

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
                editor.apply();
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.search_nav) {
            Intent getNameScreen = new Intent(getApplicationContext(),SearchActivity.class);
            startActivity(getNameScreen);
            finish();
        }
        else if (i == R.id.history_nav) {
            FirebaseUser user = getUser();
            String userName = getUserName();
            Intent getNameScreen = new Intent(getApplicationContext(),HistoryActivity.class);
            getNameScreen.putExtra("userUid", user.getUid());
            getNameScreen.putExtra("userName", userName);
            startActivity(getNameScreen);
            finish();
        }

        else if (i == R.id.favorites_nav) {
            Intent getNameScreen = new Intent(getApplicationContext(),FavoritesActivity.class);
            startActivity(getNameScreen);
            finish();
        }
    }

}
