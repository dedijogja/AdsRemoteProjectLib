package com.declient.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dekontrol.clientlib.ClientLibCore;

public class SplashActivity extends AppCompatActivity implements ClientLibCore.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ClientLibCore.init(this);
    }

    @Override
    public void onInitComplete() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
