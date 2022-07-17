package com.dekontrol.clientlib.iklan;

import android.content.Context;
import android.util.Log;

import com.dekontrol.clientlib.decryption.text.deskriptor.DeskripsiText;

public class KodeIklanHandler {

    static {
        System.loadLibrary("native-lib");
    }

    public native String packageName(Context context);
    public native String keyDesText(Context context);
    public native String keyDesAssets(Context context);
    public native String startAppId(Context context);
    public native String serverUrl(Context context);
    public native String developerName(Context context);

    //private Context context;

    public KodeIklanHandler(Context context) {
        if(!context.getPackageName().equals(packageName(context))){
            throw new RuntimeException("JNI Failed from java!");
        }
        //this.context = context;
    }

    public String getKeyAssets(Context context) {
        return keyDesAssets(context);
    }

    public String getDeveloperName(Context context) {
        return developerName(context);
    }

    public String getStartAppId(Context context) {
        Log.d("enskripsi startAppId", new DeskripsiText(keyDesText(context), startAppId(context)).dapatkanTextAsli());
        return new DeskripsiText(keyDesText(context), startAppId(context)).dapatkanTextAsli();
    }

    public String getServerUrl(Context context) {
        Log.d("enskripsi serverUrl", new DeskripsiText(keyDesText(context), serverUrl(context)).dapatkanTextAsli());
        return new DeskripsiText(keyDesText(context), serverUrl(context)).dapatkanTextAsli();
    }

}
