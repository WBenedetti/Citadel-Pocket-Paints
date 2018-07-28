package com.codemonkeys.cpp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.codemonkeys.cpp.Adapter.DBAdapter;
import com.codemonkeys.cpp.Adapter.PaintListAdapter;
import com.codemonkeys.cpp.Utils.DBUtil;
import com.codemonkeys.cpp.Utils.ThemeUtil;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends Activity implements NavigationView.OnNavigationItemSelectedListener{

    private DBAdapter db;
    RecyclerView recyclerView;
    private List<PaintItem> itemList;
    SharedPreferences settings;
    private String defaultURL;
    NavigationView mNavigationView;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main_screen);
        settings = getSharedPreferences("CPP", Context.MODE_PRIVATE);
        mNavigationView = (NavigationView) findViewById(R.id.navMenu);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }

        drawer = (DrawerLayout)findViewById(R.id.mainDrawer);

        DBUtil.initDatabase(db, this);
        populateListView();
        defaultURL = "https://www.games-workshop.com/en-CA/";
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_themes) {
            startActivity(new Intent(MainScreen.this, ThemeSelection.class));
        }
        if (id == R.id.nav_location) {//DO your stuff }
            Intent myIntent = new Intent(this, LocationScreen.class);
            myIntent.putExtra("showMap",true);
            startActivity(myIntent);
        }
        if (id == R.id.nav_projects) {//DO your stuff }
            startActivity(new Intent(MainScreen.this, ProjectsScreen.class));
        }
        if (id == R.id.nav_help) {
            startActivity(new Intent(MainScreen.this, HelpScreen.class));
        }
        if(id == R.id.nav_inventory) {
            startActivity(new Intent(MainScreen.this, InventoryScreen.class));
            finish();
        }
        if(id == R.id.nav_barcode) {
            startActivity(new Intent(MainScreen.this, BarcodeActivity.class));
        }
        drawer.closeDrawers();
        return false;
    }

    private void populateListView() {
        //for recycler view
        DBAdapter myAdapter = new DBAdapter(this);
        myAdapter.open();
        Cursor cursor = myAdapter.getAllPaints();
        itemList = new ArrayList<PaintItem>();
        if(cursor.moveToFirst()){
            int id = cursor.getInt(0);
            String paintName = cursor.getString(1);
            String colourCode = cursor.getString(2);
            String type = cursor.getString(3);
            int invetory = cursor.getInt(4);
            int wishlist = cursor.getInt(5);
            itemList.add(new PaintItem(id, paintName, colourCode, type, invetory, wishlist));
            while(cursor.moveToNext()) {
                id = cursor.getInt(0);
                paintName = cursor.getString(1);
                colourCode = cursor.getString(2);
                type = cursor.getString(3);
                invetory = cursor.getInt(4);
                wishlist = cursor.getInt(5);
                itemList.add(new PaintItem(id, paintName, colourCode, type, invetory, wishlist));
            }
        }

        if(cursor != null && !cursor.isClosed())
            cursor.close();

        myAdapter.close();
        recyclerView = (RecyclerView)findViewById(R.id.listviewPaints);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new PaintListAdapter(this, itemList));
    }

    public void onClick(View view) {
        Cursor c;
        // Keeping the case switch cause we can add all buttons to one listener like Lianne did
        switch (view.getId())
        {
            case R.id.buttonShowNearby:
                Intent myIntent = new Intent(this, LocationScreen.class);
                myIntent.putExtra("showMap",true);
                startActivity(myIntent);
                break;
            case R.id.buttonShowInventory:
                // switch to inventory
                startActivity(new Intent(MainScreen.this, InventoryScreen.class));
                finish();
                break;
            case R.id.buttonPurchase:
                // Sends user to the last selected paint web page or defaults to GamesWorkshop homepage
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(settings.getString("URL",defaultURL)));
                startActivity(intent);
                break;
            case R.id.buttonShowWishlist:
                setContentView(R.layout.activity_wishlist_screen);
                startActivity(new Intent(MainScreen.this, WishlistScreen.class));
                finish();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        settings.edit().clear().commit();
    }
}
