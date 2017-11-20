package cl.aguzman.proyectofinal.views.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cl.aguzman.proyectofinal.R;

public class SecondFragment extends Fragment {


    public SecondFragment() {
    }

    public static SecondFragment newInstance(){
        return new SecondFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

}
