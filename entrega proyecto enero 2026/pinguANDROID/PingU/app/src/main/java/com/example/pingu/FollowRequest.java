package com.example.pingu;

import com.google.gson.annotations.SerializedName;

public class FollowRequest {

    @SerializedName("id_seguidor")
    private String id_seguidor;

    public FollowRequest(String id_seguidor) {
        this.id_seguidor = id_seguidor;
    }

    public String getId_seguidor() {
        return id_seguidor;
    }

    public void setId_seguidor(String id_seguidor) {
        this.id_seguidor = id_seguidor;
    }
}