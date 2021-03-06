package com.test.shamzic.applitp1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class CommerceActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPoiClickListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, Callback {
    GoogleMap mMap;
    ArrayList<MarkerOptions> OPTIONS = new ArrayList<MarkerOptions>();
    int MODE = 3;
    String TYPE = "stadium";
    AdView mAdView;


    private double Lat;
    private double Lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ///////////////////////////
        //PubCarte.initialize(this, "ca-app-pub-4810075982521358~7575173313");
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-4810075982521358/2622229532");
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        /*
        IL est nécessaire d'officialiser notre appli sur le playstore avant de pouvoir utiliser l'ID ci-dessous
         */
        //MobileAds.initialize(this, "ca-app-pub-4810075982521358~7575173313");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        //////////////////////
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED /*&& ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED*/) {
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

        StringBuilder sbValue = new StringBuilder(sbMethod());
        PlacesTask placesTask = new PlacesTask(this, MODE);
        placesTask.execute(sbValue.toString());

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public StringBuilder sbMethod() { //use your current location here double mLatitude = 37.77657;
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + Lat + "," + Lon);

        sb.append("&rankby=distance");

        if (MODE == 1) {
            sb.append("&types=" + TYPE);//stadium//gym//spa
        }
        else if (MODE == 2) {
            //sb.append("&types=" + "park" + "stadium" + " gym" + "bicycle_store" + "shoe_store");
            //sb.append("&types=" + "restaurant"+"|"+"Park");
            sb.append("&keyword=" + "sports");
            sb.append("&types=" + "store");
        }
        else if (MODE == 3) {
            sb.append("&keyword=" + "parc");
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
        TYPE = "stadium";
        onMapReady(mMap);
        TYPE = "gym";
        onMapReady(mMap);
        TYPE = "spa";
        onMapReady(mMap);
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
