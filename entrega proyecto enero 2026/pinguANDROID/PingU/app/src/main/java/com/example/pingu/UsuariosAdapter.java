package com.example.pingu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder> {

    private List<UsuarioLista> listaUsuarios;
    private UsuarioActionListener listener;
    private boolean mostrarBotonSeguir;

    public UsuariosAdapter(List<UsuarioLista> listaUsuarios, UsuarioActionListener listener, boolean mostrarBotonSeguir) {
        this.listaUsuarios = listaUsuarios;
        this.listener = listener;
        this.mostrarBotonSeguir = mostrarBotonSeguir;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        UsuarioLista usuario = listaUsuarios.get(position);

        holder.tvNombreUsuario.setText(usuario.getNombreVisible());
        holder.tvAliasUsuario.setText("@" + usuario.getAlias());

        if (mostrarBotonSeguir) {
            if (usuario.isLoSigo()) {
                holder.btnSeguir.setVisibility(View.GONE);
                holder.btnDejarSeguir.setVisibility(View.VISIBLE);
            } else {
                holder.btnSeguir.setVisibility(View.VISIBLE);
                holder.btnDejarSeguir.setVisibility(View.GONE);
            }
        } else {
            holder.btnSeguir.setVisibility(View.GONE);
            holder.btnDejarSeguir.setVisibility(View.GONE);
        }

        holder.btnSeguir.setOnClickListener(v -> listener.onSeguirClick(usuario));
        holder.btnDejarSeguir.setOnClickListener(v -> listener.onDejarSeguirClick(usuario));
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public void actualizarLista(List<UsuarioLista> nuevaLista) {
        this.listaUsuarios = nuevaLista;
        notifyDataSetChanged();
    }

    static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreUsuario, tvAliasUsuario;
        Button btnSeguir, btnDejarSeguir;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreUsuario = itemView.findViewById(R.id.tvNombreUsuario);
            tvAliasUsuario = itemView.findViewById(R.id.tvAliasUsuario);
            btnSeguir = itemView.findViewById(R.id.btnSeguir);
            btnDejarSeguir = itemView.findViewById(R.id.btnDejarSeguir);
        }
    }
}