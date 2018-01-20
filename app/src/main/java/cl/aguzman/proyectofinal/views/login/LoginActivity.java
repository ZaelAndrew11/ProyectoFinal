package cl.aguzman.proyectofinal.views.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;

import java.util.Arrays;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.interfaces.ValidateLoginCallback;
import cl.aguzman.proyectofinal.notifications.ResponseNotificationActivity;
import cl.aguzman.proyectofinal.presenters.ValidateLogin;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity implements ValidateLoginCallback {
    private static final int RC_SIGN_IN = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);


        if (getIntent().getStringExtra("userInfo") != null) {
            Intent intent = new Intent(this, ResponseNotificationActivity.class);
            intent.putExtra("userInfo", getIntent().getStringExtra("userInfo"));
            intent.putExtra("body", getIntent().getStringExtra("info"));
            intent.putExtra("token", getIntent().getStringExtra("tokenUser"));
            startActivity(intent);
            finish();
        }else {
            new ValidateLogin(this).loginValidate();
        }
    }

    @Override
    public void signUp() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build())
                        ).setIsSmartLockEnabled(false)
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.mipmap.logo)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RC_SIGN_IN == requestCode){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(ResultCodes.OK == resultCode){
                logged();
            }else{
                if(response == null){
                    message(R.string.login_cancel);
                    return;
                }
                if(response.getErrorCode() == ErrorCodes.NO_NETWORK){
                    message(R.string.login_not_network);
                    return;
                }
                if(response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR){
                    message(R.string.login_error_unknown);
                    return;
                }
            }
            //message(R.string.login_unknown_response);
        }
    }

    @Override
    public void logged() {
        Intent intent = new Intent(this, EndRegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void message(int alert){
        Toast.makeText(this, alert, Toast.LENGTH_SHORT).show();
    }
}
