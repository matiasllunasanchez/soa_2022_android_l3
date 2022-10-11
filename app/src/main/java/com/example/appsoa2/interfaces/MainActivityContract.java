package com.example.appsoa2.interfaces;

public interface MainActivityContract {
    // El contrato es una buena practica
    // Es una interfaz de interfaces y sirve para decirme los metodos disponibles de cada capa.

    interface ViewMVP {
        /// Metodos de la vista que ella usara para definir comportamientos de recepcion o envio de informacion
        void showResultOnLabel(String string); // Hace algo en la vista con el string recibido
    }

    interface ModelMVP {
        interface OnSendToPresenter {
            // Metodos que debe tener el presentador para manejar lo que le el modelo le mande por parametro, en este caso un string
            void showMessage(String string);
        }

        // Metodos propios del modelo que realizan procesos y llamadas a BD y demas cosas de regla de negocio
        // Pueden requerir de un presentador enviado por parametro para posteriormente realizar el feedback necesario hacia la vista
        // Con esto logro separar la logica del presentador, pudiendo reutilizar este metodo en varios presentadores.
        void processDataGetResult(MainActivityContract.ModelMVP.OnSendToPresenter presenter);
    }

    interface PresenterMVP {
        // El presentador al momento de interactuar con la vista sabe que tiene que darle accion al setString
        // Estos metodos va a usar el presentador para comunicarse con el modelo y la vista
        void onSendButtonClick();

        void onDestroy();
    }
}
