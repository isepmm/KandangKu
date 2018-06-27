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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class FragmentPeriodeTernak extends android.support.v4.app.Fragment {
    private String idDevice;
    private ProgressBar loadingData;
    private ListView listTanggal;
    private PeriodeAdapter mAdapter;
    private ArrayList<String> myKey = new ArrayList<>();
    private ArrayList<Kandang> dataKandang = new ArrayList<>();
    private Long tgl;

    TextView empty;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_periode_ternak, container, false);
        idDevice = getActivity().getIntent().getStringExtra(MainActivity.ARGS_DEVICE_ID);

        listTanggal = (ListView) view.findViewById(R.id.recyclerview);
        empty = (TextView) view.findViewById(R.id.empty_view);
        mAdapter = new PeriodeAdapter(getContext(), dataKandang);
        loadingData = (ProgressBar) view.findViewById(R.id.loading_data);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.tambah);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MulaiTernak.class);
                String id = idDevice;
                intent.putExtra(MainActivity.ARGS_DEVICE_ID, id);
                startActivity(intent);
            }
        });

        if (mAdapter != null) {
            listTanggal.setAdapter(mAdapter);
            ListOnClick(listTanggal);
            loadingData.setVisibility(View.GONE);
        } else {
            Log.d("Uji", "baca");
            loadingData.setVisibility(View.GONE);
        }
        getDataKandang();


        //listTanggal.setEmptyView();
        return view;
    }

    private void ListOnClick(ListView list) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent newIntent = new Intent(getContext(), ViewDetailTernak.class);
                newIntent.putExtra("KeyValue", myKey.get(i));
                Log.i(TAG, "IDLASTKEY : " + myKey.get(i));
                newIntent.putExtra(MainActivity.ARGS_DEVICE_ID, idDevice);
                startActivity(newIntent);
            }
        });
    }

    private void getDataKandang(){
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child(idDevice).child("Periode");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadingData.setVisibility(View.VISIBLE);
                empty.setVisibility(View.VISIBLE);
                mAdapter.clear();
                dataKandang.clear();
                myKey.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    myKey.add(data.getKey());
                    dataKandang.add(data.getValue(Kandang.class));
                    empty.setVisibility(View.GONE);
                }
                loadingData.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();

                if (myKey.size() > 0) {
                    String lastKey = myKey.get(myKey.size() - 1);
                    FirebaseDatabase.getInstance().getReference().child(idDevice).child("LastKey").setValue(lastKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
