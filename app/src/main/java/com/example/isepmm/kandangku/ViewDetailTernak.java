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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewDetailTernak extends AppCompatActivity {

    private DatabaseReference periodeTernak;
    private DatabaseReference mDatabaseReference;
    private String lastKey;
    private String idDevice;

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
    TextView total_jumlah_ayam_mati;

    //Long mtanggalDatang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail_ternak);

        tanggal_datang = (TextView) findViewById(R.id.tanggal_datang);
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
        total_jumlah_ayam_mati = (TextView) findViewById(R.id.total_jumlah_ayam_mati);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        idDevice = this.getIntent().getStringExtra(MainActivity.ARGS_DEVICE_ID);
        Log.i("Coba", "onCreate: " + idDevice);
        //Read Key
//        ReadLaskey();
        lastKey = getIntent().getStringExtra("KeyValue");
        //Chlid Firebase
        periodeTernak = FirebaseDatabase.getInstance().getReference()
                .child(idDevice).child("Periode").child(lastKey);
        //Raed data Dari Firebase
        periodeTernak.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Kandang.class) != null) {
                    Kandang kandang = dataSnapshot.getValue(Kandang.class);
                     Log.d("isepp", "" + kandang.getTotal_ayam_mati());

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
                    total_jumlah_ayam_mati.setText(String.valueOf(kandang.getTotal_ayam_mati()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.tambah_ayam_mati);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewDetailTernak.this, FormAyamMati.class);
                intent.putExtra("KeyValue", lastKey);
                intent.putExtra("idDevice", idDevice);
                startActivity(intent);
            }
        });
        setTitle("Detail Ternak");
    }

    private String unixTimestimeToString(long unixTimestime) {
        Date date = new Date(unixTimestime * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
    private void hapus() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog)
                .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        periodeTernak.removeValue();
                        Toast.makeText(ViewDetailTernak.this, R.string.click_delete, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton(R.string.action_no_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }

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
                Intent history = new Intent(ViewDetailTernak.this, History.class);
                history.putExtra("KeyValue", lastKey);
                history.putExtra("idDevice", idDevice);
                startActivity(history);
                return true;
            case R.id.edit:
                Intent edit = new Intent(ViewDetailTernak.this, EditKandang.class);
                edit.putExtra("KeyValue", lastKey);
                String id = idDevice;
                edit.putExtra(MainActivity.ARGS_DEVICE_ID, id);
                startActivity(edit);
                return true;
            case R.id.action_hapus:
                hapus();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
