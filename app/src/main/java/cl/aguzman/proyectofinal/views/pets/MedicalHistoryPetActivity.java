package cl.aguzman.proyectofinal.views.pets;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.adapters.ListMedicalHistoryAdapter;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;

public class MedicalHistoryPetActivity extends AppCompatActivity {

    public static final String DIALOG_MH = "CL.AGUZMAN.PROYECTOFINAL.DIALOG_MH";
    private String key;
    ListMedicalHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history_pet);

        String name = getIntent().getStringExtra("name").toUpperCase();
        String uriPhoto = getIntent().getStringExtra("url");
        key = getIntent().getStringExtra("key");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.plusDatesBtn);
        ImageView imagePet = (ImageView) findViewById(R.id.imagePetIv);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listMedicalHistoryRv);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout.setTitle(name);
        Picasso.with(this).load(uriPhoto).into(imagePet);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddPetDialog();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ListMedicalHistoryAdapter(new Queries().getDates().child(new CurrentUser().getCurrentUid()).child(key));
        recyclerView.setAdapter(adapter);

    }


    private void showAddPetDialog(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddMedicalHistoryDateDialogFragment addMedicalHistoryDateDialogFragment = AddMedicalHistoryDateDialogFragment.newInstance(key);
        addMedicalHistoryDateDialogFragment.show(fragmentManager, DIALOG_MH);
    }
}
