package com.example.pingu;

public class LoginRequest {

    private String alias;
    private String contrasena;

    public LoginRequest(String alias, String contrasena) {
        this.alias = alias;
        this.contrasena = contrasena;
    }

    public String getAlias() {
        return alias;
    }

    public String getContrasena() {
        return contrasena;
    }
}