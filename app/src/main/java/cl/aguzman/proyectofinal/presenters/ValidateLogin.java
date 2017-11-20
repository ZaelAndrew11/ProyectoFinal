package cl.aguzman.proyectofinal.presenters;

import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.interfaces.ValidateLoginCallback;

public class ValidateLogin {
    private ValidateLoginCallback callback;

    public ValidateLogin(ValidateLoginCallback callback) {
        this.callback = callback;
    }

    public void loginValidate(){
        if (new CurrentUser().getCurrentUser() != null){
            callback.logged();
        }else{
            callback.signUp();
        }

    }
}
