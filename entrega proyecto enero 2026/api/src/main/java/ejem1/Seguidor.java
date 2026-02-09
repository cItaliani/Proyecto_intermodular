package ejem1;

import java.sql.Date;

public class Seguidor {
    private int idSeguidor;
    private int idSeguido;
    private Date fecha;

    public int getIdSeguidor() {
        return idSeguidor;
    }

    public int getIdSeguido() {
        return idSeguido;
    }

    public Date getFecha(){
        return fecha;
    }

    public void setIdSeguidor(int idSeguidor){
        this.idSeguidor=idSeguidor;
    }

    public void setIdSeguido(int idSeguido){
        this.idSeguido=idSeguido;
    }
    public void setFecha(Date fecha){
        this.fecha=fecha;
    }

    public Seguidor() {
    }

    public Seguidor(int idSeguidor, int idSeguido, Date fecha){
        this.idSeguidor=idSeguidor;
        this.idSeguido=idSeguido;
        this.fecha=fecha;
    }

}
