/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.CajeroDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author grupoLudeña
 */
public class CajeroDao implements Dao<CajeroDto> {

    private final Connection conn; // Managed via ConexionSql

    public CajeroDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public CajeroDto buscar(CajeroDto dto) { // Existing method
        String sql = "SELECT * FROM Cajero WHERE idCajero = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdCajero());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCajero(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CajeroDto buscarPorDocYLogin(String tipoDoc, String nroDoc, String login) {
        String sql = "SELECT c.*, u.*, p.* "
                + "FROM Cajero c "
                + "JOIN Usuario u ON c.idUsuario = u.idUsuario "
                + "JOIN Persona p ON u.idPersona = p.idPersona "
                + "WHERE u.login = ? AND p.tipoDocumento = ? AND p.numDocumento = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, tipoDoc);
            stmt.setString(3, nroDoc);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCajero(rs); // Make sure this maps Cajero, Usuario, Persona
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CajeroDto> listarTodos() { // Existing method
        List<CajeroDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cajero";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSetToCajero(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean insertar(CajeroDto dto) { // Adapted from existing
        String sql = "INSERT INTO Cajero (idUsuario) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, dto.getIdUsuario());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setIdCajero(generatedKeys.getInt(1));
                } else {
                    System.err.println("Creating Cajero failed, no ID obtained.");
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modificar(CajeroDto dto) { // Existing method
        String sql = "UPDATE Cajero SET idUsuario = ? WHERE idCajero = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdUsuario());
            stmt.setInt(2, dto.getIdCajero());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean borrar(CajeroDto dto) { // Existing method
        String sql = "DELETE FROM Cajero WHERE idCajero = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdCajero());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CajeroDto> listarPorCriterio(CajeroDto dto) { // Adapted from existing
        List<CajeroDto> lista = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Cajero WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (dto.getIdUsuario() > 0) { // Assuming ID is positive
            sqlBuilder.append(" AND idUsuario = ?");
            params.add(dto.getIdUsuario());
        }
        // Add other criteria as needed

        try (PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToCajero(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private CajeroDto mapResultSetToCajero(ResultSet rs) throws SQLException {
        return new CajeroDto(
                rs.getInt("idCajero"),
                rs.getInt("idUsuario")
        );
    }
}
