package com.mycompany.intercambioEstudiantil;

public class TramiteNoEncontradoException extends Exception {
    public TramiteNoEncontradoException(String idTramite) {
        super("No se encontró el trámite con ID: " + idTramite);
    }
}

