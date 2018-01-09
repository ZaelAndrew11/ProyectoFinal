package cl.aguzman.proyectofinal.presenters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.ValidateRegisterCallback;

public class ValidateEndRegister {
    private ValidateRegisterCallback callback;

    public ValidateEndRegister(ValidateRegisterCallback callback) {
        this.callback = callback;
    }

    public void validateRegisterOnload(){
        DatabaseReference reference = new Queries().getUser().child(new CurrentUser().getCurrentUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    callback.successOnload();
                }else {
                    callback.defaultOnload();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void validateRegister(String name, String email, String number, String adress){
        if(name.length() > 0 && email.length() > 0 && number.length() > 0 && adress.length() > 0){
            if(!new ValidateFields().isValidEmail(email)){
                callback.failed(R.string.email_error);
            }else if(!new ValidateFields().isValidPhoneNumber(number)){
                callback.failed(R.string.phone_error);
            }else {
                callback.success();
            }
        }else {
            callback.failed(R.string.missing_fields);
        }
    }
}
