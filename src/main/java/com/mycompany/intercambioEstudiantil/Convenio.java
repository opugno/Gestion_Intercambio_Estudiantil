package com.mycompany.intercambioEstudiantil;

public class Convenio 
{
    //ATRIBUTOS
    private String idConvenio;
    private String universidadSocia;
    private String pais;
    private int duracion; //en meses
    private String carreraAsociada;
    private String estado; //estado del convenio, si esta vigente para posyular o no.
    
    //CONSTRUCTOR
    public Convenio(String idConvenio, String universidadSocia, String pais, int duracion, String carreraAsociada, String estado) {
        this.idConvenio = idConvenio;
        this.universidadSocia = universidadSocia;
        this.pais = pais;
        this.duracion = duracion;
        this.carreraAsociada = carreraAsociada;
        this.estado = estado;
    }
    
    //SETTERS Y GETTERS
    public String getIdConvenio() {
        return idConvenio;
    }
    public void setIdConvenio(String idConvenio) {
        this.idConvenio = idConvenio;
    }

    public String getUniversidadSocia() {
        return universidadSocia;
    }
    public void setUniversidadSocia(String universidadSocia) {
        this.universidadSocia = universidadSocia;
    }

    public String getPais() {
        return pais;
    }
    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getDuracion() {
        return duracion;
    }
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getCarreraAsociada() {
        return carreraAsociada;
    }
    public void setCarreraAsociada(String carreraAsociada) {
        this.carreraAsociada = carreraAsociada;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
