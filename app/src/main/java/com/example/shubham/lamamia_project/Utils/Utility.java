package com.example.shubham.lamamia_project.Utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.example.shubham.lamamia_project.R;

public class Utility {

    public static void showSnackBar(Context context, View view, String text){
        Snackbar sb = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE);
        sb.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        sb.show();
    }

    public static void showSnackBarSuccess(Context context, View view, String text){
        Snackbar sb = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        sb.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        sb.show();
    }

    public static void showSnackBarFailShort(Context context, View view, String text){
        Snackbar sb = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        sb.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        sb.show();
    }

    public static void showSnackBarSuccessShort(Context context, View view, String text){
        Snackbar sb = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        sb.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        sb.show();
    }
}
