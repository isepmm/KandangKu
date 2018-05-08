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
import java.util.HashMap;
import java.util.Map;

public class EditKandang extends AppCompatActivity {

    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference periodeTernak;

    private String bulan[] = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Aguatua","September","Oktober","November","Desember"};
    private String dateToTitle = " ";

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
        periodeTernak = FirebaseDatabase.getInstance().getReference()
                .child("MainProgram").child("Periode").child(lastKey);

        periodeTernak.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Kandang.class) != null) {
                    Kandang kandang = dataSnapshot.getValue(Kandang.class);
                    //Log.d("Read", "" + kandang.getJumlah_ayam());
                    mtanggalDatang = kandang.getTanggal_datang();

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
        Intent currentIntent = getIntent();
        String keyValue = currentIntent.getStringExtra("KeyValue");
        DataKandang(keyValue);
        setTitle("04, Mei 2018");
    }

    private void DataKandang(String key){
        DatabaseReference curData = dataRef.child("MainProgram").child("Periode").child(key);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Long kandang = data.child("tanggal_datang").getValue(Long.class);
                    if(kandang != null){
                        String mTahun_datang = unixTimestimeToString(kandang)[0];
                        String mTanggal_datang = unixTimestimeToString(kandang)[2];
                        int monthNumber = Integer.parseInt(unixTimestimeToString(kandang)[1]);

                        String mDayString = bulan[monthNumber-1];
                        dateToTitle = mTanggal_datang + ", " + mDayString + " " + mTahun_datang;
                        Log.d("cek",kandang + "");
                        Log.d("cek2", dateToTitle);
                    }else{
                        Log.d("cek","cek");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        curData.addListenerForSingleValueEvent(listener);
    }

    private String[] unixTimestimeToString(long unixTimestime){
        Date date = new Date(unixTimestime*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH mm ss");
        String formattedDate = sdf.format(date);
        String[] time = formattedDate.split(" ");
        return time;
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
                        Long.parseLong(konsumsi_pakan.getText().toString()),
                        Long.parseLong(konsumsi_vitamin.getText().toString()),
                        Long.parseLong(konsumsi_obat.getText().toString()),
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
                        Intent save = new Intent(EditKandang.this, ViewKandang.class);
                        save.putExtra("KeyValue", lastKey);
                        startActivity(save);
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
