package com.example.gebruiker.tomverburg_ownproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Gebruiker on 19-10-2016.
 */

public class SearchListActivity extends Activity {
    private ArrayList<String> articles = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();
    private ListAdapter theAdapter;
    private ListView theListView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        Intent activityThatCalled = getIntent();
        String query = activityThatCalled.getExtras().getString("query");
        url = "https://content.guardianapis.com/search?q=" + query + "&api-key=828ceb77-f98a-4d04-9912-9a626d996386";
        new JSONTask().execute(url);
        theListView = (ListView) findViewById(R.id.searchListView);
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                goToUrl(urls.get(position));
            }
        });
        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String title = String.valueOf(adapterView.getItemAtPosition(position));
                String url = urls.get(position);
                persistence(title, url);
                return true;
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            //tries to make connection if possible
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                //adds line by line to the buffer from the api
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        //filters the string we got as a result of the method getting the information from the api
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String[] parts = result.split("\"results\":");
            String json = parts[1];
            try {
                JSONArray obj = new JSONArray(json);
                for (int i = 0; i < obj.length(); i++) {
                    JSONObject c = obj.getJSONObject(i);
                    String title = c.getString("webTitle");
                    String url = c.getString("webUrl");
                    articles.add(title);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter();
        }
    }

    public void adapter() {
        theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, articles);
        theListView.setAdapter(theAdapter);
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public void persistence(String title, String url) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(title, url);
        editor.commit();
        Toast.makeText(SearchListActivity.this, "You have added " + title + " to your favourites!", Toast.LENGTH_SHORT).show();
    }
}



