package cl.aguzman.proyectofinal.interfaces;

public interface ValidateUploadVetCallback {
    void success();
    void emailErr(int error);
    void failed(int error);
}
