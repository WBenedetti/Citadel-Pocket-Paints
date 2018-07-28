package com.codemonkeys.cpp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.codemonkeys.cpp.Adapter.DBAdapter;
import com.codemonkeys.cpp.Adapter.InventoryListAdapter;
import com.codemonkeys.cpp.Adapter.PaintListAdapter;
import com.codemonkeys.cpp.Utils.ThemeUtil;

import java.util.ArrayList;
import java.util.List;

public class InventoryScreen extends Activity implements EmptyInventoryFragment.OnFragmentInteractionListener {

    RecyclerView recyclerView;
    private List<PaintItem> itemList;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_inventory_screen);

        drawer = (DrawerLayout)findViewById(R.id.mainDrawer);
        populateInventory();
    }


    private void populateInventory() {
        DBAdapter myAdapter = new DBAdapter(this);
        myAdapter.open();
        Cursor cursor = myAdapter.getInventory();
        if(cursor.getCount() > 0) {
            itemList = new ArrayList<PaintItem>();
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                String paintName = cursor.getString(1);
                String colourCode = cursor.getString(2);
                String type = cursor.getString(3);
                itemList.add(new PaintItem(id, paintName, colourCode, type));
                while (cursor.moveToNext()) {
                    id = cursor.getInt(0);
                    paintName = cursor.getString(1);
                    colourCode = cursor.getString(2);
                    type = cursor.getString(3);
                    itemList.add(new PaintItem(id, paintName, colourCode, type));
                }
            }

            if (cursor != null && !cursor.isClosed())
                cursor.close();

        recyclerView = (RecyclerView)findViewById(R.id.inventoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new InventoryListAdapter(this, itemList));
        } else {
            EmptyInventoryFragment();
        }
        myAdapter.close();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void EmptyInventoryFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EmptyInventoryFragment  emptyInventoryFragment= new EmptyInventoryFragment();
        fragmentTransaction.replace(android.R.id.content, emptyInventoryFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onPaintList(View view) {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainScreen.class));
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
