package com.dekontrol.app.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.dekontrol.app.adapter.ListDeveloperAdapter;
import com.dekontrol.app.model.Developer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDeveloper extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppController appController;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_developer);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Mengontak server...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("List Developer");

        recyclerView = findViewById(R.id.recycleListDeveloper);
        recyclerView.addItemDecoration(new RecycleViewMargin(2, this));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        appController = (AppController) getApplication();

        reffresh();
    }

    public void bukaListApp(String idDeveloper, String namaDev){
        Intent intent = new Intent(this, ListAplikasi.class);
        intent.putExtra(Constant.KODE_INTENT, idDeveloper);
        intent.putExtra("nama_dev", namaDev);
        startActivityForResult(intent, 3);
    }

    public void tambahDeveloper(View view){
        Intent intent = new Intent(this, TambahDeveloper.class);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3 && data.getBooleanExtra(Constant.REFRESH, false)){
            reffresh();
        }
    }

    private void reffresh(){
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_LIST_DEVELOPER, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String responses) {
               dialog.dismiss();
                try {
                    JSONObject response = new JSONObject(responses);
                    String status = response.getString(Constant.STATUS);
                    if(status.equals(Constant.SUKSES)){
                        JSONObject hasil = response.getJSONObject(Constant.HASIL);
                        if(hasil.getJSONObject(Constant.DATA_LIST_DEVELOPER).getString(Constant.KOLOM_STATUS).equals(Constant.SUKSES)){
                            JSONArray jsonArray = hasil.getJSONObject(Constant.DATA_LIST_DEVELOPER).getJSONArray(Constant.HASIL);
                            List<Developer> listDeveloper = new ArrayList<>();
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Developer developer = new Developer();
                                developer.setId(jsonObject.getString(Constant.KOLOM_ID));
                                developer.setNama(jsonObject.getString(Constant.KOLOM_NAMA));
                                developer.setBanner(jsonObject.getString(Constant.KOLOM_BANNER));
                                developer.setInterstitial(jsonObject.getString(Constant.KOLOM_INTERSTITIAL));
                                developer.setVideo(jsonObject.getString(Constant.KOLOM_VIDEO));
                                developer.setStatus(jsonObject.getString(Constant.KOLOM_STATUS));
                                developer.setStatusBanner(jsonObject.getString(Constant.KOLOM_STATUSBANNER));
                                developer.setStatusInterstitial(jsonObject.getString(Constant.KOLOM_STATUSINTERSTITIAL));
                                developer.setStatusVideo(jsonObject.getString(Constant.KOLOM_STATUSVIDEO));
                                developer.setJumlahApp(jsonObject.getInt(Constant.KOLOM_APP));
                                developer.setJumlahTrafik(jsonObject.getInt(Constant.KOLOM_TRAFIK));
                                listDeveloper.add(developer);
                            }
                            ListDeveloperAdapter listDeveloperAdapter = new ListDeveloperAdapter(listDeveloper, ListDeveloper.this);
                            recyclerView.setAdapter(listDeveloperAdapter);

                            ((TextView) findViewById(R.id.txtTotalDeveloper)).setText("Total : " + String.valueOf(listDeveloper.size()) + " Developer");
                        }else{
                            List<Developer> listEmptyDeveloper = new ArrayList<>();
                            ListDeveloperAdapter listDeveloperAdapterEmpty = new ListDeveloperAdapter(listEmptyDeveloper, ListDeveloper.this);
                            recyclerView.setAdapter(listDeveloperAdapterEmpty);

                            ((TextView) findViewById(R.id.txtTotalDeveloper)).setText("Total : " + String.valueOf(listEmptyDeveloper.size()) + " Developer");
                            Toast.makeText(ListDeveloper.this, "Daftar developer kosong!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ListDeveloper.this, "Terjadi kesalahan tak dikenal!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ListDeveloper.this, "Network Error: " +error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USERNAME, appController.getUsername());
                params.put(Constant.PASSWORD, appController.getPassword());
                params.put(Constant.PACKAGE, getPackageName());
                return params;
            }
        };
        appController.addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_developer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.refresh_developer:
                reffresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Konfirmasi");
        alertDialog.setMessage("Apakah anda ingin menutup aplikasi ini dan logout dari server?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                appController.logout();
                finish();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
