package com.example.pingu;

import com.google.gson.annotations.SerializedName;

public class PostResponse {

    private String id;
    private String contenido;

    @SerializedName("id_autor")
    private String idAutor;

    @SerializedName("url_multimedia")
    private String urlMultimedia;

    @SerializedName("id_post_padre")
    private String idPostPadre;

    public String getId() {
        return id;
    }

    public String getContenido() {
        return contenido;
    }

    public String getIdAutor() {
        return idAutor;
    }

    public String getUrlMultimedia() {
        return urlMultimedia;
    }

    public String getIdPostPadre() {
        return idPostPadre;
    }
}