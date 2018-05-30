package com.example.isepmm.kandangku;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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

public class DeviceActivity extends AppCompatActivity {
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

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        listDevice = (ListView) findViewById(R.id.listView_device);
        emptyView = (TextView) findViewById((R.id.empty_device));
        addDevice = (FloatingActionButton) findViewById((R.id.scan_device));
        loadingData = (ProgressBar) findViewById(R.id.loading_data);

        final ArrayList<Device> devices = getData();

        mAdapter = new DeviceAdapter(this, devices);
        listDevice.setAdapter(mAdapter);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeviceActivity.this,ReaderActivity.class);
                startActivity(intent);
            }
        });
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        listDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent data = new Intent(DeviceActivity.this, MainActivity.class);
                String id = devices.get(i).getDeviceId();
                data.putExtra(MainActivity.ARGS_DEVICE_ID, id);
                Log.i(TAG, "idDeviceFIX : "+id);
                startActivity(data);
            }
        });
        setTitle("Kandangku");
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_keluar:
                signOutCheck();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOutCheck() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DeviceActivity.this);
        builder.setMessage("Yakin ingin keluar ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                FirebaseAuth.getInstance().signOut();
                                finish();
                            }
                        });
                    }
                }).setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

}
