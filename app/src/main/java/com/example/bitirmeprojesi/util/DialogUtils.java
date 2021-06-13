package com.example.bitirmeprojesi.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.bitirmeprojesi.R;

public class DialogUtils {

    private static MaterialDialog progressDialog;

    public static void showProgressDialog(Context context){
        if (progressDialog != null && progressDialog.isShowing()){
            return;
        }
        progressDialog =
                new MaterialDialog.Builder(context)
                        .progress(true, 100)
                        .progressIndeterminateStyle(true)
                        .build();
        progressDialog.show();
    }

    public static void hideProgressDialog(){
        if (progressDialog != null){
            progressDialog.hide();
            progressDialog = null;
        }
    }

    public static void showAlertDialog(Context context, String message){
        new MaterialDialog.Builder(context)
                .title(R.string.alert)
                .content(message)
                .positiveText(R.string.ok)
                .show();
    }
}
