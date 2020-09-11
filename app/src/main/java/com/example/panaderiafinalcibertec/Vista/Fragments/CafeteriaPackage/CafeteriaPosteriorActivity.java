package com.example.panaderiafinalcibertec.Vista.Fragments.CafeteriaPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.panaderiafinalcibertec.R;
import com.squareup.picasso.Picasso;

public class CafeteriaPosteriorActivity extends AppCompatActivity {

    TextView mTitleTv,mDetailTv,mPriceTv,mStateTv;
    ImageView mImageTv;
    String Url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafeteria_posterior);


        Url = "https://www.recetasdesbieta.com/pan-frances-casero-receta-autentica/";

        mTitleTv = findViewById(R.id.titleTv);
        mDetailTv = findViewById(R.id.descriptionTv);
        mImageTv = findViewById(R.id.imageView);
        mPriceTv = findViewById(R.id.priceTv);
        mStateTv = findViewById(R.id.stateTv);

        String image = getIntent().getStringExtra("imagen");
        String tittle = getIntent().getStringExtra("titulo");
        String desc = getIntent().getStringExtra("descripcion");
        String pric = getIntent().getStringExtra("precio");
        String esta = getIntent().getStringExtra("estado");


        mTitleTv.setText(tittle);
        mDetailTv.setText(desc);
        Picasso.get().load(image).placeholder(R.drawable.ic_charging)
                .error(R.drawable.ic_charging).into(mImageTv);
        mPriceTv.setText(pric);
        mStateTv.setText(esta);
    }
}
