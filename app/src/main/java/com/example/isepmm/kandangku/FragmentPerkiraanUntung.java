package com.example.isepmm.kandangku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentPerkiraanUntung extends android.support.v4.app.Fragment {
    DatabaseReference dataayam = FirebaseDatabase.getInstance().getReference("JumlahAyam");
    TextView jumlahayam;
    Button simpan;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_fragment_perkiraan_untung, container, false);

            jumlahayam = (TextView) view.findViewById(R.id.jumlahayam);
            simpan = (Button) view.findViewById(R.id.btn_jumlah);

            dataayam.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long ayam = dataSnapshot.getValue(long.class);
                    Log.d("dataayam", "" + String.valueOf(ayam));
                    jumlahayam.setText(String.valueOf(ayam));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calculate();
                    writeNewPost(jumlahayam);
                }
            });
        }
        return view;
    }

    public void writeNewPost(TextView jumlahayam) {
        if (!validationInputField()) {
            return;
        } else {
            Integer jumlah = Integer.valueOf(jumlahayam.getText().toString());
            dataayam.setValue(jumlah);


            Toast.makeText(getContext(), R.string.set, Toast.LENGTH_LONG).show();
        }
    }
    private boolean validationInputField() {
        boolean isValid = true;
        if (jumlahayam.getText().toString().equals("")) {
            Toast.makeText(getContext(), R.string.empty_message, Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    void calculate() {
        String awal = ((EditText) view.findViewById(R.id.jumlah_awal)).getText().toString();
        String akhir = ((EditText) view.findViewById(R.id.ayam_mati)).getText().toString();
        try {
            int intAwal = Integer.parseInt(awal);
            int intAkhir = Integer.parseInt(akhir);
            int jumlah = intAwal - intAkhir;
            if (jumlah < 0) {
                Toast.makeText(getActivity(), "Data Tidak Boleh Negatif", Toast.LENGTH_LONG).show();
                ((TextView) view.findViewById(R.id.jumlahayam)).setText("0");
            } else {
                updateView(jumlah);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Data Harus Diisi", Toast.LENGTH_LONG).show();
        }
    }
    void updateView(int jumlah) {
        ((TextView) view.findViewById(R.id.jumlahayam)).setText(String.valueOf(jumlah));
        Toast.makeText(getActivity(), "Data Berhasil dirubah", Toast.LENGTH_LONG).show();
    }
}
