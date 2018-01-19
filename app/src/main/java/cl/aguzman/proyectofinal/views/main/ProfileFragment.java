package cl.aguzman.proyectofinal.views.main;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.FcmToken;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.ValidateRegisterCallback;
import cl.aguzman.proyectofinal.models.User;
import cl.aguzman.proyectofinal.presenters.ValidateEndRegister;
import cl.aguzman.proyectofinal.views.login.LoginActivity;

public class ProfileFragment extends Fragment implements ValidateRegisterCallback {

    private DatabaseReference userRef;
    private EditText nameProfile, addressProfile, emailProfile, phoneProfile, communeProfile, cityProfile;
    private ProgressDialog progressDialog;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        final CircularImageView imageProfile = (CircularImageView) view.findViewById(R.id.photoProfileIv);
        nameProfile = (EditText) view.findViewById(R.id.userNameProfileEt);
        addressProfile = (EditText) view.findViewById(R.id.userAddressProfileEt);
        communeProfile = (EditText) view.findViewById(R.id.userCommuneProfileEt);
        cityProfile = (EditText) view.findViewById(R.id.userCityProfileEt);
        emailProfile = (EditText) view.findViewById(R.id.userEmailProfileEt);
        phoneProfile = (EditText) view.findViewById(R.id.userPhoneProfileEt);
        Button updateBtn = (Button) view.findViewById(R.id.editProfileBtn);
        TextView delete = (TextView) view.findViewById(R.id.deleteUser);

        userRef = new Queries().getUser().child(new CurrentUser().getCurrentUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (TextUtils.isEmpty(new CurrentUser().getImageUser())) {
                    Picasso.with(imageProfile.getContext()).load(R.mipmap.ic_person_black_24dp).into(imageProfile);
                } else {
                    Picasso.with(imageProfile.getContext()).load(new CurrentUser().getImageUser()).into(imageProfile);
                }

                nameProfile.setText(user.getName());
                addressProfile.setText(user.getAdress());
                communeProfile.setText(user.getCommune());
                cityProfile.setText(user.getCity());
                emailProfile.setText(user.getEmail());
                phoneProfile.setText(user.getPhone());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                progressDialog.setMessage(getString(R.string.loading));
                String name = nameProfile.getText().toString();
                String email = emailProfile.getText().toString();
                String phone = phoneProfile.getText().toString();
                String address = addressProfile.getText().toString();
                String commune = communeProfile.getText().toString();
                String city = cityProfile.getText().toString();
                new ValidateEndRegister(ProfileFragment.this).validateRegister(name, email, phone, address, commune, city);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.message_delete);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setMessage(getString(R.string.loading));
                        progressDialog.show();
                        DatabaseReference myInfo = new Queries().getUser().child(new CurrentUser().getCurrentUid());
                        DatabaseReference userDelete = new Queries().getUserdelete().child(new CurrentUser().getCurrentUid());
                        DatabaseReference vets = new Queries().getVet().child(new CurrentUser().getCurrentUid());
                        final DatabaseReference vetrefRequest = new Queries().getVetRequested().child(new CurrentUser().getCurrentUid());
                        User user = new User();
                        user.setName(nameProfile.getText().toString());
                        user.setEmail(emailProfile.getText().toString());
                        user.setPhone(phoneProfile.getText().toString());
                        user.setAdress(addressProfile.getText().toString());
                        userDelete.setValue(user);
                        myInfo.removeValue();

                        vetrefRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null){
                                    vetrefRequest.removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });

                        vets.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    String userVetKey = "";
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        userVetKey = ds.getKey();
                                    }
                                    DatabaseReference vetref = new Queries().getVetMin().child(userVetKey);
                                    vetref.removeValue();
                                }
                                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                currentUser.delete();
                                AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                        getActivity().finishAffinity();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public void failed(int error) {
        progressDialog.dismiss();
        String err = getResources().getString(error);
        Toast.makeText(getActivity(), err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success() {
        User userUpdate = new User();
        userUpdate.setName(nameProfile.getText().toString());
        userUpdate.setEmail(emailProfile.getText().toString());
        userUpdate.setPhone(phoneProfile.getText().toString());
        userUpdate.setAdress(addressProfile.getText().toString());
        userUpdate.setCommune(communeProfile.getText().toString());
        userUpdate.setCity(cityProfile.getText().toString());
        userUpdate.setToken(new FcmToken(getActivity()).get());
        userRef.setValue(userUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Información Actualizada.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void successOnload() {
    }

    @Override
    public void defaultOnload() {
    }
}
