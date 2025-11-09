/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.ProductoDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC (SQLite, MySQL u otra) del DAO de Producto.
 * Sigue la misma convención que UsuarioDao: cada método usa la Connection
 * proporcionada en el constructor y mapea a/del ProductoDto.
 */
public class ProductoDao implements Dao<ProductoDto> {

    private final Connection conn;

    /**
     * Constructor. Recibe una Connection abierta (p.ej., de ConexionSql).
     */
    public ProductoDao(Connection conn) {
        this.conn = conn;
    }

    // ───────────────────────────────────────────────────────────────────────
    // Consulta única: buscar por idProducto o por codigoBarras
    // ───────────────────────────────────────────────────────────────────────
    @Override
    public ProductoDto buscar(ProductoDto dto) {
        // Armamos la SQL dependiendo de qué atributo venga “no nulo” en el DTO:
        String sql;
        if (dto.getIdProducto() != 0) {
            sql = "SELECT * FROM Producto WHERE idProducto = ?";
        } else if (dto.getCodigoBarras() != null && !dto.getCodigoBarras().isEmpty()) {
            sql = "SELECT * FROM Producto WHERE codigoBarras = ?";
        } else {
            throw new IllegalArgumentException(
                "Para buscar un producto se requiere idProducto o codigoBarras"
            );
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (dto.getIdProducto() != 0) {
                stmt.setInt(1, dto.getIdProducto());
            } else {
                stmt.setString(1, dto.getCodigoBarras());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProducto(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // no encontrado
    }

    // ───────────────────────────────────────────────────────────────────────
    // Listar todos los productos (sin filtro)
    // ───────────────────────────────────────────────────────────────────────
    @Override
    public List<ProductoDto> listarTodos() {
        List<ProductoDto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Producto";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSetToProducto(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ───────────────────────────────────────────────────────────────────────
    // Insertar un nuevo producto
    // ───────────────────────────────────────────────────────────────────────
    @Override
    public boolean insertar(ProductoDto dto) {
        String sql = "INSERT INTO Producto(nombre, stock, precio, marca, codigoBarras) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dto.getNombre());
            stmt.setInt(2, dto.getStock());
            stmt.setDouble(3, dto.getPrecio());
            stmt.setString(4, dto.getMarca());
            stmt.setString(5, dto.getCodigoBarras());

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ───────────────────────────────────────────────────────────────────────
    // Modificar un producto existente (basado en el idProducto)
    // ───────────────────────────────────────────────────────────────────────
    @Override
    public boolean modificar(ProductoDto dto) {
        String sql = "UPDATE Producto " +
                     "SET nombre = ?, stock = ?, precio = ?, marca = ?, codigoBarras = ? " +
                     "WHERE idProducto = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dto.getNombre());
            stmt.setInt(2, dto.getStock());
            stmt.setDouble(3, dto.getPrecio());
            stmt.setString(4, dto.getMarca());
            stmt.setString(5, dto.getCodigoBarras());
            stmt.setInt(6, dto.getIdProducto());

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ───────────────────────────────────────────────────────────────────────
    // Borrar un producto (por idProducto)
    // ───────────────────────────────────────────────────────────────────────
    @Override
    public boolean borrar(ProductoDto dto) {
        String sql = "DELETE FROM Producto WHERE idProducto = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdProducto());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ───────────────────────────────────────────────────────────────────────
    // Mapea un ResultSet → ProductoDto
    // ───────────────────────────────────────────────────────────────────────
    private ProductoDto mapResultSetToProducto(ResultSet rs) throws SQLException {
        return new ProductoDto(
            rs.getInt("idProducto"),
            rs.getString("nombre"),
            rs.getInt("stock"),
            rs.getDouble("precio"),
            rs.getString("marca"),
            rs.getString("codigoBarras")
        );
    }

    @Override
    public List<ProductoDto> listarPorCriterio(ProductoDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
