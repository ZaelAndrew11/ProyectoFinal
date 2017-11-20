package cl.aguzman.proyectofinal.views.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cl.aguzman.proyectofinal.R;

public class FirstFragment extends Fragment {

    OnVarChangedFromFragment mCallback;
    public FirstFragment() {
    }

    public static FirstFragment newInstance(){
        return new FirstFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.btn_plus);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onChangeVar();
            }
        });
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
