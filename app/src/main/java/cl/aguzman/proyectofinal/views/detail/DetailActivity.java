package cl.aguzman.proyectofinal.views.detail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.DetailCallback;
import cl.aguzman.proyectofinal.models.User;
import cl.aguzman.proyectofinal.models.Vet;
import cl.aguzman.proyectofinal.presenters.ValidateDetail;
import cl.aguzman.proyectofinal.views.main.MainActivity;
import cl.aguzman.proyectofinal.views.pets.NamePetsCheckDialogFragment;

public class DetailActivity extends AppCompatActivity implements DetailCallback {

    public static final String DIALOG_NAMES_PETS_CHECK = "CL.AGUZMAN.PROYECTOFINAL.DIALOG_NAMES_PETS_CHECK";
    private String tel, phoneCall, address, commune, city, token, uid, key;
    private TextView textNotPets;
    private TextView detailScoreTv;
    private Button notPetBtn;
    private Button detailSolicitBtn;
    private ImageButton scoreBtn;
    private Button detailCallBtn;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DatabaseReference reference = new Queries().getUser().child(new CurrentUser().getCurrentUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tel = user.getPhone();
                address = user.getAdress();
                commune = user.getCommune();
                city = user.getCity();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        uid = getIntent().getStringExtra("uid");
        key = getIntent().getStringExtra("key");

        showDialogHide();

        textNotPets = (TextView) findViewById(R.id.textNotPetTv);
        notPetBtn = (Button) findViewById(R.id.notPetBtn);

        final ImageView detailImgVet = (ImageView) findViewById(R.id.detailIv);
        detailScoreTv = (TextView) findViewById(R.id.detailScoreTv);
        final TextView detailNameTv = (TextView) findViewById(R.id.detailNameTv);
        final TextView detailAdressTv = (TextView) findViewById(R.id.detailAdressTv);
        final TextView detailEmailTv = (TextView) findViewById(R.id.detailEmailTv);
        final TextView detailPhoneTv = (TextView) findViewById(R.id.detailPhoneTv);
        final TextView detailDescriptionTv = (TextView) findViewById(R.id.detailDescriptionTv);
        final TextView detailCommuneTv = (TextView) findViewById(R.id.detailCommuneTv);
        final TextView detailCityTv = (TextView) findViewById(R.id.detailCityTv);
        scoreBtn = (ImageButton) findViewById(R.id.likeIb);
        detailCallBtn = (Button) findViewById(R.id.callBtn);
        detailSolicitBtn = (Button) findViewById(R.id.solicitBtn);

        ref = new Queries().getVet();

        ref.child(uid).child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Vet vet = dataSnapshot.getValue(Vet.class);
                getSupportActionBar().setTitle(vet.getName());
                phoneCall = vet.getPhone();
                if (vet.getImage().equals("")){Picasso.with(detailImgVet.getContext()).load(R.mipmap.logo).into(detailImgVet);
                }else {Picasso.with(detailImgVet.getContext()).load(vet.getImage()).into(detailImgVet);}
                detailNameTv.setText(vet.getName());
                detailScoreTv.setText(String.valueOf(vet.getScore()));
                detailAdressTv.setText(vet.getAddress());
                detailCommuneTv.setText(vet.getCommune());
                detailCityTv.setText(vet.getCity());
                detailEmailTv.setText(vet.getEmail());
                detailPhoneTv.setText(vet.getPhone());
                detailDescriptionTv.setText(vet.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        detailSolicitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogModal();
            }
        });

        detailCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "9"+phoneCall, null));
                if (ActivityCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                    return;
                }
            }
        });

        scoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference likeRef = new Queries().getVet().child(uid).child(key);
                new ValidateDetail(DetailActivity.this).run(likeRef, key, scoreBtn.getTag().toString());
            }
        });

        notPetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showDialogHide() {
        DatabaseReference reference = new Queries().getUser().child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                token = user.getToken();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        NamePetsCheckDialogFragment checkFragment = NamePetsCheckDialogFragment.newInstance(token, tel, address, commune, city, key);
        checkFragment.show(fragmentManager, DIALOG_NAMES_PETS_CHECK);
        checkFragment.dismiss();
    }

    private void showDialogModal() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        NamePetsCheckDialogFragment checkFragment = NamePetsCheckDialogFragment.newInstance(token, tel, address, commune, city, key);
        checkFragment.show(fragmentManager, DIALOG_NAMES_PETS_CHECK);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ValidateDetail(this).verificationPets(uid);
        new ValidateDetail(this).validateLike(key);
    }

    @Override
    public void like() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreBtn.setImageResource(R.mipmap.ic_favorite_white_24dp);
                scoreBtn.setTag("dislike");
            }
        });
    }

    @Override
    public void disLike() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreBtn.setImageResource(R.mipmap.ic_favorite_border_white_24dp);
                scoreBtn.setTag("like");
            }
        });
    }

    @Override
    public void hasPet() {
        textNotPets.setVisibility(View.GONE);
        notPetBtn.setVisibility(View.GONE);
    }

    @Override
    public void notPet() {
        detailSolicitBtn.setVisibility(View.GONE);
    }

    @Override
    public void sameVet() {
        textNotPets.setVisibility(View.GONE);
        notPetBtn.setVisibility(View.GONE);
        detailSolicitBtn.setVisibility(View.GONE);
        detailCallBtn.setVisibility(View.GONE);
    }
}
