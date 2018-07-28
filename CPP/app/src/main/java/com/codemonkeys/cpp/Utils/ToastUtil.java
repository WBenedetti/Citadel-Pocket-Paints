package com.codemonkeys.cpp.Utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codemonkeys.cpp.R;

/**
 * Created by eric on 2017-12-20.
 */

public class ToastUtil {
    public static void DisplayToast(Context context, String text) {

        //Must use a layout inflator for the toast's custom theme.
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        //Setting the layout of the toast.
        View toastView = inflater.inflate(R.layout.custom_toast, null);
        TextView textview = (TextView) toastView.findViewById(R.id.text);
        textview.setText(text);

        //Setting up the actual Toast.
        Toast toast = new Toast(context);
        toast.setView(toastView);
        toast.makeText(context,text,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }


}
