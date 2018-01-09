package cl.aguzman.proyectofinal.interfaces;

import java.util.Map;

public interface ValidateUploadVetCallback {
    void success();
    void failed(int error);
    void successUpload();
    void faildUpload();
    void successImage(Map<String, Object> map, String url);
}
