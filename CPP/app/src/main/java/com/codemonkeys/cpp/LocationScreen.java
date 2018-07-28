package com.codemonkeys.cpp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.codemonkeys.cpp.Utils.ThemeUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationScreen extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    //Two variables to control the map, and map fragment.
    public GoogleMap mMap;
    public SupportMapFragment mapFragment;
    public boolean showMap = false;
    DrawerLayout drawer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.location_screen);

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navMenu);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }

        drawer = (DrawerLayout)findViewById(R.id.mainDrawer);

        //Setting up the map fragment.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        try {
            Intent myIntent = getIntent(); // gets the previously created intent
            showMap = myIntent.getExtras().getBoolean("showMap"); // will return "FirstKeyValue"
        }
        catch(Exception ex){
            //Starting normally from nav.
        }
        if (showMap==true)
            mapFragment.getView().setVisibility(View.VISIBLE);
        else
            mapFragment.getView().setVisibility(View.INVISIBLE);

        mapFragment.getMapAsync(this);



    }

    //Simple onclick to show the map / nearby stores.
    public void onClickShowStores(View view) {
        mapFragment.getView().setVisibility(View.VISIBLE);
    }

    //This will have hardcoded store locations, along with other map options (Zoom, Search, My Location)
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add this line
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng( 43.013841,-81.198152))
                .title("Current Location")
                .snippet("1001 Fanshawe College Blvd, London, N5Y 5R6")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng( 42.931326,-81.224625))
                .title("Games Workshop - White Oaks Mall")
                .snippet("1105 Wellington Rd., Unit 411, London, N6E 1V4")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng( 42.984977,-81.246262))
                .title("Imperial Hobbies Ontario")
                .snippet("256 DUNDAS STREET, LONDON, N6A 1H3")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng( 43.002082,-81.186861))
                .title("The Game Chamber INC")
                .snippet("1700 DUNDAS ST, LONDON, N5W 3C9")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


        //Setting the camera up w/ zoom prefs, by default we use Fanshawe.
        LatLng locFanshawe = new LatLng(43.013841, -81.198152);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locFanshawe));
        mMap.setMinZoomPreference(11.5f);
        mMap.setMaxZoomPreference(20.0f);



    }

    //Settings for the navigation

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.nav_home) {
            startActivity(new Intent(LocationScreen.this, MainScreen.class));
        }
        if (id == R.id.nav_themes) {
            startActivity(new Intent(LocationScreen.this, ThemeSelection.class));
        }
        if (id == R.id.nav_location) {
            startActivity(new Intent(LocationScreen.this, LocationScreen.class));
        }
        if (id == R.id.nav_projects) {
            startActivity(new Intent(LocationScreen.this, ProjectsScreen.class));
        }
        if (id == R.id.nav_help) {
            startActivity(new Intent(LocationScreen.this, HelpScreen.class));
        }
        if(id == R.id.nav_inventory) {
            startActivity(new Intent(LocationScreen.this, InventoryScreen.class));
        }
        if(id == R.id.nav_barcode){
            startActivity(new Intent(LocationScreen.this, BarcodeActivity.class));
        }
        finish();
        return false;
    }

    public Context getActivity() {
        return this;
    }
}
