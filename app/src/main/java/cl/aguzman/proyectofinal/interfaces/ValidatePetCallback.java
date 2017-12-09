package cl.aguzman.proyectofinal.interfaces;

import android.app.Dialog;
import android.app.ProgressDialog;

public interface ValidatePetCallback {
    void successPet(String path, String namePet, Dialog dialog, ProgressDialog progressDialog);
    void failedPet(int message);

}
