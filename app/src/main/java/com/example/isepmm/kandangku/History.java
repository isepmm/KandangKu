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

import java.util.ArrayList;

public class History extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private String mKey;
    private ListView listHistory;
    private MatiAdapter mAdapter;


    TextView ayammati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        listHistory = (ListView) findViewById(R.id.viewhistory);
        ayammati = (TextView) findViewById(R.id.ayam_mati);
        ReadLaskey();
        mAdapter = new MatiAdapter(this, getDataHistory());
        listHistory.setAdapter(mAdapter);

    }
    //    readlastkey
    private void ReadLaskey() {
        mDatabaseReference.child("MainProgram").child("LastKey").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mKey = dataSnapshot.getValue(String.class);
                Log.i("kunci", "onDataChange: " + mKey);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<Mati> getDataHistory() {
        final ArrayList<Mati> curMati = new ArrayList<>();
        mDatabaseReference.child("MainProgram").child("Periode").child(mKey).child("Ayam_Mati")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        curMati.clear();
                            for(DataSnapshot data : dataSnapshot.getChildren()){
                                Mati mati = data.getValue(Mati.class);
                                Mati AyamMati = new Mati(mati,data.getKey());
                                curMati.add(AyamMati);
                                Log.i("ISP", "onDataChange: "+mati);
                            }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return curMati;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
