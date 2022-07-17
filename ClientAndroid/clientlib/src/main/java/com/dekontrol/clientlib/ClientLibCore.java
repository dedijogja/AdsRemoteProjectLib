package com.dekontrol.clientlib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dekontrol.clientlib.iklan.KodeIklanHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.Mrec;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.util.HashMap;
import java.util.Map;

public class ClientLibCore {

    private static String kodeEnskripsiAsset;

    private static boolean statusAdmob, statusBanner, statusInterstitial, statusVideo, statusSplash;

    private static String appId, kodeBanner, kodeInterstitial, kodeVideo;

    private static String kodeStatusAdmob = "statusAdmobKode", kodeStatusBanner = "statusBannerKode",
            kodeStatusInterstitial = "statusInterstitialKode", kodeStatusVideo = "statusVideoKode",
            kodeStatusIklanSplash = "statusIklanSplashKode";

    private static boolean startAppInit = false;
    private static boolean clientLibCoreInit = false;

    public interface Listener{
        void onInitComplete();
    }

    private static Listener listener;

    public static void init(final Activity context){
        clientLibCoreInit = true;
        listener = (Listener) context;

        statusAdmob = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(kodeStatusAdmob, false);
        statusBanner = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(kodeStatusBanner, false);
        statusInterstitial = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(kodeStatusInterstitial, false);
        statusVideo = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(kodeStatusVideo, false);
        statusSplash = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(kodeStatusIklanSplash, true);

        kodeEnskripsiAsset = new KodeIklanHandler(context).getKeyAssets(context);

        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, new KodeIklanHandler(context).getServerUrl(context), new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String responses) {
                //result.setText(responses);

                listener.onInitComplete();
                if(statusAdmob){
                    if(statusInterstitial){
                        interstitialAd = new InterstitialAd(context.getApplicationContext());
                        interstitialAd.setAdUnitId(kodeInterstitial);
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("clientlib", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameter = new HashMap<>();
                parameter.put("package", context.getPackageName());
                parameter.put("namaapp", context.getString(R.string.app_name));
                parameter.put("namadev", new KodeIklanHandler(context).getDeveloperName(context));
                return parameter;
            }
        };
        mRequestQueue.add(stringRequest);

    }

    public static void initStartApp(Activity context){
        if(!clientLibCoreInit){
            throw new RuntimeException("ClientLibCore belum di init!");
        }
        startAppInit = true;
        StartAppSDK.init(context, new KodeIklanHandler(context).getStartAppId(context), false);
        if(!statusSplash || statusAdmob || statusInterstitial){
            StartAppAd.disableSplash();
        }
    }

    public enum AdSize{
        MEDIUM_RECTANGLE, SMART_BANNER
    }

    private static com.google.android.gms.ads.AdSize ukuran = com.google.android.gms.ads.AdSize.SMART_BANNER;
    public static void tempatkanIklanBanner(final Activity context, final LinearLayout linearLayout, final AdSize adSize){
        if(adSize == AdSize.MEDIUM_RECTANGLE){
            ukuran = com.google.android.gms.ads.AdSize.MEDIUM_RECTANGLE;
        }
        if(!clientLibCoreInit){
            throw new RuntimeException("ClientLibCore belum di init!");
        }
        if(!startAppInit){
            throw new RuntimeException("StartApp belum di init");
        }
        if(statusAdmob){
            if(statusBanner){
                final AdView adView = new AdView(context);
                adView.setAdSize(ukuran);
                adView.setAdUnitId(kodeBanner);
                adView.loadAd(new AdRequest.Builder().build());
                adView.setAdListener(new AdListener(){
                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        linearLayout.removeAllViews();
                        if(adSize == AdSize.MEDIUM_RECTANGLE){
                            linearLayout.addView(new Mrec(context));
                        }else{
                            linearLayout.addView(new Banner(context));
                        }
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        linearLayout.removeAllViews();
                        linearLayout.addView(adView);
                    }
                });
            }else{
                linearLayout.removeAllViews();
                if(adSize == AdSize.MEDIUM_RECTANGLE){
                    linearLayout.addView(new Mrec(context));
                }else{
                    linearLayout.addView(new Banner(context));
                }
            }
        }else{
            linearLayout.removeAllViews();
            if(adSize == AdSize.MEDIUM_RECTANGLE){
                linearLayout.addView(new Mrec(context));
            }else{
                linearLayout.addView(new Banner(context));
            }
        }
    }

    private static InterstitialAd interstitialAd;
    public static void tampilkanInterstitial(final Activity context, final Intent intent){
        if(!clientLibCoreInit){
            throw new RuntimeException("ClientLibCore belum di init!");
        }
        if(!startAppInit){
            throw new RuntimeException("StartApp belum di init");
        }
        if(statusAdmob) {
            if (statusInterstitial) {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.setAdListener(new AdListener(){
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                            if(intent!=null){
                                context.startActivity(intent);
                            }
                        }
                    });
                    interstitialAd.show();
                } else {
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                    if(intent!=null){
                        context.startActivity(intent);
                    }
                    StartAppAd.showAd(context);
                }
            }else{
                if(intent!=null){
                    context.startActivity(intent);
                }
                StartAppAd.showAd(context);
            }
        }else{
            if(intent!=null){
                context.startActivity(intent);
            }
            StartAppAd.showAd(context);
        }
    }
}
