package cl.aguzman.proyectofinal.presenters;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.interfaces.ValidateUploadVetCallback;

public class ValidateUploadVet {
    private ValidateUploadVetCallback callback;

    public ValidateUploadVet(ValidateUploadVetCallback callback) {
        this.callback = callback;
    }

    public void validateupload(String name, String email, String address, String phone, String description, String urlImage, String rut) {

        if (name.length() > 0 && email.length() > 0 && address.length() > 0 && phone.length() > 0 && description.length() > 0 && rut.length() > 0 && urlImage != null) {
            if (!new ValidateFields().isValidEmail(email)) {
                callback.failed(R.string.email_error);
            }else if(!new ValidateFields().isValidPhoneNumber(phone)){
                callback.failed(R.string.phone_error);
            }else if(!new ValidateFields().isValidRut(rut)){
                callback.failed(R.string.rut_error);
            }else {
                callback.success();
            }
        } else {
            callback.failed(R.string.missing_fields);
        }
    }
}
