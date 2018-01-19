package cl.aguzman.proyectofinal.presenters;

import android.util.Log;

import java.util.ArrayList;

import cl.aguzman.proyectofinal.interfaces.ValidateVetRequestCallback;

public class ValidateVetRequest {
    private ValidateVetRequestCallback callback;

    public ValidateVetRequest(ValidateVetRequestCallback callback) {
        this.callback = callback;
    }
    public void validateVet(ArrayList<String> strings){
        if (!strings.get(0).equals("He recibido su solicitud exitosamente.")){
            callback.isVet();
            Log.d("HOLA", String.valueOf(strings.get(0)));
        }
    }
}
