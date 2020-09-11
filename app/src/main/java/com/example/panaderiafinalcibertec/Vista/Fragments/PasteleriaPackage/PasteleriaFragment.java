package com.example.panaderiafinalcibertec.Vista.Fragments.PasteleriaPackage;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.panaderiafinalcibertec.Controlador.ViewHolderPasteleria;
import com.example.panaderiafinalcibertec.Modelo.Pasteleria;
import com.example.panaderiafinalcibertec.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasteleriaFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mRef;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pasteleria, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        Toolbar toolbar = view.findViewById(R.id.toolbar_main);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ImageButton recargar = view.findViewById(R.id.img_principal);
        recargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).finish();
                getActivity().overridePendingTransition(0, 0);
                startActivity(getActivity().getIntent());
                getActivity().overridePendingTransition(0, 0);
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mFirebaseDatabse = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabse.getReference("Pasteles");
        return view;
    }

    private void firebaseSearch(String searchText){

        String query = searchText.toLowerCase();

        Query firebaseSearchQuery = mRef.orderByChild("buscador").startAt(query).endAt(query+"\uf8ff");
        FirebaseRecyclerAdapter<Pasteleria, ViewHolderPasteleria> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pasteleria, ViewHolderPasteleria>(
                Pasteleria.class,
                R.layout.item_pasteleria,
                ViewHolderPasteleria.class,
                firebaseSearchQuery
        ) {
            @Override
            protected void populateViewHolder(ViewHolderPasteleria viewHolderPasteleria, Pasteleria model, int i) {
                viewHolderPasteleria.setDetails(Objects.requireNonNull(getActivity())
                                .getApplicationContext(), model.getTitulo(), model.getDescripcion(), model.getImagen()
                        , model.getPrecio(), model.getMasdescripcion());
            }

            @Override
            public ViewHolderPasteleria onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolderPasteleria viewHolderPasteleria = super.onCreateViewHolder(parent, viewType);
                viewHolderPasteleria.setOnClickListener(new ViewHolderPasteleria.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String mTittle = getItem(position).getTitulo();
                        String mDesc = getItem(position).getDescripcion();
                        String mImage = getItem(position).getImagen();
                        String mPric = getItem(position).getPrecio();
                        String mMDescp = getItem(position).getMasdescripcion();

                        Intent intent = new Intent(view.getContext(), PasteleriaPosteriorActivity.class);
                        intent.putExtra("titulo", mTittle);
                        intent.putExtra("descripcion", mDesc);
                        intent.putExtra("masdescripcion", mMDescp);
                        intent.putExtra("precio", mPric);
                        intent.putExtra("imagen", mImage);

                        startActivity(intent);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return viewHolderPasteleria;
            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnline()){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            FirebaseRecyclerAdapter<Pasteleria, ViewHolderPasteleria> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pasteleria, ViewHolderPasteleria>(
                    Pasteleria.class,
                    R.layout.item_pasteleria,
                    ViewHolderPasteleria.class,
                    mRef
            ) {
                @Override
                protected void populateViewHolder(ViewHolderPasteleria viewHolderPasteleria, Pasteleria model, int i) {
                    viewHolderPasteleria.setDetails(Objects.requireNonNull(getActivity())
                                    .getApplicationContext(), model.getTitulo(), model.getDescripcion(), model.getImagen()
                            , model.getPrecio(), model.getMasdescripcion());
                    progressDialog.dismiss();
                }

                @Override
                public ViewHolderPasteleria onCreateViewHolder(ViewGroup parent, int viewType) {
                    ViewHolderPasteleria viewHolderPasteleria = super.onCreateViewHolder(parent, viewType);
                    viewHolderPasteleria.setOnClickListener(new ViewHolderPasteleria.ClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            String mTittle = getItem(position).getTitulo();
                            String mDesc = getItem(position).getDescripcion();
                            String mImage = getItem(position).getImagen();
                            String mPric = getItem(position).getPrecio();
                            String mMDescp = getItem(position).getMasdescripcion();

                            //Pasar la informacion al nuevo activity con un intent
                            Intent intent = new Intent(view.getContext(), PasteleriaPosteriorActivity.class);
                            intent.putExtra("titulo", mTittle);
                            intent.putExtra("descripcion", mDesc);
                            intent.putExtra("masdescripcion", mMDescp);
                            intent.putExtra("precio", mPric);
                            intent.putExtra("imagen", mImage);

                            startActivity(intent);

                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });

                    return viewHolderPasteleria;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.menu_navigation,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();

        if (id == R.id.action_search){

            return true;
        }
        return super.onOptionsItemSelected(item);


    }



}
