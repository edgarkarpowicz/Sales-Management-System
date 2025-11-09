/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.PersonaDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author grupoLudeña
 */

public class PersonaDao implements Dao<PersonaDto> {

    private final Connection conn; // Managed via ConexionSql

    public PersonaDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public PersonaDto buscar(PersonaDto dto) { // Existing method
        String sql = "SELECT * FROM Persona WHERE idPersona = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdPersona());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPersona(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PersonaDto> listarTodos() { // Existing method
        List<PersonaDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Persona";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSetToPersona(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean insertar(PersonaDto dto) { // Adapted from existing
        String sql = "INSERT INTO Persona (idDomicilio, nombre, apellido, numDocumento, tipoDocumento, cuit, condicionAfip, genero, fechaNacimiento, email) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, dto.getIdDomicilio());
            stmt.setString(2, dto.getNombre());
            stmt.setString(3, dto.getApellido());
            stmt.setString(4, dto.getNumDocumento());
            stmt.setString(5, dto.getTipoDocumento());
            stmt.setString(6, dto.getCuit());
            stmt.setString(7, dto.getCondicionAfip());
            stmt.setString(8, dto.getGenero());
            stmt.setString(9, dto.getFechaNacimiento());
            stmt.setString(10, dto.getEmail());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dto.setIdPersona(generatedKeys.getInt(1));
                } else {
                    System.err.println("Creating Persona failed, no ID obtained.");
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modificar(PersonaDto dto) { // Existing method
        String sql = "UPDATE Persona SET idDomicilio = ?, nombre = ?, apellido = ?, numDocumento = ?, tipoDocumento = ?, " +
                     "cuit = ?, condicionAfip = ?, genero = ?, fechaNacimiento = ?, email = ? WHERE idPersona = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdDomicilio());
            stmt.setString(2, dto.getNombre());
            stmt.setString(3, dto.getApellido());
            stmt.setString(4, dto.getNumDocumento());
            stmt.setString(5, dto.getTipoDocumento());
            stmt.setString(6, dto.getCuit());
            stmt.setString(7, dto.getCondicionAfip());
            stmt.setString(8, dto.getGenero());
            stmt.setString(9, dto.getFechaNacimiento());
            stmt.setString(10, dto.getEmail());
            stmt.setInt(11, dto.getIdPersona());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean borrar(PersonaDto dto) { // Existing method
        String sql = "DELETE FROM Persona WHERE idPersona = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdPersona());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<PersonaDto> listarPorCriterio(PersonaDto dto) { // Adapted from existing
        List<PersonaDto> lista = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Persona WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (dto.getNombre() != null && !dto.getNombre().isEmpty()) {
            sqlBuilder.append(" AND nombre LIKE ?");
            params.add("%" + dto.getNombre() + "%");
        }
        if (dto.getApellido() != null && !dto.getApellido().isEmpty()) {
            sqlBuilder.append(" AND apellido LIKE ?");
            params.add("%" + dto.getApellido() + "%");
        }
        if (dto.getNumDocumento() != null && !dto.getNumDocumento().isEmpty()) {
            sqlBuilder.append(" AND numDocumento = ?");
            params.add(dto.getNumDocumento());
        }
        // Add other criteria as needed

        try (PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToPersona(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private PersonaDto mapResultSetToPersona(ResultSet rs) throws SQLException { // Existing method
        return new PersonaDto(
                rs.getInt("idPersona"),
                rs.getInt("idDomicilio"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("numDocumento"),
                rs.getString("tipoDocumento"),
                rs.getString("cuit"),
                rs.getString("condicionAfip"),
                rs.getString("genero"),
                rs.getString("fechaNacimiento"),
                rs.getString("email")
        );
    }
}
