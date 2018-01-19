package cl.aguzman.proyectofinal.views.main;

import android.app.Activity;
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

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.app_name);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
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
                getSupportActionBar().setTitle(R.string.app_name);
                return true;
            case R.id.navigation_pets:
                replaceFragment(ListPetsFragment.newInstance());
                getSupportActionBar().setTitle(R.string.your_pets);
                return true;
            case R.id.navigation_request_vet:
                replaceFragment(ListVetRequestFragment.newInstance());
                getSupportActionBar().setTitle(R.string.vet_request);
                return true;
            case R.id.navigation_profile:
                replaceFragment(ProfileFragment.newInstance());
                getSupportActionBar().setTitle(R.string.your_profile);
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


    @Override
    public void onBackPressed() {
        int seletedItemId = navigation.getSelectedItemId();
        if (R.id.navigation_home != seletedItemId) {
            setHomeItem(MainActivity.this);
        } else {
            super.onBackPressed();
        }
    }

    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) activity.findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

}
