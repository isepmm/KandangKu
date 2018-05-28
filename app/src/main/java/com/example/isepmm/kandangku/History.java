package com.example.isepmm.kandangku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class History extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private String mKey;
    private ListView listHistory;
    private MatiAdapter mAdapter;
    ArrayList<Mati> list;


    TextView tanggalsekarang;
    TextView ayammati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mKey = getIntent().getStringExtra("KeyValue");//readkey
        listHistory = (ListView) findViewById(R.id.viewhistory);
        tanggalsekarang = (TextView) findViewById(R.id.tanggal_datang);
        ayammati = (TextView) findViewById(R.id.ayam_mati);
        getDataHistory();

        //ArrayList
        list = new ArrayList<>();
        mAdapter = new MatiAdapter(this, list);
        listHistory.setAdapter(mAdapter);
    }
    //Read History
    private void getDataHistory() {
        mDatabaseReference.child("MainProgram").child("Periode").child(mKey).child("Ayam_Mati")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Mati mati = data.getValue(Mati.class);
                            Mati AyamMati = new Mati(mati, data.getKey());
                            list.add(AyamMati);
                            Log.i("AyamMati", "onDataChange: " + mati);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}