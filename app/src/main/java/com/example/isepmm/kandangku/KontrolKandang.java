package com.example.isepmm.kandangku;

/**
 * Created by Isepmm on 21/01/2018.
 */

public class KontrolKandang {
    private int mSuhuTertinggi;
    private int mSuhuTerendah;

    public KontrolKandang(int mSuhuTertinggi, int mSuhuTerendah) {
        this.mSuhuTertinggi = mSuhuTertinggi;
        this.mSuhuTerendah = mSuhuTerendah;
    }

    public int getmSuhuTertinggi() {
        return mSuhuTertinggi;
    }

    public int getmSuhuTerendah() {
        return mSuhuTerendah;
    }
}
