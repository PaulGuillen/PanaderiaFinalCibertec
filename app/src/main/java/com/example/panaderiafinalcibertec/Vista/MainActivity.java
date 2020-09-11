package com.example.panaderiafinalcibertec.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.panaderiafinalcibertec.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void IngresarUsuario(View view) {
        Intent i = new Intent(MainActivity.this,DescripcionActivity.class);
        startActivity(i);
    }
}
