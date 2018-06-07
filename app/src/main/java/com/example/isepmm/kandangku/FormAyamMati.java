package com.example.isepmm.kandangku;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class FormAyamMati extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;

    private String mKey;
    private EditText ayammati;
    private EditText tanggalsekarang;
    private Button simpan;
    private String idDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_ayam_mati);
        mKey = getIntent().getStringExtra("KeyValue");
        tanggalsekarang = (EditText) findViewById(R.id.tgl_sekarang);
        ayammati = (EditText) findViewById(R.id.ayam_mati);
        simpan = (Button) findViewById(R.id.simpan);
        idDevice = getIntent().getStringExtra("idDevice");

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        tanggalsekarang.setKeyListener(null);


        tanggalsekarang.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePicker(tanggalsekarang);
                }
            }
        });
        //input ke firebase
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationInputField()) {
                    return;
                }
                long unixDate = changeStringToUnixTimestamp(tanggalsekarang.getText().toString());
                String a = ayammati.getText().toString();
                Mati mati = new Mati(unixDate,Integer.valueOf(a));
                mDatabaseReference.child(idDevice).child("Periode").child(mKey).child("Ayam_Mati").push().setValue(mati);
                getDataHistory();
                finish();
                Toast.makeText(FormAyamMati.this, "Data Berhasil Disimpan", Toast.LENGTH_LONG).show();
            }
        });

    }
    //Tanggal (android:inputType="date")
    private void datePicker(final EditText editText) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                //selectedday = selectedday + 1;
                Calendar c = Calendar.getInstance();
                c.set(selectedyear, selectedmonth, selectedday);
                Date date = c.getTime();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                editText.setText(format.format(date));
            }
        }, mYear, mMonth, mDay);
        mDatePicker.show();
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    private long changeStringToUnixTimestamp(String textDate){
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = (Date)formatter.parse(textDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  date.getTime()/1000;
    }

    private boolean validationInputField() {
        boolean isValid = true;
        if (ayammati.getText().toString().equals("")) {
            Toast.makeText(this, R.string.empty_message, Toast.LENGTH_LONG).show();
            isValid = false;
        }else if (tanggalsekarang.getText().toString().equals("")) {
            Toast.makeText(this, R.string.empty_message, Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    private void getDataHistory() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child(idDevice).child("Periode").child(mKey).child("Ayam_Mati")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long jumlahTotal = 0;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            jumlahTotal = jumlahTotal + data.child("jumlah_ayam_mati").getValue(Long.class);
                        }
                        mDatabaseReference
                                .child(idDevice)
                                .child("Periode")
                                .child(mKey)
                                .child("total_ayam_mati")
                                .setValue(jumlahTotal);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}