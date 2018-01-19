package cl.aguzman.proyectofinal.views.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.Map;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.ValidateUploadVetCallback;
import cl.aguzman.proyectofinal.models.Vet;
import cl.aguzman.proyectofinal.presenters.ValidateUploadImageVet;
import cl.aguzman.proyectofinal.presenters.ValidateUploadVet;


public class UploadVetActivity extends AppCompatActivity implements ValidateUploadVetCallback {
    private static final int PICK_IMAGE = 666;
    private ProgressDialog progressDialog;
    private DatabaseReference reference = new Queries().root();

    private CircularImageView imgLogo;
    private Button camPlusBtn;
    private EditText nameVet;
    private EditText rutVet;
    private EditText emailVet;
    private EditText addressVet;
    private EditText communeVet;
    private EditText cityVet;
    private EditText phoneVet;
    private EditText descriptionVet;

    private String urlImage = "";
    private String key;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_vet);
        getSupportActionBar().setTitle(R.string.new_vet_title);
        camPlusBtn = (Button) findViewById(R.id.camPlusBtn);
        imgLogo = (CircularImageView) findViewById(R.id.logoVetIv);
        nameVet = (EditText) findViewById(R.id.nameVetEt);
        rutVet = (EditText) findViewById(R.id.rutVetEt);
        emailVet = (EditText) findViewById(R.id.emailVetEt);
        addressVet = (EditText) findViewById(R.id.addressVetEt);
        communeVet = (EditText) findViewById(R.id.communeVetEt);
        cityVet = (EditText) findViewById(R.id.cityVetEt);
        phoneVet = (EditText) findViewById(R.id.phoneVetEt);
        descriptionVet = (EditText) findViewById(R.id.descriptionVetEt);
        Button btnUpload = (Button) findViewById(R.id.uploadDataVetbtn);

        camPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(intent.EXTRA_LOCAL_ONLY, false);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uid = new CurrentUser().getCurrentUid();
                key = reference.child("veterinarios").child(uid).push().getKey();

                String nameCount = nameVet.getText().toString().trim();
                String rutCount = rutVet.getText().toString().trim();
                String emailCount = emailVet.getText().toString().trim();
                String addressCount = addressVet.getText().toString().trim();
                String communeCount = communeVet.getText().toString().trim();
                String citeCount = cityVet.getText().toString().trim();
                String phoneCount = phoneVet.getText().toString().trim();

                new ValidateUploadVet(UploadVetActivity.this).validateupload(nameCount, emailCount, addressCount, phoneCount, rutCount, communeCount, citeCount);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri urlPickImage = data.getData();
            urlImage = data.getData().toString();
            Picasso.with(this).load(urlPickImage).into(imgLogo);
            imgLogo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }


    @Override
    public void success() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        new ValidateUploadImageVet(this).uploadImage(urlImage);
    }

    @Override
    public void failed(int error) {
        String err = this.getResources().getString(error);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void successUpload() {
        progressDialog.dismiss();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.alert_title);
        alertDialog.setMessage(R.string.alert);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(UploadVetActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    @Override
    public void faildUpload() {
        Toast.makeText(UploadVetActivity.this, R.string.fail_upload, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successImage(Map<String, Object> map, String url) {
        Vet vet = new Vet();
        Vet vet_min = new Vet();

        vet_min.setName(nameVet.getText().toString().toLowerCase());
        vet_min.setPublish(true);
        vet_min.setUid(uid);
        vet_min.setKey(key);
        vet_min.setScore(0);
        vet_min.setImage(url);

        vet.setName(nameVet.getText().toString());
        vet.setRut(rutVet.getText().toString());
        vet.setEmail(emailVet.getText().toString());
        vet.setAddress(addressVet.getText().toString());
        vet.setCommune(communeVet.getText().toString());
        vet.setCity(cityVet.getText().toString());
        vet.setPhone(phoneVet.getText().toString());
        vet.setDescription(descriptionVet.getText().toString());
        vet.setScore(0);
        vet.setImage(url);
        vet.setKey(key);

        map.put("veterinarios/" + uid + "/" + key, vet);
        map.put("veterinarios_min/" + key, vet_min);
    }
}
