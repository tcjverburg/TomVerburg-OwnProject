package com.example.gebruiker.tomverburg_ownproject;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Gebruiker on 25-10-2016.
 */

public class MyAsyncTask extends AsyncTask<String, String, String> {

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

    //Source:http://stackoverflow.com/questions/26202568/android-pass-function-reference-to-asynctask
    public interface TaskListener {
        public void onFinished(String result);
    }

    // This is the reference to the associated listener
    private final TaskListener taskListener;

    public MyAsyncTask(TaskListener listener) {
        // The listener reference is passed in through the constructor
        this.taskListener = listener;
    }

    //filters the string we got as a result of the method getting the information from the api
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // In onPostExecute we check if the listener is valid
        if(this.taskListener != null) {

            // And if it is we call the callback function on it.
            this.taskListener.onFinished(result);
        }
    }
}
