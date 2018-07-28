package com.codemonkeys.cpp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codemonkeys.cpp.Adapter.DBAdapter;
import com.codemonkeys.cpp.Utils.ThemeUtil;
import com.codemonkeys.cpp.Utils.ToastUtil;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BarcodeActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtView;
    ImageView imageView;
    Bitmap bitMap;
    String mCurrentPhotoPath;
    BarcodeDetector detector;
    DrawerLayout drawer;

    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_barcode);

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navMenu);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }
        drawer = (DrawerLayout)findViewById(R.id.mainDrawer);

         detector = new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.EAN_13 | Barcode.EAN_8)
                        .build();

        if(!detector.isOperational()){
            ToastUtil.DisplayToast(this,"Scanner not available! Try again.");
            return;
        }

    }

    public void onClickTakePicture(View view) {
        dispatchTakePictureIntent();
    }

    /*
        Method Name: dispatchTakePictureIntent
        Purpose:     Creates a new intent to take a picture with the camera,
                     we use the createImageFile to return a temporary file to bind our image to
        Accepts:     Nothing
        Returns:     Nothing
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.i("TakePictureIntent", "Device memeory is full!");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.codemonkeys.cpp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode== RESULT_OK){
            File image = new File(mCurrentPhotoPath);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bitMap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);

            Frame frame = new Frame.Builder().setBitmap(bitMap).build();
            SparseArray<Barcode> barcodes = detector.detect(frame);

            if(barcodes.size() == 0) {
                ToastUtil.DisplayToast(this, "Unable to read barcode.");
            } else {
                Barcode thisCode = barcodes.valueAt(0);
                DBAdapter dba = new DBAdapter(this);
                dba.open();
                PaintItem paint = dba.getPaintByUPC(thisCode.rawValue.toString());
                if(dba.addToInventory(paint))
                    ToastUtil.DisplayToast(this, paint.getPaintName() + " added to inventory!");
                else
                    ToastUtil.DisplayToast(this, paint.getPaintName() + " already in inventory.");
                dba.close();
            }

            if(!image.delete())
                ToastUtil.DisplayToast(this, "Cannot delete image");
        }
    }

    /*
        Method name: createImageFile
        Purpose:     creates a temporary file name that is unique to be saved on the device
        Accepts:     Nothing
        Returns:     A new temporary file
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.nav_home) {
            startActivity(new Intent(BarcodeActivity.this, MainScreen.class));
        }
        if (id == R.id.nav_themes) {
            startActivity(new Intent(BarcodeActivity.this, ThemeSelection.class));
        }
        if (id == R.id.nav_location) {
            startActivity(new Intent(BarcodeActivity.this, LocationScreen.class));
        }
        if (id == R.id.nav_projects) {
            startActivity(new Intent(BarcodeActivity.this, ProjectsScreen.class));
        }
        if (id == R.id.nav_help) {
            startActivity(new Intent(BarcodeActivity.this, HelpScreen.class));
        }
        if(id == R.id.nav_inventory) {
            startActivity(new Intent(BarcodeActivity.this, InventoryScreen.class));
        }
        if(id == R.id.nav_barcode){
            startActivity(new Intent(BarcodeActivity.this, BarcodeActivity.class));
        }
        drawer.closeDrawers();
        return false;
    }
}
