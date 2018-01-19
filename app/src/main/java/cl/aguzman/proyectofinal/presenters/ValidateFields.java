package cl.aguzman.proyectofinal.presenters;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

public class ValidateFields {
    public boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    public boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == 8) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    public boolean isValidRut(CharSequence rut){
        if(!TextUtils.isEmpty(rut)){
            Pattern pattern = Pattern.compile("\\b\\d{1,8}\\-[K|k|0-9]+");
            return pattern.matcher(rut).matches();
        }
        return false;
    }
}
