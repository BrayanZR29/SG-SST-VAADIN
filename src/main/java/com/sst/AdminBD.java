package com.sst;

import com.sst.util.CargadorPropiedades;
import com.sst.util.PoolConexiones;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Administrador de Base de Datos - SG-SST
 * Permite: Consultar, Exportar, Limpiar y Mantener la base de datos
 */
public class AdminBD {
    private static final Logger LOG = LoggerFactory.getLogger(AdminBD.class);
    private static final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            CargadorPropiedades.cargar("application.properties");
            PoolConexiones.inicializar();
            
            menuPrincipal();
            
            PoolConexiones.cerrar();
        } catch (Exception e) {
            LOG.error("Error en AdminBD: {}", e.getMessage(), e);
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void menuPrincipal() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n========================================");
            System.out.println("  ADMINISTRADOR DE BASE DE DATOS - SG-SST");
            System.out.println("========================================");
            System.out.println("1. Consultar datos");
            System.out.println("2. Exportar tabla a HTML");
            System.out.println("3. Ver estadísticas");
            System.out.println("4. Ejecutar consulta SQL");
            System.out.println("5. Limpiar datos (PELIGROSO)");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(entrada.nextLine().trim());
                switch (opcion) {
                    case 1 -> consultarDatos();
                    case 2 -> exportarTabla();
                    case 3 -> verEstadisticas();
                    case 4 -> ejecutarConsultaSQL();
                    case 5 -> limpiarDatos();
                    case 6 -> salir = true;
                    default -> System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido");
            }
        }
        System.out.println("Adios");
    }

    private static void consultarDatos() {
        System.out.println("\n--- CONSULTAR DATOS ---");
        System.out.println("1. Usuarios");
        System.out.println("2. Eventos");
        System.out.println("3. Investigaciones");
        System.out.println("4. Acciones Correctivas");
        System.out.print("Seleccione tabla: ");
        
        try {
            int tabla = Integer.parseInt(entrada.nextLine().trim());
            String sqlTabla = switch (tabla) {
                case 1 -> "SELECT * FROM usuarios";
                case 2 -> "SELECT * FROM eventos ORDER BY fecha_hora DESC";
                case 3 -> "SELECT * FROM investigaciones ORDER BY fecha_investigacion DESC";
                case 4 -> "SELECT * FROM acciones_correctivas";
                default -> null;
            };
            
            if (sqlTabla != null) {
                mostrarResultados(sqlTabla);
            } else {
                System.out.println("Opción inválida");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un número válido");
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
            LOG.error("Error SQL: {}", e.getMessage());
        }
    }

    private static void exportarTabla() {
        System.out.println("\n--- EXPORTAR TABLA ---");
        System.out.println("1. Usuarios");
        System.out.println("2. Eventos");
        System.out.println("3. Investigaciones");
        System.out.println("4. Acciones Correctivas");
        System.out.print("Seleccione tabla: ");
        
        try {
            int tabla = Integer.parseInt(entrada.nextLine().trim());
            String nombreTabla = switch (tabla) {
                case 1 -> "usuarios";
                case 2 -> "eventos";
                case 3 -> "investigaciones";
                case 4 -> "acciones_correctivas";
                default -> null;
            };
            
            if (nombreTabla != null) {
                exportarHTML(nombreTabla);
            } else {
                System.out.println("Opción inválida");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un número válido");
        }
    }

    private static void exportarHTML(String tabla) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String nombreArchivo = tabla + "_" + timestamp + ".html";
        
        String sql = "SELECT * FROM " + tabla;
        
        try (Connection conexion = PoolConexiones.obtenerConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             FileWriter fw = new FileWriter(nombreArchivo)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Escribir HTML
            fw.write("<!DOCTYPE html>\n<html>\n<head>\n");
            fw.write("<meta charset=\"UTF-8\"/>");
            fw.write("<style>\n");
            fw.write("table {border: medium solid #6495ed;border-collapse: collapse;width: 100%;}\n");
            fw.write("th{font-family: monospace;border: thin solid #6495ed;padding: 5px;background-color: #D0E3FA;}\n");
            fw.write("th{text-align: left;}\n");
            fw.write("td{font-family: sans-serif;border: thin solid #6495ed;padding: 5px;text-align: center;}\n");
            fw.write(".odd{background:#e8edff;}\n");
            fw.write("img{padding:5px; border:solid; border-color: #dddddd #aaaaaa #aaaaaa #dddddd; border-width: 1px 2px 2px 1px; background-color:white;}\n");
            fw.write("</style>\n</head>\n<body>\n");
            fw.write("<table><tr><th colspan=\"" + columnCount + "\"><pre><code>" + tabla + "</code></pre></th></tr>");
            
            // Encabezados
            fw.write("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                fw.write("<th>" + metaData.getColumnName(i) + "</th>");
            }
            fw.write("</tr>");
            
            // Filas
            boolean odd = true;
            while (rs.next()) {
                fw.write(odd ? "<tr class=\"odd\">" : "<tr>");
                for (int i = 1; i <= columnCount; i++) {
                    Object valor = rs.getObject(i);
                    fw.write("<td>" + (valor != null ? valor.toString() : "&nbsp;") + "</td>");
                }
                fw.write("</tr>");
                odd = !odd;
            }
            
            fw.write("</table></body></html>");
            System.out.println("✓ Tabla exportada a: " + nombreArchivo);
            LOG.info("Tabla {} exportada a {}", tabla, nombreArchivo);
        } catch (SQLException | IOException e) {
            System.out.println("Error al exportar: " + e.getMessage());
            LOG.error("Error al exportar tabla {}: {}", tabla, e.getMessage());
        }
    }

    private static void verEstadisticas() {
        System.out.println("\n--- ESTADÍSTICAS ---");
        
        try (Connection conexion = PoolConexiones.obtenerConexion()) {
            // Contar usuarios
            int usuarios = contarRegistros(conexion, "usuarios");
            System.out.println("Usuarios: " + usuarios);
            
            // Contar eventos
            int eventos = contarRegistros(conexion, "eventos");
            System.out.println("Eventos: " + eventos);
            
            // Eventos por estado
            System.out.println("\nEventos por estado:");
            mostrarConteosPor(conexion, "eventos", "estado");
            
            // Eventos por tipo
            System.out.println("\nEventos por tipo:");
            mostrarConteosPor(conexion, "eventos", "tipo");
            
            // Investigaciones
            int investigaciones = contarRegistros(conexion, "investigaciones");
            System.out.println("\nInvestigaciones: " + investigaciones);
            
            // Investigaciones por estado
            System.out.println("\nInvestigaciones por estado:");
            mostrarConteosPor(conexion, "investigaciones", "estado");
            
            // Acciones correctivas
            int acciones = contarRegistros(conexion, "acciones_correctivas");
            System.out.println("\nAcciones correctivas: " + acciones);
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            LOG.error("Error en estadísticas: {}", e.getMessage());
        }
    }

    private static int contarRegistros(Connection conexion, String tabla) throws SQLException {
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tabla)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private static void mostrarConteosPor(Connection conexion, String tabla, String columna) throws SQLException {
        String sql = "SELECT " + columna + ", COUNT(*) as cantidad FROM " + tabla + 
                     " GROUP BY " + columna + " ORDER BY cantidad DESC";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("  " + rs.getString(1) + ": " + rs.getInt(2));
            }
        }
    }

    private static void ejecutarConsultaSQL() {
        System.out.println("\n--- EJECUTAR CONSULTA SQL ---");
        System.out.print("Ingrese consulta SELECT: ");
        String sql = entrada.nextLine().trim();
        
        if (!sql.toUpperCase().startsWith("SELECT")) {
            System.out.println("Error: Solo se permiten consultas SELECT");
            return;
        }
        
        try {
            mostrarResultados(sql);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            LOG.error("Error ejecutando SQL: {}", e.getMessage());
        }
    }

    private static void mostrarResultados(String sql) throws SQLException {
        try (Connection conexion = PoolConexiones.obtenerConexion();
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Mostrar encabezados
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i));
                if (i < columnCount) System.out.print(" | ");
            }
            System.out.println("\n" + "-".repeat(100));
            
            // Mostrar filas
            int filas = 0;
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    Object valor = rs.getObject(i);
                    System.out.print(valor != null ? valor.toString() : "NULL");
                    if (i < columnCount) System.out.print(" | ");
                }
                System.out.println();
                filas++;
            }
            System.out.println("Total: " + filas + " registros");
        }
    }

    private static void limpiarDatos() {
        System.out.println("\n--- LIMPIAR DATOS (PELIGROSO) ---");
        System.out.println("Advertencia: Esto eliminará datos de la base de datos");
        System.out.println("1. Eliminar todos los eventos");
        System.out.println("2. Eliminar todas las investigaciones");
        System.out.println("3. Eliminar todas las acciones correctivas");
        System.out.println("4. Limpiar TODO (usuarios, eventos, investigaciones, acciones)");
        System.out.println("5. Cancelar");
        System.out.print("Seleccione opción: ");
        
        try {
            int opcion = Integer.parseInt(entrada.nextLine().trim());
            
            if (opcion == 5) {
                System.out.println("Operación cancelada");
                return;
            }
            
            System.out.print("¿Está seguro? (escriba 'SI' para confirmar): ");
            String confirmacion = entrada.nextLine().trim().toUpperCase();
            
            if (!confirmacion.equals("SI")) {
                System.out.println("Operación cancelada");
                return;
            }
            
            switch (opcion) {
                case 1 -> ejecutarActualizacion("DELETE FROM eventos");
                case 2 -> ejecutarActualizacion("DELETE FROM investigaciones");
                case 3 -> ejecutarActualizacion("DELETE FROM acciones_correctivas");
                case 4 -> {
                    ejecutarActualizacion("DELETE FROM acciones_correctivas");
                    ejecutarActualizacion("DELETE FROM investigaciones");
                    ejecutarActualizacion("DELETE FROM eventos");
                    ejecutarActualizacion("DELETE FROM usuarios WHERE nombre_usuario NOT IN ('admin', 'responsable')");
                }
                default -> System.out.println("Opción inválida");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un número válido");
        }
    }

    private static void ejecutarActualizacion(String sql) {
        try (Connection conexion = PoolConexiones.obtenerConexion();
             Statement stmt = conexion.createStatement()) {
            
            int filasAfectadas = stmt.executeUpdate(sql);
            System.out.println("✓ " + filasAfectadas + " registros eliminados");
            LOG.info("Ejecución de limpieza: {} registros afectados", filasAfectadas);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            LOG.error("Error ejecutando actualización: {}", e.getMessage());
        }
    }
}
