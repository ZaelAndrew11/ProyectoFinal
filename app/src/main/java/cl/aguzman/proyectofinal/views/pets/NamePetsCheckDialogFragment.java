package cl.aguzman.proyectofinal.views.pets;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.adapters.ListPetsAdapterCheck;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.FcmToken;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.CallVetCallback;
import cl.aguzman.proyectofinal.interfaces.ValidateNotificationSend;
import cl.aguzman.proyectofinal.notifications.SendNotification;

public class NamePetsCheckDialogFragment extends DialogFragment implements CallVetCallback, ValidateNotificationSend {
    private RecyclerView recyclerView;
    private Button button;
    private ListPetsAdapterCheck adapterCheck;
    private Dialog dialog;
    private String tokenVet, phone, address, commune, city, key;
    private ArrayList infoArr;
    private ProgressDialog progressDialog;

    public NamePetsCheckDialogFragment() {
    }

    public static NamePetsCheckDialogFragment newInstance(String tokenVet, String phone, String adreess, String commune, String city, String key) {
        NamePetsCheckDialogFragment petsCheckDialogFragment = new NamePetsCheckDialogFragment();
        Bundle args = new Bundle();
        args.putString("tokenVet", tokenVet);
        args.putString("phone", phone);
        args.putString("address", adreess);
        args.putString("commune", commune);
        args.putString("city", city);
        args.putString("key", key);
        petsCheckDialogFragment.setArguments(args);
        return petsCheckDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        tokenVet = getArguments().getString("tokenVet");
        phone = getArguments().getString("phone");
        address = getArguments().getString("address");
        commune = getArguments().getString("commune");
        city = getArguments().getString("city");
        key = getArguments().getString("key");
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
                String tokenUser =  new FcmToken(getActivity()).get();
                new SendNotification(NamePetsCheckDialogFragment.this).sendNotification(tokenVet, getString(R.string.vet_request_text), getString(R.string.vet_request_second_text), phone, address, commune, city, infoArr, progressDialog, tokenUser);
            }
        });
    }

    @Override
    public void sendOk() {
        DatabaseReference ref = new Queries().getVetRequested().child(new CurrentUser().getCurrentUid()).child(key);
        ref.setValue(true);
        getDialog().dismiss();
        alertDialogShow(R.string.notification_ok);
    }

    @Override
    public void sendFail() {
        getDialog().dismiss();
        alertDialogShow(R.string.notificartion_fail);
    }

    @Override
    public void infoPets(ArrayList list) {
        infoArr = list;
        if (infoArr.size() > 0) {
            button.setEnabled(true);
            button.setBackgroundResource(R.drawable.button_style);
        } else {
            button.setEnabled(false);
            button.setBackgroundResource(R.drawable.button_style_disabled);
        }
    }

    public void alertDialogShow(final int message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialogShow = builder.create();
                dialogShow.show();
            }
        });
    }

}
