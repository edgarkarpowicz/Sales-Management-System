/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.UsuarioDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author grupoLudeña
 */

public class UsuarioDao implements Dao<UsuarioDto> {

    private Connection conn; // Managed via ConexionSql

    public UsuarioDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public UsuarioDto buscar(UsuarioDto dto) {
        String sql = "SELECT * FROM Usuario WHERE idUsuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdUsuario());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public UsuarioDto buscarPorLogin(String login) {
        String sql = "SELECT * FROM Usuario WHERE login = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<UsuarioDto> listarTodos() {
        List<UsuarioDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean insertar(UsuarioDto dto) {
        String sql = "INSERT INTO Usuario (idPersona, login, password, fechaUltAcceso) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, dto.getIdPersona());
            stmt.setString(2, dto.getLogin());
            stmt.setString(3, dto.getPassword()); // Consider hashing passwords before this step
            stmt.setString(4, dto.getFechaUltAcceso());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setIdUsuario(generatedKeys.getInt(1));
                } else {
                     System.err.println("Creating Usuario failed, no ID obtained.");
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modificar(UsuarioDto dto) {
        String sql = "UPDATE Usuario SET idPersona = ?, login = ?, password = ?, fechaUltAcceso = ? WHERE idUsuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdPersona());
            stmt.setString(2, dto.getLogin());
            stmt.setString(3, dto.getPassword()); // Consider hashing
            stmt.setString(4, dto.getFechaUltAcceso());
            stmt.setInt(5, dto.getIdUsuario());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean borrar(UsuarioDto dto) {
        String sql = "DELETE FROM Usuario WHERE idUsuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdUsuario());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<UsuarioDto> listarPorCriterio(UsuarioDto dto) {
        List<UsuarioDto> lista = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Usuario WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (dto.getLogin() != null && !dto.getLogin().isEmpty()) {
            sqlBuilder.append(" AND login = ?");
            params.add(dto.getLogin());
        }
        // Add other criteria as needed

        try (PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private UsuarioDto mapResultSetToUsuario(ResultSet rs) throws SQLException {
        return new UsuarioDto(
                rs.getInt("idUsuario"),
                rs.getInt("idPersona"),
                rs.getString("login"),
                rs.getString("password"),
                rs.getString("fechaUltAcceso")
        );
    }
}