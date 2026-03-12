package com.example.pingu;

import com.google.gson.annotations.SerializedName;

public class SeguidorResponse {

    @SerializedName("idSeguidor")
    private String idSeguidor;

    @SerializedName("idSeguido")
    private String idSeguido;

    @SerializedName("fecha")
    private Long fecha;

    public String getIdSeguidor() {
        return idSeguidor;
    }

    public String getIdSeguido() {
        return idSeguido;
    }

    public Long getFecha() {
        return fecha;
    }
}