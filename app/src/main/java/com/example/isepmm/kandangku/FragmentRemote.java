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

public class FragmentRemote extends android.support.v4.app.Fragment {
    DatabaseReference datasekarang = FirebaseDatabase.getInstance().getReference().child("MainProgram").child("SuhuSekarang");
    DatabaseReference datatertinggi = FirebaseDatabase.getInstance().getReference().child("MainProgram").child("SuhuTertinggi");
    DatabaseReference dataterendah = FirebaseDatabase.getInstance().getReference().child("MainProgram").child("SuhuTerendah");

    TextView suhusekarang;
    EditText suhutertinggi;
    EditText suhuterendah;
    Button ubah;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_remote, container, false);

        suhusekarang = (TextView) view.findViewById(R.id.suhusekarang);
        suhutertinggi = (EditText) view.findViewById(R.id.suhutertinggi);
        suhuterendah = (EditText) view.findViewById(R.id.suhuterendah);
        ubah = (Button) view.findViewById(R.id.ubah);

        datasekarang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(float.class) != null) {
                    float suhuSekarang = dataSnapshot.getValue(float.class);
                    Log.d("suhusekarang", "" + String.valueOf(suhuSekarang));
                    suhusekarang.setText(String.valueOf(suhuSekarang));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        datatertinggi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(float.class) != null) {
                    float tinggi = dataSnapshot.getValue(float.class);
                    suhutertinggi.setText(String.valueOf(tinggi));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dataterendah.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(float.class) != null) {
                    float rendah = dataSnapshot.getValue(float.class);
                    suhuterendah.setText(String.valueOf(rendah));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeNewPost(suhutertinggi , suhuterendah);
            }
        });

        return view;
    }

    public void writeNewPost(EditText SuhuTertinggi, EditText SuhuTerendah) {
        if (!validationInputField(Float.valueOf(SuhuTertinggi.getText().
                toString()),Float.valueOf(SuhuTerendah.getText().toString()))) {
            return;
        } else {
            float suhuTinggi  = Float.valueOf(SuhuTertinggi.getText().toString());
            float suhuRendah = Float.valueOf(SuhuTerendah.getText().toString());
            datatertinggi.setValue(suhuTinggi);
            dataterendah.setValue(suhuRendah);

            Toast.makeText(getContext(), R.string.set, Toast.LENGTH_LONG).show();
        }
    }

    private boolean validationInputField(float tinggi, float rendah) {
        boolean isValid = true;
        if (suhutertinggi.getText().toString().equals("")) {
            Toast.makeText(getContext(), R.string.empty_message, Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (suhuterendah.getText().toString().equals("")) {
            Toast.makeText(getContext(), R.string.empty_message, Toast.LENGTH_LONG).show();
            isValid = false;
        }else  if(tinggi < rendah ){
            Toast.makeText(getContext(), R.string.kurang, Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
}
