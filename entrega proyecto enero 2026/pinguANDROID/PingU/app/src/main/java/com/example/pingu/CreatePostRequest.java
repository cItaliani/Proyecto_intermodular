package com.example.pingu;

public class CreatePostRequest {

    private String contenido;
    private String urlMultimedia;
    private String id_autor;
    private String idPostPadre;

    public CreatePostRequest(String contenido, String urlMultimedia, String id_autor, String idPostPadre) {
        this.contenido = contenido;
        this.urlMultimedia = urlMultimedia;
        this.id_autor = id_autor;
        this.idPostPadre = idPostPadre;
    }

    public String getContenido() {
        return contenido;
    }

    public String getUrlMultimedia() {
        return urlMultimedia;
    }

    public String getId_autor() {
        return id_autor;
    }

    public String getIdPostPadre() {
        return idPostPadre;
    }
}