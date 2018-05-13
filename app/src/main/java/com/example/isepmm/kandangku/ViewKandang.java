package com.example.isepmm.kandangku;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.isepmm.kandangku.R.id.tanggal_datang;

public class ViewKandang extends AppCompatActivity {

    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference periodeTernak;

    private KandangAdapter mAdapter;

    private String bulan[] = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Aguatua", "September", "Oktober", "November", "Desember"};
    private String dateToTitle = " ";

    TextView tanggal_datang;
    TextView jumlah_total_doc;
    TextView harga_doc;
    TextView berat_panen;
    TextView boarding;
    TextView harga_pasar;
    TextView konsumsi_minum;
    TextView konsumsi_obat;
    TextView konsumsi_pakan;
    TextView konsumsi_vitamin;
    TextView penggunaan_listrik;
    String lastKey;
    Long mtanggalDatang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_kandang);

        jumlah_total_doc = (TextView) findViewById(R.id.jumlah_total_doc);
        harga_doc = (TextView) findViewById(R.id.harga_doc);
        berat_panen = (TextView) findViewById(R.id.berat_panen);
        boarding = (TextView) findViewById(R.id.boarding);
        harga_pasar = (TextView) findViewById(R.id.harga_pasar);
        konsumsi_minum = (TextView) findViewById(R.id.konsumsi_minum);
        konsumsi_pakan = (TextView) findViewById(R.id.konsumsi_pakan);
        konsumsi_vitamin = (TextView) findViewById(R.id.konsumsi_vitamin);
        konsumsi_obat = (TextView) findViewById(R.id.konsumsi_obat);
        penggunaan_listrik = (TextView) findViewById(R.id.penggunaan_listrik);

        //read firebase
        lastKey = getIntent().getStringExtra("KeyValue");
        periodeTernak = FirebaseDatabase.getInstance().getReference()
                .child("MainProgram").child("Periode").child(lastKey);

        periodeTernak.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Kandang.class) != null) {
                    Kandang kandang = dataSnapshot.getValue(Kandang.class);
                    // Log.d("jumlahtotaldoc", "" + kandang.getJumlah_ayam());
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
        String tgl = currentIntent.getStringExtra("tgl");
        Long title = Long.parseLong(tgl);
        Log.i("tgl ternak", " : " + tgl);
        DataKandang(keyValue);
        setTitle(tgl);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.tambah);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewKandang.this, Ayam.class);
                startActivity(intent);
            }
        });
    }

    private void DataKandang(String key) {
        DatabaseReference curData = dataRef.child("MainProgram").child("Periode").child(key);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Long kandang = data.child("tanggal_datang").getValue(Long.class);
                    if (kandang != null) {
                        String mTahun_datang = unixTimestimeToString(kandang)[0];
                        String mTanggal_datang = unixTimestimeToString(kandang)[2];
                        int monthNumber = Integer.parseInt(unixTimestimeToString(kandang)[1]);

                        String mDayString = bulan[monthNumber - 1];
                        dateToTitle = mTanggal_datang + ", " + mDayString + " " + mTahun_datang;
                        Log.d("cek", kandang + " ");
                        Log.d("cek2", dateToTitle);
                    } else {
                        Log.d("cek", "");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        curData.addListenerForSingleValueEvent(listener);
    }

    private String[] unixTimestimeToString(long unixTimestime) {
        Date date = new Date(unixTimestime * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH mm ss");
        String formattedDate = sdf.format(date);
        String[] time = formattedDate.split(" ");
        return time;
    }


//    private String unixTimestime(long unixTimestime) {
//        Date date = new java.util.Date(unixTimestime * 1000L);
//// the format of your date
//        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
//// give a timezone reference for formatting (see comment at the bottom)
//        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
//        String formattedDate = sdf.format(date);
//        String[] time = formattedDate.split(" ");
//        return formattedDate;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tools_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                Intent history = new Intent(ViewKandang.this, History.class);
                startActivity(history);
                return true;
            case R.id.edit:
                Intent edit = new Intent(ViewKandang.this, EditKandang.class);
                edit.putExtra("KeyValue", lastKey);
                startActivity(edit);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent view = new Intent(ViewKandang.this, MainActivity.class);
        startActivity(view);
    }
}
