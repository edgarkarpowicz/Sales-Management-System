// Archivo: VentaDao.java
// Paquete: dao

package dao;

import dto.VentaDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC (SQLite) del DAO para la tabla Venta.
 * Sigue la misma convención que ProductoDao: cada método usa la Connection
 * proporcionada en el constructor y mapea a/del VentaDto.
 *
 * Esquema de tabla Venta (SQLite):
 *
 * CREATE TABLE IF NOT EXISTS Venta (
 *   idVenta      INTEGER PRIMARY KEY AUTOINCREMENT,
 *   idCliente    INTEGER NOT NULL,
 *   idCajero     INTEGER NOT NULL,
 *   idDescuento  INTEGER,         -- puede ser NULL
 *   idFactura    INTEGER NOT NULL,
 *   noTarjeta    TEXT,            -- opcional
 *   tipo         TEXT NOT NULL,
 *   estado       TEXT NOT NULL
 * );
 */
public class VentaDao {
    private final Connection conn;

    /**
     * @param conn Conexión JDBC ya abierta a la base SQLite donde existe la tabla Venta.
     */
    public VentaDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserta una nueva venta (sin idVenta). Al finalizar, asigna el id autogenerado
     * al campo idVenta del DTO y retorna true si se insertó correctamente.
     *
     * @param dto VentaDto sin idVenta (idVenta == 0).
     * @return true si se insertó 1 fila, false en otro caso.
     * @throws SQLException en caso de error de JDBC/SQLite.
     */
    public boolean insertar(VentaDto dto) throws SQLException {
        conn.setAutoCommit(false);
        String sql = "INSERT INTO Venta " +
                     "(idCliente, idCajero, idDescuento, idFactura, noTarjeta, tipo, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement stmt = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            // 1) idCliente (NOT NULL)
            stmt.setInt(1, dto.getIdCliente());
            // 2) idCajero (NOT NULL)
            stmt.setInt(2, dto.getIdCajero());
            // 3) idDescuento (puede ser NULL)
            if (dto.getIdDescuento() != null) {
                stmt.setInt(3, dto.getIdDescuento());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            // 4) idFactura (NOT NULL)
            stmt.setInt(4, dto.getIdFactura());
            // 5) noTarjeta (puede ser NULL o cadena vacía)
            if (dto.getNoTarjeta() != null && !dto.getNoTarjeta().isEmpty()) {
                stmt.setString(5, dto.getNoTarjeta());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }
            // 6) tipo (NOT NULL)
            stmt.setString(6, dto.getTipo());
            // 7) estado (NOT NULL)
            stmt.setString(7, dto.getEstado());

            int filas = stmt.executeUpdate();
            if (filas != 1) {
                return false;
            }

            // Obtener el idVenta autogenerado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGen = rs.getInt(1);
                    dto.setIdVenta(idGen);
                }
            }
            conn.commit();
            return true;
        }
    }

    /**
     * Busca una venta por su idVenta.
     *
     * @param idVenta identificador de la venta a buscar.
     * @return VentaDto si existe, o null si no se encuentra.
     * @throws SQLException en caso de error de JDBC.
     */
    public VentaDto buscarPorId(int idVenta) throws SQLException {
        String sql = "SELECT idVenta, idCliente, idCajero, idDescuento, idFactura, " +
                     "noTarjeta, tipo, estado FROM Venta WHERE idVenta = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer idDesc = rs.getObject("idDescuento") != null
                                     ? rs.getInt("idDescuento")
                                     : null;
                    String nTarjeta  = rs.getString("noTarjeta");
                    return new VentaDto(
                        rs.getInt("idVenta"),
                        rs.getInt("idCliente"),
                        rs.getInt("idCajero"),
                        idDesc,
                        rs.getInt("idFactura"),
                        nTarjeta,
                        rs.getString("tipo"),
                        rs.getString("estado")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Actualiza una venta existente. Requiere que dto.getIdVenta() > 0.
     *
     * @param dto VentaDto completo (incluyendo idVenta).
     * @return true si se actualizó exactamente 1 fila, false en otro caso.
     * @throws SQLException en caso de error de JDBC.
     */
    public boolean actualizar(VentaDto dto) throws SQLException {
        if (dto.getIdVenta() <= 0) {
            throw new IllegalArgumentException("El idVenta debe ser mayor que cero para actualizar.");
        }

        String sql = "UPDATE Venta SET " +
                     "idCliente = ?, idCajero = ?, idDescuento = ?, idFactura = ?, " +
                     "noTarjeta = ?, tipo = ?, estado = ? " +
                     "WHERE idVenta = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getIdCliente());
            stmt.setInt(2, dto.getIdCajero());
            if (dto.getIdDescuento() != null) {
                stmt.setInt(3, dto.getIdDescuento());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setInt(4, dto.getIdFactura());
            if (dto.getNoTarjeta() != null && !dto.getNoTarjeta().isEmpty()) {
                stmt.setString(5, dto.getNoTarjeta());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }
            stmt.setString(6, dto.getTipo());
            stmt.setString(7, dto.getEstado());
            stmt.setInt(8, dto.getIdVenta());

            int filas = stmt.executeUpdate();
            return filas == 1;
        }
    }

    /**
     * Elimina la venta con el idVenta dado.
     *
     * @param idVenta identificador de la venta a eliminar.
     * @return true si se eliminó exactamente 1 fila, false si no existía.
     * @throws SQLException en caso de error de JDBC.
     */
    public boolean eliminar(int idVenta) throws SQLException {
        String sql = "DELETE FROM Venta WHERE idVenta = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);
            int filas = stmt.executeUpdate();
            return filas == 1;
        }
    }

    /**
     * Lista todas las ventas existentes en la tabla.
     *
     * @return List<VentaDto> con todas las ventas; la lista puede estar vacía si no hay filas.
     * @throws SQLException en caso de error de JDBC.
     */
    public List<VentaDto> listarTodos() throws SQLException {
        List<VentaDto> lista = new ArrayList<>();
        String sql = "SELECT idVenta, idCliente, idCajero, idDescuento, idFactura, " +
                     "noTarjeta, tipo, estado FROM Venta;";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Integer idDesc = rs.getObject("idDescuento") != null
                                 ? rs.getInt("idDescuento")
                                 : null;
                String nTarjeta = rs.getString("noTarjeta");
                VentaDto dto = new VentaDto(
                    rs.getInt("idVenta"),
                    rs.getInt("idCliente"),
                    rs.getInt("idCajero"),
                    idDesc,
                    rs.getInt("idFactura"),
                    nTarjeta,
                    rs.getString("tipo"),
                    rs.getString("estado")
                );
                lista.add(dto);
            }
        }
        return lista;
    }
}
