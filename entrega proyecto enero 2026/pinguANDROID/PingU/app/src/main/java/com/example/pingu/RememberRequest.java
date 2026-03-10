package com.example.pingu;

public class RememberRequest {

    private String correo_electronico;

    public RememberRequest(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }
}