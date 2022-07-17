package com.dekontrol.app.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dekontrol.app.AppController;
import com.dekontrol.app.Constant;
import com.dekontrol.app.R;
import com.dekontrol.app.RecycleViewMargin;
import com.dekontrol.app.adapter.ListAplikasiAdapter;
import com.dekontrol.app.model.Aplikasi;
import com.dekontrol.app.model.Developer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAplikasi extends AppCompatActivity {

    private AppController appController;
    private String idDeveloper;
    private String namaDev;
    private RecyclerView recyclerView;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_aplikasi);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Mengontak server...");

        namaDev = getIntent().getStringExtra("nama_dev");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(namaDev);

        idDeveloper = getIntent().getStringExtra(Constant.KODE_INTENT);
        recyclerView = findViewById(R.id.recycleListAplikasi);
        recyclerView.addItemDecoration(new RecycleViewMargin(2, this));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        appController = (AppController) getApplication();

        refresh();
    }

    public void setting(View view){
        Intent intent = new Intent(this, EditDeveloper.class);
        intent.putExtra(Constant.KODE_INTENT, idDeveloper);
        startActivityForResult(intent, 3);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constant.REFRESH, true);
        setResult(3, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_aplikasi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.refresh_aplikasi:
                refresh();
                return true;
            case R.id.cross_promote:
                Intent intent = new Intent(this, PromoteActivity.class);
                intent.putExtra(Constant.KOLOM_IDDEVELOPER, idDeveloper);
                intent.putExtra(Constant.KOLOM_NAMA, namaDev);
                startActivity(intent);
                return true;
            case R.id.hapus_developer:
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Konfirmasi");
                alertDialog.setMessage("Apakah anda yakin ingin mengahpus data developer dan semua aplikasi yang berada di dalamnya?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hapusDeveloper();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                return true;
        }
        return true;
    }

    private void hapusDeveloper(){
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_HAPUS_DEVELOPER, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String responses) {
                dialog.dismiss();
                try {
                    JSONObject response = new JSONObject(responses);
                    String status = response.getString(Constant.STATUS);
                    if(status.equals(Constant.SUKSES)){
                        //sukses dihapus
                        Toast.makeText(ListAplikasi.this, response.getString(Constant.PESAN), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra(Constant.REFRESH, true);
                        setResult(3, intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(ListAplikasi.this, "Network Error : "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USERNAME, appController.getUsername());
                params.put(Constant.PASSWORD, appController.getPassword());
                params.put(Constant.PACKAGE, getPackageName());
                params.put(Constant.KOLOM_ID, idDeveloper);
                return params;
            }
        };
        appController.addToRequestQueue(stringRequest);
    }

    private void refresh(){
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_LIST_APLIKASI, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String responses) {
                dialog.dismiss();
                try {
                    JSONObject response = new JSONObject(responses);
                    String status = response.getString(Constant.STATUS);
                    if(status.equals(Constant.SUKSES)){
                        JSONObject hasil = response.getJSONObject(Constant.HASIL);

                        JSONObject objectDeveloper = hasil.getJSONObject(Constant.DATA_DEVELOPER);
                        Developer developer = new Developer();
                        developer.setId(objectDeveloper.getString(Constant.KOLOM_ID));
                        developer.setNama(objectDeveloper.getString(Constant.KOLOM_NAMA));
                        developer.setJumlahApp(objectDeveloper.getInt(Constant.KOLOM_APP));
                        developer.setJumlahTrafik(objectDeveloper.getInt(Constant.KOLOM_TRAFIK));
                        developer.setStatus(objectDeveloper.getString(Constant.KOLOM_STATUS));
                        developer.setStatusBanner(objectDeveloper.getString(Constant.KOLOM_STATUSBANNER));
                        developer.setStatusInterstitial(objectDeveloper.getString(Constant.KOLOM_STATUSINTERSTITIAL));
                        developer.setStatusVideo(objectDeveloper.getString(Constant.KOLOM_STATUSVIDEO));
                        developer.setBanner(objectDeveloper.getString(Constant.KOLOM_BANNER));
                        developer.setInterstitial(objectDeveloper.getString(Constant.KOLOM_INTERSTITIAL));
                        developer.setVideo(objectDeveloper.getString(Constant.KOLOM_VIDEO));

                        findViewById(R.id.bawah).setVisibility(View.VISIBLE);
                        TextView onoff = findViewById(R.id.onOffDev);
                        int aktifdev = 0;
                        if(developer.getStatusBanner().equals(Constant.KODE_ON)){
                            aktifdev++;
                        }
                        if(developer.getStatusInterstitial().equals(Constant.KODE_ON)){
                            aktifdev++;
                        }
                        if(developer.getStatusVideo().equals(Constant.KODE_ON)){
                            aktifdev++;
                        }
                        if(developer.getStatus().equals(Constant.KODE_ON)){
                            onoff.setTextColor(ContextCompat.getColor(ListAplikasi.this, R.color.hijau));
                            onoff.setText("ON (" + String.valueOf(aktifdev) + "/3)");
                        }else{
                            onoff.setTextColor(ContextCompat.getColor(ListAplikasi.this, R.color.merah));
                            onoff.setText("OFF ("+ String.valueOf(aktifdev) + "/3)");
                        }

                        if(hasil.getJSONObject(Constant.DATA_LIST_APLIKASI).getString(Constant.KOLOM_STATUS).equals(Constant.SUKSES)){
                            JSONArray jsonArray = hasil.getJSONObject(Constant.DATA_LIST_APLIKASI).getJSONArray(Constant.HASIL);
                            List<Aplikasi> listAplikasi = new ArrayList<>();
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Aplikasi aplikasi = new Aplikasi();

                                aplikasi.setId(jsonObject.getString(Constant.KOLOM_ID));
                                aplikasi.setIdDeveloper(jsonObject.getString(Constant.KOLOM_IDDEVELOPER));
                                aplikasi.setNama(jsonObject.getString(Constant.KOLOM_NAMA));
                                aplikasi.setPacage(jsonObject.getString(Constant.KOLOM_PACKAGE));
                                aplikasi.setBanner(jsonObject.getString(Constant.KOLOM_BANNER));
                                aplikasi.setInterstitial(jsonObject.getString(Constant.KOLOM_INTERSTITIAL));
                                aplikasi.setVideo(jsonObject.getString(Constant.KOLOM_VIDEO));
                                aplikasi.setStatus(jsonObject.getString(Constant.KOLOM_STATUS));
                                aplikasi.setStatusBanner(jsonObject.getString(Constant.KOLOM_STATUSBANNER));
                                aplikasi.setStatusInterstitial(jsonObject.getString(Constant.KOLOM_STATUSINTERSTITIAL));
                                aplikasi.setStatusVideo(jsonObject.getString(Constant.KOLOM_STATUSVIDEO));
                                aplikasi.setTrafikTolak(jsonObject.getInt(Constant.KOLOM_TRAFIKTOLAK));
                                aplikasi.setTrafikTerima(jsonObject.getInt(Constant.KOLOM_TRAFITERIMA));
                                aplikasi.setDate(jsonObject.getString(Constant.KOLOM_DATE));

                                listAplikasi.add(aplikasi);

                            }
                            ListAplikasiAdapter listAplikasiAdapter = new ListAplikasiAdapter(listAplikasi, ListAplikasi.this, developer);
                            recyclerView.setAdapter(listAplikasiAdapter);
                        }else{
                            List<Aplikasi> listAplikasi = new ArrayList<>();
                            ListAplikasiAdapter listAplikasiAdapter = new ListAplikasiAdapter(listAplikasi, ListAplikasi.this, developer);
                            recyclerView.setAdapter(listAplikasiAdapter);
                            Toast.makeText(ListAplikasi.this, "Daftar aplikasi kosong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                   // Toast.makeText(ListAplikasi.this, response.getString(Constant.PESAN), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(ListAplikasi.this, "Network Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USERNAME, appController.getUsername());
                params.put(Constant.PASSWORD, appController.getPassword());
                params.put(Constant.PACKAGE, getPackageName());
                params.put(Constant.KOLOM_ID, idDeveloper);
                return params;
            }
        };
        appController.addToRequestQueue(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3 && data.getBooleanExtra(Constant.REFRESH, false)){
            refresh();
        }
    }

    public void bukaDetail(Intent intent){
        startActivityForResult(intent, 3);
    }


}
