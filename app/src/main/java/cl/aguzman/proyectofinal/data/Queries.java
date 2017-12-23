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

    public DatabaseReference getUser(){
        return databaseReference.child("users");
    }

    public DatabaseReference getPetsnames(){
        return databaseReference.child("mascotas");
    }

    public  DatabaseReference getDates(){
        return databaseReference.child("historial_medico");
    }

}
