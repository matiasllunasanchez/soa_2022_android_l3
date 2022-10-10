package com.example.appsoa2.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appsoa2.R;
import com.example.appsoa2.contracts.MainActivityContract;

public class PrimaryActivity extends AppCompatActivity implements MainActivityContract.ViewMVP{

    @Override // Este metodo lo dejamos fijo
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
    }


    @Override
    public void showResultOnLabel(String string) {

    }
}
