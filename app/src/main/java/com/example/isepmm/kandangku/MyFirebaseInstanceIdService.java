package com.example.isepmm.kandangku;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.content.ContentValues.TAG;


public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String REG_TOKEN= "REG_TOKEN";

    @Override
    public void onTokenRefresh() {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.i(REG_TOKEN, "onTokenRefresh: "+recent_token);
        PrefManager pref = new PrefManager(MyFirebaseInstanceIdService.this);
        pref.setToken(recent_token);
    }
}
