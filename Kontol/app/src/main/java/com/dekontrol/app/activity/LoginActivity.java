package com.dekontrol.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private AppController appController;

    //private ProgressDialog dialog;
    //private boolean debug = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appController = (AppController) getApplication();

//        if(debug){
//            appController.setUsername("deadmin");
//            appController.setPassword("222222");
//            startActivity(new Intent(LoginActivity.this, ListDeveloper.class));
//            finish();
//        }


        //dialog = new ProgressDialog(this);

        username = findViewById(R.id.editUsername);
        password = findViewById(R.id.editPassword);




        if(appController.getUsername() != null && appController.getPassword() != null){
            startActivity(new Intent(this, ListDeveloper.class));
            finish();
        }
    }

    public void login(View v){
        if(username.getText().toString().isEmpty()){
            username.setError("tak boleh kosong");
            return;
        }

        if(password.getText().toString().isEmpty()){
            password.setError("tak boleh kosong");
            return;
        }

        findViewById(R.id.btn).setVisibility(View.GONE);
        findViewById(R.id.progress).setVisibility(View.VISIBLE);

        final String user = username.getText().toString();
        final String pass = password.getText().toString();

        //dialog.setMessage("Mengontak server...");
        //dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String responses) {
                //dialog.dismiss();
                findViewById(R.id.btn).setVisibility(View.VISIBLE);
                findViewById(R.id.progress).setVisibility(View.GONE);
                try {
                    JSONObject response = new JSONObject(responses);
                    String status = response.getString(Constant.STATUS);
                    if(status.equals(Constant.SUKSES)){
                        appController.setUsername(user);
                        appController.setPassword(pass);
                        startActivity(new Intent(LoginActivity.this, ListDeveloper.class));
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, response.getString(Constant.PESAN), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //dialog.dismiss();
                findViewById(R.id.btn).setVisibility(View.GONE);
                findViewById(R.id.progress).setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, "Network Error: " +error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USERNAME, user);
                params.put(Constant.PASSWORD, pass);
                params.put(Constant.PACKAGE, getPackageName());
                return params;
            }
        };
        appController.addToRequestQueue(stringRequest);
    }
}
