package com.example.pingu;

import com.google.gson.annotations.SerializedName;

public class UpdateUserRequest {

    @SerializedName("nombre_visible")
    private String nombreVisible;

    private String biografia;
    private String contrasena;

    @SerializedName("fotografia")
    private String fotografia;

    public UpdateUserRequest(String nombreVisible, String biografia, String contrasena, String fotografia) {
        this.nombreVisible = nombreVisible;
        this.biografia = biografia;
        this.contrasena = contrasena;
        this.fotografia = fotografia;
    }

    public String getNombreVisible() {
        return nombreVisible;
    }

    public String getBiografia() {
        return biografia;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getFotografia() {
        return fotografia;
    }
}