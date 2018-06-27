package com.example.isepmm.kandangku;

import android.content.Context;
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
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;


public class FragmentGrafik extends android.support.v4.app.Fragment implements OnChartGestureListener, OnChartValueSelectedListener {

    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();

    private String bulan[] = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
    private String dateToTitle = " ";
    private DatabaseReference mDatabaseReference;
    private DatabaseReference dataPrediksi;
    private DatabaseReference ayamMati;
    private DatabaseReference hargaAyam;
    private String mKey;
    private String idDevice;

    private long prediksiRansum1;
    private long prediksiRansum2;
    private long prediksiRansum3;
    private long prediksiRansum4;
    private long prediksiRansum5;
    private long prediksiPanen1;
    private long prediksiPanen2;
    private long prediksiPanen3;
    private long prediksiPanen4;
    private long prediksiPanen5;

    TextView tanggalperiode;
    TextView ransum1;
    TextView ransum2;
    TextView ransum3;
    TextView ransum4;
    TextView ransum5;
    TextView panen1;
    TextView panen2;
    TextView panen3;
    TextView panen4;
    TextView panen5;

    private LineChart mChart;
    private LineDataSet mSetSuhu;
    private ArrayList<Entry> mValuesSuhu;
    View view;

    public FragmentGrafik() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment_grafik, container, false);

        tanggalperiode = (TextView) view.findViewById(R.id.tglperiode);
        ransum1 = (TextView) view.findViewById(R.id.ransum1);
        ransum2 = (TextView) view.findViewById(R.id.ransum2);
        ransum3 = (TextView) view.findViewById(R.id.ransum3);
        ransum4 = (TextView) view.findViewById(R.id.ransum4);
        ransum5 = (TextView) view.findViewById(R.id.ransum5);
        panen1 = (TextView) view.findViewById(R.id.panen1);
        panen2 = (TextView) view.findViewById(R.id.panen2);
        panen3 = (TextView) view.findViewById(R.id.panen3);
        panen4 = (TextView) view.findViewById(R.id.panen4);
        panen5 = (TextView) view.findViewById(R.id.panen5);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        idDevice = getActivity().getIntent().getStringExtra(MainActivity.ARGS_DEVICE_ID);
        mValuesSuhu = new ArrayList<>();
        devineDevice();
        databaseReference();

        return view;
    }

    private void databaseReference(){
        mDatabaseReference.child(idDevice).child("LastKey").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mKey = dataSnapshot.getValue(String.class);
                Log.i(TAG, "onDataChange: "+mKey);
                getDataPrediksi(mKey);
                DataKandang();
                readSuhu();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private  void getDataPrediksi(String mKey){
        dataPrediksi = FirebaseDatabase.getInstance().getReference().child(idDevice).child("Periode").child(mKey);
        dataPrediksi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    /*long mAyamHidup = dataSnapshot.child("jumlah_ayam").getValue(Long.class);
                    long mAyamMati = dataSnapshot.child("total_ayam_mati").getValue(Long.class);
                    long mHargaAyam = dataSnapshot.child("harga_pasar").getValue(Long.class);

                    hitungPredikisi(mAyamHidup , mAyamMati, mHargaAyam);*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void hitungPredikisi(long ayamHidup, long ayamMati, long mHargaAyam){
        long jumlahAyam = ayamHidup - ayamMati;
        Log.i(TAG, "hitungPredikisi [JUMLAH AYAM] : " + jumlahAyam);

        prediksiRansum1 = jumlahAyam * 114;
        prediksiRansum2 = jumlahAyam * 289;
        prediksiRansum3 = jumlahAyam * 512;
        prediksiRansum4 = jumlahAyam * 715;
        prediksiRansum5 = jumlahAyam * 930;

        prediksiPanen1 = jumlahAyam * 173 * mHargaAyam;
        prediksiPanen2 = jumlahAyam * 429 * mHargaAyam;
        prediksiPanen3 = jumlahAyam * 823 * mHargaAyam;
        prediksiPanen4 = jumlahAyam * 1334 * mHargaAyam;
        prediksiPanen5 = jumlahAyam * 1919 * mHargaAyam;

        ransum1.setText(String.valueOf(prediksiRansum1));
        ransum2.setText(String.valueOf(prediksiRansum2));
        ransum3.setText(String.valueOf(prediksiRansum3));
        ransum4.setText(String.valueOf(prediksiRansum4));
        ransum5.setText(String.valueOf(prediksiRansum5));

        panen1.setText(String .valueOf(prediksiPanen1));
        panen2.setText(String .valueOf(prediksiPanen2));
        panen3.setText(String .valueOf(prediksiPanen3));
        panen4.setText(String .valueOf(prediksiPanen4));
        panen5.setText(String .valueOf(prediksiPanen5));
    }

    private void DataKandang() {
        final ArrayList<Long> tanggalKandang = new ArrayList<>();
        DatabaseReference curData = dataRef.child(idDevice).child("Periode");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Long kandang = data.child("tanggal_datang").getValue(Long.class);
                    if (kandang != null) {
                        tanggalKandang.add(kandang);
                    } else {
                        Log.d("Cek", "cek");
                    }
                }
                String mTahun_datang = unixTimestimeToString(tanggalKandang.get(tanggalKandang.size() - 1))[0];
                String mTanggal_datang = unixTimestimeToString(tanggalKandang.get(tanggalKandang.size() - 1))[2];
                int monthNumber = Integer.parseInt(unixTimestimeToString(tanggalKandang.get(tanggalKandang.size() - 1))[1]);

                String mDayString = bulan[monthNumber - 1];
                dateToTitle = mTanggal_datang + " " + mDayString + " " + mTahun_datang;
                Log.d("Hasil", "" + dateToTitle);
                tanggalperiode.setText(dateToTitle);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        curData.addListenerForSingleValueEvent(listener);
    }

    private String[] unixTimestimeToString(long unixTimestime) {
        Date date = new Date(unixTimestime * 1000L);
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

        mChart.animateX(2500);
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
    }

    private String simpleDateFormat() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");
        return df.format(c);
    }

    private void readSuhu() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference myRef = database.getReference();
        myRef.keepSynced(true);

        Log.i(TAG, "readSuhu: " + mKey);
        myRef.child(idDevice).child("Periode").child(mKey).child("Suhu").child(simpleDateFormat()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mValuesSuhu.clear();
                int i = 0;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    try {
                        String data = userSnapshot.getValue(String.class);
                        float value = Float.valueOf(data);
                        Log.i(TAG, "onDataChange: " + data);
                        final Entry entry = new Entry(i, value);
                        mValuesSuhu.add(entry);
                        Log.i(TAG, "DATASUHU: " + mValuesSuhu);
                        i++;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                setData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
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
