package com.example.pingu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<PostMuro> listaPosts;
    private PostActionListener listener;

    public PostAdapter(List<PostMuro> listaPosts, PostActionListener listener) {
        this.listaPosts = listaPosts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostMuro post = listaPosts.get(position);

        holder.tvAutor.setText(post.getAutor());
        holder.tvContenido.setText(post.getContenido());
        int likes = post.getNumeroLikes();
        holder.tvNumeroLikes.setText(likes == 1 ? "1 like" : likes + " likes");
        holder.btnEliminar.setVisibility(post.isEsMio() ? View.VISIBLE : View.GONE);
        holder.btnLike.setVisibility(post.isUsuarioHaDadoLike() ? View.GONE : View.VISIBLE);
        holder.btnDislike.setVisibility(post.isUsuarioHaDadoLike() ? View.VISIBLE : View.GONE);

        holder.btnLike.setOnClickListener(v -> listener.onLike(post));
        holder.btnDislike.setOnClickListener(v -> listener.onDislike(post));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminar(post));
        holder.btnResponder.setOnClickListener(v -> listener.onResponder(post));
        holder.btnVerRespuestas.setOnClickListener(v -> listener.onVerRespuestas(post, holder.tvRespuestas));
    }

    @Override
    public int getItemCount() {
        return listaPosts.size();
    }

    public void actualizarLista(List<PostMuro> nuevaLista) {
        this.listaPosts = nuevaLista;
        notifyDataSetChanged();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvAutor, tvContenido, tvRespuestas, tvNumeroLikes;
        Button btnLike, btnDislike, btnEliminar, btnResponder, btnVerRespuestas;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAutor = itemView.findViewById(R.id.tvAutor);
            tvContenido = itemView.findViewById(R.id.tvContenido);
            tvRespuestas = itemView.findViewById(R.id.tvRespuestas);
            tvNumeroLikes = itemView.findViewById(R.id.tvNumeroLikes);

            btnLike = itemView.findViewById(R.id.btnLike);
            btnDislike = itemView.findViewById(R.id.btnDislike);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnResponder = itemView.findViewById(R.id.btnResponder);
            btnVerRespuestas = itemView.findViewById(R.id.btnVerRespuestas);
        }
    }
}