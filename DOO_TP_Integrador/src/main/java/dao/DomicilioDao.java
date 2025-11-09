/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.DomicilioDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author grupoLudeña
 */

public class DomicilioDao implements Dao<DomicilioDto> {

    private Connection conn; // In a real scenario, this would be managed via ConexionSql

    public DomicilioDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public DomicilioDto buscar(DomicilioDto dto) {
        String sql = "SELECT * FROM Domicilio WHERE idDomicilio = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdDomicilio());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToDomicilio(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Proper logging should be used
        }
        return null;
    }

    @Override
    public List<DomicilioDto> listarTodos() {
        List<DomicilioDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Domicilio";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSetToDomicilio(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean insertar(DomicilioDto dto) {
        String sql = "INSERT INTO Domicilio (nomCalle, numVivienda, numDepartamento, codPostal, localidad, departamento, pais) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, dto.getNomCalle());
            stmt.setInt(2, dto.getNumVivienda());
            stmt.setInt(3, dto.getNumDepartamento());
            stmt.setInt(4, dto.getCodPostal());
            stmt.setString(5, dto.getLocalidad());
            stmt.setString(6, dto.getDepartamento());
            stmt.setString(7, dto.getPais());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }
            // Retrieve the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setIdDomicilio(generatedKeys.getInt(1));
                } else {
                    // This case might indicate an issue if ID is expected
                    System.err.println("Creating Domicilio failed, no ID obtained.");
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modificar(DomicilioDto dto) {
        String sql = "UPDATE Domicilio SET nomCalle = ?, numVivienda = ?, numDepartamento = ?, codPostal = ?, localidad = ?, departamento = ?, pais = ? WHERE idDomicilio = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dto.getNomCalle());
            stmt.setInt(2, dto.getNumVivienda());
            stmt.setInt(3, dto.getNumDepartamento());
            stmt.setInt(4, dto.getCodPostal());
            stmt.setString(5, dto.getLocalidad());
            stmt.setString(6, dto.getDepartamento());
            stmt.setString(7, dto.getPais());
            stmt.setInt(8, dto.getIdDomicilio());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean borrar(DomicilioDto dto) {
        String sql = "DELETE FROM Domicilio WHERE idDomicilio = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdDomicilio());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<DomicilioDto> listarPorCriterio(DomicilioDto dto) {
        // Example: Search by localidad if provided
        List<DomicilioDto> lista = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Domicilio WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (dto.getLocalidad() != null && !dto.getLocalidad().isEmpty()) {
            sqlBuilder.append(" AND localidad LIKE ?");
            params.add("%" + dto.getLocalidad() + "%");
        }
        // Add other criteria as needed

        try (PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToDomicilio(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private DomicilioDto mapResultSetToDomicilio(ResultSet rs) throws SQLException {
        return new DomicilioDto(
                rs.getInt("idDomicilio"),
                rs.getString("nomCalle"),
                rs.getInt("numVivienda"),
                rs.getInt("numDepartamento"),
                rs.getInt("codPostal"),
                rs.getString("localidad"),
                rs.getString("departamento"),
                rs.getString("pais")
        );
    }
}
