package com.codemonkeys.cpp.Adapter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codemonkeys.cpp.PaintItem;

/**
 * Created by wbenedetti on 9/25/2017.
 */
public class DBAdapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "Name";
    static final String KEY_COLOURCODE = "ColourCode";
    static final String KEY_TYPE = "Type";
    static final String KEY_COLOURGROUP = "ColourGroup";
    static final String KEY_INVENTORY = "InInventory";
    static final String KEY_WISHLIST = "InWishList";
    static final String KEY_PRICE = "Price";
    static final String KEY_UPC = "UPC";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "CPP.db";
    static final String DATABASE_TABLE = "PAINTS";
    static final String INVENTORY_TABLE = "INVENTORY";
    static final String WISHLIST_TABLE = "WISHLIST";
    static final int DATABASE_VERSION = 1;

   static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS PAINTS ( _id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, Name TEXT, ColourCode TEXT, Type TEXT, ColourGroup TEXT, Price REAL ); " +
                    "CREATE TABLE IF NOT EXISTS INVENTORY ( _id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, PaintID INTEGER, Name TEXT, ColourCode TEXT ); " +
                    "CREATE TABLE IF NOT EXISTS WISHLIST( _id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, PaintID INTEGER, Name TEXT, ColourCode TEXT ); ";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS PAINTS; DROP TABLE IF EXISTS INVENTORY; DROP TABLE IF EXISTS WISHLIST;");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---retrieves all the paints---
    public Cursor getAllPaints()
    {
        DBAdapter dba = this;
        String[] data = {dba.KEY_ROWID, dba.KEY_NAME, dba.KEY_COLOURCODE, dba.KEY_TYPE, dba.KEY_INVENTORY, dba.KEY_WISHLIST};
        return db.query(DATABASE_TABLE, data
                    , null, null, null, null, null);
    }

    public Cursor getInventory() {
        DBAdapter dba = this;
        String rawQuery = "";
        Cursor cursor = null;
        try {
            dba.open();
           rawQuery = "SELECT PAINTS._id, PAINTS.Name, PAINTS.ColourCode, PAINTS.Type " +
                        "FROM PAINTS INNER JOIN INVENTORY ON PAINTS._id = INVENTORY.PaintID";
            cursor = db.rawQuery(rawQuery,null);
        } catch(Exception ex) {
            Log.d("getInventory()", ex.getMessage());
        }
        return cursor;
    }

    public Cursor getWishlist() {
        DBAdapter dba = this;
        String rawQuery = "";
        Cursor cursor = null;
        try {
            dba.open();
            rawQuery = "SELECT PAINTS._id, PAINTS.Name, PAINTS.ColourCode, PAINTS.Type " +
                    "FROM PAINTS INNER JOIN WISHLIST ON PAINTS._id = WISHLIST.PaintID";
            cursor = db.rawQuery(rawQuery, null);
        }catch(Exception ex) {
            Log.d("getWishlist()", ex.getMessage());
        }
        return cursor;
    }

    public String getPaintTypeById(int id) {
        String type = null;
        DBAdapter dba = this;
        String[] data = {dba.KEY_TYPE};
        dba.open();
        Cursor c = db.query(DATABASE_TABLE, data, dba.KEY_ROWID + "=" + id, null, null, null, null);
        if(c.moveToFirst()){
            type = c.getString(0);
        }
        c.close();
        return type;
    }

    public int removeFromInventory(int id) {
        ContentValues values = new ContentValues();
        values.put("InInventory", 0);
        db.update(DATABASE_TABLE, values, "_id=" + id, null);
        return db.delete(INVENTORY_TABLE, "PaintID = " + id, null);
    }

    public int removeFromWishlist(int id )  {
        ContentValues values = new ContentValues();
        values.put("InWishlist", 0);
        db.update(DATABASE_TABLE, values, "_id=" + id, null);
        return db.delete(WISHLIST_TABLE, "PaintID = " + id, null);
    }

    public PaintItem getPaintByUPC(String upc) {
        DBAdapter dba = this;
        PaintItem paint = null;
        try {
            Cursor cursor = db.rawQuery("SELECT _id, Name, ColourCode, Type, InInventory, InWishlist FROM PAINTS WHERE UPC='"+upc+"'", null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    paint = new PaintItem(cursor.getInt(0), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5));
                }
            }
        } catch(Exception ex) {
            Log.e("getPaintByUPC", ex.getMessage());
        }
        return paint;
    }

    public boolean addToInventory(PaintItem i) {
        DBAdapter dba = this;
        dba.open();
        String query = "Select * from Inventory where PaintID = " + i.getId();
        Cursor cursor = db.rawQuery(query,null);
        Log.d("CURSOR", ""+cursor.getCount());
        if(cursor.getCount() <= 0) {
            ContentValues values = new ContentValues();
            values.put("PaintID", i.getId());
            db.insert(INVENTORY_TABLE, null, values);

            ContentValues contentValues = new ContentValues();
            contentValues.put("InInventory", 1);
            db.update(DATABASE_TABLE, contentValues,"_id=" + i.getId(), null);
            cursor.close();
            dba.close();
            return true;
        }
        else {
            cursor.close();
            dba.close();
            return false;
        }
    }

    public boolean addToWishlist(PaintItem i) {
        DBAdapter dba = this;
        dba.open();
        String query = "SELECT * FROM WISHLIST WHERE PaintID = " + i.getId();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() <= 0) {
            ContentValues values = new ContentValues();
            values.put("PaintID", i.getId());
            db.insert(WISHLIST_TABLE, null, values);

            ContentValues contentValues = new ContentValues();
            contentValues.put("InWishList", 1);
            db.update(DATABASE_TABLE, contentValues,"_id=" + i.getId(), null);
            cursor.close();
            dba.close();
            return true;
        } else {
            cursor.close();
            dba.close();
            return false;
        }
    }
}
