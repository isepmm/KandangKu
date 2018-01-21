package com.example.isepmm.kandangku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentPerkiraanUntung extends android.support.v4.app.Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_fragment_perkiraan_untung, container, false);
            view.findViewById(R.id.btn_jumlah).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calculate();
                }
            });
        }
        return view;
    }

    void calculate() {
        String awal = ((EditText) view.findViewById(R.id.jumlah_awal)).getText().toString();
        String akhir = ((EditText) view.findViewById(R.id.ayam_mati)).getText().toString();
        try {
            int intAwal = Integer.parseInt(awal);
            int intAkhir = Integer.parseInt(akhir);
            int jumlah = intAwal - intAkhir;
            if (jumlah < 0) {
                Toast.makeText(getActivity(), "Data Tidak Boleh Negatif ", Toast.LENGTH_LONG).show();
                ((TextView) view.findViewById(R.id.jumlah_ayam_sekarang)).setText("0");
            } else {
                updateView(jumlah);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Data Harus Diisi", Toast.LENGTH_LONG).show();
        }
    }

    void updateView(int jumlah) {
        ((TextView) view.findViewById(R.id.jumlah_ayam_sekarang)).setText(String.valueOf(jumlah));
        Toast.makeText(getActivity(), "Data Berhasil dirubah", Toast.LENGTH_LONG).show();
    }
}
