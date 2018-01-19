package cl.aguzman.proyectofinal.views.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.adapters.ListPetsAdapter;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.GetMedicalHistory;
import cl.aguzman.proyectofinal.interfaces.ValidateEmptyListsCallback;
import cl.aguzman.proyectofinal.presenters.ValidateEmptyLists;
import cl.aguzman.proyectofinal.views.pets.AddPetDialogFragment;
import cl.aguzman.proyectofinal.views.pets.MedicalHistoryPetActivity;

public class ListPetsFragment extends Fragment implements GetMedicalHistory, ValidateEmptyListsCallback{

    public static final String DIALOG = "CL.AGUZMAN.PROYECTOFINAL.DIALOG";
    private ListPetsAdapter adapter;
    private RecyclerView recyclerView;
    private TextView messageEmpty;
    private DatabaseReference reference;

    public ListPetsFragment() {
    }

    public static ListPetsFragment newInstance(){
        return new ListPetsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pets, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference = new Queries().getPetsnames().child(new CurrentUser().getCurrentUid());
        new ValidateEmptyLists(this).validate(reference);
        messageEmpty = (TextView) view.findViewById(R.id.messageEmptyTv);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.plusPetsBtn);
        recyclerView = (RecyclerView) view.findViewById(R.id.listPetsRv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ListPetsAdapter(this, new Queries().getPetsnames().child(new CurrentUser().getCurrentUid()));
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPetDialog();
            }
        });
    }

    private void showAddPetDialog(){
        FragmentManager fragmentManager = getFragmentManager();
        AddPetDialogFragment addPetDialogFragment = AddPetDialogFragment.newInstance();
        addPetDialogFragment.show(fragmentManager, DIALOG);
    }

    @Override
    public void getMedicalHistory(String namePet, String photoPetUrl, String key) {
        Intent intent = new Intent(getActivity(), MedicalHistoryPetActivity.class);
        intent.putExtra("name", namePet);
        intent.putExtra("url", photoPetUrl);
        intent.putExtra("key", key);
        startActivity(intent);
    }

    @Override
    public void emptyList() {
        messageEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void notEmptyList() {
        messageEmpty.setVisibility(View.GONE);
    }


}
