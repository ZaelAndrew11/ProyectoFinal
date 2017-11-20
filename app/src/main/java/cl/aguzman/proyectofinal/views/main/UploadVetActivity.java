package cl.aguzman.proyectofinal.views.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.ValidateUploadVetCallback;
import cl.aguzman.proyectofinal.models.Vet;
import cl.aguzman.proyectofinal.presenters.ValidateUploadVet;


public class UploadVetActivity extends AppCompatActivity implements ValidateUploadVetCallback{
    private static final int PICK_IMAGE = 666;
    private ProgressDialog progressDialog;
    private DatabaseReference reference = new Queries().root();

    ImageView imgLogo;
    EditText nameVet;
    EditText emailVet;
    EditText addressVet;
    EditText phoneVet;
    EditText descriptionVet;

    String urlImage;
    String urlImageDownload;
    String key;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_vet);
        imgLogo = (ImageView) findViewById(R.id.logoVetIv);
        nameVet = (EditText) findViewById(R.id.nameVetEt);
        emailVet = (EditText) findViewById(R.id.emailVetEt);
        addressVet = (EditText) findViewById(R.id.addressVetEt);
        phoneVet = (EditText) findViewById(R.id.phoneVetEt);
        descriptionVet = (EditText) findViewById(R.id.descriptionVetEt);
        Button btnUpload = (Button) findViewById(R.id.uploadDataVetbtn);

        imgLogo.setOnClickListener(new View.OnClickListener() {
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
                String emailCount = emailVet.getText().toString().trim();
                String addressCount = addressVet.getText().toString().trim();
                String phoneCount = phoneVet.getText().toString().trim();
                String descriptionCount = descriptionVet.getText().toString().trim();

                new ValidateUploadVet(UploadVetActivity.this).validateupload(nameCount, emailCount, addressCount, phoneCount, descriptionCount, urlImage);
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

    public void uploadImage(String path) {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        String url = "gs://proyectoandroid-c8ac3.appspot.com/veterinarios/" + uid + "/vet-" + ts + ".jpg";
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(url);

        storageReference.putFile(Uri.parse(path)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") String url = taskSnapshot.getDownloadUrl().toString();
                url = url.split("&token=")[0];
                urlImageDownload = url;

                Map<String, Object > map = new HashMap<String, Object>();

                Vet vet = new Vet();
                Vet vet_min = new Vet();

                vet_min.setName(nameVet.getText().toString());
                vet_min.setUid(uid);
                vet_min.setKey(key);
                vet_min.setScore(0);
                vet_min.setImage(urlImageDownload);

                vet.setName(nameVet.getText().toString());
                vet.setEmail(emailVet.getText().toString());
                vet.setAddress(addressVet.getText().toString());
                vet.setPhone(phoneVet.getText().toString());
                vet.setDescription(descriptionVet.getText().toString());
                vet.setScore(0);
                vet.setImage(urlImageDownload);

                map.put("veterinarios/"+uid+"/"+key, vet);
                map.put("veterinarios_min/"+uid+"/"+key, vet_min);
                reference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(UploadVetActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadVetActivity.this, R.string.fail_upload, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void success() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        uploadImage(urlImage);
    }

    @Override
    public void emailErr(int error) {
        String err = this.getResources().getString(error);
        emailVet.setError(err);
    }

    @Override
    public void failed(int error) {
        String err = this.getResources().getString(error);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }


}
