package cl.aguzman.proyectofinal.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CurrentUser {
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }
    public String email(){
        return getCurrentUser().getEmail();
    }
    public String getCurrentUid(){
        return getCurrentUser().getUid();
    }

    public String getImageUser(){
        return getCurrentUser().getPhotoUrl().toString();
    }

    public String getNameUser(){
        return getCurrentUser().getDisplayName();
    }

    public String getPhotoUser(){
        return  getCurrentUser().getPhotoUrl().toString();
    }
}
