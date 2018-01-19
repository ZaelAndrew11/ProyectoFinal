package cl.aguzman.proyectofinal.presenters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import cl.aguzman.proyectofinal.interfaces.ValidateEmptyListsCallback;

public class ValidateEmptyLists {

    private ValidateEmptyListsCallback callback;

    public ValidateEmptyLists(ValidateEmptyListsCallback callback) {
        this.callback = callback;
    }

    public void validate(DatabaseReference reference) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    callback.emptyList();
                } else {
                    callback.notEmptyList();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
