package cl.aguzman.proyectofinal;

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
