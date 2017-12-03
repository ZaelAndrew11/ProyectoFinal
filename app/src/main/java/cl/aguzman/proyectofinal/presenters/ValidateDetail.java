package cl.aguzman.proyectofinal.presenters;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import cl.aguzman.proyectofinal.interfaces.DetailCallback;
import cl.aguzman.proyectofinal.models.Vet;

public class ValidateDetail {
    DetailCallback callback;

    public   void run(DatabaseReference reference){
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Vet vet = mutableData.getValue(Vet.class);
                Log.d("DATAS", String.valueOf(vet));
                if(mutableData.getValue() == null){
                    return Transaction.success(mutableData);
                }

                if(mutableData.getValue() != null){
                    int like = vet.getScore()+1;
                    mutableData.child("score").setValue(like);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
            }
        });
    }
}
