package cl.aguzman.proyectofinal.presenters;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.ValidateUploadVetCallback;

public class ValidateUploadImageVet {
    private ValidateUploadVetCallback callback;
    private DatabaseReference reference = new Queries().root();
    private String uid;

    public ValidateUploadImageVet(ValidateUploadVetCallback callback) {
        this.callback = callback;
    }

    public void uploadImage(String path) {
        uid = new CurrentUser().getCurrentUid();
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

                Map<String, Object > map = new HashMap<String, Object>();

                callback.successImage(map, url);

                reference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.successUpload();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.faildUpload();
                    }
                });
            }
        });
    }
}
