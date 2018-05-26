package com.example.isepmm.kandangku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

public class DeviceActivity extends Activity {
    private String idDevice;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference uidUser = FirebaseDatabase.getInstance().getInstance().getReference().child("User");
    DatabaseReference deviceId = FirebaseDatabase.getInstance().getInstance().getReference().child("User").child(firebaseUser.getUid());


    private ArrayList<Device> arrayDevices= new ArrayList<>();

    private ListView listDevice;
    private TextView emptyView;
    private FloatingActionButton addDevice;
    private ProgressBar loadingData;
    private DeviceAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        listDevice = (ListView) findViewById(R.id.listView_device);
        emptyView = (TextView) findViewById((R.id.empty_device));
        addDevice = (FloatingActionButton) findViewById((R.id.scan_device));
        loadingData = (ProgressBar) findViewById(R.id.loading_data);

        mAdapter = new DeviceAdapter(this,getData());
        listDevice.setAdapter(mAdapter);

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeviceActivity.this,ReaderActivity.class);
                startActivity(intent);
            }
        });

        listDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent data = new Intent(DeviceActivity.this, MainActivity.class);
                data.putExtra(MainActivity.ARGS_DEVICE_ID, idDevice);
                Log.i(TAG, "idDeviceFIX : "+idDevice);
                startActivity(data);
            }
        });
    }

    public ArrayList<Device> getData(){
        final ArrayList<Device> curData = new ArrayList<>();
        loadingData.setVisibility(View.VISIBLE);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dataDevice = FirebaseDatabase.getInstance().getReference().child("User");
        dataDevice.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                curData.clear();
                if(dataSnapshot != null){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        Device device = data.getValue(Device.class);
                        Device curDevice = new Device(device,data.getKey());
                        curData.add(curDevice);
                        idDevice = data.getKey();
                    }
                }
                loadingData.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return curData;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        // return true so that the menu pop up is opened
        return true;

    }

}
