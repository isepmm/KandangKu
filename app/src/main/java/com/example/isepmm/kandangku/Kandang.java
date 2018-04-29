package com.example.isepmm.kandangku;

/**
 * Created by Isepmm on 02/04/2018.
 */

public class Kandang {
    private long tanggal_datang;
    private long tanggal_sekarang;
    private int jumlah_ayam;
    private int jumlah_ayam_mati;
    private long harga_doc;
    private long berat_panen;
    private long boarding;
    private long harga_pasar;
    private int konsumsi_minum;
    private int konsumsi_obat;
    private int konsumsi_pakan;
    private int konsumsi_vitamin;
    private int penggunaan_listrik;

    public Kandang(){

    }

    public Kandang(long tanggal_datang, int jumlah_ayam, long harga_doc, long berat_panen, long boarding, long harga_pasar, int konsumsi_minum, int konsumsi_obat, int konsumsi_pakan, int konsumsi_vitamin, int penggunaan_listrik) {
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

    public Kandang(long tanggal_sekarang, int jumlah_ayam, int jumlah_ayam_mati) {
        this.tanggal_sekarang = tanggal_sekarang;
        this.jumlah_ayam = jumlah_ayam;
        this.jumlah_ayam_mati = jumlah_ayam_mati;
    }

    public long getTanggal_datang() {
        return tanggal_datang;
    }

    public long getTanggal_sekarang() {
        return tanggal_sekarang;
    }

    public int getJumlah_ayam() {
        return jumlah_ayam;
    }

    public int getJumlah_ayam_mati() {
        return jumlah_ayam_mati;
    }

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

    public int getKonsumsi_minum() {
        return konsumsi_minum;
    }

    public int getKonsumsi_obat() {
        return konsumsi_obat;
    }

    public int getKonsumsi_pakan() {
        return konsumsi_pakan;
    }

    public int getKonsumsi_vitamin() {
        return konsumsi_vitamin;
    }

    public int getPenggunaan_listrik() {
        return penggunaan_listrik;
    }
}
