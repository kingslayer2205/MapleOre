package com.example.mapleore.Buyers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mapleore.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

    check = getIntent().getStringExtra("check");

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(check.equals("settings"))
        {

        }
        else if(check.equals("login"))
        {

        }

    }
}
