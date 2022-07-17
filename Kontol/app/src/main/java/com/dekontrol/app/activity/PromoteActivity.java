package com.dekontrol.app.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dekontrol.app.AppController;
import com.dekontrol.app.Constant;
import com.dekontrol.app.R;
import com.dekontrol.app.RecycleViewMargin;
import com.dekontrol.app.adapter.ListPromoteAdapter;
import com.dekontrol.app.model.Promote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromoteActivity extends AppCompatActivity {

    private String idDeveloper, developerName;
    private RecyclerView recyclerView;
    private AppController appController;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote);

        idDeveloper = getIntent().getStringExtra(Constant.KOLOM_IDDEVELOPER);
        developerName = getIntent().getStringExtra(Constant.KOLOM_NAMA);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Mengontak server...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Promote : " + developerName);

        recyclerView = findViewById(R.id.recyclePromote);
        recyclerView.addItemDecoration(new RecycleViewMargin(2, this));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        appController = (AppController) getApplication();

        reffresh();
    }

    private int jumlahPromote = 0;
    private void reffresh(){
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_LIST_PROMOTE, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String responses) {
                dialog.dismiss();
                try {
                    JSONObject response = new JSONObject(responses);
                    String status = response.getString(Constant.STATUS);
                    if(status.equals(Constant.SUKSES)){
                        JSONObject hasil = response.getJSONObject(Constant.HASIL);
                        if(hasil.getJSONObject(Constant.DATA_LIST_PROMOTE).getString(Constant.KOLOM_STATUS).equals(Constant.SUKSES)){
                            JSONArray jsonArray = hasil.getJSONObject(Constant.DATA_LIST_PROMOTE).getJSONArray(Constant.HASIL);
                            List<Promote> listPromote = new ArrayList<>();
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Promote promote = new Promote();
                                promote.setId(jsonObject.getString(Constant.KOLOM_ID));
                                promote.setIdDeveloper(jsonObject.getString(Constant.KOLOM_IDDEVELOPER));
                                promote.setNama(jsonObject.getString(Constant.KOLOM_NAMA));
                                promote.setPacage(jsonObject.getString(Constant.KOLOM_PACKAGE));
                                promote.setIklan(jsonObject.getString(Constant.KOLOM_IKLAN));
                                promote.setPersen(jsonObject.getInt(Constant.KOLOM_PERSEN));
                                listPromote.add(promote);
                            }
                            ListPromoteAdapter listPromoteAdapter = new ListPromoteAdapter(listPromote, PromoteActivity.this);
                            recyclerView.setVisibility(View.VISIBLE);
                            findViewById(R.id.txtPromoteOff).setVisibility(View.GONE);
                            recyclerView.setAdapter(listPromoteAdapter);

                            jumlahPromote = 0;
                            for(Promote promote : listPromote){
                                jumlahPromote += promote.getPersen();
                            }
                            findViewById(R.id.btnTambahPromote).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(PromoteActivity.this, TambahPromote.class);
                                    intent.putExtra(Constant.SISA_PERSEN, 100-jumlahPromote);
                                    intent.putExtra(Constant.KOLOM_IDDEVELOPER, idDeveloper);
                                    intent.putExtra(Constant.KOLOM_NAMA, developerName);
                                    startActivityForResult(intent, 3);
                                }
                            });

                        }else{
                            List<Promote> listEmptyPromote = new ArrayList<>();
                            ListPromoteAdapter listPromoteAdapter = new ListPromoteAdapter(listEmptyPromote, PromoteActivity.this);
                            recyclerView.setVisibility(View.GONE);
                            findViewById(R.id.txtPromoteOff).setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(listPromoteAdapter);

                            //enable button
                            findViewById(R.id.btnTambahPromote).setEnabled(true);
                            findViewById(R.id.btnTambahPromote).setClickable(true);
                            findViewById(R.id.btnTambahPromote).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(PromoteActivity.this, TambahPromote.class);
                                    intent.putExtra(Constant.SISA_PERSEN, 100);
                                    intent.putExtra(Constant.KOLOM_IDDEVELOPER, idDeveloper);
                                    intent.putExtra(Constant.KOLOM_NAMA, developerName);
                                    startActivityForResult(intent, 3);
                                }
                            });
                        }
                    }else{
                        Toast.makeText(PromoteActivity.this, "Terjadi kesalahan tak dikenal!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PromoteActivity.this, "Network Error: " +error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USERNAME, appController.getUsername());
                params.put(Constant.PASSWORD, appController.getPassword());
                params.put(Constant.KOLOM_PACKAGE, getPackageName());
                params.put(Constant.KOLOM_IDDEVELOPER, idDeveloper);
                return params;
            }
        };
        appController.addToRequestQueue(stringRequest);
    }

    public void bukaDetailPromote(String idPromote, String namaApp, int maxPersen){
        Intent intent = new Intent(this, DetailPromote.class);
        intent.putExtra(Constant.KOLOM_ID, idPromote);
        intent.putExtra(Constant.KOLOM_NAMA, namaApp);
        intent.putExtra(Constant.KOLOM_MAX_PERSEN, maxPersen);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3 && data.getBooleanExtra(Constant.REFRESH, false)){
            reffresh();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_promote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.refresh_promote:
                reffresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
       finish();
    }
}
