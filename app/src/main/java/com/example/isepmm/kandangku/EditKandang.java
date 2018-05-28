package com.example.isepmm.kandangku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class EditKandang extends AppCompatActivity {

    DatabaseReference periodeTernak;

    TextView tanggal_datang;
    EditText jumlah_total_doc;
    EditText harga_doc;
    EditText berat_panen;
    EditText boarding;
    EditText harga_pasar;
    EditText konsumsi_minum;
    EditText konsumsi_obat;
    EditText konsumsi_pakan;
    EditText konsumsi_vitamin;
    EditText penggunaan_listrik;
    String lastKey;
    Long mtanggalDatang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kandang);

        tanggal_datang = (TextView) findViewById(R.id.tanggal_datang);
        jumlah_total_doc = (EditText) findViewById(R.id.jumlah_total_doc);
        harga_doc = (EditText) findViewById(R.id.harga_doc);
        berat_panen = (EditText) findViewById(R.id.berat_panen);
        boarding = (EditText) findViewById(R.id.boarding);
        harga_pasar = (EditText) findViewById(R.id.harga_pasar);
        konsumsi_minum = (EditText) findViewById(R.id.konsumsi_minum);
        konsumsi_pakan = (EditText) findViewById(R.id.konsumsi_pakan);
        konsumsi_vitamin = (EditText) findViewById(R.id.konsumsi_vitamin);
        konsumsi_obat = (EditText) findViewById(R.id.konsumsi_obat);
        penggunaan_listrik = (EditText) findViewById(R.id.penggunaan_listrik);

        //read firebase
        lastKey = getIntent().getStringExtra("KeyValue");
        //Chlid Firebase
        periodeTernak = FirebaseDatabase.getInstance().getReference()
                .child("MainProgram").child("Periode").child(lastKey);
        //Raed data Dari Firebase
        periodeTernak.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Kandang.class) != null) {
                    Kandang kandang = dataSnapshot.getValue(Kandang.class);
                    Log.d("Read", "" + kandang.getJumlah_ayam());
                    mtanggalDatang = kandang.getTanggal_datang();
                    long mtgl = kandang.getTanggal_datang();
                    String textTgl = unixTimestimeToString(mtgl);
                    tanggal_datang.setText(textTgl);
                    jumlah_total_doc.setText(String.valueOf(kandang.getJumlah_ayam()));
                    harga_doc.setText(String.valueOf(kandang.getHarga_doc()));
                    berat_panen.setText(String.valueOf(kandang.getBerat_panen()));
                    boarding.setText(String.valueOf(kandang.getBoarding()));
                    harga_pasar.setText(String.valueOf(kandang.getHarga_pasar()));
                    konsumsi_minum.setText(String.valueOf(kandang.getKonsumsi_minum()));
                    konsumsi_pakan.setText(String.valueOf(kandang.getKonsumsi_pakan()));
                    konsumsi_vitamin.setText(String.valueOf(kandang.getKonsumsi_vitamin()));
                    konsumsi_obat.setText(String.valueOf(kandang.getKonsumsi_obat()));
                    penggunaan_listrik.setText(String.valueOf(kandang.getPenggunaan_listrik()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        setTitle("Edit");
    }

    private String unixTimestimeToString(long unixTimestime) {
        Date date = new Date(unixTimestime * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_detail, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // edit data
            case R.id.save:
                Kandang kandang = new Kandang(mtanggalDatang,
                        Long.parseLong(jumlah_total_doc.getText().toString()),
                        Long.parseLong(harga_doc.getText().toString()),
                        Long.parseLong(berat_panen.getText().toString()),
                        Long.parseLong(boarding.getText().toString()),
                        Long.parseLong(harga_pasar.getText().toString()),
                        Long.parseLong(konsumsi_minum.getText().toString()),
                        Long.parseLong(konsumsi_obat.getText().toString()),
                        Long.parseLong(konsumsi_pakan.getText().toString()),
                        Long.parseLong(konsumsi_vitamin.getText().toString()),
                        Long.parseLong(penggunaan_listrik.getText().toString())
                        );
                Map<String, Object> tes = kandang.toMap();
                periodeTernak.updateChildren(tes, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    //Problem with saving the data
                    if (databaseError != null) {
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        //Data uploaded successfully on the server
                        Toast.makeText(EditKandang.this, "Data Berhasil Diubah", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
