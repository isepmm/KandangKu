package com.example.isepmm.kandangku;

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

public class FragmentKontrolKandang extends android.support.v4.app.Fragment {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("SuhuSekarang");
    DatabaseReference datatertinggi = FirebaseDatabase.getInstance().getReference("SuhuTertinggi");
    DatabaseReference dataterendah = FirebaseDatabase.getInstance().getReference("SuhuTerendah");

    TextView suhusekarang;
    EditText suhutertinggi;
    EditText suhuterendah;
    Button ubah;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_kontrol_kandang, container, false);

        suhusekarang = (TextView) view.findViewById(R.id.suhusekarang);
        suhutertinggi = (EditText) view.findViewById(R.id.suhutertinggi);
        suhuterendah = (EditText) view.findViewById(R.id.suhuterendah);
        ubah = (Button) view.findViewById(R.id.ubah);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long suhu = dataSnapshot.getValue(long.class);
                Log.d("Value", "" + String.valueOf(suhu));
                suhusekarang.setText(String.valueOf(suhu));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        datatertinggi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long tinggi = dataSnapshot.getValue(long.class);
                Log.d("tinggi", "" + String.valueOf(tinggi));
                suhutertinggi.setText(String.valueOf(tinggi));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dataterendah.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long rendah = dataSnapshot.getValue(long.class);
                Log.d("rendah", "" + String.valueOf(rendah));
                suhuterendah.setText(String.valueOf(rendah));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeNewPost(suhutertinggi, suhuterendah);

            }
        });

        return view;
    }

    public void writeNewPost(EditText SuhuTertinggi, EditText SuhuTerendah) {
        if (!validationInputField()) {
            return;
        } else {
            Integer suhuTinggi = Integer.valueOf(SuhuTertinggi.getText().toString());
            Integer suhuRendah = Integer.valueOf(SuhuTerendah.getText().toString());
            datatertinggi.setValue(suhuTinggi);
            dataterendah.setValue(suhuRendah);

            Toast.makeText(getContext(), R.string.set, Toast.LENGTH_LONG).show();
        }
    }

    private boolean validationInputField() {
        boolean isValid = true;
        if (suhutertinggi.getText().toString().equals("")) {
            Toast.makeText(getContext(), R.string.empty_message, Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (suhuterendah.getText().toString().equals("")) {
            Toast.makeText(getContext(), R.string.empty_message, Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
}
