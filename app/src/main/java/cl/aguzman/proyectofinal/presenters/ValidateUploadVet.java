package cl.aguzman.proyectofinal.presenters;

import android.text.TextUtils;
import android.util.Patterns;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.interfaces.ValidateUploadVetCallback;

public class ValidateUploadVet {
    private ValidateUploadVetCallback callback;

    public ValidateUploadVet(ValidateUploadVetCallback callback) {
        this.callback = callback;
    }

    public void validateupload(String name, String email, String address, String phone, String description, String urlImage) {

        if (name.toString().length() > 0 && email.toString().length() > 0 && address.toString().length() > 0 && phone.toString().length() > 0 && description.toString().length() > 0 && urlImage != null) {
            if (!isValidEmail(email)) {
                callback.failed(R.string.email_error);
            }else if(!isValidPhoneNumber(phone)){
                callback.failed(R.string.phone_error);
            } else {
                callback.success();
            }
        } else {
            callback.failed(R.string.missing_fields);
        }
    }


    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }
}
