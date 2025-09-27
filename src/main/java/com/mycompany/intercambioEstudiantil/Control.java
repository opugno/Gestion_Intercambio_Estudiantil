package com.mycompany.intercambioEstudiantil;
import java.util.*;

public class Control 
{
    //Primera coleccion, convenios
    private List<Convenio> convenios = new ArrayList<>();
    // Mapa de estudiantes por rut
    //private Map<String, Estudiante> estudiantes = new HashMap<>();
    private HashMap<String, Estudiante> estudiantes = new HashMap<>();
    
    //GETTER Y SETTER
    public List<Convenio> getConvenios() 
    {
        return List.copyOf(convenios);
    }
    
    public Collection<Estudiante> getEstudiantes() 
    {
    return Collections.unmodifiableCollection(new ArrayList<>(estudiantes.values()));
    }
    
    // MÉTODOS y sobrecarga de metodos
    public void registrarEstudiante(Estudiante e) 
    {  
        estudiantes.put(e.getRut(), e);
    }
    public void registrarEstudiante(String rut, String nombre, String carrera, int anioIngreso) 
    {
        Estudiante e = new Estudiante(rut, nombre, carrera, anioIngreso, "Postulación", null);
        registrarEstudiante(e);
    }
    
    public void agregarConvenio(Convenio c)
    { 
        convenios.add(c); 
    }

    public Estudiante buscarEstudiante(String rut) 
    { 
        return estudiantes.get(rut); 
    }
    
    public Convenio buscarConvenio(String codigoConvenio) 
    {
        for (Convenio c : convenios)
        {
            if (c.getIdConvenio().equalsIgnoreCase(codigoConvenio))
            { 
                return c;
            }
        }
        return null;
    }
    
    public List<Estudiante> listarEstudiantes() 
    {
        List<Estudiante> lista = new ArrayList<>(estudiantes.values()); // copia defensiva
        lista.sort(Comparator.comparing(Estudiante::getNombre, String.CASE_INSENSITIVE_ORDER));
        return Collections.unmodifiableList(lista); // no modificable desde afuera
    }

    
    
    //Agregar datos iniciales
    public void datos() {
        // Estudiantes
        registrarEstudiante("11.111.111-1", "Ana Pérez", "ICI", 2022);
        registrarEstudiante("22.222.222-2", "Bruno Díaz", "ICINF", 2021);

        // Convenios y sus requisitos
        Set<TipoDocumento> reqA = new HashSet<>(Arrays.asList(
            TipoDocumento.CERT_NACIMIENTO, TipoDocumento.CERT_ALUMNO_REGULAR
        ));
        Set<TipoDocumento> reqB = new HashSet<>(Arrays.asList(
            TipoDocumento.PASAPORTE, TipoDocumento.CERTIFICADO_NOTAS
        ));

        agregarConvenio(new Convenio("A-2025", "Convenio A", "Universidad A", "País A", reqA, "Tres meses", "Arquitectura"));
        agregarConvenio(new Convenio("B-2025", "Convenio B", "Universidad B", "País B", reqB, "Doce meses", "Ingeniería Industrial"));
        
    }
}

