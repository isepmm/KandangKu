package com.example.isepmm.kandangku;

import android.content.DialogInterface;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        FragmentPencatatanSuhu fragmentPencatatanSuhu = new FragmentPencatatanSuhu();
        FragmentKontrolKandang fragmentKontrolKandang = new FragmentKontrolKandang();
        FragmentPerkiraanUntung fragmentPerkiraanUntung = new FragmentPerkiraanUntung();

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_pencatatan_suhu:
                    setTitle(R.string.pencatatn_suhu);
                    changeFragment(new FragmentPencatatanSuhu());
                    break;
                case R.id.navigation_kontrol_kandang:
                    setTitle(R.string.kontrol_kandang);
                    changeFragment(new FragmentKontrolKandang());
                    break;
                case R.id.navigation_perkiraan_untung:
                    setTitle(R.string.perkiraan_untung);
                    changeFragment(new FragmentPerkiraanUntung());
                    break;
            }
            return true;
        }
    };


    public void changeFragment(android.support.v4.app.Fragment fragment) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.content, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        changeFragment(new FragmentPencatatanSuhu());
        setTitle(R.string.pencatatn_suhu);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setMessage(R.string.exit)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        moveTaskToBack(true);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                })
                .show();
    }
}
