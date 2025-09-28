package com.mycompany.intercambioEstudiantil;

import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;


public class Main
{
    public static void main(String[] args) throws IOException {
        Control herramientas = new Control();
        herramientas.datos(); 
        
        Scanner leer = new Scanner(System.in);
        //SIA 2.2
        DataStore store = new DataStore(herramientas);

        // Los datos se cargan desde CSV, si no hay datos puedes dejar los datos semillas
        try {
        store.load();
        if (herramientas.getEstudiantes().isEmpty() && herramientas.getConvenios().isEmpty()) 
        {
            herramientas.datos();
        }
        } catch (Exception ex) 
        {
            System.out.println("No se pudo cargar CSV: " + ex.getMessage());
        }

        // 2Mostrar ruta de trabajo para ubicar la carpeta 'data'
        System.out.println("Working dir: " + java.nio.file.Paths.get("").toAbsolutePath());
        
        // Si cerramos sin seleccionar la opcion 0
        Runtime.getRuntime().addShutdownHook(new Thread(() -> 
        {
            try { store.save(); } catch (Exception ignored) {}
        }));
        
        //MENÚ
        while(true)
        {
            System.out.println("\n=== Sistema Gestión de Intercambio ===");
            System.out.println("1) Registrar estudiante");
            System.out.println("2) Tramite");
            System.out.println("3) Subir documento a trámite");
            System.out.println("4) Ver estado de trámite");
            System.out.println("5) Convenio");
            System.out.println("6) Configurar requisitos de un convenio");
            System.out.println("7) Ver estudiantes");
            System.out.println("8) Buscar (1 o más niveles)");
            System.out.println("0) Salir");
            System.out.print("Opcion: ");
            String menu = leer.nextLine();
            
            try 
            {
                switch (menu)
                {
                    case "1": 
                    {
                        System.out.println("Registrar estudiante:");
                        System.out.println("  a) Usando campos (rut, nombre, carrera, año)");
                        System.out.println("  b) Usando objeto Estudiante");
                        System.out.print("Elige a/b: ");
                        String modo = leer.nextLine().trim().toLowerCase();

                        if ("a".equals(modo)) 
                        {
                            System.out.print("RUT: ");
                            String rut = leer.nextLine();
                            System.out.print("Nombre: ");
                            String nombre = leer.nextLine();
                            System.out.print("Carrera: ");
                            String carrera = leer.nextLine();
                            System.out.print("Año de ingreso: ");
                            int anio = Integer.parseInt(leer.nextLine());

                            herramientas.registrarEstudiante(rut, nombre, carrera, anio); // <-- sobrecarga por campos
                            System.out.println("Estudiante registrado (por campos).");
                        } else if ("b".equals(modo)) {
                            System.out.print("RUT: ");
                            String rut = leer.nextLine();
                            System.out.print("Nombre: ");
                            String nombre = leer.nextLine();
                            System.out.print("Carrera: ");
                            String carrera = leer.nextLine();
                            System.out.print("Año de ingreso: ");
                            int anio = Integer.parseInt(leer.nextLine());

                            Estudiante e = new Estudiante(rut, nombre, carrera, anio, "Postulación", null);
                            herramientas.registrarEstudiante(e); // <-- sobrecarga con objeto
                            System.out.println("Estudiante registrado (por objeto).");
                        } else {
                            System.out.println("Opción inválida.");
                        }
                        break;
                    }
                    case "2": // SUBMENÚ de CONVENIOS -> TRÁMITES (2da colección anidada)
                    { 
                        System.out.print("ID del Convenio: ");
                        String idC = leer.nextLine();
                        Convenio c = herramientas.buscarConvenio(idC);
                        if (c == null) {
                            System.out.println("No existe un convenio con id: " + idC);
                            break;
                        }

                        while (true) {
                            System.out.println("\n=== Submenú Trámites del Convenio " + idC + " ===");
                            System.out.println("1) Crear trámite (para un RUT estudiante)");
                            System.out.println("2) Listar trámites");
                            System.out.println("3) Subir documento a un trámite");
                            System.out.println("4) Eliminar trámite");
                            System.out.println("5) Eliminar documento de un trámite");
                            System.out.println("0) Volver");
                            System.out.print("Opción: ");
                            String op2 = leer.nextLine();

                            try {
                                switch (op2) {
                                    case "1": { // Crear
                                        System.out.print("RUT del estudiante: ");
                                        String rut = leer.nextLine();
                                        System.out.print("ID de trámite (T-1/ C-3) Opcional, puedes presionar enter y se crea solo el ID: ");
                                        String idT = leer.nextLine();
                                        boolean ok = herramientas.crearTramite(idC, rut, idT.isBlank() ? null : idT);
                                        System.out.println(ok ? "Trámite creado." : "No se pudo crear (revisa convenio/rut).");
                                        break;
                                    }
                                    case "2": { // Listar
                                        java.util.List<Tramite> ts = herramientas.listarTramites(idC);
                                        if (ts.isEmpty()) {
                                            System.out.println("Sin trámites.");
                                        } else {
                                            for (Tramite t : ts) {
                                                System.out.println("- " + t.getIdTramite()
                                                        + " | Estudiante: " + t.getEstudiante().getRut()
                                                        + " | Estado: " + t.getEstado()
                                                        + " | Docs: " + t.getDocumentos().keySet());
                                            }
                                        }
                                        break;
                                    }
                                    case "3": { // Subir documento
                                        /*System.out.print("ID Trámite: ");
                                        String idT = leer.nextLine();
                                        System.out.println("Tipos válidos:");
                                        for (TipoDocumento td : TipoDocumento.values()) System.out.println(" - " + td.name());
                                        System.out.print("Tipo documento: ");
                                        String tipo = leer.nextLine();
                                        System.out.print("Nombre archivo: ");
                                        String arch = leer.nextLine();
                                        try {
                                            boolean ok = herramientas.subirDocumentoATramite(idC, idT, TipoDocumento.valueOf(tipo.trim()), arch);
                                            System.out.println(ok ? "Documento subido." : "No se pudo subir (revisa ids).");
                                        } catch (IllegalArgumentException ex) {
                                            System.out.println("TipoDocumento inválido.");
                                        }
                                        break;*/
                                        System.out.print("ID Trámite: ");
                                        String idT = leer.nextLine();
                                        System.out.println("Tipos válidos:");
                                        for (TipoDocumento td : TipoDocumento.values()) System.out.println(" - " + td.name());
                                        System.out.print("Tipo documento: ");
                                        String tipo = leer.nextLine();
                                        System.out.print("Nombre archivo: ");
                                        String arch = leer.nextLine();

                                        try {
                                            TipoDocumento td = TipoDocumento.valueOf(tipo.trim());
                                            herramientas.subirDocumentoATramiteStrict(idC, idT, td, arch);
                                            System.out.println("Documento subido.");
                                        } catch (IllegalArgumentException ex) { // valueOf falló
                                            System.out.println("TipoDocumento inválido.");
                                        } catch (DocumentoDuplicadoException ex) {
                                            System.out.println( ex.getMessage());
                                        } catch (TramiteNoEncontradoException ex) {
                                            System.out.println( ex.getMessage());
                                        } catch (Exception ex) {
                                            System.out.println("Error inesperado: " + ex.getMessage());
                                        }
                                        break;
                                    }
                                    case "4": { // Eliminar trámite
                                        System.out.print("ID Trámite: ");
                                        String idT = leer.nextLine();
                                        boolean ok = herramientas.eliminarTramite(idC, idT);
                                        System.out.println(ok ? "Trámite eliminado." : "No se pudo eliminar.");
                                        break;
                                    }
                                    case "5": { // Eliminar documento de un trámite
                                        System.out.print("ID Trámite: ");
                                        String idT = leer.nextLine();
                                        System.out.println("Tipos válidos:");
                                        for (TipoDocumento td : TipoDocumento.values()) System.out.println(" - " + td.name());
                                        System.out.print("Tipo documento a eliminar: ");
                                        String tipo = leer.nextLine();
                                        try {
                                            boolean ok = herramientas.eliminarDocumentoDeTramite(idC, idT, TipoDocumento.valueOf(tipo.trim()));
                                            System.out.println(ok ? "Documento eliminado." : "No se pudo eliminar (revisa ids).");
                                        } catch (IllegalArgumentException ex) {
                                            System.out.println("TipoDocumento inválido.");
                                        }
                                        break;
                                    }
                                    case "0":
                                        // volver al menú principal
                                        System.out.println("Volviendo…");
                                        return; // <- sale del case "2" y vuelve al menú principal
                                    default:
                                        System.out.println("Opción inválida.");
                                }
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                    }
                    case "3":
                    {
                        System.out.print("Código convenio: "); 
                        String idConvenio = leer.nextLine();
                        Convenio conve = herramientas.buscarConvenio(idConvenio);
                        if (conve == null) { 
                            System.out.println("Convenio no existe."); 
                            break; 
                        }

                        System.out.print("Id trámite: "); 
                        String id = leer.nextLine();
                        Tramite trami = conve.getTramites().stream().filter(x -> x.getIdTramite().equals(id)).findFirst().orElse(null);
                        if (trami == null) { System.out.println("Trámite no existe."); break; }

                        System.out.println("Tipos disponibles: " + Arrays.toString(TipoDocumento.values()));
                        System.out.print("Tipo (exacto): "); 
                        String tipo = leer.nextLine();
                        System.out.print("Nombre archivo: "); 
                        String archivo = leer.nextLine();

                        trami.subirDocumento(tipo, archivo);
                        conve.validarYActualizarEstado(trami);
                        System.out.println("Documento subido. Estado actual: " + trami.getEstado());
                        break;
                    }
                    case "4":
                    {
                        System.out.print("Código convenio: "); 
                        String idConvenio = leer.nextLine();
                        Convenio conve = herramientas.buscarConvenio(idConvenio);
                        if (conve == null) 
                        { 
                            System.out.println("Convenio no existe."); 
                            break; 
                        }

                        System.out.print("Id trámite: "); 
                        String idTramite = leer.nextLine();
                        Tramite trami = conve.getTramites().stream().filter(x -> x.getIdTramite().equals(idTramite)).findFirst().orElse(null);
                        if (trami == null) 
                        { 
                            System.out.println("Trámite no existe."); 
                            break; 
                        }

                        System.out.println("Requisitos: " + conve.getRequisitos());
                        System.out.println("Subidos: " + trami.getDocumentos().keySet());
                        System.out.println("Estado: " + trami.getEstado());
                        break;
                    }
                    case "5": 
                    {
                        while (true) 
                        {
                            System.out.println("\n=== Submenú Convenios ===");
                            System.out.println("1) Agregar convenio");
                            System.out.println("2) Listar convenios");
                            System.out.println("3) Modificar convenio");
                            System.out.println("4) Eliminar convenio");
                            System.out.println("0) Volver");
                            System.out.print("Opción: ");
                            String op5 = leer.nextLine();

                            try {
                                switch (op5) {
                                    case "1": { // Agregar
                                        System.out.print("ID: ");
                                        String id = leer.nextLine();

                                        System.out.print("Nombre: ");
                                        String nom = leer.nextLine();

                                        System.out.print("Universidad socia: ");
                                        String uni = leer.nextLine();

                                        System.out.print("País: ");
                                        String pais = leer.nextLine();

                                        System.out.print("Duración (ej: '6 meses'): ");
                                        String dur = leer.nextLine();

                                        System.out.print("Carrera asociada: ");
                                        String car = leer.nextLine();

                                        // Requisitos (opcional)
                                        java.util.Set<TipoDocumento> req = new java.util.HashSet<>();
                                        System.out.print("¿Agregar requisitos? (S/N): ");
                                        String r = leer.nextLine();
                                        if (r.equalsIgnoreCase("S")) {
                                            System.out.println("Tipos válidos:");
                                            for (TipoDocumento td : TipoDocumento.values()) System.out.println(" - " + td.name());
                                            System.out.print("Ingresa requisitos separados por coma (ej: PASAPORTE,CERT_ALUMNO_REGULAR): ");
                                            String linea = leer.nextLine();
                                            if (!linea.isBlank()) {
                                                for (String tok : linea.split(",")) {
                                                    try { req.add(TipoDocumento.valueOf(tok.trim())); }
                                                    catch (IllegalArgumentException ex) { System.out.println("Tipo inválido ignorado: " + tok.trim()); }
                                                }
                                            }
                                        }

                                        Convenio c = new Convenio(id, nom, uni, pais, req, dur, car);
                                        herramientas.agregarConvenio(c);
                                        System.out.println("✔ Convenio agregado.");
                                        break;
                                    }
                                    case "2": { // Listar
                                        var lista = herramientas.getConvenios();
                                        if (lista.isEmpty()) {
                                            System.out.println("No hay convenios.");
                                        } else {
                                            System.out.println("\nID | Nombre | Univ. socia | País | Duración | Carrera | Requisitos");
                                            for (Convenio c : lista) {
                                                String reqs = c.getRequisitos().isEmpty()
                                                        ? "-"
                                                        : String.join(",", c.getRequisitos().stream().map(Enum::name).collect(Collectors.toList()));
                                                System.out.println(c.getIdConvenio() + " | " + c.getNombre() + " | " +
                                                        c.getUniversidadSocia() + " | " + c.getPais() + " | " +
                                                        c.getDuracion() + " | " + c.getCarreraAsociada() + " | " + reqs);
                                            }
                                        }
                                        break;
                                    }
                                    case "3": { // Modificar
                                        System.out.print("ID del convenio a modificar: ");
                                        String id = leer.nextLine();
                                        Convenio c = herramientas.buscarConvenio(id);
                                        if (c == null) {
                                            System.out.println("No existe convenio con id: " + id);
                                            break;
                                        }
                                        System.out.print("Nuevo nombre (Enter='" + c.getNombre() + "'): ");
                                        String nom = leer.nextLine(); if (nom.isBlank()) nom = null;

                                        System.out.print("Nueva universidad socia (Enter='" + c.getUniversidadSocia() + "'): ");
                                        String uni = leer.nextLine(); if (uni.isBlank()) uni = null;

                                        System.out.print("Nuevo país (Enter='" + c.getPais() + "'): ");
                                        String pais = leer.nextLine(); if (pais.isBlank()) pais = null;

                                        System.out.print("Nueva duración (Enter='" + c.getDuracion() + "'): ");
                                        String dur = leer.nextLine(); if (dur.isBlank()) dur = null;

                                        System.out.print("Nueva carrera asociada (Enter='" + c.getCarreraAsociada() + "'): ");
                                        String car = leer.nextLine(); if (car.isBlank()) car = null;

                                        boolean ok = herramientas.editarConvenio(id, nom, uni, pais, dur, car);
                                        System.out.println(ok ? "✔ Convenio actualizado." : "No se pudo actualizar.");
                                        break;
                                    }
                                    case "4": { // Eliminar
                                        System.out.print("ID del convenio a eliminar: ");
                                        String id = leer.nextLine();
                                        System.out.print("¿Confirmas eliminar el convenio " + id + "? (S/N): ");
                                        String conf = leer.nextLine();
                                        if (!conf.equalsIgnoreCase("S")) {
                                            System.out.println("Operación cancelada.");
                                            break;
                                        }
                                        boolean ok = herramientas.eliminarConvenio(id);
                                        System.out.println(ok ? "✔ Convenio eliminado. Estudiantes desasociados." : "No existe ese convenio.");
                                        break;
                                    }
                                    case "0":
                                        System.out.println("Volviendo…");
                                        break; // sale del switch
                                    default:
                                        System.out.println("Opción inválida.");
                                }

                                if (op5.equals("0")) break; // salir del while del submenú

                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        break; // volver al menú principal
                    }
                    case "6": 
                    {
                        System.out.print("Código convenio: "); 
                        String idConvenio = leer.nextLine();
                        Convenio conve = herramientas.buscarConvenio(idConvenio);
                        if (conve == null) 
                        { 
                            System.out.println("Convenio no existe."); 
                            break; 
                        }

                        while (true) 
                        {
                            System.out.println("\n[Configurar requisitos] " + conve.getIdConvenio() + " - " + conve.getNombre());
                            System.out.println("Requisitos actuales: " + conve.getRequisitos());
                            System.out.println("Tipos disponibles: " + java.util.Arrays.toString(TipoDocumento.values()));
                            System.out.println("a) Agregar requisito");
                            System.out.println("b) Quitar requisito");
                            System.out.println("c) Terminar");
                            System.out.print("Opción: ");
                            String menuDos = leer.nextLine();

                            if ("a".equalsIgnoreCase(menuDos)) 
                            {
                                System.out.print("Ingrese tipo EXACTO a agregar: ");
                                String tipo = leer.nextLine();
                                try 
                                {
                                    conve.agregarRequisito(TipoDocumento.valueOf(tipo));
                                    System.out.println("Requisito agregado y trámites revalidados.");
                                } 
                                catch (IllegalArgumentException e) 
                                {
                                    System.out.println("Tipo no válido.");
                                }
                            } 
                            else if ("b".equalsIgnoreCase(menuDos)) 
                            {        
                                System.out.print("Ingrese tipo EXACTO a quitar: ");
                                String tipo = leer.nextLine();
                                try 
                                {
                                    conve.quitarRequisito(TipoDocumento.valueOf(tipo));
                                    System.out.println("Requisito quitado y trámites revalidados.");
                                } 
                                catch (IllegalArgumentException e) 
                                {
                                    System.out.println("Tipo no válido.");
                                }
                            } 
                            else if ("c".equalsIgnoreCase(menuDos)) 
                            {
                                break;
                            } 
                            else 
                            {
                                System.out.println("Opción inválida.");
                            }
                        }
                        break;
                    }
                    case "7": 
                    {
                        System.out.println("\n=== Listado de estudiantes ===");
                        List<Estudiante> lista = new ArrayList<>(herramientas.listarEstudiantes());

                        if (lista.isEmpty()) {
                            System.out.println("No hay estudiantes registrados.");
                            break;
                        }

                        // El formato de como se muestra la lista
                        System.out.printf("%-12s %-25s %-24s %-5s %-14s %-20s%n",
                                "RUT", "Nombre", "Carrera", "Año", "Estado", "Convenio");

                        for (Estudiante e : lista) {
                            String conv = (e.getConvenio() != null) ? e.getConvenio().getNombre() : "-";
                            System.out.printf("%-12s %-25s %-24s %-5d %-14s %-20s%n",
                                    e.getRut(),
                                    e.getNombre(),
                                    e.getCarrera(),
                                    e.getAnioIngreso(),
                                    e.getEstadoProceso(),
                                    conv);
                        }
                        break;
                    }
                    case "8":{ // SUBMENÚ BÚSQUEDA
                        while (true) 
                        {
                            System.out.println("1) Buscar en Estudiantes (nivel 1)");
                            System.out.println("2) Buscar en Convenios (nivel 1)");
                            System.out.println("3) Buscar en Trámites (nivel 2)");
                            System.out.println("4) Buscar por Documento (en Trámites)");
                            System.out.println("5) Búsqueda Global (1+2 niveles)");
                            System.out.println("0) Volver");
                            System.out.print("Opción: ");
                            String opB = leer.nextLine();

                            if (opB.equals("0")) { System.out.println("Volviendo…"); break; }

                            System.out.print("Texto a buscar: ");
                            String q = leer.nextLine();

                            switch (opB) {
                                case "1": { // Estudiantes
                                    var lst = herramientas.buscarEstudiantesPorNombre(q);
                                    if (lst.isEmpty()) System.out.println("Sin resultados.");
                                    else for (Estudiante e : lst)
                                        System.out.println("- " + e.getRut() + " | " + e.getNombre() + " | " + e.getCarrera()
                                                + (e.getEstadoProceso()==null? "" : " | Estado: " + e.getEstadoProceso()));
                                    break;
                                }
                                case "2": { // Convenios
                                    var lst = herramientas.buscarConveniosPorId(q);
                                    if (lst.isEmpty()) System.out.println("Sin resultados.");
                                    else for (Convenio c : lst)
                                        System.out.println("- " + c.getIdConvenio() + " | " + c.getNombre()
                                                + " | " + c.getUniversidadSocia() + " | " + c.getPais());
                                    break;
                                }
                                case "3": { // Trámites
                                    System.out.println("Aquí puedes buscar por idTramite, rut/nombre estudiante, estado, convenio, documentos");
                                    var lst = herramientas.buscarTramitesPorTexto(q);
                                    if (lst.isEmpty()) System.out.println("Sin resultados.");
                                    else for (Tramite t : lst) {
                                        String rut = (t.getEstudiante()==null? "-" : t.getEstudiante().getRut());
                                        System.out.println("- " + t.getIdTramite() + " | Est: " + rut
                                                + " | Estado: " + t.getEstado());
                                    }
                                    break;
                                }
                                case "4": { // Documentos (en trámites)
                                    var lst = herramientas.buscarTramitesPorDocumento(q);
                                    if (lst.isEmpty()) System.out.println("Sin resultados.");
                                    else for (Tramite t : lst) {
                                        String rut = (t.getEstudiante()==null? "-" : t.getEstudiante().getRut());
                                        System.out.print("- " + t.getIdTramite() + " | Est: " + rut + " | Docs: ");
                                        if (t.getDocumentos()==null || t.getDocumentos().isEmpty()) {
                                            System.out.println("-");
                                        } else {
                                            java.util.List<String> parts = new java.util.ArrayList<>();
                                            for (java.util.Map.Entry<TipoDocumento, DocumentoSubido> e : t.getDocumentos().entrySet()) {
                                                String nom = (e.getValue()==null || e.getValue().getNombreArchivo()==null) ? "-" : e.getValue().getNombreArchivo();
                                                parts.add(e.getKey().name() + ":" + nom);
                                            }
                                            System.out.println(String.join(", ", parts));
                                        }
                                    }
                                    break;
                                }
                                case "5": { // Global
                                    var lst = herramientas.buscarGlobal(q);
                                    if (lst.isEmpty()) System.out.println("Sin resultados.");
                                    else for (String line : lst) System.out.println("- " + line);
                                    break;
                                }
                                default:
                                    System.out.println("Opción inválida.");
                            }
                        }
                        break;
                    }    
                    case "0": 
                        try 
                        {
                            store.save();
                            System.out.println("Datos guardados en carpeta 'data/'.");
                        } catch (Exception ex) 
                        {
                                System.out.println("No se pudo guardar CSV: " + ex.getMessage());
                        }
                        return;
                    default: System.out.println("Opción inválida.");
                }
            } 
            catch (Exception e)
            {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}    
