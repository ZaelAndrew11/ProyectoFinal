package cl.aguzman.proyectofinal.views.pets;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.adapters.ListMedicalHistoryAdapter;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.MedicalHistoryCallback;

public class MedicalHistoryPetActivity extends AppCompatActivity implements MedicalHistoryCallback{

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
        TextView medicalHistoryTitle = (TextView) findViewById(R.id.medicalHistoryTitleTv);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listMedicalHistoryRv);
        setSupportActionBar(toolbar);

        medicalHistoryTitle.setText(getString(R.string.medical_history_title) + " " + name);
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
        adapter = new ListMedicalHistoryAdapter(this, new Queries().getDates().child(new CurrentUser().getCurrentUid()).child(key));
        recyclerView.setAdapter(adapter);

    }


    private void showAddPetDialog(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddMedicalHistoryDateDialogFragment addMedicalHistoryDateDialogFragment = AddMedicalHistoryDateDialogFragment.newInstance(key);
        addMedicalHistoryDateDialogFragment.show(fragmentManager, DIALOG_MH);
    }

    @Override
    public void removeRegister(final String tag, final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.remove_register);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference refRegister = new Queries().getDates().child(new CurrentUser().getCurrentUid()).child(key).child(tag);
                refRegister.removeValue();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
