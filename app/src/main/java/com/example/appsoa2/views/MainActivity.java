package com.example.appsoa2.views;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import android.app.Activity;
import android.app.ProgressDialog;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.example.appsoa2.interfaces.MainActivityContract;
import com.example.appsoa2.presenters.MainPresenter;

import net.londatiga.android.bluetooth.R;

public class MainActivity extends Activity implements MainActivityContract.ViewMVP {

    private MainActivityContract.PresenterMVP presenter;
    private static final String TAG = "MainActivity";
    private TextView textView;

    private TextView txtEstado;
    private ProgressDialog mProgressDlg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // bloquear
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setContentView(R.layout.activity_main);
        this.initialize();
    }

    private void initialize() {

        Button btnPrimary = findViewById(R.id.button_primary);
        Button btnSecondary = findViewById(R.id.button_secondary);

        btnPrimary.setOnClickListener(this.btnListener);
        btnSecondary.setOnClickListener(this.btnListener);

        this.textView = this.findViewById(R.id.textView);
        this.presenter = new MainPresenter(this);
        this.txtEstado = (TextView) findViewById(R.id.txtEstado);
        this.mProgressDlg = new ProgressDialog(this);
        this.mProgressDlg.setCancelable(false);
        presenter.initializeBluetoothModule(this, mProgressDlg);
        Log.i(TAG, "Paso al estado Createad");
    }


    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_primary:
                    Log.i(TAG, "Ir a pantalla de adm de luminosidad ");
                    startActivity(presenter.prepareToNavigateToPrimaryScreen(MainActivity.this));
                    break;
                case R.id.button_secondary:
                    Log.i(TAG, "Click en SECONDARY ");
                    try {
                        startActivity(presenter.prepareToNavigateToSecondaryScreen(MainActivity.this));
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
    public void onPause() {
        this.presenter.turnOffBluetoothAdapter();
        super.onPause();
    }

    @Override
    public void onResume() {
        showToast("Pantalla lista");
        this.presenter.reconnectBluetoothDevice();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // unregisterReceiver(mReceiver);
        super.onDestroy();
        presenter.unregisterReceiver();
    }

    @Override
    public void showResultOnLabel(String string) {
        Log.i(TAG, "Se ejecuta metodo para setear el string");
        this.textView.setText(string);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        showToast(presenter.grantPermissions(requestCode, permissions, grantResults, this));
    }

}

