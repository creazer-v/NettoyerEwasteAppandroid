package com.play.nettoyer.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.play.nettoyer.R;

public class loading {

    private static Activity activity;
private static AlertDialog dialog;

public loading(Activity myActivity){
    activity=myActivity;
}
public static void startloading(){
    AlertDialog.Builder builder=new AlertDialog.Builder(activity);
    LayoutInflater inflater=activity.getLayoutInflater();
    builder.setView(inflater.inflate(R.layout.activity_progressbox,null));
    builder.setCancelable(false);
    dialog=builder.create();
    dialog.show();


}
public static void dismissDialog(){
    dialog.dismiss();
}
}
