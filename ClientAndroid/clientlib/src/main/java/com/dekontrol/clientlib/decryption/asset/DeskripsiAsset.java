package com.dekontrol.clientlib.decryption.asset;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DeskripsiAsset extends AsyncTask<String, Void, byte[]>{
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String key = "";
    private byte[] byteResult = null;

    private ListenerDecrypt listenerDecrypt;

    public DeskripsiAsset(Context context, String key){
        this.context = context;
        this.key = key;
    }


    @Override
    protected byte[] doInBackground(String... params) {
        SecretKey baru =  new SecretKeySpec(Base64.decode(key, Base64.DEFAULT),
                0, Base64.decode(key, Base64.DEFAULT).length, "AES");
        try {
            InputStream inputStream = context.getAssets().open(params[0]);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            Cipher AesCipher = Cipher.getInstance("AES");
            AesCipher.init(Cipher.DECRYPT_MODE, baru);
            byteResult = AesCipher.doFinal(bytes);

        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | IOException | BadPaddingException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return byteResult;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        listenerDecrypt.onSelesaiDecrypt(bytes);
    }

    public void setListenerDecrypt(ListenerDecrypt listenerDecrypts){
        if(listenerDecrypt == null){
            this.listenerDecrypt = listenerDecrypts;
        }
    }

    public interface ListenerDecrypt{
        void onSelesaiDecrypt(byte[] bytes);
    }

}
