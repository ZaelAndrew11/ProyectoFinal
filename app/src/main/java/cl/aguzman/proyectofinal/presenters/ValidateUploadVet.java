package cl.aguzman.proyectofinal.presenters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.interfaces.ValidateUploadVetCallback;

public class ValidateUploadVet {
    private ValidateUploadVetCallback callback;

    public ValidateUploadVet(ValidateUploadVetCallback callback) {
        this.callback = callback;
    }

    public void validateupload(String name, String email, String address, String phone, String description, String urlImage) {

        if (name.toString().length() > 0 && email.toString().length() > 0 && address.toString().length() > 0 && phone.toString().length() > 0 && description.toString().length() > 0 && urlImage != null) {
            if (!emailValidator(email)) {
                callback.emailErr(R.string.email_error);
            } else {
                callback.success();
            }
        } else {
            callback.failed(R.string.missing_fields);
        }
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
