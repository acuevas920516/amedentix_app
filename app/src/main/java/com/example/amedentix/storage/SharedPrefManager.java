package com.example.amedentix.storage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.amedentix.activities.FormLogin;
import com.example.amedentix.activities.MainActivity;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "my_shared_preff";

    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }


    public static synchronized SharedPrefManager getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }


    public void saveUser(String email, String passw, String token, String id) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("id", id);
        editor.putString("email", email);
        editor.putString("passw", passw);
        editor.putString("token", token);

        editor.apply();

    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", "").equals("");
    }

    public String getToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", null);
    }

    public String getId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", null);
    }

    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

}
