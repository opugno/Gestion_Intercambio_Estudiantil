package com.mycompany.intercambioEstudiantil;

import java.util.*;
import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException {
        Control herramientas = new Control();
        herramientas.datos(); 
        
        Scanner leer = new Scanner(System.in);
        while(true)
        {
            System.out.println("\n=== Sistema Gestión de Intercambio ===");
            System.out.println("1) Registrar estudiante");
            System.out.println("2) Crear trámite de postulación");
            System.out.println("3) Subir documento a trámite");
            System.out.println("4) Ver estado de trámite");
            System.out.println("5) Listar convenios y trámites");
            System.out.println("6) Configurar requisitos de un convenio");
            System.out.println("7) Ver estudiantes");
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
                    case "2": 
                    {
                        System.out.print("Código convenio: "); 
                        String idConvenio = leer.nextLine();
                        System.out.print("RUT estudiante: "); 
                        String rut = leer.nextLine();

                        Convenio conve = herramientas.buscarConvenio(idConvenio);
                        Estudiante estu = herramientas.buscarEstudiante(rut);
                        if (conve == null || estu == null) 
                        { 
                            System.out.println("Datos no válidos."); 
                            break; 
                        }

                        Tramite tra = conve.crearTramite(estu);
                        System.out.println("Trámite creado: " + tra.getIdTramite());
                        System.out.println("Requisitos: " + conve.getRequisitos());
                        break;
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
                        for (Convenio conve : herramientas.getConvenios()) 
                        {
                            System.out.println("\n[" + conve.getIdConvenio() + "] " + conve.getNombre() + " – Req: " + conve.getRequisitos());
                            for (Tramite t : conve.getTramites()) 
                            {
                                System.out.println("  Trámite " + t.getIdTramite() + " | " + t.getEstudiante().getNombre() + " | " + t.getEstado());
                                t.getDocumentos().forEach((k,v)-> System.out.println("    - " + k + " -> " + v.getNombreArchivo()));
                            }
                        }
                        break;
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
                                    System.out.println("OK: requisito agregado y trámites revalidados.");
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
                                    System.out.println("OK: requisito quitado y trámites revalidados.");
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

                        // Encabezado tipo tabla
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
                    case "0": return;
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
