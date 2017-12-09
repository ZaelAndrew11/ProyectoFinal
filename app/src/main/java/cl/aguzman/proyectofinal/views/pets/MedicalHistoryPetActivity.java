package cl.aguzman.proyectofinal.views.pets;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import cl.aguzman.proyectofinal.R;

public class MedicalHistoryPetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history_pet);

        String name = getIntent().getStringExtra("name");
        String uriPhoto = getIntent().getStringExtra("url");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ImageView imagePet = (ImageView) findViewById(R.id.imagePetIv);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout.setTitle(name);
        Picasso.with(this).load(uriPhoto).into(imagePet);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
