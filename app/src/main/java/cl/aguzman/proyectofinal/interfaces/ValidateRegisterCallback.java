package cl.aguzman.proyectofinal.interfaces;

public interface ValidateRegisterCallback {
    void success();
    void successOnload();
    void defaultOnload();
    void failed(int error);
}
