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
    private DetailCallback callback;
    private DatabaseReference likesRef;

    public ValidateDetail(DetailCallback callback) {
        this.callback = callback;
    }

    public void validateLike(String key){
        likesRef = new Queries().getLikes().child(new CurrentUser().getCurrentUid()).child(key);
        likesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    callback.disLike();
                }else {
                    callback.like();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void run(DatabaseReference reference, final String key, final String tag) {
        likesRef = new Queries().getLikes().child(new CurrentUser().getCurrentUid()).child(key);
        final DatabaseReference refVet = new Queries().getVetMin().child(key).child("score");
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                int score = mutableData.getValue(Vet.class).getScore();
                if (mutableData.getValue() == null) {
                    return Transaction.success(mutableData);
                }
                if (mutableData.getValue() != null) {
                    if (tag.equals("like")) {
                        callback.like();
                        likesRef.setValue(true);
                        score++;

                    } else {
                        callback.disLike();
                        likesRef.setValue(null);
                        score--;
                    }
                    mutableData.child("score").setValue(score);
                    refVet.setValue(score);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d("ERROR", "likeTransaction:onComplete:" + databaseError);
            }
        });
    }

    public void verificationPets(String uid) {
        String currentUid = new CurrentUser().getCurrentUid();
        DatabaseReference refPets = new Queries().getPetsnames().child(currentUid);

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
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

    }

}
