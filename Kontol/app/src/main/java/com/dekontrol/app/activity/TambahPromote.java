package com.dekontrol.app.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TambahPromote extends AppCompatActivity {

    private TextView txtPersentase;
    private EditText editNama, editPackage, editIklan, editPersen;
    private int maxPersen;
    private String idDeveloper;

    private AppController appController;
    private ProgressDialog dialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_promote);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Mengontak server...");

        appController = (AppController) getApplication();

        maxPersen = getIntent().getIntExtra(Constant.SISA_PERSEN, 0);
        idDeveloper = getIntent().getStringExtra(Constant.KOLOM_IDDEVELOPER);
        String namaDeveloper = getIntent().getStringExtra(Constant.KOLOM_NAMA);

        getSupportActionBar().setTitle("Tambah Promote : " + namaDeveloper);

        txtPersentase = findViewById(R.id.txtPersentase);
        txtPersentase.setText("Persentase (MAX : " + String.valueOf(maxPersen) + ") : ");

        editNama = findViewById(R.id.promoteNamaApp);
        editPackage = findViewById(R.id.promotePackage);
        editIklan = findViewById(R.id.promoteLinkIklan);
        editPersen = findViewById(R.id.promotePersentaseMuncul);
    }

    public void simpanPromote(View view){
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
        }else if(Integer.parseInt(persen) > maxPersen){
            editPersen.setError("Melebihi MAX!");
            fail = true;
        }

        if(fail){
            return;
        }

        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_TAMBAH_PROMOTE, new Response.Listener<String>() {
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
                    Toast.makeText(TambahPromote.this, response.getString(Constant.PESAN), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(TambahPromote.this, "Network Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
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
                return params;
            }
        };
        appController.addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constant.REFRESH, true);
        setResult(3, intent);
        finish();
    }
}
