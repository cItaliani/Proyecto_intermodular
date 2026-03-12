package com.example.pingu;

public class UsuarioLista {

    private String id;
    private String alias;
    private String nombreVisible;
    private boolean loSigo;

    public UsuarioLista(String id, String alias, String nombreVisible, boolean loSigo) {
        this.id = id;
        this.alias = alias;
        this.nombreVisible = nombreVisible;
        this.loSigo = loSigo;
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

    public boolean isLoSigo() {
        return loSigo;
    }

    public void setLoSigo(boolean loSigo) {
        this.loSigo = loSigo;
    }
}