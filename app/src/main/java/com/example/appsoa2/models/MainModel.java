package com.example.appsoa2.models;

import com.example.appsoa2.interfaces.MainActivityContract;

public class MainModel implements MainActivityContract.ModelMVP {
    // Extiende del contrato


    // Sabe que cualquier presentador que tenga va a tner q ejecutar el onFinished por que le contrato lo dice.
    @Override
    public void processDataGetResult(OnSendToPresenter presenter) {
        // Hace logica de negocio y devuelve algo para mostrar a la vista, en este caso el mensaje resultado.
        // Procesa informacion, consulta base de datos, hace operaciones y aplica reglas de negocio
        // Devuelve el resultado mediante un mensaje al presentador para que el se ocupe de manejarlo.
        presenter.showMessage("MENSAJE DE RESULTADO DESDE EL MODELO PARA LA VISTA");
    }
}
