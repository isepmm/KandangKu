package com.example.isepmm.kandangku;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Isepmm on 31/05/2018.
 */

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    private static final String PREF_TOKEN = "token_id";

    private static final String TOKEN_ID = "TokenID";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_TOKEN, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void setToken(String token) {
        editor.putString(TOKEN_ID, token);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(TOKEN_ID, null);
    }
}