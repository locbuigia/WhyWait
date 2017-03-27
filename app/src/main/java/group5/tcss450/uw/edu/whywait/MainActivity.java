package group5.tcss450.uw.edu.whywait;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String Key_Search = "name";

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;

    private double longitude, latitude;

    private Marker mCurrLocationMarker;

    private static final String TAG = "LocationsActivity";
    private static final String KEY_DB = "DB";

    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    String place;
    String name;
    public static int caseToParse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        place = getIntent().getStringExtra("type");
        name = getIntent().getStringExtra("name");


    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 4000);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyCfUf81B45d045Yf-9PiCtlF7RXQN9tr7I");
        caseToParse = 1;
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    private boolean saveToSqlite(String name) {
        SearchDB courseDB = (SearchDB) getIntent().getSerializableExtra(KEY_DB);

        if (courseDB == null) {
            courseDB = new SearchDB(this);
        }
        return courseDB.insertSearches(name);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        Location lastLocation = new Location("Last Location");
        lastLocation.setLatitude(latitude);
        lastLocation.setLongitude(longitude);
        if (location.distanceTo(lastLocation) > 1000) {
            //Place current location marker
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            LatLng latlng = new LatLng(latitude, longitude);
            Log.d("lat:" , "" + latitude);
            Log.d("lng:" , "" + longitude);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        }

        if (place != null) {
            mMap.clear();
            Log.d("onClick", "Button is Clicked");
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            String url = getUrl(latitude, longitude, place);
            Object[] DataTransfer = new Object[2];
            DataTransfer[0] = mMap;
            DataTransfer[1] = url;
            Log.d("onClick", url);
            final GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
            getNearbyPlacesData.execute(DataTransfer);
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    DataParser dp;
                    dp = getNearbyPlacesData.mList.get(getNearbyPlacesData.mPubMarkerMap.get(marker));
                    Intent intent = new Intent(getApplication(), DetailActivity.class);

                    Location desti = new Location("Destination");
                    desti.setLatitude(Double.parseDouble(dp.getLat()));
                    desti.setLongitude(Double.parseDouble(dp.getLng()));

                    intent.putExtra("name", dp.getName());
                    intent.putExtra("vicinity", dp.getVicinity());
                    intent.putExtra("openNow", dp.getOpenNow());
                    intent.putExtra("rating", dp.getRating());

                    startActivity(intent);
                }
            });
        }
        if (name != null) {
            SearchHistory(name);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    /*
     * Requests the user for permission to use location.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // popup permission requestion for location.
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private String getUrlText(double latitude, double longitude, String query) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        googlePlacesUrl.append("query=" + removeSpace(query));
        googlePlacesUrl.append("&key=" + "AIzaSyCfUf81B45d045Yf-9PiCtlF7RXQN9tr7I");
        googlePlacesUrl.append("&location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 400);
        Log.d("getUrl", googlePlacesUrl.toString());
        caseToParse = 2;
        return (googlePlacesUrl.toString());
    }

    private String removeSpace(String str) {
        String returnStr;
        return returnStr = str.replace(" ", "_");
    }

    public void Search(View v) {
        EditText location_tf = (EditText) findViewById(R.id.search_field);
        String location = location_tf.getText().toString();
        mMap.clear();
        Log.d("onClick", "Button is Clicked");
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        saveToSqlite(location);
        String url = getUrlText(latitude, longitude, location);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        final GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                DataParser dp;
                dp = getNearbyPlacesData.mList.get(getNearbyPlacesData.mPubMarkerMap.get(marker));
                Intent intent = new Intent(getApplication(), DetailActivity.class);

                intent.putExtra("name", dp.getName());
                intent.putExtra("vicinity", dp.getVicinity());
                intent.putExtra("openNow", dp.getOpenNow());
                intent.putExtra("rating", dp.getRating());

                startActivity(intent);
            }
        });
    }

    public void SearchHistory(String name) {
        mMap.clear();
        Log.d("onClick", "Button is Clicked");
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        String url = getUrlText(latitude, longitude, name);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        final GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                DataParser dp;
                dp = getNearbyPlacesData.mList.get(getNearbyPlacesData.mPubMarkerMap.get(marker));
                Intent intent = new Intent(getApplication(), DetailActivity.class);

                intent.putExtra("name", dp.getName());
                intent.putExtra("vicinity", dp.getVicinity());
                intent.putExtra("openNow", dp.getOpenNow());
                intent.putExtra("rating", dp.getRating());

                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_list) {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_map) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
