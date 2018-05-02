package com.test.shamzic.applitp1;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>>{
    JSONObject jObject; // Invoked by execute() method of this object
    int MODE;
    CommerceActivity Act;

    ArrayList<MarkerOptions> Options = new ArrayList<MarkerOptions>();

    public ParserTask(CommerceActivity cont, int mode) {
        Act = cont;
        MODE = mode;
    }

    @Override
    protected List<HashMap<String, String>> doInBackground(String... jsonData) {
        List<HashMap<String, String>> places = null;
        Place_JSON placeJson = new Place_JSON();
        try {
            jObject = new JSONObject(jsonData[0]);
            places = placeJson.parse(jObject);
        }
        catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        //Log.d("DONNE", places.toString());
        return places;
    }

    // Executed after the complete execution of doInBackground() method
    protected void onPostExecute(List<HashMap<String, String>> list) {
        Log.d("Map", "list size: " + list.size());

        // Clears all the existing markers;
        for (int i = 0; i < list.size(); i++) {
            // Creating a marker
            MarkerOptions markerOptions = new MarkerOptions(); // Getting a place from the places list

            HashMap<String, String> hmPlace = list.get(i); // Getting latitude of the place

            // Getting latitude of the place
            double lat = Double.parseDouble(hmPlace.get("lat"));
            // Getting longitude of the place
            double lng = Double.parseDouble(hmPlace.get("lng"));
            // Getting name
            String name = hmPlace.get("place_name"); Log.d("Map", "place: " + name);
            // Getting vicinity
            String vicinity = hmPlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            // Setting the position for the marker
            markerOptions.position(latLng);
            markerOptions.title(name + " : " + vicinity);
            if (MODE == 1) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            }
            else {
                if (MODE == 3){
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }

            }
            //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            Options.add(markerOptions);

            Act.mMap.addMarker(Options.get(i));
        }

    }

}
