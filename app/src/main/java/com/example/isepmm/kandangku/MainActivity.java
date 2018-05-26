package com.example.isepmm.kandangku;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static final String ARGS_DEVICE_ID = "device id";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        FragmentPeriodeTernak fragmentDataTernak = new FragmentPeriodeTernak();
        FragmentRemote fragmentRemote = new FragmentRemote();
        FragmentGrafik fragmentGrafik = new FragmentGrafik();

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_periodeternak:
                    setTitle(R.string.periode_ternak);
                    changeFragment(new FragmentPeriodeTernak());
                    break;
                case R.id.navigation_remote:
                    setTitle(R.string.remote);
                    changeFragment(new FragmentRemote());
                    break;
                case R.id.navigation_grafik:
                    setTitle(R.string.grafik);
                    changeFragment(new FragmentGrafik());
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

        changeFragment(new FragmentPeriodeTernak());
        setTitle(R.string.periode_ternak);
    }

//    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
//        alertbox.setMessage(R.string.exit)
//                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        moveTaskToBack(true);
//                    }
//                })
//                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        arg0.dismiss();
//                    }
//                })
//                .show();
//    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
        startActivity(intent);
    }
}
