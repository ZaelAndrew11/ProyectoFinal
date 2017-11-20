package cl.aguzman.proyectofinal.views.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cl.aguzman.proyectofinal.R;

public class ThirdFragment extends Fragment {


    public ThirdFragment() {
    }

    public static ThirdFragment newInstance(){
        return new ThirdFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

}
