package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author grupoLudeña
 */

// VERSION DE DB EN RESOURCES
// ES EN MEMORIA. NO APLICA CAMBIOS PERSISTENTES A LA DB Y CADA VEZ QUE SE CIERRA LA APP SE BORRAN LOS DATOS
public final class ConexionSql {

    private String URL_CONEXION_JDBC;
    private Connection connection = null;

    public ConexionSql() {
        try {
            String resourcePath = "/ubp/doo/doo_tp_integrador/dooTPIntegradorDB.db";
            URL dbResourceUrl = getClass().getResource(resourcePath);

            if (dbResourceUrl == null) {
                System.err.println("¡Error Crítico! No se encontró el archivo de base de datos en la ruta de recursos: " + resourcePath);
                throw new RuntimeException("Archivo de base de datos no encontrado en recursos: " + resourcePath);
            }

            File dbFile = new File(dbResourceUrl.toURI());
            String dbAbsolutePath = dbFile.getAbsolutePath();

            this.URL_CONEXION_JDBC = "jdbc:sqlite:" + dbAbsolutePath;
            // System.out.println("ConexionSql: Intentando conectar a: " + this.URL_CONEXION_JDBC); // Para depuración

        } catch (java.net.URISyntaxException e) {
            System.err.println("Error al convertir la URL del recurso a URI: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error de sintaxis en URI de base de datos: " + e.getMessage(), e);
        } catch (Exception e) {
             System.err.println("Error inesperado al construir la URL de la base de datos: " + e.getMessage());
             e.printStackTrace();
             this.URL_CONEXION_JDBC = null;
             throw new RuntimeException("Error al inicializar la URL de conexión: " + e.getMessage(), e);
        }
        abrir();
    }

    private void abrir() {
        if (this.URL_CONEXION_JDBC == null) {
            System.err.println("ConexionSql: No se puede abrir la conexión, URL_CONEXION_JDBC es nula.");
            this.connection = null;
            return;
        }
        try {
            this.connection = DriverManager.getConnection(this.URL_CONEXION_JDBC);
            // System.out.println("ConexionSql: Conexión establecida."); // Para depuración
        } catch (SQLException e) {
            System.err.println("ConexionSql: Error al abrir la conexión SQL: " + e.getMessage());
            System.err.println("ConexionSql: URL utilizada: " + this.URL_CONEXION_JDBC);
            e.printStackTrace();
            this.connection = null;
        }
    }

    public void cerrar() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                this.connection = null;
                // System.out.println("ConexionSql: Conexión cerrada."); // Para depuración
            }
        } catch (SQLException ex) {
            System.err.println("ConexionSql: Error al cerrar la conexión SQL: " + ex.getMessage());
            ex.printStackTrace();
            this.connection = null;
        }
    }

    public Connection getConnection() {
        if (this.connection == null) {
            System.err.println("ConexionSql: Se solicitó una conexión nula (apertura inicial pudo fallar).");
        }
        return this.connection;
    }
}


//// VERSIÓN WINDOWS - PERSISTENTE - NO SE BORRA CUANDO SE CIERRA LA APP
//// ARCHIVO DE DB TIENE QUE ESTAR EN DISCO
//public final class ConexionSql {
//
//    private static final String DB_PATH_WINDOWS = "C:\\Users\\Facundo-I\\Desktop\\2da Entrega - ALMOST OVER\\2da Entrega\\DOO_TP_Integrador\\dbDOOTPIntegrador\\dooTPIntegradorDB.db";
//    private String URL_CONEXION_JDBC;
//    private Connection connection = null;
//
//    public ConexionSql() {
//        try {
//            // Replace backslashes with slashes or escape them if needed
//            this.URL_CONEXION_JDBC = "jdbc:sqlite:" + DB_PATH_WINDOWS.replace("\\", "/");
//            abrir();
//        } catch (Exception e) {
//            System.err.println("Error al inicializar la conexión JDBC: " + e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException("No se pudo conectar a la base de datos.");
//        }
//    }
//
//    private void abrir() {
//        try {
//            this.connection = DriverManager.getConnection(this.URL_CONEXION_JDBC);
//            // Optional debug
//            System.out.println("✔ Conectado a: " + this.URL_CONEXION_JDBC);
//        } catch (SQLException e) {
//            System.err.println("Error al abrir la conexión: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    public void cerrar() {
//        try {
//            if (this.connection != null && !this.connection.isClosed()) {
//                this.connection.close();
//                this.connection = null;
//                System.out.println("✔ Conexión cerrada.");
//            }
//        } catch (SQLException e) {
//            System.err.println("Error al cerrar la conexión: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    public Connection getConnection() {
//        return this.connection;
//    }
//}