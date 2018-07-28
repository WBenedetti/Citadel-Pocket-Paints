package com.codemonkeys.cpp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codemonkeys.cpp.Utils.ThemeUtil;

public class ThemeSelection extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_theme_selection);


        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navMenu);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }
        drawer = (DrawerLayout)findViewById(R.id.mainDrawer);

    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.nav_home) {
            startActivity(new Intent(ThemeSelection.this, MainScreen.class));
        }
        if (id == R.id.nav_themes) {
            startActivity(new Intent(ThemeSelection.this, ThemeSelection.class));
        }
        if (id == R.id.nav_location) {
            startActivity(new Intent(ThemeSelection.this, LocationScreen.class));
        }
        if (id == R.id.nav_projects) {
            startActivity(new Intent(ThemeSelection.this, ProjectsScreen.class));
        }
        if (id == R.id.nav_help) {
            startActivity(new Intent(ThemeSelection.this, HelpScreen.class));
        }
        if(id == R.id.nav_inventory) {
            startActivity(new Intent(ThemeSelection.this, InventoryScreen.class));
        }
        if(id == R.id.nav_barcode){
            startActivity(new Intent(ThemeSelection.this, BarcodeActivity.class));
        }
        finish();
        drawer.closeDrawers();
        return false;
    }

    public void onClickTheme(View view) {
        //Finding the "pageBackground"
        LinearLayout layout = (LinearLayout) findViewById(R.id.pageBackground);
        //Which button was clicked
        switch (view.getId()) {
            case R.id.btnDark:
                ThemeUtil.changeToTheme(this, ThemeUtil.DarkTheme);
                break;
            case R.id.btnLight:
                ThemeUtil.changeToTheme(this, ThemeUtil.LightTheme);
                break;
            case R.id.btnBlue:
                ThemeUtil.changeToTheme(this, ThemeUtil.BlueTheme);
                break;
            case R.id.btnRed:
                ThemeUtil.changeToTheme(this, ThemeUtil.RedTheme);
                break;
        }
    }

}
