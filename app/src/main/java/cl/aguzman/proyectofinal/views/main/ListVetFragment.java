package cl.aguzman.proyectofinal.views.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.adapters.ListVetAdapter;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.GetContentCallback;
import cl.aguzman.proyectofinal.presenters.ValidateVetExist;
import cl.aguzman.proyectofinal.views.detail.DetailActivity;

public class ListVetFragment extends Fragment implements GetContentCallback{

    OnVarChangedFromFragment mCallback;
    private ListVetAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    public ListVetFragment() {
    }

    public static ListVetFragment newInstance(){
        return new ListVetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_vet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.btn_plus);
        EditText searchtext = (EditText) view.findViewById(R.id.searchEt);
        recyclerView = (RecyclerView) view.findViewById(R.id.listVetRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ListVetAdapter(this, new Queries().getVetMin().orderByChild("publish").equalTo(true));
        recyclerView.setAdapter(adapter);

        new ValidateVetExist(this).validateVet();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onChangeVar();
            }
        });

        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                int textCount = s.toString().trim().length();
                if(textCount == 0){
                    resetFilter();
                }else{
                    filter(s.toString());
                }
            }
        });
    }

    void filter(String text){
        adapter = new ListVetAdapter(this, new Queries().root().child("veterinarios_min").orderByChild("name").startAt(text));
        recyclerView.setAdapter(adapter);
        adapter.updateList();
    }

    void resetFilter(){
        adapter = new ListVetAdapter(this, new Queries().root().child("veterinarios_min"));
        recyclerView.setAdapter(adapter);
        adapter.updateList();
    }

    @Override
    public void getDetail(String uid, String key) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("key", key);
        startActivity(intent);
    }

    @Override
    public void vetExist() {
        floatingActionButton.setVisibility(View.GONE);
    }

    @Override
    public void VetNotExist() {
        floatingActionButton.setVisibility(View.VISIBLE);
    }


    // Tu Activity deberá implementar esta interfaz
    public interface OnVarChangedFromFragment {
        public void onChangeVar();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnVarChangedFromFragment) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " debes implementar OnVarChangedFromFragment");
        }
    }
}
