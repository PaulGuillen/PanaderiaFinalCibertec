package com.example.panaderiafinalcibertec.Vista.Fragments.PanaderiaPackage;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.panaderiafinalcibertec.Controlador.ViewHolderPanaderia;
import com.example.panaderiafinalcibertec.Controlador.ViewHolderPasteleria;
import com.example.panaderiafinalcibertec.Modelo.Panaderia;
import com.example.panaderiafinalcibertec.Modelo.Pasteleria;
import com.example.panaderiafinalcibertec.R;
import com.example.panaderiafinalcibertec.Vista.Fragments.PasteleriaPackage.PasteleriaPosteriorActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PanaderiaFragment extends Fragment {


    private LinearLayoutManager mLayoutManager;
    private SharedPreferences mSharedPref;
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private ProgressDialog progressDialog;
    private DatabaseReference mRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_panaderia, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar_main);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mSharedPref = getActivity().getSharedPreferences("SortSettings", MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Ordenar", "actuales");

        if (mSorting.equals("actuales")) {
            mLayoutManager = new LinearLayoutManager(getContext());
            //esto cargara los items de nuevos al comienzo
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
        } else if (mSorting.equals("antiguos")) {
            mLayoutManager = new LinearLayoutManager(getContext());
            //esto cargara los items viejos al comienzo
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false);
        }

        //Enviar el layout como LinearLayout
        mRecyclerView.setLayoutManager(mLayoutManager);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Panaderia");

        return  view;
    }

    private void firebaseSearch(String searchText){

        String query = searchText.toLowerCase();

        Query firebaseSearchQuery = mRef.orderByChild("buscador").startAt(query).endAt(query+"\uf8ff");
        FirebaseRecyclerAdapter<Panaderia, ViewHolderPanaderia> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<Panaderia, ViewHolderPanaderia>(
                        Panaderia.class,
                        R.layout.item_panaderia,
                        ViewHolderPanaderia.class,
                        firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderPanaderia viewHolderPanaderia, Panaderia model, int i) {
                        viewHolderPanaderia.setDetails(getActivity().getApplicationContext(), model.getTitulo(), model.getImagen(),model.getPrecio(),
                                model.getDescripcion(),model.getEstado());
                        progressDialog.dismiss();
                    }

                    @Override
                    public ViewHolderPanaderia onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolderPanaderia viewHolderPanaderia = super.onCreateViewHolder(parent, viewType);
                        viewHolderPanaderia.setOnClickListener(new ViewHolderPanaderia.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                String mTittle = getItem(position).getTitulo();
                                String mImage = getItem(position).getImagen();
                                String mPric = getItem(position).getPrecio();
                                String mDesc = getItem(position).getDescripcion();
                                String mEst = getItem(position).getEstado();

                                //Pasar la informacion al nuevo activity con un intent
                                Intent intent = new Intent(view.getContext(), PanaderiaPosteriorActivity.class);
                                intent.putExtra("titulo", mTittle);
                                intent.putExtra("imagen", mImage);
                                intent.putExtra("precio", mPric);
                                intent.putExtra("descripcion", mDesc);
                                intent.putExtra("estado", mEst);

                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });
                        return viewHolderPanaderia;
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
            FirebaseRecyclerAdapter<Panaderia, ViewHolderPanaderia> firebaseRecyclerAdapter = new
                    FirebaseRecyclerAdapter<Panaderia, ViewHolderPanaderia>(
                            Panaderia.class,
                            R.layout.item_panaderia,
                            ViewHolderPanaderia.class,
                            mRef
                    ) {
                        @Override
                        protected void populateViewHolder(ViewHolderPanaderia viewHolderPanaderia, Panaderia model, int i) {
                            viewHolderPanaderia.setDetails(getActivity().getApplicationContext(), model.getTitulo(), model.getImagen(),model.getPrecio(),
                                    model.getDescripcion(),model.getEstado());
                            progressDialog.dismiss();
                        }

                        @Override
                        public ViewHolderPanaderia onCreateViewHolder(ViewGroup parent, int viewType) {
                            ViewHolderPanaderia viewHolderPanaderia = super.onCreateViewHolder(parent, viewType);
                            viewHolderPanaderia.setOnClickListener(new ViewHolderPanaderia.ClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                    String mTittle = getItem(position).getTitulo();
                                    String mImage = getItem(position).getImagen();
                                    String mPric = getItem(position).getPrecio();
                                    String mDesc = getItem(position).getDescripcion();
                                    String mEst = getItem(position).getEstado();

                                    //Pasar la informacion al nuevo activity con un intent
                                    Intent intent = new Intent(view.getContext(), PanaderiaPosteriorActivity.class);
                                    intent.putExtra("titulo", mTittle);
                                    intent.putExtra("imagen", mImage);
                                    intent.putExtra("precio", mPric);
                                    intent.putExtra("descripcion", mDesc);
                                    intent.putExtra("estado", mEst);

                                    startActivity(intent);
                                }

                                @Override
                                public void onItemLongClick(View view, int position) {

                                }
                            });
                            return viewHolderPanaderia;
                        }
                    };
            mRecyclerView.setLayoutManager(new GridLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext(),2));
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

        int id = item.getItemId();
        if (id == R.id.action_sort) {
            showSortDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);


    }

    private void showSortDialog() {
        //Las opciones que se veran en el dialog
        String[] sortOptions = {"Actuales", "Antiguos"};
        //Crear un dialogo de alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Ordenar por").setIcon(R.drawable.ic_sort).setItems(sortOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //ordenar por actuales
                    SharedPreferences.Editor editor = mSharedPref.edit();
                    editor.putString("Ordenar", "actuales");
                    editor.apply();//aplicar y guaardar el valor

                } else if (which == 1) {
                    //ordenar por los angitugos
                    SharedPreferences.Editor editor = mSharedPref.edit();
                    editor.putString("Ordenar", "antiguos");
                    editor.apply();//aplicar y guaardar el valor

                }
            }
        });

        builder.show();
    }

}
