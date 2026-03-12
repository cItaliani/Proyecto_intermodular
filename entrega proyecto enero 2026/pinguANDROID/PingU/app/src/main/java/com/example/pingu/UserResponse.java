package com.example.pingu;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("user_id")
    private String id;

    private String alias;

    @SerializedName("nombre_visible")
    private String nombreVisible;

    @SerializedName("correo_electronico")
    private String correoElectronico;

    private String biografia;

    @SerializedName("fotografia")
    private String fotografia;

    @SerializedName("fecha_alta")
    private long fechaAlta;

    public long getFechaAlta() {
        return fechaAlta;
    }

    public String getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }

    public String getNombreVisible() {
        return nombreVisible;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getBiografia() {
        return biografia;
    }

    public String getFotografia() {
        return fotografia;
    }
}