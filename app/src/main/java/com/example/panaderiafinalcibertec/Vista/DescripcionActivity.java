package com.example.panaderiafinalcibertec.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.panaderiafinalcibertec.R;
import com.example.panaderiafinalcibertec.Vista.Fragments.CafeteriaPackage.CafeteriaFragment;
import com.example.panaderiafinalcibertec.Vista.Fragments.InicioPackage.InicioFragment;
import com.example.panaderiafinalcibertec.Vista.Fragments.PanaderiaPackage.PanaderiaFragment;
import com.example.panaderiafinalcibertec.Vista.Fragments.PasteleriaPackage.PasteleriaFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class DescripcionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InicioFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_inicio:
                            selectedFragment = new InicioFragment();
                            break;
                        case R.id.nav_panaderia:
                            selectedFragment = new PanaderiaFragment();
                            break;
                        case R.id.nav_pasteleria:
                            selectedFragment = new PasteleriaFragment();
                            break;
                        case R.id.nav_cafeteria:
                            selectedFragment = new CafeteriaFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
}
