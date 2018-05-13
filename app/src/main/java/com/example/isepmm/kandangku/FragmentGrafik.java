package com.example.isepmm.kandangku;

import android.os.Bundle;
import android.graphics.DashPathEffect;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class FragmentGrafik extends android.support.v4.app.Fragment implements OnChartGestureListener, OnChartValueSelectedListener {
    DatabaseReference datasekarang = FirebaseDatabase.getInstance().getReference().child("MainProgram").child("SuhuSekarang");
    DatabaseReference datatertinggi = FirebaseDatabase.getInstance().getReference().child("MainProgram").child("SuhuTertinggi");
    DatabaseReference dataterendah = FirebaseDatabase.getInstance().getReference().child("MainProgram").child("SuhuTerendah");

    DatabaseReference suhu = FirebaseDatabase.getInstance().getReference();

    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();

    private String bulan[] = {"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Ags","Sep","Okt","Nov","Des"};
    private String dateToTitle = " ";

    TextView suhusekarang;
    TextView suhutertinggi;
    TextView suhuterendah;
    TextView tanggalperiode;

    private LineChart mChart;
    private LineDataSet mSetSuhu;
    private ArrayList<Entry> mValuesSuhu;
    View view;

    public FragmentGrafik (){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment_grafik, container, false);

        suhusekarang = (TextView) view.findViewById(R.id.suhuSekarang);
        suhutertinggi = (TextView) view.findViewById(R.id.suhutertinggi);
        suhuterendah = (TextView) view.findViewById(R.id.suhuterendah);
        tanggalperiode = (TextView) view.findViewById(R.id.tglperiode);

        DataKandang();

        datasekarang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(float.class) != null) {
                    float suhuSekarang = dataSnapshot.getValue(float.class);
                    Log.d("suhuSekarang", "" + String.valueOf(suhuSekarang));
                    suhusekarang.setText(String.valueOf(suhuSekarang));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        datatertinggi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(float.class) != null) {
                    float tinggi = dataSnapshot.getValue(float.class);
                    suhutertinggi.setText(String.valueOf(tinggi));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dataterendah.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(float.class) != null) {
                    float rendah = dataSnapshot.getValue(float.class);
                    suhuterendah.setText(String.valueOf(rendah));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mValuesSuhu = new ArrayList<>();

        devineDevice();
        return view;
    }

    private void DataKandang(){
        final ArrayList<Long> tanggalKandang = new ArrayList<>();
        DatabaseReference curData = dataRef.child("MainProgram").child("Periode");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Long kandang = data.child("tanggal_datang").getValue(Long.class);
                    if(kandang != null){
                        tanggalKandang.add(kandang);
                    }else{
                        Log.d("Cek","cek");
                    }
                }
                String mTahun_datang = unixTimestimeToString(tanggalKandang.get(tanggalKandang.size() - 1))[0];
                String mTanggal_datang = unixTimestimeToString(tanggalKandang.get(tanggalKandang.size() - 1))[2];
                int monthNumber = Integer.parseInt(unixTimestimeToString(tanggalKandang.get(tanggalKandang.size() - 1))[1]);

                String mDayString = bulan[monthNumber-1];
                dateToTitle = mTanggal_datang + ", " + mDayString + " " + mTahun_datang;
                Log.d("Hasil",""+ dateToTitle);
                tanggalperiode.setText(dateToTitle);
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

    private void devineDevice() {
        mChart = view.findViewById(R.id.line_chart);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        mChart.getDescription().setEnabled(false);

        mChart.setTouchEnabled(true);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);

        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setAxisMaximum(24);
        xAxis.setAxisMinimum(0);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaximum(40f);
        leftAxis.setAxisMinimum(0f);

        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);
        readSuhu();

        mChart.animateX(2500);
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
    }

    private void readSuhu() {
        for (int i = 0; i < 24; i++) {
            mValuesSuhu.add(new Entry(i, (float) (Math.random() * 30 + 1))); // change to real data
        }

        setData();
    }

    private void setData() {
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            mSetSuhu = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            mSetSuhu.setValues(mValuesSuhu);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            mSetSuhu = new LineDataSet(mValuesSuhu, "Suhu");

            mSetSuhu.setDrawIcons(false);

            int color1 = ResourcesCompat.getColor(getResources(), R.color.colorChart, null);
            mSetSuhu.enableDashedHighlightLine(10f, 5f, 0f);
            mSetSuhu.setColor(color1);
            mSetSuhu.setCircleColor(color1);
            mSetSuhu.setLineWidth(1f);
            mSetSuhu.setCircleRadius(3f);
            mSetSuhu.setDrawCircleHole(false);
            mSetSuhu.setValueTextSize(9f);
            mSetSuhu.setDrawValues(false);
//          mSetSuhu.setDrawFilled(true);
            mSetSuhu.setFormLineWidth(1f);
            mSetSuhu.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            mSetSuhu.setFormSize(15.f);
            mSetSuhu.setFillColor(color1);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(mSetSuhu);

            LineData data = new LineData(dataSets);
            mChart.setData(data);
        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleX() + ", high: " + mChart.getHighestVisibleX());
        Log.i("MIN MAX", "xmin: " + mChart.getXChartMin() + ", xmax: " + mChart.getXChartMax() + ", ymin: " + mChart.getYChartMin() + ", ymax: " + mChart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

}
