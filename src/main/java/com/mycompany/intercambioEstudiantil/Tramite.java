package com.mycompany.intercambioEstudiantil;

import java.util.*;

//cada tramite tiene los documentos subidos por el estudiante para postular a un convenio
public class Tramite 
{
    public enum Estado {EN_PROCESO, COMPLETO}
    
    //ATRIBUTOS
    private String idTramite;
    private Estudiante estudiante;
    private Map<TipoDocumento, DocumentoSubido> documento = new HashMap<>();
    private Estado estado =  Estado.EN_PROCESO;
    
    //CONSTRUCTOR
    public Tramite(String idTramite, Estudiante estudiante)
    {
        this.idTramite = idTramite;
        this.estudiante = estudiante;
    }
    
    //SETTERS Y GETTERS
    public String getIdTramite(){
        return idTramite;
    }
    public void setIdTramite(String idTramite){
        this.idTramite = idTramite;
    }
    
    public Estudiante getEstudiante(){
        return estudiante;
    }
    public void setEstudiante(Estudiante estudiante)
    {
        this.estudiante = estudiante;
    }
    
   // public Map<TipoDocumento, DocumentoSubido> getDocumentos(){
   //     return documento;
   // }
    public Map<TipoDocumento, DocumentoSubido> getDocumentos()
    {
        return Collections.unmodifiableMap(new HashMap<>(documento));
    }
    public void setDocumento(Map<TipoDocumento, DocumentoSubido> documento){
        this.documento = documento;
    }
    
    public Estado getEstado(){
        return estado;
    }
    public void setEstado(Estado estado){
        this.estado = estado;
    }
    
    
    //METODOS y sobrecarga de metodos
    public void subirDocumento(TipoDocumento tipo, String nombreArchivo) 
    {
        documento.put(tipo, new DocumentoSubido(tipo, nombreArchivo, java.time.LocalDate.now()));
    }
    public void subirDocumento(String tipo, String nombreArchivo) 
    {
        subirDocumento(TipoDocumento.valueOf(tipo), nombreArchivo);
    }

    // Si se cumplen todos los requisitos del convenio
    public boolean estaCompleto(Set<TipoDocumento> requisitos) 
    {
        for (TipoDocumento req : requisitos) 
        {
            if (!documento.containsKey(req)) 
                return false;
        }
        return true;
    }
    
    public void subirDocumentoSeguro(TipoDocumento tipo, String nombreArchivo) throws DocumentoDuplicadoException 
    {
    if (documentosYaTiene(tipo)) {
        throw new DocumentoDuplicadoException("El documento " + tipo + " ya está subido para este trámite.");
    }
    subirDocumento(tipo, nombreArchivo);
    }

    private boolean documentosYaTiene(TipoDocumento tipo) 
    {
        return this.getDocumentos().containsKey(tipo);
    }

}

