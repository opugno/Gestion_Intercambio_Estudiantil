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
    
    // Buscar un trámite por id dentro de un convenio
    public Tramite buscarTramite(String idConvenio, String idTramite) {
        Convenio c = buscarConvenio(idConvenio);
        if (c == null) return null;
        for (Tramite t : c.getTramites()) {
            if (t.getIdTramite().equals(idTramite)) return t;
        }
        return null;
    }

    // Crear trámite para un estudiante en un convenio (idTramite opcional)
    public boolean crearTramite(String idConvenio, String rutEstudiante, String idTramiteOpcional) {
        Convenio c = buscarConvenio(idConvenio);
        Estudiante e = buscarEstudiante(rutEstudiante);
        if (c == null || e == null) return false;

        Tramite t = c.crearTramite(e); // usa tu factory ya existente
        if (idTramiteOpcional != null && !idTramiteOpcional.isBlank()) {
            t.setIdTramite(idTramiteOpcional);
        }
        // recalcular estado por si hay requisitos
        c.validarYActualizarEstado(t);
        return true;
    }

    //Listar trámites de un convenio
    public java.util.List<Tramite> listarTramites(String idConvenio) {
        Convenio c = buscarConvenio(idConvenio);
        return (c == null) ? java.util.Collections.emptyList() : new java.util.ArrayList<>(c.getTramites());
    }

    //Editar (estado y/o estudiante asignado)
    public boolean editarTramite(String idConvenio, String idTramite,
                                 Tramite.Estado nuevoEstado, String nuevoRutEstudiante) {
        Convenio c = buscarConvenio(idConvenio);
        if (c == null) return false;
        Tramite t = buscarTramite(idConvenio, idTramite);
        if (t == null) return false;

        if (nuevoRutEstudiante != null && !nuevoRutEstudiante.isBlank()) {
            Estudiante nuevo = buscarEstudiante(nuevoRutEstudiante);
            if (nuevo == null) return false;
            t.setEstudiante(nuevo);
        }
        if (nuevoEstado != null) {
            t.setEstado(nuevoEstado);
        }
        c.validarYActualizarEstado(t);
        return true;
    }

    // Eliminar trámite
    public boolean eliminarTramite(String idConvenio, String idTramite) {
        Convenio c = buscarConvenio(idConvenio);
        if (c == null) return false;
        return c.getTramites().removeIf(x -> x.getIdTramite().equals(idTramite));
    }

    // Acciones de documentos dentro del trámite
    public boolean subirDocumentoATramite(String idConvenio, String idTramite,
                                          TipoDocumento tipo, String nombreArchivo) {
        Convenio c = buscarConvenio(idConvenio);
        if (c == null) return false;
        Tramite t = buscarTramite(idConvenio, idTramite);
        if (t == null) return false;

        t.subirDocumento(tipo, nombreArchivo);
        c.validarYActualizarEstado(t);
        return true;
    }

    public boolean eliminarDocumentoDeTramite(String idConvenio, String idTramite, TipoDocumento tipo) {
        Convenio c = buscarConvenio(idConvenio);
        if (c == null) return false;
        Tramite t = buscarTramite(idConvenio, idTramite);
        if (t == null) return false;

        if (t.getDocumentos().remove(tipo) != null) {
            c.validarYActualizarEstado(t);
            return true;
        }
        return false;
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
    
    //  Versiones que lanzan excepciones para SIA 2.8 y 2.9
    public Estudiante buscarEstudianteStrict(String rut) throws EstudianteNoEncontradoException {
        Estudiante e = buscarEstudiante(rut);
        if (e == null) throw new EstudianteNoEncontradoException(rut);
        return e;
    }

    public void subirDocumentoATramiteStrict(String idConvenio, String idTramite,
                                             TipoDocumento tipo, String nombreArchivo)
            throws TramiteNoEncontradoException, DocumentoDuplicadoException {
        Convenio c = buscarConvenio(idConvenio);
        if (c == null) throw new TramiteNoEncontradoException(idTramite); // Podemos crear ConvenioNoEncontradoException

        Tramite t = buscarTramite(idConvenio, idTramite);
        if (t == null) throw new TramiteNoEncontradoException(idTramite);

        t.subirDocumentoSeguro(tipo, nombreArchivo); // Aqui puede saltar DocumentoDuplicadoException
        c.validarYActualizarEstado(t);
    }

}

