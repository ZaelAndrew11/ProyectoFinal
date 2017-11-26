package cl.aguzman.proyectofinal.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Queries {
    private  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public DatabaseReference root(){
        return databaseReference;
    }

    public DatabaseReference getVet(){
        return databaseReference.child("veterinarios");
    }

}
