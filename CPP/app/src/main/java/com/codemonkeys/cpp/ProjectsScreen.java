package com.codemonkeys.cpp;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.codemonkeys.cpp.Utils.ThemeUtil;

public class ProjectsScreen extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_projects_screen);

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navMenu);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }
        drawer = (DrawerLayout)findViewById(R.id.mainDrawer);
        loadTestData();
    }


    void loadTestData() {
// Array of choices
        String colors[] = {"Red", "Blue", "White", "Yellow", "Black", "Green", "Purple", "Orange", "Grey", "Brown", "Pink", "Bone", "Flesh", "Gold", "Silver"};
        String types[] = {"Base", "Shade", "Layer", "Dry", "Glaze", "Technical", "Texture", "Air"};

// Selection of the spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinnerColour);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinnerType);

// Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner2.setAdapter(spinnerArrayAdapter2);
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.nav_home) {
            startActivity(new Intent(ProjectsScreen.this, MainScreen.class));
        }
        if (id == R.id.nav_themes) {
            startActivity(new Intent(ProjectsScreen.this, ThemeSelection.class));
        }
        if (id == R.id.nav_location) {
            startActivity(new Intent(ProjectsScreen.this, LocationScreen.class));
        }
        if (id == R.id.nav_projects) {
            startActivity(new Intent(ProjectsScreen.this, ProjectsScreen.class));
        }
        if (id == R.id.nav_help) {
            startActivity(new Intent(ProjectsScreen.this, HelpScreen.class));
        }
        if(id == R.id.nav_inventory) {
            startActivity(new Intent(ProjectsScreen.this, InventoryScreen.class));
        }
        if(id == R.id.nav_barcode){
            startActivity(new Intent(ProjectsScreen.this, BarcodeActivity.class));
        }
        finish();
        drawer.closeDrawers();
        return false;
    }
}
