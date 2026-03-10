package com.example.pingu;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    private String id;
    private String alias;

    @SerializedName("nombre_visible")
    private String nombreVisible;

    @SerializedName("correo_electronico")
    private String correoElectronico;

    private String biografia;

    @SerializedName("fotografia_url")
    private String fotografiaUrl;

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

    public String getFotografiaUrl() {
        return fotografiaUrl;
    }
}
