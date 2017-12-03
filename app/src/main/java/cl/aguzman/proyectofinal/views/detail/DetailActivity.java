package cl.aguzman.proyectofinal.views.detail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.models.User;
import cl.aguzman.proyectofinal.models.Vet;
import cl.aguzman.proyectofinal.notifications.SendNotification;
import cl.aguzman.proyectofinal.presenters.ValidateDetail;

public class DetailActivity extends AppCompatActivity {

    String tel;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final String uid = getIntent().getStringExtra("uid");
        final String key = getIntent().getStringExtra("key");

        final ImageView detailImgVet = (ImageView) findViewById(R.id.detailIv);
        final TextView detailScoreTv = (TextView) findViewById(R.id.detailScoreTv);
        final TextView detailNameTv = (TextView) findViewById(R.id.detailNameTv);
        final TextView detailAdressTv = (TextView) findViewById(R.id.detailAdressTv);
        final TextView detailEmailTv = (TextView) findViewById(R.id.detailEmailTv);
        final TextView detailPhoneTv = (TextView) findViewById(R.id.detailPhoneTv);
        final TextView detailDescriptionTv = (TextView) findViewById(R.id.detailDescriptionTv);
        ImageButton scoreBtn = (ImageButton) findViewById(R.id.likeIb);
        Button detailCallBtn = (Button) findViewById(R.id.callBtn);
        Button detailSolicitBtn = (Button) findViewById(R.id.solicitBtn);

        DatabaseReference ref = new Queries().getVet();
        ref.child(uid).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Vet vet = dataSnapshot.getValue(Vet.class);
                tel = vet.getPhone();
                Picasso.with(detailImgVet.getContext()).load(vet.getImage()).into(detailImgVet);
                detailScoreTv.setText(String.valueOf(vet.getScore()));
                detailNameTv.setText(vet.getName());
                detailAdressTv.setText(vet.getAddress());
                detailEmailTv.setText(vet.getEmail());
                detailPhoneTv.setText(vet.getPhone());
                detailDescriptionTv.setText(vet.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        detailSolicitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = new Queries().getUser().child(uid);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        token = user.getToken();
                        new SendNotification().sendNotification(token, "Solicitud de veterinario", "Por favor necesito un veterinario para mi perrito");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });

        detailCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tel, null));
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
                new ValidateDetail().run(likeRef);
            }
        });

    }
}
