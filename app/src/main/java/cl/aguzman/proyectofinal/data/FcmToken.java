package cl.aguzman.proyectofinal.data;

import android.content.Context;
import android.content.SharedPreferences;

public class FcmToken {
    private static final String FCM = "cl.aguzman.proyectofinal.FCM";
    private static final String FCM_TOKEN = "cl.aguzman.proyectofinal.FCM_TOKEN";
    private Context context;

    public FcmToken(Context context) {
        this.context = context;
    }

    public void save(String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FCM, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putString(FCM_TOKEN, token);
        prefEditor.apply();
    }

    public String get() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FCM, Context.MODE_PRIVATE);
        return sharedPreferences.getString(FCM_TOKEN, null);
    }
}
