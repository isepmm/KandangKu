package com.example.isepmm.kandangku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentPencatatanSuhu extends android.support.v4.app.Fragment {

    public FragmentPencatatanSuhu (){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_pencatatan_suhu, container, false);
        return view;
    }
}
