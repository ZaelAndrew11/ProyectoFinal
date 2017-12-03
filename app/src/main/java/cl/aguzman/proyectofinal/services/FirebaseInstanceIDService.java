package cl.aguzman.proyectofinal.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import cl.aguzman.proyectofinal.data.FcmToken;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        new FcmToken(this).save(token);
        Log.d("TOKEN", token);
    }
}
