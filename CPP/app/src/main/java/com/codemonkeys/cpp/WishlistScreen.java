package com.codemonkeys.cpp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.codemonkeys.cpp.Adapter.DBAdapter;
import com.codemonkeys.cpp.Adapter.InventoryListAdapter;
import com.codemonkeys.cpp.Adapter.WishlistAdapter;
import com.codemonkeys.cpp.Utils.ThemeUtil;

import java.util.ArrayList;
import java.util.List;

public class WishlistScreen extends Activity implements EmptyWishlistFragment.OnFragmentInteractionListener {

    RecyclerView recyclerView;
    private List<PaintItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_wishlist_screen);
        populateWishlist();
    }

    private void populateWishlist() {
        DBAdapter myAdapter = new DBAdapter(this);
        myAdapter.open();
        Cursor cursor = myAdapter.getWishlist();
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

            recyclerView = (RecyclerView)findViewById(R.id.wishlistList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(new WishlistAdapter(this, itemList));
        } else {
            EmptyWishlistFragment();
        }
        myAdapter.close();
    }

    public void EmptyWishlistFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EmptyWishlistFragment  emptyWishlistFragment= new EmptyWishlistFragment();
        fragmentTransaction.replace(android.R.id.content, emptyWishlistFragment);
        // add to the backstack
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onReturn(View view) {
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
    public void onFragmentInteraction(Uri uri) {

    }
}
