package com.dekontrol.app.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dekontrol.app.AppController;
import com.dekontrol.app.Constant;
import com.dekontrol.app.R;
import com.dekontrol.app.model.Promote;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailPromote extends AppCompatActivity {

    private TextView txtPersentase;
    private EditText editNama, editPackage, editIklan, editPersen;

    private AppController appController;
    private ProgressDialog dialog;

    private String idPromote;
    private int maxPersen;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_promote);

        idPromote = getIntent().getStringExtra(Constant.KOLOM_ID);
        String namaPromote = getIntent().getStringExtra(Constant.KOLOM_NAMA);
        maxPersen = getIntent().getIntExtra(Constant.KOLOM_MAX_PERSEN, 0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(namaPromote);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Mengontak server...");

        appController = (AppController) getApplication();

        txtPersentase = findViewById(R.id.txtPersentase);
        editNama = findViewById(R.id.promoteNamaApp);
        editPackage = findViewById(R.id.promotePackage);
        editIklan = findViewById(R.id.promoteLinkIklan);
        editPersen = findViewById(R.id.promotePersentaseMuncul);

        loadData();
    }

    private int newMaxPersen;
    private String idDeveloper;
    private void loadData(){
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_DETAIL_PROMOTE, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String responses) {
                dialog.dismiss();
                try {
                    JSONObject response = new JSONObject(responses);
                    String status = response.getString(Constant.STATUS);
                    if(status.equals(Constant.SUKSES)){
                        JSONObject hasil = response.getJSONObject(Constant.HASIL);
                        if(hasil.getJSONObject(Constant.DATA_PROMOTE).getString(Constant.KOLOM_STATUS).equals(Constant.SUKSES)){
                            JSONObject promoteObject = hasil.getJSONObject(Constant.DATA_PROMOTE).getJSONObject(Constant.HASIL);
                            Promote promote = new Promote();
                            promote.setId(promoteObject.getString(Constant.KOLOM_ID));
                            promote.setIdDeveloper(promoteObject.getString(Constant.KOLOM_IDDEVELOPER));
                            promote.setNama(promoteObject.getString(Constant.KOLOM_NAMA));
                            promote.setPacage(promoteObject.getString(Constant.KOLOM_PACKAGE));
                            promote.setIklan(promoteObject.getString(Constant.KOLOM_IKLAN));
                            promote.setPersen(promoteObject.getInt(Constant.KOLOM_PERSEN));

                            editNama.setText(promote.getNama());
                            editPackage.setText(promote.getPacage());
                            editIklan.setText(promote.getIklan());
                            editPersen.setText(String.valueOf(promote.getPersen()));
                            newMaxPersen = maxPersen+promote.getPersen();
                            txtPersentase.setText("Persentase (MAX : " + String.valueOf(newMaxPersen) + ") : ");
                            idDeveloper = promote.getIdDeveloper();
                        }else{
                            Toast.makeText(DetailPromote.this, "Data promote tak ditemukan!", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }

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
                Toast.makeText(DetailPromote.this, "Newtwork Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USERNAME, appController.getUsername());
                params.put(Constant.PASSWORD, appController.getPassword());
                params.put(Constant.PACKAGE, getPackageName());
                params.put(Constant.KOLOM_ID, idPromote);
                return params;
            }
        };
        appController.addToRequestQueue(stringRequest);
    }

    public void simpanPerubahan(View view){
        final String nama, pacage, iklan, persen;

        nama = editNama.getText().toString();
        pacage = editPackage.getText().toString();
        iklan = editIklan.getText().toString();
        persen = editPersen.getText().toString();

        boolean fail = false;
        if(nama.isEmpty()){
            editNama.setError("Tak boleh kosong!");
            fail = true;
        }

        if(pacage.isEmpty()){
            editPackage.setError("Tak boleh kosong!");
            fail = true;
        }

        if(iklan.isEmpty()){
            editIklan.setError("Tak boleh kosong!");
            fail = true;
        }

        if(persen.isEmpty()){
            editPersen.setError("Tak boleh kosong!");
            fail = true;
        }

        if(Integer.parseInt(persen) > newMaxPersen){
            editPersen.setError("Melebihi MAX!");
            fail = true;
        }

        if(fail){
            return;
        }

        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_EDIT_PROMOTE, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String responses) {
                dialog.dismiss();
                try {
                    JSONObject response = new JSONObject(responses);
                    String status = response.getString(Constant.STATUS);
                    if(status.equals(Constant.SUKSES)){
                        //sukses diperbarui
                        refresh = true;
                    }
                    Toast.makeText(DetailPromote.this, response.getString(Constant.PESAN), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(DetailPromote.this, "Network Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USERNAME, appController.getUsername());
                params.put(Constant.PASSWORD, appController.getPassword());
                params.put(Constant.PACKAGE, getPackageName());
                params.put(Constant.KOLOM_NAMA, nama);
                params.put(Constant.KOLOM_PACKAGE_PROMOTE, pacage);
                params.put(Constant.KOLOM_IKLAN, iklan);
                params.put(Constant.KOLOM_PERSEN, persen);
                params.put(Constant.KOLOM_IDDEVELOPER, idDeveloper);
                params.put(Constant.KOLOM_ID, idPromote);
                return params;
            }
        };
        appController.addToRequestQueue(stringRequest);
    }

    boolean refresh = false;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constant.REFRESH, refresh);
        setResult(3, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_promote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.refresh_detail_promote:
                loadData();
                return true;
            case R.id.hapus_detail_promote:
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Konfirmasi");
                alertDialog.setMessage("Apakah anda yakin ingin mengahpus data promote ini?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hapusPromote();
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

    private void hapusPromote(){
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_HAPUS_PROMOTE, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String responses) {
                dialog.dismiss();
                try {
                    JSONObject response = new JSONObject(responses);
                    String status = response.getString(Constant.STATUS);
                    if(status.equals(Constant.SUKSES)){
                        //sukses dihapus
                        Toast.makeText(DetailPromote.this, response.getString(Constant.PESAN), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DetailPromote.this, "Network Error: "+ error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USERNAME, appController.getUsername());
                params.put(Constant.PASSWORD, appController.getPassword());
                params.put(Constant.PACKAGE, getPackageName());
                params.put(Constant.KOLOM_ID, idPromote);
                return params;
            }
        };
        appController.addToRequestQueue(stringRequest);
    }


}
