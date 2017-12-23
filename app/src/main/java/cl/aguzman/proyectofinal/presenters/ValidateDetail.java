package cl.aguzman.proyectofinal.presenters;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.DetailCallback;
import cl.aguzman.proyectofinal.models.Vet;

public class ValidateDetail {
    DetailCallback callback;

    public ValidateDetail(DetailCallback callback) {
        this.callback = callback;
    }

    public void run(DatabaseReference reference) {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Vet vet = mutableData.getValue(Vet.class);
                Log.d("DATAS", String.valueOf(vet));
                if (mutableData.getValue() == null) {
                    return Transaction.success(mutableData);
                }

                if (mutableData.getValue() != null) {
                    int like = vet.getScore() + 1;
                    mutableData.child("score").setValue(like);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
            }
        });
    }//end run

    public void verificationPets(String uid) {
        String currentUid = new CurrentUser().getCurrentUid();
        DatabaseReference refPets = new Queries().getPetsnames().child(currentUid);

        Log.d("UID", uid + " - " + currentUid);

        if (uid.equals(currentUid)) {
            callback.sameVet();
        } else {
            refPets.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        callback.hasPet();
                    } else {
                        callback.notPet();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }

    }
}
