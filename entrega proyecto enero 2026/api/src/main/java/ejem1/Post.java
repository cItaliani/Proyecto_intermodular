package ejem1;

import java.time.LocalDate;

public class Post {
    private String url_multimedia;
    private String contenido;
    private String fecha_creacion;

    // region de setters
    public void setUrl_multimedia(String url_multimedia) {
        this.url_multimedia = url_multimedia;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = LocalDate.now().toString();
    }

    // endregion
    // region de getters
    public String getUrl_multimedia() {
        return url_multimedia;
    }

    public String getContenido() {
        return contenido;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    // endregion
    public Post() {

    }

    public Post(String url_multimedia, String contenido, String fecha_creacion) {

        this.url_multimedia = url_multimedia;
        this.contenido = contenido;
        this.fecha_creacion = fecha_creacion;
    }

}
