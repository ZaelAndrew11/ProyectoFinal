package cl.aguzman.proyectofinal.views.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import cl.aguzman.proyectofinal.R;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ListVetFragment.OnVarChangedFromFragment{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        FragmentTransaction fragmentTransaction = sanitizer();
        fragmentTransaction.add(R.id.fragmentsContainer, ListVetFragment.newInstance());
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                replaceFragment(ListVetFragment.newInstance());
                return true;
            case R.id.navigation_dashboard:
                replaceFragment(ListPetsFragment.newInstance());
                return true;
            case R.id.navigation_notifications:
                replaceFragment(ListVetRequestFragment.newInstance());
                return true;
            case R.id.navigation_profile:
                replaceFragment(ProfileFragment.newInstance());
                return true;
        }
        return false;
    }

    private FragmentTransaction sanitizer(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentById(R.id.fragmentsContainer);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        /*if (animation) {
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        }*/
        return ft;
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = sanitizer();
        fragmentTransaction.add(R.id.fragmentsContainer, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onChangeVar() {
        Intent intent = new Intent(this, UploadVetActivity.class);
        startActivity(intent);
    }
}
