package com.example.isepmm.kandangku;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("MainProgram").child("Periode");
    private ListView listHistory;
    private KandangAdapter mAdapter;
    private ArrayList<String> myKey = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listHistory = (ListView) findViewById(R.id.viewhistory);


    }
    private ArrayList<Kandang> getDataHistory() {
        final ArrayList<Kandang> curKandang = new ArrayList<>();

        dataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Kandang kandang = dataSnapshot.getValue(Kandang.class);
                myKey.add(dataSnapshot.getKey());
                if (kandang != null) {
                    curKandang.add(kandang);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return curKandang;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

}
