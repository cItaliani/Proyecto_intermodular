package com.example.pingu;

public class RegisterRequest {

    private String alias;
    private String nombre_visible;
    private String correo_electronico;
    private String contrasena;
    private String biografia;
    private String fotografia;

    public RegisterRequest(String alias, String nombre_visible, String correo_electronico,
                           String contrasena, String biografia, String fotografia) {
        this.alias = alias;
        this.nombre_visible = nombre_visible;
        this.correo_electronico = correo_electronico;
        this.contrasena = contrasena;
        this.biografia = biografia;
        this.fotografia = fotografia;
    }

    public String getAlias() {
        return alias;
    }

    public String getNombre_visible() {
        return nombre_visible;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getBiografia() {
        return biografia;
    }

    public String getFotografia() {
        return fotografia;
    }
}