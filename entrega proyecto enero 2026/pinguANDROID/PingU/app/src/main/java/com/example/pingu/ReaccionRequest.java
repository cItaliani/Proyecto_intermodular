package com.example.pingu;

import com.google.gson.annotations.SerializedName;

public class ReaccionRequest {

    @SerializedName("idUsuario")
    private String idUsuario;

    public ReaccionRequest(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }
}