package com.example.main.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.Modelo.Usuario;
import com.example.main.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AdapterUsuarios extends RecyclerView.Adapter<AdapterUsuarios.ViewHolderUsuarios> implements View.OnClickListener {

    private Context context;
    private ArrayList<Usuario> listaDatos;
    private View.OnClickListener listener;

    public AdapterUsuarios(Context context, ArrayList<Usuario> listaDatos) {
        this.context = context;
        this.listaDatos = listaDatos;
    }

    @Override
    public ViewHolderUsuarios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,null,false);
        view.setOnClickListener(this);
        return new ViewHolderUsuarios(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUsuarios holder, int position) {
        holder.asginarDatos(listaDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public class ViewHolderUsuarios extends RecyclerView.ViewHolder {
        TextView txtUser, txtUserName, txtEmail;

        public ViewHolderUsuarios(@NonNull View itemView) {
            super(itemView);
            txtUser = (TextView) itemView.findViewById(R.id.txtUser);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
        }

        public void asginarDatos(Usuario usuario) {
            txtUser.setText(usuario.getName());
            txtUserName.setText(usuario.getUsername());
            txtEmail.setText(usuario.getEmail());
        }
    }
}