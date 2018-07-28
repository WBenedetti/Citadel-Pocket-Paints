package com.codemonkeys.cpp.Utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by eric on 2017-12-20.
 */

public class IntentUtil {
    public static void start(Intent intent, Context context) {
        context.startActivity(intent);
    }
}
