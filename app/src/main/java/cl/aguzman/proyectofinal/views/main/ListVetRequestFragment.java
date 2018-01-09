package cl.aguzman.proyectofinal.views.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cl.aguzman.proyectofinal.R;
import cl.aguzman.proyectofinal.adapters.ListVetRequestAdapter;
import cl.aguzman.proyectofinal.data.CurrentUser;
import cl.aguzman.proyectofinal.data.Queries;
import cl.aguzman.proyectofinal.interfaces.GetContentVetRequestCallback;
import cl.aguzman.proyectofinal.views.detail.DetailActivity;

public class ListVetRequestFragment extends Fragment implements GetContentVetRequestCallback{

    private ListVetRequestAdapter adapter;

    public ListVetRequestFragment() {
    }

    public static ListVetRequestFragment newInstance(){
        return new ListVetRequestFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listVetRequestRv);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setHasFixedSize(true);
        adapter = new ListVetRequestAdapter(this, new Queries().getVetRequested().child(new CurrentUser().getCurrentUid()), new Queries().getVetMin());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void getDetail(String uid, String key) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("key", key);
        startActivity(intent);
    }
}
