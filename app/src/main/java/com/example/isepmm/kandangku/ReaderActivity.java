package com.example.isepmm.kandangku;

import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.zip.Inflater;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ReaderActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    public static final int REQUEST_CAMERA = 1;
    public ZXingScannerView scannerView;
    private boolean cek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!checkPermission()) {
                requestPermission();
            }
        }

    }

    /**
     * checking for camera permission
     * @return is camera granted
     */
    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(ReaderActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * request permission
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    /**
     * result of request permission
     * @param requestCode request code
     * @param permission permission
     * @param grantResults grant result
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String permission[], @NonNull int grantResults[]) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (!cameraAccepted) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                displayAlertMessage(
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                                    requestPermission(new String[]{CAMERA}, REQUEST_CAMERA);}
                                            }

                                            private void requestPermission(String[] strings, int requestCamera) {
                                            }
                                        });
                                return;
                            }
                        }
                    }

                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (checkPermission()) {
                scannerView = new ZXingScannerView(this);
                setContentView(scannerView);
            }
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        } else {
            requestPermission();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    /**
     * showing alert message
     * @param listener listen for action click
     */
    public void displayAlertMessage(DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(ReaderActivity.this)
                .setMessage("you need to allow access for both permission")
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    public void handleResult(Result result) {
        final String idDevice = result.getText();

        readDeviceData(idDevice);
    }

    private void readDeviceData(final String deviceID) {
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.keepSynced(true);
        myRef.child("User").child(mCurrentUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String idBarcode = dataSnapshot.getKey();
                Log.i("IDDEVICE", "onChildAdded: "+idBarcode);
                if (idBarcode.equals(deviceID)){
                    finish();
                    Toast.makeText(ReaderActivity.this, "ID Device sudah ada!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    showDialog(deviceID);
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
    }

    public void showDialog(final String idDevice){
        AlertDialog.Builder builder = new AlertDialog.Builder(ReaderActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.form_device_name, null);
        final EditText editTextName = viewDialog.findViewById(R.id.edit_text_device);

        builder.setView(viewDialog).setPositiveButton(R.string.simpan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                String deviceName = editTextName.getText().toString().trim();

                saveToDatabase(idDevice, deviceName);
                goToDeviceActivity(idDevice);

            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                finish();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void goToDeviceActivity(String idDevice){
        Intent gotoDeviceActivity = new Intent(ReaderActivity.this, DeviceActivity.class);
        gotoDeviceActivity.putExtra("idDevice", idDevice);
        startActivity(gotoDeviceActivity);
    }

    public void saveToDatabase(String idDevice, String deviceName){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dataDevice = FirebaseDatabase.getInstance().getReference().child("User");
        Device newDevice = new Device(deviceName,false);
        dataDevice.child(firebaseUser.getUid()).child(idDevice).setValue(newDevice);

    }
}
