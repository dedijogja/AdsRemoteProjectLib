package com.dekontrol.app.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dekontrol.app.AppController;
import com.dekontrol.app.Constant;
import com.dekontrol.app.R;
import com.dekontrol.app.adapter.SpinAdapter;
import com.dekontrol.app.model.Aplikasi;
import com.dekontrol.app.model.Developer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailAplikasi extends AppCompatActivity {

    private AppController appController;
    private String idAplikasi, idDeveloper;
    private Developer developer;
    private Aplikasi aplikasi;
    private List<Developer> listDeveloper = new ArrayList<>();

    private EditText pakage, nama, banner, interstitial, video, trafikTolak, trafikTerima, record, appid, startapp, splash;
    private Spinner spinnerBanner, spinnerInterstitial, spinnerVideo, spinnerListDeveloper, spinnerStartApp;
    private Switch switchStatus;
    private RadioGroup grupDetailAplikasi;

    private TextView kesimpulanBanner, kesimpulanInterstitial, kesimpulanVideo, kesimpulanSplash, kesimpulanStartApp;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_aplikasi);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Mengontak server...");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("nama_aplikasi"));

        kesimpulanBanner = findViewById(R.id.txtKesimpulanBanner);
        kesimpulanInterstitial = findViewById(R.id.txtKesimpulanInterstitial);
        kesimpulanVideo = findViewById(R.id.txtKesimpulanVideo);
        kesimpulanSplash = findViewById(R.id.txtKesimpulanSplash);
        kesimpulanStartApp = findViewById(R.id.txtKesimpulanStartApp);

        spinnerListDeveloper = findViewById(R.id.spinnerListDeveloper);

        appid = findViewById(R.id.editAppIdDetailApliikasi);
        pakage = findViewById(R.id.editPackageAplikasi);
        nama = findViewById(R.id.editNamaAplikasi);
        banner = findViewById(R.id.editBannerAplikasi);
        interstitial = findViewById(R.id.editInterstitialAplikasi);
        video = findViewById(R.id.editVideoAplikasi);
        trafikTolak= findViewById(R.id.editTrafikTolak);
        trafikTerima = findViewById(R.id.editTrafikTerima);
        record = findViewById(R.id.editRecord);
        startapp = findViewById(R.id.editStartAppIdApp);
        splash = findViewById(R.id.editSplashAplikasi);

        spinnerBanner = findViewById(R.id.spinnerBannerAplikasi);
        spinnerInterstitial = findViewById(R.id.spinnerInterstitialAplikasi);
        spinnerVideo = findViewById(R.id.spinnerVideoAplikasi);
        switchStatus = findViewById(R.id.switchStatusAplikasi);
        spinnerStartApp = findViewById(R.id.spinnerStartAppApp);

        grupDetailAplikasi = findViewById(R.id.grupDetailAplikasi);

        appController = (AppController) getApplication();

        idAplikasi = getIntent().getStringExtra(Constant.KODE_INTENT);
        idDeveloper = getIntent().getStringExtra("id_developer");

        loadData();
    }

    private void loadData(){
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_DETAIL_APLIKASI, new Response.Listener<String>() {
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
                        developer = new Developer();
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
                        developer.setAppid(objectDeveloper.getString(Constant.KOLOM_APPID));
                        developer.setStatusSplash(objectDeveloper.getString(Constant.KOLOM_STATUSSPLASH));
                        developer.setStatusStartApp(objectDeveloper.getString(Constant.KOLOM_STATUSSTARTAPP));
                        developer.setStartApp(objectDeveloper.getString(Constant.KOLOM_STARTAPP));

                        idDeveloper = developer.getId();

                        JSONArray listDeveloperArray = hasil.getJSONArray(Constant.DATA_LIST_DEVELOPER);
                        for(int i = 0; i<listDeveloperArray.length(); i++){
                            Developer dev = new Developer();
                            JSONObject lDev = listDeveloperArray.getJSONObject(i);
                            dev.setId(lDev.getString(Constant.KOLOM_ID));
                            dev.setNama(lDev.getString(Constant.KOLOM_NAMA));
                            dev.setJumlahApp(lDev.getInt(Constant.KOLOM_APP));
                            dev.setJumlahTrafik(lDev.getInt(Constant.KOLOM_TRAFIK));
                            dev.setStatus(lDev.getString(Constant.KOLOM_STATUS));
                            dev.setStatusBanner(lDev.getString(Constant.KOLOM_STATUSBANNER));
                            dev.setStatusInterstitial(lDev.getString(Constant.KOLOM_STATUSINTERSTITIAL));
                            dev.setStatusVideo(lDev.getString(Constant.KOLOM_STATUSVIDEO));
                            dev.setBanner(lDev.getString(Constant.KOLOM_BANNER));
                            dev.setInterstitial(lDev.getString(Constant.KOLOM_INTERSTITIAL));
                            dev.setVideo(lDev.getString(Constant.KOLOM_VIDEO));
                            dev.setAppid(lDev.getString(Constant.KOLOM_APPID));
                            dev.setStatusSplash(lDev.getString(Constant.KOLOM_STATUSSPLASH));
                            dev.setStatusStartApp(lDev.getString(Constant.KOLOM_STATUSSTARTAPP));
                            dev.setStartApp(lDev.getString(Constant.KOLOM_STARTAPP));

                            listDeveloper.add(dev);
                        }

                        if(hasil.getJSONObject(Constant.DATA_APLIKASI).getString(Constant.STATUS).equals(Constant.SUKSES)){
                            JSONObject objectAplikasi = hasil.getJSONObject(Constant.DATA_APLIKASI).getJSONObject(Constant.HASIL);
                            aplikasi = new Aplikasi();

                            aplikasi.setId(objectAplikasi.getString(Constant.KOLOM_ID));
                            aplikasi.setNama(objectAplikasi.getString(Constant.KOLOM_NAMA));
                            aplikasi.setPacage(objectAplikasi.getString(Constant.KOLOM_PACKAGE));
                            aplikasi.setAppId(objectAplikasi.getString(Constant.KOLOM_APPID));
                            aplikasi.setBanner(objectAplikasi.getString(Constant.KOLOM_BANNER));
                            aplikasi.setInterstitial(objectAplikasi.getString(Constant.KOLOM_INTERSTITIAL));
                            aplikasi.setVideo(objectAplikasi.getString(Constant.KOLOM_VIDEO));
                            aplikasi.setStatus(objectAplikasi.getString(Constant.KOLOM_STATUS));
                            aplikasi.setStatusBanner(objectAplikasi.getString(Constant.KOLOM_STATUSBANNER));
                            aplikasi.setStatusInterstitial(objectAplikasi.getString(Constant.KOLOM_STATUSINTERSTITIAL));
                            aplikasi.setStatusVideo(objectAplikasi.getString(Constant.KOLOM_STATUSVIDEO));
                            aplikasi.setTrafikTolak(objectAplikasi.getInt(Constant.KOLOM_TRAFIKTOLAK));
                            aplikasi.setTrafikTerima(objectAplikasi.getInt(Constant.KOLOM_TRAFITERIMA));
                            aplikasi.setDate(objectAplikasi.getString(Constant.KOLOM_DATE));
                            aplikasi.setStatusSplash(objectAplikasi.getString(Constant.KOLOM_STATUSSPLASH));
                            aplikasi.setSplash(objectAplikasi.getString(Constant.KOLOM_SPLASH));
                            aplikasi.setStatusStartApp(objectAplikasi.getString(Constant.KOLOM_STATUSSTARTAPP));
                            aplikasi.setStartApp(objectAplikasi.getString(Constant.KOLOM_STARTAPP));

                            setData();
                        }else{
                            Toast.makeText(DetailAplikasi.this, "Terjadi kesalahan saat membaca data aplikasi!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(DetailAplikasi.this, "Newtwork Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USERNAME, appController.getUsername());
                params.put(Constant.PASSWORD, appController.getPassword());
                params.put(Constant.PACKAGE, getPackageName());
                params.put(Constant.KOLOM_IDAPLIKASI, idAplikasi);
                params.put(Constant.KOLOM_IDDEVELOPER, idDeveloper);
                return params;
            }
        };
        appController.addToRequestQueue(stringRequest);
    }

    private void setData(){
        pakage.setText(aplikasi.getPacage());
        nama.setText(aplikasi.getNama());
        banner.setText(aplikasi.getBanner());
        interstitial.setText(aplikasi.getInterstitial());
        video.setText(aplikasi.getVideo());
        appid.setText(aplikasi.getAppId());
        startapp.setText(aplikasi.getStartApp());
        splash.setText(aplikasi.getSplash());
        if(aplikasi.getStatus().equals("on")){
            switchStatus.setChecked(true);
        }else{
            switchStatus.setChecked(false);
        }
        switch (aplikasi.getStatusBanner()) {
            case "ondev":
                spinnerBanner.setSelection(1);
                break;
            case "onapp":
                spinnerBanner.setSelection(2);
                break;
            default:
                spinnerBanner.setSelection(0);
                break;
        }
        switch (aplikasi.getStatusInterstitial()) {
            case "ondev":
                spinnerInterstitial.setSelection(1);
                break;
            case "onapp":
                spinnerInterstitial.setSelection(2);
                break;
            default:
                spinnerInterstitial.setSelection(0);
                break;
        }
        switch (aplikasi.getStatusVideo()) {
            case "ondev":
                spinnerVideo.setSelection(1);
                break;
            case "onapp":
                spinnerVideo.setSelection(2);
                break;
            default:
                spinnerVideo.setSelection(0);
                break;
        }
        switch (aplikasi.getStatusSplash()){
            case "onstartappoffline":
                grupDetailAplikasi.check(R.id.gruponstartappoffline);
                break;
            case "onstartapponline":
                grupDetailAplikasi.check(R.id.gruponstartapponline);
                break;
            case "onadmob":
                grupDetailAplikasi.check(R.id.gruponadmob);
                break;
            case "ondev":
                grupDetailAplikasi.check(R.id.grupondev);
                break;
            default:
                grupDetailAplikasi.check(R.id.grupoff);
                break;
        }
        switch (aplikasi.getStatusStartApp()) {
            case "ondev":
                spinnerStartApp.setSelection(0);
                break;
            case "ononline":
                spinnerStartApp.setSelection(1);
                break;
            case "onoffline":
                spinnerStartApp.setSelection(2);
                break;
            default:
                spinnerStartApp.setSelection(3);
                break;
        }

        trafikTolak.setText(String.valueOf(aplikasi.getTrafikTolak()));
        trafikTerima.setText(String.valueOf(aplikasi.getTrafikTerima()));
        record.setText(aplikasi.getDate());

        List<String> daftarDev = new ArrayList<>();
        for(Developer d : listDeveloper){
            daftarDev.add(d.getNama());
        }
        SpinAdapter spinnerAdapter = new SpinAdapter(this, android.R.layout.simple_list_item_1, daftarDev);
        spinnerListDeveloper.setAdapter(spinnerAdapter);
        spinnerListDeveloper.setSelection(getIndexFromIdDeveloper(developer.getId()));

        dataBerubah();

        grupDetailAplikasi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                dataBerubah();
            }
        });

        spinnerStartApp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataBerubah();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerListDeveloper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                developer = listDeveloper.get(position);
                dataBerubah();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataBerubah();
            }
        });
        spinnerBanner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataBerubah();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerInterstitial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataBerubah();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerVideo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataBerubah();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int getIndexFromIdDeveloper(String idDeveloper){
        for(int i = 0; i<listDeveloper.size(); i++){
            if(idDeveloper.equals(listDeveloper.get(i).getId())){
                return i;
            }
        }
        return 0;
    }

    private void dataBerubah(){
        if(!developer.getStatusStartApp().equals("off")){
            switch (spinnerStartApp.getSelectedItem().toString()) {
                case "ondev":
                    if (developer.getStatusStartApp().equals("onoffline")) {
                        setOn(kesimpulanStartApp, "onoffline (" + "DEV" + ")", ContextCompat.getColor(this, R.color.orange));
                    } else {
                        setOn(kesimpulanStartApp, "ononline (DEV) (" + developer.getStartApp() + ")", ContextCompat.getColor(this, R.color.hijau));
                    }
                    break;
                case "ononline":
                    setOn(kesimpulanStartApp, "ononline (APP) (" + aplikasi.getStartApp() + ")", ContextCompat.getColor(this, R.color.hijau));
                    break;
                case "onoffline":
                    setOn(kesimpulanStartApp, "onoffline (APP)", ContextCompat.getColor(this, R.color.orange));
                    break;
                default:
                    setOff(kesimpulanStartApp);
                    break;
            }

        }else{
            setOff(kesimpulanStartApp);
        }


        if(!developer.getStatusSplash().equals("off")){
            switch (grupDetailAplikasi.getCheckedRadioButtonId()) {
                case R.id.gruponstartappoffline:
                    setOn(kesimpulanSplash, "ONSTARTAPP (APP) (" + "offline" + ")", ContextCompat.getColor(this, R.color.hijau));
                    break;
                case R.id.gruponstartapponline:
                    setOn(kesimpulanSplash, "ONSTARTAPP (APP) (" + "online" + ")", ContextCompat.getColor(this, R.color.hijau));
                    break;
                case R.id.gruponadmob:
                    setOn(kesimpulanSplash, "ONADMOB (" + "APP" + ")", ContextCompat.getColor(this, R.color.hijau));
                    break;
                case R.id.grupondev:
                    switch (developer.getStatusSplash()) {
                        case "onstartappoffline":
                            setOn(kesimpulanSplash, "ONSTARTAPP (DEV) (" + "offline" + ")", ContextCompat.getColor(this, R.color.orange));
                            break;
                        case "onstartapponline":
                            setOn(kesimpulanSplash, "ONSTARTAPP (DEV) (" + "online" + ")", ContextCompat.getColor(this, R.color.orange));
                            break;
                        case "onadmob":
                            setOn(kesimpulanSplash, "ONADMOB (" + "DEV" + ")", ContextCompat.getColor(this, R.color.orange));
                            break;
                        default:
                            setOff(kesimpulanSplash);
                            break;
                    }
                    break;
                default:
                    setOff(kesimpulanSplash);
                    break;
            }

        }else{
            setOff(kesimpulanSplash);
        }


        if(developer.getStatus().equals(Constant.KODE_ON)){
            if(switchStatus.isChecked()){

                if(developer.getStatusBanner().equals(Constant.KODE_ON)){
                    switch (spinnerBanner.getSelectedItem().toString()) {
                        case Constant.KODE_ONDEV:
                            setOn(kesimpulanBanner, "ONDEV (" + developer.getBanner() + ")", ContextCompat.getColor(this, R.color.orange));
                            break;
                        case Constant.KODE_ONAPP:
                            setOn(kesimpulanBanner, "ONAPP (" + aplikasi.getBanner() + ")", ContextCompat.getColor(this, R.color.hijau));
                            break;
                        default:
                            setOff(kesimpulanBanner);
                            break;
                    }

                }else{
                    setOff(kesimpulanBanner);
                }

                if(developer.getStatusInterstitial().equals(Constant.KODE_ON)){
                    switch (spinnerInterstitial.getSelectedItem().toString()) {
                        case Constant.KODE_ONDEV:
                            setOn(kesimpulanInterstitial, "ONDEV (" + developer.getInterstitial() + ")", ContextCompat.getColor(this, R.color.orange));
                            break;
                        case Constant.KODE_ONAPP:
                            setOn(kesimpulanInterstitial, "ONAPP (" + aplikasi.getInterstitial() + ")", ContextCompat.getColor(this, R.color.hijau));
                            break;
                        default:
                            setOff(kesimpulanInterstitial);
                            break;
                    }
                }else{
                    setOff(kesimpulanInterstitial);
                }

                if(developer.getStatusVideo().equals(Constant.KODE_ON)){
                    switch (spinnerVideo.getSelectedItem().toString()) {
                        case Constant.KODE_ONDEV:
                            setOn(kesimpulanVideo, "ONDEV (" + developer.getVideo() + ")", ContextCompat.getColor(this, R.color.orange));
                            break;
                        case Constant.KODE_ONAPP:
                            setOn(kesimpulanVideo, "ONAPP (" + aplikasi.getVideo() + ")", ContextCompat.getColor(this, R.color.hijau));
                            break;
                        default:
                            setOff(kesimpulanVideo);
                            break;
                    }
                }else{
                    setOff(kesimpulanVideo);
                }
            }else{
                setOff(kesimpulanBanner);
                setOff(kesimpulanInterstitial);
                setOff(kesimpulanVideo);
            }
        }else{
            setOff(kesimpulanBanner);
            setOff(kesimpulanInterstitial);
            setOff(kesimpulanVideo);
        }
    }

    private void setOff(TextView textView){
        textView.setText("OFF");
        textView.setTextColor(ContextCompat.getColor(this, R.color.merah));
    }

    private void setOn(TextView textView, String mesage, int color){
        textView.setText(mesage);
        textView.setTextColor(color);
    }

    private String status, statusBanner, statusInterstitial, statusVideo, statusSplash, statusStartApp;
    public void perbaruiAplikasi(View view){
        final String nam = nama.getText().toString();
        final String ban = banner.getText().toString();
        final String ins = interstitial.getText().toString();
        final String vid = video.getText().toString();
        final String apid = appid.getText().toString();
        final String setarApp = startapp.getText().toString();
        final String seplash = splash.getText().toString();

        idDeveloper = developer.getId();

        status = Constant.KODE_OFF;
        statusBanner = Constant.KODE_OFF;
        statusInterstitial = Constant.KODE_OFF;
        statusVideo = Constant.KODE_OFF;
        statusSplash = "ondev";
        statusStartApp = "ondev";

        if(switchStatus.isChecked()){
            status = Constant.KODE_ON;
        }

        statusBanner = spinnerBanner.getSelectedItem().toString();
        statusInterstitial = spinnerInterstitial.getSelectedItem().toString();
        statusVideo = spinnerVideo.getSelectedItem().toString();
        statusStartApp = spinnerStartApp.getSelectedItem().toString();

        int idx = grupDetailAplikasi.getCheckedRadioButtonId();
        RadioButton rdButton = findViewById(idx);
        statusSplash = rdButton.getText().toString();

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
            appid.setError("Tak Boleh Kosong!");
            fail = true;
        }
        if(setarApp.isEmpty()){
            startapp.setError("Tak Boleh Kosong!");
            fail = true;
        }
        if(seplash.isEmpty()){
            splash.setError("Tak Boleh Kosong!");
            fail = true;
        }

        if(fail){
            return;
        }

        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_EDIT_APLIKASI, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String responses) {
                dialog.dismiss();
                try {
                    JSONObject response = new JSONObject(responses);
                    String status = response.getString(Constant.STATUS);
                    if(status.equals(Constant.SUKSES)){
                        refresh = true;
                        Toast.makeText(DetailAplikasi.this, response.getString(Constant.PESAN), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DetailAplikasi.this, "Network Error: " +error.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put(Constant.KOLOM_IDAPLIKASI, idAplikasi);
                params.put(Constant.KOLOM_IDDEVELOPER, idDeveloper);
                params.put(Constant.KOLOM_STATUS, status);
                params.put(Constant.KOLOM_STATUSBANNER, statusBanner);
                params.put(Constant.KOLOM_STATUSINTERSTITIAL, statusInterstitial);
                params.put(Constant.KOLOM_STATUSVIDEO, statusVideo);
                params.put(Constant.KOLOM_APPID, apid);
                params.put(Constant.KOLOM_STATUSSPLASH, statusSplash);
                params.put(Constant.KOLOM_STATUSSTARTAPP, statusStartApp);
                params.put(Constant.KOLOM_STARTAPP, setarApp);
                params.put(Constant.KOLOM_SPLASH, seplash);
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
        getMenuInflater().inflate(R.menu.menu_detail_aplikasi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.refresh_detail_aplikasi:
                loadData();
                return true;
            case R.id.hapus_detail_aplikasi:
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Konfirmasi");
                alertDialog.setMessage("Apakah anda yakin ingin mengahpus data aplikasi ini?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hapusAplikasi();
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

    private void hapusAplikasi(){
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_HAPUS_APLIKASI, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String responses) {
                dialog.dismiss();
                try {
                    JSONObject response = new JSONObject(responses);
                    String status = response.getString(Constant.STATUS);
                    if(status.equals(Constant.SUKSES)){
                        //sukses dihapus
                        Toast.makeText(DetailAplikasi.this, response.getString(Constant.PESAN), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DetailAplikasi.this, "Network Error: "+ error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USERNAME, appController.getUsername());
                params.put(Constant.PASSWORD, appController.getPassword());
                params.put(Constant.PACKAGE, getPackageName());
                params.put(Constant.KOLOM_ID, idAplikasi);
                return params;
            }
        };
        appController.addToRequestQueue(stringRequest);
    }

}
