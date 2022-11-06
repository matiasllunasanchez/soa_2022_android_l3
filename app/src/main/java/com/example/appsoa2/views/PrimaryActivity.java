package com.example.appsoa2.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import net.londatiga.android.bluetooth.R;
import com.example.appsoa2.interfaces.PrimaryActivityContract;
import com.example.appsoa2.presenters.PrimaryPresenter;
import com.example.appsoa2.views.components.MinMaxFilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Formatter;
import java.util.UUID;

public class PrimaryActivity extends Activity implements PrimaryActivityContract.ViewMVP {
    private static final String TAG = "PrimaryActivity";
    private PrimaryActivityContract.PresenterMVP presenter;
    private static final int MIN_LIGHT_VALUE = 30;
    private static final int EMPTY_LIGHT_VALUE = 0;
    private static final int MEDIUM_LIGHT_VALUE = 65;

    private Button btnSave, btnBack, btnRefresh;
    private TextView txtCurrentLightLevel;
    private EditText inputTextbox;
    private SeekBar seekBarValue;
    private ImageView lampImg;

    // Bluetooth Stuff
    Handler bluetoothIn;
    final int handlerState = 0; //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;
    // SPP UUID service  - Funciona en la mayoria de los dispositivos
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String for MAC address del Hc05
    private static String address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_primary);
        this.initialize();
    }

    private void initialize() {
        this.initializeButtons();
        this.initializeBluetoothModule();
        this.initializeLabels();
        this.initializeOthers();
        this.presenter = new PrimaryPresenter(this);

        Log.i(TAG, "Paso al estado Createad");
    }

    private void initializeButtons() {
        this.btnSave = findViewById(R.id.button_primary_save);
        this.btnBack = findViewById(R.id.button_primary_back);
        this.btnRefresh = findViewById(R.id.button_primary_refresh);

        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lightValue = Integer.parseInt(String.valueOf(inputTextbox.getText()));
                presenter.saveInputValue(lightValue);
                // Mando 9 y luego el valor del 0 al 100.
                // Falta limitar esto de 10 a 90
                mConnectedThread.write("9");
                int lightResultValue = lightValue > 90 ? 90 : lightValue<10? 10: lightValue;
                mConnectedThread.write(String.valueOf(lightResultValue));
            }
        });

        this.btnBack.setOnClickListener(new View.OnClickListener() {
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

        this.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Solicito luminosidad actual
                mConnectedThread.write("1");
            }
        });

    }

    private void initializeLabels() {
        this.txtCurrentLightLevel = this.findViewById(R.id.text_primary_currentLightLevel);
    }

    private void initializeOthers() {
        this.seekBarValue = (SeekBar) this.findViewById(R.id.seekbar_primary_finalLightLevel);
        this.inputTextbox = (EditText) this.findViewById(R.id.input_primary_finalLightLevel);
        this.inputTextbox.setFilters(new InputFilter[]{new MinMaxFilter("0", "100")});
        this.lampImg = (ImageView) this.findViewById(R.id.image_primary_led);

        this.seekBarValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TAG, "Mientras cambia la barra" + progress);
                inputTextbox.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "Apenas arranca a cambiar la barra");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "Al terminar de cambiar la barra");
            }
        });

        this.inputTextbox.addTextChangedListener(new TextWatcher() {
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
        Log.i(TAG, "Estee " + EMPTY_LIGHT_VALUE);
        // this.setLampLevel(value);
        this.txtCurrentLightLevel.setText("Porcentaje de luz: " + String.valueOf(value) + "%");
        this.setLampLevel(value);
    }

    private void setLampLevel(int value) {
        if (value > EMPTY_LIGHT_VALUE) {
            if (value > MIN_LIGHT_VALUE) {
                if (value > MEDIUM_LIGHT_VALUE) {
                    this.lampImg.setImageResource(R.drawable.lamp1_full);
                } else {
                    this.lampImg.setImageResource(R.drawable.lamp1_med);
                }
            } else {
                this.lampImg.setImageResource(R.drawable.lamp1_min);
            }
        } else {
            this.lampImg.setImageResource(R.drawable.lamp_values);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Bluetooth zone
    private void initializeBluetoothModule(){
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        //defino el Handler de comunicacion entre el hilo Principal  el secundario.
        //El hilo secundario va a mostrar informacion al layout atraves utilizando indeirectamente a este handler
        this.bluetoothIn = manejadorMensajes_PrimaryThread();
    }

    @Override
    //Cuando se ejecuta el evento onPause se cierra el socket Bluethoot, para no recibiendo datos
    public void onPause() {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    @Override
    //Cada vez que se detecta el evento OnResume se establece la comunicacion con el HC05, creando un
    //socketBluethoot
    public void onResume() {
        super.onResume();

        Intent intent=getIntent();
        Bundle extras=intent.getExtras();
        Log.i(TAG, "extraaaas  "+extras);
        address = extras.getString("Direccion_Bluethoot");
        Log.i(TAG, "Adresss "+address);
         BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        }
        catch (IOException e) {
            showToast( "La creacción del Socket fallo");
        }
        // Establish the Bluetooth socket connection.
        try {
            btSocket.connect();
        }
        catch (IOException e) {
            try {
                btSocket.close();
            }
            catch (IOException e2) {
                //insert code to deal with this
            }
        }

        // Va  recibir datos
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        // Mando un caracter al hacer onResume para chequear la conexion del dispositivo.
        // Si no tira excepcion esta bien. Va a lanzar el metodo WRITE o FINISH
        // Depaso pido la luminosidad actual para plasmarla en la pantalla.
        mConnectedThread.write("1");
    }

    //Handler que sirve que permite mostrar datos en el Layout al hilo secundario
    private Handler manejadorMensajes_PrimaryThread () {
         @SuppressLint("HandlerLeak") Handler handlerObject = new Handler() {
            @SuppressLint("HandlerLeak")
            public void handleMessage(android.os.Message msg)
            {
                // Manejo mensaje recibido a travez del Thread secundario
                if (msg.what == handlerState) {
                    // Lo unico que recibo es luminosidad actual
                    // Caso para UN mensaje recibido desde el dispositivo.
                    int currentLightLevel = (int) msg.obj;
                    presenter.saveInputValue(currentLightLevel);
                }
            }
        };
        return handlerObject;
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        // Crear el socket para comunicacion por BT
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //Constructor de la clase del Thread Secondary
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        // Metodo que espera mensajes desde el dispositivo
        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            while (true)
            { // Queda espearndo mensajes desde el dispositivo.
                try
                {
                    //se leen los datos del Bluethoot
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);

                    //se muestran en el layout de la activity, utilizando el handler del hilo
                    // principal antes mencionado
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        // Metodo para enviar / escribir en el dispositivo
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                showToast("La conexion fallo");
                finish();

            }
        }
    }

}
