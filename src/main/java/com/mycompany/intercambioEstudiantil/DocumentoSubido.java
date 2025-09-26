/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intercambioEstudiantil;

public class DocumentoSubido 
{
    //ATRIBUTOS
    private TipoDocumento tipo;
    private String nombreArchivo;
    private LocalDate fechaSubida;

    //CONSTRUCTOR
    public DocumentoSubido(TipoDocumento tipo, String nombreArchivo, LocalDate fechaSubida) {
        this.tipo = tipo;
        this.nombreArchivo = nombreArchivo;
        this.fechaSubida = fechaSubida;
    }
    
    //GETTER Y SETTER
    public TipoDocumento getTipo(){ 
        return tipo; 
    }
    public void setTipo(TipoDocumento tipo){
        this.tipo = tipo;
    }
    
    public String getNombreArchivo(){ 
        return nombreArchivo; 
    }
    public void setNombreArchivo(String nombreArchivo){
        this.nombreArchivo = nombreArchivo;
    }
    
    public LocalDate getFechaSubida(){
        return fechaSubida; 
    }
    public void setFechaSubida(LocalDate fechaSubida){
        this.fechaSubida = fechaSubida;
    }
    
}
