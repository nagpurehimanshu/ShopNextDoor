package com.example.shopnextdoor.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.shopnextdoor.R;

public class LoadingDialog {
    Activity activity;
    AlertDialog alertDialog;

    public LoadingDialog(Activity myActivity){
        activity = myActivity;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.dialog_progress, null));

        builder.setCancelable(true);
        alertDialog = builder.create();
    }

    public void startDialog(){
        alertDialog.show();
    }

    public void dismissDialog(){
        alertDialog.dismiss();
    }
}
