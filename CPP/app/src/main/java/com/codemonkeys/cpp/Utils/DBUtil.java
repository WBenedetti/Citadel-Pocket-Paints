package com.codemonkeys.cpp.Utils;

import android.app.Activity;

import com.codemonkeys.cpp.Adapter.DBAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by eric on 2017-11-22.
 */

public class DBUtil {
    public static void initDatabase(DBAdapter db, Activity activity){
        db = new DBAdapter(activity);
        // get the existing database file or from assets folder if doesn't exist
        try {
            String destPath = "/data/data/" + activity.getPackageName() + "/databases";
            File f = new File(destPath + "/CPP.db");
            int db_size = activity.getBaseContext().getAssets().open("CPP.db").available();
            if (!f.exists()) {
                //f.mkdirs();
                f.createNewFile();
                // copy from the db from the assets folder
                CopyDB(activity.getBaseContext().getAssets().open("CPP.db"),
                        new FileOutputStream(destPath + "/CPP.db"));
            }
            // if current db is smaller than one needing to be pushed, replace copy on device
            if(f.length() < db_size) {
                f.delete();
                CopyDB(activity.getBaseContext().getAssets().open("CPP.db"),
                        new FileOutputStream(destPath + "/CPP.db"));
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException{
        // Copy one byte at a time
        byte[] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0)
        {
            outputStream.write(buffer,0,length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();  // close streams
    }

}
