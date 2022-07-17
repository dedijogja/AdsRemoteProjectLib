package com.dekontrol.clientlib.decryption.text.deskriptor;


import com.dekontrol.clientlib.decryption.text.algoritma.DeskriptorAlgoritma;

public class DeskripsiText extends DeskriptorAlgoritma {

    public DeskripsiText(){

    }

    public DeskripsiText(String key, String textUntukDiDeskripsi){
        setKey(key);
        setTextUntukDiDeskripsi(textUntukDiDeskripsi);
    }

    @Override
    public void setKey(String key) {
        super.setKey(key);
    }

    @Override
    public void setTextUntukDiDeskripsi(String textUntukDiDeskripsi) {
        super.setTextUntukDiDeskripsi(textUntukDiDeskripsi);
    }

    @Override
    public String dapatkanTextAsli() {
        return super.dapatkanTextAsli();
    }
}
