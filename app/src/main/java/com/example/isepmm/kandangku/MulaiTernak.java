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

public class MulaiTernak extends AppCompatActivity {

    DatabaseReference lastkey = FirebaseDatabase.getInstance().getReference().child("MainProgram").child("LastKey");

    private EditText tanggalmulai;
    private EditText jumlahayam;
    private EditText hargadoc;
    private Button mulai;

    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Mulai Berternak");
        setContentView(R.layout.activity_mulai_ternak);

        tanggalmulai = (EditText) findViewById(R.id.tanggal_datang_doc);
        jumlahayam = (EditText) findViewById(R.id.jumlah_ayam);
        hargadoc = (EditText) findViewById(R.id.harga_doc);
        mulai = (Button) findViewById(R.id.mulai);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        tanggalmulai.setKeyListener(null);

        tanggalmulai.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePicker(tanggalmulai);
                }
            }
        });
        //Input Ke Firebase
        mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationInputField()) {
                    return;
                }
                long unixDate = changeStringToUnixTimestamp(tanggalmulai.getText().toString());
                String a = jumlahayam.getText().toString();
                String b = hargadoc.getText().toString();
                Kandang kandang = new Kandang(unixDate,Integer.valueOf(a),Long.valueOf(b),0,0,0,0,0,0,0,0);
                String mKey = mDatabaseReference.push().getKey();
                mDatabaseReference.child("MainProgram").child("Periode").child(mKey).setValue(kandang);
                lastkey.setValue(mKey);
                finish();

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
                //selectedmonth = selectedmonth + 1;
                Calendar c = Calendar.getInstance();
                c.set(selectedyear, selectedmonth, selectedday);
                Date date = c.getTime();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                editText.setText(format.format(date));
            }
        }, mYear, mMonth, mDay);
        mDatePicker.show();
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    private long changeStringToUnixTimestamp(String textDate){
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = (Date)formatter.parse(textDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  date.getTime()/1000;
    }
    //Toast
    private boolean validationInputField() {
        boolean isValid = true;
        if (tanggalmulai.getText().toString().equals("")) {
            Toast.makeText(this, R.string.empty_message, Toast.LENGTH_LONG).show();
            isValid = false;
       } else if (jumlahayam.getText().toString().equals("")) {
            Toast.makeText(this, R.string.empty_message, Toast.LENGTH_LONG).show();
            isValid = false;
        } else if (hargadoc.getText().toString().equals("")) {
            Toast.makeText(this, R.string.empty_message, Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
}
