package ejem1;
public class User {
    // respuesta.getString("alias");
    // respuesta.getString("nombre_visible");
    // respuesta.getString("correo_electronico");
    // respuesta.getString("contrasena");
    // respuesta.getString("biografia");
    // respuesta.getString("fotografia");

    private String alias;
    private String nombre_visible;
    private String correo_electronico;
    private String contrasena;
    private String biografia;
    private String fotografia;

    // region de setters
    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setNombre_visible(String nombre_visible) {
        this.nombre_visible = nombre_visible;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    // endregion
    // region de getters
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
    // endregion

    public User(String alias, String nombre_visible, String correo_electronico, String contrasena, String biografia,
            String fotografia) {
        this.alias = alias;
        this.nombre_visible = nombre_visible;
        this.correo_electronico = correo_electronico;
        this.contrasena = contrasena;
        this.biografia = biografia;
        this.fotografia = fotografia;
    }

}
