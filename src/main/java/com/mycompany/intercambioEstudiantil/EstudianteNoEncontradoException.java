package com.mycompany.intercambioEstudiantil;

public class EstudianteNoEncontradoException extends Exception 
{
    public EstudianteNoEncontradoException(String rut) {
        super("No se encontró estudiante con RUT: " + rut);
    }
}

