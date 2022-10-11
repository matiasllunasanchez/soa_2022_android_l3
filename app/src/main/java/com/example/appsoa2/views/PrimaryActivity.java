package com.example.appsoa2.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appsoa2.R;
import com.example.appsoa2.interfaces.PrimaryActivityContract;
import com.example.appsoa2.presenters.PrimaryPresenter;
import com.example.appsoa2.views.components.MinMaxFilter;

import java.util.Formatter;

public class PrimaryActivity extends AppCompatActivity implements PrimaryActivityContract.ViewMVP {
    private static final String TAG = "PrimaryActivity";
    private PrimaryActivityContract.PresenterMVP presenter;

    private Button btnSave, btnBack;
    private TextView txtCurrentLightLevel;
    private EditText inputTextbox;
    private SeekBar seekBarValue;
    private boolean changedProgrammatically = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        initialize();
    }

    private void initialize() {
        initializeButtons();
        initializeLabels();
        initializeOthers();
        presenter = new PrimaryPresenter(this);
        Log.i(TAG, "Paso al estado Createad");
    }

    private void initializeButtons() {
        btnSave = findViewById(R.id.button_primary_save);
        btnBack = findViewById(R.id.button_primary_back);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.saveInputValue(Integer.parseInt(String.valueOf(inputTextbox.getText())));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Click en BACK ");
                try {
                    Intent k = new Intent(PrimaryActivity.this, MainActivity.class);
                    startActivity(k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initializeLabels() {
        txtCurrentLightLevel = findViewById(R.id.text_primary_currentLightLevel);
    }

    private void initializeOthers() {
        // Se inicializa y se pone valor min y max
        seekBarValue = (SeekBar) findViewById(R.id.seekbar_primary_finalLightLevel);
        inputTextbox = (EditText) findViewById(R.id.input_primary_finalLightLevel);
        inputTextbox.setFilters(new InputFilter[]{new MinMaxFilter("0", "100")});

        seekBarValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TAG, "Mientras cambia la barra" + progress);
                // txtCurrentLightLevel.setText(String.valueOf(progress) + "%");
                inputTextbox.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "Apenas arranca a cambiar la barra");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "Al terminar de cambiar la barra");
                // inputTextbox.setText(String.valueOf(seekBar.val));
            }
        });

        inputTextbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "Antes de cambiar input");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "Mientra cambia input" + i);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG, "Despues de cambiar input" + editable);

                if (editable.toString().equals(""))
                    seekBarValue.setProgress(0);
                else {
                    seekBarValue.setProgress(Integer.parseInt(String.valueOf(editable)));
                }
            }
        });
    }

    @Override
    public void setResultValue(int value) {
        Log.i(TAG, "Se guardo valor de luz: " + value);
        txtCurrentLightLevel.setText("Porcentaje de luz: " + String.valueOf(value) + "%");
    }
}
