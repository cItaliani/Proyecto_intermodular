package com.example.pingu;

public class PostMuro {

    private String idPost;
    private String idAutor;
    private String autor;
    private String contenido;
    private boolean esMio;
    private boolean usuarioHaDadoLike;
    private int numeroLikes;

    public PostMuro(String idPost, String idAutor, String autor, String contenido,
                    boolean esMio, boolean usuarioHaDadoLike, int numeroLikes) {
        this.idPost = idPost;
        this.idAutor = idAutor;
        this.autor = autor;
        this.contenido = contenido;
        this.esMio = esMio;
        this.usuarioHaDadoLike = usuarioHaDadoLike;
        this.numeroLikes = numeroLikes;
    }

    public String getIdPost() {
        return idPost;
    }

    public String getIdAutor() {
        return idAutor;
    }

    public String getAutor() {
        return autor;
    }

    public String getContenido() {
        return contenido;
    }

    public boolean isEsMio() {
        return esMio;
    }

    public boolean isUsuarioHaDadoLike() {
        return usuarioHaDadoLike;
    }

    public void setUsuarioHaDadoLike(boolean usuarioHaDadoLike) {
        this.usuarioHaDadoLike = usuarioHaDadoLike;
    }

    public int getNumeroLikes() {
        return numeroLikes;
    }

    public void setNumeroLikes(int numeroLikes) {
        this.numeroLikes = numeroLikes;
    }
}