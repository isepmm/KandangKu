package com.example.isepmm.kandangku;

import android.content.Intent;
import android.graphics.DashPathEffect;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragmentPencatatanSuhu extends android.support.v4.app.Fragment{
//    private LineChart mChart;
//    private LineDataSet mSetSuhu;
//    private ArrayList<Entry> mValuesSuhu;
//    View view;
//
//    public FragmentPencatatanSuhu (){
//
//    }

    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("MainProgram").child("Periode");

    private ListView listTanggal;
    private KandangAdapter mAdapter;
    private ArrayList<String> myKey = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_pencatatan_suhu, container, false);

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
            Log.d("Uji","Goblok");
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
