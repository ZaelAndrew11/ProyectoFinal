package cl.aguzman.proyectofinal.views.pets;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.ValidatePetCallback;
import cl.aguzman.proyectofinal.models.MedicalHistory;
import cl.aguzman.proyectofinal.presenters.ValidatePet;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class AddPetDialogFragment extends DialogFragment implements ValidatePetCallback {
    private ImageView photoPet;
    private EditText namePet;
    private Button addPetBtn;

    private String path;
    private MagicalPermissions magicalPermissions;
    private MagicalCamera magicalCamera;
    private int PHOTO_SIZE = 30;

    private DatabaseReference refPets;
    private String uid;
    private String petName;

    ProgressDialog progressDialog;
    Dialog dialog;

    public AddPetDialogFragment() {
    }

    public static AddPetDialogFragment newInstance() {
        return new AddPetDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_pet, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());

        photoPet = (ImageView) view.findViewById(R.id.petIv);
        namePet = (EditText) view.findViewById(R.id.petNameTv);
        addPetBtn = (Button) view.findViewById(R.id.addPetBtn);

        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        magicalPermissions = new MagicalPermissions(getActivity(), permissions);
        magicalCamera = new MagicalCamera(getActivity(), PHOTO_SIZE, magicalPermissions);

        photoPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magicalCamera.takeFragmentPhoto(AddPetDialogFragment.this);
            }
        });

        addPetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = getDialog();
                petName = namePet.getText().toString().trim().toLowerCase();
                new ValidatePet(AddPetDialogFragment.newInstance()).validateUploadPet(petName, path, dialog, progressDialog);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        magicalPermissions.permissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        magicalCamera.resultPhoto(requestCode, resultCode, data);
        Log.d("CODE", String.valueOf(requestCode));
        Log.d("CODE", String.valueOf(RESULT_OK));
        if (RESULT_OK == resultCode) {
            Bitmap photo = magicalCamera.getPhoto();
            path = magicalCamera.savePhotoInMemoryDevice(photo, "avatar_pet", "Pets", MagicalCamera.JPEG, true);
            path = "file://" + path;
            Picasso.with(photoPet.getContext()).load(path).centerCrop().fit().into(photoPet);
        } else {

        }
    }




    @Override
    public void successPet(String path, final String namePet, final Dialog dialog, final ProgressDialog progressDialog) {
        progressDialog.show();
        uid = new CurrentUser().getCurrentUid();
        String url = "gs://proyectoandroid-c8ac3.appspot.com/mascotas/" + uid + "/" + namePet + ".jpg";
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(url);
        refPets = new Queries().getPetsnames();
        storageReference.putFile(Uri.parse(path)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") String url = taskSnapshot.getDownloadUrl().toString();
                url = url.split("&token=")[0];

                String key = refPets.child(uid).push().getKey();

                MedicalHistory medicalHistory = new MedicalHistory();
                medicalHistory.setNamePet(namePet);
                medicalHistory.setPhotoPet(url);
                medicalHistory.setKey(key);

                refPets.child(uid).child(key).setValue(medicalHistory).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void failedPet(int message) {
        String msj = getApplicationContext().getResources().getString(message);
        Toast.makeText(getApplicationContext(), msj, Toast.LENGTH_SHORT).show();
    }
}
