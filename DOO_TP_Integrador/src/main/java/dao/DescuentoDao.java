/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.CajeroDto;
import dto.DescuentoDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author edgar
 */
public class DescuentoDao implements Dao<CajeroDto> {

    private final Connection conn; // Managed via ConexionSql

    public DescuentoDao(Connection conn) {
        this.conn = conn;
    }

    public DescuentoDto buscarPorTipo(String tipo) {
        String sql = "SELECT * FROM Descuento WHERE tipo = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tipo);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToDescuento(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DescuentoDto mapResultSetToDescuento(ResultSet rs) throws SQLException {
        DescuentoDto dto = new DescuentoDto();
        dto.setIdDescuento(rs.getInt("idDescuento"));
        dto.setTipo(rs.getString("tipo"));
        dto.setValor(rs.getFloat("valor")); // use float since your DTO uses float
        return dto;
    }

    @Override
    public CajeroDto buscar(CajeroDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CajeroDto> listarPorCriterio(CajeroDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CajeroDto> listarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean insertar(CajeroDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean modificar(CajeroDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean borrar(CajeroDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
