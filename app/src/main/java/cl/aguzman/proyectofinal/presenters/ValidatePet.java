package cl.aguzman.proyectofinal.presenters;

import android.app.Dialog;
import android.app.ProgressDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.ValidatePetCallback;

public class ValidatePet {
    ValidatePetCallback callback;
    DatabaseReference refPets = new Queries().getPetsnames();
    String uid = new CurrentUser().getCurrentUid();

    public ValidatePet(ValidatePetCallback callback) {
        this.callback = callback;
    }

    public void validateUploadPet(final String name, final String photo, final Dialog dialog, final ProgressDialog progressDialog){
        if(name != "" && photo != null){
            refPets.child(uid).child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        callback.failedPet(R.string.pet_exist);
                    }else{
                        callback.successPet(photo, name, dialog, progressDialog);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }else{
            callback.failedPet(R.string.error_pet);
        }
    }
}
