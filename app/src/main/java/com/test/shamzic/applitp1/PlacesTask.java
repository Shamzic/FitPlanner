package com.test.shamzic.applitp1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.os.AsyncTask.Status.RUNNING;

public class PlacesTask extends AsyncTask<String, Integer, String> {
    CommerceActivity Act;
    int MODE;
    ArrayList<MarkerOptions> Options;
    String data = null;

    public PlacesTask(CommerceActivity commerceActivity, int mode) {
       Act = commerceActivity;
       MODE = mode;
    }

    // Invoked by execute() method of this object
    @Override
    protected String doInBackground(String... url) {
        try {
            data = downloadUrl(url[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }
    // Executed after the complete execution of doInBackground() method
    @Override
    protected void onPostExecute(String result) {
        ParserTask parserTask = new ParserTask(Act, MODE);
        // Start parsing the Google places in JSON format
        // Invokes the "doInBackground()" method of the class
        parserTask.execute(result);
    }

    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try { URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString(); br.close();
        }

        catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
