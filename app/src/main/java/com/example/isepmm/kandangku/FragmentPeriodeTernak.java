package com.example.isepmm.kandangku;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentPeriodeTernak extends android.support.v4.app.Fragment{
    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("MainProgram").child("Periode");

    private ListView listTanggal;
    private KandangAdapter mAdapter;
    private ArrayList<String> myKey = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_periode_ternak, container, false);

        listTanggal = (ListView) view.findViewById(R.id.recyclerview);
        mAdapter = new KandangAdapter(getContext(),getDataKandang());

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.tambah);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MulaiKandang.class);
                startActivity(intent);
            }
        });
        if(mAdapter != null){
            listTanggal.setAdapter(mAdapter);
            ListOnClick(listTanggal);
        }else{
            Log.d("Uji","baca");
        }

        //listTanggal.setEmptyView();
        return view;
    }

    private void ListOnClick(ListView list){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("KeyCuk", myKey.get(i));
                Intent newIntent = new Intent(getContext(),ViewKandang.class);
                newIntent.putExtra("KeyValue",myKey.get(i));
                startActivity(newIntent);
            }
        });
    }

    private ArrayList<Kandang> getDataKandang(){
        final ArrayList<Kandang> curKandang = new ArrayList<>();

        dataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Kandang kandang = dataSnapshot.getValue(Kandang.class);
                myKey.add(dataSnapshot.getKey());
                if(kandang != null){
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
}
