package com.example.isepmm.kandangku;

import android.content.Intent;
import android.os.Bundle;
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
    private String idDevice;
    //Chlid Firebase
    DatabaseReference suhu1;
    DatabaseReference suhu2;
    DatabaseReference suhu3;

    DatabaseReference rataSuhu;
    DatabaseReference dataKelembaban;
    DatabaseReference dataTertinggi;
    DatabaseReference dataTerendah;
    //DatabaseReference dataOnCooling;
    //DatabaseReference dataOffCooling;
    DatabaseReference dataAmoniaSekarang;

    TextView rataRataSuhu;
    TextView kelembabanSekarang;
    TextView suhuBlok1;
    TextView suhuBlok2;
    TextView suhuBlok3;
    EditText suhuTertinggi;
    EditText suhuTerendah;
    //EditText onCooling;
    //EditText offCooling;
    TextView amoniasekarang;
    Button ubah;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_remote, container, false);

        idDevice = getActivity().getIntent().getStringExtra(MainActivity.ARGS_DEVICE_ID);

        suhu1 = FirebaseDatabase.getInstance().getReference().child(idDevice).child("Suhu1");
        suhu2 = FirebaseDatabase.getInstance().getReference().child(idDevice).child("Suhu2");
        suhu3 = FirebaseDatabase.getInstance().getReference().child(idDevice).child("Suhu3");
        rataSuhu = FirebaseDatabase.getInstance().getReference().child(idDevice).child("RataSuhu");
        dataKelembaban = FirebaseDatabase.getInstance().getReference().child(idDevice).child("KelembabanSekarang");
        dataTertinggi = FirebaseDatabase.getInstance().getReference().child(idDevice).child("SuhuTertinggi");
        dataTerendah = FirebaseDatabase.getInstance().getReference().child(idDevice).child("SuhuTerendah");
//        dataOnCooling = FirebaseDatabase.getInstance().getReference().child("MainProgram").child("CoolingOn");
//        dataOffCooling = FirebaseDatabase.getInstance().getReference().child("MainProgram").child("CoolingOff");
        dataAmoniaSekarang = FirebaseDatabase.getInstance().getReference().child(idDevice).child("AmoniaSekarang");

        rataRataSuhu = (TextView) view.findViewById(R.id.rataSuhu);
        kelembabanSekarang = (TextView) view.findViewById(R.id.kelembaban);
        suhuBlok1 = (TextView) view.findViewById(R.id.suhu1);
        suhuBlok2 = (TextView) view.findViewById(R.id.suhu2);
        suhuBlok3 = (TextView) view.findViewById(R.id.suhu3);
        suhuTertinggi = (EditText) view.findViewById(R.id.suhutertinggi);
        suhuTerendah = (EditText) view.findViewById(R.id.suhuterendah);
        //onCooling = (EditText) view.findViewById(R.id.onCoolling);
        //offCooling = (EditText) view.findViewById(R.id.offCooling);
        amoniasekarang = (TextView) view.findViewById(R.id.amonia);
        ubah = (Button) view.findViewById(R.id.ubah);

        suhu1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Integer.class) != null) {
                    int suhu1 = dataSnapshot.getValue(Integer.class);
                    suhuBlok1.setText(String.valueOf(suhu1));
                } else {
                    suhuBlok1.setText("-");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        suhu2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Integer.class) != null) {
                    int suhu2 = dataSnapshot.getValue(Integer.class);
                    suhuBlok2.setText(String.valueOf(suhu2));
                } else {
                    suhuBlok2.setText("-");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        suhu3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Integer.class) != null) {
                    int suhu3 = dataSnapshot.getValue(Integer.class);
                    suhuBlok3.setText(String.valueOf(suhu3));
                } else {
                    suhuBlok3.setText("-");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //Read Data Suhu Rata Rata
        rataSuhu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Integer.class) != null) {
                    int rataRata = dataSnapshot.getValue(Integer.class);
                    FragmentRemote.this.rataRataSuhu.setText(String.valueOf(rataRata));
                } else {
                    FragmentRemote.this.rataRataSuhu.setText("-");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //Read Data Suhu Kelembaban
        dataKelembaban.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Integer.class) != null) {
                    int kelembaban = dataSnapshot.getValue(Integer.class);
                    kelembabanSekarang.setText(String.valueOf(kelembaban));
                } else {
                    kelembabanSekarang.setText("-");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Read Data Suhu Tertinggi
        dataTertinggi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Integer.class) != null) {
                    int tinggi = dataSnapshot.getValue(Integer.class);
                    suhuTertinggi.setText(String.valueOf(tinggi));
                } else {
                    ubah.setVisibility(View.GONE);
                    suhuTertinggi.setText("-");
                    suhuTertinggi.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Read Data Suhu Terendah
        dataTerendah.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Integer.class) != null) {
                    int rendah = dataSnapshot.getValue(Integer.class);
                    suhuTerendah.setText(String.valueOf(rendah));
                } else {
                    ubah.setVisibility(View.GONE);
                    suhuTerendah.setText("-");
                    suhuTerendah.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Read Data OnCooling
        /*dataOnCooling.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Integer.class) != null) {
                    int on = dataSnapshot.getValue(Integer.class);
                    onCooling.setText(String.valueOf(on));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        //Read Data OffCooling
        /*dataOffCooling.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Integer.class) != null) {
                    int off = dataSnapshot.getValue(Integer.class);
                    offCooling.setText(String.valueOf(off));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        //Read Data amonia
        dataAmoniaSekarang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(float.class) != null) {
                    float amonia = dataSnapshot.getValue(float.class);
                    amoniasekarang.setText(String.valueOf(amonia));
                } else {
                    amoniasekarang.setText("-");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //untuk mengubah data
        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeNewPost(suhuTertinggi, suhuTerendah);//, onCooling, offCooling);
            }
        });

        return view;
    }
    //Input data ke Firebase
    public void writeNewPost(EditText SuhuTertinggi, EditText SuhuTerendah){//, EditText OnCooling, EditText OffCooling ) {
        if (!validationInputField(Integer.valueOf(SuhuTertinggi.getText().
                toString()),Integer.valueOf(SuhuTerendah.getText().toString()))) {
            return;
        } else {
            int suhuTinggi  = Integer.valueOf(SuhuTertinggi.getText().toString());
            int suhuRendah = Integer.valueOf(SuhuTerendah.getText().toString());
            //int onCooling = Integer.valueOf(OnCooling.getText().toString());
            //int offCooling = Integer.valueOf(OffCooling.getText().toString());
            dataTertinggi.setValue(suhuTinggi);
            dataTerendah.setValue(suhuRendah);
            //dataOnCooling.setValue(onCooling);
            //dataOffCooling.setValue(offCooling);

            Toast.makeText(getContext(), R.string.set, Toast.LENGTH_LONG).show();
        }
    }
    //Toast
    private boolean validationInputField(int tinggi, int rendah) {
        boolean isValid = true;
        if (suhuTertinggi.getText().toString().equals("")) {
            Toast.makeText(getContext(), R.string.empty_message, Toast.LENGTH_LONG).show();
            isValid = false;
        }/*else if (onCooling.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Data On Cooling kosong", Toast.LENGTH_LONG).show();
            isValid = false;
        }*/else if (suhuTerendah.getText().toString().equals("")) {
            Toast.makeText(getContext(), R.string.empty_message, Toast.LENGTH_LONG).show();
            isValid = false;
        }/*else if (offCooling.getText().toString().equals("")) {
            Toast.makeText(getContext(),"Data Off Cooling kosong", Toast.LENGTH_LONG).show();
            isValid = false;
        }*/else  if(tinggi < rendah ){
            Toast.makeText(getContext(), R.string.kurang, Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
}
