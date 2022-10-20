package com.example.appsoa2.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appsoa2.presenters.MainPresenter;
import com.example.appsoa2.R;
import com.example.appsoa2.interfaces.MainActivityContract;

public class MainActivity extends AppCompatActivity implements MainActivityContract.ViewMVP {

    private MainActivityContract.PresenterMVP presenter;
    private static final String TAG = "MainActivity";
    private TextView textView;

    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.buttonNext:
                    Log.i(TAG, "Click en NEXT ");
                    break;
                case R.id.buttonSend:
                    Log.i(TAG, "Click en SEND ");
                    presenter.onSendButtonClick();
                    break;
                case R.id.buttonStartAct:
                    Log.i(TAG, "Click en START ");
                    break;
                case R.id.button_primary:
                    Log.i(TAG, "Click en PRIMARY ");
                    try {
                        Intent k = new Intent(MainActivity.this, PrimaryActivity.class);
                        startActivity(k);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.button_secondary:
                    Log.i(TAG, "Click en SECONDARY ");
                    try {
                        Intent k = new Intent(MainActivity.this, SecondaryActivity.class);
                        startActivity(k);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    Log.i(TAG, "Default de switch botones ");
                    throw new IllegalStateException("Unexpected value " + view.getId());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        Button btnStart = findViewById(R.id.buttonStartAct);
        Button btnNext = findViewById(R.id.buttonNext);
        Button btnSend = findViewById(R.id.buttonSend);
        Button btnPrimary = findViewById(R.id.button_primary);
        Button btnSecondary = findViewById(R.id.button_secondary);

        btnStart.setOnClickListener(btnListener);
        btnNext.setOnClickListener(btnListener);
        btnSend.setOnClickListener(btnListener);
        btnPrimary.setOnClickListener(btnListener);
        btnSecondary.setOnClickListener(btnListener);

        textView = findViewById(R.id.textView);
        presenter = new MainPresenter(this);

        Log.i(TAG, "Paso al estado Createad");
    }

    @Override
    public void showResultOnLabel(String string) {
        Log.i(TAG, "Se ejecuta metodo para setear el string");
        this.textView.setText(string);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Paso al estado Resumed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Paso al estado Stopped");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Paso al estado Paused");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Paso al estado Restarted");
    }

}