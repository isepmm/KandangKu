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
import java.util.List;

/**
 * Created by Isepmm on 06/04/2018.
 */

public class KandangAdapter extends ArrayAdapter<Kandang> {

    private TextView tanggal_datang;
    private String bulan[] = {"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Ags","Sep","Okt","Nov","Des"};
    public KandangAdapter(Context context, List objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.kandangku, parent, false);
        }

        Kandang current = getItem(position);

        tanggal_datang = (TextView) convertView.findViewById(R.id.tanggal_datang);

        assert current != null;
        String mTahun_datang = unixTimestimeToString(current.getTanggal_datang())[0];
        String mTanggal_datang = unixTimestimeToString(current.getTanggal_datang())[2];
        int monthNumber = Integer.parseInt(unixTimestimeToString(current.getTanggal_datang())[1]);

        String mDayString = bulan[monthNumber-1];
        tanggal_datang.setText(mTanggal_datang + ", " + mDayString + " " + mTahun_datang);

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
