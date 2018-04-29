package com.example.isepmm.kandangku;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewKandang extends AppCompatActivity {

    private String bulan[] = {"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Ags","Sep","Okt","Nov","Des"};
    private String dateToTitle = " ";

    private TextView jumlah_total_doc;
    private TextView harga_doc;
    private TextView berat_panen;
    private TextView boarding;
    private TextView harga_pasar;
    private TextView konsumsi_minum;
    private TextView konsumsi_obat;
    private TextView konsumsi_pakan;
    private TextView konsumsi_vitamin;
    private TextView penggunaan_listrik;

    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_kandang);

        jumlah_total_doc = (TextView) findViewById(R.id.jumlah_total_doc);
        harga_doc = (TextView) findViewById(R.id.harga_doc);
        /*berat_panen = (TextView) findViewById(R.id.berat_panen);
        boarding = (TextView) findViewById(R.id.boarding);
        harga_pasar = (TextView) findViewById(R.id.harga_pasar);
        konsumsi_minum = (TextView) findViewById(R.id.konsumsi_minum);
        konsumsi_obat = (TextView) findViewById(R.id.konsumsi_obat);
        konsumsi_pakan = (TextView) findViewById(R.id.konsumsi_pakan);
        konsumsi_vitamin = (TextView) findViewById(R.id.konsumsi_vitamin);
        penggunaan_listrik = (TextView) findViewById(R.id.penggunaan_listrik);
*/
        Intent currentIntent = getIntent();
        String keyValue = currentIntent.getStringExtra("KeyValue");
        DataKandang(keyValue);
        setTitle("TANGGAL");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.tambah);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewKandang.this, Ayam.class);
                startActivity(intent);
            }
        });
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
                        Log.d("cek","ASU");
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
        inflater.inflate(R.menu.edit_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                Intent intent = new Intent(ViewKandang.this, History.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
