package com.example.isepmm.kandangku;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Isepmm on 02/04/2018.
 */

public class Kandang {
    private long tanggal_datang;
    //private long tanggal_sekarang;
    private long jumlah_ayam;
    //private long jumlah_ayam_mati;
    private long harga_doc;
    private long berat_panen;
    private long boarding;
    private long harga_pasar;
    private long konsumsi_minum;
    private long konsumsi_obat;
    private long konsumsi_pakan;
    private long konsumsi_vitamin;
    private long penggunaan_listrik;

    public Kandang(){

    }

    public Kandang(long tanggal_datang, long jumlah_ayam, long harga_doc, long berat_panen, long boarding, long harga_pasar, long konsumsi_minum, long konsumsi_obat, long konsumsi_pakan, long konsumsi_vitamin, long penggunaan_listrik) {
        this.tanggal_datang = tanggal_datang;
        this.jumlah_ayam = jumlah_ayam;
        this.harga_doc = harga_doc;
        this.berat_panen = berat_panen;
        this.boarding = boarding;
        this.harga_pasar = harga_pasar;
        this.konsumsi_minum = konsumsi_minum;
        this.konsumsi_obat = konsumsi_obat;
        this.konsumsi_pakan = konsumsi_pakan;
        this.konsumsi_vitamin = konsumsi_vitamin;
        this.penggunaan_listrik = penggunaan_listrik;
    }

//    public Kandang(long tanggal_sekarang, long jumlah_ayam, long jumlah_ayam_mati) {
//        this.tanggal_sekarang = tanggal_sekarang;
//        this.jumlah_ayam = jumlah_ayam;
//        this.jumlah_ayam_mati = jumlah_ayam_mati;
//    }

    public long getTanggal_datang() {
        return tanggal_datang;
    }

    public long getJumlah_ayam() {
        return jumlah_ayam;
    }

//    public long getJumlah_ayam_mati() {
//        return jumlah_ayam_mati;
//    }

    public long getHarga_doc() {
        return harga_doc;
    }

    public long getBerat_panen() {
        return berat_panen;
    }

    public long getBoarding() {
        return boarding;
    }

    public long getHarga_pasar() {
        return harga_pasar;
    }

    public long getKonsumsi_minum() {
        return konsumsi_minum;
    }

    public long getKonsumsi_obat() {
        return konsumsi_obat;
    }

    public long getKonsumsi_pakan() {
        return konsumsi_pakan;
    }

    public long getKonsumsi_vitamin() {
        return konsumsi_vitamin;
    }

    public long getPenggunaan_listrik() {
        return penggunaan_listrik;
    }

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("tanggal_datang", tanggal_datang);
        result.put("jumlah_ayam", jumlah_ayam);
        result.put("harga_doc", harga_doc);
        result.put("berat_panen", berat_panen);
        result.put("boarding", boarding);
        result.put("harga_pasar", harga_pasar);
        result.put("konsumsi_minum", konsumsi_minum);
        result.put("konsumsi_pakan", konsumsi_pakan);
        result.put("konsumsi_vitamin", konsumsi_vitamin);
        result.put("konsumsi_obat", konsumsi_obat);
        result.put("penggunaan_listrik", penggunaan_listrik);

        return result;
    }
}
