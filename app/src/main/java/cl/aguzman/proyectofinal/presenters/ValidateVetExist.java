package cl.aguzman.proyectofinal.presenters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.GetContentCallback;

public class ValidateVetExist {

    private GetContentCallback callback;

    public ValidateVetExist(GetContentCallback callback) {
        this.callback = callback;
    }

    public void validateVet(){
        DatabaseReference ref = new Queries().getVet().child(new CurrentUser().getCurrentUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    callback.vetExist();
                }else{
                    callback.VetNotExist();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
