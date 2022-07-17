package com.dekontrol.app.model;

public class Promote {
    private String id, idDeveloper, nama, pacage, iklan;
    private int persen;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdDeveloper() {
        return idDeveloper;
    }

    public void setIdDeveloper(String idDeveloper) {
        this.idDeveloper = idDeveloper;
    }

    public String getPacage() {
        return pacage;
    }

    public void setPacage(String pacage) {
        this.pacage = pacage;
    }

    public String getIklan() {
        return iklan;
    }

    public void setIklan(String iklan) {
        this.iklan = iklan;
    }

    public int getPersen() {
        return persen;
    }

    public void setPersen(int persen) {
        this.persen = persen;
    }
}
