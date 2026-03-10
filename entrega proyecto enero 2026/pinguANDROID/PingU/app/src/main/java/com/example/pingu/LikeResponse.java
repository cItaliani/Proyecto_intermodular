package com.example.pingu;

import com.google.gson.annotations.SerializedName;

public class LikeResponse {

    @SerializedName("idUsuario")
    private String idUsuario;

    @SerializedName("id_post")
    private String idPost;

    @SerializedName("fecha_creacion")
    private String fechaCreacion;

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getIdPost() {
        return idPost;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }
}