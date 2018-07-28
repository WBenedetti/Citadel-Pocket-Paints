package com.codemonkeys.cpp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.codemonkeys.cpp.Utils.ThemeUtil;

public class HelpScreen extends Activity implements NavigationView.OnNavigationItemSelectedListener, View.OnTouchListener {

    DrawerLayout drawer;

    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    ImageView view;

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    // remember some points for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_help_screen);

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navMenu);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }
        drawer = (DrawerLayout)findViewById(R.id.mainDrawer);
        view = (ImageView)findViewById(R.id.imageView);
        view.setOnTouchListener(this);
        view.setImageMatrix(matrix);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ImageView myView = (ImageView) view;
        // Handle touch events here...
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(motionEvent.getX(), motionEvent.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(motionEvent);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, motionEvent);

                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // ...
                    matrix.set(savedMatrix);
                    matrix.postTranslate(motionEvent.getX() - start.x,
                            motionEvent.getY() - start.y);
                }
                else if (mode == ZOOM) {
                    float newDist = spacing(motionEvent);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        myView.setImageMatrix(matrix);
        return true; // indicate event was handled
    }

    private float spacing(MotionEvent motionEvent) {
        // ...
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    /** Calculate the mid point of the first two fingers */
    private void midPoint(PointF point, MotionEvent motionEvent) {
        // 2 finger midpoint
        float x = motionEvent.getX(0) + motionEvent.getX(1);
        float y = motionEvent.getY(0) + motionEvent.getY(1);
        point.set(x / 2, y / 2);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.nav_home) {
            startActivity(new Intent(HelpScreen.this, MainScreen.class));
        }
        if (id == R.id.nav_themes) {
            startActivity(new Intent(HelpScreen.this, ThemeSelection.class));
        }
        if (id == R.id.nav_location) {
            startActivity(new Intent(HelpScreen.this, LocationScreen.class));
        }
        if (id == R.id.nav_projects) {
            startActivity(new Intent(HelpScreen.this, ProjectsScreen.class));
        }
        if(id == R.id.nav_inventory) {
            startActivity(new Intent(HelpScreen.this, InventoryScreen.class));
        }
        if(id == R.id.nav_barcode){
            startActivity(new Intent(HelpScreen.this, BarcodeActivity.class));
        }
        finish();
        drawer.closeDrawers();
        return false;
    }
}
