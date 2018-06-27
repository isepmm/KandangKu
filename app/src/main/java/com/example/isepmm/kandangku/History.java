package com.example.isepmm.kandangku;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class History extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private String mKey;
    private ListView listHistory;
    private MatiAdapter mAdapter;
    private String idDevice;
    ArrayList<Mati> list;

    TextView tanggalsekarang;
    TextView ayammati;
    TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mKey = getIntent().getStringExtra("KeyValue");//readkey
        listHistory = (ListView) findViewById(R.id.viewhistory);
        tanggalsekarang = (TextView) findViewById(R.id.tanggal_datang);
        ayammati = (TextView) findViewById(R.id.ayam_mati);
        empty = (TextView) findViewById(R.id.empty_view);

        idDevice = getIntent().getStringExtra("idDevice");

        getDataHistory();
        setTitle("Riwayat Ayam");

        //ArrayList
        list = new ArrayList<>();

        mAdapter = new MatiAdapter(this, list);
        listHistory.setAdapter(mAdapter);
        listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final View viewEditor = LayoutInflater.from(History.this).inflate(R.layout.history_editor, null);

                final EditText tanggal = (EditText)viewEditor.findViewById(R.id.tanggal_datang);
                final EditText ayamMati = (EditText)viewEditor.findViewById(R.id.ayam_mati);
                final String idMati = list.get(i).getId();
                tanggal.setKeyListener(null);
                tanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        datePicker(tanggal);
                    }
                });
                tanggal.setText(unixTimestimeToString(list.get(i).getTanggal_sekarang()));
                ayamMati.setText(String.valueOf(list.get(i).getJumlah_ayam_mati()));
                AlertDialog.Builder builder = new AlertDialog.Builder(History.this);
                builder.setTitle(R.string.mati)
                        .setView(viewEditor)
                        .setPositiveButton(R.string.simpan, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                long mTanggal = changeStringToUnixTimestamp(tanggal.getText().toString());
                                String mAyamMati = ayamMati.getText().toString();
                                Mati mati = new Mati(mTanggal,Integer.valueOf(mAyamMati));
                                Map<String, Object> hashMapMati = mati.toMap();
                                mDatabaseReference.child(idDevice)
                                        .child("Periode")
                                        .child(mKey)
                                        .child("Ayam_Mati")
                                        .child(idMati)
                                        .updateChildren(hashMapMati);
                                Toast.makeText(History.this, "Data Berhasil Disimpan", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        })
                        .create().show();
            }
        });
    }

    //Read History
    private void getDataHistory() {
        mDatabaseReference.child(idDevice).child("Periode").child(mKey).child("Ayam_Mati")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Mati mati = data.getValue(Mati.class);
                            Mati AyamMati = new Mati(mati, data.getKey());
                            list.add(AyamMati);
                            empty.setVisibility(View.GONE);
                            Log.i("AyamMati", "onDataChange: " + mati);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

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
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                editText.setText(format.format(date));
            }
        }, mYear, mMonth, mDay);
        mDatePicker.show();
        //mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private String unixTimestimeToString(long unixTimestime){
        Date date = new Date(unixTimestime*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf.format(date);
        return formattedDate;
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

}