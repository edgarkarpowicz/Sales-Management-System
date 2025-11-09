/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios; // Or a package name of your choice like 'service' or 'negocio'

import dao.ConexionSql; // Your existing connection class
import dao.UsuarioDao;
import dto.UsuarioDto;
import java.sql.Connection;

/**
 *
 * @author grupoLudeña
 */

public class UsuarioService {

    public UsuarioDto autenticarUsuario(String login, String password) {
        ConexionSql conexionSql = new ConexionSql(); // Manages opening and closing
        Connection conn = conexionSql.getConnection();
        
        if (conn == null) {
            System.err.println("Error de conexión en UsuarioService.");
            // Optionally, throw a custom service exception
            return null; 
        }

        UsuarioDao usuarioDao = new UsuarioDao(conn);
        UsuarioDto usuario = null;

        try {
            usuario = usuarioDao.buscarPorLogin(login);

            if (usuario != null) {
                // IMPORTANT: Implement password hashing!
                // For now, direct comparison as in your original LoginController.
                // Replace this with secure password checking (e.g., PasswordUtils.verifyPassword(password, usuario.getPassword()))
                if (usuario.getPassword().equals(password)) {
                    // Update last access time if needed (could be another method in UsuarioDao)
                    // String fechaActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    // usuario.setFechaUltAcceso(fechaActual);
                    // usuarioDao.modificar(usuario); // Assuming 'modificar' updates fechaUltAcceso
                    return usuario; // Authentication successful
                } else {
                    return null; // Password incorrect
                }
            } else {
                return null; // User not found
            }
        } finally {
            conexionSql.cerrar(); // Ensure connection is closed
        }
    }

    public boolean esCajero(int idUsuario) {
        ConexionSql conexionSql = new ConexionSql();
        Connection conn = conexionSql.getConnection();
        if (conn == null) return false;
        try {
            // Un cajero existe si hay un registro en la tabla Cajero con ese idUsuario
            var stmt = conn.prepareStatement("SELECT 1 FROM Cajero WHERE idUsuario = ?");
            stmt.setInt(1, idUsuario);
            var rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            return false;
        } finally {
            conexionSql.cerrar();
        }
    }

    public boolean esAdministrativo(int idUsuario) {
        ConexionSql conexionSql = new ConexionSql();
        Connection conn = conexionSql.getConnection();
        if (conn == null) return false;
        try {
            // Un administrativo existe si hay un registro en la tabla Administracion con ese idUsuario
            var stmt = conn.prepareStatement("SELECT 1 FROM Administracion WHERE idUsuario = ?");
            stmt.setInt(1, idUsuario);
            var rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            return false;
        } finally {
            conexionSql.cerrar();
        }
    }
}