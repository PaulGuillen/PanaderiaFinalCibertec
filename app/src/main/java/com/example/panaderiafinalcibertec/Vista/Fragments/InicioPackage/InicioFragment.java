package com.example.panaderiafinalcibertec.Vista.Fragments.InicioPackage;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.panaderiafinalcibertec.Controlador.ViewHolderNoticias;
import com.example.panaderiafinalcibertec.Modelo.Noticia;
import com.example.panaderiafinalcibertec.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class InicioFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        ImageButton recargar = view.findViewById(R.id.img_principal);
        recargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).recreate();
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabse.getReference("Noticia");

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnline()){
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                FirebaseRecyclerAdapter<Noticia, ViewHolderNoticias> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<Noticia, ViewHolderNoticias>(
                                Noticia.class,
                                R.layout.item_noticias,
                                ViewHolderNoticias.class,
                                mRef
                        ) {
                            @Override
                            protected void populateViewHolder(ViewHolderNoticias viewHolderNoticias
                                    , Noticia model, int position) {
                                viewHolderNoticias.setDetails(Objects.requireNonNull(getActivity())
                                        .getApplicationContext(), model.getTitulo(), model.getDescripcion(), model.getFecha());
                                progressDialog.dismiss();
                            }
                        };
                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        }else{
            try {
                final AlertDialog alertDialog= new AlertDialog.Builder(getActivity()).create();

                alertDialog.setTitle("Información");
                alertDialog.setMessage("Parece que no estas conectado a internet - no podemos encontrar " +
                        " ninguna red");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

                alertDialog.show();
            } catch (Exception e) {
                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(getActivity(), "Sin conexion a internet!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
