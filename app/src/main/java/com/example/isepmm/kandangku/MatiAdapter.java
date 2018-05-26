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

import java.util.ArrayList;

/**
 * Created by Isepmm on 25/05/2018.
 */

public class MatiAdapter extends ArrayAdapter<Mati> {
    private TextView ayammati;

    public MatiAdapter(@NonNull Context context, @NonNull ArrayList<Mati> objects) {
        super(context, 0, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_view_history,parent,false);
        }
        ayammati = (TextView) convertView.findViewById(R.id.ayam_mati);

        Mati mati = getItem(position);
        long jumlah = mati.getJumlah_ayam_mati();

        ayammati.setText(Long.toString(jumlah));

        return convertView;
    }
}
