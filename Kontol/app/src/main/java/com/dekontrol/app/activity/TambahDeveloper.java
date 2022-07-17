package com.dekontrol.app.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dekontrol.app.AppController;
import com.dekontrol.app.Constant;
import com.dekontrol.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TambahDeveloper extends AppCompatActivity {

    private EditText nama, banner, interstitial, video, appid, splash, startapp;
    private AppController appController;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_developer);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Mengontak server...");

        appController = (AppController) getApplication();

        nama = findViewById(R.id.editTambahNamaDeveloper);
        banner = findViewById(R.id.editTambahBannerDeveloper);
        interstitial = findViewById(R.id.editTambahInterstitialDeveloper);
        video = findViewById(R.id.editTambahVideoDeveloper);
        appid = findViewById(R.id.editTambahAppIdDeveloper);
        splash = findViewById(R.id.editTambahSplashDeveloper);
        startapp = findViewById(R.id.editTambahStartAppIdDeveloper);
    }

    public void simpan(View view){
        final String nam = nama.getText().toString();
        final String ban = banner.getText().toString();
        final String ins = interstitial.getText().toString();
        final String vid = video.getText().toString();
        final String apid = appid.getText().toString();
        final String spl = splash.getText().toString();
        final String starId = startapp.getText().toString();

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
            splash.setError("Tak Boleh Kosong!");
            fail = true;
        }
        if(starId.isEmpty()){
            startapp.setError("Tak Boleh Kosong!");
            fail = true;
        }

        if(fail){
            return;
        }

        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_TAMBAH_DEVELOPER, new Response.Listener<String>() {
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
                    Toast.makeText(TambahDeveloper.this, response.getString(Constant.PESAN), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(TambahDeveloper.this, "Network Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put(Constant.KOLOM_APPID, apid);
                params.put(Constant.KOLOM_SPLASH, spl);
                params.put(Constant.KOLOM_STARTAPP, starId);
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
