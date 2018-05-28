package com.example.isepmm.kandangku;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class FragmentPeriodeTernak extends android.support.v4.app.Fragment{
    private String idDvice;
    private ProgressBar loadingData;
    private ListView listTanggal;
    private PeriodeAdapter mAdapter;
    private ArrayList<String> myKey = new ArrayList<>();
    private Long tgl;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_periode_ternak, container, false);

        idDvice = getActivity().getIntent().getStringExtra(MainActivity.ARGS_DEVICE_ID);
        Log.i(TAG, "idDeviceFIX: " + idDvice);

        listTanggal = (ListView) view.findViewById(R.id.recyclerview);
        mAdapter = new PeriodeAdapter(getContext(),getDataKandang());
        loadingData = (ProgressBar) view.findViewById(R.id.loading_data);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.tambah);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MulaiTernak.class);
                startActivity(intent);
            }
        });
        if(mAdapter != null){
            listTanggal.setAdapter(mAdapter);
            ListOnClick(listTanggal);
        }else{
            Log.d("Uji","baca");
        }
        loadingData.setVisibility(View.VISIBLE);

        //listTanggal.setEmptyView();
        return view;
    }

    private void ListOnClick(ListView list){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent newIntent = new Intent(getContext(),ViewDetailTernak.class);
                newIntent.putExtra("KeyValue",myKey.get(i));
                Log.i(TAG, "ID?LASTKEY : "+myKey.get(i));
                newIntent.putExtra("tgl",tgl.toString());
                startActivity(newIntent);
            }
        });
    }

    private ArrayList<Kandang> getDataKandang(){
        final ArrayList<Kandang> curKandang = new ArrayList<>();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child(idDvice).child("Periode");
        dataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Kandang kandang = dataSnapshot.getValue(Kandang.class);
                tgl = kandang.getTanggal_datang();
                Log.i("Data ternak", " : " + tgl);
                myKey.add(dataSnapshot.getKey());
                if(kandang != null){
                    curKandang.add(kandang);
                    mAdapter.notifyDataSetChanged();
                    loadingData.setVisibility(View.GONE);
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
