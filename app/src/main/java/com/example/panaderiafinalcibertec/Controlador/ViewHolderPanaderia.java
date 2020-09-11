package com.example.panaderiafinalcibertec.Controlador;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.panaderiafinalcibertec.R;
import com.squareup.picasso.Picasso;

public class ViewHolderPanaderia extends RecyclerView.ViewHolder  {

    private View mView;

    public ViewHolderPanaderia(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });


    }

    public  void setDetails(Context ctx, String titulo, String imagen, String precio, String descripcion, String estado){

        TextView mTituloView = itemView.findViewById(R.id.tv_titulo_panaderia);
        ImageView mImagenView = itemView.findViewById(R.id.img_imagen_panaderia);
        TextView mPrecioView = itemView.findViewById(R.id.tv_precio_panaderia);
        TextView mDescripcionView = itemView.findViewById(R.id.tv_descripcion_panaderia);
        TextView mEstadoView = itemView.findViewById(R.id.tv_estado_panaderia);

        Picasso.get().load(imagen).placeholder(R.drawable.ic_charging)
                .error(R.drawable.ic_charging).into(mImagenView);
        mTituloView.setText(titulo);
        mPrecioView.setText(precio);
        mDescripcionView.setText(descripcion);
        mEstadoView.setText(estado);

    }

    private  ViewHolderPanaderia.ClickListener mClickListener;

    public  interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view,int position);
    }

    public void setOnClickListener(ViewHolderPanaderia.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
