package com.dekontrol.app.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dekontrol.app.AppController;
import com.dekontrol.app.Constant;
import com.dekontrol.app.R;
import com.dekontrol.app.model.Developer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditDeveloper extends AppCompatActivity {

    private EditText nama, banner, interstitial, video, appid, splash, startapp, intervalInterstitial;
    private Switch switchStatus, switchBanner, switchInterstitial, switchVideo;
    private Spinner spinnerStartAppDev;
    private RadioGroup radioGroupIklanSplash;
    private AppController appController;

    private String id = null;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_developer);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Mengontak server...");

        appController = (AppController) getApplication();

        getSupportActionBar().setTitle("Edit Developer");

        nama = findViewById(R.id.editTambahNamaDeveloper);
        banner = findViewById(R.id.editTambahBannerDeveloper);
        interstitial = findViewById(R.id.editTambahInterstitialDeveloper);
        video = findViewById(R.id.editTambahVideoDeveloper);
        appid = findViewById(R.id.editAppID);
        splash = findViewById(R.id.editTambahSplashDeveloper);
        startapp = findViewById(R.id.editStartAppIdDev);
        intervalInterstitial = findViewById(R.id.editIntervalInterstitial);

        switchStatus = findViewById(R.id.switchStatus);
        switchBanner = findViewById(R.id.switchBanner);
        switchInterstitial = findViewById(R.id.switchInterstitial);
        switchVideo = findViewById(R.id.switchVideo);

        spinnerStartAppDev = findViewById(R.id.spinnerStartAppDev);

        radioGroupIklanSplash = findViewById(R.id.grup);

        id = getIntent().getStringExtra(Constant.KODE_INTENT);

        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_DETAIL_DEVELOPER, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String responses) {
                dialog.dismiss();
                try {
                    JSONObject response = new JSONObject(responses);
                    String status = response.getString(Constant.STATUS);
                    if(status.equals(Constant.SUKSES)){
                        JSONObject jsonObject = response.getJSONObject(Constant.HASIL);
                        Developer developer = new Developer();
                        developer.setId(jsonObject.getString(Constant.KOLOM_ID));
                        developer.setNama(jsonObject.getString(Constant.KOLOM_NAMA));
                        developer.setAppid(jsonObject.getString(Constant.KOLOM_APPID));
                        developer.setBanner(jsonObject.getString(Constant.KOLOM_BANNER));
                        developer.setInterstitial(jsonObject.getString(Constant.KOLOM_INTERSTITIAL));
                        developer.setVideo(jsonObject.getString(Constant.KOLOM_VIDEO));
                        developer.setStatus(jsonObject.getString(Constant.KOLOM_STATUS));
                        developer.setStatusBanner(jsonObject.getString(Constant.KOLOM_STATUSBANNER));
                        developer.setStatusInterstitial(jsonObject.getString(Constant.KOLOM_STATUSINTERSTITIAL));
                        developer.setStatusVideo(jsonObject.getString(Constant.KOLOM_STATUSVIDEO));
                        developer.setJumlahApp(jsonObject.getInt(Constant.KOLOM_APP));
                        developer.setJumlahTrafik(jsonObject.getInt(Constant.KOLOM_TRAFIK));
                        developer.setStatusSplash(jsonObject.getString(Constant.KOLOM_STATUSSPLASH));
                        developer.setSplash(jsonObject.getString(Constant.KOLOM_SPLASH));
                        developer.setStartApp(jsonObject.getString(Constant.KOLOM_STARTAPP));
                        developer.setStatusStartApp(jsonObject.getString(Constant.KOLOM_STATUSSTARTAPP));
                        developer.setIntervalInterstitial(jsonObject.getInt(Constant.KOLOM_INTERVAL_INTERSTITIAL));

                        nama.setText(developer.getNama());
                        banner.setText(developer.getBanner());
                        interstitial.setText(developer.getInterstitial());
                        video.setText(developer.getVideo());
                        appid.setText(developer.getAppid());
                        splash.setText(developer.getSplash());
                        startapp.setText(developer.getStartApp());
                        intervalInterstitial.setText(String.valueOf(developer.getIntervalInterstitial()));

                        if(developer.getStatus().equals("on")){
                            switchStatus.setChecked(true);
                        }

                        if(developer.getStatusBanner().equals("on")){
                            switchBanner.setChecked(true);
                        }

                        if(developer.getStatusInterstitial().equals("on")){
                            switchInterstitial.setChecked(true);
                        }

                        if(developer.getStatusVideo().equals("on")){
                            switchVideo.setChecked(true);
                        }

                        switch (developer.getStatusSplash()) {
                            case "onstartappoffline":
                                radioGroupIklanSplash.check(R.id.gruponstartappoffline);
                                break;
                            case "onstartapponline":
                                radioGroupIklanSplash.check(R.id.gruponstartapponline);
                                break;
                            case "onadmob":
                                radioGroupIklanSplash.check(R.id.gruponadmob);
                                break;
                            default:
                                radioGroupIklanSplash.check(R.id.grupoff);
                                break;
                        }

                        switch (developer.getStatusStartApp()) {
                            case "onoffline":
                                spinnerStartAppDev.setSelection(0);
                                break;
                            case "ononline":
                                spinnerStartAppDev.setSelection(1);
                                break;
                            default:
                                spinnerStartAppDev.setSelection(2);
                                break;
                        }

                    }
                   // Toast.makeText(EditDeveloper.this, response.getString(Constant.PESAN), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(EditDeveloper.this, "Network Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USERNAME, appController.getUsername());
                params.put(Constant.PASSWORD, appController.getPassword());
                params.put(Constant.PACKAGE, getPackageName());
                params.put(Constant.KOLOM_ID, id);
                return params;
            }
        };
        appController.addToRequestQueue(stringRequest);
    }

    private String status, statusBanner, statusInterstitial, statusVideo, statusSplash, statusStartApp;

    public void simpanPembaruan(View view){
        final String nam = nama.getText().toString();
        final String ban = banner.getText().toString();
        final String ins = interstitial.getText().toString();
        final String vid = video.getText().toString();
        final String apid = appid.getText().toString();
        final String spl = splash.getText().toString();
        final String setar = startapp.getText().toString();
        final String inter = intervalInterstitial.getText().toString();

        status = "off";
        statusBanner = "off";
        statusInterstitial = "off";
        statusVideo = "off";
        statusSplash = "onstartapp";
        statusStartApp = "onoffline";

        if(switchStatus.isChecked()){
            status = "on";
        }

        if(switchBanner.isChecked()){
            statusBanner = "on";
        }

        if(switchInterstitial.isChecked()){
            statusInterstitial = "on";
        }

        if(switchVideo.isChecked()){
            statusVideo = "on";
        }

        int idRadio = radioGroupIklanSplash.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(idRadio);
        statusSplash = radioButton.getText().toString();

        statusStartApp = spinnerStartAppDev.getSelectedItem().toString();

        boolean fail = false;
        if(nam.isEmpty()){
            nama.setError("Tak boleh kosong!");
            fail = true;
        }
        if(ban.isEmpty()){
            banner.setError("Tak boleh kosong!");
            fail = true;
        }
        if(ins.isEmpty()){
            interstitial.setError("Tak boleh kosong!");
            fail = true;
        }
        if(vid.isEmpty()){
            video.setError("Tak boleh kosong!");
            fail = true;
        }
        if(apid.isEmpty()){
            appid.setError("Tak boleh kosong!");
            fail = true;
        }
        if(spl.isEmpty()){
            splash.setError("Tak boleh kosong!");
            fail = true;
        }
        if(setar.isEmpty()){
            startapp.setError("Tak boleh kosong!");
            fail = true;
        }
        if(inter.isEmpty()){
            intervalInterstitial.setError("Tak boleh kosong!");
            fail = true;
        }

        if(Integer.parseInt(intervalInterstitial.getText().toString()) <= 0){
            intervalInterstitial.setError("Minimal 1, artinya tanpa interval");
            fail = true;
        }

        if(fail){
            return;
        }

        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_EDIT_DEVELOPER, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String responses) {
                dialog.dismiss();
                try {
                    JSONObject response = new JSONObject(responses);
                    String status = response.getString(Constant.STATUS);
                    if(status.equals(Constant.SUKSES)){
                        //sukses ditambahkan
                        Intent intent = new Intent();
                        intent.putExtra(Constant.REFRESH, true);
                        setResult(3, intent);
                        finish();
                    }
                    Toast.makeText(EditDeveloper.this, response.getString(Constant.PESAN), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(EditDeveloper.this, "Network Error: "+ error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USERNAME, appController.getUsername());
                params.put(Constant.PASSWORD, appController.getPassword());
                params.put(Constant.PACKAGE, getPackageName());
                params.put(Constant.KOLOM_NAMA, nam);
                params.put(Constant.KOLOM_BANNER, ban);
                params.put(Constant.KOLOM_INTERSTITIAL, ins);
                params.put(Constant.KOLOM_VIDEO, vid);
                params.put(Constant.KOLOM_ID, id);
                params.put(Constant.KOLOM_STATUS, status);
                params.put(Constant.KOLOM_STATUSBANNER, statusBanner);
                params.put(Constant.KOLOM_STATUSINTERSTITIAL, statusInterstitial);
                params.put(Constant.KOLOM_STATUSVIDEO, statusVideo);
                params.put(Constant.KOLOM_APPID, apid);
                params.put(Constant.KOLOM_STATUSSPLASH, statusSplash);
                params.put(Constant.KOLOM_SPLASH, spl);
                params.put(Constant.KOLOM_STATUSSTARTAPP, statusStartApp);
                params.put(Constant.KOLOM_STARTAPP, setar);
                params.put(Constant.KOLOM_INTERVAL_INTERSTITIAL, inter);
                return params;
            }
        };
        appController.addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constant.REFRESH, false);
        setResult(3, intent);
        finish();
    }
}
