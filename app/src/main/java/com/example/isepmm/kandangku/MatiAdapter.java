package com.example.isepmm.kandangku;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Isepmm on 25/05/2018.
 */

public class MatiAdapter extends ArrayAdapter<Mati> {
    private TextView ayammati;
    private TextView tanggalsekarang;

    private String bulan[] = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};

    public MatiAdapter(@NonNull Context context, @NonNull ArrayList<Mati> objects) {
        super(context, 0, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_view_history,parent,false);
        }

        Mati current = getItem(position);

        ayammati = (TextView) convertView.findViewById(R.id.ayam_mati);
        tanggalsekarang = (TextView) convertView.findViewById(R.id.tanggal_datang);

        Mati mati = getItem(position);
        long jumlah = mati.getJumlah_ayam_mati();
        long tanggal = mati.getTanggal_sekarang();

        ayammati.setText(Long.toString(jumlah));
        tanggalsekarang.setText(Long.toString(tanggal));

        assert current != null;
        String mTahun_datang = unixTimestimeToString(current.getTanggal_sekarang())[0];
        String mTanggal_datang = unixTimestimeToString(current.getTanggal_sekarang())[2];
        int monthNumber = Integer.parseInt(unixTimestimeToString(current.getTanggal_sekarang())[1]);

        String mDayString = bulan[monthNumber-1];
        tanggalsekarang.setText(mTanggal_datang + " " + mDayString + " " + mTahun_datang);

        return convertView;
    }
    private String[] unixTimestimeToString(long unixTimestime){
        Date date = new Date(unixTimestime*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH mm ss");
        String formattedDate = sdf.format(date);

        String[] time = formattedDate.split(" ");
        return time;
    }
}
