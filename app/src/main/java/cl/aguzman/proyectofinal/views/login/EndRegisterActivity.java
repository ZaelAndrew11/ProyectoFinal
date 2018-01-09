package cl.aguzman.proyectofinal.views.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.FcmToken;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.ValidateRegisterCallback;
import cl.aguzman.proyectofinal.models.User;
import cl.aguzman.proyectofinal.presenters.ValidateEndRegister;
import cl.aguzman.proyectofinal.views.main.MainActivity;

public class EndRegisterActivity extends AppCompatActivity implements ValidateRegisterCallback {

    private EditText name, email, phone, adress;
    private ImageView imageProfile;
    private Button button;
    private ProgressDialog dialog;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_register);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();
        new ValidateEndRegister(this).validateRegisterOnload();
        imageProfile = (ImageView) findViewById(R.id.profileImageIv);
        name = (EditText) findViewById(R.id.nameProfileEt);
        email = (EditText) findViewById(R.id.emailProfileEt);
        phone = (EditText) findViewById(R.id.phoneProfileEt);
        adress = (EditText) findViewById(R.id.adressProfileEt);
        button = (Button) findViewById(R.id.sendProfileBtn);

        ref = new Queries().getUser().child(new CurrentUser().getCurrentUid());
        String imgUrl = new CurrentUser().getImageUser();
        String nameUser = new CurrentUser().getNameUser();
        String emailUser = new CurrentUser().email();
        if (imgUrl != null) {
            Picasso.with(imageProfile.getContext()).load(imgUrl).into(imageProfile);
        } else {

        }
        name.setText(nameUser);
        email.setText(emailUser);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = name.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String phoneText = phone.getText().toString().trim();
                String adressText = adress.getText().toString().trim();
                new ValidateEndRegister(EndRegisterActivity.this).validateRegister(nameText, emailText, phoneText, adressText);
            }
        });
    }

    @Override
    public void success() {
        User userRegister = new User();
        userRegister.setName(name.getText().toString());
        userRegister.setEmail(email.getText().toString());
        userRegister.setPhone(phone.getText().toString());
        userRegister.setAdress(adress.getText().toString());
        userRegister.setToken(new FcmToken(EndRegisterActivity.this).get());
        ref.setValue(userRegister);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void successOnload() {
        ref.child("token").setValue(new FcmToken(EndRegisterActivity.this).get());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void defaultOnload() {
        dialog.dismiss();
    }

    @Override
    public void failed(int error) {
        Toast.makeText(this, this.getResources().getString(error), Toast.LENGTH_SHORT).show();
    }
}
