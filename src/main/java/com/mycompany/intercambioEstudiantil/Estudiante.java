package com.mycompany.intercambioEstudiantil;

public class Estudiante 
{
    //ATRIBUTOS
    private String rut;
    private String nombre;
    private String carrera;
    private int anioIngreso;
    private String estadoProceso; //"Postulación", "Aceptado", "Rechazado", "En curso"
    private Convenio convenio;
    
    //CONSTRUCTOR
    public Estudiante(String rut, String nombre, String carrera, int anioIngreso, String estadoProceso, Convenio convenio)
    {
        this.rut = rut;
        this.nombre = nombre;
        this.carrera = carrera;
        this.anioIngreso = anioIngreso;
        this.estadoProceso = estadoProceso;
        this.convenio = convenio;
    }
    
    //Métodos Getter y Setter
    public String getRut(){
        return rut;  
    }
    public void setRut(String rut){
        this.rut = rut;
    }
    
    public String getNombre(){
        return nombre;  
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public String getCarrera(){
        return carrera;  
    }
    public void setCarrera(String carrera){
        this.carrera = carrera;
    }
    
    public int getAnioIngreso(){
        return anioIngreso;  
    }
    public void setAnioIngreso(int anioIngreso){
        this.anioIngreso = anioIngreso;
    }
    
    
    public String getEstadoProceso(){
        return estadoProceso;  
    }
    public void setEstadoProceso(String estadoProceso){
        this.estadoProceso = estadoProceso;
    }
    
    public Convenio getConvenio(){
        return convenio;  
    }
    public void setConvenio(Convenio convenio){
        this.convenio = convenio;
    }
}
