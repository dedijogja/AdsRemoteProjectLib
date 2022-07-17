package com.declient.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dekontrol.clientlib.ClientLibCore;
import com.dekontrol.clientlib.iklan.KodeIklanHandler;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClientLibCore.initStartApp(this);

        setContentView(R.layout.activity_main);

        LinearLayout wdah = findViewById(R.id.wadahAdsBesar);
        ClientLibCore.tempatkanIklanBanner(this, wdah, ClientLibCore.AdSize.MEDIUM_RECTANGLE);

        TextView tv = findViewById(R.id.sample_text);
        KodeIklanHandler kodeIklanHandler = new KodeIklanHandler(this);
        tv.setText(kodeIklanHandler.getKeyAssets(this));
    }



}
