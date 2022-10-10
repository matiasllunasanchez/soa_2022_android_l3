package com.example.appsoa2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Contract.ViewMVP {

    private Contract.PresenterMVP presenter;
    private static final String TAG = "MainActivity";
    private TextView textView;

    // Se crea el "escuchador de botones"
    // Este es un atributo privado de la vista, y es una forma de manejar el listener general de la vista
    // Particularmente se crea un listener que solo maneja el resultado de clickear en algun boton
    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                // El ID del elemento seleccionado desde la vista es recibido como parametro.
                // Dependiendo de que boton fue seleccionado se hacen diferentes cosas o se tira error.
                case R.id.buttonNext:
                    Log.i(TAG,"Click en NEXT ");
                    break;
                case R.id.buttonSend:
                    Log.i(TAG,"Click en SEND ");
                    presenter.onSendButtonClick();
                    break;
                case R.id.buttonStartAct:
                    Log.i(TAG,"Click en START ");
                    break;
                case R.id.button_primary:
                    Log.i(TAG,"Click en PRIMARY ");
                    break;
                case R.id.button_secondary:
                    Log.i(TAG,"Click en SECONDARY ");
                    break;
                default:
                    Log.i(TAG,"Default de switch botones ");
                    throw new IllegalStateException("Unexpected value "+ view.getId());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Esto queda fijo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Se instancian los botones durante la creacion de la vista
        // Aca se le asigna a cierta instancia un id en la vista real.
        Button btnStart = findViewById(R.id.buttonStartAct);
        Button btnNext = findViewById(R.id.buttonNext);
        Button btnSend = findViewById(R.id.buttonSend);
        Button btnPrimary = findViewById(R.id.button_primary);
        Button btnSecondary = findViewById(R.id.button_secondary);

        // Aca se linkea el listener que va a usar cada elemento, en este caso es el mismo para todos los botones.
        btnStart.setOnClickListener(btnListener);
        btnNext.setOnClickListener(btnListener);
        btnSend.setOnClickListener(btnListener);
        btnPrimary.setOnClickListener(btnListener);
        btnSecondary.setOnClickListener(btnListener);

        // Esta seria otra forma de crear un listener para un boton que no sea la de crear un listener general para botones
        // Conviene usarla solo si existe un solo boton en la vista
        /*
        btnSecondary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        */

        textView = findViewById(R.id.textView);
        presenter = new Presenter(this);



        Log.i(TAG,"Paso al estado Createad");
    }

    @Override
    public void showResultOnLabel(String string) {
        // La vista sabe que su forma de mostrar el resultado es sobre un label de texto.
        // Entonces trata el string recibido y se lo asigna al input correspondiente.
        // En este caso hay un solo textLabel en la vista, por lo que se le asigna el resultado
        Log.i(TAG,"Se ejecuta metodo para setear el string");
        this.textView.setText(string);
    }


    // Los siguientes metodos sobreescrito son solo para mostrar cuando la activity pasa a cierto estado
    // Puedo hacer lo que quiera en estos momentos si los se manejar, aca nada mas muestro logs
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"Paso al estado Resumed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"Paso al estado Stopped");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"Paso al estado Paused");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG,"Paso al estado Restarted");
    }

}