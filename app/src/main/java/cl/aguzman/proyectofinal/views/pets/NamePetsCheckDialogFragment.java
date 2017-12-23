package cl.aguzman.proyectofinal.views.pets;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.adapters.ListPetsAdapterCheck;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.CallVetCallback;
import cl.aguzman.proyectofinal.notifications.SendNotification;

public class NamePetsCheckDialogFragment extends DialogFragment implements CallVetCallback{
    private RecyclerView recyclerView;
    private Button button;
    private ListPetsAdapterCheck adapterCheck;
    private Dialog dialog;
    private String tokenVet;

    public NamePetsCheckDialogFragment() {
    }

    public static NamePetsCheckDialogFragment newInstance(String tokenVet) {
        NamePetsCheckDialogFragment petsCheckDialogFragment = new NamePetsCheckDialogFragment();
        Bundle args = new Bundle();
        args.putString("tokenVet", tokenVet);
        petsCheckDialogFragment.setArguments(args);
        return petsCheckDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenVet = getArguments().getString("tokenVet");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_name_pets_check, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.listNamePetsRv);
        button = (Button) view.findViewById(R.id.namePetsCheckBtn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        adapterCheck = new ListPetsAdapterCheck(this, new Queries().getPetsnames().child(new CurrentUser().getCurrentUid()));
        recyclerView.setAdapter(adapterCheck);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendNotification().sendNotification(tokenVet, "Solicitud de veterinario", "Por favor necesito un veterinario para mi perrito");
            }
        });
    }

    @Override
    public void infoPets(ArrayList list) {
        if (list.size() > 0){
            button.setEnabled(true);
        }else{
            button.setEnabled(false);
        }
    }
}
