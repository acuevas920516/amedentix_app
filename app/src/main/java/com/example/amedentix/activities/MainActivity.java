package com.example.amedentix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.amedentix.R;
import com.example.amedentix.storage.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn())
        {
            Intent intent = new Intent(this,Home.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_anim,R.anim.slide_left_anim);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn())
        {
            Intent intent = new Intent(this,Home.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_anim,R.anim.slide_left_anim);
        }
    }

    public void openFormLogin(View view)
    {
        Intent intent = new Intent(this,FormLogin.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_anim,R.anim.slide_left_anim);
    }

    public void openFormRegister(View view)
    {
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_anim,R.anim.slide_left_anim);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_right_anim,R.anim.slide_left_anim);
    }
}
