package com.example.isepmm.kandangku;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.isepmm.kandangku.R.id.id_device;
import static com.example.isepmm.kandangku.R.id.tanggal_datang;

/**
 * Created by Isepmm on 17/05/2018.
 */

public class DeviceAdapter extends ArrayAdapter<Device> {
    private TextView device_id;
    private TextView device_name;

    public DeviceAdapter(@NonNull Context context, @NonNull ArrayList<Device> objects) {
        super(context, 0, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_device,parent,false);
        }
        device_id = (TextView) convertView.findViewById(R.id.id_device);
        device_name = (TextView) convertView.findViewById(R.id.nama_device);

        Device device = getItem(position);

        device_id.setText(device.getDeviceId());
        device_name.setText(device.getDeviceName());


        return convertView;
    }
}
