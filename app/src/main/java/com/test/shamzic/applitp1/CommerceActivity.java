package com.test.shamzic.applitp1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.places.Places;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.internal.PlaceEntity;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.location.places.Place;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static android.os.AsyncTask.Status.FINISHED;
import static android.os.AsyncTask.Status.RUNNING;


public class CommerceActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPoiClickListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, Callback {
    GoogleMap mMap;
    ArrayList<MarkerOptions> OPTIONS = new ArrayList<MarkerOptions>();
    int MODE = 0;


    /*private Button requestButton;
    private TextView resultsTextView;
    private Snackbar snackbar;
    private LinearLayout linearLayout;*/

    //private final OkHttpClient client = new OkHttpClient();

    private double Lat;
    private double Lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ArrayList<LocationProvider> providers = new ArrayList<LocationProvider>();
        ArrayList<String> names = (ArrayList) locationManager.getProviders(true);

        for (String name : names) {
            providers.add(locationManager.getProvider(name));
        }

        Criteria critere = new Criteria();
        // Pour indiquer la précision voulue
        // On peut mettre ACCURACY_FINE pour une haute précision ou ACCURACY_COARSE pour une moins bonne précision
        critere.setAccuracy(Criteria.ACCURACY_COARSE);// Est-ce que le fournisseur doit être capable de donner une altitude ?
        critere.setAltitudeRequired(false);// Est-ce que le fournisseur doit être capable de donner une direction ?
        critere.setBearingRequired(true);// Est-ce que le fournisseur peut être payant ?
        critere.setCostAllowed(false);// Pour indiquer la consommation d'énergie demandée

        // Criteria.POWER_HIGH pour une haute consommation, Criteria.POWER_MEDIUM pour une consommation moyenne et Criteria.POWER_LOW pour une basse consommation
        critere.setPowerRequirement(Criteria.POWER_MEDIUM);
        critere.setSpeedRequired(false);// Est-ce que le fournisseur doit être capable de donner une vitesse ?

        String Best = locationManager.getBestProvider(critere, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location L = locationManager.getLastKnownLocation(Best);
        Lat = L.getLatitude();
        Lon = L.getLongitude();

        // Get the SupportMapFragment and register for the callback
        // when the map is ready for use.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 90000, 60, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnPoiClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else {
            // Show rationale and request permission.
        }
        LatLng Position = new LatLng(Lat, Lon);
        float zoomLevel = 15.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Position, zoomLevel));

        // Customise the styling of the base map using a JSON object defined
        // in a string resource file. First create a MapStyleOptions object
        // from the JSON styles string, then pass this to the setMapStyle
        // method of the GoogleMap object.
        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));

        if (!success) {
            Log.e("JSON", "Style parsing failed.");
        }
        Log.d("Coucout", "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        StringBuilder sbValue = new StringBuilder(sbMethod());
        PlacesTask placesTask = new PlacesTask(this);
        placesTask.execute(sbValue.toString());

        if (placesTask.getStatus() == FINISHED){
            /*Log.d("Coucout","IL A FIIIIIIIIIIIIIIIIIINNNNNNNNNNNNNNNNNNNNNIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
            try {
                Log.d("Coucout", placesTask.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }*/
        }


        //Log.d("url qu'on renvoie", sbValue.toString());


        //String A = placesTask.doInBackground(sbValue.toString());
        //placesTask.onPostExecute(A);


        /*for (int i = 0; i < I;i++ ){
            mMap.addMarker(placesTask.Options.get(i));

        }*/
        //PlacesTask

        /*mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Lat, Lon))
                .title("Hello world"));*/

        // Add a marker in Sydney and move the camera

        //LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(Position).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Position));

    }

    /*@Override
    public void onPoiClick(PointOfInterest poi) {
        Toast.makeText(getApplicationContext(), "Clicked: " +
                        poi.name + "\nPlace ID:" + poi.placeId +
                        "\nLatitude:" + poi.latLng.latitude +
                        " Longitude:" + poi.latLng.longitude,
                Toast.LENGTH_SHORT).show();

    }*/


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    /*private void makeView() {
        requestButton = new Button(this);
        requestButton.setText("Lancer une requête");
        requestButton.setOnClickListener(this);

        resultsTextView = new TextView(this);

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(requestButton);
        linearLayout.addView(resultsTextView);
    }*/

    public StringBuilder sbMethod() { //use your current location here double mLatitude = 37.77657;
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + Lat + "," + Lon);

        sb.append("&rankby=distance");
        //sb.append("&keyword=park");
        if (MODE == 0) {
            //sb.append("&types=" + "park" + "stadium" + " gym" + "bicycle_store" + "shoe_store");
            //sb.append("&types=" + "restaurant"+"|"+"Park");
            sb.append("&types=" + "shoe_store"+"|"+"park");
        }
        if (MODE == 1) {
            //sb.append("&types=" + "park" + "stadium" + " gym" + "bicycle_store" + "shoe_store");
            //sb.append("&types=" + "restaurant"+"|"+"Park");
            sb.append("&types=" + "stadium");
        }
        if (MODE == 2) {
            //sb.append("&types=" + "park" + "stadium" + " gym" + "bicycle_store" + "shoe_store");
            //sb.append("&types=" + "restaurant"+"|"+"Park");
            sb.append("&types=" + "shoe_store");
        }
        if (MODE == 2) {
            //sb.append("&types=" + "park" + "stadium" + " gym" + "bicycle_store" + "shoe_store");
            //sb.append("&types=" + "restaurant"+"|"+"Park");
            sb.append("&types=" + "park");
        }
        //sb.append("&sensor=true");
        sb.append("&key=AIzaSyADlRw1YSZ0iRmaVMA_t04UmGNyWvVkySs&Lat");
        Log.d("Map", "api: " + sb.toString());
        return sb;
        }

    public void clickSports(View v){
        MODE = 1;
        Log.d("TOUCHE", String.valueOf(MODE));
        mMap.clear();
        onMapReady(mMap);
        /*StringBuilder sbValue = new StringBuilder(sbMethod());
        PlacesTask placesTask = new PlacesTask(this);
        placesTask.execute(sbValue.toString());*/
    }
    public void clickCommerces(View v){
        MODE = 2;
        Log.d("TOUCHE", String.valueOf(MODE));
        mMap.clear();
        onMapReady(mMap);
        /*StringBuilder sbValue = new StringBuilder(sbMethod());
        PlacesTask placesTask = new PlacesTask(this);
        placesTask.execute(sbValue.toString());*/
    }
    public void clickParcs(View v){
        MODE = 3;
        Log.d("TOUCHE", String.valueOf(MODE));
        mMap.clear();
        onMapReady(mMap);
        /*StringBuilder sbValue = new StringBuilder(sbMethod());
        PlacesTask placesTask = new PlacesTask(this);
        placesTask.execute(sbValue.toString());*/
    }
    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

    }
}
