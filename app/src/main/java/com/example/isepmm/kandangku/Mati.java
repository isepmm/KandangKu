package com.example.isepmm.kandangku;

/**
 * Created by Isepmm on 24/05/2018.
 */

public class Mati {
    private long tanggal_sekarang;
    private long jumlah_ayam_mati;
    private String id;

    public Mati(){

    }

    public Mati(long tanggal_sekarang, long jumlah_ayam_mati) {
        this.tanggal_sekarang = tanggal_sekarang;
        this.jumlah_ayam_mati = jumlah_ayam_mati;
    }

    public Mati(Mati mati, String id) {
        this.tanggal_sekarang = mati.getTanggal_sekarang();
        this.jumlah_ayam_mati = mati.getJumlah_ayam_mati();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public long getTanggal_sekarang() {
        return tanggal_sekarang;
    }

    public long getJumlah_ayam_mati() {
        return jumlah_ayam_mati;
    }
}
